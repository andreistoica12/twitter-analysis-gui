import os
import pandas as pd
from matplotlib import pyplot as plt
import random
import networkx as nx
import argparse

import matplotlib
# Set the Matplotlib backend to 'Agg' to avoid Qt platform plugin error
# I don't need to interact with the graphs within WSL2, they should be saved to file.
matplotlib.use('Agg')
import matplotlib.patches as mpatches



def get_central_node_tweet_id(central_node_subnetwork):
    central_node_tweet_id = central_node_subnetwork[central_node_subnetwork['reference_id'] == '#']
    
    if len(central_node_tweet_id) != 1:
        raise Exception("There should be exactly one row with '#' in 'reference_id' => the central node")
    
    return central_node_tweet_id['tweet_id'].values[0]



def get_reactions_by_type(reaction_types, central_node_subnetwork):
    reactions = central_node_subnetwork[central_node_subnetwork['reference_type'].isin(reaction_types)]

    return reactions



def parse_offset_string(offset_string):
    total_minutes = 0

    if "d" in offset_string:
        days_part, offset_string = offset_string.split("d")
        total_minutes += int(days_part) * 24 * 60

    if "h" in offset_string:
        hours_part, offset_string = offset_string.split("h")
        total_minutes += int(hours_part) * 60

    if "min" in offset_string:
        minutes_part = offset_string.replace("min", "")
        total_minutes += int(minutes_part)

    if not any(char in offset_string for char in ["d", "h", "min"]):
        raise ValueError("Invalid offset string format. It should contain at least one of 'd', 'h', or 'min'.")


    return pd.Timedelta(minutes=total_minutes)



def row_value_for(column_name, tweet_id, dataset):
    return dataset.loc[dataset['tweet_id'] == tweet_id, column_name].item()



def get_reactions_by_type_by_time_interval(start_of_interval, end_of_interval, reaction_types, central_node_subnetwork):
    reactions_by_type = get_reactions_by_type(reaction_types, central_node_subnetwork)

    central_node_tweet_id = get_central_node_tweet_id(central_node_subnetwork)
    central_node_posting_time = row_value_for('created_at', central_node_tweet_id, central_node_subnetwork)

    offset1 = parse_offset_string(start_of_interval)
    start_time = central_node_posting_time + offset1

    if end_of_interval == "LAST_REACTION":
        end_time = pd.Timestamp.utcnow()
    else:
        offset2 = parse_offset_string(end_of_interval)
        end_time = central_node_posting_time + offset2

    reactions_by_type_by_time_interval = reactions_by_type[(reactions_by_type['created_at'] >= start_time) & 
                                                           (reactions_by_type['created_at'] < end_time)]
                        

    return reactions_by_type_by_time_interval



# function to add value labels - adds the value of y
def add_labels_y_value(x,y):
    """Function that takes the x and y-axis to be passed onto a plot function and generates labels,
    such that on top of each y value, it is displayed centrally.

    Args:
        x (list): list of labels for x-axis of a plot
        y (list): list of values for y-axis of a plot
    """    
    for i in range(len(x)):
        plt.text(x[i], y[i], y[i], ha = 'center', va = 'bottom')



def plot_reaction_counts_barchart(reaction_types, reactions_labels, start_of_interval, end_of_interval, central_node_subnetwork, folder_path):
    
    data = { f'{reactions_labels[reaction_type]}' : len(get_reactions_by_type_by_time_interval(start_of_interval, end_of_interval, [reaction_type], central_node_subnetwork))
            for reaction_type in reaction_types}
    
    keys = list(data.keys())
    values = list(data.values())

    # Create a bar chart of the counts
    plt.bar(keys, values, edgecolor='black')
    plt.xticks(rotation=45)
    plt.subplots_adjust(bottom=0.25)
    # Add labels to the top of each bar
    add_labels_y_value(keys, values)
    plt.xlabel('Reaction type')
    plt.ylabel('Number of reactions')
    plt.title("Reaction Counts per Type", loc="center", pad=10)

    path = os.path.join(folder_path, 'network_graph.png')
    plt.savefig(path)
    plt.close()




def plot_network(reaction_interval, reaction_types, reactions_labels, central_node_subnetwork, folder_path):
    # Split the string based on the "-"
    intervals_boundaries = reaction_interval.split("-")
    start_of_interval = intervals_boundaries[0]
    end_of_interval = intervals_boundaries[1]

    if 'retweeted' not in reaction_types:
        print(f'Started building graph for {", ".join(reactions_labels[reaction_type] for reaction_type in reaction_types)} posted between {reaction_interval}...')

        reactions_by_type_by_time_interval = get_reactions_by_type_by_time_interval(start_of_interval, end_of_interval, reaction_types, central_node_subnetwork)

        print(f'Number of {", ".join(reactions_labels[reaction_type] for reaction_type in reaction_types)} posted between {reaction_interval}: {len(reactions_by_type_by_time_interval)}')
        # Create an empty directed graph
        G = nx.DiGraph()

        central_node = get_central_node_tweet_id(central_node_subnetwork)
        # Add the source tweet as the central node
        G.add_node(central_node)

        # Add the reply tweets as nodes and edges
        reaction_nodes = reactions_by_type_by_time_interval['tweet_id'].to_numpy()
        G.add_nodes_from(reaction_nodes)
        G.add_edges_from(zip(reaction_nodes, [central_node] * len(reaction_nodes)))
        # print(f'Added all {G.number_of_nodes()} nodes. Now grouping the reactions by author_id...')

        # group the original dataframe by 'author_id' and count the number of occurrences
        grouped = reactions_by_type_by_time_interval.groupby('author_id').size()

        # keep only the groups where the count is greater than 1
        grouped = grouped[grouped > 1]
        # print('Grouped reactions where author posted more than 1 reaction.')

        # create a new dataframe with the desired columns and 'author_count'
        df_authors_with_multiple_reactions = reactions_by_type_by_time_interval[['tweet_id', 'author_id']].loc[reactions_by_type_by_time_interval['author_id'].isin(grouped.index)]
        df_authors_with_multiple_reactions['author_count'] = df_authors_with_multiple_reactions['author_id'].apply(lambda x: grouped[x])
        # print('Created helper dataframe.')

        unique_author_counts = df_authors_with_multiple_reactions['author_count'].unique()
        color_dict = {count: "#" + ''.join([random.choice('0123456789ABCDEF') for j in range(6)])
                    for count in unique_author_counts}
        # print('Created node color dictionary.')


        def is_author_with_multiple_reactions(node, df_authors_with_multiple_reactions):
            return node in df_authors_with_multiple_reactions['tweet_id'].values

        def get_author_count(node, df_authors_with_multiple_reactions):
            row = df_authors_with_multiple_reactions.loc[df_authors_with_multiple_reactions['tweet_id'] == node]
            author_count = row['author_count'].iloc[0]

            return author_count

        # print('Adding colors to the node_colors list...')
        node_colors = []
        for node in G.nodes():
            if node == central_node:
                node_colors.append('red')
            else:
                node_colors.append(color_dict[get_author_count(node, df_authors_with_multiple_reactions)] 
                                if is_author_with_multiple_reactions(node, df_authors_with_multiple_reactions) else 'black')
        # print('Added all colors.')


        # Create legend patches for each category
        legend_patches = [
            mpatches.Patch(color='red', label='Original/Source Tweet'),
            mpatches.Patch(color='black', label='Reactions whose authors posted a single reaction to the Source Tweet'),
        ]

        for count, color in color_dict.items():
            legend_patches.append(mpatches.Patch(color=color, label=f'Reactions whose authors posted {count} reactions to the Source Tweet'))



        # Set the node size to 20
        node_size = 20
        # Set the edge color to grey and the opacity to 0.7
        edge_color = 'grey'
        edge_alpha = 0.7
        # print('Creating the layout...')
        # get the spring layout
        pos = nx.spring_layout(G)

        # print('Started drawing the graph...')
        # Draw the graph
        nx.draw(G, pos=pos, with_labels=False, node_size=node_size, node_color=node_colors, edge_color=edge_color, alpha=edge_alpha)


        # Create the legend
        legend = plt.legend(handles=legend_patches, title='Node Categories', fontsize='small')

        # Get the current figure and axes
        fig = plt.gcf()
        ax = plt.gca()

        # Add the legend to the right of the figure
        fig.subplots_adjust(right=0.7)  # Adjust this value as needed
        fig.legend(legend.get_patches(), [patch.get_label() for patch in legend.get_patches()], loc='center left', bbox_to_anchor=(1, 0.5))
        
        # print('Finished drawing. Now saving to file...')
        path = os.path.join(folder_path, f'network_graph.png')
        plt.savefig(path)
        plt.close()
    else:
        plot_reaction_counts_barchart(reaction_types, reactions_labels, start_of_interval, end_of_interval, central_node_subnetwork, folder_path)


    print('Done')


def format_minutes(minutes):
    minutes = int(minutes)

    days = minutes // (60 * 24)
    hours = (minutes % (60 * 24)) // 60
    mins = minutes % 60
    
    formatted_time = ""
    
    if days > 0:
        formatted_time += f"{days}d"
    
    if hours > 0:
        formatted_time += f"{hours}h"
    
    if mins >= 0 or not formatted_time:
        formatted_time += f"{mins}min"
    
    return formatted_time



def reference_id_to_int(reference_id):
    if reference_id == '#':
        return reference_id
    else:
        try:
            return int(reference_id)
        except ValueError:
            return reference_id
        




def main():


    # In order to avoid boilerplate code, I decided to add some command line arguments when running
    # this script, instead of creating a separte, almost identical, script file for each date or modifying in the code.
    # The argument is: --date .
    # This way, if the user wishes to run the script in a terminal window, he/she can specify this
    # argument themselves. The steps to parse the command line argument are the following:
    # 1. Create an argument parser
    parser = argparse.ArgumentParser()

    # 2. Add argument for: date
    parser.add_argument('--startTime', type=str, default='15', help='Start time')
    parser.add_argument('--endTime', type=str, default='45', help='End time')
    parser.add_argument('--combination', type=str, default='0', help='Combination of reactions')


    # 3. Parse the command-line arguments
    args = parser.parse_args()

    startTime = args.startTime
    endTime = args.endTime
    combination = args.combination
    combination = int(combination)

    print(f"Start time: {startTime}, End time: {endTime}, Combination: {combination}")

    networkx_dir_path = os.path.dirname(os.path.abspath(__file__))
    output_dir_path = os.path.join(networkx_dir_path, 'graph')

    # Check if the folder exists
    if not os.path.exists(output_dir_path):
        # If the folder doesn't exist, create it
        os.makedirs(output_dir_path)


    python_dir_path = os.path.dirname(networkx_dir_path)
    input_filename = 'top_original_tweet_subnetwork.csv'
    input_data_path = os.path.join(python_dir_path, 'input-data', input_filename)



    print(f"Reading input data ({input_filename})...")
    central_node_subnetwork = pd.read_csv(input_data_path)
    print(f"Input data read. Number of tweets: {len(central_node_subnetwork)}.")

    print("Applying transformations to input dataframe...")
    central_node_subnetwork.drop_duplicates(subset=['tweet_id'], inplace=True)
    central_node_subnetwork.reset_index(drop=True, inplace=True)
    central_node_subnetwork['reference_id'] = central_node_subnetwork['reference_id'].apply(reference_id_to_int)
    # Convert the 'created_at' column to datetime
    central_node_subnetwork['created_at'] = pd.to_datetime(central_node_subnetwork['created_at'])

    reaction_types_full_list = [['replied_to'],
                                ['quoted'], 
                                ['replied_to', 'quoted'], 
                                ['retweeted'],
                                ['replied_to', 'retweeted'],
                                ['quoted', 'retweeted'], 
                                ['replied_to', 'quoted', 'retweeted']]
    
    reaction_types = reaction_types_full_list[combination]
    
    reactions_labels = {
        'quoted': 'quotes',
        'replied_to': 'replies',
        'retweeted': 'retweets'
    }

    reaction_interval = f"{format_minutes(startTime)}-{format_minutes(endTime)}"

    plot_network(reaction_interval, reaction_types, reactions_labels, central_node_subnetwork, output_dir_path)
    



if __name__ == "__main__":
    main()

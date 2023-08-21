import os
import json
import pandas as pd
import argparse


reaction_labels = {
    'quoted': 'QUOTE',
    'replied_to': 'REPLY',
    'retweeted': 'RETWEET'
}



def string_to_int(reference_id):
    try:
        return int(reference_id)
    except ValueError:
        return reference_id



def create_json_original(original_tweet_id, dataset):

    has_duplicates = dataset[dataset['tweet_id'] == original_tweet_id]['tweet_id'].duplicated().any()
    if has_duplicates:
        raise Exception("Duplicate tweet id. Make sure the tweet id is unique.")
    
    def row_value_for(column_name):
        return dataset.loc[dataset['tweet_id'] == original_tweet_id, column_name].item()

    original = {}
    original["original_tweet_id"] = original_tweet_id
    unfiltered_text = row_value_for('text')
    original["original_text"] = unfiltered_text.replace('\n', '')
    original["ORIGINAL_created_at"] = row_value_for('created_at')
    original["ORIGINAL_location"] = row_value_for('location')
    original["post_id"] = f"post_{original_tweet_id}"
    original["original_author_id"] = f"ORIGINAL_TWEET_author_{row_value_for('author_id')}"
    original["ag_o_name"] = row_value_for('name')
    original["original_author_props_id"] = f"original_author_props_{row_value_for('author_id')}"
    original["ORIGINAL_credible"] = row_value_for('credible')
    original["ORIGINAL_username"] = row_value_for('username')
    original["ORIGINAL_verified"] = row_value_for('verified')
    original["ORIGINAL_followers_count"] = row_value_for('followers_count')
    original["ORIGINAL_following_count"] = row_value_for('following_count')

    # Convert all values to strings
    for key in original:
        original[key] = str(original[key])

    return original



def row_value_for(column_name, original_tweet_id, dataset):
    return dataset.loc[dataset['tweet_id'] == original_tweet_id, column_name].item()



# def parse_offset_string(offset_string):
#     if offset_string.endswith("min"):  # Offset in minutes
#         offset_minutes = int(offset_string[:-3])  # Extract the numeric part of the string (excluding the last 3 characters)
#         return pd.Timedelta(minutes=offset_minutes)
#     elif offset_string.endswith("h"):  # Offset in hours
#         offset_hours = int(offset_string[:-1])  # Extract the numeric part of the string (excluding the last character)
#         return pd.Timedelta(hours=offset_hours)
#     else:
#         raise ValueError("Invalid offset string. It should end with 'min' for minutes or h for hours.")
    


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




def get_reactions_to_original_tweet_in_interval_by_type(original_tweet_id, start_of_interval, end_of_interval, reaction_types, dataset):
    
    original_posting_time = row_value_for('created_at', original_tweet_id, dataset)

    offset1 = parse_offset_string(start_of_interval)
    start_time = original_posting_time + offset1

    if end_of_interval == "LAST_REACTION":
        end_time = pd.Timestamp.utcnow()
    else:
        offset2 = parse_offset_string(end_of_interval)
        end_time = original_posting_time + offset2

    reactions = dataset[(dataset['reference_id'] == original_tweet_id) &
                        (dataset['reference_type'].isin(reaction_types)) &
                        (dataset['reference_type'] != '#') &
                        (dataset['created_at'] >= start_time) & 
                        (dataset['created_at'] < end_time)]

    return reactions


reaction_labels = {
    'quoted': 'quotes',
    'replied_to': 'replies',
    'retweeted': 'retweets'
}



def springify_reaction_types(reaction_types):
    return "_".join(reaction_labels[reaction_type] for reaction_type in reaction_types)



def create_json_group_of_reactions(original_tweet_id, start_of_interval, end_of_interval, reaction_types, dataset, total_nr_of_reactions):
    has_duplicates = dataset[dataset['tweet_id'] == original_tweet_id]['tweet_id'].duplicated().any()
    if has_duplicates:
        raise Exception("Duplicate tweet id. Make sure the tweet id is unique.")
    
    reactions = get_reactions_to_original_tweet_in_interval_by_type(original_tweet_id, start_of_interval, end_of_interval, reaction_types, dataset)
    nr_of_unique_author_ids = reactions['author_id'].nunique()

    
    group_of_reactions = {}
    group_of_reactions["react_id"] = f"reacts_for_{start_of_interval}_{end_of_interval}"
    group_of_reactions["reaction_group_of_authors_id"] = f"{springify_reaction_types(reaction_types)}_authors_for_{start_of_interval}_{end_of_interval}"
    group_of_reactions["nr_of_distinct_authors"] = nr_of_unique_author_ids
    group_of_reactions["reaction_group_of_tweets_id"] = f"{springify_reaction_types(reaction_types)}_for_{start_of_interval}_{end_of_interval}"
    group_of_reactions["time_interval"] = f"{start_of_interval} - {end_of_interval}"
    group_of_reactions["nr_of_reactions"] = f"{len(reactions)} out of total {total_nr_of_reactions}"
    group_of_reactions["percentage_out_of_total_reactions"] = f"{round(len(reactions) / total_nr_of_reactions * 100, 2)}%"
    group_of_reactions["nr_of_replies"] = reactions['reference_type'].value_counts().get('replied_to', 0)
    group_of_reactions["nr_of_quotes"] = reactions['reference_type'].value_counts().get('quoted', 0)
    group_of_reactions["nr_of_retweets"] = reactions['reference_type'].value_counts().get('retweeted', 0)

    # Convert all values to strings
    for key in group_of_reactions:
        group_of_reactions[key] = str(group_of_reactions[key])

    return group_of_reactions



def create_json_data(original_tweet_id, reaction_interval, reaction_types, dataset, total_nr_of_reactions):
    # Split the string based on the "-"
    intervals_boundaries = reaction_interval.split("-")
    start_of_interval = intervals_boundaries[0]
    end_of_interval = intervals_boundaries[1]

    data = {}
    data["original"] = create_json_original(original_tweet_id, dataset)
    data["group_of_reactions"] = create_json_group_of_reactions(original_tweet_id, start_of_interval, end_of_interval, reaction_types, dataset, total_nr_of_reactions)

    return data



def create_all_json_data(original_tweet_id, reaction_intervals, reaction_types, dataset, dirpath):
    if not isinstance(reaction_intervals, dict):
        raise TypeError("The reaction intervals have to be written in a dictionary.")
    
    total_nr_of_reactions = len(dataset[(dataset['reference_id'] == original_tweet_id) &
                               (dataset['reference_type'] != '#')])
    
    for interval in reaction_intervals.values():
        data = create_json_data(original_tweet_id, interval, reaction_types, dataset, total_nr_of_reactions)

        path = os.path.join(dirpath, f"data2.json")

        # Write the dictionary to a JSON file
        with open(path, "w") as json_file:
            json.dump(data, json_file, indent=4)



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
    parser.add_argument('--combination', type=int, default=1, help='Combination of reactions')


    # 3. Parse the command-line arguments
    args = parser.parse_args()

    startTime = args.startTime
    endTime = args.endTime
    combination = args.combination


    print(f"Start time: {startTime}, End time: {endTime}, Combination: {combination}")
    
    model2_dir_path = os.path.dirname(os.path.abspath(__file__))
    python_dir_path = os.path.dirname(model2_dir_path)
    # input_filename = 'covaxxy_merged_test.csv'
    # input_filename = 'covaxxy_merged_25_days.csv'
    input_filename = 'top_original_tweet_subnetwork.csv'
    input_data_path = os.path.join(python_dir_path, 'input-data', input_filename)


    reaction_types_full_list = [['replied_to'],
                                ['quoted'], 
                                ['replied_to', 'quoted'], 
                                ['replied_to', 'retweeted'],
                                ['quoted', 'retweeted'], 
                                ['replied_to', 'quoted', 'retweeted']]
    
    reaction_types = reaction_types_full_list[combination]


    tweet_id_most_reactions_subnetwork = pd.read_csv(input_data_path)
    tweet_id_most_reactions_subnetwork['reference_id'] = tweet_id_most_reactions_subnetwork['reference_id'].apply(string_to_int)
    # Convert the 'created_at' column to datetime
    tweet_id_most_reactions_subnetwork['created_at'] = pd.to_datetime(tweet_id_most_reactions_subnetwork['created_at'])
    
    subnetwork_by_type = tweet_id_most_reactions_subnetwork[tweet_id_most_reactions_subnetwork['reference_type'].isin(reaction_types) | (tweet_id_most_reactions_subnetwork['reference_type'] == '#')]

    original_tweet = subnetwork_by_type[subnetwork_by_type['reference_id'] == '#']
    # Check if there is exactly one row
    if len(original_tweet) != 1:
        raise ValueError("Expected exactly one row with 'reference_id' equal to '#' => the original tweet in the subnetwork!")
    # Get the value of the 'tweet_id' column from the single row
    tweet_id_most_reactions = original_tweet['tweet_id'].values[0]

    reaction_intervals = {
        0: f"{format_minutes(startTime)}-{format_minutes(endTime)}",
    }

    print("Creating the output JSON files...")
    output_dirpath = os.path.join(model2_dir_path, 'data')

    create_all_json_data(tweet_id_most_reactions, reaction_intervals, reaction_types, subnetwork_by_type, output_dirpath)

    print("Output files created successfully.")

    



if __name__ == "__main__":
    main()

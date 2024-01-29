import os
import subprocess
from flask import Flask, send_file, request
from flask_cors import CORS

app = Flask(__name__)
CORS(app)  # Allow CORS for all routes

@app.route('/api/networkx/graph', methods=['GET'])
def generate_graph():
    start_time = request.args.get('startTime')
    end_time = request.args.get('endTime')
    combination = request.args.get('combination')

    # # Construct the command to activate the virtual environment and run the script
    # script_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', 'flask-app', 'python', 'networkx'))

    # Construct the path to the script folder (relative to the Dockerfile)
    script_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'python', 'networkx'))

    script_path = os.path.join(script_folder, 'longitudinal_subnetworks.py')
    
    command = f'python {script_path} --startTime={start_time} --endTime={end_time} --combination={combination}'

    print(command)

    # Run the command using subprocess
    subprocess.run(command, shell=True)

    # Construct the path to the generated PNG file
    generated_png_path = os.path.abspath(os.path.join(script_folder, 'graph', 'network_graph.png'))

    return send_file(generated_png_path, mimetype='image/png')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)

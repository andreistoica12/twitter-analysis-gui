# Use an official Python image as a parent image
FROM python:3.10

# Set the working directory in the container
WORKDIR /app

# Copy all files (except venv/) to the container
COPY . /app
RUN rm -rf venv/

# Create and activate the virtual environment
RUN python -m venv venv
ENV PATH="/app/venv/bin:${PATH}"
RUN /bin/bash -c "source venv/bin/activate"

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Expose the port on which the Flask app will run
EXPOSE 5000

# Specify the command to run on container startup
CMD ["python", "app.py"]

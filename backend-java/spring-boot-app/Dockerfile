# Use an official Maven image as a parent image
FROM maven:3.8-jdk-11

# Set the working directory in the container
WORKDIR /app

# Install python3-venv
RUN apt-get update && \
    apt-get install -y python3-venv && \
    apt-get install -y graphviz

# Copy the project files into the container at /app
COPY . /app

# Add the provconvert directory to the PATH
ENV PATH="/app/toolbox/target/appassembler/bin:${PATH}"

# Set execute permissions for provconvert
RUN chmod +x /app/toolbox/target/appassembler/bin/provconvert

# Build the application using Maven
RUN mvn clean install

# Specify the command to run on container startup
CMD ["mvn", "spring-boot:run"]

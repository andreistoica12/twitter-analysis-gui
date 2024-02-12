# Twitter Analysis GUI

Proof-of-concept graphical user interface which integrates multiple social network analysis modules: provenance, network graphs and distributions of opinion changes within the Twitter social platform.

## Table of Contents

- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Technology stack](#technology-stack)
- [Usage](#usage)
- [License](#license)

## Introduction

The project is intended to be used as a standalone interative pipeline for visualising social network data. It offers a variety of visualiation types, making use of custom-defined features.

First, provenance can be explained as the documented history of the origin, movement and evolution of data, along with its associated entities. It captures the lineage of data, offering insights into the interactions with various agents and processes. Due to the rising concerns about data integrity in the current technological era, transparency and reproducibility, the role of provenance cannot be overstated. The author defined 3 provenance models specifically tilored to Twitter interactions, one of which is integrated in this release, in order to provide a different approach to analysing this fast-paced envionment. The model was picked to provide information about the subnetwork of reactions of a central node. It is essential to acknowledge that, due to the computationally expensive nature of processing large datasets, such as the one at our disposal, our design choice was to select the tweet with the biggest number of reactions from a pre-existing Twitter dataset, playing the role of the original tweet. Nonetheless, users are entitled to choose the time interval, after the moment the original tweet was released, when a set of reactions were posted. Another filter which allows flexibility is the choice of the reaction type(s) to be taken into
account. Any combination of replies, quotes and retweets, respectively, can be selected, hence
generating a subnetwork tailored to each user’s needs. Note that provenance information can also be represented in text (.provn file extension).

Then, in an attempt to offer an alternative perspective view on the data, the proposed implementation includes the option to construct network graphs, using the same filters as for the provenance visualizations.

Lastly, on a different page within the graphical interface, users can depict distributions of opinion changes within Twitter interactions. The user input is the textual reaction types(s) to be included in the analysis, i.e. any combination of replies and/or quotes. The author implemented an algorithm to detect opinion changes for textual tweets, based on the sentiment scores of their texts. More explicitly, for each Twitter interaction between a user and a certain tweet, i.e. the set of textual reactions to a tweet posted by the same author over time, their sentiment scores had been pre-computed, using the SentiStrength Python module. The opinion change is defined as the biggest difference in sentiment scores within a certain set of reactions to a tweet. The bigger the difference, the bigger the opinion change. This way, users are able to detect whether the social media environment is a factor of influence in people's opinions and if the environment is an optimal means of spreading (mis)information.

The pipeline may be of use to both researchers who appreciate alternative views on data or practitioners who would like to develop machine-learning algorithms to, for instance, predict user behaviour within social media networks or classify tweets based on their documented lineage.

This project is part of the author's Master's Thesis in Computing Science at the Unviversity of Groningen (**[MSc Thesis - Andrei Stoica](https://drive.google.com/file/d/1ccP7dii6qer1SMckYH1lcJBKOZKcyoOX/view?usp=sharing)**). If you want to read the report, please send me an access request.

## Prerequisites

The project relies on Docker containers for a seamless and portable deployment. In order to successfully run the project, you need to have the following installed on your local machine:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

Note: Docker Desktop includes Docker Compose, along with Docker Engine and Docker CLI which are Compose prerequisites.

## Installation

If you wish to have access to the source code of the project on your local machine, you can clone the repository. Otherwise, downloading the `docker-comppose.yml` file in a directory of your choice will suffice.

Ensure Docker Engine is running on your system. Open a command line interface and navigate to the location of the `docker-compose.yml` file. If you chose to clone the repository, this location is the root of the project. Run the following command:

```bash
docker-compose up
```

After pulling 3 images from Docker Hub, each associated with the main components of the application (the Spring Boot and Flask backend components and the React frontent), the application can be accessed at **[localhost:3000](http://localhost:3000/)**, where the React entry point runs.

## Technology stack

### Spring Boot Backend

- **Java:**  Programming language used for creating the provenance models, binding classes and visualizations. (thorough explanations of the template and binding mechanism can be found in the full report of the thesis: **[MSc Thesis - Andrei Stoica](https://drive.google.com/file/d/1ccP7dii6qer1SMckYH1lcJBKOZKcyoOX/view?usp=sharing)**).
- **Python:** Programming language used for gathering the necessary inforamtion from the dataset to be passes onto the template mechanism.
- **Spring Boot:** Framework for building Java-based enterprise applications.

### Flask Backend

- **Python:** Programming language for the backend.
- **Flask:** Micro web framework for Python.
- **NetworkX:** Library for creating, analyzing, and visualizing complex networks.

### React Frontend

- **JavaScript (ES6+):** Programming language for the frontend.
- **React:** JavaScript library for building user interfaces.
- **Node.js:** JavaScript runtime for building server-side applications.
- **npm (Node Package Manager):** Package manager for JavaScript and Node.js.

## Usage

You can freely interact with the visual components of the graphical interface, creating a custom configuration as described in the **Introduction**. After each configuration, a GET request to the corresponding backend server will be made and the resuling resource will be created and returned and displayed.

## License

This project is licensed under the [MIT License](LICENSE).


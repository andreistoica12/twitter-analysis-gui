package com.rug.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class NetworkXService {

    public String executePythonScriptAndGetGraphFilePath(String startTime, String endTime, String combination) throws IOException, InterruptedException {
        String springBootAppDir = System.getProperty("user.dir");

        String networkxFolderPath = springBootAppDir + File.separator + 
                                    "src" + File.separator + 
                                    "main" + File.separator + 
                                    "python" + File.separator +
                                    "networkx";

        String pythonScriptPath = networkxFolderPath + File.separator + "longitudinal_subnetworks.py";


        ProcessBuilder processBuilder = new ProcessBuilder(
            "python",
            pythonScriptPath,
            "--startTime=" + startTime,
            "--endTime=" + endTime,
            "--combination=" + combination
        );

        processBuilder.directory(new File(springBootAppDir)); // Set the working directory
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            // Python script executed successfully, now retrieve graph file path and return
            String graphFilePath = networkxFolderPath + File.separator + "graph" + File.separator + "network_graph.png";
            return graphFilePath;
        } else {
            throw new RuntimeException("Python script execution failed");
        }
    }
}

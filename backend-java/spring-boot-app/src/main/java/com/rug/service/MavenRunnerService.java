package com.rug.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;




@Service
public class MavenRunnerService {

    public String runMavenProjectAndGetSvgFiles(String startTime, String endTime) throws IOException, InterruptedException {
        String springBootAppDir = System.getProperty("user.dir");

        ProcessBuilder processBuilder = new ProcessBuilder(
            "mvn",
            "clean",
            "install",
            "-DstartTime=" + startTime,
            "-DendTime=" + endTime
        );
        processBuilder.directory(new File(springBootAppDir)); // Set the working directory
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            // Maven project ran successfully, now retrieve .svg file and do further processing in the Controller
            String svgFilePath = springBootAppDir + File.separator + "provenance-graphs" + File.separator + "model2" + File.separator + "doc2.svg";
            return svgFilePath;
        } else {
            throw new RuntimeException("Maven project execution failed");
        }
    }

}


package com.rug.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.rug.service.NetworkXService;



@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class NetworkXController {

    private final NetworkXService networkXService;

    @Autowired
    public NetworkXController(NetworkXService networkXService) {
        this.networkXService = networkXService;
    }

    @GetMapping("/networkx/graph")
    public ResponseEntity<Resource> generateGraph(
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam String combination
    ) {
        try {
            // Call the service to execute the Python script and get the graph file path
            String graphFilePath = networkXService.executePythonScriptAndGetGraphFilePath(startTime, endTime, combination);

            // Load the generated graph file as a Resource
            Resource resource = new FileSystemResource(graphFilePath);

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            // Return the graph content as a ResponseEntity
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}

package com.rug.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.rug.service.MavenRunnerService;



@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class MavenRunnerController {

    private final MavenRunnerService mavenService;

    @Autowired
    public MavenRunnerController(MavenRunnerService mavenService) {
        this.mavenService = mavenService;
    }

    @GetMapping("/provenance/model2/graph")
    public ResponseEntity<Resource> runMavenAndReturnMessage(
        @RequestParam String startTime,
        @RequestParam String endTime
    ) {
        try {
            // Call the service to run Maven and generate the SVG file path
            String svgFilePath = mavenService.runMavenProjectAndGetSvgFiles(startTime, endTime);

            // Load the generated SVG file as a Resource
            Resource resource = new FileSystemResource(svgFilePath);

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("image/svg+xml")); // Use "image/svg+xml" as the media type

            // Return the SVG content as a ResponseEntity
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}

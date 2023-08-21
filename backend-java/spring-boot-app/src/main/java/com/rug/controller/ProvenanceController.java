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

import com.rug.service.ProvenanceService;



@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class ProvenanceController {

    private final ProvenanceService provenanceService;

    @Autowired
    public ProvenanceController(ProvenanceService provenanceService) {
        this.provenanceService = provenanceService;
    }

    @GetMapping("/provenance/model2/graph")
    public ResponseEntity<Resource> runMavenAndReturnFilePath(
        @RequestParam String startTime,
        @RequestParam String endTime,
        @RequestParam String combination
    ) {
        try {
            // Call the service to run Maven and generate the SVG file path
            String svgFilePath = provenanceService.runMavenProjectAndGetSvgFilePath(startTime, endTime, combination);

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

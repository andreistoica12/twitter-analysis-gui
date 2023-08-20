package com.rug;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class AdjustSVGSize {


    public static void adjustFileSize(String SVGPathString) {
        // Convert the path string to a Path object
        Path path = Paths.get(SVGPathString);
        // Get the file name from the Path object
        String fileName = path.getFileName().toString();
 

        int height = (fileName.equals("doc1.svg")) ? 6000 : 1700;
        int width = (fileName.equals("doc1.svg")) ? 3000 : 500;


        try {

            // Load the SVG file
            File inputSVGFile = new File(SVGPathString);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document inputSVGDocument = builder.parse(inputSVGFile);

            // Get the root <svg> element
            Element SVGElement = inputSVGDocument.getDocumentElement();
            
            // Modify the viewBox attribute values
            SVGElement.setAttribute("viewBox", String.format("0 0 %s %s", height, width)); 

            // Save the modified SVG file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(SVGElement), new StreamResult(new File(SVGPathString)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void adjustFilesSizesFromFolderRecursively(File folder) {
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // If the file is a directory, recursively list its content
                    adjustFilesSizesFromFolderRecursively(file);
                } else {
                    // Get the absolute path to the file and adjust the size
                    adjustFileSize(file.getAbsolutePath());
                }
            }
        } else {
            System.out.println("No files found in the folder.");
        }
    }


    public static void main(String[] args) {

        if (args.length!=1) throw new UnsupportedOperationException("main to be called with filename");

        String pathString = args[0];
        File file = new File(pathString);


        if (!file.exists()) {
            System.out.println("The path does not exist.");
        } else if (file.isFile()) {
            adjustFileSize(pathString);
        } else if (file.isDirectory()) {
            adjustFilesSizesFromFolderRecursively(file);
        } else {
            System.out.println("The path is neither a file path nor a folder path.");
        }
    }
}

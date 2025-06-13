package project.bankrut.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.File;
import java.io.IOException;

public abstract class JsonBaseService {
    protected final ObjectMapper objectMapper;
    protected final String fileName;
    
    protected JsonBaseService(String fileName) {
        this.objectMapper = new ObjectMapper();
        this.fileName = fileName;
        initializeFile();
    }
    
    private void initializeFile() {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                ArrayNode emptyArray = objectMapper.createArrayNode();
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, emptyArray);
                System.out.println("File dibuat: " + fileName);
            } catch (IOException e) {
                System.err.println("Error membuat file " + fileName + ": " + e.getMessage());
            }
        }
    }
    
    protected ArrayNode loadArray() {
        try {
            File file = new File(fileName);
            if (file.exists() && file.length() > 0) {
                return (ArrayNode) objectMapper.readTree(file);
            }
        } catch (IOException e) {
            System.err.println("Error loading " + fileName + ": " + e.getMessage());
        }
        return objectMapper.createArrayNode();
    }
    

    protected void saveArray(ArrayNode array) throws IOException {
        File file = new File(fileName);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, array);
    }
}
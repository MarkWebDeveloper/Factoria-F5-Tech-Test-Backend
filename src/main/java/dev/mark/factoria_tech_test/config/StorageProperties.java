package dev.mark.factoria_tech_test.config;

import org.springframework.stereotype.Component;

@Component
public class StorageProperties {
    String location = "src/main/resources/static/imgs/";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
}
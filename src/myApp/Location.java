package myApp;

import java.util.HashMap;
import java.util.List;

public class Location {
    private String room;
    private String name;
    private String description;
    private HashMap<String, String> direction;
    
    public Location(String room, String name, String description, HashMap<String, String> direction) {
        this.room = room;
        this.name = name;
        this.description = description;
        this.direction = direction;
    }
    
    public String getRoom() {
        return room;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public HashMap<String, String> getDirection() {
        return direction;
    }



    @Override
    public String toString() {
        return "Location [room=" + room + ", name=" + name + ", description=" + description + ", direction=" + direction
                + "]";
    }

    
}

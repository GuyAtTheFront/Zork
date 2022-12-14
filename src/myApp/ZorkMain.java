package myApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

import javax.management.Descriptor;

public class ZorkMain {

    public static List<Location> locations = new LinkedList<>();
    public static String currentLocation = "";

    public static void main(String[] args) {
        
        String fname = "location.txt";

        try {
        BufferedReader br = Files.newBufferedReader(Paths.get(fname));
        
        String contents = "";
        String room = null;
        String name = null;
        String description = null;
        HashMap<String, String> direction = new HashMap<>();

        while((contents = br.readLine()) != null) {
            
            // if(contents.isEmpty()) {
            //     continue;
            // }

            String words[] = contents.split(":");
            
            switch (words[0]) {
                case "":
                    Location location = new Location(room, name, description, direction);
                    locations.add(location);
                    room = null; name = null; description = null; direction =  new HashMap<>();   
                    break;
                case "room":
                    room = words[1];
                    break;
                case "name":
                    name = words[1];
                    break;
                case "description":
                    description = words[1].replaceAll("<break>", "\n");
                    break;
                case "direction":
                    String[] word = words[1].trim().split(" ");
                    //System.out.println(word[0] + "||" +  word[1]);
                    direction.put(word[0], word[1]);
                    break;
                case "start":
                    currentLocation = words[1];
                    break;
                default:
            }
        } 
        //System.out.println(locations.get(0).getRoom());
        } catch(IOException e) {}

        // finished loading data
        // starting console script

        // print current location (room - computer // name - printed)
        Location currentLocationObject = getLocation(currentLocation);
        System.out.println(currentLocationObject.getName());
        // print current description
        System.out.println(currentLocationObject.getDescription());
        System.out.println("");

        Scanner scanner = new Scanner(System.in);
        while(true) {
            String input = scanner.nextLine();
            if (!(currentLocationObject.getDirection().keySet().contains(input.toLowerCase()))) {
                System.out.printf("Cannot go %s\n", input);
                continue;
            }
            String nextRoom = currentLocationObject.getDirection().get(input.trim().toLowerCase());
            System.out.println(nextRoom);
            
            Location nextLocationObject = getLocation(nextRoom);
            System.out.println(nextLocationObject.getName());
            System.out.println(nextLocationObject.getDescription());
            // System.out.println("");
            // System.out.println("okaygeee");
            // System.out.println(currentLocation);
        // System.out.println(input);

        // >> wait for user <--- scanner
        // read user input <--- scanner
        // do things based on user input <--- switch <--??
        }
    }

    public static Location getLocation(String room) {
        Location loc = null;
        for (Location l : locations) { 
            if(l.getRoom().equals(room)) {
                loc = l;
                break;
            }
        }
        return loc;
    }
}
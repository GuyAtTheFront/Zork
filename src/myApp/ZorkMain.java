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

import org.ietf.jgss.Oid;

public class ZorkMain {

    public static List<Location> locations = new LinkedList<>();
    public static String playerLocation = "";
    public static Location currentLocation = null;
    public static HashMap<String, String> locationDirections = null;
    public static String input = "";

    public static void main(String[] args) {
        
        String fname = "location.txt";

        loadData(fname);

        // Game Starting Text
        currentLocation = getLocation(playerLocation);
        locationDirections = currentLocation.getDirection();

        printHelper(currentLocation.getName(), currentLocation.getDescription());
        // print current location (room - computer // name - printed)
        
        try(Scanner scanner = new Scanner(System.in)) {;
            while(true) {
                
                input = scanner.nextLine();
                String splitInput[] = input.split(" ");

                // User hits enter, or types in spaces --> ignore
                if (input.isBlank()) continue;

                switch(splitInput.length) {
                    case 1:
                        oneWordEvents(splitInput[0]);
                        break;
                    case 2:
                        twoWordEvents(splitInput[0], splitInput[1]);
                        break;
                    default:
                        printErrorHelper(input);
                        break;
                }
            } 
        } catch (Exception e) {e.getMessage();}
    }

    private static void loadData(String fname) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(fname));) {
            /* Black cat, white cat,
             * Can catch mouse cat is good cat
             * Use the BufferedReader you're most comfortabled with
             * How you process the string from the .readLine method is most important
             * Can work can already, dont think too hard */

            String contents = "";
            String room = null;
            String name = null;
            String description = null;
            HashMap<String, String> direction = new HashMap<>();
    
            while((contents = br.readLine()) != null) {
    
                String words[] = contents.split(":");
                
                switch (words[0]) {
                    case "":
                        // An empty line in file signifies the end of a location block
                        // Create a location instance with values from previous lines
                        Location location = new Location(room, name, description, direction);
                        // Add location instance to list
                        locations.add(location);
                        // Reset variable used to default value
                        room = null; name = null; description = null; direction =  new HashMap<>();   
                        break;
                    case "room":
                        room = words[1].trim();
                        break;
                    case "name":
                        name = words[1].trim();
                        break;
                    case "description":
                        description = words[1].trim().replaceAll("<break>", "\n");
                        break;
                    case "direction":
                        String[] word = words[1].trim().split(" ");
                        direction.put(word[0], word[1]);
                        break;
                    case "start":
                        playerLocation = words[1].trim();
                        break;
                    default:
                }
            } 
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Location getLocation(String room) {
        Location loc = null;
        for (Location l : locations) { 
            if(l.getRoom().equals(room)) {
                loc = l;
                break;
            }
        }
        return loc;
    }

    private static void printHelper(String location, String description) {
        System.out.println(location);
        System.out.println(description);
        System.out.println("");
    }

    private static void printErrorHelper(String input) {
        System.out.printf("I cannot understand the command: %s\n\n", input);
    }

    private static void oneWordEvents(String w1) {

        if(w1.trim().equalsIgnoreCase("quit")){
            System.out.println("Goodbye");
            return;
        }

        if(w1.trim().equalsIgnoreCase("peek")){
            printHelper(currentLocation.getName(), currentLocation.getDescription());
            return;
        }

        if (!locationDirections.keySet().contains(w1.trim().toLowerCase())) {
            printErrorHelper(input);
            return;
        }

        if(locationDirections.keySet().contains(w1.trim().toLowerCase())) {
            String nextRoom = locationDirections.get(w1.trim().toLowerCase());
            Location nextLocation = getLocation(nextRoom);
            printHelper(nextLocation.getName(), nextLocation.getDescription());
            return;
        }
        System.out.println("If you see this, code is broken");
        return;
    }

    private static void twoWordEvents(String w1, String w2) {
        
        // First word is not "go"
        if (!w1.trim().equalsIgnoreCase("go")) {
            printErrorHelper(input);
            return;
        }

        // First word is "go"" --> check second word is valid
        if (w1.trim().equalsIgnoreCase("go")) {
            // second word is valid --> set variables
            if (locationDirections.keySet().contains(w2.toLowerCase())) {
                playerLocation = locationDirections.get(w2);
                currentLocation = getLocation(playerLocation);
                locationDirections = currentLocation.getDirection();
                printHelper(currentLocation.getName(), currentLocation.getDescription());
                return;
            } else { 
                printErrorHelper(input);
                return;
            }
        }
    }
}
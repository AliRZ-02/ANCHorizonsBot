package com.AliRZ;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageDownload {
    public static void main()throws Exception{
        /*  Purpose: This process, as named, downloads the fish, bug and villager images to one's computer
            - This process is only run once, when first setting up this program to ensure all of the required images are downloaded.
            - The Images used in the tweets were modified from the images available from the database used. I downloaded them at the beginning of the program using this method.
            - The Villager Images used come directly from the Database while Critter images used in the tweets were modified on an external app with information from the database
        */
        String [] imageDownload = {"Fish Images","Bugs Images"};
        /*
            - The following two for loops download the Images of each critter in the game (80 each for Fish and Bugs) by visiting the ACNHAPI database
            - The 'J' variable represents the type of critter (0 for Fish and 1 for Bugs) while the 'I' variable represents the Critter ID between 1 & 80
            - A similar process occurs in the last for loop, where the villager images are downloaded
         */
        /*
        for (int j =0; j<2; j++) {
            for(int i=1; i<81;i++){
                // The following try-catch block with the code for downloading of images from an external URL was modified from 'Alex's' response on this Stack Overflow thread: https://stackoverflow.com/questions/5882005/how-to-download-image-from-any-web-page-in-java
                try(InputStream inputStream = new URL("https://acnhapi.com/v1/images/"+imageDownload[j].substring(0,imageDownload[j].indexOf("I")).toLowerCase()+"/"+i).openStream()){
                    Files.copy(inputStream, Paths.get("C:\\AnyImageDownloadLocation")); // Choose the Filepath for the folder in which you want to download the images
                    System.out.println("Successfully downloaded "+imageDownload[j].substring(0, imageDownload[j].indexOf("I"))+" # "+i);
                }catch(MalformedURLException e){
                    System.out.println("Error. File not found!");
                }
            }
        }
        for (int i =1; i<392; i++){
            // The following try-catch block with the code for downloading of images from an external URL was modified from 'Alex's' response on this Stack Overflow thread: https://stackoverflow.com/questions/5882005/how-to-download-image-from-any-web-page-in-java
            try(InputStream inputStream = new URL("https://acnhapi.com/v1/images/villagers/"+i).openStream()){
                Files.copy(inputStream, Paths.get("C:\\AnyImageDownloadLocation"));
                System.out.println("Successfully downloaded Villager # "+i);
            }catch(MalformedURLException e){
                System.out.println("Error. File not found!");
            }
        }
        */
        for (int i =1; i<41; i++){
            // The following try-catch block with the code for downloading of images from an external URL was modified from 'Alex's' response on this Stack Overflow thread: https://stackoverflow.com/questions/5882005/how-to-download-image-from-any-web-page-in-java
            try(InputStream inputStream = new URL("https://acnhapi.com/v1/images/sea/"+i).openStream()){
                Files.copy(inputStream, Paths.get("C:\\AnyImageDownloadLocation"));
                System.out.println("Successfully downloaded Sea Creature # "+i);
            }catch(MalformedURLException e){
                System.out.println("Error. File not found!");
            }
        }
    }
}

/*
Name: Ali Raza Zaidi
Project: ACNHorizonsBot: A Twitter Bot focused on delivering Tweets about Villager birthdays and important information with regards to critters
Github Repository: https://github.com/AliRZ-02/ANCHorizonsBot
Creation Date: June 2020
LICENSE: This project is licensed through the GNU General Public License v3.0
Last Modified: July 12, 2020
Created using Java SE 14, Twitter4j 4.07 & GSON 2.8.6
*/

package com.AliRZ;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import static java.util.Arrays.binarySearch;

public class Main {
    public static void main(String[] args) throws Exception{
        // The main function is used as a calling ground for the required methods. The critter tweets are sent first, followed by the villager tweets and then finally a verification method
        // A calendar is created here which will be used throughout many different methods
        Calendar botCalendar = Calendar.getInstance();
        int [] currentDate = {(botCalendar.get(Calendar.YEAR)),(botCalendar.get(Calendar.MONTH)),(botCalendar.get(Calendar.DAY_OF_MONTH)), (botCalendar.get(Calendar.HOUR_OF_DAY)), (botCalendar.get(Calendar.MINUTE)), (botCalendar.get(Calendar.SECOND))};
        if ((currentDate[2] == 1) || (currentDate[2] == 22)){
            creatureTweetProcessing(currentDate, botCalendar);
            seaCreatureTweetProcessing(currentDate);
        }
        villagerTweetProcessing(botCalendar, currentDate);
        tweetVerification(currentDate);
    }
    public static void creatureTweetProcessing(int [] currentDate, Calendar botCalendar) throws Exception{
        // The following variables and array have been created to aid in the data gathering process
        String [] creatureType = {"fish","bugs"}; // This array allows for the correct URL to be used when gathering data for either the bugs or the fish
        String [] creatureIntention = {"Joining us ","Leaving us after "}; // This arrays allows for the tweet to be generated based on whether a critter is in an incoming or outgoing month
        String [][][] creatureCharacteristics = new String [2][80][8]; // The data gathered from the API is automatically parsed into an array based on the characteristics of the critter
        String [][][] creatureTweets = new String[2][80][2]; // Tweets are stored in this array
        int [][][][] creatureMonthsAvailability = new int [2][80][2][2];
        int [][][][] creatureMonthsAvailabilityExceptional = new int [2][80][2][2];
        int tweetCounter =0;

        for (int j=0; j<2;j++){
            for (int i=1; i < 81;i++ ) {
                /*
                - The following section used to access the API and parse the JSON file was modified from the website: https://devqa.io/how-to-parse-json-in-java/
                - The Arrays used have the for loop counters, and so they may be difficult to decipher
                    - For the current for loop, j represents the species being used, where '0' is for fish and '1' is for bugs
                    - For the current for loop, i represents the Species' ID. There are 80 unique fish and bugs each and so the for loop is used to access the API and parse the JSON for each critter
                    - Additionally, the third dimension of the Array contains the 8 critter characteristics read from the API
                */
                String creatureURL = "https://acnhapi.com/v1/"+creatureType[j]+"/" + i;
                String creatureJson = new Scanner(new URL(creatureURL).openStream(), "UTF-8").useDelimiter("\\A").next();
                JsonObject jsonObject = new JsonParser().parse(creatureJson).getAsJsonObject();

                creatureCharacteristics[j][i - 1][0] = jsonObject.getAsJsonObject("name").get("name-USen").getAsString();
                creatureCharacteristics[j][i - 1][1] = jsonObject.getAsJsonObject("availability").get("month-northern").getAsString();
                creatureCharacteristics[j][i - 1][7] = jsonObject.getAsJsonObject("availability").get("month-southern").getAsString(); // This Array was created in development after all of the rest of the code had been created
                creatureCharacteristics[j][i - 1][2] = jsonObject.getAsJsonObject("availability").get("time").getAsString();
                creatureCharacteristics[j][i - 1][3] = jsonObject.getAsJsonObject("availability").get("location").getAsString();
                creatureCharacteristics[j][i - 1][4] = jsonObject.getAsJsonObject("availability").get("rarity").getAsString();
                if (creatureType[j].equals("fish")){
                    creatureCharacteristics[j][i - 1][5] = jsonObject.get("shadow").getAsString();
                }else{
                    creatureCharacteristics[j][i-1][5] = ".";
                }
                creatureCharacteristics[j][i - 1][6] = jsonObject.get("price").getAsString();
                // The above section was modified from the website: https://devqa.io/how-to-parse-json-in-java/

                // The following section is reserved for logistical and formatting errors I found while looking through the database. It is not an exhaustive list
                if (creatureCharacteristics[j][i - 1][1].equals("")) {
                    creatureCharacteristics[j][i - 1][1] = "0-0";
                    creatureCharacteristics[j][i-1][7] = "0-0";
                }

                if (creatureCharacteristics[j][i-1][2].equals("")){
                    creatureCharacteristics[j][i-1][2] = " at all times";
                }else{
                    creatureCharacteristics[j][i-1][2] = " between " + creatureCharacteristics[j][i-1][2];
                }
                if (creatureType[j].equals("fish")){
                    if (creatureCharacteristics[j][i-1][5].equals("Largest (6)")){
                        creatureCharacteristics[j][i-1][5]= "Huge (6)";
                    } else if (creatureCharacteristics[j][i-1][5].equals("Smallest (1)")){
                        creatureCharacteristics[j][i-1][5] = "Tiny (1)";
                    } else if (creatureCharacteristics[j][i-1][5].equals("Narrow")){
                        creatureCharacteristics[j][i-1][5] = "Narrow (0)";
                    } else if (creatureCharacteristics[j][i-1][5].equals("Largest with fin (6)")){
                        creatureCharacteristics[j][i-1][5] = "Huge & finned (6)";
                    } else if (creatureCharacteristics[j][i-1][5].equals("Medium with fin (4)")){
                        creatureCharacteristics[j][i-1][5] = "Medium & finned (4)";
                    }
                    creatureCharacteristics[j][i-1][3] =  "in the "+creatureCharacteristics[j][i-1][3];
                }
                if (creatureCharacteristics[j][i-1][0].toLowerCase().trim().equals("tiger beetle")){
                    creatureCharacteristics[j][i-1][1] = "2-10";
                }
                if (creatureCharacteristics[j][i-1][0].toLowerCase().trim().equals("jewel beetle")){
                    creatureCharacteristics[j][i-1][7] = "10-2";
                }
                if (creatureCharacteristics[j][i-1][0].toLowerCase().trim().equals("barred knifejaw")){
                    creatureCharacteristics[j][i-1][1] = "3-11";
                }
                if (creatureCharacteristics[j][i-1][0].toLowerCase().trim().equals("spider")){
                    creatureCharacteristics[j][i][3] = "Shaking trees";
                }
                if (creatureCharacteristics[j][i-1][0].toLowerCase().trim().equals("tarantula") || creatureCharacteristics[j][i-1][0].toLowerCase().trim().equals("scorpion")) {
                    creatureCharacteristics[j][i-1][4] = "Rare";
                }
                if (creatureCharacteristics[j][i-1][0].toLowerCase().trim().equals("guppy") || creatureCharacteristics[j][i-1][0].toLowerCase().trim().equals("neon tetra")){
                    creatureCharacteristics[j][i-1][7] = "10-5";
                }
                if (creatureCharacteristics[j][i-1][0].toLowerCase().trim().equals("common bluebottle")){
                    creatureCharacteristics[j][i-1][7] = "10-2";
                }
            }

            /*
            - The following for loops are used to determine the months of availability for each critter, i.e. the incoming and leaving months for each critter
            - The j variable in the for arrays is from the outermost for loop, of which the previous for loop was also a part of.
                - The j represents the type of species, either Bugs or Fish
            - The h represents both hemispheres, with a '0' for the Northern Hemisphere and a '1' for the Southern Hemisphere
                - This is because critter availability differs based on the hemisphere.
            - The i represents the id of the species, due to the 80 unique fish and bugs being counted through
            - The k represents whether or not the data being recorded is for an incoming month or an outgoing month
                - A '0' represents an incoming month while a '1' represents an outgoing month
            - The data from the Array is parsed into integer form based on the length of the Array, as a shorter array indicates one month of availability
                while longer ones indicate multiple months or even multiple occurrences of several different months (i.e. '4-8' or even '1-3 & 9-11')
                - The 'creatureMonthsAvailabilityExceptional' Array is for the cases in which there are multiple incoming or outgoing months
             */

            for (int h =0; h < 2; h++){
                for (int i=0; i<80; i++){
                    for (int k=0; k<2; k++){
                        creatureMonthsAvailabilityExceptional[j][i][k][h]=0;
                        int hemisphereCount;
                        if (h==0){
                            hemisphereCount = 1;
                        } else{
                            hemisphereCount = 7;
                        }
                        if ((creatureCharacteristics[j][i][hemisphereCount].length() == 1) || (creatureCharacteristics[j][i][hemisphereCount].length() == 2)) {
                            creatureMonthsAvailability[j][i][k][h] = Integer.parseInt(creatureCharacteristics[j][i][hemisphereCount]);
                        }else if (creatureCharacteristics[j][i][hemisphereCount].length() >= 7){
                            if (k==0){
                                creatureMonthsAvailability[j][i][k][h] = Integer.parseInt(creatureCharacteristics[j][i][hemisphereCount].substring(0,creatureCharacteristics[j][i][hemisphereCount].indexOf('-')));
                            }else{
                                creatureMonthsAvailability[j][i][k][h] = Integer.parseInt(creatureCharacteristics[j][i][hemisphereCount].substring(creatureCharacteristics[j][i][hemisphereCount].indexOf('-')+1,creatureCharacteristics[j][i][hemisphereCount].indexOf(' ')));
                            }
                            String creatureMonthSecond = creatureCharacteristics[j][i][hemisphereCount].substring(creatureCharacteristics[j][i][hemisphereCount].indexOf('&')+2);
                            if ((creatureMonthSecond.length() == 1)|| (creatureMonthSecond.length()==2)){
                                creatureMonthsAvailabilityExceptional[j][i][k][h] = Integer.parseInt(creatureMonthSecond);;
                            }else {
                                if (k==0){
                                    creatureMonthsAvailabilityExceptional[j][i][k][h] = Integer.parseInt(creatureMonthSecond.substring(0,creatureMonthSecond.indexOf('-')));
                                }else{
                                    creatureMonthsAvailabilityExceptional[j][i][k][h] = Integer.parseInt(creatureMonthSecond.substring(creatureMonthSecond.indexOf('-')+1));
                                }

                            }
                        } else{
                            if (k==0){
                                creatureMonthsAvailability[j][i][k][h] = Integer.parseInt(creatureCharacteristics[j][i][hemisphereCount].substring(0,creatureCharacteristics[j][i][hemisphereCount].indexOf('-')));
                            }else{
                                creatureMonthsAvailability[j][i][1][h] = Integer.parseInt(creatureCharacteristics[j][i][hemisphereCount].substring(creatureCharacteristics[j][i][hemisphereCount].indexOf('-')+1));
                            }

                        }
                    }
                }
            }
        }

        /*
        - Once again, the for loops allow for the program to deliver the tweets
        - The 'j' variable represents the type of critter, whether '0' for fish or '1' for bugs;
        - The 'i' variable represents the specific species of either fish or bugs
        - The 'k' variable represents the action being performed, whether a critter is leaving (1) or incoming (0)
         */

        for (int j=0; j<2; j++){
            for (int i =0; i<80; i++){
                for (int k =0; k<2;k++){
                    creatureTweets[j][i][k]= creatureIntention[k]+ "this month is the "+creatureCharacteristics[j][i][0].substring(0, 1).toUpperCase() + creatureCharacteristics[j][i][0].substring(1);
                    if ((j==0)&&(k==0)){
                        creatureCharacteristics[j][i][5] = creatureCharacteristics[j][i][5].substring(0,creatureCharacteristics[j][i][5].indexOf('(')).trim().toLowerCase();
                        creatureCharacteristics[j][i][5] = " and it has a "+ creatureCharacteristics[j][i][5] + " shadow.";
                    }
                    if (k==0){
                        creatureTweets[j][i][k]= creatureTweets[j][i][k] + "! It is "+ creatureCharacteristics[j][i][4].toLowerCase() + " and can be found "+creatureCharacteristics[j][i][3].toLowerCase() + creatureCharacteristics[j][i][2]
                                +". It can be sold for "+creatureCharacteristics[j][i][6]+ " bells"+creatureCharacteristics[j][i][5]+" #AnimalCrossing #NewHorizons";
                    } else{
                        creatureTweets[j][i][k] = creatureTweets[j][i][k] + "! Remember to catch one if you haven't already! #AnimalCrossing #NewHorizons";
                    }
                    // Process to capitalize was modified from 'Jorgesys' response to this stack overflow thread
                    // :https://stackoverflow.com/questions/3904579/how-to-capitalize-the-first-letter-of-a-string-in-java
                }
            }
        }

        /*
        - In the following for loops section, the tweets are sent out based on whether or not the incoming or outgoing month matches a critter's characteristics
        - The 'h' variable represents the hemisphere being considered, with a '0' for the northern hemisphere and a '1' representing the southern hemisphere
        - The 'i' variable represents the actions of the critters, where '0' represents the incoming critters and '1' represents the outgoing critters
        - The 'j' variable is the critter type; '0' for fish and '1' for bugs
        - The 'k' variable represents the species of the critter
         */

        for (int h = 0; h <2; h ++){
            tweetCounter =0;
            for (int i=0; i<2;i++){
                for (int j=0; j<2;j++){
                    for (int k =0; k<80; k++){
                        if ((((creatureMonthsAvailability[j][k][i][h] == currentDate[1]+1) || (creatureMonthsAvailabilityExceptional[j][k][i][h]== currentDate[1]+1)) && (currentDate[2] == 1)) && (i == 0)){
                            tweetCounter = tweetCounter+1;
                            String tweet = creatureTweets[j][k][i];
                            String creature;
                            String hemisphere;
                            if (j==0){
                                creature = "Fish";
                            }else{
                                creature = "Bugs";
                            }
                            if (h==0){
                                hemisphere = "Northern Hemisphere";
                            } else{
                                hemisphere = "Southern Hemisphere";
                            }
                            File attachedImage = new File("C:\\AnyImageDownloadLocation");
                            if (tweetCounter == 1){
                                if (h == 0){
                                    File starterImage = new File ("C:\\AnyImageDownloadLocation");
                                    initialTweet(creatureIntention, i, starterImage, hemisphere);
                                } else{
                                    File starterImage = new File ("C:\\AnyImageDownloadLocation");
                                    initialTweet(creatureIntention, i, starterImage, hemisphere);
                                }
                                tweetReplies(tweet, attachedImage,currentDate);
                            }else{
                                tweetReplies(tweet, attachedImage, currentDate);
                            }
                        } else if ((((creatureMonthsAvailability[j][k][i][h] == currentDate[1]+1) || (creatureMonthsAvailabilityExceptional[j][k][i][h]== currentDate[1]+1)) && (currentDate[2] ==22)) && (i == 1)){ // Three Weeks have passed, give them a warning about what's happening
                            tweetCounter = tweetCounter+1;
                            String tweet = creatureTweets[j][k][i];
                            String creature;
                            String hemisphere;
                            if (j==0){
                                creature = "Fish";
                            }else{
                                creature = "Bugs";
                            }
                            if (h==0){
                                hemisphere = "Northern Hemisphere";
                            } else{
                                hemisphere = "Southern Hemisphere";
                            }
                            File attachedImage = new File("C:\\AnyImageDownloadLocation");
                            if (tweetCounter == 1){
                                if (h == 0){
                                    File starterImage = new File ("C:\\AnyImageDownloadLocation");
                                    initialTweet(creatureIntention, i, starterImage, hemisphere);
                                } else{
                                    File starterImage = new File ("C:\\AnyImageDownloadLocation");
                                    initialTweet(creatureIntention, i, starterImage, hemisphere);
                                }
                                tweetReplies(tweet, attachedImage,currentDate);
                            }else{
                                tweetReplies(tweet, attachedImage, currentDate);
                            }
                        }
                    }
                }
            }
        }
    }
    public static void villagerTweetProcessing(Calendar botCalendar, int[] currentDate) throws Exception{
        String [][] villagerCharacteristics = new String [391][6];
        int [][] villagerBirthdays = new int [391][2];

        /*
        - The 'i' variable in the following for loop represents the villager id
         */

        for (int i=0; i<391; i++){
            String villagerURL = "https://acnhapi.com/v1/villagers/" + (i+1); // Modified from https://devqa.io/how-to-parse-json-in-java/
            String villagerJson = new Scanner(new URL(villagerURL).openStream(), "UTF-8").useDelimiter("\\A").next(); // Modified from https://devqa.io/how-to-parse-json-in-java/
            JsonObject jsonObject = new JsonParser().parse(villagerJson).getAsJsonObject(); // Modified from https://devqa.io/how-to-parse-json-in-java/

            // The following section was modified from the website: https://devqa.io/how-to-parse-json-in-java/
            villagerCharacteristics[i][0] = jsonObject.getAsJsonObject("name").get("name-USen").getAsString();
            villagerCharacteristics[i][1] = jsonObject.get("personality").getAsString();
            villagerCharacteristics[i][2] = jsonObject.get("birthday").getAsString();
            villagerCharacteristics[i][3] = jsonObject.get("species").getAsString();
            villagerCharacteristics[i][4] = jsonObject.get("gender").getAsString();
            villagerCharacteristics[i][5] = jsonObject.get("catch-phrase").getAsString();
            // The above section was modified from the website: https://devqa.io/how-to-parse-json-in-java/
            if (villagerCharacteristics[i][1].equalsIgnoreCase("uchi")){
                villagerCharacteristics[i][1] = "sisterly";
            }else if (villagerCharacteristics[i][1].equalsIgnoreCase("jock")){
                villagerCharacteristics[i][1]="athletic";
            } else if(villagerCharacteristics[i][1].equalsIgnoreCase("normal")){
                villagerCharacteristics[i][1] = "sweet";
            }
            if (villagerCharacteristics[i][4].equalsIgnoreCase("male")){
                villagerCharacteristics[i][4] = "his";
            }else{
                villagerCharacteristics[i][4] = "her";
            }
        }

        /*
        - The 'i' variable in the following for loop represents the villager id
        - The 'j' variable represents the index integer of the date, where '0' represents the day of the month, and '1' represents the month itself
         */

        for (int i=0; i<391; i++){
            for (int j=0; j<2; j++)
            {
                if(j==0){
                    villagerBirthdays[i][j] = Integer.parseInt(villagerCharacteristics[i][2].substring(0,villagerCharacteristics[i][2].indexOf('/')));
                }else{
                    villagerBirthdays[i][j] = Integer.parseInt(villagerCharacteristics[i][2].substring(villagerCharacteristics[i][2].indexOf('/')+1));
                }
            }
            if ((currentDate[1]+1 == villagerBirthdays[i][1]) && (currentDate[2]== villagerBirthdays[i][0])){
                String tweet = villagerCharacteristics[i][5].toUpperCase()+ "! Happy Birthday to this "+villagerCharacteristics[i][1].toLowerCase() + " "+ villagerCharacteristics[i][3].toLowerCase()+ ", "+villagerCharacteristics[i][0]
                        +". Remember to visit " + villagerCharacteristics[i][4] +" birthday party and to bring a special gift! #AnimalCrossing #NewHorizons";
                File attachedImage = new File("C:\\AnyImageDownloadLocation");
                tweetMethod(tweet, attachedImage, currentDate);
            }
        }
    }
    public static void seaCreatureTweetProcessing(int currentDate []) throws Exception{
        // The following variables and array have been created to aid in the data gathering process
        String seaCreatureIntention;
        String hemisphere = "Northern Hemisphere";
        String [][] seaCreatureCharacteristics = new String [40][7]; // The data gathered from the API is automatically parsed into an array based on the characteristics of the critter
        String [] seaCreatureTweets = new String [44]; // Tweets are stored in this array
        String [] tempTweetsArray = new String[44];
        int [] seaCreatureIncomingMonthsNorth = new int [44];
        int [] seaCreatureOutgoingMonthsNorth = new int [44];
        int [] seaCreatureIncomingMonthsExceptionalNorth = new int [40];
        int [] seaCreatureOutgoingMonthsExceptionalNorth = new int [40];
        int [] seaCreatureIncomingMonthsSouth = new int [44];
        int [] seaCreatureOutgoingMonthsSouth = new int [44];
        int [] seaCreatureIncomingMonthsExceptionalSouth = new int [40];
        int [] seaCreatureOutgoingMonthsExceptionalSouth = new int [40];
        int [] tempSortingArray= new int [44];
        int tweetCounter =0;
        int newArrayCounter =0;

        if(currentDate[2] == 1){
            seaCreatureIntention = "Joining us";
        } else{
            seaCreatureIntention = "Leaving us after";
        }

        for (int j=0; j<40; j++) {
            /*
                - The following section used to access the API and parse the JSON file was modified from the website: https://devqa.io/how-to-parse-json-in-java/
                - The Arrays used have the for loop counters, and so they may be difficult to decipher
                - For the current for loop, i represents the Species' ID. There are 40 unique creatures and so the for loop is used to access the API and parse the JSON for each critter
                - Additionally, the third dimension of the Array contains the 7 critter characteristics read from the API
                */
            String creatureURL = "https://acnhapi.com/v1/sea/" + (j+1);
            String creatureJson = new Scanner(new URL(creatureURL).openStream(), "UTF-8").useDelimiter("\\A").next();
            JsonObject jsonObject = new JsonParser().parse(creatureJson).getAsJsonObject();

            seaCreatureCharacteristics[j][0] = jsonObject.getAsJsonObject("name").get("name-USen").getAsString();
            seaCreatureCharacteristics[j][1] = jsonObject.getAsJsonObject("availability").get("month-northern").getAsString();
            seaCreatureCharacteristics[j][2] = jsonObject.getAsJsonObject("availability").get("month-southern").getAsString();
            seaCreatureCharacteristics[j][3] = jsonObject.getAsJsonObject("availability").get("time").getAsString();
            seaCreatureCharacteristics[j][4] = jsonObject.get("speed").getAsString();
            seaCreatureCharacteristics[j][5] = jsonObject.get("shadow").getAsString();
            seaCreatureCharacteristics[j][6] = jsonObject.get("price").getAsString();


            // The following segment is used to clear logical errors with the JSON data, such as pluralization, errors in data values among others

            if (seaCreatureCharacteristics[j][0].substring(seaCreatureCharacteristics[j][0].length()-1).equals("s")){
                if (seaCreatureCharacteristics[j][0].toLowerCase().equals("chambered nautilus")){
                    seaCreatureCharacteristics[j][0] = "chambered nautili";
                } else if (seaCreatureCharacteristics[j][0].toLowerCase().equals("octopus")){
                    seaCreatureCharacteristics[j][0] = "octopi";
                } else if (seaCreatureCharacteristics[j][0].toLowerCase().equals("umbrella octopus")){
                    seaCreatureCharacteristics[j][0] = "umbrella octopi";
                }
            }else {
                if (seaCreatureCharacteristics[j][0].toLowerCase().equals("moon jellyfish")){
                    seaCreatureCharacteristics[j][0] = "moon jellyfishes";
                }else {
                    seaCreatureCharacteristics[j][0] = seaCreatureCharacteristics[j][0] + "s";
                }
            }
            if (seaCreatureCharacteristics[j][0].toLowerCase().equals("gigas giant clams")){
                seaCreatureCharacteristics[j][1] = "5-9";
            }
            if (seaCreatureCharacteristics[j][1].equals("")) {
                seaCreatureCharacteristics[j][1] = "0-0";
                seaCreatureCharacteristics[j][2] = "0-0";
            }
            if (seaCreatureCharacteristics[j][3].equals("")){
                seaCreatureCharacteristics[j][3] = " at all times";
            }else{
                seaCreatureCharacteristics[j][3] = " between " + seaCreatureCharacteristics[j][3];
            }

            if (seaCreatureCharacteristics[j][4].toLowerCase().equals("medium")){
                seaCreatureCharacteristics[j][4] = "moderately fast";
            }

            if (seaCreatureCharacteristics[j][5].toLowerCase().equals("smallest")){
                seaCreatureCharacteristics[j][5] = "tiny";
            }else if (seaCreatureCharacteristics[j][5].toLowerCase().equals("largest")){
                seaCreatureCharacteristics[j][5] = "huge";
            }

            // The follwing code creates a Tweet string and adjusts it based on the date
            // Process to capitalize was modified from 'Jorgesys' response to this stack overflow thread
            // :https://stackoverflow.com/questions/3904579/how-to-capitalize-the-first-letter-of-a-string-in-java

            seaCreatureTweets[j] = "<"+seaCreatureCharacteristics[j][1] + " :" + seaCreatureCharacteristics[j][2] + " >" + seaCreatureIntention + " this month are the "
                    + seaCreatureCharacteristics[j][0].substring(0,1).toUpperCase() +seaCreatureCharacteristics[j][0].substring(1);

            if (currentDate[2] == 1){
                seaCreatureTweets[j] = seaCreatureTweets[j] + "! They are " + seaCreatureCharacteristics[j][4].toLowerCase()+" creatures with a " + seaCreatureCharacteristics[j][5].toLowerCase()+ " shadow. They can be " +
                        "found"+seaCreatureCharacteristics[j][3] + " and can be sold for " + seaCreatureCharacteristics[j][6] + " bells. #AnimalCrossing #NewHorizons";
            }else{
                seaCreatureTweets[j] = seaCreatureTweets[j] + "! Remember to catch one if you haven't already! #AnimalCrossing #NewHorizons" + " |"+(j+1);
            }

            /*
            - The following section is used to find the month data for each creature
            - A temporary tweet array is created to help restore tweets if they have been modified from the originals
             */
            tempTweetsArray[j] = seaCreatureTweets[j];
            seaCreatureIncomingMonthsNorth[j] = Integer.parseInt(seaCreatureTweets[j].substring(1,seaCreatureTweets[j].indexOf('-')));
            seaCreatureOutgoingMonthsNorth[j] = Integer.parseInt(seaCreatureTweets[j].substring(seaCreatureTweets[j].indexOf('-')+1,seaCreatureTweets[j].indexOf(' ')));

            if ((seaCreatureTweets[j].indexOf('&') != -1) && (j!= 34)){ // The additional condition is for logistical reasons as an '&' appears in the timing slot of Sea Creature #35 whereas for all others it only appears in the months list
                seaCreatureTweets[j] = seaCreatureTweets[j].substring(seaCreatureTweets[j].indexOf('&')+2);
                seaCreatureIncomingMonthsExceptionalNorth[j] = Integer.parseInt(seaCreatureTweets[j].substring(0,seaCreatureTweets[j].indexOf('-')));
                seaCreatureOutgoingMonthsExceptionalNorth[j] = Integer.parseInt(seaCreatureTweets[j].substring(seaCreatureTweets[j].indexOf('-')+1,seaCreatureTweets[j].indexOf(' ')));
            }

            seaCreatureTweets[j] = seaCreatureTweets[j].substring(seaCreatureTweets[j].indexOf(":"));
            seaCreatureIncomingMonthsSouth[j] = Integer.parseInt(seaCreatureTweets[j].substring(1,seaCreatureTweets[j].indexOf('-')));
            seaCreatureOutgoingMonthsSouth[j] = Integer.parseInt(seaCreatureTweets[j].substring(seaCreatureTweets[j].indexOf('-')+1,seaCreatureTweets[j].indexOf(' ')));

            if ((seaCreatureTweets[j].indexOf('&') != -1) && (j!= 34)){
                seaCreatureTweets[j] = seaCreatureTweets[j].substring(seaCreatureTweets[j].indexOf('&')+2);
                seaCreatureIncomingMonthsExceptionalSouth[j] = Integer.parseInt(seaCreatureTweets[j].substring(0,seaCreatureTweets[j].indexOf('-')));
                seaCreatureOutgoingMonthsExceptionalSouth[j] = Integer.parseInt(seaCreatureTweets[j].substring(seaCreatureTweets[j].indexOf('-')+1,seaCreatureTweets[j].indexOf(' ')));
            }

            seaCreatureTweets[j] = tempTweetsArray[j];

            // Creatures with two different schedules had their second appearance added to a new array
            if ((seaCreatureTweets[j].indexOf('&') != -1) && (j!= 34)){
                seaCreatureIncomingMonthsNorth[40 + newArrayCounter] = seaCreatureIncomingMonthsExceptionalNorth[j];
                seaCreatureIncomingMonthsSouth[40 + newArrayCounter] = seaCreatureIncomingMonthsExceptionalSouth[j];
                seaCreatureOutgoingMonthsNorth[40 + newArrayCounter] = seaCreatureOutgoingMonthsExceptionalNorth[j];
                seaCreatureOutgoingMonthsSouth[40 + newArrayCounter] = seaCreatureOutgoingMonthsExceptionalSouth[j];
                seaCreatureTweets[40 + newArrayCounter] = seaCreatureTweets[j];
                newArrayCounter = newArrayCounter +1;
            }
        }

        // This restores the original tweet string after any modifications it went through
        for (int i = 0; i<44; i++){
            tempTweetsArray[i] = seaCreatureTweets[i];
        }

        /*
        - Depending on the date, the data is sorted based on the monthly data needed for said date (i.e. the 'leaving' months are needed on the 22nd of the month, which the data will be sorted by)
        - The data is fed into a binary sort algorithm to improve program run times and efficiency
         */


        if (currentDate[2] == 1){
            for (int i =0; i < 44; i++){
                tempSortingArray[i] = seaCreatureIncomingMonthsNorth[i];
            }
            seaCreatureBinarySearchAndTweet(tempSortingArray, seaCreatureTweets, currentDate, tweetCounter, hemisphere);
            System.out.println("--");
            for (int i =0; i < 44; i++){
                seaCreatureTweets[i] = tempTweetsArray[i];
            }

            tweetCounter = 0;
            hemisphere = "Southern Hemisphere";

            for (int j =0; j < 44; j++){
                tempSortingArray[j] = seaCreatureIncomingMonthsSouth[j];
            }
            seaCreatureBinarySearchAndTweet(tempSortingArray, seaCreatureTweets, currentDate, tweetCounter, hemisphere);
            System.out.println("--");
            for (int i =0; i < 44; i++){
                seaCreatureTweets[i] = tempTweetsArray[i];
            }
        }else if (currentDate[2] == 22){
            for (int k =0; k < 44; k++){
                tempSortingArray[k] = seaCreatureOutgoingMonthsNorth[k];
            }
            seaCreatureBinarySearchAndTweet(tempSortingArray, seaCreatureTweets, currentDate, tweetCounter, hemisphere);
            System.out.println("--");
            for (int i =0; i < 44; i++){
                seaCreatureTweets[i] = tempTweetsArray[i];
            }

            tweetCounter = 0;
            hemisphere = "Southern Hemisphere";

            for (int l =0; l < 44; l++){
                tempSortingArray[l] = seaCreatureOutgoingMonthsSouth[l];
            }
            seaCreatureBinarySearchAndTweet(tempSortingArray, seaCreatureTweets, currentDate, tweetCounter, hemisphere);
            for (int i =0; i < 44; i++){
                seaCreatureTweets[i] = tempTweetsArray[i];
            }
        }
    }
    public static void seaCreatureBinarySearchAndTweet(int [] tempSortingArray, String [] seaCreatureTweets, int [] currentDate, int tweetcounter, String hemisphere) throws Exception{
        bubbleSort(tempSortingArray, seaCreatureTweets);
        /*
        - The binary sort algorithm used here was gotten from the Java Arrays library (https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html#binarySearch(int[],%20int)), with the code for finding additional cases taken from Eran's response from
        the following StackOverflow thread: https://stackoverflow.com/questions/29193539/java-arrays-binary-search-multiple-matches
         */
        int index = Arrays.binarySearch (tempSortingArray, currentDate[1]+1);
        int lowerBound = index;
        int upperBound = index;
        String [] creatureIntention = {"Joining us ", "Leaving us after "};
        int intentionCount =0;
        while (true){
            try{
                if (index >= 0) {
                    while ((lowerBound > 0) && (tempSortingArray[lowerBound-1] == currentDate[1]+1)){
                        lowerBound--;
                    }
                    while ((upperBound < tempSortingArray.length - 1) && (tempSortingArray[upperBound+1] == currentDate[1]+1)){
                        upperBound++;
                    }
                }
                break;
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.println(e);
            }
        }
        for (int i = lowerBound; i < upperBound+1; i++) {
            while (true){
                try{
                    int imageID = Integer.parseInt(seaCreatureTweets[i].substring(seaCreatureTweets[i].indexOf('|')+1));
                    seaCreatureTweets[i] =seaCreatureTweets[i].substring(0,seaCreatureTweets[i].indexOf('|'));
                    File attachedImage = new File("C:\\AnyImageDownloadLocation");
                    tweetcounter +=1;
                    if (tweetcounter == 1){
                        if (hemisphere == "Northern Hemisphere"){
                            intentionCount =0;
                            File starterImage = new File ("C:\\AnyImageDownloadLocation");
                            initialTweet(creatureIntention, intentionCount,starterImage, hemisphere);
                            System.out.println("Initial Tweet - North: ");
                            System.out.println(tweetcounter);
                        }
                        else{
                            intentionCount = 1;
                            File starterImage = new File ("C:\\AnyImageDownloadLocation");
                            initialTweet(creatureIntention, intentionCount,starterImage, hemisphere);
                            System.out.println("Initial Tweet - South: ");
                            System.out.println(tweetcounter);
                        }
                         tweetReplies(seaCreatureTweets[i], attachedImage, currentDate);
                        System.out.println("Complemetary Tweet: "+seaCreatureTweets[i]);
                    } else{
                        tweetReplies(seaCreatureTweets[i], attachedImage, currentDate);
                        System.out.println("Complementary Tweet: "+seaCreatureTweets[i]);
                        System.out.println(tweetcounter);
                    }
                    break;
                }catch(ArrayIndexOutOfBoundsException e){
                    System.out.println("No new Sea creatures this month");
                    break;
                }
            }
        }
    }
    public static void bubbleSort (int [] tempSortingArray, String [] seaCreatureTweets){
        /*
        - The Bubble Sort Code was modified from the Bubble Sort Pseudo code from Rita Andrighetti's presentation 'Bubble Sort V2' for the ICS3U class. The code itself was modified from
        Richard Chu's Bubble Sort class lesson in Bayview Secondary School's ICS3U class in the second semester of the 2017-2018 school year.
         */
        int max = seaCreatureTweets.length;
        int last;
        int temp;
        String tempTweet;
        for (int i =0; i < max-1; i++){
            last = max-i;
            for (int j=0; j< last-1; j++){
                if (tempSortingArray[j] >= tempSortingArray[j+1]){
                    temp = tempSortingArray[j];
                    tempSortingArray[j] = tempSortingArray[j+1];
                    tempSortingArray[j+1] = temp;

                    tempTweet = seaCreatureTweets[j];
                    seaCreatureTweets[j] = seaCreatureTweets[j+1];
                    seaCreatureTweets[j+1] = tempTweet;
                }
            }
        }
    }
    public static Twitter twitterAuthentication () throws IOException {
        // The following File Input code was modified from the Bayview Secondary School ICS3U class presentation "2:Reading Files" by Rita Andrighetti
        File twitterAuthentication = new File ("C:\\AnyImageDownloadLocation");
        Scanner fileScanner = new Scanner(twitterAuthentication);
        String [] twitterKey = new String[4];
        for (int i = 0; i <4; i++){
            if (fileScanner.hasNext()){
                twitterKey[i]= fileScanner.nextLine().trim();
                twitterKey[i] = twitterKey[i].substring(twitterKey[i].indexOf(":")+1).trim();
            }
        }
        // The following code snippet was gained from the Twitter4j website: http://twitter4j.org/en/configuration.html
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(twitterKey[0])
                .setOAuthConsumerSecret(twitterKey[1])
                .setOAuthAccessToken(twitterKey[2])
                .setOAuthAccessTokenSecret(twitterKey[3])
                .setTweetModeExtended(true); // This segment was found via 'Jesse English's' response on a Twitter4j forum: https://groups.google.com/forum/#!topic/twitter4j/EqUg5R-nFgQ
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitterBot = tf.getInstance();
        return twitterBot;
        // The code snippet above was gained from the twitter4j website: http://twitter4j.org/en/configuration.html
    }
    public static void tweetVerification(int [] currentDate) throws Exception{
        // The following code was modified from the Bayview Secondary School ICS3U Course Presentation on File IO by Rita Andrighetti
        Calendar verificationClock = Calendar.getInstance();
        currentDate[3]=verificationClock.get(Calendar.HOUR_OF_DAY);
        currentDate[4] = verificationClock.get(Calendar.MINUTE);
        currentDate[5] = verificationClock.get(Calendar.SECOND);
        String currentTime = currentDate[3]+":"+currentDate[4]+":"+currentDate[5];
        PrintWriter tweetVerification = new PrintWriter(new FileWriter("C:\\AnyImageDownloadLocation", true));
        tweetVerification.println("Tweet Authenticated. The tweet(s) for "+ (currentDate[1]+1)+"/"+currentDate[2]+"/"+currentDate[0]+" has/have been sent. Thank You! This message was sent at: "+currentTime);
        tweetVerification.close();
    }
    public static void tweetMethod(String tweet, File attachedImage, int [] currentDate) throws Exception{
        /*
        - The code used to tweet the message was adapted from this youtube video by MZ M https://www.youtube.com/watch?v=PPP7_pCO3xI
        - The image attachment code and error processing information were derived from this source https://www.codota.com/code/java/methods/twitter4j.StatusUpdate/%3Cinit%3E
        - This source was also used in developing this code https://stackoverflow.com/questions/28218761/upload-image-twitter4j
         */

        Twitter twitterBot = twitterAuthentication();
        while(true){
            try{
                StatusUpdate tweetUpdate = new StatusUpdate(tweet);
                tweetUpdate.setMedia(attachedImage);
                twitterBot.updateStatus(tweetUpdate);
                System.out.println("Success! Tweet Sent: "+ "\""+tweetUpdate.getStatus()+"\"");
                break;
            }catch(TwitterException e){
                e.printStackTrace();
            }
        }
    }
    public static void tweetReplies(String tweet, File attachedImage, int [] currentDate) throws Exception{
        Twitter twitterBot = twitterAuthentication();

        /*
        - The code used to tweet the message was adapted from this youtube video by MZ M https://www.youtube.com/watch?v=PPP7_pCO3xI
        - The image attachment code and error processing information were derived from this source https://www.codota.com/code/java/methods/twitter4j.StatusUpdate/%3Cinit%3E
        - This source was also used in developing this code https://stackoverflow.com/questions/28218761/upload-image-twitter4j
        - Yasuke's response on this forum was used in developing the tweet response code: https://groups.google.com/forum/#!searchin/twitter4j/in_reply_to_status_id/twitter4j/8n6lweif9gk/EAR58xKs_PoJ.html
        - Martimc's answer on this forum was used in developing this code as well: https://forum.processing.org/one/topic/twitter4j-reply-to-tweet.html
         */

        List<Status> userTimeline = twitterBot.getUserTimeline();
        String [] latestTweet = userTimeline.toString().split(",",100);
        String [] latestTweetIDNumber = latestTweet[1].split("=",2);
        Long lastTweetID = Long.parseLong(latestTweetIDNumber[1]); // Previous Tweet ID
        System.out.println("@ ACNHorizonsBot "+tweet);
        while(true){
            try{
                StatusUpdate tweetUpdate = new StatusUpdate("@ACNHorizonsBot "+tweet);
                tweetUpdate.setMedia(attachedImage);
                tweetUpdate.setInReplyToStatusId(lastTweetID);
                twitterBot.updateStatus(tweetUpdate);
                System.out.println("Success! Tweet Sent: "+ "\""+tweetUpdate.getStatus()+"\"");
                break;
            }catch(TwitterException e){
                e.printStackTrace();
            }
        }
    }
    public static void initialTweet(String [] creatureIntention, int i, File starterImage, String hemisphere) throws Exception{
        /*
        - The code used to tweet the message was adapted from this youtube video by MZ M https://www.youtube.com/watch?v=PPP7_pCO3xI
        - The image attachment code and error processing information were derived from this source https://www.codota.com/code/java/methods/twitter4j.StatusUpdate/%3Cinit%3E
        - This source was also used in developing this code https://stackoverflow.com/questions/28218761/upload-image-twitter4j
         */

        String initialTweet = "[THREAD] Here is the list of critters that are " + creatureIntention[i].toLowerCase() + "this month in the " + hemisphere;
        Twitter twitterBot = twitterAuthentication();
        System.out.println(initialTweet);
        while(true){
            try{
                StatusUpdate tweetUpdate = new StatusUpdate(initialTweet);
                tweetUpdate.setMedia(starterImage);
                twitterBot.updateStatus(tweetUpdate);
                System.out.println("Success! Tweet Sent: "+ "\""+tweetUpdate.getStatus()+"\"");
                break;
            }catch(TwitterException e){
                e.printStackTrace();
            }
        }
    }
}

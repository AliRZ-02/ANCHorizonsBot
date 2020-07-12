# ANCHorizonsBot
Source Code for the Animal Crossing New Horizons Bot I created that tweets out villager birthdays and monthly critter information 

### Motivation
- I created this project near the tail end of my senior year in high school to showcase several skills I had picked up with respect to software design and Java programming. The inspiration behind the project was my moderate frustration as I was playing the new Animal Crossing game. The variety of fish and animals available in the game rotate every month and I was having trouble keeping record of what critters were available each month. I decided to create a Twitter bot that would take information from an Animal Crossing New Horizons Database and would tweet it at the beginning of each month. Eventually, I decided to expand the bot's functionality by adding villager birthdays because it was relatively simple to add.

## Getting Started
### Prerequisites
- To run the code in the program, you will need to download the **Twitter4J** Library from the [Twitter4J website] (http://twitter4j.org/en/index.html). Additionally, the GSON library will be required, which can be download from the [GSON Github Repository](https://github.com/google/gson). Both of these libraries can be added as Maven dependencies as well. 
- A Twitter Developer Account must be created, and the respective authentication keys obtained for the Twitter App being created.
#### Installation
- I ran the program inside the IntelliJ IDEA IDE so I imported the required files into the libraries file of my program. Additionally, inside of the Project, one has to create a Twitter Authentication File that enables the program to access the Twitter Developer Keys. 
- In the Intellij IDEA IDE, Go to the Project Structure, which can be accessed from the Files Section on a Windows machine. Click the Libraries section and add the Libraries needed from the folder that was downloaded. Detailed instructions can be found from the [Jetbrains Website](https://www.jetbrains.com/help/idea/library.html)
- Additionally, the bot requires images of each critter available and each villager available. There is a method in the program that allows the user to download all of the pictures that will be needed from an Animal Crossing Database
- Run the program once with only the method for downloading the images by commenting out the other methods. Make sure the File Location Specified exists as all images will be downloaded there.
- Once the images have been downloaded, you can comment out or remove the method for downloading the images
- Determine the File Location for the Twitter Authentication File Created and set it as the File variable in the Twitter Authentication Method
- The code should look similar to this: `File twitterAuthentication = new File (AuthenticationFileLocation);` 
- Make sure all required classes have been imported into the project.
#### Test Run
To test the program's functionality, first, comment out the Twitter Status Update Code in each method and replace the content of the tweet onto a `System.out.println` message. Once it has been ensured that the program works as intended, Revert the code that was commented out and run the program in the IDE. 
- The program automatically retrieves the user's calendar information from the system and as such, the program should run without any user input.
- When testing, make sure to change the File Locations for all files in the program to the respective files on your computer and in the program folder. Currently, all are notated with `C:\AnyImageDownloadLocation` 
#### Deployment
- To deploy this program, one can host their code on a virtual hosting platform or on a Raspberry Pi, however, I decided to create a JAR file of my program and then run a task on Windows Task Scheduler that executed the program at 10:00 AM every day, ensuring consistent tweets without having to manually run the program each day or having to pay to host my software online
### Authors
- This bot was developed by myself, [Ali Raza Zaidi](https://twitter.com/Ali_RZ02) 
### License
- This software is licensed through the GNU General Public License v3.0
### Learning Process
- As a high school student, there were several things I learned throughout the development of this piece of software
- I was unfamiliar with many of the libraries and classes I needed to use to facilitate such a project and I learned while creating, along with inspiration and modification of code from others
- I became more familiar with File I/O in Java through the necessity of reading Authentication Data from a file and from writing a verification message to an external document
- I became more familiar with using APIs and the GSON library in Java
- I became more familiar with the Twitter4j library in Java and I learned many important methods in regards to the Twitter API
- I learned to work with the Java Calendar library
- I became comfortable with multi-dimensional arrays and the logic associated with handling them
- I learned how to use the resources at my disposal to find answers to problems I was having, whether that be through Documentation, Stack Overflow or other methods
- I learned how to work with Github
- I learned how to work with errors and exceptions as well as the OAuth process
- I learned to work with sorting algorithms and search libraries, specifically, bubble sort and binary search algorithms
### Development
- The software is no longer in development, however, if errors arise in its usage, I may go and update errors (although I'd say it's pretty bug-free, hopefully)
### Acknowledgements
- Code segments used have been cited within the Main.java file; 
- Thanks to [PurpleBooth](https://gist.github.com/PurpleBooth/109311bb0361f32d87a2) on Github for the ReadMe File Template
- Thanks to [@Mattisenhower](https://twitter.com/mattisenhower) on Twitter as his Splatoon 2 Bot was an inspiration behind my ANCHorizonsBot
- Thanks to [Amir Ghahrai](https://devqa.io/how-to-parse-json-in-java/) for his code which was used to parse the JSON files from the API
- Thanks to ['Jorgesys'] (https://stackoverflow.com/questions/3904579/how-to-capitalize-the-first-letter-of-a-string-in-java) from this stack overflow thread for their code which was used to Capitalize the responses
- Thanks to [Alex] (https://stackoverflow.com/questions/5882005/how-to-download-image-from-any-web-page-in-java) for his code which was used to download the images from the API
- Thanks to [Alexis Lours](https://github.com/alexislours/ACNHAPI) for creating and upkeeping the Animal Crossing New Horizons Database.
- Thanks to Rita Andrighetti from Bayview Secondary School and their entire ICS department for their ICS3U lessons on File I/O
- Thanks to Richard Chu & Rita Andrighetti from Bayview Secondary School for their ICS3U lessons on Bubble Sort
- Thanks to the [Twitter4j team](http://twitter4j.org/en/configuration.html) for their work in creating the Twitter4j library and in documenting it, where code snippets relating to the Twitter Authentication Process were obtained from
- Thanks to [Jesse English](https://groups.google.com/forum/#!topic/twitter4j/EqUg5R-nFgQ) for their response in the linked forum, which I used in the code handling the Twitter Authentication
- Thanks to [Yasuke](https://groups.google.com/forum/#!searchin/twitter4j/in_reply_to_status_id/twitter4j/8n6lweif9gk/EAR58xKs_PoJ.html) for their response in the linked forum, which I used for the tweeting process
- Thanks to [the Youtuber MZ M](https://www.youtube.com/watch?v=PPP7_pCO3xI) for his video detailing the setup for a twitter bot, which was integral in helping me develop my program
- Thanks to [martinmc](https://forum.processing.org/one/topic/twitter4j-reply-to-tweet.html) for detailing how to reply to a tweet using the library
- Thanks to [this Codota thread](https://www.codota.com/code/java/methods/twitter4j.StatusUpdate/%3Cinit%3E) in helping me develop a method to tweet with images in my program
- Thanks to [All participating in this Stack Overflow thread](https://stackoverflow.com/questions/28218761/upload-image-twitter4j) in also helping me with the code required to tweet with images
- Thanks to [Franzanaz](https://imgur.com/gallery/PX3xHx2) on Imgur for their Animal Crossing New Horizons Leaf Pattern which I repurposed to build images for the tweets
- Thanks to [Eran](https://stackoverflow.com/questions/29193539/java-arrays-binary-search-multiple-matches) for their code to find multiple matches from a binary search 
- Thanks to Oracle for their Java Documentation which was used extensively in the project
- All intellectual property associated with Animal Crossing New Horizons belongs to Nintendo

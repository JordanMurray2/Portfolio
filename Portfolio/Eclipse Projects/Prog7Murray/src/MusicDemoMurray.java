//
//Jordan Murray
//Prog 7
//Due Date and Time: 03/26/18 before 1:30PM
//
//Description: This program will print out a user friendly menu that gives the user the option to 
//add a song, find the song with the longest runtime,find the shortest runtime, find the number of
//songs on the CD, find the total remaining time of the CD, and print out all of the details on the 
//CD, or Quit. Depending on the users choice, another class will be called and will execute the 
//calculation the user wishes to know and then prints it out for the user to see. 
//

import java.util.*;
import java.util.Scanner;

public class MusicDemoMurray {

	public static void main(String[] args) {
	  
	  Scanner keyboard = new Scanner(System.in);
	  
	  //declare variables
	  char choice = 0;
	  String fake = "";
	  SongMurray newSong = null;
	  String title = "";
	  String artist = "";
	  int minRunTime = 0;
	  int secRunTime = 0;
	  MixCDMurray newCD = new MixCDMurray();
	  int remainingMin = 0;
	  int remainingSec = 0;
	  
	  do
	  {
		    //prompt the user with menu
			System.out.println("Menu: Enter one of the following options");
			System.out.println("A: Add a song to the CD");
			System.out.println("L: Find the song with the longest runtime");
			System.out.println("S: Find the song with the shortest runtime");
			System.out.println("N: Find the number of songs on the CD");
			System.out.println("R: Find the total remaining time of the CD");
			System.out.println("P: Print out details about all songs on the CD");
			System.out.println("Q: Quit");
			fake = keyboard.next();
			choice = fake.charAt(0);
	  }//do
	  while (choice != 'A' && choice != 'a' && choice != 'L' && choice != 'l' &&
		     choice != 'S' && choice != 's' && choice != 'N' && choice != 'n' &&
	         choice != 'R' && choice != 'r' && choice != 'P' && choice != 'p' &&
		     choice != 'Q' && choice != 'q');
	  
	  while (choice != 'Q' && choice != 'q')
	  {
		switch(choice)  
		{
		  case 'a':
		  case 'A':
			newSong = new SongMurray();
			//prompt the user for the title
			System.out.println("Enter the Title of the song");
			  title = keyboard.next();
			//set the title
			newSong.setTitle(title);
			
			//prompt the user for the artist's name
			System.out.println("Enter the name of the Artist");
			  artist = keyboard.next(); 
			 
			//set the artist
			newSong.setArtist(artist);
			
			do
			{
			//Prompt the user for the length of the song in minutes
			System.out.println("Enter the runtime of the song in minutes");
			  minRunTime = keyboard.nextInt();
			  
			//Prompt the user for the length of the song in seconds
				System.out.println("Enter the runtime of the song in seconds");
				  secRunTime = keyboard.nextInt();
			  
		    //Calculate the runtime in seconds
		    secRunTime += minRunTime * 60;
			}//do
			while(secRunTime > 4800);
		    	
			//set the runtime
			newSong.setRunTime(secRunTime);
			
			//add the new song to the cd if possible
			if(newCD.addToCD(newSong)) 
			{
			  System.out.println("Your song was added");
			  System.out.println("");
			}//if
			else 
			{
		      System.out.println("Your song could not be added, There is not enough room");
			  System.out.println("");
			}//else
			break;
		  case 'l':
		  case 'L':
			//find the longest song
			if(newCD.findLongestSong() != null)
			{
			  System.out.print("The Song with the Longest runtime has the ");
			  newCD.findLongestSong().printDetails();
			  System.out.println("");
			}//if
			else System.out.println("No Songs on the CD, Please add a song");
			     System.out.println("");
			break;
		  case 's':
		  case 'S':
			//find the shortest song
			if(newCD.findShortestSong() != null)
			{
			  System.out.print("The Song with the Shortest runtime has the "); 
			  newCD.findShortestSong().printDetails();
			  System.out.println("");
			}//if
			else System.out.println("No Songs on the CD, Please add a song");
			     System.out.println("");
			break;
		  case 'n':
		  case 'N':
			if(newCD.getSize() != 0)
			{
			  //find the number of songs on the CD
			  System.out.print("The number of tracks on the CD "); 
			  System.out.println(newCD.getSize());
			  System.out.println("");
			}//if
			else System.out.println("There are no tracks on the CD, Please add a song");
			     System.out.println("");
			break;
		  case 'r':
		  case 'R':
			if(newCD.getSize() != 0)
			{
			//get the remaining time on the CD
			remainingSec = newCD.calcRemainingTime();
			remainingMin = remainingSec / 60;
			remainingSec = remainingSec % 60;
			System.out.println("The remaining time on the CD is " + remainingMin + " minutes and " + remainingSec + " seconds");
			System.out.println("");
			}//if
			else System.out.println("The CD is Empty, There is 80 minutes of time remaining.");
			     System.out.println("");
			break;
		  case 'p':
		  case 'P':
	        if(newCD.getSize() != 0)
	        {
		      newCD.printSongList();
		      System.out.println("");
	        }//if
	        else System.out.println("No Details to Print, Please add a song first");
	             System.out.println("");
		}//switch
		
		//prompt the user again
		System.out.println("Menu: Enter one of the following options");
		System.out.println("A: Add a song to the CD");
		System.out.println("L: Find the song with the longest runtime");
		System.out.println("S: Find the song with the shortest runtime");
		System.out.println("N: Find the number of songs on the CD");
		System.out.println("R: Find the total remaining time of the CD");
		System.out.println("P: Print out details about all songs on the CD");
		System.out.println("Q: Quit");
		fake = keyboard.next();
		choice = fake.charAt(0);
	  }//while
	  
	  System.out.println("Goodbye!");
	}//main
}//MusicDemoMurray

//
//Jordan Murray
//Prog 7
//Due Date and Time: 03/26/18 before 1:30PM
//
//Description: This program consists of two instance variables: mySongs which is an array of 
//SongMurray objects and mySize. There is a full and null constructor and a method to get and set
//the mySize. There is also a boolean method called addToCD which will add a song if there is room.
//The methods findLongestSong and findShortestSong will find the runtime of all of the songs entered
//so far and return the appropriate object. The method calcRemainingTime will add up all of the 
//runtimes of all of the songs, subtract that from the 80 minute time limit, and return the answer. 
//The final method is printSongList which will call the printDetails method and print out a list of
//all of the songs and their details. 

//

public class MixCDMurray
{
	//declare the variables
	  private SongMurray[] mySongs;
	  private int mySize;
		  
	  //full constructor
	  public MixCDMurray (int newSize)
	  {
		mySize = newSize;
		
	  }//MixCDMurray full constructor

	  //null constructor
	  public MixCDMurray()
	  { 
		mySongs = new SongMurray [12];
		
		//set all values in the array to empty	
		for(int i = 0; i < mySongs.length; i++)
		  mySongs[i] = null; 
	
		mySize = 0;
	  }//SongMurray null constructor
		  
	  //set size
	  public void setSize(int newSize)
	  {
	     mySize = newSize;
	  }//setSize

	  //return size
	  public int getSize()
	  {
	    return mySize;
	  }//getSize
	  
	  //add a song to the CD if it is not full
	  public boolean addToCD(SongMurray theSong)
	  {
		 //declare variables
		 boolean answer = false;
		 int time = 0;
		 
		//add up all the lengths of the song
		for(int i = 0; i < mySize; i++)
		  time += mySongs[i].getRunTime();
			
		 //find out if array is full
		 if (mySize < mySongs.length && (theSong.getRunTime() + time) <= 4800 ) 
		 {
	     answer = true;
	     mySongs[mySize] = theSong;
	     mySize++;
		 }//if
		 return answer;
	  }//addToCD
	  
	//find the song with the longest runtime	
	  public SongMurray findLongestSong()
	  {
		//declare variables 
		SongMurray longSong = mySongs[0];
		
		//find the song in the array		
		for (int j = 1; j < mySize; j++)  
		  if(mySongs[j].getRunTime() > longSong.getRunTime()) 	
			  longSong = mySongs[j];
		
		return longSong;
	  }//findLongestSong
	  
	//find the song with the shortest runtime	
	  public SongMurray findShortestSong()
	  {
		//declare variables 
		SongMurray shortSong = mySongs[0];
		
		//find the song in the array		
		for (int k = 1; k < mySize; k++)  
		  if(mySongs[k].getRunTime() < shortSong.getRunTime()) 	
			  shortSong = mySongs[k];
		
		return shortSong;
	  }//findShortestSong
	  
	  //figure out the amount of unused time on the CD
	  public int calcRemainingTime()
	  {
	    //declare variables
		int timeSoFar = 0;
		int timeLeft = 0;
		
		//add up all the lengths of the song
		for(int i = 0; i < mySize; i++)
		  timeSoFar += mySongs[i].getRunTime();
		
		//calculate time left in seconds
		timeLeft = 4800 - timeSoFar;
	
		//return the time left in seconds
	    return timeLeft;
	  }//calcRemainingTime
	  
	  //print all of the details of every track on the CD
	  public void printSongList()
	  {
		//loop through array and print if value is not null  
		for(int j = 0; j < mySize; j++)  
		{
		  if(mySongs[j] != null) 
		  {
		    System.out.println("Song " + (j + 1) + ": ");
		    mySongs[j].printDetails();
		    System.out.println("");
		  }//if 
	    }//for
	  }//printSongList
}//mixCDMurray

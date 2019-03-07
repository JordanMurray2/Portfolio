//
//Jordan Murray
//Prog 7
//Due Date and Time: 03/26/18 before 1:30PM
//
//Description: This program consists of three instance variables: myTitle, myArtist, and myRunTime. 
//There is a full and a null constructor along with all of the getters and setters for the instance
//variables. The getters and setters are called in other classes and used to set values, store them, 
//and return them so the entered values can be called on later. The last method is print details 
//which uses all of the instance variables data and prints it out in a user friendly way. 
//

public class SongMurray 
{
  //declare the variables
  private String myTitle;
  private String myArtist;
  private int myRunTime;
	  
  //full constructor
  public SongMurray(String newTitle, String newArtist, int newRunTime)
  {
	myTitle = newTitle;
	myArtist = newArtist;
	myRunTime = newRunTime;
  }//SongMurray full constructor

  //null constructor
  public SongMurray()
  { 
	myTitle = "None";
	myArtist = "None";
	myRunTime = 0;
  }//SongMurray null constructor
	  
  //set title
  public void setTitle(String newTitle)
  {
     myTitle = newTitle;
  }//setTitle

  //set artist
  public void setArtist(String newArtist)
  {
	myArtist = newArtist;
  }//setArits
	  
  //set run time
  public void setRunTime(int newRunTime)
  {
	 myRunTime = newRunTime;
  }//setRunTime

  //return title
  public String getTitle()
  {
    return myTitle;
  }//getTitle

  //return artist
  public String getArtist()
  {
	return myArtist;
  }//getArtist

  //return run time
  public int getRunTime()
  {
	return myRunTime;
  }//getRunTime
  
  //print all of the details out
  public void printDetails()
  {
	//variables
	int min = 0;
	int sec = 0;
	
	System.out.println("Title: " + myTitle);  
	System.out.println("The Artist is: " + myArtist);
	min = myRunTime / 60;
	sec = myRunTime % 60;
	System.out.println("The Runtime of the Song is: " + min + " minutes and " + sec + " seconds");
  }//toString  
}//SongMurray

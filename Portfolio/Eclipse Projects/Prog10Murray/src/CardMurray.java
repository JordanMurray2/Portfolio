//
//Jordan Murray
//Prog 10
//Due Date and Time: 04/30/18 before 1:30PM
//
//Description: This class has 2 instance variables myValue and mySuit. myValue is an integer and
//mySuit is a character. This class has a full constructor, a null constructor, and a getter and 
//setter for each of the instance variables
//

public class CardMurray 
{
  private int myValue;
  private char mySuit;
  
  //full constructor
  public CardMurray(int newValue, char newSuit) 
  {
	myValue = newValue;
	mySuit = newSuit;
  }//CardMurray full constructor
  
  //null constructor
  public CardMurray()
  {
	  myValue = 0;
	  mySuit = 'N'; //'N' for Null
  }//CardMurray null constructor
  
  //set the value
  public void setValue(int newValue)
  {
	 myValue = newValue; 
  }//setValue
  
  //set the suit
  public void setSuit(char newSuit)
  {
	mySuit = newSuit;
  }//setSuit
  
  //return the value
  public int getValue()
  {
	return myValue;
  }//getValue
  
  //return the suit
  public char getSuit()
  {
	return mySuit;  
  }//getSuit
}//CardMurray

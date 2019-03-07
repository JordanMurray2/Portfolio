//
//Jordan Murray
//Prog 10
//Due Date and Time: 04/30/18 before 1:30PM
//
//Description: This class has two instance variables: myData which is of the type CardMurray and
//myNext which is of the type NodeMurray. There is a null constructor and a full constructor which
//only takes in a CardMurray newData. There is also a getter and setter for each of the instance
//variables.
//

public class NodeMurray 
{
   //declare variables
   private CardMurray myData;
   private NodeMurray myNext;
	
   //null constructor
   public NodeMurray()
   {
	 myData = null;
	 myNext = null;
   }//null constructor
	
   //full constructor
   public NodeMurray(CardMurray newData)
   {
     myData = newData;
     myNext = null;
   }//full constructor
	
   //return myData
   public CardMurray getData()
   { 
	 return myData;
   }//getData
	
   //return myNext
   public NodeMurray getNext()
   {
	  return myNext;  
   }//getNext
   
   //set myData
   public void setData(CardMurray newData)
   {
	  myData = newData; 
   }//setData
	
   //set myNext
   public void setNext(NodeMurray newNext)
   {
	  myNext = newNext; 
   }//setData
}//NodeMurray


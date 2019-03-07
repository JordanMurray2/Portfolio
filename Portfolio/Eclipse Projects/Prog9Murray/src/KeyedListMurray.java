//
//Jordan Murray
//Prog 8
//Due Date and Time: 04/05/18 before 1:30PM
//
//Description: This class has 2 instance variables. The first is myItems which is an array of ItemMurray objects. The second is myNumItems. There is 
// a null constructor and a getter for myNumItems. Some other methods in this class are clear which sets myNumItems to 0 and clears the array, findIndex
//which is a private helper method that returns the position that an item can be found, add and remove which will return a boolean, where the answer is 
//determined by whether the user wants to either add or remove an item, retrieve will return a specific item object, isEmpty and isFull will return a
//boolean accordingly, print will call the printDetails method in ItemMurray and loop through the entire array printing the details of every item, 
//getCount will add up all of the quantities of all of the items and return the sum, and lastly calcTotal will take the quantities and the price for 
//every item and calculate the total for each specific item and then add it to the sum. calcTotal will then return a sum. 
//

public class KeyedListMurray 
{
  //declare variables 
  private NodeMurray myHead;
  
  //null constructor
  public KeyedListMurray()
  {
    myHead = null; 
  }//KeyedListMurray null constructor
 
  //return number of nodes 
 public NodeMurray getHead()
 {
   return myHead; 
 }//getHead
 
 public void setHead (NodeMurray newHead)
 {
   myHead = newHead;	 
 }//setHead
 
 //reset all values to null
 public void clear()
 {
   //resets all values in the array to empty	
   myHead = null;
 }//clear
 
 //adds an item into the array if it is not already there and the array is not full
 public boolean add(ItemMurray product)
 {
	//declare variables 
	boolean added = false;
	NodeMurray curr = myHead;
	NodeMurray prev = null;
	NodeMurray newGuy = new NodeMurray();
	
	newGuy.setData(product);
	
	if( myHead == null)
	{
	  added = true;
	  curr = newGuy;
	}//if
	
    while(curr != null && added == false)
    {
    	  if(curr.getData().getName().compareToIgnoreCase(newGuy.getData().getName()) > 0)
    	  {
    		added = true; 
    	  }//if
    	  else
    	  {
    	    prev = curr;
    	    curr = curr.getNext();
    	  }//else
    }//while
    
    if(prev == null)
    {
    	  added = true;
    	  newGuy.setNext(myHead);
    	  myHead = newGuy;
    }//if
    else
    {
    	  added = true;
    	  newGuy.setNext(curr);
    	  prev.setNext(newGuy);
    }//else
    
	return added;
 }//add
 
 //removes an item from the array if it is found. If it is not found, tell the user
 public boolean remove(String target)
 {
   //declare variables
   boolean removed = false; 
   NodeMurray curr = myHead;
   NodeMurray prev = null;
   
   while (curr != null && removed == false)
   {
	 if(target.equalsIgnoreCase(curr.getData().getName()))  
	 {
	   removed = true; 
	 }//if
	 else
	 {
		prev = curr;
		curr = curr.getNext();
	 }//else
   }//while
   
   if(removed)
   {
	if(curr == myHead)
	{
	  myHead = curr.getNext();
	}//if
	else 
	  prev.setNext(curr.getNext());
   }//if
  
   return removed;
 }//remove
 
 //finds a user-specified item and returns if the value was found or not
 public ItemMurray retrieve(String keyValue)
 { 
	//declare variable
	ItemMurray answer = null;
	NodeMurray curr = myHead;
	
	while(curr != null && answer == null)
	{
	  if(curr.getData().getName().equalsIgnoreCase(keyValue))
	    answer = curr.getData();
	  else 
	  {
	   curr = curr.getNext();
	  }//else
	}//while
	
	return answer;
 }//retrieve
 
 //checks if the array is empty
 public boolean isEmpty()
 {
   boolean answer = false;
   if(myHead == null)
	 answer = true;
   
   return answer;
 }//isEmpty
 
 //prints out all of the details
 public void print()
 {
   //declare variables
	 NodeMurray curr = myHead;
	 
	 while(curr != null )
	 {
	  //print all of the items
	  curr.getData().printDetails();
	  System.out.println("");
	  
	  //move to the next node
	  curr = curr.getNext();
	 }//while
 }//print
 
 //counts the number of items by adding up the quantities of each item
 public int getCount()
 {
	//declare variables
	int quantitySum = 0;
	NodeMurray curr = myHead;
	  
	while(curr != null)
	{
	 quantitySum += curr.getData().getQuantity();
	 curr = curr.getNext();
	}//while
	
	return quantitySum;
 }//getCount
 
 //calculates the total sum due by multiplying the quantity and price of each item
 public double calcTotal()
 {
	//declare variables
	double totalSum = 0.0;
	double totalPrice = 0.0;
	NodeMurray curr = myHead;
	double itemPrice = curr.getData().getPrice();
	int itemQuantity = curr.getData().getQuantity();
	
	while(curr != null)
	{
	  totalPrice = itemPrice*itemQuantity;
	  totalSum += totalPrice;
	  curr = curr.getNext();
	}//while
	
	return totalSum;
 }//calcTotal
}//KeyedListMurray

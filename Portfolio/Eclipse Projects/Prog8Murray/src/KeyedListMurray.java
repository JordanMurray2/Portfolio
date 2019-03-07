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
  private ItemMurray[] myItems;
  private int myNumItems;
  
  //null constructor
  public KeyedListMurray()
  {
    myItems = new ItemMurray [20];
		
	//set all values in the array to empty	
	for(int i = 0; i < 20; i++)
	  myItems[i] = null;
	  
	myNumItems = 0;  
  }//KeyedListMurray null constructor
 
  //return number of items in array
 public int getMyNumItems()
 {
   return myNumItems; 
 }//getMyNumItems
 
 //reset all values to null
 public void clear()
 {
   //resets all values in the array to empty	
   myNumItems = 0;
 }//clear
 
 //search the array for a specific value and return the place in the array the item is located, if it is not there
 //return -1
 private int findIndex(String keyValue)
 {
	//declare variable to store answer
	int answer = -1;
	int j = 0;
	//search the array 
	  for(j = 0; j < myNumItems; j++) 
	  {
	    if(myItems[j].getName().equalsIgnoreCase(keyValue))
		  answer = j;
	  }//for
    return answer;
 }//findIndex
 
 //adds an item into the array if it is not already there and the array is not full
 public boolean add(ItemMurray product)
 {
	//declare variables 
	boolean added = false;
	String productName = product.getName();
	if(findIndex(productName) == -1 && myNumItems < 20)
	{
	  added = true;
	  myItems[myNumItems]= product;
	  myNumItems++;
	}//if
	
	return added;
 }//add
 
 //removes an item from the array if it is found. If it is not found, tell the user
 public boolean remove(String keyValue)
 {
   //declare variables
   boolean removed = false; 
   int pos = findIndex(keyValue);
   if(pos != -1)
   {
	 removed = true;
	 System.out.println(myItems[pos].getName() + " item has been removed from the cart");
	 System.out.println("");
     while(pos < myNumItems - 1)
     {
    	   myItems[pos] = myItems[pos +1];
    	   pos++;
     }//while
     myNumItems--;
   }//if
   else System.out.println("Sorry, " + myItems[pos].getName() + "could not be removed");
        System.out.println("");
   
   return removed;
 }//remove
 
 //finds a user-specified item and returns if the value was found or not
 public ItemMurray retrieve(String keyValue)
 { 
	//declare variable
	ItemMurray answer = null;
	int spot = findIndex(keyValue);
	
	if(spot != -1)
	  answer = myItems[spot];
	return answer;
 }//retrieve
 
 //checks if the array is empty
 public boolean isEmpty()
 {
   boolean answer = false;
   if(myNumItems == 0)
	 answer = true;
   
   return answer;
 }//isEmpty
 
 //checks if the array is full
 public boolean isFull()
 {
   boolean answer = false; 
   if(myItems.length == myNumItems)
	 answer = true;
   
   return answer;
 }//isFull
 
 //prints out all of the details
 public void print()
 {
   //declare variables
   int i = 0;
   int k = 0;
   ItemMurray currentItem;
   String currentName = "";
   
   //loop through the array
   for(i = 0; i < myNumItems; i++)
   {
	 currentItem = myItems[i];
	 currentName = currentItem.getName();
	 //make the switches
     for (k = i-1; k >= 0 && myItems[k].getName().compareToIgnoreCase(currentName) > 0; k--)
     {
	  myItems[k+ 1] = myItems[k];
     }//for k   
	 myItems[k+1] = currentItem;
   }//for i 
   for(int j = 0; j < myNumItems; j++)
   {
   System.out.println("Item " + (j+1) + ":");
   myItems[j].printDetails();
   }//for j 
 }//print
 
 //counts the number of items by adding up the quantities of each item
 public int getCount()
 {
	//declare variables
	int quantitySum = 0;
	  
	for(int i = 0; i < myNumItems; i++)
	  quantitySum += myItems[i].getQuantity();
	
	return quantitySum;
 }//getCount
 
 //calculates the total sum due by multiplying the quantity and price of each item
 public double calcTotal()
 {
	//declare variables
	double totalSum = 0.0;
	double itemPrice = 0.0;
	
	for(int j = 0; j < myNumItems; j++)
	{
      itemPrice = myItems[j].getQuantity() * myItems[j].getPrice();
      totalSum += itemPrice;
	}//for
	
	return totalSum;
 }//calcTotal
}//KeyedListMurray

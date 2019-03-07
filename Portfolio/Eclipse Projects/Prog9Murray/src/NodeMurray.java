//Jordan Murray
//Prog 8
//Due Date and Time: 04/05/18 before 1:30PM
//
//Description: This program will start off by asking the user for the name of a file. It will read in 
//the data in the file and add the items to the list. After it has done that, it will print a menu that
//allows the user to either add an item to the list, delete an item from the list, print each item in the
//list, search for a specific item in the list, count the total number of items in the list, total the
//cost of all the items in the list, determine whether the list is empty, clear the list, or Quit. 
//Depending on what the user chooses, the appropriate method in either the ItemMurray class or the 
//KeyedListMurray class will be called. 
//

public class NodeMurray 
{
  //declare variables
  private ItemMurray myData;
  private NodeMurray myNext;
  
  public NodeMurray()
  {
	myData = null;
	myNext = null;
  }//null constructor
  
  public NodeMurray(ItemMurray newData)
  {
	myData = newData;
	myNext = null;
  }//full constructor
  
  public ItemMurray getData()
  { 
	return myData;
  }//getData
  
  public NodeMurray getNext()
  {
	return myNext;  
  }//getNext
  
  public void setData(ItemMurray newData)
  {
	myData = newData; 
  }//setData
  
  public void setNext(NodeMurray newNext)
  {
	myNext = newNext; 
  }//setData
}//NodeMurray

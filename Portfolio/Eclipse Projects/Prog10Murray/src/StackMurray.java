//
//Jordan Murray
//Prog 10
//Due Date and Time: 04/30/18 before 1:30PM
//
//Description: This class has 1 instance variable of the type NodeMurray called myHead. The 
//methods include a null constructor, isEmpty, isFull, push, and pop. IsEmpty determines if
//a stack is empty, is full checks if it is full, push adds a card to the stack, and pop removes
//a card from the stack.
//

public class StackMurray 
{
  private NodeMurray myHead;
  
  public StackMurray()
  {
	myHead = null;
  }//StackMurray null constructor
  
  //determines if the stack is empty, returns true if it is
  public boolean isEmpty()
  {
	boolean ans = false;
	if(myHead == null)
	  ans = true;
	
	return ans;
  }//isEmpty
  
  //determines if the stack is full, returns false regardless (linked list implementation)
  public boolean isFull()
  {
	return false;
  }//isFull
  
  //adds an card to the top of the stack, returns true if successful
  public boolean push(CardMurray value)
  {
	boolean pushed = false;
	if(!isFull())
	{
	  pushed = true;
	  NodeMurray newGuy = new NodeMurray();
	  newGuy.setData(value);
	  newGuy.setNext(myHead);
	  myHead = newGuy;
	}//if
	
	return pushed;
  }//push
  
  //removes a card from the stack, returns the card that was removed or popped from the top of 
  //the stack
  public CardMurray pop()
  {
	CardMurray ans = null;
	if(!isEmpty())
	{
	  ans = myHead.getData();
	  myHead = myHead.getNext();
	}//if
	
	return ans; 
  }//pop
}//StackMurray

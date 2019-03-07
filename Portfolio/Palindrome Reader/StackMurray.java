//
//Jordan Murray
//Description: This class has 1 instance variable of the type NodeMurray called myHead. The
//methods include a null constructor, isEmpty, peek, traverse, push, and pop. IsEmpty determines if
//a stack is empty, peek gets the value of the top of the stack, traverse gets the value of every Node
//in the linked list, push adds a card to the stack, and pop removes a card from the stack.

public class StackMurray{
  private NodeMurray myHead;

  //null constructor
  public StackMurray(){
	  myHead = null;
  }

  //determines if the stack is empty, returns true if it is
  public boolean isEmpty(){
	  boolean ans = false;
	  if(myHead == null)
	   ans = true;

	  return ans;
  }

  //gets the value of the top of the stack, without popping the node
  public char peek(){
    if(!isEmpty()){
      return myHead.getData();
    }else{
      return ' ';
    }
  }

  //gets the data value of every node in the linked list. Used a lot for debugging
  public void traverse(){
    NodeMurray currentNode = myHead;
    while(currentNode !=null){
      System.out.print(currentNode.getData());
      currentNode = currentNode.getNext();
    }
    System.out.println();
  }

  //adds a letter to the top of the stack, returns true if successful
  public void push(char value){
  	  NodeMurray newGuy = new NodeMurray();
  	  newGuy.setData(value);
  	  newGuy.setNext(myHead);
  	  myHead = newGuy;
  }

  //removes a char from the stack, returns the char that was removed or popped from the top of
  //the stack
  public char pop(){
	  char ans = 0;
	  if(!isEmpty()){
	    ans = myHead.getData();
	    myHead = myHead.getNext();
	  }
	  return ans;
  }
}

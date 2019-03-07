//
//Jordan Murray
//Description: This class has 2 instance variable of the type NodeMurray called myHead and myTail. The
//methods include a null constructor, isEmpty, peek, traverse, enqueue, and dequeue. IsEmpty determines if
//a stack is empty, peek gets the value of the head of the queue, traverse gets the value of all of the nodes
//in the linked list, enqueue adds a String to the stack, and dequeue removes a String from the Queue.

public class QueueMurray{
  private NodeMurray myHead;
  private NodeMurray myTail;

  //null constructor
  public QueueMurray(){
  	myHead = null;
    myTail = null;
  }

  //determines if the Queue is empty, returns true if it is
  public boolean isEmpty(){
	  boolean ans = false;
	  if(myTail == null)
	    ans = true;

	  return ans;
  }

  //gets the value of the head of the queue, without dequeueing the node
  public char peek(){
    if(!isEmpty()){
      return myHead.getData();
    }else{
      return ' ';
    }
  }

  //gets the value of all of the nodes in the queue, used a lot for debugging purposes
  public void traverse(){
    NodeMurray currentNode = myHead;
    while(currentNode !=null){
      System.out.print(currentNode.getData());
      currentNode = currentNode.getNext();
    }
    System.out.println();
  }

  //adds an char to the end of the Queue, returns true if successful
  public void enqueue(char value){
    NodeMurray newGuy = new NodeMurray();
    newGuy.setData(value);
    if(isEmpty()){
	    myHead = newGuy;
      myTail = newGuy;
    }else{
      myTail.setNext(newGuy);
      myTail = newGuy;
    }
  }

  //removes a char from the Queue, returns the char that was removed or dequeued from the front of
  //the queue
  public char dequeue(){
	  char ans = myHead.getData();
	  myHead = myHead.getNext();
      if(myHead == myTail){
        myTail = null;
      }
	  return ans;
  }
}

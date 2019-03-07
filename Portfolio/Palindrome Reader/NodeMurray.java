//
//Jordan Murray
//Description: This class has two instance variables: myData which is of the type String and
//myNext which is of the type NodeMurray. There is a null constructor and a full constructor which
//only takes in a CardMurray newData. There is also a getter and setter for each of the instance
//variables.
//

public class NodeMurray{
  //declare variables
  private char myData;
  private NodeMurray myNext;

  //null constructor
  public NodeMurray(){
	  myData = 0;
	  myNext = null;
  }

  //full constructor
  public NodeMurray(char newData){
    myData = 0;
    myNext = null;
  }

  //return myData
  public char getData(){return myData;}

  //return myNext
  public NodeMurray getNext(){return myNext;}

  //set myData
  public void setData(char newData){myData = newData;}

  //set myNext
  public void setNext(NodeMurray newNext){myNext = newNext;}
}

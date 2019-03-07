//
//Jordan Murray
//Prog 8
//Due Date and Time: 04/05/18 before 1:30PM
//
//Description:This program consists of three instance variables: myName, myQuantity, and myPrice. 
//There is a full and a null constructor along with all of the getters and setters for the instance
//variables. The getters and setters are called in other classes and used to set values, store them, 
//and return them so the entered values can be called on later. The last method is print details 
//which uses all of the instance variables data and prints it out in a user friendly way. 
//

public class ItemMurray {
  //declare the variables
  private String myName;
  private int myQuantity;
  private double myPrice;
  
  //full constructor
  public ItemMurray(String newName, int newQuantity, double newPrice)
  {
    myName = newName;
    	myQuantity = newQuantity;
    myPrice = newPrice;
  }//ItemMurray full constructor
  
  //null constructor
  public ItemMurray()
  {
	myName = "";
	myQuantity = 0;
	myPrice = 0.00;
  }//ItemMurray null constructor
  
  //set Name
  public void setName(String newName)
  {
	myName = newName;  
  }//setName
  
  //set quantity
  public void setQuantity(int newQuantity)
  {
	myQuantity = newQuantity;  
  }//setQuantity
  
  //set price
  public void setPrice(double newPrice)
  {
	myPrice = newPrice;  
  }//setPrice
  
  //return name
  public String getName()
  {
	return myName;  
  }//getName
  
  //return Quantity
  public int getQuantity()
  {
	return myQuantity;  
  }//getQuantity
  
  //return Price
  public double getPrice()
  {
	return myPrice;  
  }//getPrice
  
  //print out the details
  public void printDetails()
  {
	System.out.println("Item Name: " + myName);  
	System.out.println("Quantity: " + myQuantity);  
	System.out.println("Price: " + myPrice);  
  }//printDetials
}//ItemMurray

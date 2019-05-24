//Jordan Murray
//This class stores all of the information of a specific spice. It stores the color, the price, the quantity, and the
//quanitity taken. The quantity taken helps with the fractional knapsack portion because it is possible for some, but
//not all of the spice to be taken. Color is a string, price is a double and quanitity and quantity taken are integers.
// The methods of this class are a null constructor, full constructor, and the getters and setters for all of the
//instance variables.

public class SpiceMurray{
  //declare variables
  private String color;
  private double price;
  private int quantity;
  private int qTaken;

  //null constructor
  public SpiceMurray(){
	  color = "";
    price = 0;
    quantity = 0;
    qTaken = 0;
  }

  //full constructor
  public SpiceMurray(String newColor, double newPrice, int newQuantity, int newQTaken){
    color = newColor;
    price = newPrice;
    quantity = newQuantity;
    qTaken = newQTaken;
  }

  //return the color
  public String getColor(){return color;}

  //return the price
  public double getPrice(){return price;}

  //return the Quantity of the spice
  public int getQuantity(){return quantity;}

  //return the quantity taken
  public int getQTaken(){return qTaken;}

  //set the color
  public void setColor(String newColor){color = newColor;}

  //set the price
  public void setPrice(double newPrice){price = newPrice;}

  //set the quantity
  public void setQuantity(int newQuantity){quantity = newQuantity;}

  //set the quantity taken of the spice
  public void setQTaken(int newQTaken){qTaken = newQTaken;}
}

//
//Jordan Murray
//Prog 6
//Due Date and Time: 03/05/18 before 1:30PM
//
//Description: This program consists of getter and setter methods for three variables: width, height, 
//and fill style. There are also three extra methods that calculate the area, perimeter and construct an
//image of what the rectangle looks like. These methods perform when called through the class main in 
//RectangleDemoMurray.java. 

public class Rectangle 
{
  //declare the variables
  private int myWidth;
  private int myHeight;
  private char myFillStyle;
  
  //full constructor
  public Rectangle(int newWidth, int newHeight, char newFillStyle)
  {
	myWidth = newWidth;
	myHeight = newHeight;
	myFillStyle = newFillStyle;
  }//Rectangle full constructor

  //null constructor
  public Rectangle()
  { 
	myWidth = 10;
	myHeight = 5;
	myFillStyle = '*';
  }//Rectangle null constructor
  
  //set width
  public void setWidth(int newWidth)
  {
	myWidth = newWidth;
  }//setWidth

  //set height
  public void setHeight(int newHeight)
  {
	myHeight = newHeight;
  }//setHeight
  
  //set fill style
  public void setFillStyle(char newFillStyle)
  {
	myFillStyle = newFillStyle;
  }//setFillStyle

  //return width
  public int getWidth()
  {
    return myWidth;
  }//getWidth

  //return height
  public int getHeight()
  {
	return myHeight;
  }//getHeight

  //return fill style
  public char getFillStyle()
  {
  return myFillStyle;
  }//getFillStyle

  //calculate and return area
  public int calcArea() 
  {
    //declare variables
	int area = 50; 
	
	//calculate the area
	area = myHeight * myWidth; 
	
	return area;
  }//calcArea

  //calculate and return perimeter 
  public int calcPerimeter()
  {
	//declare variables
	int perimeter = 30;
	
	//calculate the perimeter
	perimeter = (2 * myHeight) + (2 * myWidth);
	
	return perimeter;
  }//calcPerimeter
  
  //construct visual of the rectangle
  public void drawRectangle()
  {
	for(int j = 0; j < myHeight; j++)
	{
	  for(int i = 0; i < myWidth; i++)  
	  {
	    System.out.print(myFillStyle); 
	  }//for
	  System.out.println();
	}//for
  }//drawRectangle
}//Rectangle

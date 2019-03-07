//
//Jordan Murray
//Prog 6
//Due Date and Time: 03/05/18 before 1:30PM
//
//Description: This program prints out a menu that prompts the user. The first three options do not print 
//anything but allow the user to change the width, height, or the fill style of the rectangle. If the user
//chooses one of the next two options it will calculate either the area or the perimeter based on the 
//values that they have entered, or the default values if they were not changed and print out the answer. 
//The option 'T' will print out all of the details of the rectangle such as the width, height, fill style,
//area and perimeter. Option "D' will print out a visual of the rectangle and if the user enters 'Q', then
// they will terminate the program.
//

import java.util.*;
import java.util.Scanner;

public class RectangleDemoMurray {

  public static void main(String args[])
	{
	  Scanner keyboard = new Scanner(System.in);
	    
      //declare variables
	  char userChoice = 0;
	  String fake = " ";
	  int width = 10;
	  int height = 5;
	  int area = 50;
	  int perimeter = 30;
	  String fake2 = "";
	  char fillStyle = '*';
	  Rectangle box = new Rectangle();
	  
	  //prompt the user with the menu
	  do
	  {
	    //prompt the user
		System.out.println("Menu: Enter one of the following options");
		System.out.println("W: Assign the Width");
		System.out.println("H: Assign the Height");
		System.out.println("F: Assign the Fill Style");
		System.out.println("A: Calculate the Area");
		System.out.println("P: Calculate the Perimeter");
		System.out.println("T: Text Description of the Rectangle");
		System.out.println("D: Draw the Rectangle");
		System.out.println("Q: Quit");
		fake = keyboard.next();
		userChoice = fake.charAt(0);
	  }//do
	  while (userChoice != 'W' && userChoice != 'w' && userChoice != 'H' && userChoice != 'h' &&
			 userChoice != 'F' && userChoice != 'f' && userChoice != 'A' && userChoice != 'a' &&
			 userChoice != 'P' && userChoice != 'p' && userChoice != 'T' && userChoice != 't' &&
			 userChoice != 'D' && userChoice != 'd' && userChoice != 'Q' && userChoice != 'q');
	 
	  
	  while(userChoice != 'Q' && userChoice != 'q')
	  {
		switch(userChoice)  
		{
		  case 'w':
		  case 'W':
			//ask the user for a new width
			do {
			  System.out.println("Enter the new Width");
			  width = keyboard.nextInt();
			}//do
			while(width <= 0);
			
			//set the new width
			box.setWidth(width);
		  
		    break;  
		  case 'h':
		  case 'H':
			//ask the user for a new height
			do {
			  System.out.println("Enter the new Height");
			  height = keyboard.nextInt();
			}//do
			while(height <= 0);
				
			//set the new width
			box.setHeight(height);
			
			break;	
		  case 'f':
		  case 'F':
			//ask the user for a new fill style
			  System.out.println("Enter the new Fill Style");
			  fake2 = keyboard.next();
			  fillStyle = fake2.charAt(0);
			
			//set the new width
			box.setFillStyle(fillStyle);
			
			break;	
		  case 'a':
		  case 'A'://get and print the area
			box.calcArea();
		    System.out.println(area);
		    break;
		  case 'p':
		  case 'P': //get and print the perimeter
			box.calcPerimeter();
		    System.out.println(perimeter);
		    break;
		  case 't':
		  case 'T'://print information about the box
			System.out.println("The Width of the rectangle is: " + box.getWidth());
			System.out.println("The Height of the rectangle is: " + box.getHeight());
			System.out.println("The Fill Style of the rectangle is: " + box.getFillStyle());
			System.out.println("The Area of the rectangle is: " + box.calcArea());
			System.out.println("The Perimeter of the rectangle is: " + box.calcPerimeter());
			
			break;
		  case 'd':
		  case 'D'://draw the box
			box.drawRectangle();
		}//switch
		
		//prompt the user again
		System.out.println("Menu: Enter one of the following options");
		System.out.println("W: Assign the Width");
		System.out.println("H: Assign the Height");
		System.out.println("F: Assign the Fill Style");
		System.out.println("A: Calculate the Area");
		System.out.println("P: Calculate the Perimeter");
		System.out.println("T: Text Description of the Rectangle");
		System.out.println("D: Draw the Rectangle");
		System.out.println("Q: Quit");
		fake = keyboard.next();
		userChoice = fake.charAt(0);
	  }//while
  }//main
}//RectangleDemoMurray

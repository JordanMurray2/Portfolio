//
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

import java.text.DecimalFormat;
import java.util.*;
import java.util.Scanner;
import java.io.*;

public class ShoppingDemoMurray 
{
	public static void main(String[] args)
	{
	  Scanner keyboard = new Scanner(System.in);
	  
	  //declare variables
	  int choice = 0;
	  ItemMurray newItem = null;
	  String name = "";
	  int quantity = 0;
	  double price = 0.0;
	  String item = "";
	  KeyedListMurray newList = new KeyedListMurray(); 
	  String find = "";
	  double totalCost = 0.00;
	  String fileName = "";
	  int fileItems = 0;
	  DecimalFormat moneyStyle = new DecimalFormat("$0.00");
	  
	  //ask the user for the path and name for the data file
	  System.out.print("Please enter the file name: ");
	  fileName = keyboard.next();
	  
	  //create reference to the file
	  File myFile = new File(fileName);
	  try
	  {
		//Create a second Scanner object to read the file
	    Scanner input = new Scanner(myFile);
	    
	    //read number of items that will follow in the file
	    fileItems = input.nextInt();
	    
	    for(int i = 0; i < fileItems; i++)
	    {
	    	  newItem = new ItemMurray();
	    	  
	    	  //read in and set name
	    	  name = input.next();
	    	  newItem.setName(name);
	    	  
	    	  //read in and set quantity
	    	  quantity = input.nextInt();
	    	  newItem.setQuantity(quantity);
	    	  
	    	  //read in and set the price
	    	  price = input.nextDouble();
	    	  newItem.setPrice(price);
	    	  
	    	  //add the items to the keyed list
	    	  if(newList.add(newItem))
		  {
			System.out.println(newItem.getName() + " has been added to the cart");	
		    System.out.println("");
		  }//if
		  else 
		  {
			System.out.println("Sorry, " + newItem.getName() + " could not be added");
		    System.out.println("");
		  }//else     
	    }//for
	    input.close();
	  }//try
	  
	  //if the file was not found
	  catch(FileNotFoundException ex)
	    {
	      System.out.println("Failed to find file: " + myFile.getAbsolutePath()); 
	    }//catch
	  
	  //if the number type did not match
	  catch(InputMismatchException ex)
	    {
	    	  System.out.println("Type mismatch for the number I just tried to read.");
	      System.out.println(ex.getMessage());
	    }//catch
	  
	  //if a string was entered where an integer or double should go
	  catch(NumberFormatException ex)
	  {
	    System.out.println("Failed to convert String text into an integer value.");
	    System.out.println(ex.getMessage());
	  }//catch
	  
	  //if an object is referencing a null value
	  catch(NullPointerException ex)
	  {
	    System.out.println("Null pointer exception.");
	    System.out.println(ex.getMessage());
	  }//catch
	    
	  //the last Catch if problem cannot be identified
	  catch(Exception ex)
	  {
	    	System.out.println("Something went wrong");
	      ex.printStackTrace();
	  }//catch 
	  
	  //continue with regular menu and keyboard entry
	  do
	  {
		 //prompt the user with menu
	     System.out.println("Menu: Enter one of the following options");
		 System.out.println("1. Add an item to the list");
		 System.out.println("2. Delete an item from the list");
		 System.out.println("3. Print each item in the list");
		 System.out.println("4. Search for a user-specified item in the list");
		 System.out.println("5. Count the total number of items in the list");
		 System.out.println("6. Total the cost of the items in the list");
		 System.out.println("7. Determine whether the list is empty");
		 System.out.println("8. Clear the list");
		 System.out.println("0. Quit");
		 choice = keyboard.nextInt();	
	   }//do
	   while (choice != 1 && choice != 2 && choice != 3 && choice != 4 &&
		      choice != 5 && choice != 6 && choice != 7 && choice != 8 &&
	          choice != 0);
	  
	    while (choice != 0)
	    {
		switch(choice)  
		{
		  case 1:
		    newItem = new ItemMurray();
		    //prompt the user for the name of the Item
			System.out.println("Enter the name of the Item");
			  name = keyboard.next();
			//set the title
			newItem.setName(name);
				
			//prompt the user for the quantity
			System.out.println("Enter the quantity");
			  quantity = keyboard.nextInt(); 	 
			//set the quantity
			newItem.setQuantity(quantity);
				
			//prompt the user for the price
			System.out.println("Enter the price of the Item");
			  price = keyboard.nextDouble();
			//set the quantity
			newItem.setPrice(price);
			
			//add new Item to the list if possible
			  if(newList.add(newItem))
			  {
			    System.out.println(newItem.getName() + " has been added to the cart");	
			    System.out.println("");
			  }//if
			else 
			{
				System.out.println("Sorry, " + newItem.getName() + " could not be added");
				System.out.println("");
			}//else     
			break;
			
		  case 2:
			//prompt the user for an item to delete
			System.out.println("Enter the Item you would like to delete");
			  item = keyboard.next();
			
		    //delete the Item
		    newList.remove(item);
		    
			break;
		  case 3:
		    if(newList.getMyNumItems() != 0)
			    newList.print();
		    else System.out.println("Sorry, there are no Items in the cart, please add Items");
			break;
		  
		  case 4:
			if(newList.getMyNumItems() != 0)
			{
			  System.out.println("Enter the Item you would like to find in your cart");
			    find = keyboard.next();
			  if(newList.retrieve(find) != null)	
			  {
			    newList.retrieve(find).printDetails();	
			  }//if
			  else System.out.println("The item you are looking for is not in your cart");
			}//if
			else System.out.println("There are no Items in your cart");
		    break;
		    
		  case 5:
			if(newList.getMyNumItems() != 0)
			{
		      System.out.println("There is " + newList.getCount() + " Items in your cart."); 
			}//if
			else System.out.println("There are no Items in your cart");
		    break;
		    
		  case 6:
			totalCost = newList.calcTotal();
		    System.out.println("The total cost is " + moneyStyle.format(totalCost)); 
		    break;
		    
		  case 7:
			if(newList.isEmpty())
			{
			  System.out.println("The cart is empty, please add Items");	
			}//if
			else System.out.println("The cart is not empty");
		    break;
		    
		  case 8:
			  newList.clear();
			  System.out.println("The cart has been cleared.");
			  
		  default: 
			  System.out.println("Please enter one of the values");
		 }//switch
		
		  //prompt the user again
		  System.out.println("Menu: Enter one of the following options");
		  System.out.println("1. Add an item to the cart");
		  System.out.println("2. Delete an item from the cart");
		  System.out.println("3. Print each item in the cart");
		  System.out.println("4. Search for a user-specified item in the cart");
		  System.out.println("5. Count the total number of items in the cart");
		  System.out.println("6. Total the cost of the items in the cart");
		  System.out.println("7. Determine whether the cart is empty");
		  System.out.println("8. Clear the cart");
		  System.out.println("0. Quit");
		  choice = keyboard.nextInt();
	    }//while
	    System.out.println("Goodbye!"); 
   }//main
}//ShoppingDemoMurray

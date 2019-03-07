//
//Jordan Murray
//Prog 4
//Due Date and Time: 2/19/18 before 1:30
//
//This program consists of multiple methods. The first method main asks the user for their id number, their name,
//and the number of movies that they ordered. It then calls on the other methods to do other calculations. The next 
//method will ask the user for the length of the movie with the corresponding rate so that it can calculate the cents
//per minute and returns the total cost of all of the movies. The next method calculates and returns the service charge.
//The next method adds all of this together and calculates the tax as well. The final method prints out all of the 
//results. Once back in the main method, all of the patients processed and a small summary including the number of 
//customers processed, the customer id with the highest amount with that amount, the same for person with
//the lowest amount, the total amount of all downloads purchased, and the average of the total amount of all downloads 
//purchased.

import java.text.DecimalFormat;
import java.util.*;

public class Prog4Murray 
{
  public static void main(String args[])
  {
	 Scanner keyboard = new Scanner(System.in);
	 
	 //declare variables
	 String customerName = "none";
	 int customerIdNumber = 0;
	 int orderedMovies = 0;
	 double allMovieCost = 0.00;
	 double serviceChargeCost = 0.00;
	 double totalDue = 0.00;
	 int numberOfCustomers = 0;
	 double maxAmount = Double.MIN_VALUE;
	 int maxId = 0;
	 double minAmount = Double.MAX_VALUE;
	 int lowId = 0;
	 double allDownloadSum = 0.00;
	 double average = 0.00;
	 DecimalFormat moneyStyle = new DecimalFormat("$0.00");
	 
	 //verify Id number
	 do 
	 { 
	   System.out.println("Enter your Customer Id Number or 0 (zero) to quit");
	   customerIdNumber = keyboard.nextInt();
	 } //do
	 while ((customerIdNumber < 25000 || customerIdNumber > 99999) && (customerIdNumber !=0));
	 
	 //loop to get information for multiple customers
	 while(customerIdNumber != 0)
	 {
	   //ask customer for name
	   System.out.println("Enter your name");
	   customerName = keyboard.next();
	 
	   //verify number of movies ordered
	   do 
	   {
	     System.out.println("Enter the Number of Movies Ordered");
	     orderedMovies = keyboard.nextInt(); 
	   }//do
	   while (orderedMovies <= 0);
	 
	   //get the total cost of all the movies
	   allMovieCost = chooseMovies(orderedMovies);
	 
	   //get the service charge
	   serviceChargeCost = calcServiceCharge(orderedMovies, allMovieCost);
	 
	   //figure out the total due
	   totalDue = calcTotalDue(allMovieCost, serviceChargeCost);
	 
	   //output the results
	   outputResults(customerName, customerIdNumber, orderedMovies, allMovieCost, serviceChargeCost, totalDue);
	   
	   //figure out the highest amount so far with the Id associated with that amount
	   if (maxAmount < totalDue) 
	   {
	    	 maxAmount = totalDue;
	     maxId = customerIdNumber;
	   }//if
	   
	    //figure out the lowest amount so far with the Id associated with that amount
	    if (minAmount > totalDue)
	    {
	    	  minAmount = totalDue;
	    	  lowId = customerIdNumber;
	    }//if
	    
	    //calculate the total amount of all downloads purchased
	    allDownloadSum += totalDue;

	   //increase number of customers
	   numberOfCustomers++;
	   
	   //prompt user for another Id
	   do 
		 { 
		   System.out.println("Enter your Customer Id Number or 0 (zero) to quit");
		   customerIdNumber = keyboard.nextInt();
		 } //do
		 while ((customerIdNumber < 25000 || customerIdNumber > 99999) && (customerIdNumber != 0));
	 }//while
	 
	 if (minAmount == Double.MAX_VALUE)
		 minAmount = 0.00;
	 
	 //calculate the average
	 if (numberOfCustomers != 0)
	      average = allDownloadSum / numberOfCustomers;
	    else average = 0.00;
	 
	 //print out summary
	 System.out.println("Summary:");
	 System.out.println("Number of Customers Processed: " + numberOfCustomers);
	 System.out.println("Highest Amount: " + moneyStyle.format(maxAmount));
	 System.out.println("Customer Id with Highest Amount: " + maxId);
	 System.out.println("Lowest Amount: " + moneyStyle.format(minAmount));
	 System.out.println("Customer Id with Lowest Amount: " + lowId);
	 System.out.println("Total amount of all downloads purchased: " + moneyStyle.format(allDownloadSum));
	 System.out.println("Average of all Purchase Amounts: " + moneyStyle.format(average));	 
	 
  }//main
  
  public static double chooseMovies(int numMovies)
  {
   Scanner keyboard = new Scanner(System.in);  
   
     //declare variables
     int movieLength = 0;
     String fake;
     char movieRate = '0';
     double centsPerMinute = 0.00;
     double movieCost = 0.00;
     double movieCostSum = 0.00;
     int i = 0;
     int count = 0;
     
     //loop for multiple movies
     for (i = numMovies; i > 0; i--) {
   
     //verify the length of the movie
     do 
	   {
	     System.out.println("Enter the length of the Movie Number " + (count + 1) + " in minutes");
	     movieLength = keyboard.nextInt();   
	   }//do
	 while (movieLength < 1 || movieLength > 240);
    	
     //verify the rating of the movie
     do 
     {
	  System.out.println("Enter the rating of the movie: G-rated(G),PG-rated(P), R-rated(R), X-rated(X), Other(O)");
	  fake = keyboard.next();
	  movieRate = fake.charAt(0);
     }//do
     while (movieRate !='G' && movieRate != 'P' && movieRate != 'R' && movieRate != 'X' && movieRate != 'O' &&
		   movieRate != 'g' && movieRate != 'p' && movieRate != 'r' && movieRate != 'x' && movieRate != 'o');
   
     //figure out the cents per minute based on the rating
     switch(movieRate) {
       case 'g':
       case 'G': centsPerMinute = .039;
          break;
       case 'p':
       case 'P': centsPerMinute = .054;
          break;
       case 'r':
       case 'R': centsPerMinute = .068;
          break;
       case 'x':
       case 'X': centsPerMinute = .273;
          break;
       case 'o':
       case 'O': centsPerMinute = .04;    
     }//switch
   
     //calculate and the movie cost
     movieCost = movieLength * centsPerMinute;
   
     //calculate the sum of all the movies so far
     movieCostSum += movieCost;
  
     count++;
     }//for
   
     //return the total sum of all the movies
     return movieCostSum; 
  }//chooseMovies
	 
  public static double calcServiceCharge(int numMovies, double movieCost )
  { 
	
	//declare the variables
	double serviceCharge = 0.00;
	
	//calculate the service charge based on the number of movies
	if(numMovies >= 1 && numMovies <= 3)
		serviceCharge = movieCost * .18;
	else if (numMovies >= 4 && numMovies <= 7)
		serviceCharge = movieCost * .15;
	else if (numMovies >=8 && numMovies <= 11)
		serviceCharge = movieCost * .11;
	else serviceCharge = movieCost * .05;
	
	//return the service charge
	return serviceCharge;
	
  }//calcServiceCharge
  
  public static double calcTotalDue(double movieCost, double serviceCharge)
  {
	  //declare variables
	double amountDue = 0.00;
	double tax = 0;
	double totalAmountDue = 0.00;
	
	//calculate the amount due
	amountDue = movieCost + serviceCharge;
	
	//calculate tax
	tax = amountDue * .07;
	
	//calculate the total amount due
	totalAmountDue = amountDue + tax; 
	
	//return the total amount due
	return totalAmountDue;
  }//calcTotalDue
  
  public static void outputResults(String customerName, int customerIdNumber, int numMovies, double movieCost, double serviceCharge, double totalAmountDue)
  {
	//declare variable
	DecimalFormat moneyStyle = new DecimalFormat("$0.00");
	
	//print the results
	System.out.println("Customer Name: " + customerName);
	System.out.println("Customer Id Number: " + customerIdNumber);
	System.out.println("Number of Movies Purchased: " + numMovies);
	System.out.println("Cost of the Movies: " + moneyStyle.format(movieCost));
	System.out.println("Service Charge: " + moneyStyle.format(serviceCharge));
	System.out.println("Total Amount Due: " + moneyStyle.format(totalAmountDue));
  }//outputResults
}//Prog4Murray
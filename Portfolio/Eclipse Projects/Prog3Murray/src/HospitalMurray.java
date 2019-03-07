//
//Jordan Murray
//Prog 3
//Due Date and Time: 02/12/18 before 1:30PM
//This program will take input from a user such as their patient ID number, their household income, 
//and the number of days they stayed at the hospital. Once the input has been stored in a variable, switch 
//statements are used to find the according Per Diem Rate based on the input given. A few calculations are 
//made and then the program prints all of the input and the basic information a patient would need to see,
//including the total bill. The program will then ask for another patient id and loop until the user enters 0.
//Then it will print a summary including the number of patients processed, highest bill amount along with the 
//patient id associated with the highest bill, the total amount of bills processed, and the average bill amount.
//

import java.util.Scanner;
import java.text.*;

public class HospitalMurray 
{
  public static void main(String args[])
  {
    Scanner keyboard = new Scanner(System.in);
	 
  //establish variables
  double admittanceFee = 500.00;
  double serviceFee = 0.00;
  double discount = 0.00;
  double perDiemRate = 0.00;
  int weeks = 0;
  DecimalFormat moneyStyle = new DecimalFormat("$0.00");
  String Insurance;
  String fake;
  double max = 0.00;
  double highestBill = 0.00;
  int maxId = 0;
  int numberOfPatients = 0;
  int billsProcessed = 0;
  double billSum = 0.00;
  double averageBillAmount = 0.00;
  
  //get user input
  int patientId = 0;
  	System.out.println("Enter your Patient ID number or 0 (zero) to quit");
  	patientId = keyboard.nextInt();

 //loop this code to enter multiple patients at once until 0 is entered.
  while (patientId != 0) {	
	
  double householdIncome = 0;
  //verify that household income is not negative
  do {
  	System.out.println("Enter your household Income");
  	householdIncome = keyboard.nextDouble();
  	}//do
  while (householdIncome <= 0);
  	
  char insurancePlan = '0';
  //verify the insurance plan
  do {
  	System.out.println("Enter your Insurance Plan: Blue Plus = B, Med-Health = M, Health Plan = H, No Insurance = N");
  	fake = keyboard.next();
  	insurancePlan = fake.charAt(0);
    }//do
  while (insurancePlan !='B' && insurancePlan != 'M' && insurancePlan != 'H' && insurancePlan != 'N' &&
		 insurancePlan != 'b' && insurancePlan != 'm' && insurancePlan != 'h' && insurancePlan != 'n');
  	
  int numberOfDays = 0;
  do {
  	System.out.println("Enter the number of days you have been at the hospital (1-365) ");
  	numberOfDays = keyboard.nextInt();
  }//do
  while(numberOfDays < 1 || numberOfDays > 365);
  	
  	//start switch to figure out Per Diem Rate
    switch (insurancePlan) {
      case 'b':
      case 'B': if (householdIncome < 15000) { 
    	  	  perDiemRate = 50;
      	}//if
        else    if (householdIncome >= 15000 && householdIncome <=67500) {
    	  	  perDiemRate = 85;
        }//if
        else perDiemRate = 150;
      	Insurance = "Blue Plus";
        break;
      
      case 'm':
      case 'M': if (householdIncome < 20000) {
    		  perDiemRate = 65;
      	}//if
        else if (householdIncome >= 20000 && householdIncome <= 75000) {
    	      perDiemRate = 100;
        	}//if
    	    else perDiemRate = 200;
      	Insurance = "Med-Health";
        break;
        
      case 'h':
      case 'H': if (householdIncome < 17500) {
    	    perDiemRate = 55;
    	    }//if
        else if (householdIncome >= 17500 && householdIncome <= 63000) {
    		  perDiemRate = 90;
        }//if
        else perDiemRate = 150;
        Insurance = "Health Plan";
      	break;
      	
      case 'n': 
      case 'N': 
    	  default: perDiemRate = 500;
      	Insurance = "none";
    	  }//Switch
    
    //Calculate the service fee
    serviceFee = numberOfDays * perDiemRate;
    
    //calculate number of weeks 
    weeks = numberOfDays / 7; 
    
    //Calculate if a discount is needed
    if (numberOfDays > 25) 
      discount = weeks * 300; 
    else discount = 0;
    
    //add up the total bill
    double totalBill = admittanceFee + serviceFee - discount;
    
    //Print out all of the information
    System.out.println("Patient ID number: " + patientId);
    System.out.println("Household Income: " + moneyStyle.format(householdIncome));
    System.out.println("Insurance Plan: " + Insurance);
    System.out.println("Number of Days: " + numberOfDays);
    System.out.println("Admittance Fee: " + moneyStyle.format(admittanceFee));
    System.out.println("Per Diem Rate: " + moneyStyle.format(perDiemRate));
    System.out.println("Service Fee: " +  moneyStyle.format(serviceFee));
    System.out.println("Discount: " +  moneyStyle.format(discount));
    System.out.println("Total Bill: " +  moneyStyle.format(totalBill));
    
    //figure out the highest bill so far and associate the bill with it
    highestBill = totalBill;
    if (highestBill > max) {
    	max = highestBill;
    maxId = patientId;
    }
    
    //add the sum of the bills processed so far
    billSum += totalBill;
    //increase number of patients 
    numberOfPatients++;
    //increase number of bills processed
    billsProcessed++;
    
    //prompt the user for another id
    System.out.println("Enter another Patient ID number or 0 (zero) to quit");
  	patientId = keyboard.nextInt();
   }//while

    //calculate the average bill amount
    if (billsProcessed != 0)
      averageBillAmount = billSum / billsProcessed;
    else averageBillAmount = 0.00;
    
    //print out a summary
    System.out.println("Summary:");
    System.out.println("Number of Patients Processed: " + numberOfPatients);
    System.out.println("Highest Bill Amount: " + moneyStyle.format(max));
    System.out.println("Patient Id for Hightest Bill Amount: " + maxId);
    System.out.println("Total Amount of All Bills Processed: " + moneyStyle.format(billSum));
    System.out.println("Average Bill Amount: " + moneyStyle.format(averageBillAmount));
  }//main    
}//class


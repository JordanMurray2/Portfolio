//
//Jordan Murray
//Prog 2
//Due Date and Time: 02/05/18 before 1:30PM

//This program will take input from a user such as their patient ID number, their household income, 
//and the number of days they stayed at the hospital. Once the input has been stored in a variable, switch 
//statements are used to find the according Per Diem Rate based on the input given. A few calculations are 
//made and then the program prints all of the input and the basic information a patient would need to see,
//including the total bill.
//

import java.util.Scanner;
import java.text.*;

public class HospitalMurray 
{
  public static void main(String args[])
  {
	 Scanner keyboard = new Scanner(System.in);
  //establish variables
  int admittanceFee = 500;
  int serviceFee = 0;
  int discount = 0;
  int perDiemRate = 0;
  int weeks = 0;
  DecimalFormat moneyStyle = new DecimalFormat("$0.00");
  String Insurance;
  String fake;
  
  //get user input
  int patientId = 0;
  	System.out.println("Enter your Patient ID number");
  	patientId = keyboard.nextInt();
  	
  double householdIncome = 0;
  	System.out.println("Enter your household Income");
  	householdIncome = keyboard.nextDouble();
  	
  char insurancePlan = '0';
  	System.out.println("Enter your Insurance Plan: Blue Plus = B, Med-Health = M, Health Plan = H, No Insurance = N");
  	fake = keyboard.next();
  	insurancePlan = fake.charAt(0);
  	
  int numberOfDays = 0;
  	System.out.println("Enter the number of days you have been at the hospital (1-365) ");
  	numberOfDays = keyboard.nextInt();
  	
  	//start switch to figure out Per Diem Rate
    switch (insurancePlan) {
      case 'b':
      case 'B': if (householdIncome < 15000) { 
    	  	  perDiemRate = 50;
      	}//if
        else if (householdIncome >= 15000 && householdIncome <=67500) {
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
    weeks = numberOfDays % 7; 
    
    //Calculate if a discount is needed
    if (numberOfDays > 25) {
      discount = weeks * 750; 
    }//if
    
    //add up the total bill
    int totalBill = admittanceFee + serviceFee - discount;
    
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
  }//main    
}//class


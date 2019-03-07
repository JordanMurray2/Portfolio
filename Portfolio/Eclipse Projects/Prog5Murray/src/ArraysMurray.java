//
//Jordan Murray
//Prog 5
//Due Date and Time: 2/26/18 before 1:30PM
//
//Description:This program will print a menu that gives the user 4 options. The first option will read in 
//grades and return the highest, lowest, and the average of all the grades. The second option will read in
//positive integers and indicate whether there are more even or odd, or if there is a tie, along with the
//appropriate numbers. The third option will read in negative numbers and a target value. It will then
//calculate how many times the target number was entered in the array. The fourth option is (0) to quit 
//and will then conclude the program
//

import java.util.*;

public class ArraysMurray 
{
  public static void main(String args[])
  {
    Scanner keyboard = new Scanner(System.in);
    
	//declare variables
	int userChoice = 0;
	
	do
	{
	//prompt the user
	System.out.println("Menu: Enter one of the following options");
	System.out.println("1) Handle Grades");
	System.out.println("2) More Evens or More Odds?");
	System.out.println("3) How Many Times?");
	System.out.println("0) Quit");
	userChoice = keyboard.nextInt();
	}//do
	while (userChoice != 1 && userChoice != 2 && userChoice != 3 && userChoice != 0);
	
	while (userChoice !=0) {
	  switch(userChoice)
	  {
	  case 1: handleGrades();
	    break;
	  case 2: evenOrOdd();
	    break;
	  case 3: howManyTimes();
	    break;
	  }//switch
	  
	//prompt the user again
	System.out.println("Menu: Enter one of the following options");
	System.out.println("1)Handle Grades");
	System.out.println("2)More Evens or More Odds?");
    System.out.println("3)How Many Times?");
    System.out.println("0)Quit");
	userChoice = keyboard.nextInt();
	}//while
    	
  }//main
  
  public static void handleGrades()
  {
    Scanner keyboard = new Scanner(System.in); 
    
    //declare variables 
	double[] grades = new double[10];
	int count = 0;
	double highest = 0;
	double lowest = 0;
	double average = 0;
	
	//enter the grades and store them in an array
	for (int i = 0; i < grades.length; i++)
	{
	  System.out.println("Enter grade number " + (count + 1) + " of 10");
	  grades[i] = keyboard.nextDouble();
	  count++; 
	}//for
	
	//print the highest grade
	highest = highestGrade(grades);
	System.out.println("The Highest Grade is: " + highest);
	
	//print the lowest grade
	lowest = lowestGrade(grades);
	System.out.println("The Lowest Grade is: " + lowest);
	
	//print the average grade in the array
	average = averageGrade(grades);
	System.out.println("The Average Grade is: " + average);
	
	System.out.println();
	
  }//handleGrades
  
  public static double highestGrade(double[] maxGrade)
  { 
	//declare variables
	double highGrade = maxGrade[0];
	
	//calculate the highest grade
	for (int i = 0; i < maxGrade.length; i++) 
	{
	  if (maxGrade[i] > highGrade)
	    highGrade = maxGrade[i];
	}//for
	
	//return the highest grade
	  return highGrade;
  }//highestGrade
  
  public static double lowestGrade(double[] minGrade)
  { 
	//declare variables
	double lowGrade = minGrade[0];
	
	//calculate the lowest grade
	for (int i = 0; i < minGrade.length; i++) 
	{
	  if (minGrade[i] < lowGrade)
	    lowGrade = minGrade[i];
	}//for
	
	//return the lowest grade
	  return lowGrade;
  }//lowestGrade
  
  public static double averageGrade(double [] averageGrade)
  {
    //declare variables
	double sum = 0;
	int count = 0;
	double answer = 0;
	
	//compute the average of all the grades
	for(int i = 0; i < averageGrade.length; i++)
	{
	sum += averageGrade[i];
	count++;
	}//for
	if(sum != 0)
	{
	answer = sum / count;	
	}//if
	else answer = 0;
	//return the average
	return answer;
  }//averageGrade
  
  public static void evenOrOdd()
  {
    Scanner keyboard = new Scanner(System.in); 
    
    //declare variables
    int[] posList = new int[20];
    int count = 0;
    int value = 0;
    int evenCount = 0;
    int oddCount = 0;
    
    //enter the grades and store them in an array
    System.out.println("Enter positive integer number " + (count + 1) + 
  	" of 20 (max), or Enter a negative number to stop");
  	value = keyboard.nextInt();
  		
  	//check that its not negative
  	while (value > 0 && count < posList.length) 
  	{
  	 posList[count] = value;
  	 
  	 //check if more even or more odd
  	 if(posList[count] % 2 == 0)
  	   evenCount++; 
  	 else 
  	   oddCount++;
  	 count++;
  	 System.out.println("Enter positive integer number " + (count + 1) + 
  	 " of 20 (max), or Enter a negative number to stop");
  	 value = keyboard.nextInt();
  	}//while
   
  	//go to helper
  	printOddEven(posList, evenCount, oddCount);
  }//evenOrOdd
  
  public static void printOddEven(int[] list, int even, int odd)
  {
    if(even > odd)
    {
    	  //print out even numbers
    	  System.out.println("There were more evens:");
	  for(int i = 0; i < list.length; i++)
	  {
		if(list[i] % 2 == 0 && list[i] != 0)
		{
		System.out.println(list[i]);
		}//if
	  }//for
    }//if
	else if(even < odd)
    {
	  //print out odd numbers
	  System.out.println("There were more odds:");
	  for(int i = 0; i < list.length; i++)
	  {
		if(list[i] % 2 != 0)
		{
		System.out.println(list[i]);
		}//if
	  }//for
    }//if
	else 
	{
	//print out all numbers
	System.out.println("There was a tie!");
	for(int i = 0; i < list.length; i++)
	  if(list[i] != 0)
	    System.out.println(list[i]);
	}//else
	System.out.println();
  }//printOddEvan
  
  public static void howManyTimes()
  {
	  Scanner keyboard = new Scanner(System.in); 
	    
	    //declare variables
	    double[] negList = new double[10];
	    int count = 0;
	    double value = 0;
	    double targetValue = 0;

	    
	    //enter the grades and store them in an array
	    System.out.println("Enter negative number " + (count + 1) + 
	  	" of 10 (max), or Enter a positive number to stop");
	  	value = keyboard.nextDouble();
	  		
	  	//check that its not negative
	  	while (value < 0 && count < negList.length) 
	  	{
	  	  negList[count] = value;
	  	  count++;
	  	System.out.println("Enter negative number " + (count + 1) + 
	  	" of 10 (max), or Enter a positive number to stop");
	  	value = keyboard.nextDouble();
	  	}//while
	 
	  System.out.println("Enter a Target Value");
	  targetValue = keyboard.nextDouble();
	  
	//go to helper
	printHowManyTimes(negList, targetValue);
  }//howManyTimes
  
  public static void printHowManyTimes(double[] myList, double target)
  {
	//declare variables
	int counter = 0;
	
    //print out the array
	System.out.println("Numbers Entered:");
      for(int i = 0; i < myList.length; i++)
      {
		if(myList[i] != 0)
		  System.out.println(myList[i]);
        if (target == myList[i])
        	  counter++;
      }//for
      
	//print the target number
	System.out.println("Target Number: " + target);
	
	//print how many times the target number appears
	System.out.println("The Target Number appeared " + counter + " times");
		
	//print out how many times the target number was entered
	
  }//printHowManyTimes
}//ArraysMurray

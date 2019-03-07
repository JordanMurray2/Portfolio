//
//Jordan Murray
//Prog 1
//Due Date and Time: 1/29/18 before 1:30PM
//
//This program is a modified version of a change maker. It will be able to
//return the appropriate amount of change for larger bills inculding ones, 
//fives, tens, and twenties. 

import java.util.Scanner;
import java.util.*;

public class ChangeMakerMurray 
{
	public static void main(String[] args)
	{
		int originalAmount = 0,
		amount = 0,
	    twenty = 0,
		ten = 0,
		five = 0,
		one = 0,
		quarters = 0, 
		dimes = 0, 
		nickels = 0, 
		pennies = 0;
		
		System.out.println("Enter an amount.");
		System.out.println("I will output a combination of bills and coins");
		System.out.println("that equals that amount of change.");
		
		Scanner keyboard = new Scanner(System.in);
		amount = keyboard.nextInt();
		
		originalAmount = amount;
		twenty = amount/2000;
		amount = amount%2000;
		ten = amount/1000;
		amount = amount%1000;
		five = amount/500;
		amount = amount%500;
		one = amount/100;
		amount = amount%100;
		quarters = amount/25;
		amount = amount%25;
		dimes = amount/10;
		amount = amount%10;
		nickels = amount/5;
		amount = amount%5;
		pennies = amount;
		
		System.out.println(originalAmount + 
		" in change can be given as:");
		System.out.println(twenty + " twenty");
		System.out.println(ten + " ten");
		System.out.println(five + " five");
		System.out.println(one + " one");
		System.out.println(quarters + " quarters");
		System.out.println(dimes + " dimes");
		System.out.println(nickels + " nickels");
		System.out.println(pennies + " pennies");	
	}//main
}//ChangeMakerMurray

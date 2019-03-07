//
//Jordan Murray
//Prog 10
//Due Date and Time: 04/30/18 before 1:30PM
//
//Description: This program begins by calling the deal method. It ask the user for the name of an input file and 
//reads in the list of integers and assigns a character to the integer resembling the card value. After the card items
//have been added to each players play stack, the play method is called and will repeat until there is a winner or
// the game goes over 1,000 plays. The play method calls the compare method which will compare the two players card
//values. It will then call either the winPlay method associated with the correct player or the tie method if the card
//values are equivalent. Once a winner is declared, the print method is called. 
//

import java.io.*;
import java.util.*;

public class BattleDemoMurray 
{
	public static void main(String[] args)
	{
	  //declare variable
	  StackMurray player1PlayStack = new StackMurray();
	  StackMurray player2PlayStack = new StackMurray();
	  StackMurray player1DiscardStack = new StackMurray();
	  StackMurray player2DiscardStack = new StackMurray();
	  int playCount = 0;
	  int numCardsDealt = 0;
	  
	  //call the deal method and set the return value to a variable to be used later
	  numCardsDealt = deal(player1PlayStack, player2PlayStack);
	  
	  //loop the play method until there is a clear winner or the game goes over 1,000 plays
	  while(((countCards(player1PlayStack) + countCards(player1DiscardStack)) != 0) && ((countCards(player2PlayStack) + countCards(player2DiscardStack)) != 0) && playCount < 1000) 
	  {
	  play(player1PlayStack, player2PlayStack, player1DiscardStack, player2DiscardStack); 
	  playCount++;
	  }//while
	  
	  //call the print method to print a summary of the game
	  printResults(numCardsDealt, playCount, player1PlayStack, player2PlayStack, player1DiscardStack, player2DiscardStack);
	}//main
	
	//read in a file and create a new instance of a card, setting the suit and adding it to the designated players playStack. Returns the number of cards in 
	//the file that were dealt to each of the players.
	public static int deal(StackMurray playerStack1, StackMurray playerStack2)
	{
	  Scanner keyboard = new Scanner(System.in);
	  
	  //declare variables
	  String fileName = "";
	  CardMurray newCard = null;
	  int cardValue = 0;
	  int cardCount = 0;
	  
	  
	//ask the user for the path and name for the data file
	System.out.print("Please enter the file name: ");
	fileName = keyboard.next();
	
	File myFile = new File(fileName);
	
	try
	  {
		//Create a second Scanner object to read the file
	    Scanner input = new Scanner(myFile);
	    
	    //create a new instance of a CardMurray, read in all of the integers, assign a suit, and give the card to either player1 or player2
	    while(input.hasNext())
	    {
	    	  newCard = new CardMurray();
	    	  
	    	  //read in and set card value if it is from 2-14
	    	  cardValue = input.nextInt();
	    	  if(cardValue >= 2 && cardValue <= 14)
	    		newCard.setValue(cardValue);
	    	  
	    	  // set the card Suit to C, D, H, or S 
	    	  if(cardCount % 4 == 0)
	    		 newCard.setSuit('H');  
	    	  else if(cardCount % 4 == .25)
	    		  newCard.setSuit('S');
	    	  else if(cardCount % 4 == .5)
	    		  newCard.setSuit('C');
	    	  else 
	    		  newCard.setSuit('D');
	    	 
	    	  //give card to the players play stack
	    	  if(cardCount % 2 == 0)
	    	  {
	    		playerStack1.push(newCard);
	    	  }//if
	    	  else
	    	  {
	    		playerStack2.push(newCard);
	    	  }//else
	    	  cardCount++;
	    }//while
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
	
	//return the number of cards dealt
	return cardCount;
	}//deal
	
	//pop a card from each player's play stack unless it is empty, then transfer their cards from their discard stack by calling the copy method associated with 
	//that player and then pop the card. Once the card is popped, push the card to another stack called popped to be given to the compare method.
	public static void play(StackMurray playerStack1, StackMurray playerStack2, StackMurray discard1, StackMurray discard2)
	{
	   //declare variables
	   StackMurray popped = new StackMurray();
	   
	   //check if player 1 stack is empty
	   if(playerStack1.isEmpty())
	   {
	    	 copyPlayer1(playerStack1, discard1);
	    	 popped.push(playerStack1.pop());	 
	   }//if  
	   else 
		 popped.push(playerStack1.pop());
	   
	 //check if player 2 stack is empty
	   if(playerStack2.isEmpty())
	   {
		 copyPlayer2(playerStack2, discard2);
	    	 popped.push(playerStack2.pop());
	   }//if  
	   else 
		 popped.push(playerStack2.pop());
	   
	   //give compare the popped values and the discard stacks to add the popped values to the correct player's discard stack.
	   compare(popped, discard1, discard2);
	}//play
	
	//takes in the popped stack from play method and pops the values from that stack. It then compares the values of the card objects and calls either the winPlayer1,
	//winPlayer2, or tie method. 
	public static void compare(StackMurray poppedValues, StackMurray discard1, StackMurray discard2)
	{
	  //declare variables
	  CardMurray player1Card = null;
	  CardMurray player2Card = null;
	  
	  //pop the values out of the stack and assign the card to each corresponding player
	  player2Card = poppedValues.pop();
	  player1Card = poppedValues.pop();
	  
	  //compare the values of the cards and call the correct win/tie method to put the cards into the correct discard stack.
	  if(player2Card.getValue() > player1Card.getValue())
	  {
	    winPlayer2(player2Card, player1Card, discard2);
		 
	  }//if
	  else if(player1Card.getValue() > player2Card.getValue())
	  {
		winPlayer1(player1Card, player2Card, discard1);
	  }//else if
	  else 
	  {
		tie(player1Card, player2Card, discard1, discard2);
	  }//else
	}//compare
	
	//If player 1 is the winner, push the cards into player1's discard stack
	public static void winPlayer1(CardMurray card1, CardMurray card2, StackMurray discardStack1)
	{
	  
	  discardStack1.push(card1);
	  discardStack1.push(card2);
	}//winPlay
	
	//If player 2 is the winner, push the cards into player2's discard stack
	public static void winPlayer2(CardMurray card1, CardMurray card2, StackMurray discardStack2)
	{
	  discardStack2.push(card2);
	  discardStack2.push(card1);
	}//winPlay
	
	//if the card values are the same, push each card back onto the original player's discard stack
	public static void tie(CardMurray card1, CardMurray card2, StackMurray discardStack1, StackMurray discardStack2)
	{
	  discardStack1.push(card1);
	  discardStack2.push(card2);
	}//tie
	
	//transfer the discard stack of cards into the play stack for player1 without changing the order of the cards. returns the playstack
	public static StackMurray copyPlayer1(StackMurray player1Stack, StackMurray discard)
	{
       //declare variables
	   StackMurray temp = new StackMurray();
	   StackMurray answer = null;
		 
	   //check if discard stack is empty
	  while(!discard.isEmpty())
	  {
	    temp.push(discard.pop()); 
	    player1Stack.push(temp.pop());
	    answer = player1Stack;
	  }//while
	  
	  return answer;
	}//copy
	
	//transfer the discard stack of cards into the play stack for player2 without changing the order of the cards. returns the playstack
	public static StackMurray copyPlayer2(StackMurray player2Stack, StackMurray discard)
	{
       //declare variables
	   StackMurray temp2 = new StackMurray();
	   StackMurray answer = null;
	   
	   //check if discard stack is empty
	  while(!discard.isEmpty())
	  {
	    temp2.push(discard.pop()); 
	    player2Stack.push(temp2.pop());
	    answer = player2Stack;
	  }//while
	  return answer;
	}//copy
	
	//count the number of cards in any stack that is passed to the method. Puts the cards back in the original stack in the original order they were in.
	//returns the number of cards that were in the stack
	public static int countCards(StackMurray cards)
	{
	  //declare variables
	  int cardCount = 0;
	  StackMurray temp = new StackMurray();
	  
	  //only do while the stack is not empty
	  while(!cards.isEmpty())
	  {
	    temp.push(cards.pop());
	    cardCount++;
	  }//while
	  while(!temp.isEmpty())
	  {
		cards.push(temp.pop());
	  }//while
	  
	  //return the number of cards
	  return cardCount;
	}//countCards
	
	//print out a summary of the game
	public static void printResults(int numCards, int plays, StackMurray player1play, StackMurray player2play, StackMurray player1discard, StackMurray player2discard)
	{
	  //declare variables
      int player1totalCards = countCards(player1play) + countCards(player1discard);
      int player2totalCards = countCards(player2play) + countCards(player2discard);
      
	  System.out.println("The game started with " + numCards + " cards.");
	  System.out.println("There were " + plays + " plays in the game.");
	 
	  if (player1totalCards == 0)
	  {
	    System.out.println("The game ended with a clear winner");
	    System.out.println("Player 1 ended with " + player1totalCards + " cards.");
	    System.out.println("Player 2 ended with " + player2totalCards + " cards.");
	    System.out.println("The winner was player 2");
	    
	  }//if
	  else if (player2totalCards == 0)
	  {
		System.out.println("The game ended with a clear winner");
	    System.out.println("Player 1 ended with " + player1totalCards + " cards.");
	    System.out.println("Player 2 ended with " + player2totalCards + " cards.");
	    System.out.println("The winner was player 1");
	  }//if
	  else if(plays == 1000 && player1totalCards > player2totalCards)
	  {
	    System.out.println("The game took too long");
	    System.out.println("Player 1 ended with " + player1totalCards + " cards.");
	    System.out.println("Player 2 ended with " + player2totalCards + " cards.");
	    System.out.println("The winner was player 1"); 
	  }//if 
	  else if(plays == 1000 && player1totalCards < player2totalCards)
	  {
		System.out.println("The game took too long");
	    System.out.println("Player 1 ended with " + player1totalCards + " cards.");
	    System.out.println("Player 2 ended with " + player2totalCards + " cards.");
	    System.out.println("The winner was player 2");
	  }//if
	  else
	  {
		System.out.println("The game took too long");
	    System.out.println("Player 1 ended with " + player1totalCards + " cards.");
	    System.out.println("Player 2 ended with " + player2totalCards + " cards.");
	    System.out.println("The winner was no one");
	  }//else
	}//printResults
	
}//BattleDemoMurray

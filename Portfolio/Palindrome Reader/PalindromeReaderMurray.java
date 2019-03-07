//
//Jordan Murray
//This class will read in a list of magic items from a text file and search for
//any magic items that are palindromes. It will do this by using StackMurray
//and QueueMurray which are stacks and queues implemented through a linked
//list which is created in NodeMurray

import java.io.IOException;
import java.io.File;
import java.util.Scanner;

public class PalindromeReaderMurray {
  public static void main(String[] args) {
    //Declare variables
    File myFile = new File("magicitems.txt");
    int i = 0;
    int charCounter = 0;
    int spaceChar = 0;
    int matchingChar = -1;
    StackMurray currentStack = new StackMurray();
    QueueMurray currentQueue = new QueueMurray();
    char currentChar = ' ';
    String[] magicItemsArray = new String[666];

    try{
      //pass the text file of magic items into the Scanner;
      Scanner myScanner = new Scanner(myFile);

      //loop through the text file line by line and put all of the items into an array
      while(myScanner.hasNextLine()){
        String magicItem = myScanner.nextLine();
        magicItem = magicItem.toLowerCase();
        magicItemsArray[i] = magicItem;
        i++;
      }

      //redeclare i to 0, so that it will start at the beginning of the array. Use this for loop to do all
      //computations for each individual string
      for (i = 0; i < 666; i++){
        //use the while loop to go through each individual character of the string held in index i of the array
        while(charCounter < magicItemsArray[i].length()){
          currentChar = magicItemsArray[i].charAt(charCounter);
          //ignore spaces, and push all characters onto stack/ into the queue
          if(currentChar != ' '){
            currentStack.push(currentChar);
            currentQueue.enqueue(currentChar);
          }else{
            //count the number of spaces in that word
            spaceChar++;
          }
          //count the number of characters in total including spaces
          charCounter++;
        }

        //use second loop to compare all of the letters on the stack, until the stack is empty
        while(!currentStack.isEmpty()){
          if(comparison(currentStack, currentQueue)){
            //increment the number of characters that are the same once they have been compared
            matchingChar++;
          }
        }

        //if the number of blank spaces plus the number of characters that match once they have been compared, is
        //equal to the length of the string in array index i, then the string is a palindrome and should be printed
        if((spaceChar + matchingChar) == magicItemsArray[i].length()){
          System.out.println(magicItemsArray[i]);
        }

        //reset all counters, for the next index in the array.
        charCounter = 0;
        spaceChar = 0;
        matchingChar = 0;
      }
    } catch(IOException e) {
         e.printStackTrace();
    }
  }

  //this helper method is used to compare the popped values from both the stack and queues to test for a palindrome.
  public static boolean comparison(StackMurray currStack, QueueMurray currQueue){
      if(currStack.pop() == currQueue.dequeue())
        return true;
      else
        return false;
  }
}

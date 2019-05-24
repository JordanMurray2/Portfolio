//Jordan Murray
//This class has 4 instance variables. It has the inital capacity of the knapsack which does not change, the capacity
//left, and the current capacity which are all integers. The fourth instance variable is an arraylist of spiceMurray
//objects for when the knapsack gets filled, it stores the spices taken and how much of each spice it takes.
//The methods of this class are a null constructor, a full constructor, the getters and setters for each
//instance variable, and hasRoom. Has room is a helper method that checks if the capacity left is 0.

import java.util.*;
import java.io.*;

public class KnapsackMurray{
  //declare variables
  private int initialCapacity;
  private int capacityLeft;
  private int currentCapacity;
  private ArrayList<SpiceMurray> mySpices;

  //null constructor
  public KnapsackMurray(){
	  initialCapacity = 0;
    capacityLeft = 0;
    currentCapacity = 0;
    mySpices = new ArrayList<SpiceMurray>();
  }

  //full constructor
  public KnapsackMurray(int newCapacity, int newCapacityLeft, int newCurrentCapacity, ArrayList<SpiceMurray> newSpices){
    initialCapacity = newCapacity;
    capacityLeft = newCapacityLeft;
    currentCapacity = newCurrentCapacity;
    mySpices = newSpices;
  }

  //return the initial capacity
  public int getInitialCapacity(){return initialCapacity;}

  //return the capacity left
  public int getCapacityLeft(){return capacityLeft;}

  //return the current capacity
  public int getCurrentCapacity(){return currentCapacity;}

  //return the arraylist of spices that were scooped into the knapsack
  public ArrayList<SpiceMurray> getSpices(){return mySpices;}

  //set the initial capacity
  public void setInitialCapacity(int newCapacity){initialCapacity = newCapacity;}

  //set the capacity left
  public void setCapacityLeft(int newCapacityLeft){capacityLeft = newCapacityLeft;}

  //set the current capacity
  public void setCurrentCapacity(int newCurrentCapacity){currentCapacity = newCurrentCapacity;}

  //add a new spice to the arrayList of spices that the knapsack takes
  public void setSpices(SpiceMurray newSpice){mySpices.add(newSpice);}

  //check if the capacity left is 0, meaning there is room left in the knapsack. If it is return false, if its not return true
  public boolean hasRoom(){
    if(capacityLeft == 0){
      return false;
    }else{
      return true;
    }
  }

}

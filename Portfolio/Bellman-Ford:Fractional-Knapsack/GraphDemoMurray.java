//
//Jordan Murray
//This program computes the bellman-Ford SSSP on a weighted graph that is read in by the scanner It also reads
//in a second text file that contains different spices which have a color, price, and quantity and a list of
//knapsacks and their capacities. The program also performs fractional knapsack on the spices, taking whatever
//quantity of spice that has the greatest price without going over the capacity of the knapsack.

import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.util.*;

public class GraphDemoMurray {
  public static void main(String[] args) {
    //Declare variables
    File myFile = new File("graphs2.txt");

    try{
      //pass the text file of graphs into scanner
      Scanner myScanner = new Scanner(myFile);
      //declare arrayList of vertices and edges that will be passed into the graph class later
      ArrayList<Integer> vertices = new ArrayList<Integer>();
      ArrayList<EdgesMurray> edges = new ArrayList<EdgesMurray>();
      //declare a graph object
      GraphMurray myGraph = new GraphMurray();

      //loop through the text file line by line and do what it says accordingly.
      while(myScanner.hasNextLine()){
        //declare variables
        String currLine = myScanner.nextLine();

        //if the current line is a space, skip it
        if(!currLine.equals("\n")){
          //split the string to be compared
          String[] splitted = currLine.split("\\s+");
          //if the first word in the current line is new and the vertices array in the graph is not null, call bellmanFord
          //and then reset the arrayLists in the main class for the next graph. Check if the vertices array is null, so that
          //you do not run bellman ford on the first graph before there is anything in there
          if(splitted[0].equals("new") && myGraph.getVertices() != null){
            bellmanFord(myGraph, 0);

            //reset the arrayList for the next graph after printing everything out
            vertices.clear();
            edges.clear();

          //if the first word is add, then you have to figure out if the next word is vertex or edge.
          }else if(splitted[0].equals("add")){

            //if it is vertex, add it to the vertices array in main and then set the graph's vertex array to the
            //current version of the vertices array
            if(splitted[1].equals("vertex")){
              int vertex = Integer.parseInt(splitted[2]);
              vertices.add(vertex);
              myGraph.setVertices(vertices);

            //if it is edge, get the correct vertexId from the graph and add the vertex it is connected to the
            //connectedVertices arraylist
            }else if(splitted[1].equals("edge")){
              //declare the two vertices that create the edge
              int vertex1 = Integer.parseInt(splitted[2]);
              int vertex2 = Integer.parseInt(splitted[4]);

              //store the weight in a variable
              int myWeight = Integer.parseInt(splitted[5]);

              //create the edge
              EdgesMurray newEdge = new EdgesMurray();
              newEdge.setStartVertex(vertex1);
              newEdge.setEndVertex(vertex2);
              newEdge.setWeight(myWeight);
              //add the edge to main's edge arrayList and then set the graph's edge arrayList to the current
              //version of main's edge arrayList
              edges.add(newEdge);
              myGraph.setEdges(edges);
            }//end if
          }//end if
        }//end if
      }//end while
      //call the bellman Ford algorithm on the last graph because it will not run the while loop again
      bellmanFord(myGraph, 0);

      //create a new scanner for the spice file to complete the knapsack
      File myFile2 = new File("spice.txt");
      Scanner myScanner2 = new Scanner(myFile2);
      //declare variables
      SpiceMurray[] spices = new SpiceMurray[4];
      ArrayList<KnapsackMurray> knapsacks = new ArrayList<KnapsackMurray>();
      int knapsackCounter = 0;
      int spiceCounter = 0;

      while(myScanner2.hasNextLine()){
        //declare variables
        String currentLine = myScanner2.nextLine();
        //if the current line is a space, ignore it
        if(!currentLine.equals("\n")){
          //split the current line at the spaces
          String[] split = currentLine.split("\\s+");
          //if the first part of the line is --, then it is a comment and can be ignored
          if(!split[0].equals("--")){
            if(split[0].equals("spice")){
              //Create a new spice and set all of the values
              SpiceMurray currSpice = new SpiceMurray();
              currSpice.setColor(split[3]);
              currSpice.setPrice(Double.parseDouble(split[6]));
              currSpice.setQuantity(Integer.parseInt(split[9]));

              //add the current spice to the array and increase the counter for the next spice
              spices[spiceCounter] = currSpice;
              spiceCounter++;
            }else if(split[0].equals("knapsack")){
              //create a new knapsack and set all of the values and then add to the knapsack arrayList
              KnapsackMurray currKnapsack = new KnapsackMurray();
              currKnapsack.setInitialCapacity(Integer.parseInt(split[3]));
              currKnapsack.setCapacityLeft(Integer.parseInt(split[3]));
              knapsacks.add(currKnapsack);
            }//if
          }//if
        }//if
      }
      //Call a sort that works from project 2 to sort the spices array according to their unit price in descending order.
      //update the current array to the sorted array
      spices = selectionSort(spices);

      //loop through the knapsack array list and fill all of the knapsacks
      for(int i=0; i < knapsacks.size(); i++){
        //reset the quantity of the spices for every knapsack
        spices[0].setQuantity(2);
        spices[1].setQuantity(8);
        spices[2].setQuantity(6);
        spices[3].setQuantity(4);

        //scoop into the knapsack
        scoop(knapsacks.get(i), spices);
      }

    } catch(IOException e) {
         e.printStackTrace();
         System.out.println("something went wrong");
    }//end try
  }//end main

  //compute the bellmanFord SSSP on all 4 graphs
  public static void bellmanFord(GraphMurray myGraph, int start){
    //declare variables
    int numVertices = myGraph.getVertices().size();
    int numEdges = myGraph.getEdges().size();
    int[] cost = new int[numVertices];
    int[] previous = new int[numVertices];

    //Call helper method to set the cost of every vertex
    initSingleSource(myGraph, start, cost);

    //loop through all of the edges for every vertex
    for(int i =0; i < numVertices-1; i++){
      for(int j=0; j < numEdges; j++){
        //get the current edge information to pass into relax helper method
        EdgesMurray currEdge = myGraph.getEdges().get(j);
        int source = currEdge.getStartVertex();
        int target = currEdge.getEndVertex();
        int weight = currEdge.getWeight();

        //helper method to find the path with the lowest cost/distance
        relax(source, target, weight, cost, previous);
      }
    }
    //loop through all of the edges one more time to check for lower cost
    for(int j=0; j < numEdges; j++){
      //get the current edges information
      EdgesMurray currEdge = myGraph.getEdges().get(j);
      int source = currEdge.getStartVertex();
      int target = currEdge.getEndVertex();
      int weight = currEdge.getWeight();

      //if there is still a lower cost found, then there is a negative weight cycle and BF cannot run because of a loop so return
      if(cost[source-1] != Integer.MAX_VALUE && cost[source-1] + weight < cost[target-1]){
        System.out.println("There is a negative weight cycle.");
        return;
      }
    }

    //print the result and path from the start vertex to the rest of the vertex
    for(int k=1; k < numVertices; k++){
      System.out.print("-- 1 -> " + (k+1) + " Cost is " + cost[k] + ".");
      System.out.print(" Path: 1");
      printBF(previous, k);
      System.out.println(" ");
    }
    System.out.println("");
  }

  //helper method that sets the cost of all of the vertices to the max value
  public static void initSingleSource(GraphMurray myGraph, int start, int[] cost){
    //declare variables
    int numVertices = myGraph.getVertices().size();
    //initialize values
    for(int i=0; i < numVertices ; i++){
      cost[i] = Integer.MAX_VALUE;
    }
    //set the start vertex to a cost of 0
    cost[start] = 0;
  }

  //helper method that checks if there is a path with a lower cost and sets the total cost and previous vertex
  public static void relax(int source, int target, int weight, int[] cost, int[] previous){
    if(cost[source-1] != Integer.MAX_VALUE && cost[source-1] + weight < cost[target-1]){
        //set the total cost to get to the current vertex
        cost[target-1] = cost[source-1]+weight;
        //set the target vertex to the source vertex
        previous[target-1] = source-1;
    }
  }

  //recursive method that prints out the path from the start vertex to the current vertex
  public static void printBF(int[] previousV, int v){
    if( v <= 0){
      return;
    }
    printBF(previousV, previousV[v]);
    System.out.print("-" + (v+1));
  }

  //helper method that completes selection sort on an array
  public static SpiceMurray[] selectionSort(SpiceMurray[] currentArray){
    //declare variables
    int i = 0;
    int j = 0;
    SpiceMurray tempValue = null;
    int lowestPosition = 0;
    int myLength = currentArray.length;

    //loop through the list and set the lowest unsorted positon to i
    for(i = 0; i < myLength-1; i++){
      lowestPosition = i;
      //loop through the rest of the array and make sure
      for(j = i+1; j < myLength; j++){
        if(currentArray[j].getPrice() > currentArray[lowestPosition].getPrice()){
          lowestPosition = j;
        }
      }
      //if the lowest position is not the current position then make the swap
      //there is no point in swapping something with itself
      if(i != lowestPosition){
      tempValue = currentArray[lowestPosition];
      currentArray[lowestPosition] = currentArray[i];
      currentArray[i] = tempValue;
      }
    }
    return currentArray;
  }

  //helper method to add the spices into the knapsack passed in
  public static void scoop(KnapsackMurray currKnapsack, SpiceMurray[] spices){
    //declare variables
    int counter = 0;
    SpiceMurray currSpice = spices[counter];
    int totalSpiceQuantity = 0;

    //get the sum of all of the quanitiies of the spices to use for later calculations
    for(int i=0; i < spices.length; i++){
      totalSpiceQuantity+= spices[i].getQuantity();
    }

    //Continue to scoop into the knapsack until it is full or there is no more spices
    while(currKnapsack.hasRoom() && currKnapsack.getCurrentCapacity() < totalSpiceQuantity){
      //if the current spice goes into the knapsack, it will fill it exactly
      if(currKnapsack.getCurrentCapacity() + currSpice.getQuantity() == currKnapsack.getInitialCapacity()){
        currSpice.setQTaken(currSpice.getQuantity());
        currKnapsack.setSpices(currSpice);
        currKnapsack.setCurrentCapacity(currKnapsack.getInitialCapacity());
        currKnapsack.setCapacityLeft(0);
      //if the current spice goes into the knapsack and there is still room, add it to the knapsack and then go to the next spice
      }else if(currKnapsack.getCurrentCapacity() + currSpice.getQuantity() < currKnapsack.getInitialCapacity()){
        currSpice.setQTaken(currSpice.getQuantity());
        currKnapsack.setSpices(currSpice);
        currKnapsack.setCurrentCapacity(currKnapsack.getCurrentCapacity() + currSpice.getQuantity());
        currKnapsack.setCapacityLeft(currKnapsack.getCapacityLeft() - currSpice.getQuantity());
        //increase counter to get next spice
        counter++;
        //only go to the next spice if there is a next spice
        if(counter < 4){
          currSpice = spices[counter];
        }
      //if the quantity of the current spice is too large to fit into the knapsack, then take as much of it as possible
      }else{//if(currKnapsack.getCurrentCapacity() + currSpice.getQuantity() > currKnapsack.getInitialCapacity())
        currSpice.setQTaken(currKnapsack.getCapacityLeft());
        currKnapsack.setSpices(currSpice);
        currKnapsack.setCurrentCapacity(currKnapsack.getInitialCapacity());
        currKnapsack.setCapacityLeft(0);
      }
    }
    //pass the knapsack into a helper method to print out the details
    printKnapsack(currKnapsack);
  }

  //helper method that prints the details of a knapsack
  public static void printKnapsack(KnapsackMurray currentKS){
    System.out.println("Knapsack Capacity: " + currentKS.getInitialCapacity());
    System.out.println("Quantity in Knapsack: " + currentKS.getCurrentCapacity());
    System.out.println("List of Spices in Knapsack: ");
    for(int i=0; i < currentKS.getSpices().size(); i++){
      System.out.print("Color: " + currentKS.getSpices().get(i).getColor() + ";");
      System.out.print(" Price: " + currentKS.getSpices().get(i).getPrice()+ ";");
      System.out.println(" Quantity Taken of Spice: " + currentKS.getSpices().get(i).getQTaken() + "/" + currentKS.getSpices().get(i).getQuantity()+ ";");
    }
    System.out.println(" ");
  }
}

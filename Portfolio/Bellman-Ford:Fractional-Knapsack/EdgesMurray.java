//Jordan Murray
//This class represents the edge of a directed, weighted graph. It has 3 variables, startvertex, endVertex, and weight.
//All three variables are integers. The methods of this class include a null constructor, a full constructor, and the
//getters and setters for every instance variable of the class. 

import java.util.*;
import java.io.*;

public class EdgesMurray{
  //declare variables
  int startVertex;
  int endVertex;
  int weight;

  //null constructor
  public EdgesMurray(){
    startVertex = 0;
    endVertex = 0;
    weight = 0;
  }

  //full constructor
  public EdgesMurray(int start, int end, int newWeight){
    startVertex = start;
    endVertex = end;
    weight = newWeight;
  }

  //get the start vertex
  public int getStartVertex(){return startVertex;}

  //get the end vertex
  public int getEndVertex(){return endVertex;}

  //get the weight of the edge
  public int getWeight(){return weight;}

  //set the start vertex
  public void setStartVertex(int start){startVertex = start;}

  //set the end vertex
  public void setEndVertex(int end){endVertex = end;}

  //set the weight of the edge
  public void setWeight(int newWeight){weight = newWeight;}
}

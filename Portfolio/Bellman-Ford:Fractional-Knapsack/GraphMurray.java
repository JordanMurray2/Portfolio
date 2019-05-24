//Jordan Murray
//This class represents a weighted directed graph. It has 2 instance variables that are arrayLists. One stores
//vertices and the other stores EdgeMurray objects. The methods of this class are a null constructor, a full constructor,
// and the getters and setters for each arrayList. However, when setting the arrayList, you have to pass in an arrayList,
//you do not set the arrayLists by just adding to them

import java.util.*;
import java.io.*;

public class GraphMurray{
  //declare variables
  ArrayList<Integer> myVertices;
  ArrayList<EdgesMurray> myEdges;

  //null constructor
  public GraphMurray(){
    myVertices = null;
    myEdges = null;
  }

  //full constructor
  public GraphMurray(ArrayList<Integer> vertices, ArrayList<EdgesMurray> edges){
    myVertices = vertices;
    myEdges = edges;
  }

  //get the vertices array
  public ArrayList<Integer> getVertices(){return myVertices;}

  //get the edges array
  public ArrayList<EdgesMurray> getEdges(){return myEdges;}

  //set the vertices array
  public void setVertices(ArrayList<Integer> vertex){myVertices = vertex;}

  //set the edges array
  public void setEdges(ArrayList<EdgesMurray> edge){myEdges = edge;}

}

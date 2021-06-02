import java.util.ArrayList;

/**
 * NodeMurray
 *
 * basic node class that has a name, a parent, and an arraylist of children -- for tree construction (CST.java)
 */
public class NodeMurray {
  private String name;
  private NodeMurray parent;
  private ArrayList<NodeMurray> children;

    /**
     * NodeMurray
     *
     * null constructor
     */
  public NodeMurray(){
	name = null;
	parent = null;
	children = new ArrayList<NodeMurray>();
  }

    /**
     * NodeMurray
     *
     * full constructor that sets the name of the node
     *
     * @param name
     */
  public NodeMurray(String name){
	this.name = name;
  }

    /**
     * getName
     *
     * getter for the name of a node
     *
     * @return String - the name
     */
  public String getName(){ return this.name; }

    /**
     * getParent
     *
     * getter for the parent node
     * @return NodeMurray for the parent of the current node
     */
  public NodeMurray getParent(){ return this.parent; }

    /**
     * getChild
     *
     * gets the child at a given index since it is an arraylist and returns the node at that location
     *
     * @param index -- the location in the arraylist
     *
     * @return -- the node found at the index
     */
  public NodeMurray getChild(int index){ return this.children.get(index); }

    /**
     * getChildrenLength
     *
     * gets the length of the arraylist, mainly used to check if there are children or not
     *
     * @return size of the arraylist
     */
  public int getChildrenLength(){return children.size();}

    /**
     * setName
     *
     * sets the name of the node
     *
     * @param newName
     */
  public void setName(String newName){
	this.name = newName;
  }

    /**
     * setParent
     *
     * sets the parent of the node
     *
     * @param newParent
     */
  public void setParent(NodeMurray newParent){
	this.parent = newParent;
  }

    /**
     * setchild
     *
     * adds a node to the children arraylist
     * @param newChild
     */
  public void setChild(NodeMurray newChild) {
      //if the arraylist is null, then create it
      if (this.children == null) {
          this.children = new ArrayList<NodeMurray>();
      }
      this.children.add(newChild);
  }
}

/**
 * CST
 *
 * This class is used to create a CST, consists of getters for root and current, adds a node to the tree, moves the pointer
 * that is used to keep track of the current location and also hasa print method that traverses through the tree and
 * returns a printable value to display it
 */
public class CST {
    NodeMurray root = null;
    NodeMurray curr = new NodeMurray();
    String traversalResult = "";

    public CST(){}

    /**
     * getRoot
     *
     * getter for the root node
     *
     * @return the root node
     */
    public NodeMurray getRoot() {return this.root;}

    /**
     * getCurr
     *
     * getter for the current node
     *
     * @return the current node
     */
    public NodeMurray getCurr() {return this.curr;}

    /**
     * addNode
     *
     * adds a node to the current tree, or starts a tree if a root doesn't exist
     *
     * @param name the name of the parse or token gets added to the tree
     *
     * @param kind root, branch, or leaf
     */
    public void addNode(String name, String kind){
        NodeMurray node = new NodeMurray();
        node.setName(name);

        if((this.root == null)){
            //we are the root node
            this.root = node;
            this.curr = this.root;
        } else {
            //since we are the child set the parent
            node.setParent(this.curr);
            //add ourselves to the children array
            this.curr.setChild(node);
        }
        //if we are a branch node (not leaf/ root)
        if(kind.equals("branch")){
            //update the curr node to ourself
            this.curr = node;
        }
    }

    /**
     * movePointer
     *
     * this method just resets the current to the parent
     */
    public void movePointer(){
        if(this.curr.getParent() != null){
            this.curr = this.curr.getParent();
        //if the parent is null, throw an error
        } else {
            System.out.println("ERROR: Parent for " + this.curr.getName() + " is null");
        }
    }

    /**
     * printTree
     *
     * this recursive method traverses the CST (left depth-first traversal) built in Parser and prints it out
     *
     * @param node the current node to begin traversing on
     * @param depth how far down on the tree (root = 0)
     * @return string value with the whole traversal makes it easy to print
     */
    public String printTree(NodeMurray node, int depth) {
        //add spacing depending on how far down in the tree a node is located
        for(int i = 0; i < depth; i++){
            traversalResult += "-";
        }
        //if no children (leaf node)
        if(node.getChildrenLength() == 0){
            //add the leaf node
            traversalResult += "[" + node.getName() + "]";
            traversalResult += "\n";
        } else {
            //since there are still children, add the currNode name
            traversalResult += "<" + node.getName() + "> \n";
            //and then make recursive call to expand all the children
            for(int j = 0; j < node.getChildrenLength(); j++){
                printTree(node.getChild(j), depth+1);
            }
        }
        return traversalResult;
    }
}
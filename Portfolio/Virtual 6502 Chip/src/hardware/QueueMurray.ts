//
//Jordan Murray
//Description: This class has 2 instance variable of the type NodeMurray called myHead and myTail. The
//methods include a null constructor, isEmpty, peek, traverse, enqueue, and dequeue. IsEmpty determines if
//a stack is empty, peek gets the value of the head of the queue, traverse gets the value of all of the nodes
//in the linked list, enqueue adds a String to the stack, and dequeue removes a String from the Queue.

import {NodeMurray} from "./NodeMurray";

export class QueueMurray{

    constructor(){
        this.myHead = null;
        this.myTail = null;
    }

    public myHead: NodeMurray = null;
    public myTail: NodeMurray = null;

    /*
     * determines if the Queue is empty, returns true if it is
     *
     * return: boolean
     */
    public isEmpty(): boolean{
	    let ans: boolean = false;
	    if(this.myTail == null)
	        ans = true;

	    return ans;
    }

    /*
     * gets the value of all of the nodes in the queue, used a lot for debugging purposes
     *
     * param: target
     *
     * return: NodeMurray
     */
    public traverse(target: String): NodeMurray{
        let currentNode: NodeMurray = this.myHead;
        while(currentNode != null && currentNode.getData() != target){
            currentNode = currentNode.getNext();
        }
        return currentNode;
    }

    /*
     * adds an char to the end of the Queue, returns true if successful
     *
     * param: value
     */
    public enqueue(value: String): void{
        let newGuy: NodeMurray = new NodeMurray();
        newGuy.setData(value);
        if(this.isEmpty()){
	        this.myHead = newGuy;
            this.myTail = newGuy;
        }else{
            this.myTail.setNext(newGuy);
            this.myTail = newGuy;
        }
    }

    /*
     * removes a char from the Queue, returns the char that was removed or dequeued from the front of the queue
     *
     * return String
     */
    public dequeue(): String{
	    let ans = this.myHead.getData();
        if(this.myHead == this.myTail){
            this.myHead = null;
            this.myTail = null;
        }else{
            this.myHead = this.myHead.getNext();
        }
	    return ans;
    }

    /*
     * helper method that turns the queue to an array for printing purposes
     *
     * return: String[]
     */
    public helpPrintQueue(): String[]{
        let myString: String = "";
        let myBuffer: String[] = [];
        let currNode: NodeMurray = this.myHead;
        while(currNode != null){
            myString = currNode.getData();
            myBuffer.push(myString);
            currNode = currNode.getNext();
        }
        return myBuffer;
    }
}

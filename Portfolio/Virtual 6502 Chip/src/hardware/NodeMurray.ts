//
//Jordan Murray
//Description: This class has two instance variables: myData which is of the type String and
//myNext which is of the type NodeMurray. There is a null constructor and a full constructor which
//only takes in a CardMurray newData. There is also a getter and setter for each of the instance
//variables.
//

export class NodeMurray{

    //constructor
    constructor(){
        this.value = "";
        this.next = null;
    }

    public value: String = "";
    public next: NodeMurray = null;

    //return myData
    public getData(): String {return this.value;}

    //return myNext
    public getNext(): NodeMurray {return this.next;}


    //set myData
    public setData(newData: String): void {this.value = newData;}

    //set myNext
    public setNext(newNext: NodeMurray): void {this.next = newNext;}
}

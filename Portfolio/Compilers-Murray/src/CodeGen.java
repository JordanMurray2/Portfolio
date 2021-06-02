import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeGen{
    boolean verbose;
    CST ast;
    ArrayList<Symbol> symbolTable;
    int progCount;
    ArrayList<NodeMurray> leafNodes = new ArrayList<NodeMurray>();
    String[] output = new String[256];
    ArrayList<Temp> tempTable = new ArrayList<Temp>();
    int astCount = 0;
    Temp currTemp = new Temp();
    int outputCount = 0;
    int heapCount = 256;
    int symbolCount = 0;
    int currScope = 0;
    int numScopes = -1;
    int errCount = 0;

    /**
     * CodeGen
     *
     * constructor for Code generator class
     *
     * @param verbose
     * @param ast
     * @param symbolTable
     * @param progCount
     */
    public CodeGen(boolean verbose, CST ast, ArrayList<Symbol> symbolTable, int progCount){
        this.verbose = verbose;
        this.ast = ast;
        this.symbolTable = symbolTable;
        this.progCount = progCount;
    }

    /**
     * generate
     *
     * This method generates the code by traversing through the ast passed in and then calls the appropriate method to
     * generate the proper code based on the current node
     */
    public void generate(){
        System.out.println("CODE GEN: Generating code for program: " + progCount);
        //traverse the ast to get the nodes
        traverse(ast.getRoot());

        //initialize the ouput to zero
        for(int j = 0; j < output.length; j++){
            //initialize to 00
            output[j] = "00";
        }

        //loop through the leafnodes that were extracted from the AST call the appropriate method based on parent node
        for(int i = 0; i < leafNodes.size(); i++){
            //loop until parent is null and count the number of times a block is hit, this is the current scope
            numScopes = -1;
            NodeMurray currNode = leafNodes.get(i);
            while(currNode != null){
                if(currNode.getName().equals("Block")){
                    numScopes++;
                }
                currNode = currNode.getParent();
            }
            //set the scope
            currScope = numScopes;
            //set i to the node in the ast, to keep pointers consistent
            i = astCount;

            //call the appropriate method based on parent node
            if(leafNodes.get(i).getName().equals("Block")){
                astCount+=2;
            }else if(leafNodes.get(i).getName().equals("intOP") || leafNodes.get(i).getName().equals("if Statement") ||
                    leafNodes.get(i).getName().equals("+") || leafNodes.get(i).getName().equals("While Statement")) {
                System.out.println("CODE GENERATION: operation is not supported");
                errCount++;
            }else if(leafNodes.get(i).getParent().getName().equals("Variable Declaration")){
                varDecl();
            }else if(leafNodes.get(i).getParent().getName().equals("Assignment Statement")){
                assignStmt();
            } else if(leafNodes.get(i).getParent().getName().equals("Print Statement")){
                printStmt();
            } else {
                System.out.println("CODE GENERATION: operation is not supported");
                errCount++;
            }
        }

        if(errCount == 0) {
            //once have traversed through the tree, end of program is reached, so add break statement
            output[outputCount] = "00";
            outputCount++;

            //then go back and fill in the temp table with proper values
            for (int k = 0; k < tempTable.size(); k++) {
                tempTable.get(k).setAddLength(Integer.toHexString(outputCount));
                //get the first part of the address
                String tempVal = Character.toString(tempTable.get(k).getTempAdd().charAt(0)) + Character.toString(tempTable.get(k).getTempAdd().charAt(1));
                //loop through output and replace all the temp values with the correct values from the temp table
                for (int l = 0; l < output.length; l++) {
                    if (output[l].equals(tempVal)) {
                        output[l] = tempTable.get(k).getAddLength();
                        output[l + 1] = "00";
                    }
                }
                outputCount++;
            }

            int printCount = 0;
            //print out the OP codes
            for (int j = 0; j < output.length; j++) {
                if (printCount % 8 == 0) {
                    System.out.println();
                }
                System.out.print(output[j]);
                System.out.print(" ");
                printCount++;
            }
            System.out.println();
        }
    }

    /**
     * traverse
     *
     * This method takes in the current node and gets all the values of the ast and stores them in an arrayList for easy
     * access
     *
     * @param currNode
     */
    public void traverse(NodeMurray currNode){
        if(currNode.getChildrenLength() == 0){
            leafNodes.add(currNode);
        } else {
            for(int i=0; i < currNode.getChildrenLength(); i++) {
                traverse(currNode.getChild(i));
            }
        }
    }

    /**
     * varDecl
     *
     * This method generates code for variable declarations
     */
    public void varDecl(){
        if(leafNodes.get(astCount).getName().equals("int")) {
            //set the temp value
            currTemp.setTempAdd("T" + tempTable.size() + "XX");
            currTemp.setName(leafNodes.get(astCount + 1).getName());
            currTemp.setScope(symbolTable.get(symbolCount).getScope());
            currTemp.setSymbolCount(symbolCount);
            symbolCount++;
            //set the address length according to where the stack begins
            if (tempTable.size() == 0) {
                currTemp.setAddLength("0");
            } else {
                int sumLength = 0;
                for (int i = 0; i < tempTable.size(); i++) {
                    sumLength += tempTable.get(i).getName().length();
                }
                currTemp.setAddLength(String.valueOf(sumLength));
            }
            //store the current temp val in the tempTable for backpatching
            tempTable.add(currTemp);

            //load the accumulator with zero until it is assigned
            output[outputCount] = "A9";
            outputCount++;
            output[outputCount] = "00";
            outputCount++;

            //store the value in temp address for the time being
            output[outputCount] = "8D";
            outputCount++;
            //split the address into first and second part and store in 2 different spots in output array
            String byte1 = Character.toString(currTemp.getTempAdd().charAt(0)) + Character.toString(currTemp.getTempAdd().charAt(1));
            String byte2 = Character.toString(currTemp.getTempAdd().charAt(2)) + Character.toString(currTemp.getTempAdd().charAt(3));

            //store the bytes
            output[outputCount] = byte1;
            outputCount++;
            output[outputCount] = byte2;
            outputCount++;
        } else { //for string and boolean types
            currTemp.setTempAdd("T" + tempTable.size());
            currTemp.setName(leafNodes.get(astCount + 1).getName());
            currTemp.setScope(symbolTable.get(symbolCount).getScope());
            currTemp.setSymbolCount(symbolCount);
            symbolCount++;

            //set the address length according to where the heap begins
            if (tempTable.size() == 0) {
                currTemp.setAddLength("FF");
            } //might need the else not sure yet

            //store the current temp val in the tempTable for backpatching
            tempTable.add(currTemp);
        }
        //reset temp val for next var and other pointers
        currTemp = new Temp();
        astCount += 2;
    }

    /**
     * assignStmt
     *
     * This method generates code for assignment statements
     */
    public void assignStmt(){
        //loop through the symbol table to find the match for the var and get the type
        String type = "";
        for (int k = 0; k < tempTable.size(); k++) {
            if (leafNodes.get(astCount).getName().equals(symbolTable.get(tempTable.get(k).getSymbolCount()).getName())) {
                type = symbolTable.get(tempTable.get(k).getSymbolCount()).getType();
            }
        }
        //check if it is an id/variable
        Pattern id1 = Pattern.compile("^[a-z\\s]$");
        Matcher matcher1 = id1.matcher(leafNodes.get(astCount+1).getName());
        if (matcher1.find()) {
            if (type.equals("int")) {
                //need to load the accumulator from memory address of the var setting the value
                output[outputCount] = "AD";
                outputCount++;

                int match1 = 0;
                //loop through the temp table and get the address that is linked to it
                for(int j = 0; j < tempTable.size(); j++){
                    if(leafNodes.get(astCount+1).getName().equals(tempTable.get(j).getName())){
                       match1 = j;
                    }
                }

                //store the memory address to get the value from
                String byte3 = Character.toString(tempTable.get(match1).getTempAdd().charAt(0)) + Character.toString(tempTable.get(match1).getTempAdd().charAt(1));
                String byte4 = Character.toString(tempTable.get(match1).getTempAdd().charAt(2)) + Character.toString(tempTable.get(match1).getTempAdd().charAt(3));
                output[outputCount] = byte3;
                outputCount++;
                output[outputCount] = byte4;
                outputCount++;

                //op code for storing in memory
                output[outputCount] = "8D";
                outputCount++;

                int match = 0;
                //loop through temp table to get address to store the value in the var
                for (int i = 0; i < tempTable.size(); i++) {
                    if (leafNodes.get(astCount).getName().equals(tempTable.get(i).getName())) {
                        match = i;
                    }
                }

                String byte1 = Character.toString(tempTable.get(match).getTempAdd().charAt(0)) + Character.toString(tempTable.get(match).getTempAdd().charAt(1));
                String byte2 = Character.toString(tempTable.get(match).getTempAdd().charAt(2)) + Character.toString(tempTable.get(match).getTempAdd().charAt(3));
                output[outputCount] = byte1;
                outputCount++;
                output[outputCount] = byte2;
                outputCount++;

                astCount += 2;

            } else { //if the type is string or boolean
                String storeVal = leafNodes.get(astCount + 1).getName();
                //set the start of where the string is stored
                heapCount -= storeVal.length() + 1;
                System.out.println(storeVal.length()+1);
                //set the start value of the string in the temp table
                for (int j = 0; j < tempTable.size(); j++) {
                    if (tempTable.get(j).getName().equals(leafNodes.get(astCount).getName())) {
                        tempTable.get(j).setAddLength(Integer.toString(heapCount, 16));
                    }
                }

                //temp value for looping heap at end
                int tempCount = heapCount;
                for (int i = 0; i < storeVal.length(); i++) {
                    output[tempCount] = Integer.toHexString((int) storeVal.charAt(i));
                    tempCount++;
                }
                output[tempCount] = "00";
                astCount += 2;
            }
        } else {
            if (type.equals("int")) {
                //need to load the accumulator
                output[outputCount] = "A9";
                outputCount++;

                //get the value being assigned to it, convert it to hex, and then put in next value
                int storeVal = Integer.parseInt(leafNodes.get(astCount + 1).getName(), 16);
                String newStore = "";

                //pad with 0 if needed
                if (String.valueOf(storeVal).length() == 1) {
                    newStore = "0" + String.valueOf(storeVal);
                } else {
                    newStore = String.valueOf(storeVal);
                }

                //place the value in the acc
                output[outputCount] = newStore;
                outputCount++;

                //op code for storing in memory
                output[outputCount] = "8D";
                outputCount++;

                int match = 0;
                //loop through temp table to get address to store the value in the var
                for (int i = 0; i < tempTable.size(); i++) {
                    if (leafNodes.get(astCount).getName().equals(tempTable.get(i).getName())) {
                        match = i;
                    }
                }

                String byte1 = Character.toString(tempTable.get(match).getTempAdd().charAt(0)) + Character.toString(tempTable.get(match).getTempAdd().charAt(1));
                String byte2 = Character.toString(tempTable.get(match).getTempAdd().charAt(2)) + Character.toString(tempTable.get(match).getTempAdd().charAt(3));
                output[outputCount] = byte1;
                outputCount++;
                output[outputCount] = byte2;
                outputCount++;

                astCount += 2;

            } else { //if the type is string or boolean
                String storeVal = leafNodes.get(astCount + 1).getName();
                //set the start of where the string is stored
                heapCount -= storeVal.length() + 1;

                //set the start value of the string in the temp table
                for (int j = 0; j < tempTable.size(); j++) {
                    if (tempTable.get(j).getName().equals(leafNodes.get(astCount).getName())) {
                        tempTable.get(j).setAddLength(Integer.toString(heapCount, 16));
                    }
                }

                //temp value for looping heap at end
                int tempCount = heapCount;

                for (int i = 0; i < storeVal.length(); i++) {
                    output[tempCount] = Integer.toHexString((int) storeVal.charAt(i));
                    tempCount++;
                }
                output[tempCount] = "00";
                astCount += 2;
            }
        }
    }

    /**
     * printStmt
     *
     * This method generates code for print statements
     */
    public void printStmt() {
        //use a regex to define the lowercase chars and space
        Pattern id1 = Pattern.compile("^[a-z\\s]$");
        Matcher matcher1 = id1.matcher(leafNodes.get(astCount).getName());
        //if the value in the print statement is an id
        if (matcher1.find()) {
            //loop through the symbol table to find the match for the var and get the type
            String type = "";
            for (int j = 0; j < tempTable.size(); j++) {
                if (leafNodes.get(astCount).getName().equals(symbolTable.get(tempTable.get(j).getSymbolCount()).getName()) &&
                        symbolTable.get(tempTable.get(j).getSymbolCount()).getScope() == currScope) {
                    type = symbolTable.get(j).getType();
                }
            }
            if (type.equals("int")) {
                //load the y reg from memory with value to print out
                output[outputCount] = "AC";
                outputCount++;

                //get the memory address of the var to print out
                int match = 0;
                //loop through temp table to get address to store the value in the var
                for (int i = 0; i < tempTable.size(); i++) {
                    if (leafNodes.get(astCount).getName().equals(tempTable.get(i).getName()) &&
                            symbolTable.get(tempTable.get(i).getSymbolCount()).getScope() == currScope) {
                        match = i;
                    }
                }

                //store the bytes
                String byte1 = Character.toString(tempTable.get(match).getTempAdd().charAt(0)) + Character.toString(tempTable.get(match).getTempAdd().charAt(1));
                String byte2 = Character.toString(tempTable.get(match).getTempAdd().charAt(2)) + Character.toString(tempTable.get(match).getTempAdd().charAt(3));
                output[outputCount] = byte1;
                outputCount++;
                output[outputCount] = byte2;
                outputCount++;

                astCount += 1;

                //load the x-reg with 01 for sys call to print
                output[outputCount] = "A2";
                outputCount++;
                output[outputCount] = "01";
                outputCount++;

                //make system call
                output[outputCount] = "FF";
                outputCount++;
            } else { //if type is string or boolean
                //load the y reg
                output[outputCount] = "A0";
                outputCount++;

                //get the memory address the string is located at from the temp table
                int match = 0;
                //loop through temp table to get address to store the value in the var
                for (int i = 0; i < tempTable.size(); i++) {
                    if (leafNodes.get(astCount).getName().equals(symbolTable.get(tempTable.get(i).getSymbolCount()).getName()) &&
                            symbolTable.get(tempTable.get(i).getSymbolCount()).getScope() == currScope) {
                        output[outputCount] = tempTable.get(i).getAddLength();
                    }
                }
                outputCount++;

                astCount += 1;

                //load the x-reg with 02 to print string from memory address and make system call
                output[outputCount] = "A2";
                outputCount++;
                output[outputCount] = "02";
                outputCount++;
                output[outputCount] = "FF";
                outputCount++;
            }
        } else {
            //if it is a number
            //create a regex for digits 0-9 and search for a match with the curr token
            Pattern id2 = Pattern.compile("[0-9]");
            Matcher matcher2 = id2.matcher(leafNodes.get(astCount).getName());
            if(matcher2.find()){
                //load the y reg with value to print
                output[outputCount] = "A0";
                outputCount++;
                if(Integer.toHexString(Integer.parseInt(leafNodes.get(astCount).getName())).length() == 1){
                    output[outputCount] = "0" + Integer.toHexString(Integer.parseInt(leafNodes.get(astCount).getName()));
                } else {
                    output[outputCount] = Integer.toHexString(Integer.parseInt(leafNodes.get(astCount).getName()));
                }
                outputCount++;
                //make the system call to print
                output[outputCount] = "A2";
                outputCount++;
                output[outputCount] = "01";
                outputCount++;
                output[outputCount] = "FF";
                outputCount++;

                astCount += 1;
            } else { //if it is a string or boolean
                //load the y reg with value to print
                output[outputCount] = "A0";
                outputCount++;

                //store string in heap
                String storeVal = leafNodes.get(astCount).getName();
                //set the start of where the string is stored
                heapCount-= storeVal.length()+1;

                //temp value for looping heap at end
                int tempCount = heapCount;

                for(int i = 0; i < storeVal.length(); i++){
                    output[tempCount] = Integer.toHexString((int)storeVal.charAt(i));
                    tempCount++;
                }
                output[tempCount] = "00";

                astCount += 1;

                //store the location of the start of the string
                output[outputCount] = Integer.toHexString(heapCount);
                outputCount++;
                //make the system call to print
                output[outputCount] = "A2";
                outputCount++;
                output[outputCount] = "02";
                outputCount++;
                output[outputCount] = "FF";
                outputCount++;
            }
        }
    }
}
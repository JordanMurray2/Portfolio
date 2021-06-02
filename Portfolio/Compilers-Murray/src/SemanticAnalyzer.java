import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SemanticAnalyzer
 *
 * This class takes in a token stream from the Parser class that passed through lex and parse with no errors and
 * re-examines the token stream using similar methods to parse to build an AST and symbol table.
 * This class generally performs scope checking and type checking on variables in parallel with the construction of the
 * symbol table and AST. If the semantic analysis will print out steps that trace the scope/type checking process and if
 * no errors the AST and symbol table will also be provided in the verbose output.
 */
public class SemanticAnalyzer {
    boolean verbose;
    ArrayList<Token> tokenStream;
    int progCount;
    CST ast = new CST();
    String currParse = "";
    int tokenCount = 0;
    int errCount = 0;
    int warnCount = 0;
    int scopeCount = -1;
    ArrayList<Symbol> symbolTable = new ArrayList<Symbol>();
    Symbol currSymbol = new Symbol();
    boolean inassignStmt = false;
    boolean inVarDecl = false;
    boolean inExpression = false;
    boolean inWhileStmt = false;
    boolean afterIntOP = false;
    int numScopes = -1;
    String compareType = "";
    String assignVar = "";
    int compare = 0;

    /**
     * SemanticAnalyzer
     *
     * This is the full constructor for the Parser class that sets the variables
     *
     * @param verbose
     * @param tokenStream
     * @param progCount
     */
    public SemanticAnalyzer(boolean verbose, ArrayList<Token> tokenStream, int progCount){
        this.verbose = verbose;
        this.tokenStream = tokenStream;
        this.progCount = progCount;
    }

    /**
     * parse
     *
     * This method starts the process, called from Parser.java. This method also ends the parse process by printing out the
     * AST and symbol table that is built along the way if there are no errors, and prints any errors or warnings out, if
     * found.
     */
    public void parse() {
        System.out.println("SEMANTIC ANALYZER: Analyzing program " + progCount);
        parseProgram();

        //loop through the symbol table and throw warnings if found
        for(int i =0; i < symbolTable.size(); i++){
            if(symbolTable.get(i).getName() != null) {
                // check for uninitialized and unused warnings
                if (!symbolTable.get(i).getInitialized() && !symbolTable.get(i).getUsed()) {
                    System.out.println("WARNING: Variable \'" + symbolTable.get(i).getName() +
                            "\' in scope " + symbolTable.get(i).getScope() + " is declared but not initialized or used");
                    warnCount++;
                    //check for unused variables and throw warnings accordingly
                } else if (!symbolTable.get(i).getUsed() && symbolTable.get(i).getInitialized()) {
                    System.out.println("WARNING: Variable \'" + symbolTable.get(i).getName() +
                            "\' in scope " + symbolTable.get(i).getScope() + " is declared and initialized but not used");
                    warnCount++;
                } else if (symbolTable.get(i).getUsed() && !symbolTable.get(i).getInitialized()) {
                    System.out.println("WARNING: Variable \'" + symbolTable.get(i).getName() +
                            "\' in scope " + symbolTable.get(i).getScope() + " is declared and used but not initialized");
                    warnCount++;
                }
            }
        }

        //print out complete message
        System.out.println("SEMANTIC ANALYZER: Program " + progCount + " finished with " + errCount + " error(s) and " +
                warnCount + " warning(s)");
        System.out.println("");
        //after this print the ast
        if(verbose && errCount == 0) {
            System.out.println("AST for program " + progCount);
            String traversal = ast.printTree(ast.getRoot(), 0);
            System.out.println(traversal);
            printSymbolTable(symbolTable);
            System.out.println("");
            //generate code for program if successful
            CodeGen generator = new CodeGen(true, ast, symbolTable, progCount);
            generator.generate();

        } else if (verbose && errCount > 0){
            //if errors found, skip the AST and symbol table and code generation
            System.out.println("SEMANTIC ANALYZER: AST and Symbol table skipped due to Semantic Analysis Error(s)");
            System.out.println("");
            System.out.println("Code Generation skipped due to Semantic Analysis error(s)");
        }
    }

    /**
     * parseProgram
     *
     * This method calls parseBlocka nd also closes out the last scope
     */
    public void parseProgram(){
            parseBlock();
        //if there are no errors and in the last scope, print out the closing message
        if(errCount == 0) {
            if(scopeCount > 0) {
                scopeCount-=numScopes;
            }
            if(verbose) {
                System.out.println("SEMANTIC ANALYZER: CLOSE SCOPE - " + scopeCount);
            }
        }
    }

    /**
     * parse Block
     *
     * This method defines a block according to the language as well as keeps a running count of the scopes.
     */
    public void parseBlock(){
        if(errCount == 0) {
            ast.addNode("Block", "branch");
            numScopes++;
            scopeCount = numScopes;
            if(verbose) {
                System.out.println("SEMANTIC ANALYZER: BEGIN SCOPE - " + scopeCount);
            }
            tokenCount++;
            //parse the statement list as according to grammar
            parseStmtList();
            tokenCount++;
        }
        //once parseStmtList returns, we know that there should be a closing brace, so print out message to close scope
        if(errCount == 0) {
            if (ast.getCurr() != ast.getRoot() && scopeCount != 0 && tokenCount < tokenStream.size()-1) {
                if(verbose) {
                    System.out.println("SEMANTIC ANALYZER: CLOSE SCOPE - " + scopeCount);
                }
                    ast.movePointer();

                for(int i = 0; i < symbolTable.size(); i++){
                    if(symbolTable.get(i).getScope() == scopeCount){
                        //set all the vars in the current scope to closed
                        symbolTable.get(i).setScopeClosed(true);
                    }
                }
                //return to the "parent" scope
                //scopeCount-= numScopes;
                scopeCount--;
            }
        }
    }

    /**
     * parse statement list
     *
     * This method is the same as in parse minus the move pointer for CST so when re-parsing tokens, it does not get messed up
     */
    public void parseStmtList(){
        if(errCount == 0) {
            if (tokenCount < tokenStream.size()) {
                //statement list can be empty, so if the next token is a curly brace, create the node and return
                if (!tokenStream.get(tokenCount).getValue().equals("}")) {
                    parseStmt();
                    parseStmtList();
                } else {
                    //return
                }
            }
        }
    }

    /**
     * parse statement
     *
     * This method is also the same as in parse minus the CST, for the sake of re-examining the tokens
     */
    public void parseStmt() {
        if(errCount == 0) {
            //use the current token to verify which path to take and what kind of statement comes next
            if (tokenStream.get(tokenCount).getName().equals("T_PRINT")) {
                parsePrintStmt();
            } else if (tokenStream.get(tokenCount).getName().equals("T_ID")) {
                parseAssignStmt();
            } else if (tokenStream.get(tokenCount).getName().equals("T_WHILE")) {
                parseWhileStmt();
            } else if (tokenStream.get(tokenCount).getName().equals("T_IF")) {
                parseIfStmt();
            } else if (tokenStream.get(tokenCount).getName().equals("L_BRACE")) {
                parseBlock();
            } else if (tokenStream.get(tokenCount).getName().equals("T_INT") ||
                    tokenStream.get(tokenCount).getName().equals("T_BOOL") ||
                    tokenStream.get(tokenCount).getName().equals("T_STRING")) {
                parseVarDecl();
            }
        }
    }

    /**
     * parse print statement
     *
     * This method is a simplified version of the parser method since we know token stream is valid, includes pointer to
     * AST
     */
    public void parsePrintStmt(){
        ast.addNode("Print Statement", "branch");
        tokenCount++;
        tokenCount++;
        //parse the expression in the print statement
        parseExpr();
        tokenCount++;
        if(scopeCount !=0) {
            ast.movePointer();
        }
    }

    /**
     * parse assignment statement
     *
     * This method is a simplified version of the parser method since we know token stream is valid, includes pointer to
     * AST
     */
    public void parseAssignStmt(){
        if(errCount == 0) {
            ast.addNode("Assignment Statement", "branch");
            //set bool to true, so when type checking, know where you came from
            inassignStmt = true;
            parseId();
            tokenCount++;
            parseExpr();
            //after parsing the expression, reset pointers
            inassignStmt = false;
            assignVar = "";
            if(scopeCount !=0) {
                ast.movePointer();
            }
        }
    }

    /**
     * parse variable declaration
     *
     * This method is a simplified version of the parser method since we know token stream is valid. Responsible for adding
     * newly declared variables to the symbol table. Includes pointer to AST
     */
    public void parseVarDecl(){
        ast.addNode("Variable Declaration", "branch");
        parseType();
        //set bool to true so know to check if declared later
        inVarDecl = true;
        //the actual variable following the type
        parseId();
        //set to false after parsing the id
        inVarDecl = false;
        //add the symbol just declared to the symbol table and reset the pointer to the current symbol
        symbolTable.add(currSymbol);
        currSymbol = new Symbol();
        if(scopeCount !=0) {
            ast.movePointer();
        }
    }

    /**
     * parse while statement
     *
     * This method is a simplified version of the parser method since we know token stream is valid, includes pointer to
     * AST
     */
    public void parseWhileStmt(){
        if(errCount == 0) {
            //set location so know in while statement -- helps with parse bool expr
            inWhileStmt = true;
            ast.addNode("While Statement", "branch");
            tokenCount++;
            parseBoolExpr();
            //reset bool after parsing the bool expression
            inWhileStmt = false;
            //parse block around the while loop
            parseBlock();
            if(scopeCount !=0) {
                ast.movePointer();
            }
        }
    }

    /**
     * parse if statement
     *
     * This method is a simplified version of the parser method since we know token stream is valid, includes pointer to
     * AST
     */
    public void parseIfStmt(){
        if(errCount == 0) {
            ast.addNode("If Statement", "branch");
            tokenCount++;
            parseBoolExpr();
            //parse the block that contains the statments for the if
            parseBlock();
            if(scopeCount !=0) {
                ast.movePointer();
            }
        }
    }

    /**
     * parse Expression
     *
     * This method re-examines expressions in the program and performs type and declaration checking on variables and
     * assignemnt statements
     */
    public void parseExpr(){
        if(errCount == 0) {
                //figure out the type of the expression and go to the associated parse method
                if (tokenStream.get(tokenCount).getName().equals("T_DIGIT")) {
                    //set the type of first part of condition in while statement, used to type check second part
                    if(inWhileStmt && compareType.equals("")){
                        compareType = "int";
                    }
                    //changes type from T_ID, helpful for type checking
                    tokenStream.get(tokenCount-2).setType("int");
                    //if here verify that id is of type int
                    //System.out.println(tokenStream.get(tokenCount-2).getValue());
                    //boolean typeCheck = verifyType(tokenStream.get(tokenCount-2));
                    /*if (typeCheck) {
                        //if successful, set used to true in symbol table
                        for(int i = 0; i < symbolTable.size(); i++){
                            if(tokenStream.get(tokenCount).getValue().equals(symbolTable.get(i).getName())){
                                symbolTable.get(i).setUsed(true);
                            }
                        }
                        //then proceed to parse int expr just like in parse
                        parseIntExpr();
                    //if type check fails, throw error
                    } else {
                        //System.out.println("HERE");
                        throwError("ERROR: Variable \'" + tokenStream.get(tokenCount-2).getValue() + "\' on line " +
                                tokenStream.get(tokenCount-2).getLineNum() + " assigned wrong type");
                        errCount++;
                    }*/
                    parseIntExpr();
                //if string expression found
                } else if (tokenStream.get(tokenCount).getName().equals("T_QUOTE")) {
                    //set the type of first part of condition in while statement, used to type check second part
                    if(inWhileStmt && compareType.equals("")){
                        compareType = "string";
                    }
                    //if the string expr is an assignment statement, need to check the var type
                    if(inassignStmt) {
                        tokenStream.get(tokenCount-2).setType("string");
                        //if here verify that id is of type string
                        boolean typeCheck = verifyType(tokenStream.get(tokenCount - 2));
                        if (typeCheck) {
                            //if successful set used to true
                            for (int i = 0; i < symbolTable.size(); i++) {
                                if (tokenStream.get(tokenCount).getValue().equals(symbolTable.get(i).getName())) {
                                    symbolTable.get(i).setUsed(true);
                                }
                            }
                            //then parse the string expression as normal
                            parseStringExpr();
                        //if the type check fails, throw an error
                        } else {
                            throwError("ERROR: Variable \'" + tokenStream.get(tokenCount - 2).getValue() + "\' on line " +
                                    tokenStream.get(tokenCount - 2).getLineNum() + " assigned wrong type");
                            errCount++;
                        }
                    //if not in assignment, parse the string expression like normal
                    } else {
                        parseStringExpr();
                    }
                //if boolean expression found
                } else if (tokenStream.get(tokenCount).getName().equals("L_PAREN") ||
                        tokenStream.get(tokenCount).getName().equals("T_TRUE") ||
                        tokenStream.get(tokenCount).getName().equals("T_FALSE")) {
                    //set the type of first part of condition in while statement, used to type check second part
                    if(inWhileStmt && compareType.equals("") && compare == 2){
                        compareType = "boolean";
                    }
                    //if the boolean is expression, need to check the var type
                    if(inassignStmt) {
                        boolean typeCheck = false;
                        //this var is to account for nested bool expressions
                        int tempCount = tokenCount;
                        if(tokenStream.get(tokenCount-2).getName().equals("T_ID")) {
                            tempCount = tokenCount - 2;
                            tokenStream.get(tempCount).setType("boolean");
                            typeCheck = verifyType(tokenStream.get(tempCount));
                        } else {
                            tempCount = tokenCount;
                            //loop back until you find the var you need to type check from assignemnt statement
                            while(tempCount > 0 && !tokenStream.get(tempCount).getValue().equals(assignVar)){
                                tempCount--;
                            }
                            typeCheck = verifyType(tokenStream.get(tokenCount));
                        }

                        if(typeCheck) {
                            //if the type check passes, set used to true
                            for (int i = 0; i < symbolTable.size(); i++) {
                                if (tokenStream.get(tokenCount).getValue().equals(symbolTable.get(i).getName())) {
                                    symbolTable.get(i).setUsed(true);
                                }
                            }
                            //then parse boolean expression like in parse
                            parseBoolExpr();
                        //if the type check fails, throw an error
                        } else {
                            throwError("ERROR: Variable \'" + tokenStream.get(tempCount).getValue() + "\' on line " +
                                    tokenStream.get(tempCount).getLineNum() + " assigned wrong type");
                            errCount++;
                        }
                    //if inside while statement, need to check that a var is declared
                    } else if(!inWhileStmt && tokenStream.get(tokenCount).getValue().length() ==1){
                        boolean checkDec = checkDecaration(tokenStream.get(tokenCount));
                        if(checkDec){
                            //if var has been successfully declared,  set used to true
                            for (int i = 0; i < symbolTable.size(); i++) {
                                if (tokenStream.get(tokenCount).getValue().equals(symbolTable.get(i).getName())) {
                                    symbolTable.get(i).setUsed(true);
                                }
                            }
                            //then parse the boolean expression like normal
                            parseBoolExpr();
                        }
                    //default case
                    } else {
                        parseBoolExpr();
                    }
                //if the expression found is an id
                } else {
                    //need to check the declatation of the id/variable
                    boolean checkDec = checkDecaration(tokenStream.get(tokenCount));
                    //boolean checkType = verifyType(tokenStream.get(tokenCount));
                    if(checkDec) {
                        //set the location boolean
                        inExpression = true;
                            for (int i = 0; i < symbolTable.size(); i++) {
                                if (tokenStream.get(tokenCount).getValue().equals(symbolTable.get(i).getName())) {
                                    //set used to true
                                    symbolTable.get(i).setUsed(true);
                                    if(inWhileStmt && compareType.equals("")){
                                        //since it is declared in the table, just use that to get the type
                                        compareType = symbolTable.get(i).getType();
                                    }
                                }
                            }
                            //parse id like normal
                            parseId();
                            //reset the location bool
                            inExpression = false;
                    //if the declaration check fails, then the id is not decalared in scope, so throw the error for it
                    } else {
                        throwError("ERROR: Variable \'" + tokenStream.get(tokenCount).getValue() + "\' on line " +
                                tokenStream.get(tokenCount).getLineNum() + " is not declared in scope");
                        errCount++;
                    }
                }
        }
    }

    /**
     * parse int expression
     *
     * This method is a simplified version of the parser method since we know token stream is valid
     */
    public void parseIntExpr(){
        if(errCount == 0) {
            parseDigit();
            //then check to see if the next token is an intOP or not and if it is then parse that and the expression that
            //follows
            if (tokenCount < tokenStream.size()) {
                if (tokenStream.get(tokenCount).getName().equals("T_INTOP")) {
                    afterIntOP = true;
                    parseIntOp();
                    parseExpr();
                    afterIntOP = false;
                }
            }
        }
    }

    /**
     * parse String Expression
     *
     * This method is a simplified version of the parser method since we know token stream is valid and concatenates the
     * charlist together to form the whole string. includes the pointer to the AST
     */
    public void parseStringExpr(){
        if(errCount == 0) {
            String charlist = "";
            tokenCount++;
            //loop through the tokenstream until you hit end quote and concatenate the charlist
            while (!tokenStream.get(tokenCount).getName().equals("T_QUOTE")) {
                charlist += tokenStream.get(tokenCount).getValue();
                tokenCount++;
            }
            ast.addNode(charlist, "leaf");
            tokenCount++;
        }
    }

    /**
     * parse boolean expression
     *
     * This method is a simplified version of the parser method since we know token stream is valid
     */
    public void parseBoolExpr(){
        if(errCount == 0) {
            //check if the token is a left paren, if so, then follow the first path
            if (tokenCount < tokenStream.size()) {
                if (tokenStream.get(tokenCount).getName().equals("L_PAREN")) {
                    tokenCount++;
                    //parse the expression, boolop, and the following expression
                    compare = 1;
                    parseExpr();
                    parseBoolOp();
                    compare = 2;
                    parseExpr();
                    compare = 1;
                    compareType = "";
                    tokenCount++;
                    //if a left paren is not found, assume it is just a bool val
                } else {
                    parseBoolVal();
                }
            }
        }
    }

    /**
     * parse id
     *
     * This method follows the same structure as the method in parser, but includes type checking and variable
     * declaration checking on ids
     */
    public void parseId(){
        if(errCount == 0) {
            if (inassignStmt && !inExpression) {
                //check the declaration of a var
                boolean checkDecl = checkDecaration(tokenStream.get(tokenCount));
                if (checkDecl) {
                    for (int i = 0; i < symbolTable.size(); i++) {
                        if (tokenStream.get(tokenCount).getValue().equals(symbolTable.get(i).getName()) &&
                                scopeCount == symbolTable.get(i).getScope()) {
                            //since in assign stmt, set initialized for var to true
                            symbolTable.get(i).setInitialized(true);
                            //set the assignment variable for type checking in expressions
                            assignVar = symbolTable.get(i).getName();
                        }
                    }
                    if(verbose) {
                        //track initialization
                        System.out.println("SEMANTIC ANALYZER: INITIALIZATION - Variable \'" + tokenStream.get(tokenCount).getValue() +
                                "\' initialized");
                    }
                    //parse the char like in parser
                    parseChar();
                //throw error if the variable declaration check fails
                } else {
                    throwError("ERROR: Variable \'" + tokenStream.get(tokenCount).getValue() + "\' on line " +
                            tokenStream.get(tokenCount).getLineNum() + " is not declared");
                    errCount++;
                }
            //if you are declaring a var, ensure it has not already been declared in scope
            } else if (inVarDecl) {
                boolean checkDecl = checkDecaration(tokenStream.get(tokenCount));
                if (checkDecl) {
                    //if the declaration passed, it means the var is already declared in scope, so throw error
                    throwError("ERROR: Variable \'" + tokenStream.get(tokenCount).getValue() + "\' on line " +
                            tokenStream.get(tokenCount).getLineNum() + " has already been declared in scope");
                    errCount++;
                //if it failed, parse the char and continue to declare the var
                } else {
                    parseChar();
                }
            } else if(inExpression && inassignStmt){
                //verify the type of the token and then set to assign variable to use in parsing expressions
                boolean checkType = verifyType(tokenStream.get(tokenCount));
                assignVar = tokenStream.get(tokenCount).getValue();
                if(checkType){
                    //if the type check passes, parse the char like normal
                    parseChar();
                } else {
                    //if it fails throw error
                    throwError("ERROR: Variable \'" + tokenStream.get(tokenCount-2).getValue() + "\' on line " +
                            tokenStream.get(tokenCount-2).getLineNum() + " is assigned wrong type");
                    errCount++;
                }
            } else  if(inWhileStmt) {
                //check the token in while statement
                boolean checkType = verifyType(tokenStream.get(tokenCount));
                //if passed parse char
                if(checkType){
                    parseChar();
                } else {
                    //if fails throw error
                    throwError("ERROR: Variable \'" + tokenStream.get(tokenCount).getValue() + "\' on line " +
                            tokenStream.get(tokenCount).getLineNum() + " cannot be compared to type: " + compareType);
                    errCount++;
                }
            //default case
            }else {
                parseChar();
            }
        }
    }

    /**
     * parse charlist
     *
     * This method is a simplified version of the parser method since we know token stream is valid
     */
    public void parseCharList() {
        if(errCount == 0) {
            parseChar();
            parseCharList();
        }
    }

    /**
     * parse type
     *
     * This method is a simplified version of the parser method since we know token stream is valid, it sets the
     * tokenstream type (used to be T_ID) and also sets the type of the current symbol to be added to the symbol table
     */
    public void parseType(){
        if(errCount == 0) {
            if (tokenStream.get(tokenCount).getValue().equals("int")) {
                ast.addNode("int", "leaf");
                tokenStream.get(tokenCount).setType("int");
                currSymbol.setType("int");
                tokenCount++;
            } else if (tokenStream.get(tokenCount).getValue().equals("string")) {
                ast.addNode("string", "leaf");
                tokenStream.get(tokenCount).setType("string");
                tokenCount++;
                currSymbol.setType("string");
            } else if (tokenStream.get(tokenCount).getValue().equals("boolean")) {
                ast.addNode("boolean", "leaf");
                tokenStream.get(tokenCount).setType("boolean");
                currSymbol.setType("boolean");
                tokenCount++;
            }
        }
    }

    /**
     * parse char
     *
     * This method is a simplified version of the parser method since we know token stream is valid. Sets the name, scope,
     * and the line number of the current symbol to be added to the symbol table using the token stream
     */
    public void parseChar(){

            if (tokenCount < tokenStream.size()) {
                //use a regex to define the lowercase chars and space
                Pattern id1 = Pattern.compile("^[a-z\\s]$");
                Matcher matcher1 = id1.matcher(tokenStream.get(tokenCount).getValue());
                //search for a match and if found, add the leaf node for the char
                if (matcher1.find()) {
                    ast.addNode(tokenStream.get(tokenCount).getValue(), "leaf");
                    currSymbol.setName(tokenStream.get(tokenCount).getValue());
                    currSymbol.setScope(scopeCount);
                    currSymbol.setLineNum(tokenStream.get(tokenCount).getLineNum());
                    tokenCount++;
                }
            }
    }

    /**
     * parse Digit
     *
     * This method is a simplified version of the parser method since we know token stream is valid
     */
    public void parseDigit(){
        if(errCount == 0) {
            //create a regex for digits 0-9 and search for a match with the curr token
            Pattern id1 = Pattern.compile("[0-9]");
            Matcher matcher1 = id1.matcher(tokenStream.get(tokenCount).getValue());
            //if the match is found, add the leaf node
            if (matcher1.find()) {
                ast.addNode(tokenStream.get(tokenCount).getValue(), "leaf");
                tokenCount++;
            }
        }
    }

    /**
     * parse boolOp
     *
     * This method is a simplified version of the parser method since we know token stream is valid, includes a pointer
     * to the AST
     */
    public void parseBoolOp() {
        if(errCount == 0) {
            //match the == if possible and create leaf node
            if (tokenStream.get(tokenCount).getValue().equals("==")) {
                ast.addNode("==", "leaf");
                tokenCount++;
                //if its not ==, check for the other path (!=) and create leaf node if matches
            } else if (tokenStream.get(tokenCount).getValue().equals("!=")) {
                ast.addNode("!=", "leaf");
                tokenCount++;
            }
        }
        if(scopeCount !=0) {
            ast.movePointer();
        }
    }

    /**
     * parse bool Val
     *
     * This method is a simplified version of the parser method since we know token stream is valid, includes a pointer
     * to the AST
     */
    public void parseBoolVal(){
        if(errCount == 0) {
            //match on true
            if (tokenStream.get(tokenCount).getValue().equals("true")) {
                ast.addNode("true", "leaf");
                tokenCount++;
                //if not match on false
            } else if (tokenStream.get(tokenCount).getValue().equals("false")) {
                ast.addNode("false", "leaf");
                tokenCount++;
            }
        }
    }

    /**
     * parse int op
     *
     * This method is a simplified version of the parser method since we know token stream is valid
     */
    public void parseIntOp(){
        if(errCount == 0) {
            // addition is the only op so match if possible
            if (tokenStream.get(tokenCount).getValue().equals("+")) {
                ast.addNode("+", "leaf");
                tokenCount++;
            }
        }
    }

    /**
     * printSymbolTable
     *
     * This method outputs the symbol table constructed when re-parsing the tokens
     *
     * @param symbols the arraylist of symbols/variables declared throughout the program
     */
    public void printSymbolTable(ArrayList<Symbol> symbols){
        System.out.println("Program " + progCount + " Symbol Table");
        System.out.println("-------------------------------------");
        System.out.println("Name    Type        Scope        Line");
        System.out.println("-------------------------------------");
        //loop through the table and space out nicely
        for(int i = 0; i < symbols.size(); i++){
            System.out.print(symbols.get(i).getName() + "       " + symbols.get(i).getType());
            if(symbols.get(i).getType().equals("int")){
                System.out.print("         ");
            }else if(symbols.get(i).getType().equals("string")){
                System.out.print("      ");
            }else {
                System.out.print("     ");
            }
            System.out.println(symbols.get(i).getScope() + "            " + symbols.get(i).getLineNum());
        }
    }

    /**
     * checkDeclaration
     *
     * This method checks that a variable is declared in a given scope
     *
     * @param currToken contains an id/var to check
     *
     * @return boolean on whether it was found in table or not
     */
    public boolean checkDecaration(Token currToken){
        boolean isDeclared = false;
        int numMatches = 0;
        int match = 0;
        int tempScope = scopeCount;
        //System.out.println(scopeCount);
        //check if var exists in the arrayList in any scope that is greater than or equal to the current scope
        while(numMatches == 0 && tempScope >= 0) {
            for (int i = 0; i < symbolTable.size(); i++) {
               // System.out.println("symbol: " + symbolTable.get(i).getName() + " scope: " +  symbolTable.get(i).getScope() + " closed: " + symbolTable.get(i).getScopeClosed());
                //System.out.println("tempScope: " + tempScope);
                if (symbolTable.get(i).getName().equals(currToken.getValue()) && symbolTable.get(i).getScope() == tempScope &&
                        !symbolTable.get(i).getScopeClosed()) {
                    isDeclared = true;
                    match = i;
                    numMatches++;
                }
            }
            //decrement and check the parent scope for matches
            tempScope--;
        }
        //this is to ensure a variable isn't declared in the same scope more than once
        if(numMatches > 1){
            isDeclared = false;
        }

        //this is fix re-declaration errors in scope 0
        if(tempScope < 0){
            tempScope = 0;
        }

        //this is to check for var re-declarations in the same scope - if in different scopes it is okay
        if(tempScope != scopeCount && isDeclared && inVarDecl){
            isDeclared = false;
        }

        //if declaring a variable, print out declaration if not found in table
        if(inVarDecl && !isDeclared && verbose) {
            System.out.println("SEMANTIC ANALYZER: DECLARATION - Variable \'" + currToken.getValue() +
                    "\' declared and added to Symbol table");
        }

        return isDeclared;
    }

    /**
     * verifyType
     *
     * This method is used for type checking
     *
     * @param currToken token with id to check
     *
     * @return boolean - true if types match, false if not
     */
    public boolean verifyType(Token currToken){
        boolean verified = false;
        int tempScope = scopeCount;
        int matchScope = 0;
        int match1 = -1;
        int match2 = -1;
        if(inassignStmt && inExpression){
            while (!verified && tempScope >= 0) {
                for (int i = 0; i < symbolTable.size(); i++) {
                    //loop through symbol table and get scope and name of var
                    if (symbolTable.get(i).getName().equals(currToken.getValue()) &&
                            symbolTable.get(i).getScope() == tempScope) {
                        //then check if the types are equal and set verified
                        if (symbolTable.get(i).getName().equals(tokenStream.get(tokenCount - 2).getValue())) {
                            verified = true;
                            //had to set special condition for int op
                        } else if(afterIntOP && symbolTable.get(i).getName().equals(tokenStream.get(tokenCount - 4).getValue())){
                            verified = true;
                        }
                    }
                    //set special condition for var = var
                    if (symbolTable.get(i).getName().equals(currToken.getValue()) &&
                            symbolTable.get(i).getScope() == tempScope && match1 <= 0) {
                        match1 = i;
                    }
                    if (symbolTable.get(i).getName().equals(tokenStream.get(tokenCount - 2).getValue())&&
                            symbolTable.get(i).getScope() == tempScope && match2 <= 0) {
                        match2 = i;
                    }
                }
                //decrement and check the parent scope for matches
                tempScope--;
            }

            if(match1 >= 0 && match2 >= 0) {
                System.out.println("match1: " + symbolTable.get(match1).getType());
                System.out.println("match2: " + symbolTable.get(match2).getType());
                if (symbolTable.get(match1).getType().equals(symbolTable.get(match2).getType())) {
                    verified = true;
                }
            }
        } else if(inWhileStmt) {
            System.out.println(compareType);
            while (!verified && tempScope >= 0) {
                for (int i = 0; i < symbolTable.size(); i++) {
                    //get the name and the scope
                    if (symbolTable.get(i).getName().equals(currToken.getValue()) &&
                            symbolTable.get(i).getScope() == tempScope) {
                        //check the compare type value set earlier and if they match, then it is valid
                        if (symbolTable.get(i).getType().equals(compareType)) {
                            verified = true;
                            //reset the pointer
                            compareType = "";
                        }
                    }
                }
                //decrement and check the parent scope for matches
                tempScope--;
            }
        } else {
            //check if id exists in the arrayList in any scope that is greater than or equal to the current scope and that
            //the type of the var matches the type of the token for that scope
            while (!verified && tempScope >= 0) {
                for (int i = 0; i < symbolTable.size(); i++) {
                    if (symbolTable.get(i).getName().equals(currToken.getValue()) &&
                            symbolTable.get(i).getScope() == tempScope && symbolTable.get(i).getType().equals(currToken.getType())) {
                        verified = true;
                    }
                }
                //decrement and check the parent scope for matches
                tempScope--;
            }
        }
        //print out if the type check was successful
        if(verified && verbose) {
            System.out.println("SEMANTIC ANALYZER: TYPE CHECK - Variable \'" + currToken.getValue() + "\' verified");
        }
        return verified;
    }

    /**
     * throwError
     *
     * general method used for printing error messages. makes the code neater
     *
     * @param msg string with the exact message to print out
     */
    public void throwError(String msg){
        System.out.println(msg);
    }
}
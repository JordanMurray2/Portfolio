import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser
 *
 * This class takes in a token stream from the Lexer class that passed lex with no errors and examines the token stream
 * for valid grammar. This class generally outlines a language grammar and uses this guide to validate the program passed
 * into it. If the parse passes with no errors, it will print out a CST of the program/tokens to help visualize the
 * program passed in.
 */
public class Parser {
    boolean verbose;
    ArrayList<Token> tokenStream;
    int progCount;
    CST cst = new CST();
    String currParse = "";
    int tokenCount = 0;
    int errCount = 0;

    /**
     * Parser
     *
     * This is the full constructor for the Parser class that sets the variables
     *
     * @param verbose
     * @param tokenStream
     * @param progCount
     */
    public Parser(boolean verbose, ArrayList<Token> tokenStream, int progCount){
        this.verbose = verbose;
        this.tokenStream = tokenStream;
        this.progCount = progCount;
    }

    /**
     * parse
     *
     * This method starts the parse process, called from lex. This method also ends the parse process by printing out the
     * CST that is built along the way if there are no errors.
     */
    public void parse() {
        System.out.println("PARSER: Parsing program " + progCount);
        if(verbose) {
            System.out.println("PARSER: parse()");
        }
        parseProgram();
        //if the parse was successful, print out the CST
        if(errCount == 0) {
            System.out.println("Parse completed successfully");
            System.out.println("");
            if(verbose) {
                System.out.println("CST for program " + progCount);
                String traversal = cst.printTree(cst.getRoot(), 0);
                System.out.println(traversal);
            }
            //if no errors, move on to parse the program
            SemanticAnalyzer analyzer = new SemanticAnalyzer(true, tokenStream, progCount);
            analyzer.parse();
        //if the parse was not successful, print out the errors and skip the CST
        } else {
            System.out.println("Parse completed with " + errCount + " error(s)");
            System.out.println("");
            if(verbose) {
                System.out.println("CST skipped for program " + progCount + " due to PARSER error(s)");
                System.out.println("");
            }
            //if errors found, do not move onto semantic analysis
            System.out.println("Semantic Analysis skipped for program " + progCount + " due to PARSER error(s)");
            System.out.println("");
        }
    }

    /**
     * parseProgram
     *
     * This method defines a program according to the language as Program ::== Block $
     */
    public void parseProgram(){
        if(verbose) {
            System.out.println("PARSER: parseProgram()");
        }
        //Program will always be root -- gotta start somewhere
        cst.addNode("Program", "root");
        //as defined in the grammar
        parseBlock();
        currParse = "parseProgram";
        //match the EOP symbol as defined in the grammar
        if(tokenCount < tokenStream.size()) {
            match(tokenStream.get(tokenCount), currParse);
        //throw error if tokens are off and EOP not current token
        } else if(!tokenStream.get(tokenStream.size()-1).getName().equals("T_EOP")){
            throwError("T_EOP", tokenStream.get(tokenStream.size()-1));
            errCount++;
        }
    }

    /**
     * parse Block
     *
     * This method defines a block according to the language as Block ::== { StatementList }
     */
    public void parseBlock(){
        if(verbose) {
            System.out.println("PARSER: parseBlock()");
        }
        cst.addNode("Block", "branch");

        //match the curly brace according to the grammar
        currParse = "parseBlock";
        if(tokenCount < tokenStream.size()) {
            match(tokenStream.get(tokenCount), currParse);
            tokenCount++;
        }
        //parse the statement list as according to grammar
        parseStmtList();

        //match the second/closing curly brace
        currParse = "parseBlock2";
        if(tokenCount < tokenStream.size()) {
            match(tokenStream.get(tokenCount), currParse);
            tokenCount++;
        }

        //only move the pointer if everything is running smoothly -- you could be at the top of the CST and cause
        //problems
        if( tokenCount < tokenStream.size() && tokenStream.get(tokenCount).getName().equals("T_EOP")) {
            cst.movePointer();
        }
    }

    /**
     * parse statement list
     *
     * This method defines a statement list according to the language as StatementList ::== Statement StatementList or
     * empty
     */
    public void parseStmtList(){
        if(verbose) {
            System.out.println("PARSER: parseStmtList()");
        }
        if(tokenCount < tokenStream.size()) {
            //statement list can be empty, so if the next token is a curly brace, create the node and return
            if (!tokenStream.get(tokenCount).getValue().equals("}")) {
                cst.addNode("Statement List", "branch");
                parseStmt();
                parseStmtList();
            } else if (tokenStream.get(tokenCount).getValue().equals("}") &&
                    tokenStream.get(tokenCount - 1).getValue().equals("{")) {
                cst.addNode("Statement List", "branch");
            }
        }
        cst.movePointer();
    }

    /**
     * parse statement
     *
     * This method defines a statement according to the language as Statement ::== PrintStatement, AssignmentStatement,
     * VarDecl, WhileStatement, IfStatement, or a Block
     */
    public void parseStmt() {
        if(verbose) {
            System.out.println("PARSER: parseStmt()");
        }
        cst.addNode("Statement", "branch");
        if(tokenCount < tokenStream.size()) {
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
            //if none of the statements are found, throw an error
            } else {
                System.out.println("PARSER: ERROR: invalid statement -- Expected [print, assign, while, if, var declaration," +
                        " or block statement ] got [ " + tokenStream.get(tokenCount).getName() + " ] with value '" +
                        tokenStream.get(tokenCount).getValue() + "' on line " + tokenStream.get(tokenCount).getLineNum());
                errCount++;
                tokenCount++;
            }
        }
        cst.movePointer();
    }

    /**
     * parse print statement
     *
     * This method defines a print statement according to the language as PrintStatement ::== print ( Expr )
     */
    public void parsePrintStmt(){
        if(verbose) {
            System.out.println("PARSER: parsePrintStmt()");
        }
        cst.addNode("Print Statement", "branch");
        //match the keyword print
        currParse = "parsePrintStmt";
        if(tokenCount < tokenStream.size()) {
            match(tokenStream.get(tokenCount), currParse);
            tokenCount++;
        }
        //match the open paren
        currParse = "parsePrintStmt2";
        if(tokenCount < tokenStream.size()) {
            match(tokenStream.get(tokenCount), currParse);
            tokenCount++;
        }
        //parse the expression in the print statement
        parseExpr();
        //match the closing paren for the print statement
        currParse = "parsePrintStmt3";
        if(tokenCount < tokenStream.size()) {
            match(tokenStream.get(tokenCount), currParse);
            tokenCount++;
        }
        cst.movePointer();
    }

    /**
     * parse assignment statement
     *
     * This method defines an assignment statement according to the language as AssignmentStatement ::== Id = Expr
     */
    public void parseAssignStmt(){
        if(verbose) {
            System.out.println("PARSER: parseAssignStmt()");
        }
        cst.addNode("Assignment Statement", "branch");
        //immediately call parse Id, because that is what is expected according to the grammmar
        parseId();
        //match the assignment (=) symbol
        currParse = "parseAssignStmt";
        if(tokenCount < tokenStream.size()) {
            match(tokenStream.get(tokenCount), currParse);
            tokenCount++;
        }
        //parse the expression following the id and =
        parseExpr();
        cst.movePointer();
    }

    /**
     * parse variable declaration
     *
     * This method defines a variable declaration according to the language as VarDecl ::== type Id
     */
    public void parseVarDecl(){
        if(verbose) {
            System.out.println("PARSER: parseVarDecl()");
        }
        cst.addNode("Variable Declaration", "branch");
        //figure out the type
        parseType();
        //the actual variable following the type
        parseId();
        cst.movePointer();
    }

    /**
     * parse while statement
     *
     * This method defines a program according to the language as WhileStatement ::== while BooleanExpr Block
     */
    public void parseWhileStmt(){
        if(verbose) {
            System.out.println("PARSER: parseWhileStmt()");
        }
        cst.addNode("While Statement", "branch");
        //match the keyword while
        currParse = "parseWhileStmt";
        if(tokenCount < tokenStream.size()) {
            match(tokenStream.get(tokenCount), currParse);
            tokenCount++;
        }
        //parse the boolean expression for the condition
        parseBoolExpr();
        //parse block around the while loop
        parseBlock();
        cst.movePointer();
    }

    /**
     * parse if statement
     *
     * This method defines an if statement according to the language as IfStatement ::== if BooleanExpr Block
     */
    public void parseIfStmt(){
        if(verbose) {
            System.out.println("PARSER: parseIfStmt()");
        }
        cst.addNode("If Statement", "branch");
        //match keyword if
        currParse = "parseIfStmt";
        if(tokenCount < tokenStream.size()) {
            match(tokenStream.get(tokenCount), currParse);
            tokenCount++;
        }
        //parse boolean expression for if statement
        parseBoolExpr();
        //parse the block that contains the statments for the if
        parseBlock();
        cst.movePointer();
    }

    /**
     * parse Expression
     *
     * This method defines an Expression according to the language as Expr ::== IntExpr, StringExpr, BooleanExpr, or Id
     */
    public void parseExpr(){
        if(verbose) {
            System.out.println("PARSER: parseExpr()");
        }
        cst.addNode("Expr", "branch");
        //figure out the type of the expression and go to the associated parse method
        if(tokenCount < tokenStream.size()) {
            if (tokenStream.get(tokenCount).getName().equals("T_DIGIT")) {
                parseIntExpr();
            } else if (tokenStream.get(tokenCount).getName().equals("T_QUOTE")) {
                parseStringExpr();
            } else if (tokenStream.get(tokenCount).getName().equals("L_PAREN") ||
                    tokenStream.get(tokenCount).getName().equals("T_TRUE") ||
                    tokenStream.get(tokenCount).getName().equals("T_FALSE")) {
                parseBoolExpr();
            } else {
                parseId();
            }
        }
        cst.movePointer();
    }

    /**
     * parse int expression
     *
     * This method defines an int expression according to the language as IntExpr ::== digit intop Expr or just a digit
     */
    public void parseIntExpr(){
        if(verbose) {
            System.out.println("PARSER: parseIntExpr()");
        }
        cst.addNode("Int Expr", "branch");
        //parse a digit no matter which path
        parseDigit();
        //then check to see if the next token is an intOP or not and if it is then parse that and the expression that
        //follows
        if(tokenCount < tokenStream.size()) {
            if (tokenStream.get(tokenCount).getName().equals("T_INTOP")) {
                parseIntOp();
                parseExpr();
            }
        }
        //if token not equal to intOp, return to expr because it was only a digit
        cst.movePointer();
    }

    /**
     * parse String Expression
     *
     * This method defines a string expression according to the language as StringExpr ::== " CharList "
     */
    public void parseStringExpr(){
        if(verbose) {
            System.out.println("PARSER: parseStringExpr()");
        }
        cst.addNode("String Expr", "branch");
        //match the first quote
        currParse = "parseStringExpr";
        if(tokenCount < tokenStream.size()) {
            match(tokenStream.get(tokenCount), currParse);
            tokenCount++;
        }
        //parse the char list according to the grammar
        parseCharList();
        //then match the second quote
        if(tokenCount < tokenStream.size()) {
            match(tokenStream.get(tokenCount), currParse);
            tokenCount++;
        }
        cst.movePointer();
    }

    /**
     * parse boolean expression
     *
     * This method defines a boolean expression according to the language as BooleanExpr ::== ( Expr boolop Expr )
     * or just a boolval
     */
    public void parseBoolExpr(){
        if(verbose) {
            System.out.println("PARSER: parseBoolExpr()");
        }
        cst.addNode("Boolean Expr", "branch");
        //check if the token is a left paren, if so, then follow the first path
        if(tokenCount < tokenStream.size()) {
            if (tokenStream.get(tokenCount).getName().equals("L_PAREN")) {
                //match the left paren
                currParse = "parseBoolExpr";
                match(tokenStream.get(tokenCount), currParse);
                tokenCount++;
                //parse the expression, boolop, and the following expression
                parseExpr();
                parseBoolOp();
                parseExpr();
                //then match the closing or right paren
                currParse = "parseBoolExpr2";
                match(tokenStream.get(tokenCount), currParse);
                tokenCount++;
            //if a left paren is not found, assume it is just a bool val
            } else {
                parseBoolVal();
            }
        }
        cst.movePointer();
    }

    /**
     * parse id
     *
     * This method defines an id according to the language as Id ::== char
     */
    public void parseId(){
        if(verbose) {
            System.out.println("PARSER: parseId()");
        }
        cst.addNode("Id", "branch");
        //immediately go to parse Char
        parseChar();
        cst.movePointer();
    }

    /**
     * parse charlist
     *
     * This method defines a charlist according to the language as CharList ::== char CharList, space CharList, or ε
     */
    public void parseCharList() {
        if(verbose) {
            System.out.println("PARSER: parseCharList()");
        }
        cst.addNode("CharList", "branch");
        if(tokenCount < tokenStream.size()) {
            //space char is handled with the rest of the chars
            if (tokenStream.get(tokenCount).getName().equals("T_CHAR")) {
                parseChar();
                parseCharList();
            }
        }
        //empty or nothing is okay! so just return when charlist is not equal to space or char
        cst.movePointer();
    }

    /**
     * parse type
     *
     * This method defines a type according to the language as type ::== int, string, or boolean
     */
    public void parseType(){
        if(verbose) {
            System.out.println("PARSER: parseType()");
        }
        cst.addNode("Type", "branch");
        //check the token value to find the type and match accordingly by creating a leaf node on the CST
        if(tokenCount < tokenStream.size()) {
            if (tokenStream.get(tokenCount).getValue().equals("int")) {
                cst.addNode("int", "leaf");
                tokenCount++;
            } else if (tokenStream.get(tokenCount).getValue().equals("string")) {
                cst.addNode("string", "leaf");
                tokenCount++;
            } else if (tokenStream.get(tokenCount).getValue().equals("boolean")) {
                cst.addNode("boolean", "leaf");
                tokenCount++;
            //if none of the above match, then there is a parse error
            } else {
                errCount++;
                throwError("int, string, or boolean", tokenStream.get(tokenCount));
            }
        }
        cst.movePointer();
    }

    /**
     * parse char
     *
     * This method defines a char according to the language as Char ::== a-z and space
     */
    public void parseChar(){
        if(verbose) {
            System.out.println("PARSER: parseChar()");
        }
        cst.addNode("Char", "branch");
        if(tokenCount < tokenStream.size()) {
            //use a regex to define the lowercase chars and space
            Pattern id1 = Pattern.compile("^[a-z\\s]$");
            Matcher matcher1 = id1.matcher(tokenStream.get(tokenCount).getValue());
            //search for a match and if found, add the leaf node for the char
            if (matcher1.find()) {
                cst.addNode(tokenStream.get(tokenCount).getValue(), "leaf");
                tokenCount++;
            //if a match isnt found, then there is an error
            } else {
                errCount++;
                throwError("char a-z", tokenStream.get(tokenCount));
            }
        }
        cst.movePointer();
    }

    /**
     * parse Digit
     *
     * This method defines a digit according to the language as Digit ::== 1-9
     */
    public void parseDigit(){
        if(verbose) {
            System.out.println("PARSER: parseDigit()");
        }
        cst.addNode("Digit", "branch");
        if(tokenCount < tokenStream.size()) {
            //create a regex for digits 0-9 and search for a match with the curr token
            Pattern id1 = Pattern.compile("[0-9]");
            Matcher matcher1 = id1.matcher(tokenStream.get(tokenCount).getValue());
            //if the match is found, add the leaf node
            if (matcher1.find()) {
                cst.addNode(tokenStream.get(tokenCount).getValue(), "leaf");
                tokenCount++;
            //if made it this far and digit isnt found, there is a parse error
            } else {
                errCount++;
                throwError("digit 0-9", tokenStream.get(tokenCount));
            }
        }
        cst.movePointer();
    }

    /**
     * parse boolOp
     *
     * This method defines a boolOP according to the language as boolop ::== != or ==
     */
    public void parseBoolOp() {
        if(verbose) {
            System.out.println("PARSER: parseBoolOp()");
        }
        cst.addNode("Bool Op", "branch");
        if(tokenCount < tokenStream.size()) {
            //match the == if possible and create leaf node
            if (tokenStream.get(tokenCount).getValue().equals("==")) {
                cst.addNode("==", "leaf");
                tokenCount++;
            //if its not ==, check for the other path (!=) and create leaf node if matches
            } else if (tokenStream.get(tokenCount).getValue().equals("!=")) {
                cst.addNode("!=", "leaf");
                tokenCount++;
            //if it is not either, throw an error
            } else {
                errCount++;
                throwError("== or !=", tokenStream.get(tokenCount));
            }
        }
        cst.movePointer();
    }

    /**
     * parse bool Val
     *
     * This method defines a bool val according to the language as BoolVal ::== true or false
     */
    public void parseBoolVal(){
        if(verbose) {
            System.out.println("PARSER: parseBoolVal()");
        }
        cst.addNode("Bool Val", "branch");
        if(tokenCount < tokenStream.size()) {
            //match on true
            if (tokenStream.get(tokenCount).getValue().equals("true")) {
                cst.addNode("true", "leaf");
                tokenCount++;
            //if not match on false
            } else if (tokenStream.get(tokenCount).getValue().equals("false")) {
                cst.addNode("false", "leaf");
                tokenCount++;
            //if neither true or false, throw an error
            } else {
                errCount++;
                throwError("true,false, or boolean expression", tokenStream.get(tokenCount));
            }
        }
        cst.movePointer();
    }

    /**
     * parse int op
     *
     * This method defines an int op according to the language as intop ::== +
     */
    public void parseIntOp(){
        if(verbose) {
            System.out.println("PARSER: parseIntOp()");
        }
        cst.addNode("Int Op", "branch");
        if(tokenCount < tokenStream.size()) {
            // addition is the only op so match if possible
            if (tokenStream.get(tokenCount).getValue().equals("+")) {
                cst.addNode("+", "leaf");
                tokenCount++;
            //if not throw an error
            } else {
                errCount++;
                throwError("true or false", tokenStream.get(tokenCount));
            }
        }
        cst.movePointer();
    }

    /**
     * match
     *
     * This method is used to ensure that a given token is in its correct location.
     *
     * @param currToken the token being checked
     * @param currParse tracker for where the token came from. A way to know what symbol you need to check, based on
     *                  what you are parsing at the moment.
     */
    public void match(Token currToken, String currParse){
        //switch on where you are in the parse
        switch(currParse){
            //if here check for EOP at end of program, if found, create leaf node, if not throw error
            case "parseProgram" : {
                if (!currToken.getName().equals("T_EOP")) {
                    errCount++;
                    throwError("T_EOP", currToken);
                } else {
                    cst.addNode("$", "leaf");
                }
                break;
            }
            //if here check for Left brace, if found, create leaf node, if not throw error
            case "parseBlock" : {
                if(!currToken.getName().equals("L_BRACE")){
                    errCount++;
                    throwError("L_BRACE", currToken);
                } else {
                    cst.addNode("{", "leaf");
                }
                break;
            }
            //if here check for right brace, if found, create leaf node, if not throw error
            case "parseBlock2" : {
                if(!currToken.getName().equals("R_BRACE")){
                    errCount++;
                    throwError("R_BRACE", currToken);
                } else {
                    cst.addNode("}", "leaf");
                }
                break;
            }
            //if here check for print keyword, if found, create leaf node, if not throw error
            case "parsePrintStmt" : {
                if(!currToken.getName().equals("T_PRINT")){
                    errCount++;
                    throwError("T_PRINT", currToken);
                } else {
                    cst.addNode("print", "branch");
                }
                break;
            }
            //if here check for left paren, if found, create leaf node, if not throw error
            case "parsePrintStmt2" : {
                if(!currToken.getName().equals("L_PAREN")){
                    errCount++;
                    throwError("L_PAREN", currToken);
                }else {
                    cst.addNode("(", "branch");
                }
                break;
            }
            //if here check for right paren, if found, create leaf node, if not throw error
            case "parsePrintStmt3" : {
                if(!currToken.getName().equals("R_PAREN")){
                    errCount++;
                    throwError("R_PAREN", currToken);
                }else {
                    cst.addNode(")", "branch");
                }
                break;
            }
            //if here check for = symbol, if found, create leaf node, if not throw error
            case "parseAssignStmt" : {
                if(!currToken.getName().equals("T_ASSIGN")){
                    errCount++;
                    throwError("T_ASSIGN", currToken);
                }else {
                    cst.addNode("=", "branch");
                }
                break;
            }
            //if here check for while keyword, if found, create leaf node, if not throw error
            case "parseWhileStmt" : {
                if(!currToken.getName().equals("T_WHILE")){
                    errCount++;
                    throwError("T_WHILE", currToken);
                }else {
                    cst.addNode("while", "branch");
                }
                break;
            }
            //if here check for if keyword, if found, create leaf node, if not throw error
            case "parseIfStmt" : {
                if(!currToken.getName().equals("T_IF")){
                    errCount++;
                    throwError("T_IF", currToken);
                }else {
                    cst.addNode("if", "branch");
                }
                break;
            }
            //if here check for quote, if found, create leaf node, if not throw error
            case "parseStringExpr" : {
                if(!currToken.getName().equals("T_QUOTE")){
                    errCount++;
                    throwError("T_QUOTE", currToken);
                }else {
                    cst.addNode("\"", "branch");
                }
                break;
            }
            //if here check for left paren, if found, create leaf node, if not throw error
            case "parseBoolExpr" : {
                if(!currToken.getName().equals("L_PAREN")){
                    errCount++;
                    throwError("L_PAREN", currToken);
                }else {
                    cst.addNode("(", "branch");
                }
                break;
            }
            //if here check for right paren, if found, create leaf node, if not throw error
            case "parseBoolExpr2" : {
                if(!currToken.getName().equals("R_PAREN")){
                    errCount++;
                    throwError("R_PAREN", currToken);
                }else {
                    cst.addNode(")", "branch");
                }
                break;
            }
        }
    }

    /**
     * throwError
     *
     * This method prints out the error messages from the match method, if a match fails
     * @param expectedToken -- the expected value for the section of the parse
     * @param currToken -- the actual token found
     */
    public void throwError(String expectedToken, Token currToken){
        if(verbose) {
            System.out.println("PARSER: ERROR: Expected [ " + expectedToken + " ] got [ " + currToken.getName() +
                    " ] with value '" + currToken.getValue() + "' on line " + currToken.getLineNum());
        }
        //increment the token count to avoid infinite loops on the current token
        tokenCount++;
    }
}
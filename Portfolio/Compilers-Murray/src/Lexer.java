import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

/**
 * Lexer class
 *
 * This class reads in a text file and prints out a stream of tokens if they are valid as well as reports errors and
 * warnings.
 */
public class Lexer {
    boolean verbose;
    public Lexer(){}
    //The user can choose to see the token stream output or not by changing boolean in Compiler.java
    public Lexer(boolean verbose){this.verbose = verbose;}

    Token currToken = new Token();
    String scanVal = "";
    String bestMatch = "";
    String bestType = "";
    int currLineNum = 0;
    int matchLineNum = 0;
    int startPosMatch = 0;
    int endPosMatch = 0;
    int lastPos = 1;
    int currPos = 0;
    int programCounter = 0;
    int errCounter = 0;
    int warnCounter = 0;
    boolean matchFound = false;
    boolean inString = false;
    boolean inComment = false;
    boolean inProgram = true;
    ArrayList<Token> tokenStream = new ArrayList<Token>();

    /**
     * readFile
     *
     * takes in a txt file and reads the program and scans char by char to find matches for tokens according to a language
     * grammar
     *
     * @param file
     */
    public void readFile(String file){
        //try catch for reading file
        try {
            File output = new File(file);
            Scanner myReader = new Scanner(output);
            programCounter++;

            //loop through the file
            while (myReader.hasNextLine()) {
                //Check if there is a program, if so, start Lexing!
                if(inProgram) {
                    printInfo("Lexing Program " + programCounter);
                    inProgram = false;
                    tokenStream = new ArrayList<Token>();
                }
                currLineNum++;
                String myData = myReader.nextLine();

                //loop through the current line char by char
                for(int i = 0; i < myData.length(); i++){
                    //every time a char is scanned, increment the curr position
                    currPos++;

                    //store the char read in into a string for pattern matching
                    if(!scanVal.equals("")){
                        scanVal = scanVal + Character.toString(myData.charAt(i));
                    } else {
                        scanVal = Character.toString(myData.charAt(i));
                    }

                    //Throw error if there is something that is not in language grammar
                    if(!findKeyword(Character.toString(myData.charAt(i))) &&
                            !findId(Character.toString(myData.charAt(i))) &&
                            !findSymbol(Character.toString(myData.charAt(i))) &&
                            !findDigit(Character.toString(myData.charAt(i))) &&
                            !findSpace(Character.toString(myData.charAt(i))) && bestMatch.equals("")){
                        errCounter++;
                        throwUnrecognized(Character.toString(myData.charAt(i)), currLineNum, currPos-1);
                        scanVal = "";
                    }

                    //This prevents printing a token for the / symbol after exiting a comment
                    if(Character.toString(myData.charAt(i)).equals("/")) {
                        //if one of these chars is at the end of the line, throw an error
                        if(myData.length()-1 == i) {
                            if (Character.toString(myData.charAt(i)).equals("/")) {
                                errCounter++;
                                throwUnrecognized(Character.toString(myData.charAt(i)), currLineNum, currPos);
                            } else if (Character.toString(myData.charAt(i)).equals("*")) {
                                errCounter++;
                                throwUnrecognized(Character.toString(myData.charAt(i)), currLineNum, currPos);
                            }
                        }
                        scanVal = "";
                        if(i < myData.length()-1){
                            if (!Character.toString(myData.charAt(i + 1)).equals("*")) {
                                currPos++;
                            }
                        }
                    }

                    //ensure i is not the end of the line so that can check next characters
                    if(i < myData.length()-1) {
                        //check if in a comment and flip bool if so
                        if (Character.toString(myData.charAt(i)).equals("/") &&
                                Character.toString(myData.charAt(i + 1)).equals("*") && !inComment) {
                            if(!bestMatch.equals("")){
                                createToken();
                            }
                            currPos = i + 2;
                            lastPos = currPos;
                            inComment = true;
                        } else if (Character.toString(myData.charAt(i)).equals("/") &&
                                !Character.toString(myData.charAt(i + 1)).equals("*") && !inComment) {
                            errCounter++;
                            throwUnrecognized(Character.toString(myData.charAt(i)), currLineNum, currPos);
                            i++;
                            currPos = i+1;
                            lastPos = currPos;
                        } else if (Character.toString(myData.charAt(i)).equals("*") &&
                                !Character.toString(myData.charAt(i + 1)).equals("/") && !inComment) {
                            errCounter++;
                            throwUnrecognized(Character.toString(myData.charAt(i)), currLineNum, currPos-1);
                            scanVal = "";
                        }

                        //check if there is an equality
                        if(Character.toString(myData.charAt(i)).equals("=") &&
                                Character.toString(myData.charAt(i+1)).equals("=")) {
                            //create the last token before creating the != token
                            if (!bestMatch.equals("")) {
                                createToken();
                                i = currPos;
                            } else {
                                //set the values for the != token
                                bestMatch = "==";
                                startPosMatch = lastPos + 1;
                                endPosMatch = currPos;
                                bestType = "symbol";
                                matchLineNum = currLineNum;
                                createToken();
                                //i = currPos;
                            }
                        }
                        //check if there is an inequality
                        if(Character.toString(myData.charAt(i)).equals("!") &&
                                Character.toString(myData.charAt(i+1)).equals("=")){
                            //create the last token before creating the != token
                            if(!bestMatch.equals("")){
                                createToken();
                                i = currPos;
                            }else {
                                //set the values for the != token
                                bestMatch = "!=";
                                startPosMatch = lastPos + 1;
                                endPosMatch = currPos;
                                bestType = "symbol";
                                matchLineNum = currLineNum;
                                createToken();
                                i = currPos;
                            }
                        //! is not valid by itself
                        } else if(Character.toString(myData.charAt(i)).equals("!") &&
                                !Character.toString(myData.charAt(i+1)).equals("=")){
                            if(!bestMatch.equals("")) {
                                createToken();
                            }
                            errCounter++;
                            throwUnrecognized(Character.toString(myData.charAt(i)), currLineNum, currPos-1);
                            scanVal = "";
                        }
                    }//end check for i

                    //loop while inside comment until find closing comment
                    while(inComment && (i != myData.length())){
                        //throw warning if never closed
                        if(Character.toString(myData.charAt(i)).equals("$")){
                            warnCounter++;
                            throwWarning("Comment did not close");
                            inComment = false;
                        }else {
                            //check if ending found
                            if (Character.toString(myData.charAt(i)).equals("*") &&
                                    Character.toString(myData.charAt(i + 1)).equals("/") && inComment) {
                                currPos = i + 2;
                                lastPos = currPos;
                                inComment = false;
                            } else
                                currPos++;
                            i++;
                            bestMatch = "";
                        }
                    }
                    //if there is a " and you are not currently in a string, flip boolean and print the " token
                    if(Character.toString(myData.charAt(i)).equals("\"") && !inString && !inComment){
                        if(!bestMatch.equals("")){
                            createToken();
                            inString = true;
                            startPosMatch = lastPos + 1;
                            endPosMatch = currPos;
                            bestType = "symbol";
                            matchLineNum = currLineNum;
                            bestMatch = Character.toString(myData.charAt(i));
                            createToken();
                        } else {
                            inString = true;
                            startPosMatch = lastPos + 1;
                            endPosMatch = currPos;
                            bestType = "symbol";
                            matchLineNum = currLineNum;
                            bestMatch = Character.toString(myData.charAt(i));
                            createToken();
                        }
                    //if there is a " and you are in a string, flip boolean and print the " token
                    } else if(Character.toString(myData.charAt(i)).equals("\"") && inString && !inComment){
                        inString = false;
                        startPosMatch = lastPos;
                        endPosMatch = currPos;
                        bestType = "symbol";
                        matchLineNum = currLineNum;
                        bestMatch = Character.toString(myData.charAt(i));
                        createToken();
                    }

                    //if you are in a string and an id is found at the current position, create a token for it
                    if(inString && findId(Character.toString(myData.charAt(i)))){
                        startPosMatch = lastPos;
                        bestMatch = Character.toString(myData.charAt(i));
                        createToken();
                        lastPos = i+2;
                        currPos = lastPos+1;
                        //if you are in a string and find anything other than a char, space, or nothing throw an error
                    } else if(inString && !(findId(Character.toString(myData.charAt(i))) ||
                            findSpace(Character.toString(myData.charAt(i))) ||
                            Character.toString(myData.charAt(i)).equals("\""))){
                        errCounter++;
                        throwUnrecognized(Character.toString(myData.charAt(i)), currLineNum, i);
                    }

                    //if you are in a string and find a space char, create a token for it
                    if(inString && findSpace(Character.toString(myData.charAt(i))) && !inComment){
                        startPosMatch = lastPos;
                        bestMatch = Character.toString(myData.charAt(i));
                        createToken();
                        lastPos = i+1;
                        currPos = lastPos;
                    }

                    //if you loop back around and the best match is equal to a symbol, create the token for it
                    if(findSymbol(bestMatch)){
                        //go back 1 char to where the symbol is
                        bestMatch = Character.toString(myData.charAt(i-1));
                        bestType = "Symbol";
                        startPosMatch = currPos-1;
                        //adjust the start position if dealing with a space vs. no space
                        if(bestMatch.equals("+") && findSpace(Character.toString(myData.charAt(i-2)))){
                            startPosMatch = currPos;
                        }
                        matchLineNum = currLineNum;
                        createToken();
                        //reset pointers to keep scanning
                        lastPos = i;
                        currPos = lastPos+1;
                        scanVal = Character.toString(myData.charAt(i));
                        startPosMatch = currPos;
                    }

                    //if the best match is a digit after looping through, create a token for it
                    if(findDigit(bestMatch)){
                        bestMatch = Character.toString(myData.charAt(i-1));
                        bestType = "Digit";
                        startPosMatch = currPos-1;
                        matchLineNum = currLineNum;
                        createToken();
                        lastPos = i;
                        currPos = lastPos+1;
                        scanVal = Character.toString(myData.charAt(i));
                        startPosMatch = currPos;
                    }

                    //if no boundaries are found such as symbols, and spaces
                    if(!findSymbol(Character.toString(myData.charAt(i))) &&
                            !findSpace(Character.toString(myData.charAt(i)))){
                        //search for a keyword and if it is true set the match
                        if(findKeyword(scanVal) && !inString && !inComment){
                            startPosMatch = lastPos;
                            endPosMatch = i;
                            bestMatch = scanVal;
                            bestType = "keyword";
                            matchLineNum = currLineNum;
                            //if nothing comes back search for an id
                        } else if(findId(scanVal) && !inString && !inComment){
                            startPosMatch = lastPos;
                            endPosMatch = i;
                            bestMatch = scanVal;
                            bestType = "id";
                            matchLineNum = currLineNum;
                            //if nothing comes back search for a digit
                        }else if (findDigit(scanVal) && !inString && !inComment) {
                            startPosMatch = lastPos;
                            endPosMatch = currPos;
                            bestMatch = scanVal;
                            bestType = "digit";
                            matchLineNum = currLineNum;
                        }
                    //if a boundary is found
                    } else {
                        //if the curr char is not a space
                        if (!findSpace(Character.toString(myData.charAt(i)))) {
                            //search for a symbol
                            if (findSymbol(Character.toString(myData.charAt(i)))) {
                                //if not the first position
                                if(i != 0) {
                                    //if the best match is not set, not in a comment, and there is no inequality
                                    if (bestMatch.equals("") && !inComment &&
                                            (!Character.toString(myData.charAt(i - 1)).equals("=") &&
                                                    !Character.toString(myData.charAt(i - 1)).equals("!"))) {
                                        startPosMatch = currPos;
                                        endPosMatch = currPos;
                                        //symbol being weird, create a token for it here
                                        if(Character.toString(myData.charAt(i)).equals("}")){
                                            startPosMatch = currPos;
                                            bestMatch = scanVal;
                                            createToken();
                                        }
                                        bestMatch = scanVal;
                                        bestType = "symbol";
                                        matchLineNum = currLineNum;
                                        if(Character.toString(myData.charAt(i)).equals(")") && Character.toString(myData.charAt(i-1)).equals("\"")){
                                            //System.out.println("HERE");
                                            createToken();
                                            if(currPos == myData.length()-1) {
                                                i = currPos + 1;
                                            }
                                        }
                                        //if best match exists with no inequality or comment characters
                                    } else if (!inComment && !bestMatch.equals("*") && !bestMatch.equals("/") &&
                                            !Character.toString(myData.charAt(i - 1)).equals("=") &&
                                            !Character.toString(myData.charAt(i - 1)).equals("!")) {
                                        //if the bestmatch was set to keyword
                                        if(findKeyword(bestMatch)){
                                            createToken();
                                            i = currPos;
                                        //if its not a keyword, we know that the best match has to be 1 position long
                                        }else{
                                            createToken();
                                            i = currPos;
                                        }
                                    }
                                    //if i = 0 and a symbol was found, create a token for it
                                } else {
                                    startPosMatch = lastPos;
                                    endPosMatch = currPos;
                                    bestMatch = scanVal;
                                    bestType = "symbol";
                                    matchLineNum = currLineNum;
                                    createToken();
                                }
                            }
                            //if the curr char is space
                        } else {
                            if (!bestMatch.equals("") && !inComment && !bestMatch.equals("*") && !bestMatch.equals("/") && i-2 > 0){
                                if(Character.toString(myData.charAt(i-2)).equals("(") ||
                                        findDigit(Character.toString(myData.charAt(i-1)))){
                                    startPosMatch = lastPos +1;
                                }else {
                                    startPosMatch = lastPos;
                                }
                                createToken();
                                i = currPos;
                                //curr char is space and best match isn't found yet
                            } else {
                                scanVal = "";
                                lastPos = currPos;
                            }
                        }
                    }
                    //if end of program sign is found create the last token
                    if(Character.toString(myData.charAt(i)).equals("$")){
                        //make sure not in comment
                        if(myData.length()-1 == i && !bestMatch.equals("") && !bestMatch.equals("*") && !inComment && !bestMatch.equals("/")){
                            startPosMatch = lastPos;
                            createToken();
                        }

                        //if an end quote was never found, throw a warning
                        if(inString){
                            warnCounter++;
                            throwWarning("String did not close");
                            inString = false;
                        }
                        //print the end of program info
                        printInfo("Lex completed with " + errCounter + " errors");
                        printInfo("Lex completed with " + warnCounter + " warnings");
                        inProgram = true;
                        System.out.println("");

                        //if no errors, move on to parse the program
                        if(errCounter == 0){
                            Parser parser = new Parser(true, tokenStream, programCounter);
                            parser.parse();
                        //else, skip due to lex errors
                        } else {
                            System.out.println("PARSER: skipped for program " +  programCounter + " due to Lexer error(s)");
                            System.out.println("");
                            System.out.println("CST skipped for program " +  programCounter + " due to Lexer error(s)");
                            System.out.println("");
                        }
                        if(myReader.hasNextLine()) {
                            errCounter = 0;
                            warnCounter = 0;
                            programCounter++;
                        }
                    }
                    //check for token at the end of the line
                    if(myData.length()-1 == i && !bestMatch.equals("") && !bestMatch.equals("*") &&
                            !inComment && !bestMatch.equals("/")){
                        createToken();
                        i = currPos;
                    }
                }//end for loop

                //if end of line and still inside string, throw error b/c new line is not allowed in string
                if(inString){
                    errCounter++;
                    throwUnrecognized("new line", currLineNum, currPos);
                    scanVal = "";
                }

                lastPos = 1;
                currPos = 0;
            }//end while loop

            //if got to end of file and did not find the EOP symbol, throw a warning
            if(!inProgram){
                //let the user know the warning
                if(verbose) {
                    throwWarning("Missing End of Program char: $");
                    System.out.println("Adding EOP Token...");
                }
                //fix the warning and move on to parse
                if(!inString && !inComment && errCounter == 0){
                    bestMatch = "$";
                    bestType = "symbol";
                    matchLineNum = currLineNum;
                    createToken();
                    System.out.println("");
                    Parser parser = new Parser(true, tokenStream, programCounter);
                    parser.parse();
                }
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * findKeyword
     *
     * searches a scanned value or bestmatch for the keywords in the grammar using regex
     *
     * @param myKeyword
     *
     * @return boolean if match is true
     */
    private boolean findKeyword(String myKeyword){
        boolean matchFound = false;
        String regexKeyword = "(while)|(print)|(string)|(if)|(int)|(boolean)|(true)|(false)";

        if (Pattern.matches(regexKeyword, myKeyword)) {
            matchFound = true;
        }

        return matchFound;
    }

    /**
     * findId
     *
     * searches a scanned value or bestmatch for the ids/chars in the grammar using regex
     *
     * @param myId
     *
     * @return boolean if match is true
     */
    private boolean findId(String myId){
        boolean matchFound = false;
        Pattern id1 = Pattern.compile("^[a-z]$");

        Matcher matcher1 = id1.matcher(myId);

        if(matcher1.find()) {
            matchFound = true;
        }
        return matchFound;
    }


    /**
     * findSymbol
     *
     * searches a scanned value or bestmatch for the symbols in the grammar using regex
     *
     * @param mySymbol
     *
     * @return boolean if match is true
     */
   private boolean findSymbol(String mySymbol){
       boolean matchFound = false;
       String regexSymbol = "[${}()=!+/*\"]";

       if (Pattern.matches(regexSymbol, mySymbol)) {
           matchFound = true;
       }

       return matchFound;
    }

    /**
     * findDigit
     *
     * searches a scanned value or bestmatch for the digits in the grammar using regex
     *
     * @param myDigit
     *
     * @return boolean if match is true
     */
    private boolean findDigit(String myDigit){
        boolean matchFound = false;
        Pattern digits = Pattern.compile("[0-9]");

        Matcher matcher0 = digits.matcher(myDigit);

        if(matcher0.find()) {
            matchFound = true;
        }

        return matchFound;
    }

    /**
     * findSpace
     *
     * searches a scanned value or bestmatch for a space character
     *
     * @param mychar
     *
     * @return boolean if match is true
     */
    private boolean findSpace(String mychar){
        boolean matchFound = false;
        Pattern id1 = Pattern.compile("\\s");
        Matcher matcher1 = id1.matcher(mychar);

        if(matcher1.find()) {
            matchFound = true;
        }

        return matchFound;
    }

    /**
     * createToken
     *
     * sets all the values of the token object and then calls print method to print it out in stream then resets values
     */
    private void createToken(){
        currToken.setValue(bestMatch);
        currToken.setLineNum(matchLineNum);
        currToken.setPosition(startPosMatch);
        currToken.setType(bestType);
        printToken(currToken);
        tokenStream.add(currToken);

        //Reset values and pointers
        currToken = new Token();
        matchFound = false;
        scanVal = "";
        bestMatch = "";
        bestType = "";
        lastPos = endPosMatch+1;
        currPos = endPosMatch;
        endPosMatch = 0;
        startPosMatch = 0;
    }

    /**
     * printToken
     *
     * prints out the info for the current token found by lexer
     *
     * @param currToken
     */
    private void printToken(Token currToken){
            currToken.setName(getTokenName());
        if(verbose) {
            System.out.println("DEBUG LEXER ---> " + currToken.getName() + " [ " + currToken.getValue() + " ] Found at ( " + currToken.getLineNum() +
                    " : " + currToken.getPosition() + " )");
        }
    }

    /**
     * getTokenName
     *
     * This method gets the name of a token so it is easier to read in the stream
     *
     * @return string with the name of the token passed in
     */
    private String getTokenName(){
        String nameVal = "";
        if(findKeyword(currToken.getValue())){
            switch(currToken.getValue()){
                case "print":
                    nameVal = "T_PRINT";
                    break;
                case "while":
                    nameVal = "T_WHILE";
                    break;
                case "if":
                    nameVal = "T_IF";
                    break;
                case "int":
                    nameVal = "T_INT";
                    break;
                case "boolean":
                    nameVal = "T_BOOL";
                    break;
                case "string":
                    nameVal = "T_STRING";
                    break;
                case "false":
                    nameVal = "T_FALSE";
                    break;
                case "true":
                    nameVal = "T_TRUE";
                    break;
            }
        }
        if(findId(currToken.getValue()) && inString){
            nameVal = "T_CHAR";
        } else if(findId(currToken.getValue()) && !inString){
            nameVal = "T_ID";
        }
        if(findSymbol(currToken.getValue())){
            switch(currToken.getValue()){
                case "{":
                    nameVal = "L_BRACE";
                    break;
                case "}":
                    nameVal = "R_BRACE";
                    break;
                case "(":
                    nameVal = "L_PAREN";
                    break;
                case ")":
                    nameVal = "R_PAREN";
                    break;
                case "=":
                    nameVal = "T_ASSIGN";
                    break;
                case "+":
                    nameVal = "T_INTOP";
                    break;
                case "\"":
                    nameVal = "T_QUOTE";
                    break;
                case "$":
                    nameVal = "T_EOP";
                    break;
            }
        }
        if(findDigit(currToken.getValue())){
            nameVal = "T_DIGIT";
        }
        if(findSpace(currToken.getValue())){
            nameVal = "T_CHAR";
        }
        if(currToken.getValue().equals("==")){
            nameVal = "T_EQUAL";
        }
        if(currToken.getValue().equals("!=")){
            nameVal = "T_UNEQUAL";
        }
        return nameVal;
    }

    /**
     * printInfo
     *
     * general print method to print a message
     *
     * @param msg String with info to be printed to the user
     */
    private void printInfo(String msg){
        System.out.println("INFO  LEXER ---> " + msg);
    }

    /**
     * throwUnrecognized
     *
     * method for throwing errors if there is an unrecognized symbol in the program that is not in the language grammar
     *
     * @param token
     * @param lineNum
     * @param pos
     */
    private void throwUnrecognized(String token, int lineNum, int pos){
        if(verbose) {
            System.out.println("Error: (" + lineNum + ":" + pos + ") Unrecognized Token: " + token);
        }
    }

    /**
     * throwWarning
     *
     * helper method to print out warning messages as they pop up during the lexical analysis
     *
     * @param msg of warning to be printed to the user
     */
    private void throwWarning(String msg){
        if(verbose) {
            System.out.println("Warning: " + msg);
        }
    }
}
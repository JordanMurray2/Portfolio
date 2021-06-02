/**
 * Symbol
 *
 * class that stores info about a variable that is declared in a program. It has the var name, type, scope it is located
 * in, the line number its positioned at, whether or not the scope its located in is closed, and  whether the var is used
 * or initialized. Used in SemanticAnalyzer.java to store all of the symbols in a program to print out symbol table, if
 * successful analyzation.
 */
public class Symbol {
    private String name;
    private String type;
    private int scope;
    private int lineNum;
    private boolean scopeClosed;
    private boolean used;
    private boolean initialized;

    /**
     * Symbol
     *
     * null constructor
     */
    public Symbol(){
        name = null;
        type = null;
        scope = 0;
        lineNum = 0;
        scopeClosed = false;
        used = false;
        initialized = false;
    }

    /**
     * Symbol
     *
     * full constructor that sets the name of the node
     *
     * @param name
     */
    public Symbol(String name, String type){
        this.name = name;
        this.type = type;
    }

    /**
     * getName
     *
     * getter for the name of a symbol
     *
     * @return String - the name
     */
    public String getName(){ return this.name; }

    /**
     * getType
     *
     * getter for the type of a symbol
     *
     * @return String - the type
     */
    public String getType(){ return this.type; }

    /**
     * getScope
     *
     * getter for the scope of a symbol
     *
     * @return int the scope number
     */
    public int getScope(){ return this.scope; }

    /**
     * getLineNum
     *
     * getter for the Line Number a symbol is located on
     *
     * @return int - the line number
     */
    public int getLineNum(){ return this.lineNum; }

    /**
     * getScopeClosed
     *
     * returns true or false if the scope the var is in is closed or not
     *
     * @return boolean - true/false depending if scope is closed for symbol
     */
    public boolean getScopeClosed(){ return this.scopeClosed; }

    /**
     * getUsed
     *
     * getter for the boolean to know if symbol has been used
     *
     * @return boolean - the value for used
     */
    public boolean getUsed(){ return this.used; }

    /**
     * getInitialized
     *
     * getter for initialized value to know if symbol was initialized
     *
     * @return boolean - the value for initialized
     */
    public boolean getInitialized(){ return this.initialized; }


    /**
     * setName
     *
     * sets the name of the symbol
     *
     * @param newName
     */
    public void setName(String newName){
        this.name = newName;
    }

    /**
     * setType
     *
     * sets the type of the symbol
     *
     * @param newType
     */
    public void setType(String newType){
        this.type = newType;
    }

    /**
     * setName
     *
     * sets the Scope of the symbol
     *
     * @param newScope
     */
    public void setScope(int newScope){
        this.scope = newScope;
    }

    /**
     * setLineNum
     *
     * sets the line number of the symbol
     *
     * @param newLineNum
     */
    public void setLineNum(int newLineNum){
        this.lineNum = newLineNum;
    }

    /**
     * setScopeClosed
     *
     * sets the boolean to true when scope has been closed
     *
     * @param closed
     */
    public void setScopeClosed(boolean closed){
        this.scopeClosed = closed;
    }

    /**
     * setUsed
     *
     * sets the boolean to true when symbol is used
     *
     * @param used
     */
    public void setUsed(boolean used){
        this.used = used;
    }

    /**
     * setInitialized
     *
     * sets the boolean to true when symbol is initialized
     *
     * @param initialized
     */
    public void setInitialized(boolean initialized){
        this.initialized = initialized;
    }
}

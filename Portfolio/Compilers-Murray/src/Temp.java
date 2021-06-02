/**
 * Temp
 *
 * class that will store temp values in a temp table for code generation
 */
public class Temp {
    private String tempAdd;
    private String name;
    private String addLength;
    private int scope;
    private int symbolCount;


    /**
     * Temp
     *
     * null constructor
     */
    public Temp() {
        tempAdd = null;
        name = null;
        addLength = null;
        scope = 0;
        symbolCount = 0;
    }

    /**
     * Temp
     *
     * full constructor that sets the values of the temp thing
     *
     * @param tempAdd -- temporary address that will be backpatched
     * @param name -- name of the variable
     * @param addLength -- the actual address that is backpatched
     * @param scope -- the scope the var is located in
     * @param symbolCount -- location in the symbol table
     */
    public Temp(String tempAdd, String name, String addLength, int scope, int symbolCount) {
        this.tempAdd = tempAdd;
        this.name = name;
        this.addLength = addLength;
        this.scope = scope;
        this.symbolCount = symbolCount;
    }
    /**
     * getTempAdd
     *
     * getter for temporary address
     *
     * @return String - the temp address
     */
    public String getTempAdd(){ return this.tempAdd; }

    /**
     * getName
     *
     * getter for the name of a node
     *
     * @return String - the name
     */
    public String getName(){ return this.name; }

    /**
     * getAddLength
     *
     * getter for the actual address of the var in the stack/heap
     *
     * @return String - the address
     */
    public String getAddLength(){ return this.addLength; }

    /**
     * getScope
     *
     * getter for the scope of a var
     *
     * @return int - the scope number
     */
    public int getScope(){ return this.scope; }

    /**
     * getSymbolCount
     *
     * getter for the location in the symbol table
     *
     * @return int - the array index
     */
    public int getSymbolCount(){ return this.symbolCount; }

    /**
     * setTempAdd
     *
     * sets the temporary address of the var
     *
     * @param newTemp -- the temp number
     */
    public void setTempAdd(String newTemp){
        this.tempAdd = newTemp;
    }

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
     * setAddLength
     *
     * sets the actual address of the var
     *
     * @param newLength -- the address in stack/heap
     */
    public void setAddLength(String newLength){
        this.addLength = newLength;
    }

    /**
     * setScope
     *
     * sets the scope of the var
     *
     * @param newScope
     */
    public void setScope(int newScope){
        this.scope = newScope;
    }

    /**
     * setSymbolCount
     *
     * sets the index located in symbol table
     *
     * @param newSymbolCount
     */
    public void setSymbolCount(int newSymbolCount){
        this.symbolCount = newSymbolCount;
    }
}
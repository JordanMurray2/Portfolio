/**
 * Token
 *
 * This class creates an object that will store info about a specific token that the lexer produces
 */
public class Token {
    String name;
    String value;
    int lineNum;
    int position;
    String type;

    //null constructor
    public Token(){}

    /**
     * full constructor
     * @param name
     * @param value
     * @param lineNum
     * @param position
     * @param type
     */
    public Token(String name, String value, int lineNum, int position, String type){
        this.name = name;
        this.value = value;
        this.lineNum = lineNum;
        this.position = position;
        this.type = type;
    }

    /**
     * getName
     *
     * @return name of token
     */
    public String getName(){
        return this.name;
    }

    /**
     * getValue
     *
     * @return value
     */
    public String getValue(){
        return this.value;
    }

    /**
     * getLineNum
     *
     * @return line number the token was found
     */
    public int getLineNum(){
        return this.lineNum;
    }

    /**
     * getPosition
     *
     * @return position on the line the token was found
     */
    public int getPosition(){
        return this.position;
    }

    /**
     * getType
     *
     * @return type of the token : ex) keyword, id, etc
     */
    public String getType(){
        return this.type;
    }

    /**
     * setName
     *
     * @param name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setValue
     *
     * @param value of token
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * setLineNum
     *
     * @param set the line number of the token
     */
    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    /**
     * setPosition
     *
     * @param set the posiiton on the line
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * setType
     *
     * @param set the type of the token
     */
    public void setType(String type) {
        this.type = type;
    }
}

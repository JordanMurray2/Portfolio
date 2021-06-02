/**
 * Compiler class
 *
 * this class will call all the componenets of the compiler
 */
public class Compiler {

    /**
     * main
     *
     * The main method will have instances of all the componenets of the compiler
     *
     * @param args
     */
    public static void main(String[] args) {
        //see the output of the different components of compiler
        boolean verbose = true;

        Lexer myLexer = new Lexer(verbose);
        if(args.length > 0) {
            myLexer.readFile(args[0]);
        }
    }
}

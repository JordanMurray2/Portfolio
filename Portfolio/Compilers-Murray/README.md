# Compilers-Murray
Java Compiler for Spring 2021

### Running Lexer
run `javac Compiler.java` to compile the file

then run `java Compiler.java <textfile>` and the lexer will automatically start processing the text file with sample programs

NOTE: there is a verbose mode option which is a boolean value in the Compiler class which can be set to true or false depending on if you want to see the token stream printed by the Lexer class. It is currently set to true

### Running Parser
run `javac Compiler.java` to compile the file

then run `java Compiler.java <textfile>` and the lexer will automatically start processing the text file with sample programs and pass the program to the parser, if it passes Lex

NOTE: there is a verbose mode option which is a boolean value in the Lexer class (where parser object is created) which can be set to true or false depending on if you want to see the parse process and CST printed or not. It is currently set to true

### Running Semantic Analysis
run `javac Compiler.java` to compile the file

then run `java Compiler.java <textfile>` and the lexer will automatically start processing the text file with sample programs and pass the program to the parser, if it passes Lex. From Parser, if successful, it will pass the program to semantic analysis. 


NOTE: there is a verbose mode option which is a boolean value in the Parser class (where semantic analyzer object is created) which can be set to true or false depending on if you want to see the parse process and CST printed or not. It is currently set to true

### Running Code Generation
run `javac Compiler.java` to compile the file
run `javac CodeGen.java` to compile the file

then run `java Compiler.java <textfile>` and the lexer will automatically start processing the text file with sample programs and pass the program to the parser, if it passes Lex. From Parser, if successful, it will pass the program to semantic analysis. If semantic analysis passes, it will then pass the AST and symbol table to code gen and generate code for that program to be run in the OS. 


### Labs and Output write-up
The labs are located in the labs folder under the main repository directory

The output write-up for the lexer is under project1LexerOutput

The output write-up for the parser is under project2ParserOutput

The output write-up for the semantic analyzer is under project3SemanticOutput

The output write-up for the code generation is under project4CodeGenOutput

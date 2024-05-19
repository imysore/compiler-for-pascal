package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * This class is a statement that represents a Writeln command.
 * 
 * It contains one instance variable: an Expression representing the expression contained within
 * the Writeln command.
 * 
 * To execute this statement, we simply execute the expression contained within the Writeln command
 * and print the result.
 * 
 * @author Ishan Mysore
 * @version 11/20/23
 */
public class Writeln extends Statement
{
    private Expression exp;
    
    /**
     * Creates a new Writeln and initializes the instance variable to the corresponding parameter.
     * @param exp an Expression representing the expression contained within the Writeln command
     * @postcondition the instance variable is set as described above
     */
    public Writeln(Expression exp)
    {
        this.exp = exp;
    }

    /**
     * Given the environment, executes this statement by executing the expression contained within
     * the Writeln command and printing the result.
     * @param env the environment with all the variables
     * @throws InterruptedException
     * @postcondition the Writeln statement has been executed, and the result has been printed
     */
    public void exec(Environment env) throws InterruptedException
    {
        System.out.println(exp.eval(env));
    }

    /**
     * Generates MIPS code for a WRITELN that moves the value in $v0 to $a0, prints the value in
     * $a0, and prints the newLine character.
     * @param e the Emitter that writes code to the output file
     */
    public void compile(Emitter e)
    {
        exp.compile(e);
        e.emit("move $a0 $v0");
        e.emit("li $v0 1");
        e.emit("syscall");
        e.emit("li $v0 4");
        e.emit("la $a0, nL\t# prints newline statement");
        e.emit("syscall");
    }
}

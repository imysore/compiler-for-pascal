package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * This abstract class represents a statement that we will parse. A statement can be one of
 * the following: Assignment, Block, Break, Continue, For, If, While, and Writeln.
 * 
 * The class also contains an abstract method called exec. The method will be overriden in
 * each individual subclass since each subclass executes and parses the statement 
 * differently.
 * 
 * @author Ishan Mysore
 * @version 11/20/23
 */
public abstract class Statement
{
    /**
     * Given an environment with all the variables, execute the statement.
     * Each subclass knows how to execute its type of statement given the environment, so 
     * this method will be overriden in each subclass.
     * @param env the environment with all the variables and their values
     * @throws InterruptedException if a CONTINUE statement is encountered
     */
    public abstract void exec(Environment env) throws InterruptedException;

    /**
     * Compile method for an statement; will be overriden in subclasses
     * @param e the Emitter that writes code to the output file
     * @throws RuntimeException if this method needs to be implemented in subclasses
     */
    public void compile(Emitter e)
    {
        throw new RuntimeException("Implement me!!!");
    }
}

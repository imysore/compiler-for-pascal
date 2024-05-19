package ast;

import java.util.*;

import emitter.Emitter;
import environment.Environment;

/**
 * This class is a statement that represents a block of statements, starting with BEGIN and ending
 * with END.
 * 
 * It contains one instance variable: a List of Statements that stores each statement in the block
 * of statements.
 * 
 * To execute this statement, we simply execute each statement in the list of statements.
 * 
 * @author Ishan Mysore
 * @version 11/20/23
 */
public class Block extends Statement
{
    private List<Statement> stmts;

    /**
     * Creates a new Block and initializes the instance variables to the corresponding
     * parameters.
     * @param stmts a List of statements that stores each statement in the block of statements
     * @postcondition the instance variables are set as described above
     */
    public Block(List<Statement> stmts)
    {
        this.stmts = stmts;
    }

    /**
     * Executes this statement by executing each statement in the list of statements.
     * @param env the Environment with all the variables
     * @postcondition all the statements in the block have been executed
     */
    public void exec(Environment env) throws InterruptedException
    {
        for (Statement stmt : stmts)
        {
            stmt.exec(env);
        }
    }

    /**
     * Generates MIPS code for a Block that compiles all the statements within the Block.
     * @param e the Emitter that writes code to the output file
     */
    public void compile(Emitter e)
    {
        for (Statement stmt : stmts)
        {
            stmt.compile(e);
        }
    }
}

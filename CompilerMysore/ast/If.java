package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * This class is a statement that represents an if-then-else block.
 * 
 * It contains three instance variables: a Condition representing the boolean expression that needs
 * to be satisfied, a Statement to execute if the condition is satisfied, and a Statement to execute
 * if the condition is not satisfied (null if one is not provided).
 * 
 * To execute this statement, we evaluate the former statement if the condition is satisfied. If it
 * is not satisfied, we evaluate the latter statement, if one is provided.
 * 
 * @author Ishan Mysore
 * @version 11/20/23
 */
public class If extends Statement
{
    private Condition condition;
    private Statement statement;
    private Statement elseStatement;

    /**
     * Creates a new If and initializes the instance variables to the corresponding parameters.
     * @param condition a Condition representing the boolean expression that needs to be satisfied
     * @param statement a Statement to execute if the condition is satisfied
     * @param elseStatement a Statement to execute if the condition is not satisfied (null if one
     * is not provided)
     */
    public If(Condition condition, Statement statement, Statement elseStatement)
    {
        this.condition = condition;
        this.statement = statement;
        this.elseStatement = elseStatement;
    }

    /**
     * Given an environment, executes this if-then-else statement by evaluating the former
     * statement if the condition is satisfied, or evaluating the latter statement (if one is 
     * provided) if the condition is not satisfied.
     * @param env the environment with all the variables
     * @postcondition the if statement has been executed
     */
    public void exec(Environment env) throws InterruptedException
    {
        if (condition.eval(env))
        {
            statement.exec(env);
        }
        else if (elseStatement != null)
        {
            elseStatement.exec(env);
        }
    }

    /**
     * Generates MIPS code for an If statement that generates a label name, compiles the
     * condition, compiles the statement to be executed if the condition is true, and then
     * jumps to the label that executes the remaining code. If an elseStatement is provided, then
     * the method also compiles the elseStatement and jumps to that target label.
     * @param e the Emitter that writes code to the output file
     */
    public void compile(Emitter e)
    {
        String label = "endif" + e.nextIfLabelID();
        condition.compile(e, label);
        statement.compile(e);
        e.emit("j " + label);
        e.emit(label + ":");
        if (elseStatement != null)
        {
            elseStatement.compile(e);
            e.emit("endif" + e.nextIfLabelID() + ":");
        }
    }
}
package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * This class is a statement that represents a while loop.
 * 
 * It contains two instance variables: a Condition representing a boolean expression that must be
 * satisfied in order for the loop to run, and a Statement representing the statement to execute
 * while the given condition is true.
 * 
 * To execute this statement, we simply execute the given statement while the given condition is
 * true.
 * 
 * @author Ishan Mysore
 * @version 11/20/23
 */
public class While extends Statement
{
    private Condition condition;
    private Statement statement;

    /**
     * Creates a new While and initializes the instance variables to the corresponding parameters.
     * @param condition a Condition representing a boolean expression that must be satisfied in
     * order for the loop to run
     * @param statement a Statement representing the statement to execute while the given condition
     * is true
     * @postcondition the instance variables are set as described above
     */
    public While(Condition condition, Statement statement)
    {
        this.condition = condition;
        this.statement = statement;
    }

    /**
     * Given the environment, executes this statement by executing the given statement while the
     * given condition is true.
     * If a BREAK statement is encountered, the method is terminated.
     * If a CONTINUE statement is encountered, we immediately advance to the next iteration.
     * @param env the environment with all the variables
     * @postcondition the while loop has been executed
     */
    public void exec(Environment env)
    {
        try
        {
            while (condition.eval(env))
            {
                statement.exec(env);
            }
        }
        catch (RuntimeException exp1)
        {
            
        }
        catch (InterruptedException exp2)
        {
            exec(env);
        }
    }

    /**
     * Generates MIPS code for a while loop that generates label names for the actual while loop
     * and for the code that executes after the while loop, compiles the condition, compiles the
     * statement, jumps back to the while loop until the condition is satisfied, and declares the
     * endLabel for the remainder of the code.
     * @param e the Emitter that writes code to the output file
     */
    public void compile(Emitter e)
    {
        int labelId = e.nextWhileLabelID();
        String label = "while" + labelId;
        String endLabel = "whileEnd" + labelId;
        e.emit(label + ":");
        condition.compile(e, endLabel);
        statement.compile(e);
        e.emit("j " + label);
        e.emit(endLabel + ":");
    }
}

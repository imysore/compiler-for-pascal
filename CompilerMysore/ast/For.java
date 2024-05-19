package ast;

import environment.Environment;

/**
 * This class is a statement that represents a for loop.
 * 
 * It contains five instance variables: a String representing the name of the loop variable, an
 * Expression representing the starting value for the loop variable, an Expression representing the
 * ending value for the loop variable, a Statement representing the statement to execute inside the
 * for loop, and an integer representing the current value of the loop variable.
 * 
 * To execute this statement, we simply execute the given statement a fixed amount of times,
 * incrementing the loop variable's value after every iteration.
 * 
 * @author Ishan Mysore
 * @version 10/14/23
 */
public class For extends Statement
{
    private String variable;
    private Expression start;
    private Expression end;
    private Statement statement;
    private int current;

    /**
     * Creates a new For and initializes the instance variables to the corresponding parameters.
     * @param variable a String representing the name of the loop variable 
     * @param start an Expression representing the starting value for the loop variable
     * @param end an Expression representing the ending value for the loop variable
     * @param statement a Statement representing the statement to execute inside the for loop
     * @postcondition the instance variables are set as described above
     */
    public For(String variable, Expression start, Expression end, Statement statement)
    {
        this.variable = variable;
        this.start = start;
        this.end = end;
        this.statement = statement;
    }

    /**
     * Given the environment, executes the given statements a fixed amount of times, incrementing
     * the loop variable's value after every iteration.
     * If a BREAK statement is encountered, the method is terminated.
     * If a CONTINUE statement is encountered, we immediately increment the loop variable's value 
     * and advance to the next iteration.
     * @param env the environment with all the variables
     * @postcondition the for loop has been executed
     */
    public void exec(Environment env) throws InterruptedException
    {
        try
        {
            current = start.eval(env);
            env.setVariable(variable, current);
            for (int i = current; i <= end.eval(env); i++)
            {
                statement.exec(env);
                current++;
                env.setVariable(variable, current);
            }
        }
        catch (RuntimeException exp1)
        {

        }
        catch (InterruptedException exp2)
        {
            current++;
            env.setVariable(variable, current);
            for (int i = current; i <= end.eval(env); i++)
            {
                statement.exec(env);
                current++;
                env.setVariable(variable, current);
            }
        }
        
    }


}

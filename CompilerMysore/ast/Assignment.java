package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * This class is a statement that represents the assigning of a given variable to a given value.
 * 
 * It contains two instance variables: a String representing the variable's name and an Expression
 * representing the variable's value.
 * 
 * To execute this statement, we simply call the environment's setVariable method, passing in the
 * variable's name and the evaluation of the variable's value.
 * 
 * @author Ishan Mysore
 * @version 12/14/23
 */
public class Assignment extends Statement
{
    private String var;
    private Expression exp;

    /**
     * Creates a new Assignment and initializes the instance variables to the corresponding
     * parameters.
     * @param var the name of the variable
     * @param exp the value of the variable
     * @postcondition the instance variables are set as described above
     */
    public Assignment(String var, Expression exp)
    {
        this.var = var;
        this.exp = exp;
    }

    /**
     * Given the environment, executes this statement by calling the environment's setVariable 
     * method to add a map entry with the variable's name and value.
     * @param env the Environment with all the variables
     * @throws InterruptedException
     * @postcondition a new map entry has been added with the variable's name and value
     */
    public void exec(Environment env) throws InterruptedException
    {
        env.setVariable(var, exp.eval(env));
    }

    /**
     * Generates MIPS code for the Assignment class.
     * If the variable is global, it compiles the value for the expression and pushes $v0 onto the
     * stack.
     * If the variable is local, it calculates the offset of the variable and stores it at that
     * position in the stack.
     * @param e the Emitter that writes code to the output file
     */
    public void compile(Emitter e)
    {
        exp.compile(e);

        if (e.isLocalVariable(var))
        {
            e.emit("sw $v0 " + e.getOffset(var) + "($sp)");
        }
        else
        {
            e.emit("la $t0 var" + var + "\t\t# loads the variable name into $t0");
            e.emit("sw $v0 ($t0)\t\t# stores the contents of $t0 into $v0");
        }
        
    }
}
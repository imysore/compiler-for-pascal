package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * This class is an expression that represents a variable.
 * 
 * It contains one instance variable: a String representing the name of the variable.
 * 
 * To evaluate this expression, we simply call the environment's getVariable method to retrieve
 * the value associated with this variable's name.
 * 
 * @author Ishan Mysore
 * @version 11/20/23
 */
public class Variable extends Expression
{
    private String name;
    
    /**
     * Creates a new Variable and initializes the instance variable to the corresponding parameter.
     * @param name a String representing the name of the variable.
     * @postcondition the instance variable is set as described above
     */
    public Variable(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    /**
     * Given an environment, evaluate this expression by calling the environment's getVariable
     * method to retrieve the value associated with this variable's name.
     * @return an integer representing the value associated with this variable's name
     */
    public int eval(Environment env)
    {
        return env.getVariable(name);
    }

    /**
     * Generates MIPS code for a variable.
     * If we are executing a procedure, and the variable is a local variable, then we calculate the
     * offset and load the variable from that position in the stack.
     * Otherwise, we load the variable's name into $t0, and then load the value associated with
     * that variable's address into $v0.
     * @param e the Emitter that writes code to the output file
     */
    public void compile(Emitter e)
    {

        if (e.getProcedureName() != null && e.isLocalVariable(name))
        {
            e.emit("lw $v0 " + e.getOffset(name) + "($sp)");
        }
        else
        {
            e.emit("la $t0 var" + name + "\t# loads variable name into $t0");
            e.emit("lw $v0 ($t0)\t# loads value associated with the variable's address into $v0");
        }
        
    }
}
package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * This class is an expression that represents an integer number.
 * 
 * It contains one instance variable: an integer representing the value of the number
 * 
 * To evaluate this expression, we simply return the value of the number.
 * 
 * @author Ishan Mysore
 * @version 11/20/23
 */
public class Number extends Expression
{
    private int value;
    
    /**
     * Creates a new Number and initializes the instance variable to the corresponding parameter.
     * @param value an integer representing the value of the number
     * @postcondition the instance variable is set as described above
     */
    public Number(int value)
    {
        this.value = value;
    }

    /**
     * Evaluates this expression by returning the value of the number
     * @return the value of the number
     */
    public int eval(Environment env)
    {
        return value;
    }

    /**
     * Generates MIPS code for this number that stores the number's value into $v0.
     * @param e the Emitter that writes code to the output file
     */
    public void compile(Emitter e)
    {
        e.emit("li $v0 " + value + "\t# stores number's value into $v0");
    }
}

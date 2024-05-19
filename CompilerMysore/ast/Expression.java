package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * This abstract class represents an expression that we will evaluate. A expression can be one of
 * the following: BinOp, Number, Variable.
 * 
 * The class also contains an abstract method called eval. The method will be overriden in
 * each individual subclass since each subclass evaluates the expression differently.
 * 
 * @author Ishan Mysore
 * @version 11/20/23
 */
public abstract class Expression
{
    /**
     * Given an environment with all the variables, evaluate the expression.
     * Each subclass knows how to evaluate its type of expression given the environment, so this 
     * method will be overriden in each subclass.
     * @param env the environment with all the variables and their values
     * @return an integer representing the evaluated expression
     * @throws InterruptedException
     */
    public abstract int eval(Environment env) throws InterruptedException;

    /**
     * Compile method for an expression; will be overriden in subclasses
     * @param e the Emitter that writes code to the output file
     * @throws RuntimeException if this method needs to be implemented in subclasses
     */
    public void compile(Emitter e)
    {
        throw new RuntimeException("Implement me!!!");
    }
}

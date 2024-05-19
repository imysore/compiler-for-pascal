package ast;

import environment.Environment;

/**
 * This class is a statement that represents the CONTINUE keyword.
 *
 * To execute this statement, we simply throw an InterruptedException to go to the next iteration
 * of the loop.
 * 
 * @author Ishan Mysore
 * @version 10/14/23
 */
public class Continue extends Statement
{
    /**
     * Throws an InterruptedException in order to go to the next iteration of the loop
     * @param env the environment with all the variables
     * @throws InterruptedException
     */
    public void exec(Environment env) throws InterruptedException
    {
        throw new InterruptedException();
    }
}

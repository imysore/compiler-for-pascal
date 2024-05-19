package ast;

import environment.Environment;

/**
 * This class is a statement that represents the BREAK keyword.
 *
 * To execute this statement, we simply throw a RuntimeException to break from the loop.
 * 
 * @author Ishan Mysore
 * @version 10/14/23
 */
public class Break extends Statement
{
    /**
     * Throws a RuntimeException in order to break from the loop.
     * @param env the environment with all the variables
     * @throws RuntimeException
     */
    public void exec(Environment env)
    {
        throw new RuntimeException();
    }
}

package ast;

import java.util.List;

import emitter.Emitter;
import environment.Environment;

/**
 * This class is an expression that represents a call to a procedure.
 * 
 * It contains two instance variables: a String representing the name of the procedure and a
 * List of Expressions representing the arguments inside the procedure call.
 * 
 * To evaluate this expression, we create a local environment and assign the corresponding
 * parameters to the corresponding arguments inside the local environment. Then, we execute the
 * statement inside the local environment, and return the variable with the same name as the
 * procedure name.
 * 
 * @author Ishan Mysore
 * @version 12/14/23
 */
public class ProcedureCall extends Expression
{
    private String name;
    private List<Expression> args;

    /**
     * Creates a new ProcedureCall and initializes the instance variables.
     * @param name a String representing the name of the procedure
     * @param args a List of Expressions representing the arguments inside the procedure call
     */
    public ProcedureCall(String name, List<Expression> args)
    {
        this.name = name;
        this.args = args;
    }

    /**
     * To evaluate this expression, we create a local environment and assign the corresponding
     * parameters to the corresponding arguments inside the local environment. Then, we execute the
     * statement inside the local environment, and return the variable with the same name as the
     * procedure name.
     * @param env the environment storing all the variables
     * @throws InterruptedException if a CONTINUE statement is encountered
     */
    public int eval(Environment env) throws InterruptedException
    {
        List<String> params = env.getProcedure(name).getParams();
        Statement statement = env.getProcedure(name).getStatement();
        Environment temp = env;
        if (temp.getParent() != null)
            temp = temp.getParent();
        Environment localEnv = new Environment(temp);
        for (int i = 0; i < args.size(); i++)
        {
            localEnv.declareVariable(params.get(i), args.get(i).eval(env));
        }
        localEnv.declareVariable(name, 0);
        statement.exec(localEnv);
        return localEnv.getVariable(name);
    }

    /**
     * Generates MIPS code for a procedure call.
     * First, it pushes the return address onto the stack and pushes all the arguments onto the
     * stack. Then, it jumps to the procedure. After the procedure is finished, it pops all the
     * arguments off the stack. Finally, it pops off the return address.
     * @param e the Emitter that writes code to the output file
     */
    public void compile(Emitter e)
    {
        e.emitPush("$ra");
        for (int i = 0; i < args.size(); i++)
        {
            args.get(i).compile(e);
            e.emitPush("$v0");
        }
        e.emit("jal proc" + name);
        for (int i = 0; i < args.size(); i++)
        {
            e.emitPop("$t0");
        }
        e.emitPop("$ra");
    }
}

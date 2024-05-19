package ast;

import java.util.List;

import emitter.Emitter;
import environment.Environment;

/**
 * This class is a statement that represents a declaration of a procedure.
 * 
 * It has four instance variables: a String representing the name of the procedure, a List of
 * Strings representing the parameters in the procedure, a Statement representing the statement
 * contained within the procedure, and a List of local variables that will be declared inside the
 * procedure.
 * 
 * To execute this statement, we simply tell the environment to add this instance to the procedure
 * symbol table.
 * 
 * @author Ishan Mysore
 * @version 12/14/23
 */
public class ProcedureDeclaration extends Statement
{
    private String name;
    private List<String> params;
    private List<String> localVars;
    private Statement statement;

    /**
     * Creates a new ProcedureDeclaration and initializes the instance variables.
     * @param name a String representing the name of the procedure
     * @param params a List of Strings representing the parameters in the procedure
     * @param localVars a List of local variables declared within the procedure
     * @param statement a Statement representing the statement contained within the procedure
     */
    public ProcedureDeclaration(String name, List<String> params, List<String> localVars, Statement statement)
    {
        this.name = name;
        this.params = params;
        this.localVars = localVars;
        this.statement = statement;
    }

    /**
     * Executes this statement by telling the environment to add this instance to the procedure
     * symbol table.
     */
    public void exec(Environment env) throws InterruptedException
    {
        env.setProcedure(name, this);
    }

    /**
     * Returns the name of the procedure.
     * @return the instance variable name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the statement contained within the procedure.
     * @return the instance variable statement
     */
    public Statement getStatement()
    {
        return statement;
    }

    /**
     * Returns the list of parameters that this procedure contains.
     * @return the instance variable params
     */
    public List<String> getParams()
    {
        return params;
    }

    /**
     * Returns the list of local variables declared within the procedure
     * @return the instance variable localVars
     */
    public List<String> getLocalVars()
    {
        return localVars;
    }

    /**
     * Generates MIPS code for a procedure declaration.
     * First, it declares the procedure name as a label. Next, it pushes the return value onto the
     * stack, and then pushes all the local variables onto the stack. After that, it sets the
     * procedure context, and executes the statement contained within the procedure. Once the
     * statement has finished execution, the method pops off all the local variables, pops off the
     * return value, jumps back to the main segment, and clears the procedure context.
     * @param e the Emitter that writes code to the output file
     */
    public void compile(Emitter e)
    {
        e.emit("proc" + name + ":");
        e.emit("# return value");
        e.emitPush("$zero");
        for (String localVar : localVars)
        {
            e.emitPush("$zero");
        }
        e.setProcedureContext(this);
        statement.compile(e);
        for (String localVar : localVars)
        {
            e.emitPop("$v0");
        }
        e.emitPop("$v0");
        e.emit("jr $ra");
        e.clearProcedureContext();
    }
}

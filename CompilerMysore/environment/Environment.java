package environment;

import java.util.HashMap;
import java.util.Map;

import ast.ProcedureDeclaration;

/**
 * The job of this class is to remember the values of variables and procedures.
 * 
 * It has two instance variables: a HashMap<String, Integer> where the key is the name of the
 * variable and the value is the value of the variable, and a HashMap<String, ProcedureDeclaration>
 * where the key is the name of the procedure and the value is the associated ProcedureDeclaration
 * 
 * The class also has getter and setter methods.
 */
public class Environment
{
    private Map<String, Integer> variables;
    private Map<String, ProcedureDeclaration> procedures;
    private Environment parent;

    /**
     * Creates a new Environment and initializes the instance variables.
     * @postcondition assignments is set to a new HashMap<String, Integer>
     */
    public Environment(Environment parent)
    {
        variables = new HashMap<String, Integer>();
        procedures = new HashMap<String, ProcedureDeclaration>();
        this.parent = parent;
    }

    /**
     * Returns the parent of the environment.
     * @return the instance variable parent
     */
    public Environment getParent()
    {
        return parent;
    }

    /**
     * Adds a new entry with the given variable's name and value, or changes the value of the
     * variable associated with the variable's name (if the name exists already)
     * @param variable a String representing the name of the variable
     * @param value an integer representing the value of the variable
     */
    public void declareVariable(String variable, int value)
    {
        variables.put(variable, value);
    }

    /**
     * If the variable is not contained in the parent environment, then this method puts the
     * variable and value as a new map entry in the parent environment. Otherwise, the method calls
     * declareVariable to put the variable in the local environment.
     * @param variable a String representing the name of the variable
     * @param value an integer representing the value of the variable
     */
    public void setVariable(String variable, int value)
    {
        if (variables.containsKey(variable))
        {
            variables.put(variable, value);
        }
        else if (parent != null && parent.getVariables().containsKey(variable))
        {
            parent.getVariables().put(variable, value);  
        }
        else
        {
            declareVariable(variable, value);
        }
    }

    /**
     * Given the variable's name, retrieve the value associated with the name. If the variable is
     * contained within the local environment, then we look for the variable inside the local
     * environment. Otherwise, we look for the variable inside the global environment.
     * @param variable a String representing the name of the variable
     * @return the value associated with the name of the variable
     */
    public int getVariable(String variable)
    {
        if (parent == null || variables.containsKey(variable))
        {
            return variables.get(variable);
        }
        return parent.getVariables().get(variable);
    }

    /**
     * Adds a new entry with the given procedure's name and value, or changes the value of the
     * procedure associated with the procedure's name (if the name exists already)
     * @param variable a String representing the name of the procedure
     * @param value a ProcedureDeclaration representing the value of the procedure
     */
    public void setProcedure(String name, ProcedureDeclaration procedure)
    {
        procedures.put(name, procedure);
    }

    /**
     * Given the procedure's name, retrieve the ProcedureDeclaration associated with the name.
     * @param variable a String representing the name of the procedure
     * @return the ProcedureDeclaration associated with the name of the procedure
     */
    public ProcedureDeclaration getProcedure(String name)
    {
        if (parent != null)
        {
            return parent.getProcedure(name);
        }
        return procedures.get(name);
    }

    /**
     * Returns the HashMap of variables.
     * @return the instance variable variables
     */
    public Map<String, Integer> getVariables()
    {
        return variables;
    }

    /**
     * Returns the HashMap of procedures.
     * @return the instance variable procedures
     */
    public Map<String, ProcedureDeclaration> getProcedures()
    {
        return procedures;
    }
}
package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * This class represents a boolean expression, or a condition.
 * 
 * It contains three instance variables: a String representing the boolean operator 
 * (=, <>, <, >, <=, >=), an Expression for the operand on the left side of the operator, and an
 * Expression for the operand on the right side of the operator.
 * 
 * To evaluate the boolean expression, we simply perform the operator on the two operands, and
 * return true or false.
 * 
 * @author Ishan Mysore
 * @version 11/20/23
 */
public class Condition
{
    private String relop;
    private Expression exp1;
    private Expression exp2;

    /**
     * Creates a new Condition and initializes the instance variables to the corresponding
     * parameters.
     * @param op a String representing the boolean operator (=, <>, <, >, <=, >=)
     * @param exp1 an Expression for the operand on the left side of the operator
     * @param exp2 an Expression for the operand on the right side of the operator
     * @postcondition the instance variables are set as described above
     */
    public Condition(String relop, Expression exp1, Expression exp2)
    {
        this.relop = relop;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    /**
     * Given the environment, evaluate this condition by performing the boolean operator
     * on the operands.
     * @param env the environment with all the variables
     * @return true if the operation is a true statement, false otherwise
     * @throws InterruptedException
     */
    public boolean eval(Environment env) throws InterruptedException
    {
        int exp1Eval = exp1.eval(env);
        int exp2Eval = exp2.eval(env);

        if (relop.equals("="))
        {
            return exp1Eval == exp2Eval;
        }
        if (relop.equals("<>"))
        {
            return exp1Eval != exp2Eval;
        }
        if (relop.equals("<"))
        {
            return exp1Eval < exp2Eval;
        }
        if (relop.equals(">"))
        {
            return exp1Eval > exp2Eval;
        }
        if (relop.equals("<="))
        {
            return exp1Eval <= exp2Eval;
        }
        return exp1Eval >= exp2Eval;
    }

    /**
     * Generates MIPS code for a Condition that compiles each side of the Condition (storing the 
     * first result in $t0 and the second result in $v0), and then compares the two results as
     * given by the relop. If the condition is not met, then the program will jump to the code at
     * the label given by target.
     * @param e the Emitter that writes code to the output file
     * @param target the label to jump to if the condition is not met
     */
    public void compile(Emitter e, String target)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);
        e.emitPop("$t0");
        if (relop.equals("<"))
        {
            e.emit("bge $t0 $v0 " + target + "\t# jumps to target label if $t0 is greater than or equal to $v0");
        }
        if (relop.equals("<="))
        {
            e.emit("bgt $t0 $v0 " + target + "\t# jumps to target label if $t0 is greater than $v0");
        }
        if (relop.equals("="))
        {
            e.emit("bne $t0 $v0 " + target + "\t# jumps to target label if $t0 doesn't equal $v0");
        }
        if (relop.equals(">="))
        {
            e.emit("blt $t0 $v0 " + target + "\t# jumps to target label if $t0 is less than $v0");
        }
        if (relop.equals(">"))
        {
            e.emit("ble $t0 $v0 " + target + "\t# jumps to target label if $t0 is less than or equal to $v0");
        }
        if (relop.equals("<>"))
        {
            e.emit("beq $t0 $v0 " + target + "\t# jumps to target label if $t0 equals $v0");
        }
    }
}

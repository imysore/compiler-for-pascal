package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * This class is an expression that represents a binary operator.
 * 
 * It contains three instance variables: a String representing the binary operator 
 * (+, -, *, /, mod), an Expression for the operand on the left side of the operator, and an
 * Expression for the operand on the right side of the operator.
 * 
 * To evaluate this expression, we simply add/subtract/multiply/divide/mod the two operands.
 * 
 * @author Ishan Mysore
 * @version 11/20/23
 */
public class BinOp extends Expression
{
    private String op;
    private Expression exp1;
    private Expression exp2;

    /**
     * Creates a new BinOp and initializes the instance variables to the corresponding
     * parameters.
     * @param op a String representing the binary operator (+, -, *, /, mod)
     * @param exp1 an Expression for the operand on the left side of the operator
     * @param exp2 an Expression for the operand on the right side of the operator
     * @postcondition the instance variables are set as described above
     */
    public BinOp(String op, Expression exp1, Expression exp2)
    {
        this.op = op;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    /**
     * Given the environment, evaluate this binary operator by performing the binary
     * operation on the operands.
     * @param env the environment with all the variables
     * @return an integer representing the result of the evaluation
     * @throws InterruptedException
     */
    public int eval(Environment env) throws InterruptedException
    {
        int exp1Eval = exp1.eval(env);
        int exp2Eval = exp2.eval(env);

        if (op.equals("+"))
        {
            return exp1Eval + exp2Eval;
        }
        if (op.equals("-"))
        {
            return exp1Eval - exp2Eval;
        }
        if (op.equals("*"))
        {
            return exp1Eval * exp2Eval;
        }
        if (op.equals("/"))
        {
            return exp1Eval / exp2Eval;
        }
        return exp1Eval % exp2Eval;
    }

    /**
     * Generates MIPS code for a BinOp that compiles each side of the BinOp (storing the first
     * result in $t0 and the second result in $v0), and then adds, subtracts, multiplies, or
     * divides the BinOp as appropriate.
     * @param e the Emitter that writes code to the output file
     */
    public void compile(Emitter e)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);
        e.emitPop("$t0");

        if (op.equals("+"))
        {
            e.emit("addu $v0 $t0 $v0\t# adds $v0 and $t0, stores result in $v0");
        }
        if (op.equals("-"))
        {
            e.emit("subu $v0 $t0 $v0\t# subtracts $v0 and $t0, stores result in $v0");
        }
        if (op.equals("*"))
        {
            e.emit("multu $v0 $t0\t# multiplies $v0 and $t0");
            e.emit("mflo $v0\t# stores result in $v0");
        }
        if (op.equals("/"))
        {
            e.emit("divu $t0 $v0\t# divides $v0 and $t0");
            e.emit("mflo $v0\t# stores result in $v0");
        }
    }
}

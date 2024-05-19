package ast;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import emitter.Emitter;
import environment.Environment;

/**
 * This class represents the complete program, and serves as the root of our AST.
 * 
 * It has three instance variables: a List of Variables representing the set of variables in the 
 * program, a List of ProcedureDeclarations representing the set of procedures in the program, and
 * a Statement representing the main body.
 * 
 * To execute this statement, we simply execute each procedure and then execute the main statement.
 * 
 * @author Ishan Mysore
 * @version 11/20/23
 */
public class Program
{
    private List<Variable> variables;
    private List<ProcedureDeclaration> procedures;
    private Statement mainStatement;

    /**
     * Creates a new Program and initializes the instance variables.
     * @param procedures a List of ProcedureDeclarations representing the set of procedures in the
     * program
     * @param mainStatement a Statement representing the main body
     */
    public Program(List<Variable> variables, List<ProcedureDeclaration> procedures, Statement mainStatement)
    {
        this.variables = variables;
        this.procedures = procedures;
        this.mainStatement = mainStatement;
    }

    /**
     * To execute this statement, we simply execute each procedure and then execute the main
     * statement.
     * @param env the environment storing all the variables
     * @throws InterruptedException if we encounter a CONTINUE statement
     */
    public void exec(Environment env) throws InterruptedException
    {
        for (ProcedureDeclaration procedure : procedures)
        {
            procedure.exec(env);
        }
        mainStatement.exec(env);
    }

    /**
     * Generates MIPS code for this file that prints the @author and @version,creates a .data 
     * section with all the variables (including a newline character), sets up the .text and 
     * .globl main sections, compiles the mainStatement, and finally sets up normal termination.
     * @param outputFile the output file to write the MIPS code to
     */
    public void compile(String outputFile)
    {
        Emitter e = new Emitter(outputFile);
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        e.emit("# This is an auto-generated file with MIPS Code generated from a Pascal program, using Ishan's compiler.");
        e.emit("# @author Ishan Mysore");
        e.emit("# @version " + formattedDate);
        e.emit(".data");
        e.emit("nL: .asciiz \"\\n\"");
        for (Variable var : variables)
        {
            e.emit("var" + var.getName() + ":\t.word 0");
        }
        e.emit(".text");
        e.emit(".globl main");
        e.emit("main:");
        mainStatement.compile(e);
        e.emit("li $v0, 10\t# normal termination");
        e.emit("syscall");
        for (ProcedureDeclaration p : procedures)
        {
            p.compile(e);
        }
        e.close();
    }
}
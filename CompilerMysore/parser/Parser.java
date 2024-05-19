package parser;

import java.util.ArrayList;
import java.util.List;

import ast.Assignment;
import ast.BinOp;
import ast.Block;
import ast.Break;
import ast.Condition;
import ast.Continue;
import ast.Expression;
import ast.For;
import ast.If;
import ast.Number;
import ast.ProcedureCall;
import ast.ProcedureDeclaration;
import ast.Program;
import ast.Statement;
import ast.Variable;
import ast.While;
import ast.Writeln;

import scanner.ScanErrorException;
import scanner.Scanner;

/**
 * This class implements a Parser for the Pascal language.
 * It possesses the ability to parse and compile Assignment, Block, Break, Continue, For, If, While,
 * ProcedureDeclaration, Program, and Writeln statements.
 * The parser prints the value that should be printed as a result of the program's execution, and
 * writes MIPS code that corresponds to the PASCAL code.
 * 
 * @author Ishan Mysore
 * @version 11/20/23
 */
public class Parser
{
    private Scanner scanner;
    private String currentToken;

    /**
     * Instantiates a new Parser and initializes the instance variables.
     * - scanner is initialized to the Scanner passed in as a parameter
     * - currentToken is initialized to the first token
     * - varMapInts is initialized to a new HashMap with keys as Strings and values as Integers
     * @param s the Scanner that this parser will use
     * @postcondition the instance variables are instantiated as described above
     * @throws ScanErrorException result of calling the nextToken() method of the Scanner class
     */
    public Parser(Scanner s) throws ScanErrorException
    {
        scanner = s;
        currentToken = scanner.nextToken();
    }

    /**
     * If the parameter passed into this method is equal to currentToken, then this method will
     * advance to the next token. Otherwise, this method will throw an IllegalArgumentException
     * @param expected the expected String that the scanner will "eat"
     * @postcondition currentToken equals the next token if currentToken equals expected
     * @throws ScanErrorException if currentToken does not equal expected
     */
    private void eat(String expected) throws ScanErrorException
    {
        if (currentToken.equals(expected))
        {
            currentToken = scanner.nextToken();
        }
        else
        {
            throw new ScanErrorException("Expected " + expected + " but received " + currentToken + ".");
        }
    }

    /**
     * Parses a number and returns the number parsed.
     * @precondition current token is an integer
     * @postcondition number token has been eaten
     * @return a Number representing the value of the parsed integer
     */
    private Number parseNumber() throws ScanErrorException
    {
        int num = Integer.parseInt(currentToken);
        eat(currentToken);
        return new Number(num);
    }

    /**
     * Parses a factor, which is any expression that might be multiplied or divided, or contains
     * a variable or procedure call.
     * @return an Expression representing the parsed factor
     * @throws ScanErrorException if currentToken does not equal expected
     */
    public Expression parseFactor() throws ScanErrorException
    {
        if (currentToken.equals("("))
        {
            eat("(");
            Expression num = parseExpression();
            eat(")");
            return num;
        }
        else if (currentToken.equals("-"))
        {
            eat(currentToken);
            return new BinOp("*", new Number(-1), parseFactor());
        }
        else if (currentToken.compareTo("0") >= 0 && currentToken.compareTo("9") <= 0)
        {
            Number num = parseNumber();
            return num;
        }
        String current = currentToken;
        eat(currentToken);
        if (currentToken.equals("("))
        {
            eat("(");
            List<Expression> args = new ArrayList<Expression>();
            while (! currentToken.equals(")"))
            {
                args.add(parseExpression());
                if (currentToken.equals(","))
                {
                    eat(",");
                }
            }
            eat(")");
            return new ProcedureCall(current, args);
        }
        return new Variable(current);
        
    }

    /**
     * Parses a term, which is any expression that might be added or subtracted.
     * @postcondition currentToken has been eaten
     * @return an Expression representing the parsed term
     * @throws ScanErrorException if currentToken does not equal expected
     */
    public Expression parseTerm() throws ScanErrorException
    {
        Expression val = parseFactor();
        while (currentToken.equals("*") || currentToken.equals("/") || currentToken.equals("mod"))
        {   
            if (currentToken.equals("*"))
            {
                eat("*");
                val = new BinOp("*", val, parseFactor());
            }
            else if (currentToken.equals("/"))
            {
                eat("/");
                val = new BinOp("/", val, parseFactor());
            }
            else if (currentToken.equals("mod"))
            {
                eat("mod");
                val = new BinOp("%", val, parseFactor());
            }
        }
        return val;
    }

    /**
     * Parses an expression, which is contained within the WRITELN statement.
     * @postcondition currentToken has been eaten
     * @return an Expression representing the parsed expression
     * @throws ScanErrorException if currentToken does not equal expected
     */
    public Expression parseExpression() throws ScanErrorException
    {
        Expression val = parseTerm();
        while (currentToken.equals("+") || currentToken.equals("-"))
        {   
            if (currentToken.equals("+"))
            {
                eat("+");
                val = new BinOp("+", val, parseTerm());
            }
            else if (currentToken.equals("-"))
            {
                eat("-");
                val = new BinOp("-", val, parseTerm());
            }
        }
        return val;
    }

    /**
     * Parses and returns a condition, which is a boolean expression.
     * @return a new Condition with the expression on the left of the boolean operator,
     * the boolean operator, and the expression on the right of the boolean operator.
     * @throws ScanErrorException if currentToken does not equal expected
     */
    public Condition parseCondition() throws ScanErrorException
    {
        Expression exp1 = parseExpression();
        String relop = currentToken;
        eat(currentToken);
        Expression exp2 = parseExpression();
        return new Condition(relop, exp1, exp2);
    }

    /**
     * Parses a statement.
     * 
     * If the currentToken equals "WRITELN", then the method returns an expression.
     * 
     * If the currentToken equals "BEGIN", then the method parses statements until the currentToken
     * equals "END", and returns the block of statements parsed.
     * 
     * If the currentToken equals "IF", then the method parses and returns a IF (condition) THEN 
     * (statement) or IF (condition) THEN (statement) ELSE (statement) block.
     * 
     * If the currentToken equals "WHILE", then the method parses and returns a WHILE (condition)
     * DO (statement) block.
     * 
     * If the currentToken equals "FOR", then the method parses and returns a FOR (variable) := (value)
     * TO (value) DO (statement) block.
     * 
     * If the currentToken equals "BREAK", then the method parses and returns a BREAK statement which
     * terminates the loop.
     * 
     * If the currentToken equals "CONTINUE", then the method parses and returns a CONTINUE statement
     * which advances to the next iteration of the loop.
     * 
     * If the currentToken equals anything else but the EOF token, then the method assumes that a
     * variable is being parsed and stores the parsed expression as the value for the variable. It then
     * returns this assignment.
     * 
     * @throws ScanErrorException if currentToken does not equal expected
     */
    public Statement parseStatement() throws ScanErrorException
    {
        if (currentToken.equals("WRITELN"))
        {
            eat("WRITELN");
            eat("(");
            Expression exp = parseExpression();
            eat(")");
            eat(";");
            return new Writeln(exp);
        }
        else if (currentToken.equals("BEGIN"))
        {
            eat("BEGIN");
            List<Statement> stmts = new ArrayList<Statement>();
            while (! currentToken.equals("END"))
            {
                stmts.add(parseStatement());
            }
            eat("END");
            eat(";");
            return new Block(stmts);
        }
        else if (currentToken.equals("IF"))
        {
            eat("IF");
            Condition condition = parseCondition();
            eat("THEN");
            Statement statement = parseStatement();
            Statement elseStatement = null;
            if (currentToken.equals("ELSE"))
            {
                eat("ELSE");
                elseStatement = parseStatement();
            }
            return new If(condition, statement, elseStatement);
        }
        else if (currentToken.equals("WHILE"))
        {
            eat("WHILE");
            Condition condition = parseCondition();
            eat("DO");
            Statement statement = parseStatement();
            return new While(condition, statement);
        }
        else if (currentToken.equals("FOR"))
        {
            eat("FOR");
            String variable = currentToken;
            eat(currentToken);
            eat(":=");
            Expression start = parseExpression();
            eat("TO");
            Expression end = parseExpression();
            eat("DO");
            Statement statement = parseStatement();
            return new For(variable, start, end, statement);
        }
        else if (currentToken.equals("BREAK"))
        {
            eat("BREAK");
            eat(";");
            return new Break();
        }
        else if (currentToken.equals("CONTINUE"))
        {
            eat("CONTINUE");
            eat(";");
            return new Continue();
        }
        else if (! currentToken.equals("EOF"))
        {
            String current = currentToken;
            eat(currentToken);
            eat(":=");
            Expression expression = parseExpression();
            eat(";");
            return new Assignment(current, expression);
        }
        return null;
    }

    /**
     * Parses the entire program by parsing all the variables, procedures, and main
     * statement. A procedure is in this syntax: PROCEDURE (args); VAR ____; (if any)
     * statement;
     * @return a new Program with a list of variables, procedures, and the main statement
     * @throws ScanErrorException if currentToken does not equal expected
     */
    public Program parseProgram() throws ScanErrorException
    {
        List<Variable> variables = new ArrayList<Variable>();
        while (currentToken.equals("VAR"))
        {
            eat("VAR");
            while (! currentToken.equals(";"))
            {
                variables.add(new Variable(currentToken));
                eat(currentToken);
                if (currentToken.equals(","))
                {
                    eat(",");
                }
            }
            eat(";");
        }
        List<ProcedureDeclaration> procedures = new ArrayList<ProcedureDeclaration>();
        while (currentToken.equals("PROCEDURE"))
        {
            eat("PROCEDURE");
            String name = currentToken;
            eat(currentToken);
            eat("(");
            List<String> params = new ArrayList<String>();
            while (! currentToken.equals(")"))
            {
                String param = currentToken;
                eat(currentToken);
                params.add(param);
                if (currentToken.equals(","))
                {
                    eat(",");
                }
            }
            eat(")");
            eat(";");
            List<String> localVars = new ArrayList<String>();
            if (currentToken.equals("VAR"))
            {
                eat("VAR");
                while (! currentToken.equals(";"))
                {
                    localVars.add(currentToken);
                    eat(currentToken);
                    if (currentToken.equals(","))
                    {
                        eat(",");
                    }
                }
                eat(";");
            }
            Statement statement = parseStatement();
            procedures.add(new ProcedureDeclaration(name, params, localVars, statement));
        }
        Statement mainStatement = parseStatement();
        return new Program(variables, procedures, mainStatement);
    }
}
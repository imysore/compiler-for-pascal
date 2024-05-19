package scanner;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

/**
 * 
 * NOTEBOOK ENTRY QUESTION
 * 
 * 
 * 1. If the next character had been a newline or an open parenthesis, we can also 
 *    conclude that we reached the end of the token representing "IF".
 * 
 */

/**
 * Scanner is a simple scanner for Compilers and Interpreters (2014-2015) lab exercise 1
 * @author Anu Datar
 *  
 * Usage:
 *      This scanner class reads in tokens from an input stream or a String, and classifies
 * them as a digit, letter, operator, or END OF FILE.
 * 
 * @author Ishan Mysore
 * @version 9/4/23
 *
 */
public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;

    /**
     * Scanner constructor for construction of a scanner that 
     * uses an InputStream object for input.  
     * Usage: 
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }
    /**
     * Scanner constructor for constructing a scanner that 
     * scans a given input string.  It sets the end-of-file flag an then reads
     * the first character of the input string into the instance field currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * Retrieves the next character from the input by reading it.
     * If the next character is -1, that means there are no more tokens
     * left and the eof (end of file) variable gets set to true.
     * @postcondition eof becomes true if currentChar == -1
     * @throws IOException if the file is not found
     */
    private void getNextChar()
    {
        try
        {
            currentChar = (char) (in.read());
            if (currentChar == -1)
            {
                eof = true;
            }
        }
        catch (IOException io) { io.printStackTrace(); }
        finally {}
    }
    /**
     * Advances to the next character if the character passed in as a parameter
     * equals the current character. If the parameter is not equal to the current
     * character, then the method throws a ScanErrorException
     * 
     * @param expected the character that should equal currentChar
     * @postcondition the method has advanced to the next character if expected
     * == currentChar
     * @throws ScanErrorException if expected != currentChar
     */
    private void eat(char expected) throws ScanErrorException
    {
        if (expected == currentChar)
        {
            getNextChar();
        }
        else
        {
            throw new ScanErrorException("Illegal character - expected currentChar and found char");
        }
    }

    /**
     * Determines if the char passed in as a parameter is a digit.
     * @param value a char passed in to check if it is a digit
     * @return true if value is a digit, false otherwise
     */
    public static boolean isDigit(char value)
    {
        return value >= '0' && value <= '9';
    }

    /**
     * Determines if the char passed in as a parameter is a letter.
     * @param value a char passed in to check if it is a letter
     * @return true if value is a letter, false otherwise
     */
    public static boolean isLetter(char value)
    {
        return value >= 'A' && value <= 'z';
    }

    /**
     * Determines if the char passed in as a parameter is a white space.
     * @param value a char passed in to check if it is a white space
     * @return true if value is a white space, false otherwise
     */
    public static boolean isWhiteSpace(char value)
    {
        return value == ' ' || value == '\t' || value == '\r' || value == '\n';
    }

    /**
     * Determines if the char passed in as a parameter is an operand.
     * @param value a char passed in to check if it is an operand
     * @return true if value is an operand, false otherwise
     */
    public static boolean isOperand(char value)
    {
        char[] operands = {'=','+','-','*','/','%','(',')',';',':','<','>','\'','\"'};
        for (char operand : operands)
        {
            if (value == operand)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves and returns a String representing a number lexeme.
     * @precondition currentChar is a digit to start
     * @postcondition currentChar has been advanced to the character
     * right after the last character in the lexeme
     * @return a String representing the lexeme found
     * @throws ScanErrorException if expected != currentChar
     */
    private String scanNumber() throws ScanErrorException
    {
        String result = "";
        while (isDigit(currentChar))
        {
            result += currentChar;
            eat(currentChar);
        }
        return result;
    }

    /**
     * Retrieves and returns a String representing an identifier lexeme.
     * An identifier starts with a letter and then continues until the
     * character is no longer a letter nor a digit.
     * @precondition currentChar is a letter to start
     * @postcondition currentChar has been advanced to the character
     * right after the last character in the lexeme
     * @return a String representing the lexeme found
     * @throws ScanErrorException if expected != currentChar
     */
    private String scanIdentifier() throws ScanErrorException
    {
        String result = "" + currentChar;
        eat(currentChar);
        while (isLetter(currentChar) || isDigit(currentChar))
        {
            result += currentChar;
            eat(currentChar);
        }
        return result;
    }

    /**
     * Retrieves and returns a String representing an operand lexeme.
     * <=, >=, <>, and := are counted as one token.
     * If the method encounters single or multi-line comments, it just
     * skips over them and eats until the character right after the comment
     * ends.
     * @precondition currentChar is an operand to start
     * @postcondition currentChar has been advanced to the character
     * right after the last character in the lexeme
     * @return a String representing the lexeme found. If the lexeme is a
     * comment, then the method returns the next token after the comment
     * (so it just calls nextToken)
     * @throws ScanErrorException if expected != currentChar
     */
    private String scanOperand() throws ScanErrorException
    {
        String result = "" + currentChar;
        eat(currentChar);
        if (result.equals("/") || result.equals("<") || result.equals(">") || result.equals(":")/* || result.equals("(")*/)
        {
            if (isOperand(currentChar))
            {
                result += currentChar;
                eat(currentChar);
            }
        }
        if (result.equals("//"))
        {
            while (currentChar != '\n')
            {
                eat(currentChar);
            }
            return nextToken();
        }
        if (result.equals("/*"))
        {
            handleMultiLineComments();
            eat(currentChar);
            return nextToken();
        }
        else if (result.length() > 0 && (!result.equals("<=") && !result.equals(">=") && !result.equals("<>") && !result.equals(":=") && !result.equals("((")))
        {
            result = result.substring(0,1);
        }
        return result;
    }

    /**
     * Helper method to handle multi-line comments.
     * @precondition we have just encountered an opening multi-line
     * comment
     * @postcondition currentChar has been advanced to the character
     * right after the closing multi-line comment
     * @throws ScanErrorException if expected != currentChar
     */
    private void handleMultiLineComments() throws ScanErrorException
    {
        while (currentChar != '*')
        {
            eat(currentChar);
        }
        eat(currentChar);
        if (currentChar != '/')
        {
            handleMultiLineComments();
        }
    }

    /**
     * Determines if there are any more characters in the file.
     * @return true if there are more characters in the file, false
     * if not
     */
    public boolean hasNext()
    {
        return !eof;
    }

    /**
     * Retrieves and returns the next token in the file, calling
     * the helper methods scanNumber, scanIdentifier, and scanOperand
     * appropriately. If the program encounters a period, it returns
     * the token "end" indicating the end of the file. If the starting
     * character of the token is not a digit, letter, nor operand, the
     * method throws a ScanErrorException indicating an unrecognized
     * character.
     * @return the next token
     * @throws ScanErrorException if expected != currentChar, or if we
     * encounter an unrecognized character
     */
    public String nextToken() throws ScanErrorException
    {
        while (hasNext() && isWhiteSpace(currentChar))
        {
            eat(currentChar);
        }
        if (!hasNext() || currentChar == '.')
        {
            eof = true;
            return "EOF";
        }
        if (isDigit(currentChar))
        {
            return scanNumber();
        }
        if (isLetter(currentChar))
        {
            return scanIdentifier();
        }
        if (isOperand(currentChar))
        {
            return scanOperand();
        }
        if (currentChar == ',')
        {
            eat(currentChar);
            return ",";
        }
        throw new ScanErrorException("Unrecognized character: " + currentChar);
    }
}
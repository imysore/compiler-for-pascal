package parser;

import java.io.File;
import java.io.FileInputStream;

import environment.Environment;
import scanner.Scanner;

/**
 * A tester for the Parser class.
 * @throws FileNotFoundException if the file path is not recognized
 * @throws ScanErrorException if expected != currentChar, or if we
 * encounter an unrecognized character
 * 
 * @author Ishan Mysore
 * @version 10/14/23
 */
public class ParserTester
{
    public static void main(String[] args) throws Exception
    {
        for (int i = 11; i <= 29; i++)
        {
            Scanner scanner = new Scanner(new FileInputStream(new File("/Users/ishanmysore/Documents/Ishan/Harker/10th Grade/ATCS - Compilers/CompilerMysore/parser/parserTest" + i + ".txt")));
            Parser parser = new Parser(scanner);

            Scanner scanner2 = new Scanner(new FileInputStream(new File("/Users/ishanmysore/Documents/Ishan/Harker/10th Grade/ATCS - Compilers/CompilerMysore/parser/parserTest" + i + ".txt")));
            Parser parser2 = new Parser(scanner2);
            Environment env = new Environment(null);

            System.out.println("Testing parserTest" + i + ".txt\n");
            while (scanner.hasNext())
            {
                parser.parseProgram().compile("MIPSOutput" + (i-10) + ".txt");
            }
            while (scanner2.hasNext())
            {
                parser2.parseProgram().exec(env);
            }
            System.out.println("\n");
        }
        System.out.println("All tests successful!!!");
        System.out.println("MIPS code has been generated for all tests in the output files.");
    }
}
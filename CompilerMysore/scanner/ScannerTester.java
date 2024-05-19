package scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * A tester for the Scanner class.
 * Tests two files -- ScannerTest and ScannerTestAdvanced.
 * @throws FileNotFoundException if the file path is not recognized
 * @throws ScanErrorException if expected != currentChar, or if we
 * encounter an unrecognized character
 */
public class ScannerTester {
    
    public static void main(String[] args) throws FileNotFoundException, ScanErrorException
    {
        Scanner scanner = new Scanner(new FileInputStream(new File("/Users/ishanmysore/Documents/Ishan/Harker/10th Grade/ATCS - Compilers/CompilerMysore/scanner/ScannerTestAdvanced.txt")));
        String token = scanner.nextToken();
        while (! token.equals("EOF"))
        {
            System.out.println(token);
            token = scanner.nextToken();
        }
        
        System.out.println("\n\n\n");

        Scanner scanner2 = new Scanner(new FileInputStream(new File("/Users/ishanmysore/Documents/Ishan/Harker/10th Grade/ATCS - Compilers/CompilerMysore/scanner/ScannerTest.txt")));
        String token2 = scanner2.nextToken();
        while (! token2.equals("EOF"))
        {
            System.out.println(token2);
            token2 = scanner2.nextToken();
        }
    }
}

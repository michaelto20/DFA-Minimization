import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;

public class DFAMinimization{
	public static void main(String[] args ){
		try{
			
		//Get file path for file containing DFA description
		String path = "";
	    Scanner in = new Scanner(System.in);
	    System.out.println("Please enter the absolute path of the file containing the DFA you wish to minimize.");
	    path = in.nextLine();
	    
	    //parse file into DFA
	    BadToTheBoneParser<String> parse = new BadToTheBoneParser<String>();
	    DFA nonMin = parse.ParseFile(path);
	    PrettifyDFA prettyDFA = new PrettifyDFA(nonMin);
	    prettyDFA.Run();
	    System.out.println(nonMin.ToString());
	    in.close();
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}
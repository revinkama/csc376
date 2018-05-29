
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class InputProgram {
	public static void main(String[] args){
		System.out.println("Standard Input: ");
		try{
			BufferedReader word= new BufferedReader(new InputStreamReader(System.in));
			String line;
			while((line=word.readLine())!=null && line.equalsIgnoreCase("terminate")==false){
				System.out.println(line);
			}
			commandLineArguments(args);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public static void commandLineArguments(String[] args){
		System.out.println("Command line arguments:");
		String opt1="";
		String opt2="";
		String opt3="";
		String fin = null;
		boolean opt1word=false;
		boolean opt2word=false;
		boolean opt3word=false;
		for(int i=0;i<args.length-1;i++){
			if(args[i].equals("-o")){
				if(args[i+1] != "-"){
					opt1=args[i+1];
					opt1word=true;
				}
			}
			else if(args[i].equals("-t")){
				if(args[i+1]!="-"){
					opt2= args[i+1];
					opt2word=true;
				}
			}
			else if(args[i].equals("-h")){
				opt3word=true;
			}
		}
		if(opt1.length()!=0 && opt1word==true)System.out.println("option 1: "+opt1);
		if(opt2.length()!=0 && opt2word==true)System.out.println("option 2: "+opt2);
		if(opt3word==true)System.out.println("option 3");
		//if(opt1 != null) fin = fin + opt1;
		//if(opt2 != null) fin = fin + opt2;
		//if(opt3 != null) fin = fin + opt3;
		//System.out.println(fin);
	}

}


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client{
	
	//global variables
	private Socket cS;
	private Scanner userInput;
	private PrintWriter output;
	private Scanner input; 
	String message;
	
	
	
	//Constructor
	public Client(int portNumber){
		try{
			this.cS = new Socket("localhost", portNumber);
			this.userInput = new Scanner (new InputStreamReader(System.in));
			this.output = new PrintWriter(cS.getOutputStream(), true);
			this.input = new Scanner(new InputStreamReader(cS.getInputStream()));
		}
		catch(Exception e){
			e.printStackTrace();
		}
			
	}
	
	
		
	public static void main(String[] args){
		int portNumber;
		try{
			//gets port number from command line
			portNumber = Integer.parseInt(args[0]);
		}
		catch(Exception e){
			//invalid port number
			System.out.println("Unexpected Input");
			return;
		}
		
		Client client = new Client(portNumber);
		client.execute();
		
	}
	
	
	//runs the write and read methods
	public void execute(){
		ExecutorService thread = Executors.newFixedThreadPool(5);
		thread.submit(this::readFromServer);
		thread.submit(this::writeToServer);
		thread.shutdown();
	}
		
		
	//reads messages from server
	public void readFromServer(){
		while(input.hasNextLine()){
			message = input.nextLine();
			if(message.length()==0){
				break;
			}
			System.out.println(message);
		}
		closeClient();
	}
	
	//writes messages to server
	public void writeToServer(){
		while(userInput.hasNextLine()){
			message = userInput.nextLine();
			if(message.length()==0){
				break;
			}
			output.println(message);
		}
		closeClient();
	}
	
	//closes client socket
	public void closeClient(){
		try{
			cS.shutdownOutput();
			cS.close();
			System.exit(0);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Messenger {
	 public static void main(String[] args) {
		 	MessengerInterface mess = null;
	        
	        try {

	            if (args[0].equals("-l")) {
	                mess = new Server(Integer.parseInt(args[1]));

	            }
	            else {
	            	mess = new Client(Integer.parseInt(args[0]));
	            }

	        } catch (Exception e) { 
	        	
	        }

	       
	        mess.run();
	        mess.stop();
	    }

}

class Server implements MessengerInterface{
	
	private ExecutorService threads;
	private Client client;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private BufferedReader input;
	private BufferedReader userInput;
	private PrintWriter output;
	String message;

	public Server(int portNumber) throws IOException {
		this.serverSocket = new ServerSocket(portNumber);
		this.clientSocket = serverSocket.accept();
		this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		this.userInput = new BufferedReader(new InputStreamReader(System.in));
		this.threads = Executors.newFixedThreadPool(2);
		this.output = new PrintWriter(clientSocket.getOutputStream(), true);
	}

	public void read() throws IOException {
		do{
			client.run();
		}
		while(this.clientSocket.isConnected());
		close();
	}

	public void write() throws IOException {
		do{
			client.run();
		}
		while (this.clientSocket.isConnected());
		close();
	}

	public void close() {
        try {
            serverSocket.close();
            clientSocket.shutdownOutput();
			clientSocket.close();
            System.exit(0);
        } catch (Exception e) {}
    }

	@Override
	public void run() {
		this.threads.submit(() -> {
			try {
				read();
			} catch (IOException e) {
			}
		});
		this.threads.submit(() -> {
			try {
				write();
			} catch (IOException e) {
			}
		});
	}

	@Override
	public void stop() {
		this.threads.shutdownNow();
	}

}

class Client implements MessengerInterface{

	private Socket clientSocket;
	private BufferedReader input;
	private BufferedReader userInput;
	private PrintWriter output;
	private ExecutorService threads;
	String option;

	public Client(int portNbr) throws IOException {
		this.clientSocket = new Socket("localhost", portNbr);
		this.userInput = new BufferedReader(new InputStreamReader(System.in));
		 this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		this.threads = Executors.newFixedThreadPool(2);
		this.output = new PrintWriter(clientSocket.getOutputStream(), true);
	}

	public void read() throws IOException {
		do{
			System.out.println("Enter an option: ('m', 'f', 'x'): ");
			System.out.println("	(M)essage (send)");
			System.out.println("	(F)ile (request)");
			System.out.println("    e(X)it");
			option = input.readLine();
			if(option.equalsIgnoreCase("m")){
				System.out.println("Enter Message: ");
				option = input.readLine();
			}
			else if(option.equalsIgnoreCase("f")){
				System.out.println("Enter File Name: ");
				option = input.readLine();
			}
			else{
				input.close();
				close();
				stop();
			}
		}
		while(this.clientSocket.isConnected());

	}

	public void write() throws IOException {
		do{
			System.out.println("Enter an option: ('m', 'f', 'x'): ");
			System.out.println("	(M)essage (send)");
			System.out.println("	(F)ile (request)");
			System.out.println("    e(X)it");
			option = output.readLine();
			if(option.equalsIgnoreCase("m")){
				System.out.println("Enter Message: ");
				option = output.readLine();
			}
			else if(option.equalsIgnoreCase("f")){
				System.out.println("Enter File Name: ");
				option = output.readLine();
			}
			else{
				input.close();
				close();
				stop();
			}
		}
		while(this.clientSocket.isConnected());
	}

	public void close() {
        try {
            clientSocket.shutdownOutput();
			clientSocket.close();
			System.out.println("closing your socket...goodbye");
            System.exit(0);
        } catch (Exception e) {}
    }

	@Override
	public void run() {
		this.threads.submit(() -> {
			try {
				read();
			} catch (IOException e) {
			}
		});
		this.threads.submit(() -> {
			try {
				write();
			} catch (IOException e) {
			}
		});
	}

	@Override
	public void stop() {
		this.threads.shutdownNow();
	}

}

interface MessengerInterface{
	public void run();
	public void stop();
}


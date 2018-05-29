import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//lets clients connect to server a communicate
public class Server{
	
	//global variables
	ServerSocket serverSock;
	HashMap<Socket, PrintWriter> sock;
	ExecutorService thread;
	
	
	//Constructor
	public Server(int portNumber){
		try{
			this.serverSock = new ServerSocket(portNumber);
			this.sock = new HashMap<Socket, PrintWriter>();
			this.thread = Executors.newFixedThreadPool(5);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
		
		
	//gets the port number from command line
	public static void main(String[] args){
		int portNumber;
		try{
			portNumber = Integer.parseInt(args[0]);
		}
		
		//invalid input for port number
		catch(Exception e){
            System.out.println("Invalid Port Number");
            return;
		}
		
		
		Server server = new Server(portNumber);
		server.listen();
		server.close();
	}
	
	//listens for client connection
	public void listen(){
		while(true){
			try{
				
				//connects with client and gets the output stream
				Socket client = serverSock.accept();
				PrintWriter output = new PrintWriter(client.getOutputStream(), true);
				
				//populates the hashmap/sockets
				sock.put(client, output);
				
				//thread to handle reading and writing between clients
				thread.submit(()-> handleMessages(client));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	//reads and writes between clients
	public void handleMessages(Socket s){
		try{
			String messages= null;
			String clientName = null;
			
			//reads input
			BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			while((messages = input.readLine())!= null){
				//if first word then it is their name
				if(clientName == null){
					clientName = messages;
				}
				else{
					for(Socket s1 : sock.keySet()){
						if(!s1.equals(s)){
							sock.get(s1).println(clientName + ": " + messages);
						}
					}
				}
			}
			//once standard input is closed, the socket is removed
			sock.remove(sock);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//closes all threads
	public void close(){
		thread.shutdown();
	}
}

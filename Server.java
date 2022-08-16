/**
 * 
 * The server class successfully creates a new Server, accepts new clients (with notification),
 * recieves data from the client(s), and provides a switch case that correlates with
 * the menu inside the client program, which handles the client's requests accordingly.
 * Finally, all printwriters, BufferedReaders, are closed, along with (optionally) the server
 * socket.
 * 
 * @authors: Keath Sawdo, Dayana Chapman, John Dayeh
 * @version: 2/23/21
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

public class Server implements Runnable
{
	Socket csocket;
	Server(Socket csocket)
	{
		this.csocket = csocket;
	}
	
	public static void main(String[] args) throws IOException
	{
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Please enter a port: ");
		int port = scan.nextInt();
		System.out.println(port);		
		ServerSocket serverSocket = new ServerSocket(port, 25);		
		
		System.out.println("Listening to port " + port);					
		while(true)
		{
			Socket socket = serverSocket.accept();							
			System.out.println("New client connected!");
			new Thread(new Server(socket)).start();
		}
	}
	
	public void run()
	{
		try
		{
			InputStream input = csocket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));																										//data from clients.
			String menuChoice = reader.readLine();																
			OutputStream output = csocket.getOutputStream();									
			PrintWriter writer = new PrintWriter(output, true);
				
			switch(menuChoice)
			{
				case "1":
				{	
					final String cmd = "date";										
																						 
																						
				    try {

				        Process process = Runtime.getRuntime().exec(cmd);

				        InputStream in = process.getInputStream();
				        InputStreamReader isr = new InputStreamReader(in);
				        BufferedReader br = new BufferedReader(isr);
				        String line;

				        while ((line = br.readLine()) != null) {					
				           writer.println(line);									
				        }

				    } catch (Exception e) {
				        e.printStackTrace(System.err);
				    } finally{
					        
				    }
				    break;
				}																	
				case "2":															
				{
					final String cmd = "uptime";

				    try {

				        Process process = Runtime.getRuntime().exec(cmd);

				        InputStream in = process.getInputStream();
				        InputStreamReader isr = new InputStreamReader(in);
				        BufferedReader br = new BufferedReader(isr);
				        String line;
					        
				        while ((line = br.readLine()) != null)
				        {														
				       		writer.println(line);
					        	
				        }
				    } catch (Exception e) {
				        e.printStackTrace(System.err);
				    } finally{
					        
				    }
						
					break;
				}
				case "3":
				{	
					final String cmd = "free";

				    try {

				        Process process = Runtime.getRuntime().exec(cmd);

				        InputStream in = process.getInputStream();
				        InputStreamReader isr = new InputStreamReader(in);
				        BufferedReader br = new BufferedReader(isr);
				        String line;
					        
				        while ((line = br.readLine()) != null)
				        {														
			        		writer.println(line);
					        	
				        }

				    } catch (Exception e) {
				        e.printStackTrace(System.err);
				    } finally{
					        
				    }
						
					break;
				}
				case "4":
				{
					final String cmd = "netstat";

				    try {

				        Process process = Runtime.getRuntime().exec(cmd);

				        InputStream in = process.getInputStream();
				        InputStreamReader isr = new InputStreamReader(in);
				        BufferedReader br = new BufferedReader(isr);
				        String line;
				        while ((line = br.readLine()) != null) {
				           writer.println(line);
				        }

				    } catch (Exception e) {
				        e.printStackTrace(System.err);
				    } finally{
					        
				    }
						
					break;
				}
				case "5":
				{
					final String cmd = "who -a";

				    try {

				        Process process = Runtime.getRuntime().exec(cmd);

				        InputStream in = process.getInputStream();
				        InputStreamReader isr = new InputStreamReader(in);
				        BufferedReader br = new BufferedReader(isr);
				        String line;

				        while ((line = br.readLine()) != null) {
				           writer.println(line);
				        }

				    } catch (Exception e) {
				        e.printStackTrace(System.err);
				    } finally{
					        
				    }
					break;
				}
				case "6":
				{
					final String cmd = "ps -aux";

				    try {

				        Process process = Runtime.getRuntime().exec(cmd);
				        InputStream in = process.getInputStream();
				        InputStreamReader isr = new InputStreamReader(in);
				        BufferedReader br = new BufferedReader(isr);
				        String line;

				        while ((line = br.readLine()) != null) {
				           writer.println(line);
				        }

				    } catch (Exception e) {
				        e.printStackTrace(System.err);
				    } finally{
					        
				    }
					break;
					
				}
				case "Q":																	
				{																			
					System.out.println("Thank you for trying our program!");
					System.out.println("\t- Keath Sawdo, Dayanna Chapman, John Dayeh");
						
					break;
				}
							
			}
			writer.flush();
			writer.close();																	
			reader.close();							
		} 
		catch(IOException e) 
		{
			System.out.println("Server Socket Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}

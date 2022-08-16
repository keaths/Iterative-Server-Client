/**
 * 
 * The client program successfully recieves the desired amount of clients from the user,
 * displays the menu of services, retrieves the users menu input, begins a loop delimited by
 * the amount of desired clients, and creates multiple thread(s) via the Threadpool array.
 * From their, each thread is joined so that completion of each thread is completed in one instance.
 * After recieving the data back from the clientThread class, the client retrieves the total times for
 * all clients via the public integer 'totalTime' in the class clientThread, calculates the average,
 * and displays each to the user.
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
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client 
{
	public static void main(String[] args) throws IOException
	{
			int i = 0;	
			Scanner scan = new Scanner(System.in);
			
			System.out.println("Please enter the port: ");
			int port = scan.nextInt();
			
			System.out.println("Please enter the address: ");
			String address = scan.next();
				
			System.out.println("client count: ");										//recieving desired amount of clients.
			int clientCount = scan.nextInt();
			while(clientCount <= 0 || clientCount > 100)
			{
				System.out.println("Error! Please enter a client count between 1 and 100: ");
				clientCount = scan.nextInt();
			}
			String menuChoice;
		do {
			
			////////////////////////////////////////
		
			System.out.println("\tMENU");
			System.out.println("1. Date & Time");
			System.out.println("2. Uptime");											//displaying the menu to the client.
			System.out.println("3. Memory Use");
			System.out.println("4. Netstat");
			System.out.println("5. Current Users");
			System.out.println("6. Running Processes");
			System.out.println("Q. Quit");
			
			////////////////////////////////////////
			
			System.out.println("\nmenu Choice: ");
			menuChoice = scan.next();
			while(!menuChoice.equals("1") && !menuChoice.equals("2") && !menuChoice.equals("3") && 
					!menuChoice.equals("4") && !menuChoice.equals("5") && !menuChoice.equals("6") && !menuChoice.equalsIgnoreCase("Q"))
			{
				System.out.println("- Error! Please enter a valid menu entry: ");		//if user enters invalid entry, loop persists
				menuChoice = scan.next();												//until valid entry is detected.
			}
			if (menuChoice.equalsIgnoreCase("Q"))										//if Q is selected, client closes, prints thank you.
			{
				System.out.println("\nThank you for trying our Program!\n\t- Keath Sawdo, Dayana Chapman, John Dayeh\n\n");
				System.exit(1);
			}
					
			/////////////////////////////////////////
			
			clientThread ThreadPool[] = new clientThread[clientCount];					//creating new clientThread objects/threads,
			for(i = 0; i < ThreadPool.length; i++)										//and beginning each thread.
			{
				ThreadPool[i] = new clientThread(address, port, menuChoice, i);			//counting index used to identify client
				ThreadPool[i].start();
			}
			for(i = 0; i < ThreadPool.length; i++)										//joining the threads to finish all at
			{																			//once.
				try {
					ThreadPool[i].join();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
			
			///////////////////////////////////////////
			
			System.out.println("\n----------------------------------------");
			System.out.println("- Sum time for all clients: " + ((double)clientThread.totalTime) + "ms");		//calculates the sum and average
			System.out.println("- Average time for all clients: " + (String.format("%.3f", 						//times by utilizing the clientThread's
					(double)clientThread.totalTime / (double)clientCount)) + "ms");								//public integer 'totalTime'.
			System.out.println("----------------------------------------");
			
			
			
		}while(menuChoice!= "Q");
		scan.close();
		
	}
	
}

/**
 * 
 * This class is responsible for executing each of the created client threads. 
 * It creates a new socket for each client, begins a timer, accepts the clients menu choice from
 * the client class, sends the choice to the server for calculation, in which the server
 * returns the correct case statement. The clientThread reads this, and displays it to the 
 * client console, with the timer stopped, and a total and average time displayed.
 * 
 * @author Keath Sawdo, Dayanna Chapman, 
 *
 */
	class clientThread extends Thread
	{
				
		public String ip;														//declaring clientThread's public variables.
		public int port;
		public String menuChoice;												//menuChoice assures that every client spawned
																				//will execute the same menu option, since 
		Socket socket = null;													//it is part of the clientThread object.
		public OutputStream output = null;
		public InputStream input = null;
		public PrintWriter writer = null;
		public BufferedReader reader = null;
		public String messageString = null;
		public int clientID;													//labels each client with a counting index.
		public static int totalTime = 0;										//initiates totalTime as zero for each thread.
		
		public clientThread(String ip, int port, String menuChoice, int clientID)
		{
			
			this.ip = ip;
			this.port = port;
			this.menuChoice = menuChoice;
			this.clientID = clientID;
		}
		
		public void run()
		{
			ArrayList<String> fullMSG = new ArrayList<String>();
			totalTime = 0;														//re-establishing totalTime as zero, so each thread will not
																				//continuously build upon the previously set value.
			/////////////////////////////////////
			
			try {
				socket = new Socket(ip, port);									//creating new socket to connect to serverSocket.
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}	
					
			//////////////////////////////////////////////////////
						
			try {
				output = socket.getOutputStream();
				input = socket.getInputStream();
			} catch (IOException e1) {											//declaring printwriters to send data to the server,
																				//and BufferedReaders to recieve data from the server.
				e1.printStackTrace();
			}

			writer = new PrintWriter(output, true);
			reader = new BufferedReader(new InputStreamReader(input));
			
			/////////////////////////////////////////////////////
					
			long startTime = System.currentTimeMillis();						//starting the timer for the roundtrip of each client's
			writer.println(menuChoice);											//command.
			
			
			
			try {
				if(menuChoice.equals("4") || menuChoice.equals("5") || menuChoice.equals("6") || menuChoice.equals("2") || 
						menuChoice.equals("3") || menuChoice.equals("1"))
				{																				//utilizing loop for multi-line menu options.
					while((messageString = reader.readLine()) != null)							
					{
						fullMSG.add(messageString);												//data recieved from server.
					}
					long endTime = System.currentTimeMillis();									
					long duration = endTime - startTime;										//total duration converted.
					
					System.out.println("--------------------------------------------------------------------------------\nclient " + clientID + ": ");
					int i = 0;
					for(i = 0; i <fullMSG.size(); i++)											//every element of String arraylist is printed.
					{
						System.out.println(fullMSG.get(i));
						
					}
					System.out.println("\nclient " + clientID + " time: " + duration + "ms");
					System.out.println("--------------------------------------------------------------------------------");		//and is displayed to client.							
												
					totalTime = totalTime + (int)duration;										//totalTime is now added onto by the duration.
				}
	
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			////////////////////////////////////////////////////////
																					
			try {
				socket.close();																	//closing current socket.
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
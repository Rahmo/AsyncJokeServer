/** 
 * 
 1. Abdulrahman Alzahrani / 1/25/2015:

 2. JavaSE[1.8]

 3. Precise command-line compilation examples / instructions:

 e.g.:

 > javac JokeServer.java

 > javac JokeClient.java

 > javac JokeClientAdmin.java

 > java JokeServer

 > java JokeClient

 > java JokeClientAdmin


 4. Precise examples / instructions to run this program:

 e.g.:

 In separate shell windows:

 >   Java JokeServer
 2nd Thread: Abdulrahman's Admin looper is starting up 
 1st Thread: Abdulrahman's Joke server starting up.

 In another shell window 
 >Java JokeClient
 Abdulrahman's Joke Client, 1.8.

 Using server: localhost, Port: 45001
 Please enter your name  rahmo

 Press Enter to get Jokes or Proverbs 
 The valid inputs are : 
 (quit) to close the client. 

 Hey:Rahmo  D.Why doesn't NASA send cows to space?  
 Because the steak would be too high.


 In another shell window  
 >Java JokeClientAdmin
 Abdulrahman Joke Client Admin, 1.8.

 Using server: localhost, Port: 45005
 Enter the mode that you want to switch: 
 The valid inputs are  : 
 a) Joke mode 
 b)Proverb mode 
 c) Maintance mode 
 b
 you chose a Proverb mode


 5. List of files needed for running the program.

 e.g.:


 a. JokeServer.java
 b. JokeClient.java
 c.  JokeClientAdmin.java

 5. Notes:



 ----------------------------------------------------------*/

import java.awt.List;
import java.io.*; // Get the Input Output libraries
import java.net.*; // Get the Java networking libraries
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.Callable;

public class JokeClient {
	
	public static boolean portTaken = false ;
	  public static  String Joke ;
		static int PORT = 4555;
		static boolean switcher = false;
		  static MulticastSocket sk;
		    static byte[] buf = new byte[1000];
	 public static 	SumLooper AL = new SumLooper(); // instantiate an object
	 public static	Thread t = new Thread(AL); // apply it to another thread
	@SuppressWarnings("static-access")
	public static void main(String args[]) {
		String serverName;
		if (args.length < 1)
			serverName = "localhost";
		else
			serverName = args[0];
		System.out.println("Abdulrahman's Joke Client, 1.8.\n");
		System.out.println("Using server: " + serverName + ", Port: 45001");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			String UsrName;
			UUID ClientKey = UUID.randomUUID(); // Universally unique identifier
												// intialize in the client to be
												// sent to the server
			System.out.print("Please enter your name  "); // ask the client a
															// name so it can be
															// used for him to
															// be tracked
		
				System.out.flush();
			UsrName = in.readLine(); // get the user name for the client
			String Order;
			

//				
			
			do {
				
				System.out
						.printf("\n Press Enter to get Jokes or Proverbs \n The valid inputs are : \n (quit) to close the client. ");
			
				
				Order = in.readLine();// this is each command that is inserted
										// by the client so i can tack if
										// entered Quit
				System.out.flush();
			String s = 	t.getState().toString(); 
		
		if (t.getState().toString()=="NEW")
		{
			t.start();
		}
		if( t.getState().toString()== "TERMINATED")
		{
			 SumLooper AL = new SumLooper(); // instantiate an object
			 Thread t = new Thread(AL); // apply it to another thread
			 t.start();
		}
				
				if (!(Order.indexOf("quit") < 0) == false) {
					
					
					
					SendInfo(UsrName, ClientKey, serverName);
					
					
					// execute this
															// function to
																// process
																// usrname and
																// UUID and the
																// name and the
																// command
				}

			} while (Order.indexOf("quit") < 0);

		} catch (IOException x) {
			x.printStackTrace();
		} 
	}

	@SuppressWarnings("static-access")
	static
	 void  SendInfo(String name, UUID ClientKey, String serverName) {
		
		 
		 
		Socket sock;
		BufferedReader fromServer;
		PrintStream toServer;
		String textFromServer;
		
		try {
			sock = new Socket(serverName, 45001); /*
												 * Open connection to server
												 * port, using 1577
												 */
			// Create filter I/O streams for the socket:
			fromServer = new BufferedReader(new InputStreamReader(
					sock.getInputStream()));
			toServer = new PrintStream(sock.getOutputStream());
			// Send machine name or IP address to server:
			toServer.println(name); // so the user get the name and output in
									// his panel that this user is connected
			// toServer.println(name); // Send the joke client name to joke
			// server
			toServer.println(ClientKey);// send the unique identifier to the
										// server so the server track it in a
										// hashmap
			toServer.flush();
			
			
			// Read two or three lines of response from the server,
			// and block while synchronously waiting:
			
			//*** Set up UDP ***//
		
		
		
	
	
//		//*** Finiash UDP set up***//
		
		
				

			      
				//-*****************************************************//
				 if (portTaken == false){
			 				  InetAddress address = InetAddress.getLocalHost();
			 		    		sk =  new MulticastSocket(PORT); 
			 		    	

			 		    		portTaken = true;
			 		    	} 
				
				 while(JokeClient.switcher == false){
			try {  
					
			 			

			 			 if (JokeClient.switcher != true)
							{
			 		
			 				
			 				 if (!sk.isClosed()){	
			 			 DatagramPacket dgp = new DatagramPacket(buf, buf.length);
							 sk.receive(dgp);
//							 sk.getReuseAddress();
//							 sk.setBroadcast(true);
							 
							// sk.setSoTimeout(20000);
							 sk.close();
							 portTaken = false;
							 t.interrupt();
							// sk.disconnect();
							
							 Joke = new String(dgp.getData());
							
					  if (SumLooper.SumValid == false){
								 	System.out.println(Joke);
							 }
//							Thread.currentThread().wait();
							
			 	}
			 		
						//	JokeClient.sk.close();
							JokeClient.switcher =true;
							
							}
						
						
						
					
						
					 }
			catch (SocketTimeoutException e){
				
			}
				catch (SocketException e) {
				
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				} 
			
				//***********************************************************//

			
			//TODO calculate 
				
//				if (textFromServer != null)
//					System.out.println(textFromServer);
			
				
			}} catch (IOException x) {
			System.out.println("Socket error.");
			x.printStackTrace();
		}
	}

}
	class SumLooper  implements  Runnable  {
		static boolean SumValid = false;
		int PORT = 4555;
		  DatagramSocket sk;
		@SuppressWarnings({ "null", "deprecation" })
		@Override
		public void run() {
			
//			synchronized(this){
			while(true)
			{
				
				if (JokeClient.switcher == true){
//					System.out.println("The results are ready /n "+ " " + JokeClient.Joke.toString() );
//				
					JokeClient.switcher = false;
					
//					Thread.currentThread().notify();
					//JokeClient.sk.disconnect();
					break;
//				}
				
				}
				
//				if (JokeClient.listOfSymbols.size()>=1)
//				{
//					System.out.println("results from the server are ready");
//					System.out.println(JokeClient.listOfSymbols.toString()+" nice");
//					
//			      
//					
//				} 
				
			System.out.printf("While waiting Proverbs/Jokes please enter numbers to sum to play :-) ");
			
			
			BufferedReader Numbers = new BufferedReader(new InputStreamReader(System.in));
		
			 String num = null;
			try {
				System.out.flush();
				num = Numbers.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			 String[] strArray = num.split(" ");int sum = 0;
		      int[] intArray = new int[strArray.length];
		      for(int j = 0; j < strArray.length; j++) {
		    	  try{
			    		 intArray[j] = Integer.parseInt(strArray[j]);
			          sum += intArray[j];
			    	 }
			    	 
			    	 catch (NumberFormatException nfe) {
			    		
			    		
			    	 continue;
			    	 }}
		     
			 System.out.println(sum);
			 SumValid = false;
			 }
			 
			
//				while(running) {
//			        f();
//			        if (Thread.interrupted()) {
//			        	Thread.currentThread().interrupt();
//			        }
//				}
		} 
		
		
		 
		   
			
			
			
			
			
		}
		
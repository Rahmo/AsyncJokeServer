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
import java.io.*; // Get the Input Output libraries
import java.net.*; // Get the Java networking libraries
import java.util.*;

public class JokeServer {
	public static boolean controlSwitch = true; // to turn the system on or off
												// but i haven't add the
												// shutdown feature
	public static HashMap<String, ActivityTracker> UsersTracker = new HashMap<String, ActivityTracker>();// this
																											// is
																											// useful
																											// for
																											// having
																											// a

	// a clientID and its jokes and proverbs that are inside another class

	public static void main(String a[]) throws IOException {
		int q_len = 6; /* Number of requests for OpSys to queue */
		int port = 45001; // the port
		AdminLooper AL = new AdminLooper(); // instantiate an object
		Thread t = new Thread(AL); // apply it to another thread
		t.start(); // ...and start it, waiting for administration input
		ServerSocket servsock = new ServerSocket(port, q_len);
		System.out
				.println("1st Thread: Abdulrahman's Joke server starting up.\n");
		Socket sock;
		while (controlSwitch) {

			sock = servsock.accept(); // wait for the next client connection
			new WorkerClient(sock).start(); // Spawn worker to handle it
		}
	}
}

class AdminLooper implements Runnable { // The second thread
	public static boolean adminControlSwitch = true;
	public static String mode = "a";

	public void run() { // RUNning the Admin listen loop
		System.out
				.println("2nd Thread: Abdulrahman's Admin looper is starting up ");
		int q_len = 6; /* Number of requests for OpSys to queue */
		int port = 45005; // We are listening at a different port for Admin
		// clients
		Socket sock;
		try	 {
			ServerSocket servsock = new ServerSocket(port, q_len);
			while (adminControlSwitch) {
				sock = servsock.accept(); // wait for the next client connection
				new AdminWorker(sock).start(); // Spawn worker to handle it
			}
		} catch (IOException ioe) {
			System.out.println(ioe);
		}

	}

}

	


class AdminWorker extends Thread {
	/*
	 * JokeClientAdmin thread check to administer the mode between jokes and
	 * proverbs and maintainance and change the mode according to the input
	 */
	Socket sock; // Class member, socket, local to Worker.
	String mode;// the mode is public so it can be used by all

	AdminWorker(Socket s) { // Constructor, assign arg s to local sock
		sock = s;
	}

	public void run() {

		PrintStream ToClient = null; // to stream an output to the client
		BufferedReader FromClient = null;// to get an input from the client
		try {
			FromClient = new BufferedReader( // to buffer a character input
					// stream
					// //from Client
					new InputStreamReader(sock.getInputStream()));// the buffer
			// read the
			// stream
			// input
			// from that
			// socket
			ToClient = new PrintStream(sock.getOutputStream());// to set up the
			// printstream to
			// listen to sock
			// //to client
			// Note that this branch might not execute when expected:
			if (AdminLooper.adminControlSwitch != true) {
				ToClient.println("Server is off !!!");
			} else
				try {

					String mode = FromClient.readLine();
					System.out.println("the current running mode is " + mode); // print
																				// out
																				// in
																				// the
																				// server
																				// panel
																				// whats
																				// been
																				// recieved
					PrintToClient(mode, ToClient); // also printing it to the
													// client JokeClientAdmin to
													// make sure of it

				} catch (IOException x) {
					System.out.println("Server read error");
					x.printStackTrace();
				}
			sock.close(); // close this connection, but not the server
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

	static void PrintToClient(String mode, PrintStream out) { // getting the
																// inserted mode
																// and print it
																// to client
																// what is
																// chosen

		try {
			String txt = GetModeTxt(mode); // making a check of according
											// whatever choosen have same
											// equivilant txt to output it
			out.println(txt);
		} catch (Exception ex) {
			out.println("Failed in atempt to look up " + mode);
		}
	}

	static String GetModeTxt(String Mode) { // switch helper and set set the
											// mode according to user input
		String txt = null;
		switch (Mode) {

		case "a":
			AdminLooper.mode = "a";
			txt = "you chose a joke mode";
			break;
		case "b":
			AdminLooper.mode = "b";
			txt = "you chose a Proverb mode";
			break;
		case "c":
			AdminLooper.mode = "c";
			txt = "you chose Maintaincne mode";
			break;
		default:
			txt = "Invalid inputs";
			break;
		}

		return txt;
	}
}


class Clientlooper implements Runnable {
	int PORT = 4555;
	  DatagramSocket sk;
	    byte[] buf = new byte[1000];
	  boolean Sumloop = false;
	  boolean portTaken = false;
	@Override
	public void run() {	
	
		try {

			InetAddress address = InetAddress.getLocalHost();
	    DatagramPacket dgp = new DatagramPacket(buf, buf.length,address,4555);
//	    if (portTaken == false){
//	    		sk = new DatagramSocket(PORT); 
//	    	}
	        portTaken = true;
	        sk.setReuseAddress(true);
			System.out.println("UDP Server connection has started");
			 while (true) {
				 sk.receive(dgp);
				 Sumloop = true;
			      String rcvd = new String(dgp.getData(), 0, dgp.getLength()) ;
			      System.out.println(rcvd);
			      
			      String[] strArray = rcvd.split(" ");int sum = 0;
			      int[] intArray = new int[strArray.length];
			      for(int i = 0; i < strArray.length; i++) {
			    	 try{
			    		 intArray[i] = Integer.parseInt(strArray[i]);
			          sum += intArray[i];
			    	 }
			    	 
			    	 catch (NumberFormatException nfe) {
			    		  // Handle the condition when str is not a number.
			    		
			    	 continue;
			          }
			        
			      }
System.out.println("UDP process is ongoing ");
//			      BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		      int outMessage = sum; 
		      buf = ("Sum is: " + outMessage).getBytes();
		      DatagramPacket out = new DatagramPacket(buf, buf.length, dgp.getAddress(), 4555);
		      sk.send(out);
		      System.out.flush();
		    
		      
		      Sumloop = false;
			 }	
		  
		} 
		 catch (SocketException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			 portTaken = true;
			e.printStackTrace();
		}
	
		
		finally {
	        if (sk != null) {
	            sk.close();
	        }
	 } 
	   
	
	}  
}
class WorkerClient extends Thread {// JokeClient Thread
	Socket sock; // Class member, socket, local to Worker.
	int PORT = 4555;
	WorkerClient(Socket s) { // Constructor, assign arg s to local sock
		sock = s;
	}

	public void run() {
//		Clientlooper l = new Clientlooper();
//		Thread c = new Thread();
//		c.start();
//		try {
//			Thread.sleep(40000);
//			if (l.Sumloop == true) {
//				Thread.class.wait();
//			}
//			
//			
//		} catch (InterruptedException e) {
//		
//			e.printStackTrace();
//		}
		
		
 
		System.out.println(JokeServer.UsersTracker.size()
				+ " Clients connected !!"); // to print to the server panel how
											// many client are connected
											// according to the tracker
		PrintStream ToClient = null; // to stream an output to the client
		BufferedReader FromClient = null;// to get an input from the client
		try {
			DatagramSocket datagramSocket = new DatagramSocket()  ;
			Thread.sleep(10000);
			//TODO  Sleep
			
		
		    byte[] outBuf = new byte[1000]; 
		    InetAddress hostAddress = InetAddress.getByName("localhost");
		    DatagramPacket out = new DatagramPacket(outBuf, outBuf.length, hostAddress, PORT);

		
			FromClient = new BufferedReader( // to buffer a character input
					// stream
					// //from Client
					new InputStreamReader(sock.getInputStream()));// the buffer
			// read the
			// stream
			// input
			// from that
			// socket
			ToClient = new PrintStream(sock.getOutputStream());// to set up the //TODO To client
			// printstream to
			// listen to sock
			// //to client
			// Note that this branch might not execute when expected:

			if (JokeServer.controlSwitch != true) {
				//ToClient.println("Server is down");//TODO To client
				 String outString = "Server Is down";
				 outBuf = outString.getBytes();
				  out = new DatagramPacket(outBuf, outBuf.length, hostAddress, PORT);
				  datagramSocket.send(out);
			} else
				try {
					// String name;
					// name = FromClient.readLine();
					String name = null;
					if (name == null) {
						name = FromClient.readLine(); // name of joke client
						String ClientKey = FromClient.readLine(); // Read the
																	// UUID from
																	// the
																	// client
						ActivityTracker Tracker = new ActivityTracker(); // instantiate
																			// an
																			// tracker
																			// obj
																			// for
																			// the
																			// client
																			// (empty)
						System.out.println("User: " + name + " is connected. ");
						System.out
								.println("JokeServer received a response from "
										+ name + " .\n");
						if (!JokeServer.UsersTracker.containsKey(ClientKey)) // checking
																				// if
																				// the
																				// client
																				// is
																				// already
																				// in
																				// the
																				// tracker
																				// or
																				// not
						{
							JokeServer.UsersTracker.put(ClientKey, Tracker); // insert
																				// the
																				// UUID
																				// of
																				// a
																				// specific
																				// client
																				// and
																				// its
																				// tracker

						}
						/*-----------------------------------------------------------*/
						if (AdminLooper.mode == "a") { // if the mode is Jokes

							// have an object of randomizer class of enum
							if (JokeServer.UsersTracker.get(ClientKey).JokesGroup
									.isEmpty()) // if its a new client or client
												// that has finished a rotation
							{
								while (JokeServer.UsersTracker.get(ClientKey).JokesGroup // to
																							// make
																							// sure
																							// to
																							// insert
																							// 5
																							// jokes
																							// only
																							// in
																							// the
																							// linked
																							// list
										.size() <= 4) {
									ActivityTracker.Jokes j = ActivityTracker.Jokes
											.getRandom(); // have a random joke
															// to insert in the
															// linked list
									while (!JokeServer.UsersTracker
											.get(ClientKey).JokesGroup
											.contains(j)) // make sure that the
															// random joke is
															// not inserted yet
									{
										JokeServer.UsersTracker.get(ClientKey).JokesGroup// add
																							// the
																							// random
																							// joke
																							// to
																							// the
																							// linked
																							// list
																							// of
																							// jokes
												.add(j);
									}
								}

							}

							Iterator<ActivityTracker.Jokes> it2 = JokeServer.UsersTracker // iterate
																							// through
																							// the
																							// jokes
																							// Linkedlist
									.get(ClientKey).JokesGroup.iterator();
							ActivityTracker.Jokes t = it2.next();// have an
																	// object
																	// from the
																	// first one
																	// in the
																	// linked
																	// list to
																	// output
							
							//TODO TOClient
							//ToClient.println("Hey:" + name + " " + t.toString()); // print
							 String outString = "Hey:" + name + " " + t.toString();
							 outBuf = outString.getBytes();
							  out = new DatagramPacket(outBuf, outBuf.length, hostAddress, PORT);
							  datagramSocket.send(out);													// it
																					// to
																					// the
																					// client
																					// the
																					// joke
							JokeServer.UsersTracker.get(ClientKey).JokesGroup
									.remove(t);// remove form the linked list
												// for that specific client

						}

						/*
						 * ------------------------------------------------------
						 * ------
						 */
						if (AdminLooper.mode == "b") { // if the mode is proverb

							if (JokeServer.UsersTracker.get(ClientKey).ProverbsGroup // Check
																						// whether
																						// the
																						// client
																						// proberbs
																						// object
																						// is
																						// empty
																						// ?
									.isEmpty()) // populate myhash for this
							// client to make tracking
							{
								while (JokeServer.UsersTracker.get(ClientKey).ProverbsGroup // add
																							// a
																							// randomized
																							// verson
																							// of
																							// provebs
																							// (5
																							// proverbs)
										.size() <= 4) {

									ActivityTracker.Proverbs j = ActivityTracker.Proverbs
											.getRandom(); // //have a random
															// proverb to insert
															// in the linked
															// list
									while (!JokeServer.UsersTracker
											.get(ClientKey).ProverbsGroup
											.contains(j)) // add to client
															// object of provebs
															// a random Proverb
									{
										JokeServer.UsersTracker.get(ClientKey).ProverbsGroup
												.add(j); // add here to
															// ProverbsGroup
															// object
									}
								}
							}
							Iterator<ActivityTracker.Proverbs> it2 = JokeServer.UsersTracker
									.get(ClientKey).ProverbsGroup.iterator(); // iterate
																				// through
																				// the
																				// linked
																				// list
																				// of
																				// proverbs
							ActivityTracker.Proverbs t = it2.next();
//							ToClient.println("Hey:" + name + " "
//									+ "The Provereb : " + t.toString()); // print//TODO TO client
																			// it
							 String outString = "Hey:" + name + " "+ "The Provereb : " + t.toString();										// out
							 outBuf = outString.getBytes();
							  out = new DatagramPacket(outBuf, outBuf.length, hostAddress, PORT);
							  datagramSocket.send(out);													// it
															
							JokeServer.UsersTracker.get(ClientKey).ProverbsGroup
									.remove(t);// delete it.
						}

						/*-------------------------------------------------------------------*/
						if (AdminLooper.mode == "c") {// if the mode is
														// maintainance output
														// to the client the
														// following
//							ToClient.println("Sorry "//TODO TO client
//									+ name
//									+ " The server is in maintainance mode Please try again later");
						
							 String outString ="Sorry "//TODO TO client
									+ name + " The server is in maintainance mode Please try again later";			
									 outBuf = outString.getBytes();
									  out = new DatagramPacket(outBuf, outBuf.length, hostAddress,PORT);
									  datagramSocket.send(out);	
						
						}

					}

				} catch (IOException x) {
					System.out.println("Server read error");
					x.printStackTrace();
				}
			sock.close(); // close this connection, but not the server
			;
		} catch (IOException | InterruptedException ioe) {
			System.out.println(ioe);
		}

	}

}

class ActivityTracker// this class to contains Jokes and proverbs enum , also an
						// empty objects of jokes and proverbs that are
						// constructed
{
	public enum Jokes { // enum that has Jokes with tits txts
		ONE {
			public String toString() {
				return " A.Why are relationships complex?   \n  Because you're real, but your girlfriend is imaginary";
			}
		},

		TWO {
			public String toString() {
				return " B.What does a Dallas Cowboys fan do when his team has won the Super Bowl?  \n  He turns off the PlayStation.";
			}
		},
		THREE {
			public String toString() {
				return " C.What did the grape say after the elephant stepped on him?  \n  Nothing, he just let out a little whine.";
			}
		},
		FOUR {
			public String toString() {
				return " D.Why doesn't NASA send cows to space?  \n  Because the steak would be too high.";
			}
		},
		Five {
			public String toString() {
				return " E.You are smiling ? \n Good :-) ";
			}
		};

		static public Jokes getRandom() { // get a random Joke from math
			return values()[(int) (Math.random() * values().length)];
		}

	}

	public enum Proverbs {// enum that has Proverbs with its txts
		ONE {
			public String toString() {
				return " A.When the winds of change blow, some people build walls and others build windmills.";
			}
		},

		TWO {
			public String toString() {
				return " B.Money is a good servant but a bad master.";
			}
		},
		THREE {
			public String toString() {
				return " C.If you want to go fast, go alone. If you want to go far, go together.";
			}
		},
		FOUR {
			public String toString() {
				return " D.The tongue has no bones, yet it crushes bones.";
			}
		},
		Five {
			public String toString() {
				return " E.Don't speak unless you can improve the silence.";
			}
		};
		static public Proverbs getRandom() {// get a random proverb using math
			return values()[(int) (Math.random() * values().length)];
		}
	}

	public LinkedList<Jokes> JokesGroup;
	public LinkedList<Proverbs> ProverbsGroup;

	public ActivityTracker() {
		JokesGroup = new LinkedList<Jokes>(); // a new linked list of the Jokes
												// enum
		ProverbsGroup = new LinkedList<Proverbs>(); // a new linked list of the
													// proverbs enum
	}

}

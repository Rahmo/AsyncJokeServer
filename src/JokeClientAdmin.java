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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class JokeClientAdmin {
	public static void main(String args[]) {
		String serverName;
		if (args.length < 1)
			serverName = "localhost";
		else
			serverName = args[0];
		System.out.println("Abdulrahman Joke Client Admin, 1.8.\n");
		System.out.println("Using server: " + serverName + ", Port: 45005"); // printing
																				// out
																				// the
																				// server
																				// name
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {// BufferReader to read the input.
			String AdminIn;
			do {
				System.out
						.print("Enter the mode that you want to switch: \n The valid inputs are  : \n a) Joke mode \n b) Proverb mode \n c) Maintance mode \n");
				System.out.flush();//
				AdminIn = in.readLine();// read what the client admin entered
				if (AdminIn.indexOf("quit") < 0)// check if the entered is quit
												// or not
					getRemoteAddress(AdminIn, serverName); // sending the mode
															// that has been
															// entered to server
			} while (AdminIn.indexOf("quit") < 0); // if the recieved input is
													// the string "quit" then it
													// will finish the process
													// of the client
			System.out.println("Cancelled by user request.");
		} catch (IOException x) {
			x.printStackTrace();
		}
	}

	static void getRemoteAddress(String AdminIn, String serverName) {
		Socket sock;
		BufferedReader fromServer;
		PrintStream toServer;
		String textFromServer;
		// System.out.println ("Cancelled by user request.");

		try {
			sock = new Socket(serverName, 45005); /*
												 * Open connection to server
												 * port, using 1577
												 */

			// Create filter I/O streams for the socket:
			fromServer = new BufferedReader(new InputStreamReader(
					sock.getInputStream())); // buffer and read data from that
												// specific socket
			toServer = new PrintStream(sock.getOutputStream());// set up the way
																// to the server
																// to send data
			// Send machine name or IP address to server:
			toServer.println(AdminIn);
			toServer.flush();// send data to the server
			// Read two or three lines of response from the server,
			// and block while synchronously waiting:
			for (int i = 1; i <= 3; i++) { // reading three lines from server
				textFromServer = fromServer.readLine();// reading
				if (textFromServer != null)
					System.out.println(textFromServer);
			} // as long as its not null output it
			sock.close();
		} catch (IOException x) {
			System.out.println("Socket error.");
			x.printStackTrace();
		}
	}

}
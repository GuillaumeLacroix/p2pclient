package com.guillaumelacroix.p2pclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the port to listen to: ");
		String port = br.readLine();
		System.out.println("Enter a username: ");
		String username = br.readLine();
		
		User user = new User("127.0.0.1", port, username);
		
		// Start a receiving thread
		ReceivingThread receiving_thread = new ReceivingThread(Integer.valueOf(port));
		receiving_thread.start();
		
		// Connect to the server
		String address = "http://127.0.0.1:8080/connect?" + user.getUserRepresentation();
		URL url = new URL(address);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
		{
			System.out.println("Could not connect to the server");
			return;
		}
		
		// Now start the client thread
		ClientThread client = null;
		while (true)
		{
			// This loop ensures that whenever a client quits their current conversation, a new one opens up
			if (client == null || !client.isAlive())
			{
				if (client != null)
				{
					System.out.println("Would you like to chat with another person? [y/n]");
					String answer = null;
					while ((answer = br.readLine()) != null)
					{
						if ("y".contentEquals(answer))
						{
							break;
						}
						else if ("n".contentEquals(answer))
						{
							receiving_thread.interrupt();
							client.interrupt();
							return;
						}
						else
						{
							System.out.println("Would you like to chat with another person? [y/n]");							
						}
					}
				}
				
				client = new ClientThread(br, username);
				if (client.connect())
				{
					System.out.println("Now chatting with " + client.getTargetUsername() + ". Enter !e to quit");
					client.start();
				}
				else
				{
					System.err.println("Could not connect client");
				}
			}
		}
	}
}

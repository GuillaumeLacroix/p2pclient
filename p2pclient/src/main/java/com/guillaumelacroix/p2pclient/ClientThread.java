package com.guillaumelacroix.p2pclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a class for a Client Thread. It is in charge of handling user inputs
 * for chatting, as well as connecting to the remote target to write into
 * 
 * @author Guillaume Lacroix
 *
 */
public class ClientThread extends Thread {
	private BufferedReader _br;
	private Socket _socket;
	private String _target_username;
	private String _own_username;

	public ClientThread(BufferedReader br, String username) {
		_br = br;
		_own_username = username;
	}

	/**
	 * This method connects to the remote address entered by the user
	 * 
	 * @return true if the connection is successful, false otherwise
	 * @throws IOException
	 */
	public boolean connect() throws IOException {
		Map<String, User> user_list = getUserList();
		
		System.out.println("Enter the target's username");
		try {
			_br = new BufferedReader(new InputStreamReader(System.in));
			String input = _br.readLine();
			while (!user_list.containsKey(input)) {
				user_list = getUserList();
				input = _br.readLine();
			}
			
			User target_user = user_list.get(input);

			_target_username = target_user.getUsername();
			String target_address = target_user.getAddress();
			int target_port = target_user.getPort();
			_socket = new Socket(target_address, target_port);
			return _socket.isConnected();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	private Map<String, User> getUserList() throws IOException
	{
		Map<String, User> users = new HashMap<String, User>();
		
		// Get the list of users
		URL url = new URL("http://127.0.0.1:8080/users");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			User user = User.fromString(inputLine);
			if (user != null)
			{
				users.put(user.getUsername(), user);
			}
		}
		in.close();
		
		System.out.println("Available users:");
		for (String username : users.keySet())
		{
			System.out.println(username);
		}

		return users;
	}

	/**
	 * This method returns the username that this client is connected to
	 * 
	 * @return the remote username that this client is connected to, or "Unknown
	 *         connection" if it is not connected
	 */
	public String getTargetUsername() {
		if (_socket != null && _socket.isConnected()) {
			return _target_username;
		}

		return "Unknown connection";
	}

	/**
	 * This method closes the open resources
	 */
	private void close() {
		try {
			if (_socket != null && !_socket.isClosed()) {
				_socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			// Initialise the PrintWriter to write into
			PrintWriter pw = new PrintWriter(_socket.getOutputStream(), true);
			while (true) {
				String line;
				if ((line = _br.readLine()) != null && !line.isEmpty()) {
					if ("!e".contentEquals(line)) {
						System.out.println("Connection closed with " + _target_username);
						close();
						break;
					} else {
						// Write the line
						pw.println("[" + _own_username + "] " + line);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			close();
		}
	}

	@Override
	public void interrupt() {
		close();

		super.interrupt();
	}
}

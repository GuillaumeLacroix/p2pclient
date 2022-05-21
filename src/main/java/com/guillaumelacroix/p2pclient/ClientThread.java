package com.guillaumelacroix.p2pclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This is a class for a Client Thread.
 * It is in charge of handling user inputs for chatting,
 * as well as connecting to the remote target to write into
 * 
 * @author Guillaume Lacroix
 *
 */
public class ClientThread extends Thread {
	private BufferedReader _br;
	private Socket _socket;
	
	public ClientThread(BufferedReader br)
	{
		_br = br;
	}
	
	/**
	 * This method connects to the remote address entered by the user
	 * @return true if the connection is successful, false otherwise
	 */
	public boolean connect()
	{
		System.out.println("Enter the target's [address:port]");
		try {
			_br = new BufferedReader(new InputStreamReader(System.in));
			String input = _br.readLine();
			String[] inputs = input.split(":");
			while (inputs.length != 2)
			{
				System.out.println("Usage: [address:port]");
				input = _br.readLine();
				inputs = input.split(":");
			}
			
			String target_address = inputs[0];
			int target_port = Integer.valueOf(inputs[1]);
			_socket = new Socket(target_address, target_port);
			return _socket.isConnected();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * This method returns the remote address that this client is connected to
	 * @return the remote address that this client is connected to, or "Unknown connection" if it is not connected
	 */
	public String getConnectedAddress()
	{
		if (_socket != null && _socket.isConnected())
		{
			return _socket.getInetAddress() + ":" + _socket.getPort();
		}
		
		return "Unknown connection";
	}

	/**
	 * This method closes the open resources
	 */
	private void close()
	{
		try {
			if (_socket != null && !_socket.isClosed())
			{
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
			while (true)
			{
				String line;
				if ((line = _br.readLine()) != null && !line.isEmpty())
				{
					if ("!e".contentEquals(line))
					{
						System.out.println("Connection closed with " + getConnectedAddress());
						close();
						break;
					}
					else
					{
						// Write the line
						pw.println(line);
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

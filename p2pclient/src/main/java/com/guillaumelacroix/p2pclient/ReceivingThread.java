package com.guillaumelacroix.p2pclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * This is a class in charge of accepting connections
 * in order to receive chat messages and display them to the user
 * 
 * @author Guillaume Lacroix
 *
 */
public class ReceivingThread extends Thread
{
	private ServerSocket _server_socket;
	
	public ReceivingThread(int port) throws IOException
	{
		_server_socket = new ServerSocket(port);
	}
	
	@Override
	public void run()
	{
		try {
			while (true) {
				Socket socket = _server_socket.accept();

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String s;
				while (!socket.isInputShutdown() && (s = bufferedReader.readLine()) != null)
				{
					System.out.println(s);					
				}
			}
		} catch (SocketException e) {
			// Fail silently
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void interrupt() {
		if (_server_socket != null)
		{
			try {
				_server_socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.interrupt();
	}
}

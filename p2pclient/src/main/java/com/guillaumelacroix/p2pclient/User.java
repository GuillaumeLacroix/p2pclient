package com.guillaumelacroix.p2pclient;

public class User {
	private String _address;
	private int _port;
	private String _username;
	
	public static User fromString(String user_representation)
	{
		String[] strings = user_representation.split("&");
		if (strings.length != 3)
		{
			System.out.println("Could not match 3 arguments");
		}
		else
		{
			return new User(strings[0].split("=")[1], strings[1].split("=")[1], strings[2].split("=")[1]);
		}
		
		return null;
	}
	
	public User(String address, String port, String username)
	{
		_address = address;
		_port = Integer.valueOf(port);
		_username = username;
	}

	public String getUsername() {
		return _username;
	}

	public int getPort() {
		return _port;
	}

	public String getAddress() {
		return _address;
	}
	
	public String getUserRepresentation() {
		return "address=" + _address + "&port=" + _port + "&username=" + _username;
	}
}

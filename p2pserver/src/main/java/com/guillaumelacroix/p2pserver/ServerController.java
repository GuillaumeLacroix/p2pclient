package com.guillaumelacroix.p2pserver;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {
	private Map<String, User> _username_to_users;

	public ServerController() {
		_username_to_users = new HashMap<String, User>();
	}

	@RequestMapping(value = "/connect", method = RequestMethod.POST)
	public String connect(@RequestParam("address") String address, @RequestParam("port") String port,
			@RequestParam("username") String username, HttpServletRequest request) {
		User new_user = new User(address, port, username);
		if (new_user != null && !_username_to_users.containsKey(new_user.getUsername())) {
			_username_to_users.put(new_user.getUsername(), new_user);
			return "OK";
		} else {
			System.err.println("Could not add user " + username);
			return "NOK";
		}
	}

	@RequestMapping(value = "/users")
	public String getConnectedUsers(HttpServletRequest request) {
		String message = "";
		for (User user : _username_to_users.values()) {
			if (!message.isEmpty()) {
				message += "\n";
			}
			message += user.getUserRepresentation();
		}
		return message;
	}

	@RequestMapping(value = "*", method = RequestMethod.GET)
	@ResponseBody
	public String getFallback() {
		return "Wrong mapping asked";
	}

}
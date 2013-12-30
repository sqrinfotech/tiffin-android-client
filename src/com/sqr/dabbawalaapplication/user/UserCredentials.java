package com.sqr.dabbawalaapplication.user;

public class UserCredentials {
	
	private String user_id;
	private String name;
	private String username;
	private String email;
	private String address;
	private String location;
	
	public UserCredentials(String user_id, /*String name,*/ String username/*, String email,
			String address, String location*/) {
		super();
		this.user_id = user_id;
		//this.name = name;
		this.username = username;
		//this.email = email;
		/*this.address = address;
		this.location = location;*/
	}
	
	
	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}

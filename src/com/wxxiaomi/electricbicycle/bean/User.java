package com.wxxiaomi.electricbicycle.bean;


public class User {

	public int id;
	public String username;
	public String password;
	public UserCommonInfo userCommonInfo;
	
	public static class UserCommonInfo{
		public int userid;
		public String name;
		public String head;
		public String emname;
	}
}

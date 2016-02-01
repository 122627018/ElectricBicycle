package com.wxxiaomi.electricbicycle.bean.format;

import java.util.List;

import com.wxxiaomi.electricbicycle.bean.User;

public class NearByPerson {
	public List<UserLocatInfo> userLocatList;
	public static class UserLocatInfo{
		public User user;
		public double[] locat;
	}
}

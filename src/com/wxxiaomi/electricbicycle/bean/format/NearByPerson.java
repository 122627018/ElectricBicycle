package com.wxxiaomi.electricbicycle.bean.format;

import java.util.List;

import com.wxxiaomi.electricbicycle.bean.User.UserCommonInfo;

public class NearByPerson {
	public List<UserLocatInfo> userLocatList;
	public static class UserLocatInfo{
		public UserCommonInfo userCommonInfo;
		public double[] locat;
	}
}

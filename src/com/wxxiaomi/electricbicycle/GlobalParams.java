package com.wxxiaomi.electricbicycle;

import java.util.List;

import com.wxxiaomi.electricbicycle.bean.User;
import com.wxxiaomi.electricbicycle.bean.User.UserCommonInfo;

/**
 * 全局化的对象
 * @author Administrator
 *
 */
public class GlobalParams {
	public static User user = null;
	
	/**
	 * 记录此时登陆的账号是否是第一次登陆
	 */
//	public static boolean isFirst = true;
	
	public static List<UserCommonInfo> friendList = null;
}

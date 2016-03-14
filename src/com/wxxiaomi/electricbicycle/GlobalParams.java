package com.wxxiaomi.electricbicycle;

import java.util.List;

import com.baidu.mapapi.search.core.PoiInfo;
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
	
	/**
	 * 初始化是否成功
	 */
	public static boolean isInitSuccess = true;
	
	/**
	 * 测试用,當前位置
	 */
	public static double latitude ;
	public static double longitude ;
	
	/**
	 * 获取路线的结果,终点
	 */
	public static PoiInfo poiInf;
}

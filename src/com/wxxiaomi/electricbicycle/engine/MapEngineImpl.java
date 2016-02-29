package com.wxxiaomi.electricbicycle.engine;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wxxiaomi.electricbicycle.ConstantValue;
import com.wxxiaomi.electricbicycle.GlobalParams;
import com.wxxiaomi.electricbicycle.bean.format.NearByPerson;
import com.wxxiaomi.electricbicycle.bean.format.common.ReceiceData;
import com.wxxiaomi.electricbicycle.util.HttpClientUtil;

public class MapEngineImpl {

	/**
	 * 获取附近的人
	 * @param latitude
	 * @param longitude
	 */
	public ReceiceData<NearByPerson> getNearByFromServer(double latitude, double longitude){
		String url = ConstantValue.SERVER_URL+"ActionServlet?action=getnearby&userid="+GlobalParams.user.id
				+"&latitude="+latitude
				+"&longitude="+longitude;
		String json = HttpClientUtil.doGet(url);
		try {
			Gson gson = new Gson();
			ReceiceData<NearByPerson> result = gson.fromJson(json, new TypeToken<ReceiceData<NearByPerson>>(){}.getType());
			return result;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

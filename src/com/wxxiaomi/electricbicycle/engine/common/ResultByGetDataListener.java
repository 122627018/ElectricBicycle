package com.wxxiaomi.electricbicycle.engine.common;

import com.wxxiaomi.electricbicycle.bean.format.common.ReceiceData;

public interface ResultByGetDataListener<T> {
	void success(ReceiceData<T> result);
	void error(String error);
}

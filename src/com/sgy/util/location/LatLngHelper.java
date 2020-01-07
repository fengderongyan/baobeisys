package com.sgy.util.location;

public class LatLngHelper {
 
	/**
	 * 将百度经纬度转换成高德地图经纬度
	 * @param point
	 * @param type
	 * @return
	 */
	public static double parseBToAPoint(double point,String type){
		double pointLatLng = 0;
		if("lat".equals(type)){
			pointLatLng = (0.9999952854 * (point) + 0.0000032485 * (point) - 0.0063143296); 
		}else{
			pointLatLng = (0.0000020147 * (point) + 0.9999993874 * (point) - 0.0064751997);
		}
//		double alat = (0.9999952854 * (point/1E6) + 0.0000032485 * (point/1E6) - 0.0063143296); 
//		double alng = (0.0000020147 * (point/1E6) + 0.9999993874 * (point/1E6) - 0.0064751997);
		return pointLatLng;
	}
	
}
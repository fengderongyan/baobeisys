package com.sgy.util.location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.sgy.util.location.amaplocation.AddressComponent;
import com.sgy.util.location.amaplocation.AmapLocationJsonbean;
import com.sgy.util.location.amaplocation.Regeocode;
import com.sgy.util.location.amaplocation.StreetNumber;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class LocationHelper {
	/**
	 * 根据百度地图经纬度获取具体中文位置 
	 * @param latitude
	 *            纬度
	 * @param longitude
	 *            经度
	 * @return 具体到街道门牌号码
	 */
	public static String geoPoiAddrBMap(String latitude, String longitude) {
		String url ="http://api.map.baidu.com/geocoder?output=xml&location="+latitude+",%20"+longitude+"&key=37492c0ee6f924cb5e934fa08c6b1676";
		URL myURL = null;
		URLConnection httpsConn = null;
		try {
			myURL = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		try {
			String json = "";
			String addr = "";
			httpsConn = (URLConnection) myURL.openConnection();
			if (httpsConn != null) {
				InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
				BufferedReader br = new BufferedReader(insr);
				String data = null;
				while ((data = br.readLine()) != null) {
					json += data;
				}
				insr.close();
			}
			if(!json.equals("")) {
				addr = json.substring(json.indexOf("formatted_address")+18,json.indexOf("</formatted_address>"));
				return addr ;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	/**
     * 转换坐标系
     * @author yuanfy 2014-10-16
     * @param x 维度
     * @param y 经度
     * @return
     */
    public Map<String,String> converterLngLat(String x, String y) {
        Map<String,String> map = new HashMap<String,String>();
        try {
            URL url = new URL("http://api.map.baidu.com/ag/coord/convert?from=0&to=2&x=" + x + "&y=" + y);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
            out.flush();
            out.close();

            String sCurrentLine = ""; // 当前行
            String sTotalString = ""; // 总字符
            InputStream urlStream = connection.getInputStream();
            BufferedReader l_reader = new BufferedReader(new InputStreamReader(urlStream));
            while((sCurrentLine = l_reader.readLine()) != null) {
                if(!sCurrentLine.equals(""))
                    sTotalString += sCurrentLine;
            }
            sTotalString = sTotalString.substring(1, sTotalString.length() - 1);
            String[] results = sTotalString.split("\\,");
            if(results.length == 3) {
                if(results[0].split("\\:")[1].equals("0")) {
                    String mapX = results[1].split("\\:")[1];
                    String mapY = results[2].split("\\:")[1];
                    mapX = mapX.substring(1, mapX.length() - 1);
                    mapY = mapY.substring(1, mapY.length() - 1);
                    mapX = new String(Base64.decode(mapX));
                    mapY = new String(Base64.decode(mapY));
                    map.put("lng", mapX);
                    map.put("lat", mapY);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 根据高德地图获取经纬度周边建筑物
     * @author yuanfy 2014-11-04
     * @param latitude
     * @param longitude
     * @return
     */
    public String geoPoiAddrAMap(String latitude, String longitude) {
        if(null == latitude || "".equals(latitude)){
            return null;
        }
        String poiAddr = "";
        String url = "http://restapi.amap.com/v3/geocode/regeo?location="+longitude+","+latitude+"&key=e15111eda84b994b479cce474f1996ca&s=rsv3&radius=100&extensions=all&callback=jsonp";
        URL myURL = null;
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        try {
            String jsonData = "";
            httpsConn = (URLConnection) myURL.openConnection();
            if(httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;
                while((data = br.readLine()) != null) {
                    jsonData += data;
                }
                insr.close();
            }
            try {
                // 获取地址解析对象
                AmapLocationJsonbean amap = poiAddrAnalyze(jsonData);
                if(amap == null){
                    return null;
                }
                Regeocode regeocode = amap.getRegeocode();
                if(regeocode == null || "".equals(regeocode.getFormatted_address())){
                    return null;
                }
                // 地址组件
                AddressComponent addrComponent = amap.getRegeocode().getAddressComponent();
                if(addrComponent == null){
                    return null;
                }
                // 省
                String province = addrComponent.getProvince();
                // 市
                String city = addrComponent.getCity();
                // 区
                String district = addrComponent.getDistrict();
                // 乡镇
                String township = addrComponent.getTownship();
                // 街道
                StreetNumber street = addrComponent.getStreetNumber();
                
                if(street == null || "".equals(street.getStreet())) {
                    String formattedAddress = amap.getRegeocode().getFormatted_address();
                    poiAddr = province + city + district + township;
                    if(poiAddr.length()<formattedAddress.length()){
                        return formattedAddress;
                    }
                    return province + city + district+township;
                }
                String streetName = street.getStreet();
                String streetNum = street.getNumber();
                
                String address = streetName+streetNum;
                String direction = street.getDirection();
                String distance = street.getDistance();
                String dis = "";
                if(!"".equals(distance)){
                    dis = Math.round(Double.parseDouble(distance)) + "米附近";
                }
                if("东".equals(direction)) {
                    direction = "向西";
                } else if("南".equals(direction)) {
                    direction = "向北";
                } else if("西".equals(direction)) {
                    direction = "向东";
                } else if("北".equals(direction)) {
                    direction = "向南";
                } else if("东南".equals(direction)) {
                    direction = "西北";
                } else if("西南".equals(direction)) {
                    direction = "东北";
                } else if("东北".equals(direction)) {
                    direction = "西南";
                } else if("西北".equals(direction)) {
                    direction = "东南";
                }
                return province + city + district + address + direction + dis;
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 地址解析
     * @author YuanFy 2014-11-04
     * @param jsonData
     * @return
     */
    public AmapLocationJsonbean poiAddrAnalyze(String jsonData){
        if(null == jsonData || "".equals(jsonData)){
            return null;
        }
        String jsonResult = jsonData.substring(6);
        jsonResult = jsonResult.substring(0,jsonResult.length()-1);
        jsonResult = jsonResult.replaceAll("\\[\\]", "\"\"");
        AmapLocationJsonbean aMapLocation = new Gson().fromJson(jsonResult, AmapLocationJsonbean.class);
        return aMapLocation;
    }
    
	public static void main(String[] args) {
//		// 116.367429,39.919355
//		String addr = geocodeAddr("38.9146943", "121.612382");// (38.9146943,121.612382);
	}
}

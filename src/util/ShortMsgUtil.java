package util;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ShortMsgUtil {
    public static void main(String[] args) {
        //短信下发
    	String CODE = sendMsg("111111", "13585283047");
    	System.out.println("code " + CODE );
        //查询余额
      /*  String balanceUrl = "https://xxx/msg/balance/json";
        Map map1 = new HashMap();
        map1.put("account","N*******");
        map1.put("password","************");
        JSONObject js1 = (JSONObject) JSONObject.toJSON(map1);
        System.out.println(sendSmsByPost(balanceUrl,js1.toString()));*/
    }
    
    /**
     * 描述：<br>
     * @param msg 消息
     * @param phones 多个使用英文逗号分隔
     * @return
     * @author zhangcc
     * @Date : 2019-07-02
     */
    public static String sendMsg(String msg, String phones){
    	
    	//短信下发
        String sendUrl = "http://smssh1.253.com/msg/send/json";
        Map map = new HashMap();
        map.put("account","N5334251");//API账号
        map.put("password","YJ2nats4xdff58");//API密码
        map.put("msg", msg);//短信内容
        map.put("phone", phones);//手机号
        map.put("report","false");//是否需要状态报告
        map.put("extend","123");//自定义扩展码
        JSONObject js = (JSONObject) JSONObject.toJSON(map);
        String res = sendSmsByPost(sendUrl,js.toString());
        Map info = (Map)JSONUtils.parse(res);
        String code = (String) info.get("code");
        System.out.println(res);
        return code;
    }
    
    
    public static String sendSmsByPost(String path, String postContent) {
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();
            OutputStream os=httpURLConnection.getOutputStream();
            os.write(postContent.getBytes("UTF-8"));
            os.flush();
            StringBuilder sb = new StringBuilder();
            int httpRspCode = httpURLConnection.getResponseCode();
            if (httpRspCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
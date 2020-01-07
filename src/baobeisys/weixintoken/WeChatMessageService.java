package baobeisys.weixintoken;

import java.security.MessageDigest;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.github.wxpay.sdk.WXPayUtil;

@Service
public class WeChatMessageService {

    public String checkSignature(WeChatMessage message) {
        String signature = message.getSignature();
        String timestamp = message.getTimestamp();
        String nonce = message.getNonce();
        String token = "XXXXXXX"; //必须与请求参数中的token一致
        
        //将token、timestamp、nonce三个参数进行字典排序
        String[] arr = new String[] {token, timestamp, nonce};
        Arrays.sort(arr);

        StringBuilder content = new StringBuilder();  
        for (int i = 0; i < arr.length; i++) {  
            content.append(arr[i]);  
        }

        MessageDigest md = null;  
        String tmpStr = null;  

        try {  
            md = MessageDigest.getInstance("SHA-1");  
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest); 
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) ? message.getEchostr() : "" : "";
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param byteArray
     * @return
     */
    private String byteToStr(byte[] byteArray) {
        String strDigest = "";  
        for (int i = 0; i < byteArray.length; i++) {  
            strDigest += byteToHexStr(byteArray[i]);  
        }  
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     * @param mByte
     * @return
     */
    private String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };  
        char[] tempArr = new char[2];  
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];  
        tempArr[1] = Digit[mByte & 0X0F];  

        String s = new String(tempArr);  
        return s;  
    }
}
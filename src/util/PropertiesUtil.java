package util;

import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * 描述：PropertiesUtil文件工具类<br>
 * @author zhangyongbin
 * @Date : 2019年1月18日
 */
public class PropertiesUtil {
    private static Properties props=new Properties();
    
    /**
     * 移动端APP版本号
     * @param name
     * @return
     */
    public static String getAppVersionPropertiesValue(String name) {
    	String value = "";
    	
    	String fileName="/appversion.properties";
        InputStream in = PropertiesUtil.class.getResourceAsStream(fileName);
		if(in!=null){
			try{
				props.load(in);
				value = props.getProperty(name);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(in!=null){
					try{
						in.close();
					}catch(Exception e){}
				}
			}
		}
		return value;
	}
    
    public static String getPropertiesValue(String name) {
    	String value = "";
    	
    	String fileName="/config.properties";
        InputStream in = PropertiesUtil.class.getResourceAsStream(fileName);
		if(in!=null){
			try{
				props.load(in);
				value = props.getProperty(name);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(in!=null){
					try{
						in.close();
					}catch(Exception e){}
				}
			}
		}
		return value;
	}
    
    public static String getPropertiesValueGlobal(String name) {
    	String value = "";
    	InputStream in = PropertiesUtil.class.getResourceAsStream("/config/global.properties");
    	if(in!=null){
    		try{
    			props.load(in);
    			value = props.getProperty(name);
    		}catch(Exception e){
    			e.printStackTrace();
    		}finally{
    			if(in!=null){
    				try{
    					in.close();
    				}catch(Exception e){}
    			}
    		}
    	}
    	return value;
    }
    
    public static String getPropertiesValueSendSms(String name) {
    	String value = "";
    	InputStream in = PropertiesUtil.class.getResourceAsStream("/config/SendSms.properties");
    	if(in!=null){
    		try{
    			props.load(in);
    			value = props.getProperty(name);
    		}catch(Exception e){
    			e.printStackTrace();
    		}finally{
    			if(in!=null){
    				try{
    					in.close();
    				}catch(Exception e){}
    			}
    		}
    	}
    	return value;
    }
    
    
    public static String getUnPropertiesValue(String name) {
    	String value = "";
        InputStream in = PropertiesUtil.class.getResourceAsStream("/config/param.properties");
		if(in!=null){
			try{
				props.load(in);
				value = props.getProperty(name);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(in!=null){
					try{
						in.close();
					}catch(Exception e){}
				}
			}
		}
		return value;
	}

    public Properties getProps(){
        return props;
    }
    
}

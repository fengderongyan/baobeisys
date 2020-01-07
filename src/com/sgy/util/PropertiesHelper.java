package com.sgy.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.sgy.util.common.StringHelper;

public class PropertiesHelper {
    private StringHelper str = new StringHelper();
    
    /**
     * 根据属性名获取配置文件中的属性值
     * @param propertiesName 属性名称
     * @return 属性值
     */
    public String getPropertiesValue(String propertiesName) {
        Properties properties = new Properties();
        
        InputStream inputStream = null;
        
        try {
            inputStream = getClass().getResourceAsStream("/smsbackprocess.properties");
            properties.load(inputStream);
            return str.notEmpty(properties.get(propertiesName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

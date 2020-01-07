package com.sgy.util.location.amaplocation;


/** 
 * 位置组件
 * @author yuanfy
 * @date 2014-11-04 
 */
public class AmapLocationJsonbean {
    
    // 状态
    private String status = "";
    
    // 信息
    private String info = "";
    
    // 位置编码
    private Regeocode regeocode = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Regeocode getRegeocode() {
        return regeocode;
    }

    public void setRegeocode(Regeocode regeocode) {
        this.regeocode = regeocode;
    }
    
}

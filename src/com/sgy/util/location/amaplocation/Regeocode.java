package com.sgy.util.location.amaplocation;


/**
 * 位置编码组件
 * @author yuanfy
 * @date 2014-11-04
 */
public class Regeocode {
    
    // 格式化的地址
    private String formatted_address = "";
    
    // 地址组件
    private AddressComponent addressComponent = null;
    
    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public AddressComponent getAddressComponent() {
        return addressComponent;
    }

    public void setAddressComponent(AddressComponent addressComponent) {
        this.addressComponent = addressComponent;
    }
}

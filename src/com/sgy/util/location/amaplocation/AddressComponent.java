package com.sgy.util.location.amaplocation;


/**
 * 地址相关组件
 * @author yuanfy
 * @date 2014-11-04
 */
public class AddressComponent {
    
    // 省
    private String province = "";
    
    // 市
    private String city = "";

    // 城市编码
    private String citycode = "";
    
    // 区
    private String district = "";
    
    // 区号
    private String adcode = "";
    
    // 乡镇
    private String township = "";
    
    // 建筑物
    private Building building  = null;
    
    // 街道
    private StreetNumber streetNumber = null;
    
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }
    
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getTownship() {
        return township;
    }

    public void setTownship(String township) {
        this.township = township;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public StreetNumber getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(StreetNumber streetNumber) {
        this.streetNumber = streetNumber;
    }

}

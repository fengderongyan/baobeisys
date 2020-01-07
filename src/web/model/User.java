package web.model;

import java.util.List;
import java.util.Map;

import com.sgy.util.common.StringHelper;


/**
 * 用户模型
 * @author chang 
 * @createDate Mar 8, 2013
 * @description
 */
public class User {
	
	//系统工号
	private String operatorId = "";
	
	//密码
	private String password = "";

	//姓名
	private String name = "";

	//用户在系统中的归属组织编号
	private String orgId = "";

	//组织名称
	private String orgName = "";
	
	//手机号码
	private String mobile = "";
	
	// 用户类：1：市场 2：集团
	private String user_type = "";
    
    private String userType_id = "";
	
    //最大角色层级
	private String maxRoleLevel = "";
    
	//用户在系统中的归属组织层级
    private String orgLevel = "";
    
    //县公司编号
    private String countyId = "";
    
    //区域编号
    private String areaId = "";
    
    //网格编号
    private String gridId = "";
    
    //微区域
    private String tinyId = "";
    
    // 角色
    private String roleIds = "";

    // 角色名称
    private String roleNames = "";
    
    // 角色等级
    private int roleLevel; 
    //头像名称
    private String qm_name; 
    
    //头像url
    private String qm_url; 
    
    //token
    private String token; 
    
    //紧急联系人名称
    private String jjLxrName; 
    
    //紧急联系人号码
    private String jjLxrPhone; 

    
	

	public int getRoleLevel() {
		return roleLevel;
	}

	public String getJjLxrName() {
		return jjLxrName;
	}

	public void setJjLxrName(String jjLxrName) {
		this.jjLxrName = jjLxrName;
	}

	public String getJjLxrPhone() {
		return jjLxrPhone;
	}

	public void setJjLxrPhone(String jjLxrPhone) {
		this.jjLxrPhone = jjLxrPhone;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getQm_name() {
		return qm_name;
	}

	public void setQm_name(String qm_name) {
		this.qm_name = qm_name;
	}

	public String getQm_url() {
		return qm_url;
	}

	public void setQm_url(String qm_url) {
		this.qm_url = qm_url;
	}

	/**
	 * 用户角色列表
	 */
	private List<Map<String, Object>> userRoleList;

	public String getOperatorId() {
		return operatorId;
	}

	public List<Map<String, Object>> getUserRoleList() {
		return userRoleList;
	}

	public void setUserRoleList(List<Map<String, Object>> userRoleList) {
		this.userRoleList = userRoleList;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
    public void setMaxRoleLevel(String maxRoleLevel) {
        this.maxRoleLevel = maxRoleLevel;
    }
    
	/**
	 * 获取用户所有角色名称
	 * @return
	 */
	public String getRoleNames(){
		if (null == userRoleList || userRoleList.size()==0) {
			return "";
		}
		String roleNames = "";
		for (Map<String, Object> roleMap : userRoleList) {
			roleNames += "," + StringHelper.get(roleMap, "ROLE_NAME");
		}
		if (!roleNames.equals("")) {
			roleNames = roleNames.substring(1);
		}
		return roleNames;
	}
	
	/**
	 * 获取用户所有的角色编号
	 * @return
	 */
	public String getRoleIds(){
		if (null == userRoleList || userRoleList.size()==0) {
			return "";
		}
		String roleIds = "";
		for (Map<String, Object> roleMap : userRoleList) {
			roleIds += "," + StringHelper.get(roleMap, "ROLE_ID");
		}
		if (!roleIds.equals("")) {
			roleIds = roleIds.substring(1);
		}
		return roleIds;
	}
	
	/**
	 * 判定用户是否有传入角色的一个，多个使用","分割
	 * @return
	 */
	public boolean hasRoles(String roleIds){
		if (null == userRoleList || userRoleList.size()==0) {
			return false;
		}
		String[] arrRoles = roleIds.split(",");
		for (Map<String, Object> roleMap : userRoleList) {
			for (int i = 0; i < arrRoles.length; i++) {
				if (StringHelper.get(roleMap, "ROLE_ID").equals(arrRoles[i])) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
     * 获取用户所有的角色等级
     * @return
     */
    public String getRoleLevels(){
        if (null == userRoleList || userRoleList.size()==0) {
            return "";
        }
        String roleLevels = "";
        for (Map<String, Object> roleMap : userRoleList) {
            roleLevels += "," + StringHelper.get(roleMap, "ROLE_LEVEL");
        }
        if (!roleLevels.equals("")) {
            roleLevels = roleLevels.substring(1);
        }
        return roleLevels;
    }
    
    /**
     * 判定用户是否有传入角色等级的一个，多个使用","分割
     * @return
     */
    public boolean hasRoleLevels(String roleLevelIds){
        if (null == userRoleList || userRoleList.size()==0) {
            return false;
        }
        String[] arrRoleLevels = roleLevelIds.split(",");
        for (Map<String, Object> roleMap : userRoleList) {
            for (int i = 0; i < arrRoleLevels.length; i++) {
                if (StringHelper.get(roleMap, "ROLE_LEVEL").equals(arrRoleLevels[i])) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 获取用户最大的角色等级（值越小权限越大）
     * @return
     */
    public String getMaxRoleLevel(){
        if (null == userRoleList || userRoleList.size()==0) {
            return "1000";
        }
        
        String roleLevel = "";
        int maxRoleLevelTemp = 1000;
        for (Map<String, Object> roleMap : userRoleList) {
            roleLevel = StringHelper.get(roleMap, "ROLE_LEVEL");
            if(!"".equals(roleLevel)) {
                if (Integer.parseInt(roleLevel) < maxRoleLevelTemp) {
                    maxRoleLevelTemp = Integer.parseInt(roleLevel);
                }
            }
        }
        maxRoleLevel = Integer.toString(maxRoleLevelTemp);
        return maxRoleLevel;
    }

	public String getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

    /**
     * @return the countyId
     */
    public String getCountyId() {
        return countyId;
    }

    /**
     * @param countyId the countyId to set
     */
    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    /**
     * @return the areaId
     */
    public String getAreaId() {
        return areaId;
    }

    /**
     * @param areaId the areaId to set
     */
    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    /**
     * @return the gridId
     */
    public String getGridId() {
        return gridId;
    }

    /**
     * @param gridId the gridId to set
     */
    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    /**
     * @return the tinyId
     */
    public String getTinyId() {
        return tinyId;
    }

    /**
     * @param tinyId the tinyId to set
     */
    public void setTinyId(String tinyId) {
        this.tinyId = tinyId;
    }

    /**
     * @param roleIds the roleIds to set
     */
    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    /**
     * @param roleNames the roleNames to set
     */
    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    /**
     * @param roleLevels the roleLevels to set
     */
    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    /**
     * @return the user_type
     */
    public String getUser_type() {
        return user_type;
    }

    /**
     * @param user_type the user_type to set
     */
    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    /**
     * @return the userType_id
     */
    public String getUserType_id() {
        return userType_id;
    }

    /**
     * @param userType_id the userType_id to set
     */
    public void setUserType_id(String userType_id) {
        this.userType_id = userType_id;
    }
   
}

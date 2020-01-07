package moi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 手机用户对象
 * @author chenj
 *
 */
public class MoiUser {

	private String operatorId;
	
	private String name;
	
	private String mobile;
	
	private String companyName;
	
	private String companyId;
	
	private String orgId;
	
	private String orgName;
	
	private String countyId;
	
	private String orgPath;
	
	//镜像url
	private String sys_url;
	
	//ip地址
	private String server_ip;
	
	//端口
	private String server_port;
	
	private List<Map<String, String>> userRoleList;
	
	private List<Map<String, String>> userModuleList;
	
	private List<Map<String, String>> userModuleParamList;
	
	private Map<String, Object> catalog;
	
	public Map<String, Object> getCatalog() {
		return catalog;
	}

	public void setCatalog(Map<String, Object> catalog) {
		this.catalog = catalog;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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


	public String getOrgPath() {
		return orgPath;
	}

	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}

	public List<Map<String, String>> getUserRoleList() {
		return userRoleList;
	}

	public void setUserRoleList(List<Map<String, String>> userRoleList) {
		this.userRoleList = userRoleList;
	}

	public List<Map<String, String>> getUserModuleList() {
		return userModuleList;
	}

	public void setUserModuleList(List<Map<String, String>> userModuleList) {
		this.userModuleList = userModuleList;
	}
	
	public List<Map<String, String>> getUserModuleParamList() {
        return userModuleParamList;
    }

    public void setUserModuleParamList(List<Map<String, String>> userModuleParamList) {
        this.userModuleParamList = userModuleParamList;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

	/**
     * 获取用户所有角色名称
     * @return
     */
    public String getCpRoleNames(){
        String roleNames = "";
        for (Map<String, String> map: userRoleList) {
            String role_name = map.get("CP_ROLE_NAME").toString();
            roleNames += "," + role_name;
        }
        if (!roleNames.equals("")) {
            roleNames = roleNames.substring(1);
        }
        return roleNames;
    }
    
    
	/**
	 * 获取用户所有角色名称
	 * @return
	 */
	public String getRoleNames(){
		String roleNames = "";
		for (Map<String, String> map: userRoleList) {
			String role_name = map.get("ROLE_NAME").toString();
			roleNames += "," + role_name;
		}
		if (!roleNames.equals("")) {
			roleNames = roleNames.substring(1);
		}
		return roleNames;
	}
	
	/**
	 * 根据模块层级获取模块列表
	 * @return
	 */
	public List<Map<String, String>> getUserModuleByLevel(String module_level){
		List<Map<String, String>>  moduleList = new ArrayList<Map<String, String>>();
		for (Map<String, String> mMap : userModuleList) {
			String level = mMap.get("MODULE_LEVEL").toString();
			if (level.equals(module_level)) {
				moduleList.add(mMap);
			}
		}
		return moduleList;
	}
	
	/**
	 * 根据上级模块id取模块列表
	 * @return
	 */
	public List<Map<String, String>> getUserModuleBySuper(String super_id){
		List<Map<String, String>>  moduleList = new ArrayList<Map<String, String>>();
		for (Map<String, String> mMap : userModuleList) {
			String superior_id = mMap.get("SUPERIOR_ID").toString();
			if (superior_id.equals(super_id)) {
				moduleList.add(mMap);
			}
		}
		return moduleList;
	}
	
	/**
	 * 根据模块参数列表
	 * @return
	 */
	public List<Map<String, String>> getUserModuleParam(String module_id){
		List<Map<String, String>>  moduleParamList = new ArrayList<Map<String, String>>();
		for (Map<String, String> mMap : userModuleParamList) {
			String mod_id = mMap.get("MODULE_ID").toString();
			if (mod_id.equals(module_id)) {
				moduleParamList.add(mMap);
			}
		}
		return moduleParamList;
	}

    public String getSys_url() {
        return sys_url;
    }

    public void setSys_url(String sys_url) {
        this.sys_url = sys_url;
    }

    public String getServer_ip() {
        return server_ip;
    }

    public void setServer_ip(String server_ip) {
        this.server_ip = server_ip;
    }

    public String getServer_port() {
        return server_port;
    }

    public void setServer_port(String server_port) {
        this.server_port = server_port;
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
}

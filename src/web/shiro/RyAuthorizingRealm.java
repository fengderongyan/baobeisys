package web.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import web.bean.SystemInfo;
import web.model.User;

import com.sgy.util.common.StringHelper;

/**
 * 自定义realm类，该类需要继承AuthorizingRealm，并重写认证、授权方法
 * @author chang
 * @createDate Mar 8, 2013
 * @description
 */
public class RyAuthorizingRealm extends AuthorizingRealm{   

	public SystemInfo systemInfo;

	public SystemInfo getSystemInfo() {
		return systemInfo;
	}

	public void setSystemInfo(SystemInfo systemInfo) {
		this.systemInfo = systemInfo;
	}

	public RyAuthorizingRealm() {
		super();
		//设置认证token的实现类，该处使用UsernamepasswordTken,也可自定义token，如果自定义token则需继承AuthenticationToken;
		setAuthenticationTokenClass(UsernamePasswordToken.class);
		/**
		 * 设置加密算法
		 * 前台传入的明文与数据库中的加密数据进行比较，
		 * new Digest()为自定义加密类，集成simpleCredentialsMatcher类，并重写doCredentialsMatch方法
		 */
		setCredentialsMatcher(new Md5SimpleCredentialsMatcher());
	}

	/**
	 * 授权
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		//String loginName = (String) principalCollection.fromRealm(getName()).iterator().next();
		User user = (User)SecurityUtils.getSubject().getSession().getAttribute("user");
		if (null == user) {
			return null;
		} else {
			List<Map<String, Object>> rolePageList = systemInfo.getRolePermitList();
			//authorInfo封装用户权限，
			Set<String> roleIds = new HashSet<String>();
		    Set<String> permits = new HashSet<String>();
		    for (Map<String, Object> userRole : user.getUserRoleList()) {
		    	roleIds.add(StringHelper.get(userRole, "ROLE_ID"));
			}
		    
		    for (Map<String, Object> map : rolePageList) {
		    	String role_id = StringHelper.get(map, "ROLE_ID");
		    	if (user.hasRoles(role_id)) {
		    		permits.add(StringHelper.get(map, "PERMIT_URL"));
				}
			}
			SimpleAuthorizationInfo authorInfo = new SimpleAuthorizationInfo(roleIds);
			authorInfo.setStringPermissions(permits);
			return authorInfo;
		}
	}

	/**  
	 * 认证信息  
	 */  
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		User user = (User)SecurityUtils.getSubject().getSession().getAttribute("user");
		if (null != user) {
			//将从数据库中查询的用户名和密码封装到authenticationInfo对象中；shiro自动根据加密规则将前台的密码加密与之对比；
			return new SimpleAuthenticationInfo(user.getOperatorId(), user.getPassword(), getName());
		}
		return null;
	}   

	/**
	 * 更新用户授权信息缓存. 
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

}  
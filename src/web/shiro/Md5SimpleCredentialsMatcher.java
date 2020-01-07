package web.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class Md5SimpleCredentialsMatcher extends SimpleCredentialsMatcher {
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token,
			AuthenticationInfo info) {
		//对前台传入的明文数据加密，根据自定义加密规则加密，
		Object tokencredential = token.getCredentials();
		//Object tokencredential = StringHelper.md5(toBytes(token.getCredentials()));
		//从数据库获取的加密数据
		Object accunt = this.getCredentials(info);
		//返回对比结果  toBytes(token.getCredentials())
		return equals(accunt,tokencredential);
	}

}

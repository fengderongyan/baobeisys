package weixin.entity;

public class AccessToken {
	private String access_token;
	private long expireTime;
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	public AccessToken(String access_token, String expireIn) {
		super();
		this.access_token = access_token;
		this.expireTime = System.currentTimeMillis() + Integer.parseInt(expireIn) * 1000;
	}
	
	/**
	 * 描述：判断token是否过期
	 * @return
	 * @see weixin.entity.AccessToken#isExpired()
	 * @author zhangyongbin
	 */
	public boolean isExpired(){
		return System.currentTimeMillis() > expireTime;
	}
	
}

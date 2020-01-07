package web.shiro;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

/**
 * 超时的类配置
 * @author chang
 * @createDate Mar 5, 2013
 * @description
 */
public class RyFormAuthenticationFilter extends FormAuthenticationFilter {


	static {
		
	}

	@Override
	protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		HttpServletRequest request1 = (HttpServletRequest) request;
		HttpServletResponse response1 = (HttpServletResponse) response;
		String uri = request1.getRequestURI();
		if (uri.endsWith(".json")) {
			response1.setHeader("sessionstatus", "timeout");
		} else {
			saveRequest(request);
			redirectToLogin(request, response);
		}
	}
}
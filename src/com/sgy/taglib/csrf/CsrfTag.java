package com.sgy.taglib.csrf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * 该类是标签类，作用是生成一个csrfToken放在隐藏域中，并将token放入会话中
 * @author chang
 * @createDate 2014-11-14
 */
public class CsrfTag extends BodyTagSupport {
	
	private static final long serialVersionUID = 1L;
	private JspWriter out;

	public int doStartTag() throws JspException {
		out = pageContext.getOut();
		try {
			Subject currentUser = SecurityUtils.getSubject();
			Session session = currentUser.getSession();
			Map<String, String> csrfTokenMap = (Map<String, String>)session.getAttribute("csrfTokenMap");
			if (null == csrfTokenMap) {//如果用户会话中没有csrfToken集，则新建并放入session中
				csrfTokenMap = new HashMap<String, String>();
				session.setAttribute("csrfTokenMap", csrfTokenMap);
			}
			String csrfToken = UUID.randomUUID().toString()+"|baobeisys";
			String createTime = String.valueOf(System.currentTimeMillis());
			csrfTokenMap.put(csrfToken, createTime);
			out.print("<input type=\"hidden\" id=\"csrfToken\" name=\"csrfToken\" value=\""+csrfToken+"\" />");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}

}
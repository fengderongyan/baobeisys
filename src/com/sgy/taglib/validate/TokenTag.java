package com.sgy.taglib.validate;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import web.model.User;

public class TokenTag extends BodyTagSupport {

	public void printTagAttribute() {
		try {
			Subject currentUser = SecurityUtils.getSubject();
			Session session = currentUser.getSession();
			String operator_id = ((User)session.getAttribute("user")).getOperatorId();
			JspWriter out = pageContext.getOut();
			out.println("<input name=\"4d14a16b976567e3c9bebfda77b622c8\"  value=\"" + operator_id + "\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
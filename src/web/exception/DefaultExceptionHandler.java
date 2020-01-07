package web.exception;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.javaflow.bytecode.transformation.bcel.analyser.ExceptionHandler;
import org.codehaus.jackson.map.annotate.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;

import net.sf.json.JSONObject;

public class DefaultExceptionHandler implements HandlerExceptionResolver{
	
	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
	 
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object arg2,
			Exception ex) {
		ModelAndView mv = new ModelAndView();
		Map errorMap = new HashMap();
		errorMap.put("errorCode", -1);
		errorMap.put("errorInfo", "后台操作失败");
		response.setCharacterEncoding("UTF-8");
		System.out.println(ex.getMessage());
		try {
			response.getWriter().print(JSONObject.fromObject(errorMap));
			logger.error("与客户端通讯异常:" + ex.getMessage(), ex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return mv;

		
	}

}

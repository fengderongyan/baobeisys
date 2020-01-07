package web.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.webwork.interceptor.ApplicationAware;
import com.opensymphony.webwork.interceptor.ServletResponseAware;
import com.opensymphony.webwork.interceptor.ServletRequestAware;
import com.opensymphony.webwork.interceptor.SessionAware;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import web.model.User;

import com.sgy.util.Constants;
import com.sgy.util.common.StringHelper;
import com.sgy.util.spring.RequestHelper;


/**
 * 控制组件基类
 * @date 2016-2-22
 */
public class BaseController  implements SessionAware,
ServletRequestAware, ApplicationAware, ServletResponseAware {
	public static final String AJAX_MESSAGE = "success";
	public static final String SUCCESS = "true";
	public static final String FAIL = "fail";
	public static final String COM_PATH = "WEB-INF/jsp/";
	
	public StringHelper str = new StringHelper();
	public RequestHelper req = new RequestHelper();
	
	public HttpServletResponse response;
	public Map session; 
	
	
	protected Logger logger = Logger.getLogger(getClass());
	
	public User getUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute("user");
	}
	
	public void debug(String message) {
		logger.debug(message);
	}
	public void error(String message) {
		logger.error(message);
	}
	public void info(String message) {
		logger.info(message);
	}
	
	@Autowired
	public HttpServletRequest request;
	
	public void renderText(HttpServletResponse response, String result) throws IOException {
		PrintWriter out = response.getWriter();
		response.setHeader("Cache-Control", "no-store"); 
		response.setHeader("Pragma", "no-cache"); 
		response.setDateHeader("Expires", 0); 
		out.print(result);
		out.flush();
		out.close();
	}
	
	private MessageSource messageSource;
	
	@Resource(name="messageSource")
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public String getMessage(String key) {
		return this.messageSource.getMessage(key, null, null);
	}
	
	public String getAjaxValue(String paramName) {
        return req.getAjaxValue(request, paramName);
    }
	
	public void writeText(Object data, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeHtmlText(Object data, HttpServletResponse response) {
		try {
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeJsonData(Object data, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			if (data instanceof Map) {
				response.getWriter().print(JSONObject.fromObject(data));
			} else if (data instanceof List) {
				JSONArray array = JSONArray.fromObject(((List) data).toArray());
				response.getWriter().print(array);
			} else {
				response.getWriter().print(JSONObject.fromObject(data));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	
	 // 日志记录器
    public final Logger log = Logger.getLogger(this.getClass());
	
	public void writeText(Object data) {
        try {
            logger.debug("response : " + response);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public String getValue(String paramName) {
        return req.getValue(request, paramName);
    }
	
    /**
     * 用于Ajax输出内容
     * @param response
     * @param obj 需要输出的内容
     */
    public void ajaxPrint(HttpServletResponse response, Object obj) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(obj);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
    public void downloadFile(HttpServletResponse response) {
        try {
            String fileUrl = this.getAjaxValue("fileUrl");
            String fileNames[] = fileUrl.split("/");
            String lastFileName = fileNames[fileNames.length - 1];
            response.setCharacterEncoding("gbk");//gbk
            response.addHeader("Content-Disposition", "attachment;filename="
                    + java.net.URLEncoder.encode(lastFileName, "gbk").replace('+', ' '));//下载文件是的文件名要转码以及文件名做处理，如果不转码不能下载对应的文件格式
            InputStream inputStream = new FileInputStream(new File(request.getRealPath("/") + fileUrl));
            OutputStream outputStream = response.getOutputStream();
            int i = 0;
            byte b[] = new byte[2048];
            while((i = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, i);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }


    /* (非 Javadoc) 
     * <p>Title: setSession</p> 
     * <p>Description: </p> 
     * @param arg0 
     * @see com.opensymphony.webwork.interceptor.SessionAware#setSession(java.util.Map) 
     */ 
    @Override
    public void setSession(Map arg0) {
        // TODO Auto-generated method stub
        
    }

    /* (非 Javadoc) 
     * <p>Title: setApplication</p> 
     * <p>Description: </p> 
     * @param arg0 
     * @see com.opensymphony.webwork.interceptor.ApplicationAware#setApplication(java.util.Map) 
     */ 
    @Override
    public void setApplication(Map arg0) {
        // TODO Auto-generated method stub
        
    }

    /* (非 Javadoc) 
     * <p>Title: setServletResponse</p> 
     * <p>Description: </p> 
     * @param arg0 
     * @see com.opensymphony.webwork.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse) 
     */ 
    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * @return the session
     */
    public Map getSession() {
        return session;
    }
}

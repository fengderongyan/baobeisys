package moi.action.setting;

import javax.servlet.http.HttpServletResponse;
import moi.service.setting.ClientUpdateMiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import web.action.BaseController;

/**
 * 客户端版本自动更新
 * @date 2016-2-26
 */
@Controller
@RequestMapping(value = "/mobile/clientupdatemi/")
public class ClientUpdateMiController extends BaseController{

	/**
	 * spring 类型注入
	 */
	@Autowired
	public ClientUpdateMiService clientUpdateMiService;

	/**
	 * 获取当前最新版本及是否自动升级信息
	 * @return
	 */
	@RequestMapping(value="getVersion.do")
	public void getVersion(HttpServletResponse response) {
		this.writeJsonData(clientUpdateMiService.getMaxVersionMark(request),response);
	}
}

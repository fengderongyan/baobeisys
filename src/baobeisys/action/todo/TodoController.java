package baobeisys.action.todo;


import baobeisys.service.todo.TodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import web.action.BaseController;

/**
 * 加载系统首页相关信息
 * @date 2016-2-22
 */
@Controller
@RequestMapping(value = "/todo/")
public class TodoController extends BaseController {
	
	@Autowired
	public TodoService todoService;
	
	/**
     * 首页展示
     * @return
     */
    @RequestMapping(value = "frame.do")
    public String dictFrame() {
        request.setAttribute("mobiLoginList", todoService.getMobiLoginList(request));
        return COM_PATH + "baobeisys/todo/todo";
    }
	
}
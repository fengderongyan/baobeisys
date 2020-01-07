package baobeisys.weixintoken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WeChatMessageController {
    
    @Autowired
    private WeChatMessageService service;
    
    @RequestMapping(value="/getMessage.do", method=RequestMethod.GET)
    @ResponseBody
    public String getMessage(WeChatMessage message) {
        
        return service.checkSignature(message);
    }
}

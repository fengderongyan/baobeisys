package baobeisys.application;



import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sgy.util.spring.SpringHelper;


public class BackgroundApplication implements ServletContextListener {
    
    public void contextDestroyed(ServletContextEvent arg0) {

    }
    public void contextInitialized(ServletContextEvent arg0) {
        System.setProperty("dir_path", arg0.getServletContext().getRealPath("/"));  
        
        //需在applicationContext中手动添加bean
        DoorStatusUpdate doorStatusUpdate = (DoorStatusUpdate)SpringHelper.getBean("doorStatusUpdate");
        //doorStatusUpdate.start();
        //Thread定时执行任务
        /*TimingOperation timingOperation = new TimingOperation();
        timingOperation.start();*/
        
       
    }
}

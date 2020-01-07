package util;

import java.util.List;

import org.apache.log4j.Logger;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

/**
 * 
 * 描述：极光推送工具类<br>
 * @author zhangyongbin
 * @Date : 2019年1月18日
 */
public class JPushUtil {

    private final static Logger LOGGER = Logger.getLogger(JPushUtil.class);

    private final static Boolean ApnsProduction = false;//上线之后要改为true
    
    private final static String masterSecret = PropertiesUtil.getPropertiesValue("jpush_masterSecret");
    private final static String appKey = PropertiesUtil.getPropertiesValue("jpush_appKey");
    private static final String ALERT = "推送信息";
    

    /**
     * 通知推送
     * 备注：推送方式不为空时，推送的值也不能为空；推送方式为空时，推送值不做要求
     * @param type 推送方式：1、“tag”标签推送，2、“alias”别名推送，3、“registrationId”设备ID推送
     * @param value 推送的标签或别名值或设备ID值
     * @param alert 推送的内容
     */
    public static PushResult pushNotice(String type,String value,String alert){
        Builder builder= PushPayload.newBuilder();
        builder.setPlatform(Platform.all());//设置接受的平台，all为所有平台，包括安卓、ios、和微软的
        //设置如果用户不在线、离线消息保存的时间
        Options options=Options.sendno();
        options.setTimeToLive(86400l);    //设置为86400为保存一天，如果不设置默认也是保存一天
        builder.setOptions(options);
        //设置推送方式
        if(type.equals("2")){//alias
            builder.setAudience(Audience.alias(value));//根据别名推送
        }else if(type.equals("1")){//tag
            builder.setAudience(Audience.tag(value));//根据标签推送
        }else if(type.equals("3")){//registrationId
        	builder.setAudience(Audience.registrationId(value));//根据设备ID推送
        }else{
            builder.setAudience(Audience.all());//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
        }
        //设置为采用通知的方式发送消息
        builder.setNotification(Notification.alert(alert));
        PushPayload pushPayload=builder.build();
        PushResult pushResult = null;
        try{
        	System.out.println(masterSecret);
        	System.out.println(appKey);
        	JPushClient jPushClient=new JPushClient(masterSecret, appKey);
        	//JPushClient jPushClient=new JPushClient("57d74d514a6b3bfd03add5f4", "a4341dbc3b8a4fc936f08bdb");
            //进行推送，实际推送就在这一步
            pushResult=jPushClient.sendPush(pushPayload);
        }catch(Exception e){
            e.printStackTrace();
        }
        return pushResult;
    }

    /**
     * 自定义消息推送
     * 备注：推送方式不为空时，推送的值也不能为空；推送方式为空时，推送值不做要求
     * @param type 推送方式：1、“tag”标签推送，2、“alias”别名推送，3 “registrationId”设备ID推送
     * @param value 推送的标签或别名值
     * @param alert 推送的内容 
     */
    public static PushResult pushMsg(String type, String value,String alert){
        Builder builder= PushPayload.newBuilder();
        builder.setPlatform(Platform.all());//设置接受的平台
        if(type.equals("alias")){
            builder.setAudience(Audience.alias(value));//别名推送
        }else if(type.equals("tag")){
            builder.setAudience(Audience.tag(value));//标签推送
        }else if(type.equals("3")){//registrationId
        	builder.setAudience(Audience.registrationId(value));//根据设备ID推送
        }else{
            builder.setAudience(Audience.all());//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
        }
        Message.Builder newBuilder=Message.newBuilder();
        newBuilder.setMsgContent(alert);//消息内容
        Message message=newBuilder.build();
        builder.setMessage(message);
        PushPayload pushPayload=builder.build();
        PushResult pushResult = null;
        try{
        	JPushClient jPushClient=new JPushClient(masterSecret, appKey);
            pushResult=jPushClient.sendPush(pushPayload);
        }catch(Exception e){
            e.printStackTrace();
        }
        return pushResult;
    }
    
    /**
     * 自定义消息推送
     * 备注：推送方式不为空时，推送的值也不能为空；推送方式为空时，推送值不做要求
     * @param type 推送方式：1、“tag”标签推送，2、“alias”别名推送，3 “registrationId”设备ID推送
     * @param value 推送的标签或别名值
     * @param alert 推送的内容 
     */
    public static PushResult pushMsg(String registrationId,  String msg_content,String extraKey, String extrasparam,String notification_title){
        PushPayload pushPayload = pushAlertWithTitle(registrationId, msg_content, extraKey, extrasparam, notification_title);
        PushResult pushResult = null;
        try{
        	JPushClient jPushClient=new JPushClient(masterSecret, appKey);
            pushResult=jPushClient.sendPush(pushPayload);
        }catch(Exception e){
            e.printStackTrace();
        }
        return pushResult;
    }
    

    
    /**
     * 极光推送测试
     */
    public static void main(String[] args){
        //给标签为kefu的用户进行消息推送
        pushNotice("tag","kefu","你有新的任务，请及时处理");
    }

    
    
    
    /**---------------------------------------------------------------------------------------------------------------------------**/
    /**
     * 推送给设备标识参数的用户
     * @param aliasList 别名或别名组
     * @param msg_content 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int sendToAliasList(JPushClient jPushClient,List<String> aliasList,   String msg_content, String extraKey,String extrasparam,String notification_title ) {
        int result = 0;
        try {
            PushPayload pushPayload= buildPushObject_all_aliasList_alertWithTitle(aliasList,msg_content,extraKey,extrasparam,notification_title);
            LOGGER.info("推送给设备标识参数的用户"+pushPayload);
            PushResult pushResult=jPushClient.sendPush(pushPayload);
            LOGGER.info("推送结果"+pushResult);
            if(pushResult.getResponseCode()==200){
                result=1;
            }
        } catch (APIConnectionException e) {
            e.printStackTrace();

        } catch (APIRequestException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 发送给所有用户
     * @param msg_content 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int sendToAll(JPushClient jPushClient,   String msg_content,String extraKey, String extrasparam,String notification_title) {
        int result = 0;
        try {
            PushPayload pushPayload= buildPushObject_android_and_ios(msg_content,extraKey,extrasparam,notification_title);
            LOGGER.info(""+pushPayload);
            PushResult pushResult=jPushClient.sendPush(pushPayload);
            LOGGER.info(""+pushResult);
            if(pushResult.getResponseCode()==200){
                result=1;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return result;
    }

    /**
     * 推送给Tag参数的用户
     * @param tagsList Tag或Tag组
     * @param msg_content 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int sendToTagList(JPushClient jPushClient,List<String> tagsList, String msg_content,String extra, String extrasparam,String notification_title) {
        int result = 0;
        try {
            PushPayload pushPayload= buildPushObject_all_tagList_alertWithTitle(tagsList,msg_content,extra,extrasparam,notification_title);
            LOGGER.info(""+pushPayload);
            PushResult pushResult=jPushClient.sendPush(pushPayload);
            LOGGER.info(""+pushResult);
            if(pushResult.getResponseCode()==200){
                result=1;
            }
        } catch (APIConnectionException e) {
            e.printStackTrace();

        } catch (APIRequestException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 发送给所有安卓用户
     * @param notification_title 通知内容标题
     * @param msg_content 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int sendToAllAndroid(JPushClient jPushClient, String notification_title,  String msg_content,String extraKey, String extrasparam) {
        int result = 0;
        try {
            PushPayload pushPayload= buildPushObject_android_all_alertWithTitle(notification_title,msg_content,extraKey,extrasparam);
            LOGGER.info(""+pushPayload);
            PushResult pushResult=jPushClient.sendPush(pushPayload);
            LOGGER.info(""+pushResult);
            if(pushResult.getResponseCode()==200){
                result=1;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return result;
    }

    /**
     * 发送给所有IOS用户
     * @param msg_content 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int sendToAllIos(JPushClient jPushClient, String msg_content,String extraKey, String extrasparam) {
        int result = 0;
        try {
            PushPayload pushPayload= buildPushObject_ios_all_alertWithTitle(msg_content,extraKey,extrasparam);
            LOGGER.info(""+pushPayload);
            PushResult pushResult=jPushClient.sendPush(pushPayload);
            LOGGER.info(""+pushResult);
            if(pushResult.getResponseCode()==200){
                result=1;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return result;
    }




    /**
     * 向所有平台单个或多个指定别名用户推送消息
     * @param aliasList
     * @param msg_content
     * @param extrasparam
     * @return
     */
    private static PushPayload buildPushObject_all_aliasList_alertWithTitle(List<String> aliasList,  String msg_content,String extraKey, String extrasparam,String notification_title) {

        LOGGER.info("----------向所有平台单个或多个指定别名用户推送消息中......");
        //创建一个IosAlert对象，可指定APNs的alert、title等字段
        //IosAlert iosAlert =  IosAlert.newBuilder().setTitleAndBody("title", "alert body").build();

        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.all())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.alias(aliasList))
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()

                                .setAlert(msg_content)
                                .setTitle(notification_title)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey,extrasparam)

                                .build())
                        //指定当前推送的iOS通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(msg_content)
                                //直接传alert
                                //此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("default")
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey,extrasparam)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                //取消此注释，消息推送时ios将无法在锁屏情况接收
                                // .setContentAvailable(true)

                                .build())


                        .build())
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                /*.setMessage(Message.newBuilder()

                        .setMsgContent(msg_content)

                        .setTitle(msg_title)

                        .addExtra("message extras key",extrasparam)

                        .build())*/

                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(ApnsProduction)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
                        .setTimeToLive(86400)

                        .build())

                .build();

    }
    
    /**
     * 向所有平台单个或多个指定设备ID推送消息
     * @param registrationIdList
     * @param msg_content 内容
     * @param extraKey
     * @param extrasparam
     * @param notification_title 标题
     * @return
     */
    public static PushPayload pushAlertWithTitle(String registrationId,  String msg_content,String extraKey, String extrasparam,String notification_title) {
    	
        LOGGER.info("----------向所有平台单个或多个指定设备ID推送消息......");
        //创建一个IosAlert对象，可指定APNs的alert、title等字段
        //IosAlert iosAlert =  IosAlert.newBuilder().setTitleAndBody("title", "alert body").build();
        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.all())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.registrationId(registrationId))
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()
                        		
                                .setAlert(msg_content)
                                .setTitle(notification_title)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey,extrasparam)

                                .build())
                        //指定当前推送的iOS通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(msg_content)
                                
                                //直接传alert
                                //此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("default")
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey,extrasparam)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                //取消此注释，消息推送时ios将无法在锁屏情况接收
                                // .setContentAvailable(true)

                                .build())


                        .build())
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                .setMessage(Message.newBuilder()

                        .setMsgContent(msg_content)

                        .setTitle(notification_title)

                        .addExtra("message extras key",extrasparam)

                        .build())

                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(ApnsProduction)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
                        .setTimeToLive(86400)

                        .build())

                .build();
    }


    /**
     * 向所有平台所有用户推送消息
     * @param msg_content
     * @param extrasparam
     * @return
     */
    public static PushPayload buildPushObject_android_and_ios(String msg_content,String extraKey, String extrasparam,String notification_title) {
        LOGGER.info("----------向所有平台所有用户推送消息中......");
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                                .setAlert(msg_content)
                                .addPlatformNotification(AndroidNotification.newBuilder()
                                        .setTitle(notification_title)
                                        //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                        .addExtra(extraKey,extrasparam)
                                        .build()
                                )
                                .addPlatformNotification(IosNotification.newBuilder()
//                                //传一个IosAlert对象，指定apns title、title、subtitle等
//                                .setAlert(notification_title)
                                                //直接传alert
                                                //此项是指定此推送的badge自动加1
                                                .incrBadge(1)
                                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                                .setSound("default")
                                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                                .addExtra(extraKey,extrasparam)
                                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                                // .setContentAvailable(true)

                                                .build()
                                )
                                .build()
                )
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                /*.setMessage(Message.newBuilder()
                        .setMsgContent(msg_content)
                        .setTitle(msg_title)
                        .addExtra("message extras key",extrasparam)
                        .build())*/

                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(ApnsProduction)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(86400)
                        .build()
                )
                .build();
    }

    /**
     *向所有平台单个或多个指定Tag用户推送消息
     * @param tagsList
     * @param msg_content
     * @param extrasparam
     * @return
     */
    private static PushPayload buildPushObject_all_tagList_alertWithTitle(List<String> tagsList,  String msg_content,
    			String extraKey, String extrasparam,String notification_title) {

        LOGGER.info("----------向所有平台单个或多个指定Tag用户推送消息中.......");
        //创建一个IosAlert对象，可指定APNs的alert、title等字段
        //IosAlert iosAlert =  IosAlert.newBuilder().setTitleAndBody("title", "alert body").build();

        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.all())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.tag(tagsList))
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()

                                .setAlert(msg_content)
                                .setTitle(notification_title)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey,extrasparam)

                                .build())
                        //指定当前推送的iOS通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(msg_content)
                                //直接传alert
                                //此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("default")
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey,extrasparam)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                //取消此注释，消息推送时ios将无法在锁屏情况接收
                                // .setContentAvailable(true)

                                .build())


                        .build())
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                /*.setMessage(Message.newBuilder()

                        .setMsgContent(msg_content)

                        .setTitle(msg_title)

                        .addExtra("message extras key",extrasparam)

                        .build())*/

                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(ApnsProduction)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
                        .setTimeToLive(86400)

                        .build())

                .build();

    }

    /**
     * 向android平台所有用户推送消息
     * @param notification_title
     * @param msg_content
     * @param extrasparam
     * @return
     */
    private static PushPayload buildPushObject_android_all_alertWithTitle(String notification_title,  String msg_content,String extraKey, String extrasparam) {
        LOGGER.info("----------向android平台所有用户推送消息中......");
        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.android())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.all())
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(msg_content)
                                .setTitle(notification_title)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey,extrasparam)
                                .build())
                        .build()
                )
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                /*.setMessage(Message.newBuilder()
                        .setMsgContent(msg_content)
                        .setTitle(msg_title)
                        .addExtra("message extras key",extrasparam)
                        .build())*/

                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(ApnsProduction)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(86400)
                        .build())
                .build();
    }

    /**
     * 向ios平台所有用户推送消息
     * @param msg_content
     * @param extrasparam
     * @return
     */
    private static PushPayload buildPushObject_ios_all_alertWithTitle(  String msg_content,String extraKey, String extrasparam) {
        LOGGER.info("----------向ios平台所有用户推送消息中.......");
        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.ios())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.all())
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(msg_content)
                                //直接传alert
                                //此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("default")
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey,extrasparam)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                // .setContentAvailable(true)

                                .build())
                        .build()
                )
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                /*.setMessage(Message.newBuilder()
                        .setMsgContent(msg_content)
                        .setTitle(msg_title)
                        .addExtra("message extras key",extrasparam)
                        .build())*/

                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(ApnsProduction)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(86400)
                        .build())
                .build();
    }
    
}
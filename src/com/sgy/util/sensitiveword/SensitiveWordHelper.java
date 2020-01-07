package com.sgy.util.sensitiveword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sgy.util.common.StringHelper;

/** 
 * 敏感词过滤帮助类
 * @author xumx
 * @date 2014-12-08 
 */
public class SensitiveWordHelper {
    public static int minMatchTYpe = 1; //最小匹配规则
    public static int maxMatchType = 2; //最大匹配规则
    private String ENCODING = "GBK";    //字符编码
    private String sensitiveWordFilePath = "/qdyydata01/sensitive_word/"; //敏感词库文件夹
    
    /** 
     * 初始化敏感词库
     * @author xumx 2014-12-09
     * @return
     */ 
    public Map<String,String> initSensitiveWord() {
        HashMap<String, String>  sensitiveWordMap = null;
        try {
            //读取敏感词库文件
            Set<String> sensitiveWordSet = readSensitiveWordFile();
            //将敏感词库加入到HashMap中
            sensitiveWordMap = this.changeSensitiveWordSetToHashMap(sensitiveWordSet);
            
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return sensitiveWordMap;
    }

    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：
     * 中 = {
     *      isEnd = 0
     *      国 = {
     *           isEnd = 1
     *           人 = {isEnd = 0
     *                民 = {isEnd = 1}
     *                }
     *           男  = {
     *                 isEnd = 0
     *                  人 = {
     *                       isEnd = 1
     *                      }
     *              }
     *           }
     *      }
     *  五 = {
     *      isEnd = 0
     *      星 = {
     *          isEnd = 0
     *          红 = {
     *              isEnd = 0
     *              旗 = {
     *                   isEnd = 1
     *                  }
     *              }
     *          }
     *      }
     * @author xumx 2014-12-09
     * @param sensitiveWordSet  敏感词库
     */
    public HashMap<String, String> changeSensitiveWordSetToHashMap(Set<String> sensitiveWordSet) {
        HashMap<String,String> sensitiveWordMap = new HashMap<String, String>(sensitiveWordSet.size()); //初始化敏感词容器，减少扩容操作
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = sensitiveWordSet.iterator();
        while(iterator.hasNext()) {
            key = iterator.next(); //关键字
            nowMap = sensitiveWordMap;
            for(int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i); //转换成char型
                Object wordMap = nowMap.get(keyChar); //获取

                if(wordMap != null) { //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else { //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String, String>();
                    newWorMap.put("isEnd", "0"); //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }
                
                if(i == key.length() - 1) {
                    nowMap.put("isEnd", "1"); //最后一个
                }
            }
        }
        return sensitiveWordMap;
    }
    
    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：
     * 中 = {
     *      isEnd = 0
     *      国 = {
     *           isEnd = 1
     *           人 = {isEnd = 0
     *                民 = {isEnd = 1}
     *                }
     *           男  = {
     *                 isEnd = 0
     *                  人 = {
     *                       isEnd = 1
     *                      }
     *              }
     *           }
     *      }
     *  五 = {
     *      isEnd = 0
     *      星 = {
     *          isEnd = 0
     *          红 = {
     *              isEnd = 0
     *              旗 = {
     *                   isEnd = 1
     *                  }
     *              }
     *          }
     *      }
     * @author xumx 2014-12-09
     * @param sensitiveWordList  敏感词库
     */
    public HashMap<String, String> changeSensitiveWordListToHashMap(List<Map<String,Object>> sensitiveWordList) {
        HashMap<String,String> sensitiveWordMap = new HashMap<String, String>(sensitiveWordList.size()); //初始化敏感词容器，减少扩容操作
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        for (int i=0; i<sensitiveWordList.size(); i++){
            key = StringHelper.get(sensitiveWordList.get(i), "content") ; //关键字
            nowMap = sensitiveWordMap;
            for(int j = 0; j < key.length(); j++) {
                char keyChar = key.charAt(j); //转换成char型
                Object wordMap = nowMap.get(keyChar); //获取

                if(wordMap != null) { //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else { //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String, String>();
                    newWorMap.put("isEnd", "0"); //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }
                
                if(j == key.length() - 1) {
                    nowMap.put("isEnd", "1"); //最后一个
                }
            }
        }
        return sensitiveWordMap;
    }

    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
     * @author xumx 2014-12-09
     * @return
     * @throws Exception
     */ 
    public Set<String> readSensitiveWordFile() throws Exception {
        Set<String> set = new HashSet<String>();
        String word = null;
        File filePath = new File(sensitiveWordFilePath); //敏感词库文件夹
        File[] files = filePath.listFiles(); //文件列表
        if(files != null && files.length > 0) {
            for(File file : files) {
                //读取文件
                File sensitiveWordFile = new File(sensitiveWordFilePath + file.getName()); 
                InputStreamReader read = new InputStreamReader(new FileInputStream(sensitiveWordFile), ENCODING);
                try {
                    if(sensitiveWordFile.isFile() && sensitiveWordFile.exists()) { //文件流是否存在
                        BufferedReader bufferedReader = new BufferedReader(read);
                        word = null;
                        while((word = bufferedReader.readLine()) != null) { //读取文件，将文件内容放入到set中
                            set.add(word);
                        }
                    } else { //不存在抛出异常信息
                        throw new Exception("敏感词库文件不存在");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    read.close(); //关闭文件流
                }
            }
        } else {
            System.out.println(" 不存在敏感词库！ ");
        }
        return set;
    }

    /** 
     * 判断文字是否包含敏感字符
     * @author xumx 2014-12-09
     * @param sensitiveWordMap
     * @param txt
     * @param matchType 匹配规则: 1：最小匹配规则，2：最大匹配规则
     * @return 若包含返回true，否则返回false
     */ 
    public boolean isContaintSensitiveWord(Map sensitiveWordMap, String txt, int matchType) {
        boolean flag = false;
        for(int i = 0; i < txt.length(); i++) {
            int matchFlag = this.CheckSensitiveWord(sensitiveWordMap, txt, i, matchType); //判断是否包含敏感字符
            if(matchFlag > 0) { //大于0存在，返回true
                flag = true;
            }
        }
        return flag;
    }

    /** 
     * 获取文字中的敏感词
     * @author xumx 2014-12-09
     * @param sensitiveWordMap
     * @param txt
     * @param matchType 匹配规则: 1：最小匹配规则，2：最大匹配规则
     * @return
     */ 
    public Set<String> getSensitiveWord(Map sensitiveWordMap, String txt, int matchType) {
        Set<String> sensitiveWordList = new HashSet<String>();
        for(int i = 0; i < txt.length(); i++) {
            int length = CheckSensitiveWord(sensitiveWordMap, txt, i, matchType); //判断是否包含敏感字符
            if(length > 0) { //存在,加入list中
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1; //减1的原因，是因为for会自增
            }
        }
        return sensitiveWordList;
    }

    /** 
     * 检查文字中是否包含敏感字符
     * @author xumx 2014-12-09
     * @param sensitiveWordMap
     * @param txt
     * @param beginIndex
     * @param matchType
     * @return 如果存在，则返回敏感词字符的长度，不存在返回0
     */ 
    public int CheckSensitiveWord(Map sensitiveWordMap, String txt, int beginIndex, int matchType) {
        boolean flag = false; //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;    //匹配标识数默认为0
        char word = 0;
        Map nowMap = sensitiveWordMap;
        for(int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word); //获取指定key
            if(nowMap != null) { //存在，则判断是否为最后一个
                matchFlag++; //找到相应key，匹配标识+1 
                if("1".equals(nowMap.get("isEnd"))) { //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true; //结束标志位为true   
                    if(SensitiveWordHelper.minMatchTYpe == matchType) { //最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            } else { //不存在，直接返回
                break;
            }
        }
        if(matchFlag < 2 || !flag) { //长度必须大于等于1，为词 
            matchFlag = 0;
        }
        return matchFlag;
    }
    
    /** 
     * 测试
     * @author xumx 2014-12-09
     * @param args
     */ 
    public static void main(String[] args) {
        SensitiveWordHelper filter = new SensitiveWordHelper();
        Map sensitiveWordMap = filter.initSensitiveWord();
        String txt = "太多的伤感情怀也许只局限于饲养基地 荧幕中的情节，中日没有不友好的,欠高利贷的主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
                   + "然后法轮功 我们的扮演的角色就是跟随着主人公的喜红客联盟 贱人 怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"
                   + "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三级片 深人静的晚上，关上电话静静的发呆着。";
        long beginTime = System.currentTimeMillis();
        Set<String> set = filter.getSensitiveWord(sensitiveWordMap,txt, 1);
        long endTime = System.currentTimeMillis();
    }
}

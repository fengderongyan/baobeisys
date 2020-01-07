package com.sgy.util.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.sgy.util.Constants;
import com.sgy.util.db.DBHelperSpring;

public class FileHelper 
{
	public DBHelperSpring db;
	
	public FileHelper() {}
	
	public FileHelper(DBHelperSpring db) {
		this.db = db;
	}
	
	/**
	 * 读取文本文件内容
	 * @param filePathAndName 带有完整绝对路径的文件名
	 * @param encoding 文本文件打开的编码方式
	 * @return 返回文本文件的内容
	 */
	public String readTxt(String filePathAndName, String encoding)
			throws IOException {
		encoding = encoding.trim();
		StringBuffer str = new StringBuffer("");
		String st = "";
		try {
			FileInputStream fs = new FileInputStream(filePathAndName);
			InputStreamReader isr;
			if (encoding.equals("")) {
				isr = new InputStreamReader(fs);
			} else {
				isr = new InputStreamReader(fs, encoding);
			}
			BufferedReader br = new BufferedReader(isr);
			try {
				String data = "";
				while ((data = br.readLine()) != null) {
					str.append(data + " ");
				}
			} catch (Exception e) {
				str.append(e.toString());
			}
			st = str.toString();
		} catch (IOException es) {
			st = "";
		}
		return st;
	}

	/**
	 * 新建目录
	 * @param folderPath 目录
	 * @return 返回目录创建后的路径
	 */
	public String createFolder(String folderPath) {
		String txt = folderPath;
		try {
			java.io.File myFilePath = new java.io.File(txt);
			txt = folderPath;
			if (!myFilePath.exists()) {
				myFilePath.mkdir();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return txt;
	}

	/**
	 * 多级目录创建
	 * @param folderPath 准备要在本级目录下创建新目录的目录路径 例如 c:myf
	 * @param paths 无限级目录参数，各级目录以单数线区分 例如 a|b|c
	 * @return 返回创建文件后的路径 例如 c:myfac
	 */
	public String createFolders(String folderPath, String paths) {
		String txts = folderPath;
		try {
			String txt;
			txts = folderPath;
			StringTokenizer st = new StringTokenizer(paths, "|");
			for (int i = 0; st.hasMoreTokens(); i++) {
				txt = st.nextToken().trim();
				if (txts.lastIndexOf("/") != -1) {
					txts = createFolder(txts + txt);
				} else {
					txts = createFolder(txts + txt + "/");
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return txts;
	}

	/**
	 * 新建文件
	 * @param filePathAndName 文本文件完整绝对路径及文件名
	 * @param fileContent 文本文件内容
	 * @return
	 */
	public void createFile(String path,String fileName, String fileContent) {
		String filePath ="";
		try {
			 filePath = path+fileName;
		
			File myFilePath = new File(path,fileName);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			FileWriter resultFile = new FileWriter(myFilePath);
			PrintWriter myFile = new PrintWriter(resultFile);
			String strContent = fileContent;
			myFile.println(strContent);
			myFile.close();
			resultFile.close();
		} 
		catch (Exception e) {
			System.out.println(filePath);
			e.printStackTrace();
		}
	}

	 
	/**
	 * 删除文件
	 * @param filePathAndName 文本文件完整绝对路径及文件名
	 * @return Boolean 成功删除返回true遭遇异常返回false
	 */
	public boolean delFile(String filePathAndName) {
		boolean bea = false;
		try {
			String filePath = filePathAndName;
			File myDelFile = new File(filePath);
			if (myDelFile.exists()) {
				myDelFile.delete();
				bea = true;
			} 
			else {
				bea = false;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return bea;
	}

	/**
	 * 删除文件夹
	 * @param folderPath 文件夹完整绝对路径
	 * @return
	 */
	public void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定文件夹下所有文件
	 * @param path 文件夹完整绝对路径
	 * @return
	 */
	public boolean delAllFile(String path) {
		boolean bea = false;
		File file = new File(path);
		if (!file.exists()) {
			return bea;
		}
		if (!file.isDirectory()) {
			return bea;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				bea = true;
			}
		}
		return bea;
	}

	/**
	 * 复制单个文件
	 * @param oldPathFile 准备复制的文件源
	 * @param newPathFile 拷贝到新绝对路径带文件名
	 * @return
	 */
	public int copyFile(String oldPathFile, String newPathFile) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPathFile);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPathFile); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPathFile);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					// System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				fs.close();
				inStream.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	/**
	 * Spring 复制单个文件
	 * @param oldfile
	 * @param newPathFile
	 */
	public int copyFile(MultipartFile oldfile, String newPathFile) {
		try {
			int bytesum = 0;
			int byteread = 0;
			if (null != oldfile && !oldfile.getOriginalFilename().equals("") /*&& !oldfile.isEmpty()*/) { // 文件存在时
				InputStream inStream = oldfile.getInputStream(); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPathFile);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				fs.close();
				inStream.close();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	public static int copyFile(File oldfile, String newPathFile) {
		try {
			int bytesum = 0;
			int byteread = 0;
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldfile.getPath()); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPathFile);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					// System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				fs.close();
				inStream.close();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	/**
	 * 复制整个文件夹的内容
	 * @param oldPath 准备拷贝的目录
	 * @param newPath 指定绝对路径的新目录
	 * @return
	 */
	public void copyFolder(String oldPath, String newPath) {
		try {
			new File(newPath).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 移动文件
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public void moveFile(String oldPath, String newPath) {
		copyFile(oldPath, newPath);
		delFile(oldPath);
	}

	/**
	 * 移动目录
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public void moveFolder(String oldPath, String newPath) {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);
	}

	/**
	 * 上传文件后需要重命名文件
	 * 
	 * @param upload_file_name
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String getToFileName(String upload_file_name) {
		int idx = upload_file_name.lastIndexOf(".");
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String to_file_name = uuid + "." + upload_file_name.substring(idx + 1);
		return to_file_name;
	}
	
	/**
	 * 附件上传
	 * @author 戴晓飞
	 * @param file       文件
	 * @param moduleName 模块名称
	 * @param filePath   新文件路径
	 * @param uploadOpeartor  上传人
	 * @param tableName  数据库表名
	 * @param orgId  组织
	 * @param isTemplateFile  是否是模板 1 是   0否
	 * @return 返回文件编号；返回InsertTableFail：插入数据库异常；返回CopyFileFail：复制文件异常。
	 */
	public String uploadAttachment(MultipartFile file,String moduleName, String filePath,
			String uploadOpeartor, String tableName,String orgId, String isTemplateFile){
		return this.uploadAttachment(file, moduleName, filePath, uploadOpeartor, tableName, orgId, isTemplateFile, "1");
	}
	
	
	/**
	 * 附件上传
	 * @author 戴晓飞
	 * @param file       文件
	 * @param moduleName 模块名称
	 * @param filePath   新文件路径
	 * @param uploadOpeartor  上传人
	 * @param tableName  数据库表名
	 * @param orgId  组织
	 * @param isTemplateFile  是否是模板 1 是   0否
	 * @param renameFlag  重命名 1 是
	 * @return 返回文件编号；返回InsertTableFail：插入数据库异常；返回CopyFileFail：复制文件异常。
	 */
	public String uploadAttachment(MultipartFile file,String moduleName, String filePath,
			String uploadOpeartor, String tableName,String orgId,String isTemplateFile, String renameFlag)
	{
		// 如果文件为空
		if(file.isEmpty())
		{
			return "";
		}
		String fileFileName = file.getOriginalFilename();	// 原始文件名
		String attachment_suffix = "";
		if(fileFileName.lastIndexOf(".") != -1)
		{
		    attachment_suffix = fileFileName.substring(fileFileName.lastIndexOf(".") + 1).toLowerCase();
		}
		
		long attachment_size = file.getSize();      // 文件大小
		String attachmentId = UUID.randomUUID().toString(); // 附件编号
		String newFileName = fileFileName;	// 新文件名
		if (renameFlag.equals("1")) {
			newFileName = attachmentId + "." + attachment_suffix;
		}
		File saveDir = new File(filePath);
        // 如果目录不存在，创建目录
		if (!saveDir.exists() && !saveDir.isDirectory())
        {
            saveDir.mkdirs();
        }
		
		// 将文件复制到指定目录中
		int exc = copyFile(file, filePath + newFileName);
		if(exc == 1) 
		{ //复制文件成功
			// 将文件路径filePath的文件根路径截掉
			filePath = filePath.replaceAll(Constants.ATTACHMENT_ROOT, "") + newFileName;
			// 插入附件表
			String sql = "insert into t_attachment(attachment_id,attachment_path,real_name,upload_time,upload_opeartor, " +
			 			 "       link_tablename,attachment_suffix,attachment_size,module_name,is_template_file,org_id) " + 
			 			 "values(?, ?, ?, sysdate, ?, ?, ?, ?, ?,?,?)";
			exc = db.update(sql, new Object[]{attachmentId, filePath, fileFileName, uploadOpeartor, 
			         tableName, attachment_suffix, attachment_size, moduleName,isTemplateFile,orgId});
			if(exc != 1)
			{
				File uploadFile = new File(filePath + newFileName);
				uploadFile.delete();
				
				return "InsertTableFail";
			}
		} 
		else  //复制文件失败
		{ 
			return "CopyFileFail";
		}
		
		return attachmentId;
	}
	
	/**
     * 更新附件信息
     * @author 戴晓飞
     * @param file       文件
     * @param attachmentId   附件编号
     * @param systemId   系统编号
     * @param moduleName 模块名称
     * @param filePath   新文件路径
     * @param uploadOpeartor  上传人
     * @param tableName  数据库表名
     * @param busiId     业务编号（对应数据库表中的业务编号）
     * @return 1：成功，0：文件为空
     */
    public int updateAttachment(MultipartFile file, String attachmentId, String moduleName, 
            String filePath, String uploadOpeartor,String orgId)
    {
        // 如果文件为空
        if(file.isEmpty()) {
            return 0;
        }
        
        // 删除原附件
        this.deleteAttachment(attachmentId);
        
        String fileFileName = file.getOriginalFilename();   // 原始文件名
        String attachmentSuffix = "";
        if(fileFileName.lastIndexOf(".") != -1) {
            attachmentSuffix = fileFileName.substring(fileFileName.lastIndexOf(".") + 1).toLowerCase();
        }
        
        long attachment_size = file.getSize();      // 文件大小
        String newFileName = attachmentId + "." + attachmentSuffix; // 新文件名
        File saveDir = new File(filePath);
        // 如果目录不存在，创建目录
        if (!saveDir.exists() && !saveDir.isDirectory()) {
            saveDir.mkdirs();
        }
        
        String sql = "";
        // 将文件复制到指定目录中
        int exc = copyFile(file, filePath + newFileName);
        if(exc == 1) { //复制文件成功
            // 将文件路径filePath的文件根路径截掉
            filePath = filePath.replaceAll(Constants.ATTACHMENT_ROOT, "") + newFileName;
            // 插入附件表
            sql = "update t_attachment a " +
                  "   set a.real_name = ?, a.attachment_path = ?, a.upload_time = sysdate, " +
                  "       a.attachment_suffix = ?, a.attachment_size = ?, " +
                  "       a.module_name = ?, a.operating_code = ?, a.operating_date = sysdate " + 
                  " where a.attachment_id = ? ";
            exc = db.update(sql, new Object[]{fileFileName, filePath, attachmentSuffix, attachment_size, 
                    moduleName, uploadOpeartor, attachmentId});
        } 
        
        return exc;
    }
    
    /**
     * 删除附件，不更新附件记录
     * @author 戴晓飞
     * @param attachmentId 附件编号
     * @return true：附件删除成功； false：附件删除失败
     */
    public boolean deleteAttachment(String attachmentId)
    {
        // 如果附件编号为空，则直接返回
        if(attachmentId == null || "".equals(attachmentId)) {
            return false;
        }
        
        String sql = "";
        // 查询附件地址
        sql = "select a.attachment_path from t_attachment a where a.attachment_id = ?";
        String attachmentPath = db.queryForString(sql, new Object[]{attachmentId});
        
        File file = new File(Constants.ATTACHMENT_ROOT+attachmentPath);
        if(file.exists()) {
            return file.delete();
        }
        
        return false;
    }
	
	/**
	 * 删除附件
	 * @author 戴晓飞
	 * @param attachmentId 附件编号
	 * @return true：附件删除成功； false：附件删除失败
	 */
	public boolean deleteAttachment(String operatorId, String attachmentId)
	{
		// 如果附件编号为空，则直接返回
		if(attachmentId == null || "".equals(attachmentId))
		{
			return false;
		}
		
		String sql = "";
		// 查询附件地址
		sql = "select a.attachment_path from t_attachment a where a.attachment_id = ?";
		String attachmentPath = db.queryForString(sql, new Object[]{attachmentId});
		// 删除附件记录
		sql = "update t_attachment a " +
			  "   set a.status = 0, a.operating_code = ?, a.operating_date = sysdate " +
			  " where a.attachment_id = ?";
        int exc = db.update(sql, new Object[]{operatorId, attachmentId});
        if(exc == 1)
        {
            File file = new File(attachmentPath);
            if(file.exists())
            {
                return file.delete();
            }
        }
		
		return false;
	}
	
	
	/**
	 * 下载附件
	 * @author 戴晓飞
	 * @param attachmentId 附件编号
	 * @return -1：文件不存在  -2：文件读写异常  1：文件下载成功
	 * @throws Exception 
	 */
	public int downloadAttachment(String attachmentId, HttpServletResponse response) throws Exception
	{
	    String sql = "";
        // 查询附件地址
        sql = "select a.attachment_path, a.real_name from t_attachment a where a.attachment_id = ?";
        Map<String, Object> map = db.queryForMap(sql, new Object[]{attachmentId});
        
        String attachmentPath = StringHelper.get(map, "attachment_path");
        String fileName = StringHelper.get(map, "real_name");
        
        File file = new File(Constants.ATTACHMENT_ROOT + attachmentPath);
        if(!file.exists()) {
            return -1; // 文件不存在
        }
        
        FileInputStream fis = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
        	String newFileName = URLEncoder.encode(fileName, "utf-8").replaceAll("\\+", " ");
            response.reset();
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment;filename=" + newFileName);
            
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[1024];
            int bytesRead;
            while(-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        }
        catch (Exception ex) {
            return -2;  // 文件读写异常
        }
        finally {
            try {
                bos.close();
                bis.close();
                fis.close();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
        
        return 1;   // 文件下载成功
	}
	

    /**
     * 根据不同的操作系统得到对应文件路径
     * @author 徐明响
     * @param dirPath 文件路径
     */
    public String getPathBySeparator(String dirPath)
    {
        String separatorPath = "";
        // 非windows系统   
        if (File.separator.equals("/"))
        {
            separatorPath=dirPath.replaceAll("\\\\", "/");
        }
        // windows系统  
        else 
        {
            separatorPath=dirPath.replaceAll("/", "\\\\");
        }
        
        return separatorPath;
    }
    
    /**
     * 获取用UUID生成的新的文件名称和编号
     * @author 徐明响
     * @param file_name 文件名称(带后缀)
     */
	public Map<String, String> getNewFileInfoByUUID(String file_name) 
	{
		Map<String, String> newFileInfoMap = new HashMap<String, String>();
        String new_file_id = UUID.randomUUID().toString(); 
		String new_file_name = new_file_id + "." + file_name.substring(file_name.lastIndexOf(".") + 1);
		newFileInfoMap.put("new_file_id", new_file_id);
		newFileInfoMap.put("new_file_name", new_file_name);
		return newFileInfoMap; 
	}
}
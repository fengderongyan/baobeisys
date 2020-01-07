package com.sgy.util.common;

import java.io.File;
import java.io.IOException;

import com.sgy.util.db.DBHelperSpring;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;

public class UnrarHelper {
	
	public DBHelperSpring db;
	
	public UnrarHelper()
	{
		
	}
	
	/** 
	 * @Description: 获取指定压缩包中的文件夹和文件总数
	 * @author 徐明响 2013-12-08
	 * @param srcRarFile  要解压的压缩文件包
	 * @return -1：获取失败   其他：文件夹和文件总数
	 */
	public static int getUnrarFileCount(String srcRarFile) throws IOException 
	{  
        File file = new File(srcRarFile);
        Archive archive = null;  
        try
        {
        	archive = new Archive(file);
            FileHeader fileHeader = archive.nextFileHeader();
            int m = 0;
            while(null != fileHeader)
            {
               m++;
               fileHeader = archive.nextFileHeader();
            }
            return m;
        }
        catch (RarException e)
        {
            e.printStackTrace();
            return -1;
        }
    }
}
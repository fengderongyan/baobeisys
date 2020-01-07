package moi.service.setting;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import web.service.BaseService;

/**
 * 客户端版本自动更新
 * @author CHEN
 * @date 2014-01-27
 */
@Service("clientUpdateMiService")
public class ClientUpdateMiService extends BaseService{

	/**
	 * 获取当前最新版本及是否自动升级信息 是否自动升级 0：当前版本已经是最新版本 1：强制升级当前版本 2：不强制升级当前版本
	 * 
	 */
	public Map<String, String> getMaxVersionMark(HttpServletRequest request) {
		Map<String, String> resultMap = new HashMap<String, String>();
		int result = 1;// 返回是否强制升级结果
		String versionName = req.getValue(request, "versionName");// 客户端当前版本
		String sql = "select * from t_mobile_version a where a.version_code = (select max(b.version_code) from t_mobile_version b where b.status = 1)";
		Map sqlResultMap = db.queryForMap(sql);
		String versionCode = (String) sqlResultMap.get("version_code");
		String updateGrade = sqlResultMap.get("update_grade") + "";
		String updateMust = (String) sqlResultMap.get("update_must");
		String updateUnmust = (String) sqlResultMap.get("update_unmust");
		logger.debug("====versionCode==="+versionCode);
		logger.debug("====versionName==="+versionName);
		
		if (versionName.compareTo(versionCode) >= 0) {
			result = 0;
		} else {
			if ("1".equals(updateGrade)) {
				result = 1;
			} else if ("2".equals(updateGrade)) {
				result = 2;
			} else {
				// 根据已配置版本升级规则判断返回结果
				result = checkVersion(versionName, versionCode, updateMust,
						updateUnmust);
			}
		}
		resultMap.put("NEWVERSIONCODE", versionCode);
		resultMap.put("ISMUSTUPDATE", result + "");
		System.out.println(sql);
		return resultMap;
	}
	
	/**
	 * 根据配置规则判断当前客户端版本需不需要更新
	 * @param nowVersionName 客户端当前版本
	 * @param newVersionName 客户端最新版本
	 * @param update_must 强制升级版本
	 * @param update_unmust 不强制升级版本
	 * @return
	 */
	public int checkVersion(String nowVersionName, String newVersionName,
			String updateMust, String updateUnmust) {
		int result = 1;// 返回结果
		String[] upMust = updateMust.split(",");
		String childUpMust = "";// "," 分隔update_must以后的子字符串
		String childUpMustStartMark = "";// 子字符串开头符号
		String childUpMustEndMark = "";// 子字符串结束符号
		String childUpMustStart = "";// 子字符串开始版本号
		String childUpMustEnd = "";// 子字符串结束版本号

		for (int i = 0; i < upMust.length; i++) {
			childUpMust = upMust[i];
			if (childUpMust.indexOf("(") >= 0 || childUpMust.indexOf(")") >= 0 || childUpMust.indexOf("[") >= 0
					|| childUpMust.indexOf("]") >= 0) {
				childUpMustStartMark = childUpMust.substring(0, 1);
				childUpMustStart = childUpMust.substring(1, childUpMust.indexOf("~"));
				childUpMustEnd = childUpMust.substring(childUpMust.indexOf("~") + 1, childUpMust.length() - 1);
				childUpMustEndMark = childUpMust.substring(childUpMust.length() - 1, childUpMust.length());
				/*
				 * 判断表达式结果[1.1~1.8) []包含配置版本信息 ()不包含配置版本信息
				 */
				if ("(".equals(childUpMustStartMark)) {
					if (")".equals(childUpMustEndMark)) {
						if (versionCompare(nowVersionName, childUpMustStart) > 0
								&& versionCompare(nowVersionName,childUpMustEnd) < 0) {
							result = 1;
						} else {
							result = 2;
						}
					}
					else if ("]".equals(childUpMustEndMark)) {
						if (versionCompare(nowVersionName, childUpMustStart) > 0
								&& versionCompare(nowVersionName,childUpMustEnd) <= 0) {
							result = 1;
						} else {
							result = 2;
						}
					}
				}
				else if ("[".equals(childUpMustStartMark)) {
					if (")".equals(childUpMustEndMark)) {
						if (versionCompare(nowVersionName, childUpMustStart) >= 0
								&& versionCompare(nowVersionName,
										childUpMustEnd) < 0) {
							result = 1;
						} else {
							result = 2;
						}
					}
					else if ("]".equals(childUpMustEndMark)) {
						if (versionCompare(nowVersionName, childUpMustStart) >= 0
								&& versionCompare(nowVersionName,
										childUpMustEnd) <= 0) {
							result = 1;
						} else {
							result = 2;
						}
					}
				}
				// 单独配置的版本直接比较
			} else if (childUpMust.equals(nowVersionName)) {
				childUpMust = null;
				result = 1;
			}
		}
		return result;
	}

	/**
	 * 版本比较大小 v1 > v2:result > 0 v1 < v2:result < 0
	 * System.out.println(versionCompare("1.2", "1.2.1"));
	 * System.out.println(versionCompare("2.4.7", "2.4.6"));
	 * System.out.println(versionCompare("2.4.6", "2.4.5"));
	 * System.out.println(versionCompare("", "2.4.5"));
	 * System.out.println(versionCompare("2.41.6", "2.4.1"));
	 * System.out.println(versionCompare("12.4.5", "2.4.5"));
	 * System.out.println(versionCompare("12.4.5", "1.1.1"));
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static int versionCompare(String v1, String v2) {
		StringTokenizer t1 = new StringTokenizer(v1, "._");
		StringTokenizer t2 = new StringTokenizer(v2, "._");
		while (t1.hasMoreTokens()) {
			if (!t2.hasMoreTokens())
				return 1;
			int n1 = Integer.parseInt(t1.nextToken());
			int n2 = Integer.parseInt(t2.nextToken());
			int d = n1 - n2;
			if (d != 0)
				return d;
		}
		return t2.hasMoreTokens() ? -1 : 0;
	}
}

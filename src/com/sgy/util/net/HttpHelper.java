package com.sgy.util.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class HttpHelper {
	HttpClient httpClient = new HttpClient();
	public Parser myParser;
	public PostMethod postMethod;
	public NodeList nodeList;

	public int executePostMethod(String url, String[][] data) throws Exception {
		postMethod = new MyPostMethod(url);
		NameValuePair[] nvp = new NameValuePair[data.length];
		for (int i = 0; i < data.length; i++) {
			nvp[i] = new NameValuePair(data[i][0], data[i][1]);
		}
		postMethod.setRequestBody(nvp);
		return httpClient.executeMethod(postMethod);
	}

	public PostMethod getPostMethod(String url, String[][] data)
			throws Exception {
		PostMethod postMethod = new MyPostMethod(url);
		NameValuePair[] nvp = new NameValuePair[data.length];
		for (int i = 0; i < data.length; i++) {
			nvp[i] = new NameValuePair(data[i][0], data[i][1]);
		}
		postMethod.setRequestBody(nvp);
		httpClient.executeMethod(postMethod);
		return postMethod;
	}

	public String getResponseString(String url, String[][] data)
			throws HttpException, IOException {
		PostMethod postMethod = new MyPostMethod(url);
		NameValuePair[] nvp = new NameValuePair[data.length];
		for (int i = 0; i < data.length; i++) {
			nvp[i] = new NameValuePair(data[i][0], data[i][1]);
		}
		postMethod.setRequestBody(nvp);
		httpClient.executeMethod(postMethod);
		InputStream in = postMethod.getResponseBodyAsStream();
		byte[] responseBody = null;
		ByteArrayOutputStream buffer = null;
		if (in != null) {
			byte[] tmp = new byte[4096];
			int bytesRead = 0;
			buffer = new ByteArrayOutputStream(1024);
			while ((bytesRead = in.read(tmp)) != -1) {
				buffer.write(tmp, 0, bytesRead);
			}
			responseBody = buffer.toByteArray();
		}
		in.close();
		return new String(responseBody, "UTF-8");
	}
	
	/**
	 * 已get方式请求远程连接，返回请求结果字符串
	 * @param url
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public String doGet(String url) throws HttpException, IOException {
		GetMethod getMethod = new GetMethod(url);
		httpClient.executeMethod(getMethod);
		int statusCode = httpClient.executeMethod(getMethod);
		String resString = "";
		if(statusCode == HttpStatus.SC_OK){  
			System.err.println("method failed"+getMethod.getStatusLine());
			byte[] responseBody = getMethod.getResponseBody();
			//处理内容
			resString = new String(responseBody, "UTF-8");
		}
		
		return resString;
	}

	public int executePostMethod(String url) throws Exception {
		postMethod = new MyPostMethod(url);
		return httpClient.executeMethod(postMethod);
	}

	public void releaseConnection() {
		postMethod.releaseConnection();
	}

	public String getResponseBodyAsString() throws Exception {
		InputStream in = postMethod.getResponseBodyAsStream();
		byte[] responseBody = null;
		ByteArrayOutputStream buffer = null;
		if (in != null) {
			byte[] tmp = new byte[4096];
			int bytesRead = 0;
			buffer = new ByteArrayOutputStream(1024);
			while ((bytesRead = in.read(tmp)) != -1) {
				buffer.write(tmp, 0, bytesRead);
			}
			responseBody = buffer.toByteArray();
		}
		in.close();
		return new String(responseBody, "UTF-8");
	}

	/**
	 * @Param bodyStr
	 *            锟斤拷锟斤拷HTML锟斤拷bodyString
	 */
	public int getTableNum(String bodyStr) throws ParserException {
		myParser = Parser.createParser(bodyStr, "UTF-8");
		NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
		OrFilter lastFilter = new OrFilter();
		lastFilter.setPredicates(new NodeFilter[]{tableFilter});
		nodeList = myParser.parse(lastFilter);
		Node[] nodes = nodeList.toNodeArray();
		return nodes.length;
	}
	
	public NodeList getTableNodes(String bodyStr) throws ParserException {
		myParser = Parser.createParser(bodyStr, "UTF-8");
		NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
		OrFilter lastFilter = new OrFilter();
		lastFilter.setPredicates(new NodeFilter[]{tableFilter});
		return myParser.parse(lastFilter);
	}

	/**
	 * @Param t
	 *            table锟斤拷nodes锟叫碉拷锟斤拷锟斤拷
	 */
	public String[][] getTableArray(int t) throws ParserException {
		if (nodeList == null) {
			return null;
		}
		Node[] nodes = nodeList.toNodeArray();
		TableTag tg = (TableTag) nodes[t];
		TableRow[] tr = tg.getRows();
		String[][] tableArr = new String[tr.length][100];
		for (int i = 0; i < tr.length; i++) {
			TableColumn[] td = tr[i].getColumns();
			for (int j = 0; j < td.length; j++) {
				tableArr[i][j] = td[j].toPlainTextString();
			}
		}
		return tableArr;
	}

	/**
	 * @Param bodyStr
	 *            锟斤拷锟斤拷HTML锟斤拷bodyString
	 * @Param t
	 *            锟斤拷t锟斤拷Table
	 */

	public String[][] getTableArray(String bodyStr, int t)
			throws ParserException {
		NodeList nodeList = null;
		myParser = Parser.createParser(bodyStr, "UTF-8");
		NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
		OrFilter lastFilter = new OrFilter();
		lastFilter.setPredicates(new NodeFilter[]{tableFilter});
		nodeList = myParser.parse(lastFilter);
		Node[] nodes = nodeList.toNodeArray();
		TableTag tg = (TableTag) nodes[t - 1];
		TableRow[] tr = tg.getRows();
		String[][] tableArr = new String[tr.length][100];

		for (int i = 0; i < tr.length; i++) {
			TableColumn[] td = tr[i].getColumns();
			for (int j = 0; j < td.length; j++) {
				tableArr[i][j] = td[j].toPlainTextString();
			}
		}
		return tableArr;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟结交锟斤拷锟斤拷锟斤拷锟斤拷
	 */
	public static class MyPostMethod extends PostMethod {
		public MyPostMethod(String url) {
			super(url);
		}
		@Override
		public String getRequestCharSet() {
			// return super.getRequestCharSet();
			return "UTF-8";
		}
	}
	
	public static void main(String[] args) {
		HttpHelper hh = new HttpHelper();
		try {
			String resString = hh.doGet("http://www.baidu.com");
		} catch (HttpException e) {
			
		} catch (IOException e) {
			
		}
	}

}

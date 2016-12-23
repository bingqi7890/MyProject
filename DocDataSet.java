package com.java.version2;

import java.util.HashSet;
import java.util.Iterator;


/**
 * 
 * @date 2016-11-11 15:31:21
 * @description 封装第一张表的数据
 * @author ruin
 */
public class DocDataSet {
	
	private HashSet<DocData> datas=new HashSet<DocData>();
	
	/**
	 * @description 插入数据
	 * @param docid
	 * @param filename
	 * @param url
	 * @param text
	 */
	public void insertData(int docid,String filename,String url,String text){
		datas.add(new DocData(docid,filename,url,text));
	}
	
	/**
	 * @description 获得迭代器
	 * @return
	 */
	public Iterator<DocData> getIterator(){
		return datas.iterator();
	}
	
	
	/**
	 * @description DocDataSet的内部类
	 */
	public class DocData{
		

		private int docid;
		private String filename;
		private String url;
		private String text;
		
		public DocData(int docid,String filename,String url,String text){
			this.docid=docid;
			this.filename=filename;
			this.url=url;
			this.text=text;
			
		}
		public int getDocid() {
			return docid;
		}

		public void setDocid(int docid) {
			this.docid = docid;
		}

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}
}

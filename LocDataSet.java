package com.java.version2;

import java.util.HashSet;
import java.util.Iterator;
/**
 * @date 2016-11-11 15:31:21
 * @description 封装第三张表的数据
 * @author ruin
 *
 */
public class LocDataSet {
	
	HashSet<LocData> datas=new HashSet<LocData>();

	/**
	 * @description 插入一条数据
	 * @param word
	 * @param mark
	 * @param docid
	 * @param locinfo
	 */
	public void InsertData(String word,String mark,int docid,String locinfo){
		datas.add(new LocData(word,mark,docid,locinfo));
	}
	/**
	 * @description 获得遍历器
	 * @return
	 */
	public Iterator<LocData> getIterator(){
		return datas.iterator();
	}
	
	public class LocData{
		private String word=null;
		private String mark=null;
		private int docid;
		private String locinfo=null;
		
		public LocData(String word,String mark,int docid,String locinfo){
			this.word=word;
			this.mark=mark;
			this.docid=docid;
			this.locinfo=locinfo;
		}
		public String getWord() {
			return word;
		}
		public void setWord(String word) {
			this.word = word;
		}
		public int getDocid() {
			return docid;
		}
		public void setDocid(int docid) {
			this.docid = docid;
		}
		public String getLocinfo() {
			return locinfo;
		}
		public void setLocinfo(String locinfo) {
			this.locinfo = locinfo;
		}
		public String getMark() {
			return mark;
		}
		public void setMark(String mark) {
			this.mark = mark;
		}
		
		
	}
}

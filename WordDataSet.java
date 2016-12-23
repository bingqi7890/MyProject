package com.java.version2;

import java.util.HashSet;
import java.util.Iterator;
/**
 * @date 2016-11-11 15:31:21
 * @description 封装第二张表的数据
 * @author ruin
 *
 */
public class WordDataSet {
	
	HashSet<WordData> datas=new HashSet<WordData>();
	
	public void insertData(String word,String mark){
		datas.add(new WordData(word,mark));
	}
	
	public String getSM(String word){
		Iterator<WordData> itor=getIterator();
		WordData temp=null;
		for(;itor.hasNext();){
			temp=itor.next();
			if(word.equals(temp.word))
				return temp.mark;
		}
		return null;
	}
	
	public Iterator<WordData> getIterator(){
		return datas.iterator();
	}

	public class WordData{
		
		String word;
		String mark;
		
		public WordData(String word,String mark){
			this.word=word;
			this.mark=mark;
		}
		public String getWord() {
			return word;
		}
		public void setWord(String word) {
			this.word = word;
		}
		public String getMark() {
			return mark;
		}
		public void setMark(String mark) {
			this.mark = mark;
		}
	}
}

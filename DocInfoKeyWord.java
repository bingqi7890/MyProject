package com.java.version2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.local.bean.KeyLocInfo;

import net.sf.json.JSONObject;

/**
 * 
 * @author ruin
 * @date 2016年11月13日20:55:48
 * @description 封装数据层根据输入关键词获得的信息
 * 
 */
public class DocInfoKeyWord {

	// private HashSet<HashSet<KeyLocInfo>> datas=new
	// HashSet<HashSet<KeyLocInfo>>();
	// private HashMap<String,OneDocInfoKeyWord> datas=new
	// HashMap<String,OneDocInfoKeyWord>();
	private ArrayList<OneDocInfoKeyWord> datas = new ArrayList<OneDocInfoKeyWord>();

	private ArrayList<Integer> docrecords = new ArrayList<Integer>();

	/**
	 * @des 创建一个新的文档的记录
	 * @param docId
	 * @param filename
	 * @param url
	 * @param doctext
	 */
	public void createNewDocRecord(int docId, String filename, String url,
			String doctext) {
		datas.add(new OneDocInfoKeyWord(docId, filename, url, doctext));
		docrecords.add(docId);
	}

	public ArrayList<Integer> getDocrecords() {
		return docrecords;
	}

	/**
	 * @des 插入数据，会根据参数中的docid，将记录自动插入相应的文档的记录中去，插入成功会返回 1，
	 *      插入不成功是由于还没有对应docid文档的记录，返回0，需要先调用createNewDocRecord()方法创建文档记录
	 * 
	 * @param docId
	 * @param keyText
	 * @param parId
	 * @param sentId
	 * @param startPos
	 * @param endPos
	 * @param length
	 * @return
	 */
	public int insertData(int docId, String keyText, long parId, long sentId,
			long startPos, long endPos, long length) {
		if (!docrecords.contains(docId)) {
			return 0;
		}
		for (OneDocInfoKeyWord odik : datas) {
			if (odik.getDocid() == docId) {
				odik.insertData(Integer.toString(docId), keyText, parId,
						sentId, startPos, endPos, length);
				return 1;
			}
		}

		return 0;
	}

	/**
	 * 获得迭代器用于遍历所有文档数据
	 * 
	 * @return
	 */
	public Iterator<OneDocInfoKeyWord> getInterator() {
		return datas.iterator();
	}

	/**
	 * @des 封装一篇文档的数据
	 * @author shrimp
	 * 
	 */
	public class OneDocInfoKeyWord {

		private int docid;
		private String filename;
		private String url;
		private JSONObject doctext;

		private int differentKeyNum = 0;
		private HashSet<String> keywordset = new HashSet<String>();

		private ArrayList<KeyLocInfo> data = new ArrayList<KeyLocInfo>();

		/**
		 * @des 获得文档中的KeyLocInfo集合
		 * @return
		 */
		public ArrayList<KeyLocInfo> getData() {
			return data;
		}

		/**
		 * @des 获得从指定开始句子ID到终止句子ID的段落字符串
		 * @param sentstart
		 * @param sentend
		 * @return
		 */
		public String getsectionStr(long sentstart, long sentend) {
			StringBuilder sb = new StringBuilder();
			for (long i = sentstart; i <= sentend; i++) {
				sb.append(doctext.getJSONObject(String.valueOf(i)).getString(
						"text"));
			}
			return sb.toString();
		}

		/**
		 * @des 获得从指定开始句子ID到终止句子ID跨越的段落数
		 * @param sentstart
		 * @param sentend
		 * @return
		 */
		public int getoversectionNum(long sentstart, long sentend) {
			return (int) (doctext.getJSONObject(String.valueOf(sentend))
					.getLong("paraid")
					- doctext.getJSONObject(String.valueOf(sentstart)).getLong(
							"paraid") + 1);
		}

		public OneDocInfoKeyWord() {
		};

		public OneDocInfoKeyWord(int docid, String filename, String url,
				String doctext) {
			this.docid = docid;
			this.filename = filename;
			this.url = url;
			this.doctext = JSONObject.fromObject(doctext);
		}

		/**
		 * @des 在该文档中插入一个keylocinfo数据，并且自动实现了排序，sentid从小到大
		 * @param docId
		 * @param keyText
		 * @param parId
		 * @param sentId
		 * @param startPos
		 * @param endPos
		 * @param length
		 */
		public void insertData(String docId, String keyText, long parId,
				long sentId, long startPos, long endPos, long length) {
			if (!keywordset.contains(keyText)) {
				keywordset.add(keyText);
				differentKeyNum++;
			}
			KeyLocInfo kli = new KeyLocInfo(docId, keyText, parId, sentId,
					startPos, endPos, length);
			if (data.size() == 0) {
				data.add(kli);
				return;
			}
			for (int i = 0; i < data.size(); i++) {
				if (kli.sentId < data.get(i).sentId) {
					data.add(i, kli);
					return;
				}
			}
			data.add(kli);

		}

		/**
		 * @des 获得包含文档全文信息的JSONBject对象
		 * @return
		 */
		public JSONObject getdoctext() {
			return doctext;
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

		public int getDifferentKeyNum() {
			return differentKeyNum;
		}

		public void setDifferentKeyNum(int differentKeyNum) {
			this.differentKeyNum = differentKeyNum;
		}

		public Iterator<KeyLocInfo> getIterator() {
			return data.iterator();
		}

	}

}

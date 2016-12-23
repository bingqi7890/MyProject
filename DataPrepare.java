package com.java.version2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;


import com.hankcs.hanlp.HanLP;
//import com.java.extract.GetFirstword;
import com.local.bean.KeyLocInfo;
import com.local.bean.SentLocInfo;
import com.java.readfile.GetFileInfo;

import net.sf.json.JSONObject;


public class DataPrepare {
	
	HashMap<String, HashMap<String, Object>> ndocinfo = new HashMap<String, HashMap<String, Object>>();
	//system.out.println("");
	
	//DocDataSet dds = new DocDataSet();
	//WordDataSet wds = new WordDataSet();
	//LocDataSet lds = new LocDataSet();
	int docid = 1;
	//停用词
	public HashSet<String> stopword;
	
	//ArrayList<ArrayList<String>> wordset = new ArrayList<ArrayList<String>>();
	//ArrayList<String> OneWordSetTemp = new ArrayList<String>();
	/**
	 * 分出来的一个词的位置信息*N 
	 * wordlocinfo与words 里面的相一一对应
	 */
	ArrayList<ArrayList<KeyLocInfo>> wordlocinfo=new ArrayList<ArrayList<KeyLocInfo>>();
	//一篇文档中分出词的集合
	ArrayList<String> words=new ArrayList<String>();
	
	

	public String prepareData(String filepath,DocDataSet dds,WordDataSet wds,LocDataSet lds) throws IOException {
         //获得目录下所有文档的信息
		System.out.println("开始读取文档。。。");
		ndocinfo = new GetFileInfo().getAllFilesInfo_2(filepath);
		System.out.println("文档读取完成。数目："+ndocinfo.size());
		// List<com.hankcs.hanlp.dictionary.py.Pinyin> pinyinList =
		// HanLP.convertToPinyinList(thefirstword);
		String url;
		HashMap<String, Object> onedocmap;
		// 一个文档的json表示
		JSONObject js = null;
		JSONObject jstemp = null;
		SentLocInfo Senttemp;
		/**
		 * 对每篇文档进行循环（最外层）
		 */
		int count=1;
		for (Entry<String, HashMap<String, Object>> entry : ndocinfo.entrySet()) {
			url = entry.getKey();
			url=url.replaceAll("\\\\","\\\\\\\\");//防止存储进数据库斜杠消失
			onedocmap = entry.getValue();
			//wordset.add(null);
			System.out.println("正在处理文档.序号"+count++);

			js = new JSONObject();
			jstemp = new JSONObject();
			js.put("sentnum", ((List) onedocmap.get("sentsInfo")).size());
			js.put("paranum", ((List) onedocmap.get("parsInfo")).size());
			/**
			 * 对文档中的每个句子进行循环
			 */
			for (SentLocInfo sli : ((ArrayList<SentLocInfo>)(onedocmap.get("sentsInfo")))) {
				jstemp.put("sentid", sli.sentId);
				jstemp.put("paraid", sli.parId);
				jstemp.put("text", sli.sentText);
				js.put(sli.sentId.toString(), jstemp);
				jstemp = new JSONObject();

				// 开始分词
				String Str = sli.sentText;
				List<Term> termList = ToAnalysis.parse(Str);
				getStopWord();
				/**
				 * 对每个句子进行分词
				 */

				for (Term term : termList) {

					String word = term.getName();
					if (stopword.contains(word)) {
						continue;
					}
					// 去掉分词中的数字
					if (word.getBytes().length == word.length()) {

						continue;
					}
					//OneWordSetTemp.add(word);
					/**
					 * 形成关键词的位置信息
					 */
					if(!words.contains(word)){
						words.add(word);
						wordlocinfo.add(new ArrayList<KeyLocInfo>());
					}
					wordlocinfo.get(words.indexOf(word)).add(new KeyLocInfo(Integer.toString(docid),word,sli.parId,sli.sentId,0l,0l,0l));
					
					String index = "";
					// 获取关键词的第一个汉字
					String thefirstword = TheFirstWord(word);
					//获取声韵母
					List<com.hankcs.hanlp.dictionary.py.Pinyin> pinyinList = HanLP.convertToPinyinList(thefirstword);
					for (com.hankcs.hanlp.dictionary.py.Pinyin pinyin : pinyinList) {
						String SM = (pinyin).getShengmu().toString();
						if (SM.equals("none")) {
							String YM = pinyin.getYunmu().toString();
							index = YM;
						} else {
							index = SM;
						}

					}
					
					wds.insertData(word, index);
					// 第二张表数据完成
				}

				

				//wordset.add(OneWordSetTemp);
				//OneWordSetTemp = new ArrayList<String>();
				//

			}
			dds.insertData(docid++, getDocName(url), url, js.toString());
			
			// 第一张表数据准备完成
			//
			js=new JSONObject();
			//形成第三张表的json
			for (int i=0;i<words.size();i++) {
				js.put("keyword", words.get(i));
				js.put("num", wordlocinfo.get(i).size());
				int j=1;
				for(KeyLocInfo kli:wordlocinfo.get(i)){
					jstemp.put("docid", kli.docId);
					jstemp.put("keytext",kli.keyText);
					jstemp.put("parid",kli.parId);
					jstemp.put("sentid",kli.sentId);
					jstemp.put("startpos",kli.startPos);
					jstemp.put("endpos",kli.endPos);
					jstemp.put("length",kli.length);
					js.put(j++, jstemp);
					jstemp=new JSONObject();
				}
				lds.InsertData(words.get(i), wds.getSM(words.get(i)), docid-1, js.toString());
				js=new JSONObject();
			}
			
			
			//第三张表完成
			
			
			
			//wordset=new ArrayList<ArrayList<String>>();
			wordlocinfo=new ArrayList<ArrayList<KeyLocInfo>>();
			words=new ArrayList<String>();
		}
		return null;

	}

	// 取得文档标题
	public String getDocName(String url) {
		String[] temp = url.split("\\\\");
		if (temp.length >= 1) {
			String[] strtemp = temp[temp.length - 1].split("\\.");

			return strtemp[0].trim();
		}
		return "";
	}
	// 读入分词用到的停用词

	public void getStopWord() throws IOException {
		stopword = new HashSet<String>();
		BufferedReader br = new BufferedReader(new FileReader("D:\\jeeworkspace\\YIDO2.0test\\WebContent\\案件文档\\中文停用词表.txt"));
		String temp = null;
		// HashSet<String> stopWords = new HashSet<String>();
		while ((temp = br.readLine()) != null) {
			stopword.add(temp);
		}
		br.close();
	}

	/**
	 * 
	 * @param str
	 * @return 关键词的第一个字，用于声韵母
	 */
	public String TheFirstWord(String str) {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			if ((str.charAt(i) + "").getBytes().length > 1) {
				sb.append(str.charAt(i));
				// System.out.println(sb);
				// System.out.println(str.length());
				break;
			}
		}
		return sb.toString();
	}
	
}

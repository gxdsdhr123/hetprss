/**
 */
package com.neusoft.framework.common.utils;

public class HighLight  {
	private static String className="highlight";//高亮css class名称
	private static String kwSplit=" ";//关键字分隔符
	
	public static String highlight(String word,String keyWords){
		if(keyWords!=null&&!"".equals(keyWords)){
			String[] kwArray=keyWords.split(kwSplit);
			for(int i=0;i<kwArray.length;i++){
				String kwTemp=kwArray[i];
				if(!ifOtherContaninThis(kwArray,kwTemp,i)){
					word=word.replaceAll(kwTemp, getHtml(kwTemp));
				}
			}
		}
		return word;
	}
	
	private static String getHtml(String word){
		return "<em class='"+className+"'>"+word+"</em>";
	}
	
	private static boolean ifOtherContaninThis(String[] array,String str,int pos){
		boolean res=false;
		if(pos>0){
			for(int i=0;i<pos;i++){
				if(array[i].indexOf(str)>=0){
					res=true;
					break;
				}
			}
		}
		return res;
	}
}

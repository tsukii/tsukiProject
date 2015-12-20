package cn.tsuki.namecraft.jsonTools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * 		JsonType: 功能类型
 * 		ObjectType: Content中的对象类型
 * 		Num: Content中的对象个数
 * 		Content: 字符串
 *
 *JsonType:1
 *ObjectType:
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * */



public class Jsons {
	
	public static String buildJson(int jsontype,String objecttype,int elementnum,String content) throws JSONException{
		JSONObject jo = new JSONObject();
		jo.put("JsonType", jsontype);
		jo.put("ObjectType", objecttype);
		jo.put("Num", elementnum);
		jo.put("Content", content);
		return jo.toString();
	}
	
	public static JSONArray buildJsonArray(List<?> array){
		   JSONArray ja = new JSONArray();
		   for(int i = 0;i < array.size();i++){
			   Map<String,String>map = null;
			   map = JsonTools.toMap(array.get(i));
			   ja.put(map);
		   }
		   return ja;
	}
	
}

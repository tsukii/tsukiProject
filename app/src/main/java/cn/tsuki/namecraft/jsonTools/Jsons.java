package cn.tsuki.namecraft.jsonTools;


import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.tsuki.namecraft.clientJson.Login;

import cn.tsuki.namecraft.serverJson.RGetUserList;

/*
 * 		JsonType: 功能类型
 * 		ObjectType: Content中的对象类型
 * 		Num: Content中的对象个数
 * 		Content: 字符串
 *
 * JsonType:1
 * ObjectType:
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
			JSONObject jo=new JSONObject(map);
			ja.put(jo);
			//ja.put(array.get(i));
			try {
				jo = ja.getJSONObject(ja.length() - 1);
				Log.d("jo",jo.toString());
			}catch (JSONException e){
				Log.d("jo e","exp");
				e.printStackTrace();
			}

		}
		return ja;
	}

	public static void main(String args[]){
		//Hero newhero = new Hero("123","2321","xixi",3);
		Login t1 = new Login(1,"23","33"),t2 = new Login(2,"3","11s"),t3 = new Login(3,"xx","11s");
		List<Login> list = new ArrayList();
		list.add(t1);
		list.add(t2);
		list.add(t3);
		JSONArray ja = buildJsonArray(list);
		try {
			String str = buildJson(1,"2",3,ja.toString());
			System.out.println(ja.toString());
			JSONObject jo = new JSONObject(str);
			str = jo.getString("Content");
			System.out.println(str);
			ja = new JSONArray(str);
			System.out.println(ja.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//	JSONObject jo = JsonTools.toJSON(newhero);
		//	String str = jo.toString();
		//	System.out.println(str);

	}

}

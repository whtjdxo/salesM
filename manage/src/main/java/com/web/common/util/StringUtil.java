package com.web.common.util;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class StringUtil {
	/**
	 * 콤마찍기
	 * @param money
	 * @return
	 */
	public static String getComma(String money)
	{
		money = (money == null || money.equals("")) ? "0" : money;
		money = money.replaceAll("[,]", "");
		DecimalFormat df = new DecimalFormat("###,###");
		
		//System.out.println("]"+df.format(Double.parseDouble(money))+"[");
		
		return df.format(Double.parseDouble(money));
	}
	
	public static String removeComma(String money)
	{
		if(money.indexOf(",") != -1){
			return money.replace(",","");
		}
		return money;		
	}
	
	/**
	 * 널체크
	 * @param param
	 * @return
	 */
	public static String nullCheck(String param)
	{
		if(param == null || "".equals(param)) return "";
		return param;
	}
	public static String nullCheck(String param, String result) {
		if(param == null || "".equals(param)) return result;
		return param;
	}
	
	/**
	 * 네이버 지역코드 연동 
	 * @param areaCd
	 * @return
	 */
	public static String setAreaNm(String areaCd){
		String areaNm="서울시";
		
		if("2600000000".equals(areaCd)) areaNm="부산시";
		else if("2600000000".equals(areaCd)) areaNm="부산시";
		else if("2700000000".equals(areaCd)) areaNm="대구시";
		else if("2800000000".equals(areaCd)) areaNm="인천시";
		else if("2900000000".equals(areaCd)) areaNm="광주시";
		else if("3000000000".equals(areaCd)) areaNm="대전시";
		else if("3100000000".equals(areaCd)) areaNm="울산시";
		else if("4100000000".equals(areaCd)) areaNm="경기도";
		else if("4200000000".equals(areaCd)) areaNm="강원도";
		else if("4300000000".equals(areaCd)) areaNm="충청북도";
		else if("4400000000".equals(areaCd)) areaNm="충청남도";
		else if("4500000000".equals(areaCd)) areaNm="전라북도";
		else if("4600000000".equals(areaCd)) areaNm="전라남도";
		else if("4700000000".equals(areaCd)) areaNm="경상북도";
		else if("4800000000".equals(areaCd)) areaNm="경상남도";
		else if("5000000000".equals(areaCd)) areaNm="제주도";		
		return areaNm;
	}
	
	/**
	 * 전화번호 가공 444
	 * @param hp1
	 * @param hp2
	 * @param hp3
	 * @return
	 */
	public static String setHp(String hp1, String hp2, String hp3) {
		String result1 = setHpMake(hp1);
		String result2 = setHpMake(hp2);
		String result3 = setHpMake(hp3);
		return result1+result2+result3;
	}
	
	public static String setHpMake(String hp){
		int length = hp.length();
		String result ="";
		hp = nullCheck(hp);
		if(length == 4) 	result = hp;
		else if(length == 3)result = hp+" ";
		else if(length == 2)result = hp+"  ";
		else				result = "    ";
				
		return result;
	}
	
	/**
	 * 핸드폰 12 자리 view "-"
	 * 111-1234-5678
	 * @param val
	 * @return
	 */
	public static String getHp(String val){
		String strRslt = nullCheck(val);
		if(strRslt.length() == 12){
			String hp1 = strRslt.substring(0, 4).trim();
			String hp2 = strRslt.substring(4, 8).trim();
			String hp3 = strRslt.substring(8, 12).trim();
			strRslt = hp1+"-"+hp2+"-"+hp3;
		}
		return strRslt;
	}

	/**
	 * 핸드폰 10,11 자리 view "-"
	 * @param money
	 * @return
	 */
	public static String makeHp(String phoneNumber) {

		String regEx = "(^02|\\d{3})(\\d{3,4})(\\d{4})";

	   if(!Pattern.matches(regEx, phoneNumber)) return null;
	   
	   return phoneNumber.replaceAll(regEx, "$1-$2-$3");
   }
	
	/**
	 * 이메일 포맷 체크
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email){
		
		String regEx = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
		
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(email);
		boolean isNormal = m.matches();
		
		return isNormal;
	 }
	
	/**
	 * 파라미터에서 int형 리턴
	 * @param param
	 * @return
	 */
	public static int getIntParam(String param)
	{
		try
		{
			return Integer.parseInt(nullCheck(param));
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	
	/**
	 * 콤마찍기:만원
	 * @param money
	 * @return
	 */
	public static String getCommaAsMoney(String money)
	{
		money = (money == null || money.equals("")) ? "0" : money;
		if(money.length() > 4)
		{
			money = money.substring(0, money.length()-4);
		}
		else
		{
			//money = "0";
		}
		
		money = money.replaceAll("[,]", "");
		DecimalFormat df = new DecimalFormat("###,###");
		
		//System.out.println("]"+df.format(Double.parseDouble(money))+"[");
		
		return df.format(Double.parseDouble(money));
	}
	/**
	 * 콤마찍기:억원
	 * @param money
	 * @return
	 */
	public static String getCommaAsBill(String money)
	{
		money = (money == null || money.equals("")) ? "0" : money;
		
		if(money.length() > 8)
		{
			money = money.substring(0, money.length()-8);
		}
		else
		{
			//money = "0";
		}
		
		money = money.replaceAll("[,]", "");
		DecimalFormat df = new DecimalFormat("###,###");
		
		//System.out.println("]"+df.format(Double.parseDouble(money))+"[");
		
		return df.format(Double.parseDouble(money));
	}
	
	public static boolean isStringNumberChk(String s){
		boolean chk = false;
		try{
			Double.parseDouble(s);
			chk = true;
		}catch (Exception e) {
			chk = false;
		}
			return chk;
	}
	
	/*
	* Object를 json 문자열로 변경한다.
	 * @return
	 */
	public static String ObjectToJsonString(Object object){
		String returnValue = "";
		
		if (object != null){
			ObjectMapper mapper = new ObjectMapper();
			try {
				returnValue = mapper.writeValueAsString(object);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				mapper = null;
			}
		}

		return returnValue;
	}
	 /**
		 * jsonString 을 Object 로 변환한다.
		 * @param jsonString
		 * @return
		 */
		public static Object JsonStringToObject(String jsonString){
			Object returnObject = null;
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
				returnObject = mapper.readValue(jsonString, new TypeReference<Object>(){});
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println(returnObject);
			
			return returnObject;
		}
}

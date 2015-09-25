package com.lodogame.game.utils.mobileMM;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Dom4jUtil {
	
	private static Log log =  LogFactory.getLog(Dom4jUtil.class);
	
	public static Map<String, String> parserXml(String xml) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		if(StringUtils.isNotBlank(xml)){
			try {
				Document document = DocumentHelper.parseText(xml);
				Element employees = document.getRootElement();
				for(Iterator i= employees.elementIterator(); i.hasNext();){
					Element employee = (Element) i.next();
					map.put(employee.getName(), employee.getText());
				}
			} catch (DocumentException e) {
				log.error(e.getMessage());
			}
		}
		return map;
	}
	
	public static void main(String[] args) throws DocumentException {
		String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>	<hRet>0</hRet>	<status>1800</status>	<transIDO>12345678901234567</transIDO>	<versionId>100</versionId>	<userId>12345678</userId>	<cpServiceId>120123002000</cpServiceId>	<consumeCode>120123002001</consumeCode>	<cpParam>0000000000000000</cpParam></request>";
		Document document = DocumentHelper.parseText(response);
		System.out.println(document.getRootElement().getName());
//		Map<String, String> map = Dom4jUtil.parserXml(response);
//		for(Map.Entry<String, String> entry : map.entrySet()){
//			System.out.println(entry.getKey() + ":" + entry.getValue());
//		}
	}

}

package org.jivesoftware.spark.lsq;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class TestrrIQProvider implements IQProvider {
	public IQ parseIQ(XmlPullParser xp) throws Exception {
		TestrrIQ result = new TestrrIQ();
		System.out.println("parseIQ:begin------------------------------");
		System.out.println("gloabl name " + xp.getName());
		System.out.println("gloabl text " + xp.getText());
		while (true) {
			int n = xp.next();
			if (n == XmlPullParser.START_TAG) {
				System.out.println("gloabl name " + xp.getName());
				System.out.println("gloabl text " + xp.getText());
				if ("username".equals(xp.getName())) {
					System.out.println("username " + xp.nextText());// 可以根据业务模型自己开发不同的解析工具类返回相应的实体
				}
				if ("fullname".equals(xp.getName())) {
					System.out.println("fullname " + xp.nextText());// 可以根据业务模型自己开发不同的解析工具类返回相应的实体
				}
			} else if (n == XmlPullParser.END_TAG) {
				if ("query".equals(xp.getName())) {
					break;
				}
			}
		}
		System.out.println("parseIQ:end------------------------------");
		return result;
	}
}
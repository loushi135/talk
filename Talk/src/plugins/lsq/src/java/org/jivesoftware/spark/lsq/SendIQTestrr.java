package org.jivesoftware.spark.lsq;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.spark.SparkManager;

public class SendIQTestrr {
	public static void run() throws Exception {
		ProviderManager.getInstance().addIQProvider(TestrrIQ.ELEMENT,
				TestrrIQ.NAMESPACE, new TestrrIQProvider());
		XMPPConnection con = SparkManager.getConnection();
		PacketCollector collector = con
				.createPacketCollector(new PacketFilter() {
					public boolean accept(Packet p) {
						System.out.println("accept packet xml = " + p.toXML());
						if (p instanceof TestrrIQ) {
							TestrrIQ m = (TestrrIQ) p;
							System.out.println(m.toXML());
							return true;
						}
						return false;
					}
				});
		TestrrIQ disco = new TestrrIQ();
		//send request begin
		disco.setType(IQ.Type.GET);
		disco.setNode(null);
		con.sendPacket(disco);
		
		Packet packet = collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		collector.cancel();
		if (packet == null) {
		} else {
			System.out.println("final packet xml = " + packet.toXML());
		}
	}
}
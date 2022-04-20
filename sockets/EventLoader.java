package sockets;

import sockets.classes.Client;
import sockets.classes.EventHook;
import sockets.classes.Packet;
import sockets.classes.Packet.PacketBuilder;
import sockets.classes.Server;

public class EventLoader {
	public static void load(Server s, Client c) {
		//server echos message to client
		EventHook hook = new EventHook("echo") {
			@Override
			public void execute(Packet packet) {
				System.out.println(packet.getString().toUpperCase());
			}
		};
		s.getServerPacketEventManager().addEventHook(hook);
		//server takes a number and prints it
		hook = new EventHook("message") {
			@Override
			public void execute(Packet packet) {
				System.out.println("This is a test message, you sent the number "+packet.getInt());
			}
			
		};
		s.getServerPacketEventManager().addEventHook(hook);
		//server part of packet chain
		hook = new EventHook("client") {
			@Override
			public void execute(Packet packet) {
				Packet send = new PacketBuilder("server")
						.String("This is a client test")
						.Build();
				s.sendPacket(send);
			}
		};
		s.getServerPacketEventManager().addEventHook(hook);
		//client part of packet chain
		hook = new EventHook("server") {
			@Override
			public void execute(Packet packet) {
				//final part of chain, triggers message packet execution with 27 as the number
				Packet send = new PacketBuilder("message")
						.Int(27)
						.Build();
				c.sendReceivePackets(send);
			}
		};
		c.getPacketEventManager().addEventHook(hook);
	}
}

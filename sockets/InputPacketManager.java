package sockets;

import sockets.classes.Client;
import sockets.classes.Packet;
import sockets.classes.Packet.PacketBuilder;
import sockets.classes.Server;

public class InputPacketManager {
	public static void parseString(Client client, Server server, String input) {
		if (input.contains("message")) {
			//send server with single digit integer
			Packet packet = new PacketBuilder("message")
					.Int(Integer.parseInt(input.substring(input.length()-1)))
					.Build();
			client.sendReceivePacket(packet);
			
		} else if (input.contains("ping")){
			//multi-packet communication (view EventLoader)
			Packet packet = new PacketBuilder("client")
					.Build();
			client.sendReceivePacket(packet);
			
		}
	}
}

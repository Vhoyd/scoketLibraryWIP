package sockets.classes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client {
	private String address;
	private int port;
	private PacketEventManager packetEventManager = new PacketEventManager();
	
	public Client(String address, int port) throws Exception {
		this.address = address;
		this.port = port;
	}
	
	public void sendReceivePackets(Packet... packets) {
		try {
			Socket clientSocket = new Socket(address, port); 
			DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
			DataInputStream dis = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
			String packetString = "";
			for (Packet packet : packets) packetString += packet.toString();
			dos.writeUTF(packetString.toString());
			dos.flush();
			String packetBack = dis.readUTF();
			dos.close();
			dis.close();
			clientSocket.close();
			Packet[] packetsBack = Packet.getPacketsFromString(packetBack);
			packetEventManager.executePackets(packetsBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendReceivePacket(Packet packet) {
		try {
			Socket clientSocket = new Socket(address, port); 
			DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
			DataInputStream dis = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
			String packetString = packet.toString();
			dos.writeUTF(packetString.toString());
			dos.flush();
			String packetBack = dis.readUTF();
			dos.close();
			dis.close();
			clientSocket.close();
			Packet[] packets = Packet.getPacketsFromString(packetBack);
			packetEventManager.executePackets(packets);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public PacketEventManager getPacketEventManager() {
		return packetEventManager;
	}
	
	@Override
	public String toString() {
		return "Client connected at "+address+" on "+port;
	}
}

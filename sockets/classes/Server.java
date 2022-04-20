package sockets.classes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private Socket socket;
	private ServerSocket server;
	private DataInputStream dis;
	private DataOutputStream dos;
	private Thread serverRun; 
	private boolean run = true;
	private String totalPacket;
	private PacketEventManager packetEventManager= new PacketEventManager();
	
	public Server(int port) throws Exception{
		server = new ServerSocket(port);
		System.out.println("Running server on port " + port);
		
	}
	
	public void start() {
		serverRun = new Thread() {
			public void run() {
				while (run) {
					recievePacket();					
				}
			}
		};
		serverRun.start();
	}
	
	public void recievePacket() {
		try {
			totalPacket = "";
			socket = server.accept();
			dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			String packet = dis.readUTF();
			packetEventManager.executePackets(packet);
			dos.writeUTF(totalPacket);
			dos.flush();
			dos.close();
			dis.close();
			socket.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		run = false;
		serverRun.interrupt();
	}
	
	public PacketEventManager getServerPacketEventManager() {
		return packetEventManager;
	}
	
	public void sendPacket(Packet packet) {
		totalPacket += packet.toString();
	}
}

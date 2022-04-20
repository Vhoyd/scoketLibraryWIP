package sockets;

import java.util.Scanner;

import sockets.classes.Client;
import sockets.classes.Server;

public class Main {
	public static void main(String args[]) {
		try {
			//create server and client object
			Server server = new Server(1235);
			Client client = new Client("0.0.0.0", 1235);
			EventLoader.load(server, client);
			server.start();
			String in;
			Scanner input = new Scanner(System.in);
			do {
				System.out.println("Input a recognized packet name, or \"help\" for a list of recognized packets:");
				in = input.nextLine();
				if (!in.equals("quit")) {
					InputPacketManager.parseString(client, server, in);
				} else {
					input.close();
				}
			} while (!in.equals("quit"));
			server.stop();
			System.out.println("stopped");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

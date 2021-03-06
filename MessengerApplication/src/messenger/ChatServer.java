package messenger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

public class ChatServer {

	static Vector ClientSockets;
	static Vector LoginNames;

	public static void main(String[] args) throws IOException {
		ChatServer server = new ChatServer();
	}

	public ChatServer() throws IOException {
		ServerSocket serverSocket = new ServerSocket(5217);
		ClientSockets = new Vector();
		LoginNames = new Vector();

		while (true) {
			Socket client = serverSocket.accept();
			AcceptClient acceptclient = new AcceptClient(client);
		}
	}

	public class AcceptClient extends Thread {
		Socket ClientSocket;
		DataInputStream din;
		DataOutputStream dout;

		public AcceptClient(Socket client) throws IOException {
			super();
			ClientSocket = client;
			din = new DataInputStream(ClientSocket.getInputStream());
			dout = new DataOutputStream(ClientSocket.getOutputStream());

			String loginName = din.readUTF();
			LoginNames.add(loginName);
			ClientSockets.add(ClientSocket);
			start();

		}

		public void run() {
			while (true) {
				try {
					String msgFromClient = din.readUTF();
					StringTokenizer st = new StringTokenizer(msgFromClient);
					String loginName = st.nextToken();
					String msgType = st.nextToken();
					int lo = -1;
					
					String msg = " ";
					
					while (st.hasMoreTokens()) {
						msg = msg + " : "+st.nextToken(); 
					}

					if (msgType.equals("LOGIN")) {
						for (int i = 0; i < LoginNames.size(); i++) {
							Socket pSocket = (Socket) ClientSockets
									.elementAt(i);
							DataOutputStream pOut = new DataOutputStream(
									pSocket.getOutputStream());
							pOut.writeUTF(loginName + " has logged in ");

						}
						
					}else if(msgType.equals("LOGOUT")){
						for (int i = 0; i < LoginNames.size(); i++) {
							if(loginName == LoginNames.elementAt(i))
								lo=i;
							Socket pSocket = (Socket) ClientSockets.elementAt(i);
							DataOutputStream pOut = new DataOutputStream(
									pSocket.getOutputStream());
							pOut.writeUTF(loginName + " has logged out ");

						}
						if(lo>=0){
							LoginNames.removeElementAt(lo);
							ClientSockets.removeElementAt(lo);
						}
					}else{
						
						for (int i = 0; i < LoginNames.size(); i++) {
							Socket pSocket = (Socket) ClientSockets.elementAt(i);
							DataOutputStream pOut = new DataOutputStream(
									pSocket.getOutputStream());
							pOut.writeUTF(loginName + " : "+msg);

						}
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}
}

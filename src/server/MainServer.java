package server;

public class MainServer {

	public static void main(String[] args) {
		try {
			Server frame = new Server();
			frame.setVisible(true);
			frame.tSend.start();
			frame.tTake.start();
		} catch (Exception e) {
			e.printStackTrace();
		
		}
	}
}

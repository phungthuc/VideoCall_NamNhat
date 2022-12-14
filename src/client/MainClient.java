package client;

public class MainClient {

	public static void main(String[] args) {
		try {
			Client frame = new Client();
			frame.setVisible(true);
			frame.tSend.start();
			frame.tTake.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

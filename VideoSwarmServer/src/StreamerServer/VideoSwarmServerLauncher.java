package StreamerServer;
import javax.swing.JFrame;


public class VideoSwarmServerLauncher{

	public static void main(String[] args){
		VideoSwarmServer server = new VideoSwarmServer();
		server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		server.startRunning();
	}
}

package StreamerClient;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;


public class VideoSwarmClientLauncher {
	
	public static void main(String[] args){
		String ip = "127.0.0.1";
		try {
			BufferedReader breader = new BufferedReader(new FileReader("info.txt"));
			ip = breader.readLine();
			breader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe){
			ioe.printStackTrace();
		}
		VideoSwarmClient client = new VideoSwarmClient(ip);
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.startRunning();
	}
}

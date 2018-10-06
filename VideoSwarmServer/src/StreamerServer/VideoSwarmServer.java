package StreamerServer;
/**
 * The server for the movie streamer program. Gets the movies from the 
 * library and streams them to a client.
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Objects.Movie;
import Objects.Season;
import Objects.Show;


public class VideoSwarmServer extends JFrame implements ActionListener{
	
	private static int MaxClients = 3; // The maximum number of clients
	
	private JPanel panel = new JPanel(new BorderLayout());
	private JTextArea serverstate;
	private JLabel clientnum;
	private ArrayList<JLabel> clientstate = new ArrayList<JLabel>();
	private boolean running = true;
	private ServerSocket server;
	private ArrayList<ObjectInputStream> input = new ArrayList<ObjectInputStream>();
	private ArrayList<ObjectOutputStream> output = new ArrayList<ObjectOutputStream>(); 
	private ArrayList<Socket> connection = new ArrayList<Socket>();
	private ArrayList<interactionWithClient> clients = new ArrayList<interactionWithClient>();
	private ArrayList<Movie> movies = new ArrayList<Movie>(); // The movies objects
	private ArrayList<Show> shows = new ArrayList<Show>(); // The shows objects
	private int clientno = 0; // The number of clients on the server
	private int clientsjoined = 0; // The number of clients joined during this server's lifespan
	
	public VideoSwarmServer(){
		super("VideoSwarm Server");
		setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
		
		serverstate = new JTextArea();
		serverstate.setEditable(false);
		serverstate.setLineWrap(true);
		add(new JScrollPane(serverstate));
		
		clientnum = new JLabel("Number of Clients/Max: " + clientno + "/" + MaxClients);
		clientnum.setSize(getWidth()/6, getHeight()/5);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(clientnum);
		add(new JScrollPane(panel));
		
		setSize(500,500);
		setVisible(true);
	}

	
	public void startRunning(){
		serverstate.append("Server Started\n");
		try{
			
			serverstate.append("Loading Movies\n");
			loadMovies();
			serverstate.append("Movies Loaded\n");

			serverstate.append("Loading Shows\n");
			loadShows();
			serverstate.append("Shows Loaded");
			
			server = new ServerSocket(6889, 10);
			
			while(running){
				if(clientno < MaxClients){
					waitForConnection();
					setUpClientThread();
					clientno++;
					clientsjoined++;
					clientnum.setText("Number of Clients/Max: " + clientno + "/" + MaxClients);
				}
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	
	// Loads the movies from the file system
	private void loadMovies() throws IOException{
		int nummovies = 0;
		
		String path = "data/Movies/info.txt";
		BufferedReader breader = new BufferedReader(new FileReader(path));
		nummovies = Integer.parseInt(breader.readLine());
		String url = breader.readLine();
		
		for(int i = 0; i < nummovies; i++){
			path = "data/Movies/movie";
			breader = new BufferedReader(new FileReader(path + (i+1) + "/info.txt"));
			movies.add(new Movie(breader.readLine()));
			movies.get(i).setLength(Integer.parseInt(breader.readLine()));
			movies.get(i).setPlotDescription(breader.readLine());
			movies.get(i).setTrailerLength(Double.parseDouble(breader.readLine()));
			path = "Movies/movie";
			movies.get(i).setGraphicUrl((url + "/" + path + (i + 1) + "/graphic.jpg"));
			
			movies.get(i).setMovieUrl(url + "/Movies/movie" + (i + 1) + "/movie.mp4");
			movies.get(i).setTrailerUrl(url + "/Movies/movie" + (i + 1) + "/trailer.mp4");
			
			serverstate.append("Movie" + (i + 1)+ "(" + movies.get(i).getName() + ") " + " Loaded\n");
		}
		
		breader.close();
	}
	
	
	// Loads the shows from the file system
	private void loadShows() throws IOException{
		int numshows = 0;
		
		String path = "data/Shows/info.txt";
		BufferedReader breader = new BufferedReader(new FileReader(path));
		numshows = Integer.parseInt(breader.readLine());
		String url = breader.readLine();
		
		for(int i = 0; i < numshows; i++){ // The Shows
			path = "data/Shows/show";
			breader = new BufferedReader(new FileReader(path + (i+1) + "/info.txt"));
			shows.add(new Show(breader.readLine()));
			path = "Shows/show";
			shows.get(i).setNumberOfSeasons(Integer.parseInt(breader.readLine()));
			shows.get(i).setGraphicUrl(url + "/" + path + (i + 1) + "/graphic.jpg");
				for(int x = 0; x < shows.get(i).getNumberOfSeasons(); x++){ // The Seasons
					path = "data/Shows/show" + (i + 1) + "/season";
					breader = new BufferedReader(new FileReader(path + (x+1) + "/info.txt"));
					Season season = new Season(Integer.parseInt(breader.readLine()));
					season.setSeasonNumber(Integer.parseInt(breader.readLine()));
					int noepisodes = season.getNumberOfEpisodes();
						for(int j = 0; j < noepisodes; j++){ // The Episodes
							String epname = breader.readLine();
							String epdescrip = breader.readLine();
							double eplength = Double.parseDouble(breader.readLine());
							
							season.addEpisode(j + 1, epname, eplength);
							season.setEpisodeDescription(epname, epdescrip);
							season.setEpisodeUrl(epname, url + "/Shows/show" + (i + 1) + "/season" + (x + 1) + "/episode" + (j + 1) + ".mp4");
						}
					shows.get(i).addSeason(season);
				}
			serverstate.append("Show" + (i + 1)+ "(" + shows.get(i).getName() + ") " + " Loaded\n");
		}
		
		breader.close(); 
	}
	
	
	// Waits for a connection and starts it
	private void waitForConnection() throws IOException{
		connection.add(server.accept());
		serverstate.append("Client " + (clientsjoined + 1) + ": Connected\n");
	}
	

	// Sets up and starts the client thread
	private void setUpClientThread(){
		// Sets up the client label
		clientstate.add(new JLabel("Client No." + (clientsjoined + 1) + " State: Connecting"));
		clientstate.get(clientsjoined).setSize(getWidth()/6, getHeight()/5);
		panel.add(clientstate.get(clientsjoined));
		
		interactionWithClient client = new interactionWithClient(clientsjoined);
		clients.add(client);
		new Thread(client).start();	
		serverstate.append("Client " + (clientsjoined + 1) + " Thread Started\n");
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
	
	// Stops the server from accepting connections
	public void stopServer(){
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**  The runnable class for the client, needed for the thread
	 * @author gabemac
	 */
	
	public class interactionWithClient implements Runnable{
		int clientnumber = 0;
		
		public interactionWithClient(int clientno){
			clientnumber = clientno;
		}
		
		public void run(){
			try{
				
				try{
					setupStreams();
					sendLibrary();
					whileInteracting();
					
				}catch(EOFException eof){
					closeStreams();
					clientno--;
					clientnum.setText("Number of Clients/Max: " + clientno + "/" + MaxClients);
					serverstate.append("Client " + (clientnumber + 1) + ": Disconnected\n");
					panel.remove(clientstate.get(clientnumber));
					panel.updateUI();
				}finally{
					closeStreams();
				
				}
				
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
			
			return;
		}
		
		
		// Sends the movie object library to the client
		private void sendLibrary() throws IOException{
			serverstate.append("Movie Library Being Sent to Client No" + (clientnumber + 1) + "\n");
			output.get(clientnumber).writeObject(movies);
			output.get(clientnumber).flush();
			serverstate.append("Movie Library Sent to Client" + (clientnumber + 1) + "\n");
			
			serverstate.append("Show Library Being Sent to Client" + (clientnumber + 1) + "\n");
			output.get(clientnumber).writeObject(shows);
			output.get(clientnumber).flush();
			serverstate.append("Show Library Sent to Client" + (clientnumber + 1) + "\n");

		}
		
		
		// Sets up the input stream to the client
		private void setupStreams() throws IOException{
			input.add(new ObjectInputStream(connection.get(clientnumber).getInputStream()));
			output.add(new ObjectOutputStream(connection.get(clientnumber).getOutputStream()));
			output.get(clientnumber).flush();
			clientstate.get(clientnumber).setText("Client No." + (clientnumber + 1) + " State: Connected");
		}
		
		
		// Interacts with the client and get its state
		private void whileInteracting() throws IOException{
			while(true){
				try{
					String movie = (String) input.get(clientnumber).readObject();
					clientstate.get(clientnumber).setText("Client No." + (clientnumber + 1) + " Streaming: " + movie);
				}catch(ClassNotFoundException cnf){
					clientstate.get(clientnumber).setText("Client No." + (clientnumber + 1) + " State: Message Not Understood");
				}
			}
		}
		
		
		
		// Moves the clientnumber up 1 when someone disconnects
		public void adjustPosition(int disconnectedclient){
			if(clientnumber > disconnectedclient){
				clientnumber--;
			}
		}
		
		
		// Closes the clients streams
		private void closeStreams(){
			try{
				input.get(clientnumber).close();
				output.get(clientnumber).close();
				connection.get(clientnumber).close();
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
		
	}
	
}

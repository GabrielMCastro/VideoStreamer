/**
 * This is the code for the client program
 */

package StreamerClient;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import Objects.Movie;
import Objects.MovieOptionsFrame;
import Objects.Show;
import Objects.ShowOptionsFrame;
import Objects.VideoPlayer;
import Objects.VideoPlayerButtonsPanel;


public class VideoSwarmClient extends JFrame {

	private JLabel clientconnected; // Connected to server or not
	private JLabel serverstreaming; // If the server is streaming to the client
	private JPanel lpanel; // Holds labels
	private JScrollPane lscrollpane; // Holds lpanel
	private JPanel moviepanel; // Holds the buttons that hold the movie graphics
	private JScrollPane moviescrollpane; // Holds moviepanel
	private boolean connectionavailable = false;
	private String serverIP = ""; // The server's IP address
	private Socket connection; // The connection to the server
	private ObjectOutputStream output; // The output stream
	private ObjectInputStream input; // The input stream
	private ArrayList<Movie> movies; // The movies in the server's library
	private ArrayList<JButton> moviebuttons = new ArrayList<JButton>(); // The movie graphic is displayed on it
	private MovieOptionsFrame movieoptionframe; // The frame that holds the movie option
	private boolean movieoptionsshown = false; // Whether the movie option frame is on screen
	private VideoSwarmClient myself = this; // So I can pass myself onto a movie options frame
	private VideoPlayer vplayer = new VideoPlayer(this); // The Video Player
	private JTextField searchfield; // Used to search the library
	private JButton switchlib; // Switches the library from movies to show and vice versa
	private ArrayList<Show> shows; // The shows in the server's library
	private ArrayList<JButton> showbuttons = new ArrayList<JButton>(); // The show graphic is displayed on it
	private boolean showingmovies = true; // Whether showing the movie library or not
	private boolean mbuttonscreated = false; // If the movie buttons have been created
	private boolean sbuttonscreated = false; // If the show buttons have been created
	private ShowOptionsFrame showoptionsframe; // The frame that holds the show options
	private JTextArea logarea = new JTextArea();
	
	
	public VideoSwarmClient(String hostip){
		super("VideoSwarm");
		
		serverIP = hostip;
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		lpanel = new JPanel(new BorderLayout());
		lpanel.setBorder((BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.darkGray)));
		
		clientconnected = new JLabel("Client not Connected to Server");
		clientconnected.setSize(getWidth() / 6, getHeight() / 3);
		lpanel.add(clientconnected, BorderLayout.NORTH);
		
		serverstreaming = new JLabel("Server Streaming: False");
		serverstreaming.setSize(getWidth() / 6, getHeight() / 3);
		lpanel.add(serverstreaming, BorderLayout.WEST);
		
		
		searchfield = new JTextField();
		searchfield.setText("Search");
		searchfield.setPreferredSize(new Dimension(150, 30));
		searchfield.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(showingmovies){
					searchForMovie(searchfield.getText());
				}else{
					searchForShow(searchfield.getText());
				}
			}
			
		});
		lpanel.add(searchfield, BorderLayout.EAST);
		
		
		switchlib = new JButton("Shows");
		switchlib.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.darkGray));
		
		switchlib.setPreferredSize(new Dimension(100, 30));
		switchlib.setMaximumSize(new Dimension(100, 30));
		switchlib.setSize(new Dimension(100, 30));

		switchlib.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(showingmovies){
					if(movieoptionsshown){
						movieoptionframe.dispatchEvent(new WindowEvent(movieoptionframe, WindowEvent.WINDOW_CLOSING));
						movieoptionsshown = false;
					}
					for(int i = 0; i < movies.size(); i++){
						moviepanel.remove(moviebuttons.get(i));
					}
					switchlib.setText("Movies");
					showShows();
					showingmovies = false;
				}else{
					if(movieoptionsshown){
						showoptionsframe.dispatchEvent(new WindowEvent(showoptionsframe, WindowEvent.WINDOW_CLOSING));
						movieoptionsshown = false;
					}
					for(int i = 0; i < shows.size(); i++){
						moviepanel.remove(showbuttons.get(i));
					}
					switchlib.setText("Shows");
					showMovies();
					showingmovies = true;
				}
			}
		});
		Box bbox = Box.createHorizontalBox();
		bbox.add(Box.createGlue());
		bbox.add(switchlib);
		bbox.add(Box.createGlue());
		lpanel.add(bbox);		
		
		lscrollpane = new JScrollPane(lpanel);
		lscrollpane.setMaximumSize(new Dimension(getWidth(), 300));
		getContentPane().add(lscrollpane);
		
		FlowLayout mbflow = new FlowLayout();
		mbflow.setHgap(10);
		
		moviepanel = new JPanel(mbflow);
		moviepanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.darkGray));
		moviescrollpane = new JScrollPane(moviepanel);
		moviescrollpane.getHorizontalScrollBar().setUnitIncrement(10);
		getContentPane().add(moviescrollpane);
				
		setSize(400, 200);
		setVisible(true);
		
		logarea.setEditable(false);
		moviepanel.add(logarea);
		
		this.addComponentListener(new ComponentListener(){
			@Override
			public void componentResized(ComponentEvent e) {
				screenResized();
			}

			@Override
			public void componentMoved(ComponentEvent e) {	
			}

			@Override
			public void componentShown(ComponentEvent e) {	
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
	}

	
	// Runs the client
	public void startRunning(){
		try{
			connectToServer();
			if(connectionavailable == true){
				setUpStreams();
				try {
					
					movies = (ArrayList<Movie>)input.readObject();	
					for(int i = 0; i < movies.size(); i++){
						movies.get(i).setGraphic(movies.get(i).getGraphicUrl());
						logarea.append("Movie " + (i + 1) + "(" + movies.get(i).getName() + ") Loaded\n");
					}
					logarea.append("Movies Loaded\n");
					shows = (ArrayList<Show>) input.readObject();
					for(int i = 0; i < shows.size(); i++){
						shows.get(i).setGraphic(shows.get(i).getGraphicUrl());
						logarea.append("Show " + (i + 1) + "(" + movies.get(i).getName() + ") Loaded\n");
					}
					logarea.append("Shows Loaded\n");
					moviepanel.remove(logarea);
					showMovies();
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}catch(EOFException eof){
			clientconnected.setText("Server(" + serverIP + ") Terminated the Connection");
			closeStreams();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	
	// Connects to the server
	private void connectToServer() throws IOException{
		try{
			connection = new Socket(InetAddress.getByName(serverIP), 6889);
			connectionavailable = true;
			clientconnected.setText("Connecting to Server(" + serverIP + ")...");
		}catch(IOException ioe){
			clientconnected.setText("Connection to Server(" + serverIP + ") Failed");
			ioe.printStackTrace();
		}
	}
	
	
	// Sets up the output stream to the server
	private void setUpStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		clientconnected.setText("Connected to Server(" + serverIP + ")");
	}
	
	
	// Sets up the movie buttons and shows them on the screen
	private void showMovies(){
		if(mbuttonscreated == false){
			for(int i = 0; i < movies.size(); i++){
				int x = i;
				JButton button = new JButton();
				int width = moviescrollpane.getWidth() / 3;
				int height = (width * movies.get(i).getGraphic().getHeight(null)) / movies.get(i).getGraphic().getWidth(null);
				button.setPreferredSize(new Dimension(width, height));
				button.setIcon(movies.get(i).getGraphicIcon());
				button.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 6), movies.get(i).getName()));
				button.setBorderPainted(true);
				button.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(movieoptionsshown){
							movieoptionframe.dispatchEvent(new WindowEvent(movieoptionframe, WindowEvent.WINDOW_CLOSING));
							movieoptionsshown = false;
						}
						movieoptionframe = new MovieOptionsFrame(movies.get(x).getGraphic()
																,movies.get(x).getName()
																,movies.get(x).getLength(), movies.get(x).getTrailerLength()
																,movies.get(x).getPlotDescription()
																,movies.get(x).getMovieUrl()
																,movies.get(x).getTrailerUrl(), moviescrollpane, myself);
						movieoptionsshown = true;
					}
				});
				moviebuttons.add(button);
			}
			mbuttonscreated = true;
		}
		for(int i = 0; i < moviebuttons.size(); i++){
			moviepanel.add(moviebuttons.get(i));

		}
		moviepanel.repaint();
		int width = getWidth();
		int height = getHeight();
		setSize(5,5);
		setSize(width, height);
	}
	
	
	// Searches for movies containing the given keyword
	private void searchForMovie(String keyword){
		for(int i = 0; i < movies.size(); i++){
			moviepanel.remove(moviebuttons.get(i));
		}
		
		for(int i = 0; i < movies.size(); i++){
			String name = movies.get(i).getName().toLowerCase();
			if(keyword.equals("")){
				moviepanel.add(moviebuttons.get(i));
			}else if(name.contains(keyword.toLowerCase())){
				moviepanel.add(moviebuttons.get(i));
			}
		}
		moviepanel.repaint();
		int width = getWidth();
		int height = getHeight();
		setSize(5,5);
		setSize(width, height);
		
	}
	
	
	// Sets up the show buttons and shows them on screen
	private void showShows(){
		if(sbuttonscreated == false){
			for(int i = 0; i < shows.size(); i++){
				int x = i;
				JButton button = new JButton();
				int width = moviescrollpane.getWidth() / 3;
				int height = (width * shows.get(i).getGraphic().getHeight(null)) / shows.get(i).getGraphic().getWidth(null);
				button.setPreferredSize(new Dimension(width, height));
				button.setIcon(shows.get(i).getGraphicIcon());
				button.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 6), shows.get(i).getName()));
				button.setBorderPainted(true);
				button.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(movieoptionsshown){
							showoptionsframe.dispatchEvent(new WindowEvent(showoptionsframe, WindowEvent.WINDOW_CLOSING));
							movieoptionsshown = false;
						}
						showoptionsframe = new ShowOptionsFrame(shows.get(x).getGraphic()
																,shows.get(x).getName()
																,shows.get(x).getSeasons()
																,shows.get(x).getNumberOfSeasons()
																,moviescrollpane
																,myself);
						movieoptionsshown = true;
					}
				});
				showbuttons.add(button);
			}
			sbuttonscreated = true;
		}
		for(int i = 0; i < showbuttons.size(); i++){
			moviepanel.add(showbuttons.get(i));
		}
		moviepanel.repaint();
		int width = getWidth();
		int height = getHeight();
		setSize(5,5);
		setSize(width, height);
	}
	
	
	// Searches for movies containing the given keyword
	private void searchForShow(String keyword){
		for(int i = 0; i < shows.size(); i++){
			moviepanel.remove(showbuttons.get(i));
		}
		
		for(int i = 0; i < shows.size(); i++){
			String name = shows.get(i).getName().toLowerCase();
			if(keyword.equals("")){
				moviepanel.add(showbuttons.get(i));
			}else if(name.contains(keyword.toLowerCase())){
				moviepanel.add(showbuttons.get(i));
			}
		}
		moviepanel.repaint();
		int width = getWidth();
		int height = getHeight();
		setSize(5,5);
		setSize(width, height);
		
	}
		
		
	
	// Closes the streams
	public void closeStreams(){
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	
	// Streams the movie
	public void streamMovie(String url, String movie, int length){
			try {
				output.writeObject(movie);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			vplayer.playVideo(url, length, 0);
			
			serverstreaming.setText("Streaming: " + movie);
	}
	
	
	// Streams the movie trailer
	public void streamTrailer(String url, String movie, double length){
			try {
				output.writeObject(movie);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}	

			vplayer.playVideo(url, 0, length);
			
			serverstreaming.setText("Streaming: " + movie + " (Trailer)");
	}
	
	// Streams the episode
	public void streamEpisode(String url, String episodename, double length){
		try {
			output.writeObject(episodename);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		vplayer.playVideo(url, 0, length);
	
		serverstreaming.setText("Streaming: " + episodename);
	}
	
	
	
	/* VideoPlayerButtonsFrame methods
	 * Take care of the videoplayerbuttonsframe
	 */
	public void disposeVideoPlayerButtonsFrame(){
	//	vpbframe.dispose();
	}
	
	public void bringVideoPlayerButtonsFrameToFront(){
	//	vpbframe.toFront();
	}
	
	public void putVideoPlayerButtonsFrameToBack(){
	//	vpbframe.toBack();
	}	
	

	/* VideoPlayer methods
	 * Takes care of the videoplayer
	 */
	public void pauseVideo(){
		vplayer.pause();
	}
	
	public void playVideo(){
		vplayer.play();
	}
	
	// Skip to a certain position in seconds
	public void seekTo(int pos){
		vplayer.seekTo(pos);
	}
	
	public void exitVideoPlayer(){
		vplayer.exit();
	}
	
	
	// Called when the screen resizes
	private void screenResized(){
		int width = 0;
		int height = 0;
		lscrollpane.setMaximumSize(new Dimension(getWidth(), 300));
		if(showingmovies){
			for(int i = 0; i < moviebuttons.size(); i++){
				height = moviescrollpane.getHeight() - (moviescrollpane.getHeight() / 20);
				width = (height * movies.get(i).getGraphic().getWidth(null)) / movies.get(i).getGraphic().getHeight(null);
				moviebuttons.get(i).setPreferredSize(new Dimension(width,height));
				Image img = (Image) movies.get(i).getGraphic();
				ImageIcon ico =  new ImageIcon(img.getScaledInstance(width, height, java.awt.Image.SCALE_FAST));
				moviebuttons.get(i).setIcon(ico);
			}
		}else{
			for(int i = 0; i < showbuttons.size(); i++){
				height = moviescrollpane.getHeight() - (moviescrollpane.getHeight() / 20);
				width = (height * shows.get(i).getGraphic().getWidth(null)) / shows.get(i).getGraphic().getHeight(null);
				showbuttons.get(i).setPreferredSize(new Dimension(width,height));
				Image img = (Image) shows.get(i).getGraphic();
				ImageIcon ico =  new ImageIcon(img.getScaledInstance(width, height, java.awt.Image.SCALE_FAST));
				showbuttons.get(i).setIcon(ico);
			}
		}
	}
	
	
}
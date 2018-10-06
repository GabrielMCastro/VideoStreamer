package Objects;

import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import javax.swing.JFrame;

import StreamerClient.VideoSwarmClient;

public class VideoPlayer extends JFrame implements WindowListener, MouseListener{
	
	JFXPanel jfx = new JFXPanel();
	MediaPlayer mp;
	VideoSwarmClient parent = null;
	boolean vpbframeshown = true;
	boolean fullscreen = false;
	boolean jfxnotscened = true;
	BufferedImage cursorimg;
	Cursor blankcursor;
	GraphicsDevice vc;
	DisplayMode dm;
	VideoPlayerButtonsPanel vpbframe;


	
	public VideoPlayer(VideoSwarmClient parent){
		super("VideoSwarm Player");
		this.parent = parent;
		this.getContentPane().setLayout(null);
		setUndecorated(true);
		setVisible(false);
		setSize(800,400);
		
		// gets specs of screen
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc = env.getDefaultScreenDevice(); // gets specs and assigns it to graphics card variable
		dm = vc.getDisplayMode();
		
		vpbframe = new VideoPlayerButtonsPanel(parent, 60);
		vpbframe.setVisible(true);
		
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		addWindowListener(this);
		getContentPane().add(vpbframe);
		jfx.setLocation(0, 0);
		getContentPane().add(jfx);
		jfx.addMouseListener(this);
		cursorimg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		blankcursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorimg, new Point(0, 0), "blank cursor");
	}	
	
	
	public void playVideo(String url, int movielength, double movielngth){
		setVisible(true);
		
		if(movielength != 0){
			vpbframe.setMovieLength(movielength);
		}
		if(movielngth != 0){
			vpbframe.setMovieLength(movielngth);
		}
		vpbframe.setPlaybackTime(0);
		
		Platform.runLater(new Runnable(){
			public void run(){
			File f = null;		
			try {
				f = new File(new URL(url).getFile());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			
			Media m = new Media(f.toURI().toString());
	
			mp = new MediaPlayer(m);			
		    MediaView mv = new MediaView(mp);
		    mv.setPreserveRatio(true);
		    
		    
		    
		    DoubleProperty width = mv.fitWidthProperty();
		    DoubleProperty height = mv.fitHeightProperty();
		    
		    width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
		    height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));
		    
		    
		    StackPane root = new StackPane();
		    root.getChildren().add(mv);
		    
		    
		    final Scene scene = new Scene(root, 960, 540);
		    scene.setFill(Color.BLACK);
		    
		    
		    jfx.setScene(scene);
		    
		    mp.play();
		}
	});
		
		setFullScreen(dm, this);
		vpbframe.setLocation((getWidth() - vpbframe.getWidth())/2, getHeight() - vpbframe.getHeight());
		jfx.setSize(getWidth(), getHeight());
	}

	
	public void seekTo(int pos){
		mp.seek(Duration.seconds(pos));
	}
	
	public void pause(){
		mp.pause();
	}
	
	
	public void play(){
		mp.play();
	}
	
	
	// Sets the video to fullscreen
	private void setFullScreen(DisplayMode dmPara, JFrame framePara)
	{
		vc.setFullScreenWindow(framePara); // choose the JFrame to make fullscreen
		
		framePara.setResizable(false);
		
		// checks if there's settings for screen and if a change in display is supported
		if(dmPara != null && vc.isDisplayChangeSupported()) 
		{
			try
			{
				vc.setDisplayMode(dmPara); 
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}		
	}

	
	// Restores the screen from fullscreen
	public void restoreScreen()
	{
		vc.setFullScreenWindow(null);
	}
	
	
	public void exit(){
		restoreScreen();
	    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	

	@Override
	public void windowClosing(WindowEvent e) {
		mp.stop();
		vpbframeshown = false;
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		if(vpbframeshown == false){
			vpbframe.setSliderTime((int) mp.getCurrentTime().toSeconds());
			vpbframe.setVisible(true);
			jfx.setCursor(Cursor.getDefaultCursor());
			vpbframeshown = true;
		}else{
			vpbframe.setVisible(false);
			jfx.setCursor(blankcursor);
			vpbframeshown = false;
		}
	}
	
	
	// Uneeded Methods
	@Override
	public void windowOpened(WindowEvent e) {
	}

	
	@Override
	public void windowClosed(WindowEvent e) {
	}


	@Override
	public void windowIconified(WindowEvent e) {
	}


	@Override
	public void windowDeiconified(WindowEvent e) {
	}


	@Override
	public void windowActivated(WindowEvent e) {
	}


	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	
	@Override
	public void mousePressed(MouseEvent e) {	
	}


	@Override
	public void mouseReleased(MouseEvent e) {	
	}


	@Override
	public void mouseEntered(MouseEvent e) {
	}


	@Override
	public void mouseExited(MouseEvent e) {	
	}
}

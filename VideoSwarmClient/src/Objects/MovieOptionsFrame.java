package Objects;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import StreamerClient.VideoSwarmClient;

public class MovieOptionsFrame extends JFrame{
	
	private ImageIcon icon;
	private String movieName;
	private int length;
	private String plot;	
	private JLabel movieLabel;
	private JLabel lengthLabel;
	private JLabel iconLabel;
	private JLabel plotLabel;
	private JTextArea plotTextArea;
	private JButton watch;
	private JButton trailer;
	private JButton cancel;
	
	private Container c;
	
	public MovieOptionsFrame(Image icon, String movieName, int length, double trailerlength, String plot, String movieurl, String trailerurl, JScrollPane parent, VideoSwarmClient client) {
		setSize(parent.getWidth() / 2, parent.getHeight() / 2);
		getRootPane().setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.darkGray));
		
		this.icon = new ImageIcon(icon.getScaledInstance(this.getWidth() / 3, this.getHeight() / 4, java.awt.Image.SCALE_FAST));
		this.movieName = movieName;
		this.length = length;
		this.plot = plot;
		
		c = getContentPane();
		setLayout(null);
		setResizable(false);
		setUndecorated(true);
		setLocationRelativeTo(parent);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		int width = (getWidth() / 4);
		int height = (width * icon.getHeight(null)) / icon.getWidth(null);
		ImageIcon ico = new ImageIcon(icon.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
		iconLabel = new JLabel();
		iconLabel.setIcon(ico);
		iconLabel.setSize(ico.getIconWidth(), ico.getIconHeight());
		iconLabel.setLocation(getWidth() / 14, (getHeight() - ico.getIconHeight()) / 2);
		iconLabel.setBorder(new LineBorder(Color.BLACK));
		c.add(iconLabel);
		
		movieLabel = new JLabel(movieName);
		movieLabel.setFont(new Font(movieLabel.getFont().getName(), movieLabel.getFont().getStyle(), 18));
		movieLabel.setSize(movieLabel.getPreferredSize());
		movieLabel.setLocation((iconLabel.getX() + iconLabel.getWidth()) + (iconLabel.getWidth() / 5), iconLabel.getY());
		c.add(movieLabel);		
		
		lengthLabel = new JLabel("Running Time: " + getLength(length));
		lengthLabel.setSize(lengthLabel.getPreferredSize());
		lengthLabel.setLocation(movieLabel.getX(), getHeight() / 4);
		c.add(lengthLabel);
		
		plotLabel = new JLabel("Plot Summary");
		plotLabel.setSize(plotLabel.getPreferredSize());
		plotLabel.setLocation(movieLabel.getX(), lengthLabel.getY() + (lengthLabel.getHeight() * 2));
		c.add(plotLabel);
		
		plotTextArea = new JTextArea();
		plotTextArea.setEditable(false);
		plotTextArea.setLineWrap(true);
		plotTextArea.setWrapStyleWord(true);
		plotTextArea.append(plot);
		plotTextArea.setCaretPosition(0);
		
		JScrollPane plotscrollpane = new JScrollPane(plotTextArea);
		plotscrollpane.setSize(new Dimension((getWidth() - movieLabel.getX()) - (iconLabel.getWidth() / 4), getHeight() / 4));
		plotscrollpane.setLocation(plotLabel.getX(), plotLabel.getY() + (plotLabel.getHeight() + (plotLabel.getHeight() / 2)));
		c.add(plotscrollpane);
		
		watch = new JButton("Watch Movie");
		watch.setSize(watch.getPreferredSize());
		watch.setLocation(movieLabel.getX(), getHeight() - ((getHeight() - (plotscrollpane.getY() + plotscrollpane.getHeight())) / 2));
		watch.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.darkGray));
		watch.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				client.streamMovie(movieurl, movieName, length);
				dispose();
			}
		});
		c.add(watch);
				
		cancel = new JButton("Cancel");
		cancel.setSize(cancel.getPreferredSize());
		cancel.setLocation((plotscrollpane.getX() + plotscrollpane.getWidth()) - cancel.getWidth(), getHeight() - ((getHeight() - (plotscrollpane.getY() + plotscrollpane.getHeight())) / 2));
		cancel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.darkGray));
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		c.add(cancel);
		
		trailer = new JButton("Watch Trailer");
		trailer.setSize(trailer.getPreferredSize());
		trailer.setLocation((watch.getX() + watch.getWidth()) + ((cancel.getX() - (watch.getX() + watch.getWidth()) - trailer.getWidth()) / 2), getHeight() - ((getHeight() - (plotscrollpane.getY() + plotscrollpane.getHeight())) / 2));
		trailer.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.darkGray));
		trailer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				client.streamTrailer(trailerurl, movieName, trailerlength);
			}
		});
		c.add(trailer);

	}
	
	
	// Returns the length in a string in hours and minutes
	private String getLength(int length){
		int hours = 0;
		int size = length;
		
		while(size > 60){
			size = size - 60;
			hours++;
		}
		
		if(size == 1 && (hours < 1)){
			return (size + " minute");
		}
		
		if(hours < 1){
			return (size + " minutes");
		}
		
		if(hours == 1){
			return (hours + " hour, " + size + " minutes" );
		}
		
		return (hours + " hours, " + size + " minutes");
	}

}

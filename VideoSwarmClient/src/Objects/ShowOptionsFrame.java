package Objects;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import StreamerClient.VideoSwarmClient;

public class ShowOptionsFrame extends JFrame{
	
	private ArrayList<Season> seasons;
	private int numofseasons;
	private ImageIcon icon;
	private String showName;
	private JLabel showLabel;
	private JLabel iconLabel;
	private JTextArea episodeDescriptionArea;
	private JButton watch;
	private JButton cancel;
	private JLabel seasonLabel;
	private JLabel episodesLabel;
	private JComboBox seasonChooser;
	private JComboBox episodesChooser;
	private String selectedepisodeurl = "";
	private String selectedepisodename = "";
	private double selectedepisodelength = 0;
	private boolean episodeselected = false;
	
	private Container c;
	
	public ShowOptionsFrame(Image icon, String showName, ArrayList<Season> seasons, int numberofseasons, JScrollPane parent, VideoSwarmClient client) {
		setSize(parent.getWidth() / 2, parent.getHeight() / 2);
		getRootPane().setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.darkGray));
		
		this.seasons = seasons;
		this.numofseasons = numberofseasons;
		this.icon = new ImageIcon(icon.getScaledInstance(this.getWidth() / 3, this.getHeight() / 4, java.awt.Image.SCALE_FAST));
		this.showName = showName;
		
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
		
		showLabel = new JLabel(showName);
		showLabel.setFont(new Font(showLabel.getFont().getName(), showLabel.getFont().getStyle(), 18));
		showLabel.setSize(showLabel.getPreferredSize());
		showLabel.setLocation((iconLabel.getX() + iconLabel.getWidth()) + (iconLabel.getWidth() / 5), iconLabel.getY());
		c.add(showLabel);		
		
		
		// The Seasons
		seasonLabel = new JLabel("Season ");
		seasonLabel.setSize(seasonLabel.getPreferredSize());
		seasonLabel.setLocation(showLabel.getX(), showLabel.getY() + (showLabel.getHeight() + (showLabel.getHeight() / 2)));
		c.add(seasonLabel);
		
		seasonChooser = new JComboBox();
		seasonChooser.setEditable(false);
		for(int i = 0; i < numberofseasons; i++){
			seasonChooser.addItem(Integer.toString(seasons.get(i).getSeasonNumber()));
		}
		seasonChooser.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				episodeselected = false;
				episodeDescriptionArea.setText(null);
				removeEpisodes();
				showEpisodes(Integer.parseInt(seasonChooser.getSelectedItem().toString()));
			}
		});
		seasonChooser.setSize(seasonChooser.getPreferredSize());
		seasonChooser.setLocation(seasonLabel.getX() + seasonLabel.getWidth(), seasonLabel.getY());
		c.add(seasonChooser);
		
		
		
		// The Episodes
		episodesLabel = new JLabel("  Episodes: ");
		episodesLabel.setSize(episodesLabel.getPreferredSize());
		episodesLabel.setLocation(seasonChooser.getX() + seasonChooser.getWidth(), seasonLabel.getY());
		c.add(episodesLabel);
		
		
		episodesChooser = new JComboBox();
		episodesChooser.setEditable(false);
		for(int i = 0; i < seasons.get((Integer.parseInt(seasonChooser.getSelectedItem().toString()) - 1)).getNumberOfEpisodes(); i++){
			episodesChooser.addItem(seasons.get((Integer.parseInt(seasonChooser.getSelectedItem().toString()) - 1)).getEpisodeName(i + 1));
		}
		
		// Sets the first episode of the first season to be selected as the default episode to be played
		selectedepisodeurl = seasons.get(Integer.parseInt(seasonChooser.getSelectedItem().toString()) - 1).getEpisodeUrl(episodesChooser.getSelectedItem().toString());
		selectedepisodename = episodesChooser.getSelectedItem().toString();
		selectedepisodelength = seasons.get(Integer.parseInt(seasonChooser.getSelectedItem().toString()) - 1).getEpisodeLength(episodesChooser.getSelectedItem().toString());
		episodeselected = true;
		
		
		episodesChooser.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int s = Integer.parseInt(seasonChooser.getSelectedItem().toString());
				
				selectedepisodeurl = seasons.get(s - 1).getEpisodeUrl(episodesChooser.getSelectedItem().toString());
				selectedepisodename = episodesChooser.getSelectedItem().toString();
				selectedepisodelength = seasons.get(s - 1).getEpisodeLength(episodesChooser.getSelectedItem().toString());
				
				episodeselected = true;
				
				episodeDescriptionArea.setText(null);
				episodeDescriptionArea.append(seasons.get(s - 1).getEpisodeDescription(episodesChooser.getSelectedItem().toString()));
				episodeDescriptionArea.setCaretPosition(0);
			}
		});
		episodesChooser.setSize(episodesChooser.getPreferredSize());
		episodesChooser.setLocation(episodesLabel.getX() + episodesLabel.getWidth(), episodesLabel.getY());
		c.add(episodesChooser);
		
		
		episodeDescriptionArea = new JTextArea();
		episodeDescriptionArea.setEditable(false);
		episodeDescriptionArea.setLineWrap(true);
		episodeDescriptionArea.setWrapStyleWord(true);
		episodeDescriptionArea.setText(seasons.get(Integer.parseInt(seasonChooser.getSelectedItem().toString()) - 1).getEpisodeDescription(episodesChooser.getSelectedItem().toString()));
		episodeDescriptionArea.setCaretPosition(0);
						
		JScrollPane plotscrollpane = new JScrollPane(episodeDescriptionArea);
		plotscrollpane.setSize(new Dimension((getWidth() - showLabel.getX()) - (iconLabel.getWidth() / 4), getHeight() / 4));
		plotscrollpane.setLocation(showLabel.getX(), seasonChooser.getY() + (seasonChooser.getHeight() + (seasonChooser.getHeight() / 10)));
		c.add(plotscrollpane);
		
		watch = new JButton("Watch Episode");
		watch.setSize(watch.getPreferredSize());
		watch.setLocation(showLabel.getX(), (int)(getHeight() - ((getHeight() - (plotscrollpane.getY() + plotscrollpane.getHeight())) / 1.5)));
		watch.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.darkGray));
		watch.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(episodeselected){
					client.streamEpisode(selectedepisodeurl, selectedepisodename, selectedepisodelength);
					dispose();
				}
			}
		});
		c.add(watch);
				
		cancel = new JButton("Cancel");
		cancel.setSize(cancel.getPreferredSize());
		cancel.setLocation((plotscrollpane.getX() + plotscrollpane.getWidth()) - cancel.getWidth(), (int)(getHeight() - ((getHeight() - (plotscrollpane.getY() + plotscrollpane.getHeight())) / 1.5)));
		cancel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.darkGray));
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		c.add(cancel);
	}
	
	
	// Removes all the episodes being shown on the episodes panel
	private void removeEpisodes(){
		for(int i = 0; i < episodesChooser.getActionListeners().length; i++){
			episodesChooser.removeActionListener(episodesChooser.getActionListeners()[i]);
		}
		episodesChooser.removeAllItems();
	}
	
	
	// Shows the episodes of the season in the seasonpanel
	private void showEpisodes(int season){
		
		for(int i = 0; i < seasons.get(season - 1).getNumberOfEpisodes(); i++){
			episodesChooser.addItem(seasons.get(season - 1).getEpisodeName(i + 1));
		}
		episodesChooser.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int s = Integer.parseInt(seasonChooser.getSelectedItem().toString());
				
				selectedepisodeurl = seasons.get(s - 1).getEpisodeUrl(episodesChooser.getSelectedItem().toString());
				selectedepisodename = episodesChooser.getSelectedItem().toString();
				selectedepisodelength = seasons.get(s - 1).getEpisodeLength(episodesChooser.getSelectedItem().toString());
				
				episodeselected = true;
				
				episodeDescriptionArea.setText(null);
				episodeDescriptionArea.append(seasons.get(s - 1).getEpisodeDescription(episodesChooser.getSelectedItem().toString()));
				episodeDescriptionArea.setCaretPosition(0);
			}
		});
		c.revalidate();

		selectedepisodeurl = seasons.get(season - 1).getEpisodeUrl(episodesChooser.getSelectedItem().toString());
		selectedepisodename = episodesChooser.getSelectedItem().toString();
		selectedepisodelength = seasons.get(season - 1).getEpisodeLength(episodesChooser.getSelectedItem().toString());

		episodeselected = true;
		
		episodeDescriptionArea.setText(null);
		episodeDescriptionArea.append(seasons.get(season - 1).getEpisodeDescription(episodesChooser.getSelectedItem().toString()));
		episodeDescriptionArea.setCaretPosition(0);
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

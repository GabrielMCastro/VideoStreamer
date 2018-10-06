package Objects;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import StreamerClient.VideoSwarmClient;

public class VideoPlayerButtonsPanel extends JPanel{
	
	private static final int BUTTONHEIGHT = 100;
	private JButton play;
	private JButton pause;
	private JButton exit;
	private JSlider slider;
	private ChangeListener clistener;
	
	
	/* The constructor for an int movielength
	 */
	public VideoPlayerButtonsPanel(VideoSwarmClient parent, int movielength){
		
		setSize((int)(BUTTONHEIGHT * 3.7), (int)(BUTTONHEIGHT));
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.DARK_GRAY, Color.BLACK));
		
		clistener = new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				parent.seekTo(slider.getValue());
			}
		};
		
		setLayout(null);
		setVisible(true);
	
		
		exit = new JButton("Exit");
		exit.setSize(BUTTONHEIGHT / 2, BUTTONHEIGHT / 2);
		exit.setLocation((BUTTONHEIGHT / 4) - (int)(exit.getWidth() / 2.5), ((getHeight() - exit.getHeight())/2) - (exit.getHeight() / 4));
		exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.exitVideoPlayer();
			}			
		});
		exit.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
		add(exit);
		
		
		play = new JButton("Play");
		play.setSize(BUTTONHEIGHT / 2, BUTTONHEIGHT / 2);
		play.setLocation((exit.getX() + exit.getWidth()) + (play.getWidth() / 3), ((getHeight() - play.getHeight())/2) - (play.getHeight() / 4));
		play.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.playVideo();
			}
			
		});
		play.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
		add(play);
		
		
		pause = new JButton("Pause");
		pause.setSize(BUTTONHEIGHT / 2, BUTTONHEIGHT / 2);
		pause.setLocation((play.getX() + play.getWidth()) + (pause.getWidth() / 3), ((getHeight() - pause.getHeight())/2) - (pause.getHeight() / 4));
		exit.setBackground(Color.BLACK);
		pause.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.pauseVideo();
			}
		});
		pause.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
		add(pause);

		
		CustomSliderUI sui = new CustomSliderUI(slider);
		
		slider = new JSlider(JSlider.HORIZONTAL, 0, (movielength * 60), 0);
		slider.setSize((int)(BUTTONHEIGHT * 1.7), (BUTTONHEIGHT / 2));
		slider.setMajorTickSpacing((movielength * 60) / 4);
		slider.setMinorTickSpacing((movielength * 60) / 16);
		slider.setPaintTicks(true);
		slider.setUI(sui);
		slider.setFocusable(false);
		slider.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
		slider.setLocation((pause.getX() + pause.getWidth()) + (int)(pause.getWidth() / 8), ((getHeight() - slider.getHeight()) / 2) - (slider.getHeight() / 4));
		slider.addChangeListener(clistener);
		add(slider);
		
	}
	
	
	
	
	
	/* The constructor for an double movielength
	 */
	public VideoPlayerButtonsPanel(VideoSwarmClient parent, double movielength){
		
		setSize((int)(BUTTONHEIGHT * 3.7), (int)(BUTTONHEIGHT));
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.DARK_GRAY, Color.BLACK));
		
		clistener = new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				parent.seekTo(slider.getValue());
			}
		};
		
		setLayout(null);
		setVisible(true);
	
		
		exit = new JButton("Exit");
		exit.setSize(BUTTONHEIGHT / 2, BUTTONHEIGHT / 2);
		exit.setLocation((BUTTONHEIGHT / 4) - (int)(exit.getWidth() / 2.5), ((getHeight() - exit.getHeight())/2) - (exit.getHeight() / 4));
		exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.exitVideoPlayer();
			}			
		});
		exit.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
		add(exit);
		
		
		play = new JButton("Play");
		play.setSize(BUTTONHEIGHT / 2, BUTTONHEIGHT / 2);
		play.setLocation((exit.getX() + exit.getWidth()) + (play.getWidth() / 3), ((getHeight() - play.getHeight())/2) - (play.getHeight() / 4));
		play.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.playVideo();
			}
			
		});
		play.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
		add(play);
		
		
		pause = new JButton("Pause");
		pause.setSize(BUTTONHEIGHT / 2, BUTTONHEIGHT / 2);
		pause.setLocation((play.getX() + play.getWidth()) + (pause.getWidth() / 3), ((getHeight() - pause.getHeight())/2) - (pause.getHeight() / 4));
		exit.setBackground(Color.BLACK);
		pause.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.pauseVideo();
			}
		});
		pause.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
		add(pause);

		
		CustomSliderUI sui = new CustomSliderUI(slider);
		
		slider = new JSlider(JSlider.HORIZONTAL, 0, (int)(movielength * 60), 0);
		slider.setSize((int)(BUTTONHEIGHT * 1.7), (BUTTONHEIGHT / 2));
		slider.setMajorTickSpacing((int)(movielength * 60) / 4);
		slider.setMinorTickSpacing((int)(movielength * 60) / 16);
		slider.setPaintTicks(true);
		slider.setUI(sui);
		slider.setFocusable(false);
		slider.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
		slider.setLocation((pause.getX() + pause.getWidth()) + (int)(pause.getWidth() / 8), ((getHeight() - slider.getHeight()) / 2) - (slider.getHeight() / 4));
		slider.addChangeListener(clistener);
		add(slider);
		
	}
	
	
	public void setPlaybackTime(int time){
		slider.setValue(time);
	}
	
	
	public void setSliderTime(int time){
		slider.removeChangeListener(clistener);
		slider.setValue(time);
		slider.addChangeListener(clistener);
	}
	
	
	public void setMovieLength(int movielength){
		slider.removeChangeListener(clistener);
		slider.setMaximum(movielength*60);
		slider.addChangeListener(clistener);
		slider.setMajorTickSpacing((int)(movielength * 60) / 4);
		slider.setMinorTickSpacing((int)(movielength * 60) / 16);
	}
	
	public void setMovieLength(double movielength){
		slider.removeChangeListener(clistener);
		slider.setMaximum((int)(movielength*60));
		slider.addChangeListener(clistener);
		slider.setMajorTickSpacing((int)(movielength * 60) / 4);
		slider.setMinorTickSpacing((int)(movielength * 60) / 16);
	}
}

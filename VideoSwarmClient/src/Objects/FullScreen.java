package Objects;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

import javax.swing.JFrame;

public class FullScreen
{
	//allows access to graphics card
	private GraphicsDevice vc;
	
	public FullScreen()
	{
		// gets specs of screen
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc = env.getDefaultScreenDevice(); // gets specs and assigns it to graphics card variable
	}
	
	public void setFullScreen(DisplayMode dmPara, JFrame framePara)
	{
		framePara.setUndecorated(true);
		framePara.setResizable(false);
		vc.setFullScreenWindow(framePara); // choose the JFrame to make fullscreen
		
		if(dmPara != null && vc.isDisplayChangeSupported()) // checks if there's settings for screen and if a change in display is supported
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
	
	public Window getFullScreenWindow()
	{
		return vc.getFullScreenWindow();
	}
	
	public void restoreScreen()
	{
		Window w = vc.getFullScreenWindow();
		if(w != null)
			w.dispose();
		vc.setFullScreenWindow(null);
	}

}
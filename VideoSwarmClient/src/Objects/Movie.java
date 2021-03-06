/* Holds the info for the movies in the library
 */

package Objects;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Movie implements Serializable{
	static final long serialVersionUID = (long)10.1;
	
	private String name = ""; // The name of the movie
	private String graphicurl; // The movie graphic url, normally a poster
	private BufferedImage graphic; // The movie graphic, normally a poster
	private ImageIcon graphicicon; // The movie buttons icon
	private int length = 0; // The length of the movie in minutes
	private double trailerlength = 0; // The length of the trailer in minutes
	private String plotdescription = ""; // A short description of the plot 
	private String movieurl = ""; // The url of the movie
	private String trailerurl = ""; // The url of the trailer
	
	/**
	 * @param nm The name of the movie
	 */
	public Movie(String nm){
		name = nm;
	}
	
	
	/**
	 * @param nm The name of the movie
	 * @param pd The plot description
	 * @param l The length of the movie in minutes
	 * @param img The graphic of the movie, usually a poster
	 */
	public Movie(String nm, String pd, int l, String imgurl){
		name = nm;
		plotdescription = pd;
		length = l;
		graphicurl = imgurl;
	}
	
	public void setName(String nm){
		name = nm;
	}
	
	public void setGraphic(String imgurl){
		try {
			graphic = ImageIO.read(new File(new URL(graphicurl).getFile()));
			graphicicon = new ImageIcon(graphic);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setLength(int l){
		length = l;
	}
	
	public void setTrailerLength(double l){
		trailerlength = l;
	}
	
	public void setPlotDescription(String pd){
		plotdescription = pd;
	}
	
	public void setMovieUrl(String url){
		movieurl = url;
	}
	
	public void setTrailerUrl(String url){
		trailerurl = url;
	}
	
	
	public String getName(){
		return name;
	}
	
	public String getGraphicUrl(){
		return graphicurl;
	}
	
	public int getLength(){
		return length;
	}
	
	public double getTrailerLength(){
		return trailerlength;
	}
	
	public String getPlotDescription(){
		return plotdescription;
	}
	
	public String getMovieUrl(){
		return movieurl;
	}
	
	public String getTrailerUrl(){
		return trailerurl;
	}
	
	public Image getGraphic(){
		return graphic;
	}
	
	public ImageIcon getGraphicIcon(){
		return graphicicon;
	}
}

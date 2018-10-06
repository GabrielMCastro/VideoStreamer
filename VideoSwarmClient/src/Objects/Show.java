package Objects;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Show implements Serializable{
	static final long serialVersionUID = (long)10.2;
	
	private String name = ""; // The name of the show
	private String graphicurl = ""; // The show graphic url, normally for a poster
	private BufferedImage graphic; // The show's graphic, normally a poster
	private ImageIcon graphicicon; // The show's button icon
	private int noseasons = 0; // The number of seasons
	private ArrayList<Season> seasons = new ArrayList<Season>();
	
	
	/**
	 * @param nm The name of the show
	 */
	public Show(String nm){
		name = nm;
	}
	
	
	public void setName(String nm){
		name = nm;
	}
	
	public void setGraphic(String imgurl){
		graphicurl = imgurl;
		try {
			graphic = ImageIO.read(new File(new URL(graphicurl).getFile()));
			graphicicon = new ImageIcon(graphic);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setNumberOfSeasons(int s){
		noseasons = s;
	}
	
	
	public void addSeason(Season season){
		seasons.add(season);
	}
	
	
	public String getName(){
		return name;
	}
	
	public Image getGraphic(){
		return graphic;
	}
	
	public ImageIcon getGraphicIcon(){
		return graphicicon;
	}
	
	public String getGraphicUrl(){
		return graphicurl;
	}
	
	public int getNumberOfSeasons(){
		return noseasons;
	}
	
	public Season getSeason(int season){
		return seasons.get(season);
	}
	
	public ArrayList<Season> getSeasons(){
		return seasons;
	}
}

package Objects;

import java.io.Serializable;
import java.util.HashMap;

public class Season implements Serializable{
	static final long serialVersionUID = (long)10.3;

	private int seasonno = 0; // The season number of this season in the show
	private int noepisodes = 0; // The number of episodes
	private HashMap<Integer, String> episodeno = new HashMap<Integer, String>(); // Maps the episode number to the episode name
	private HashMap<String, Double> episodelength = new HashMap<String, Double>(); // Maps the episode name to episode length
	private HashMap<String, String> episodeurl = new HashMap<String, String>(); // Maps the episode name to the episode url
	private HashMap<String, String> episodedescription = new HashMap<String, String>(); // Maps the episode name to the episode description

	
	public Season(int episodes){
		noepisodes = episodes;
	}
	
	public void addEpisode(int episodenumber, String episodename, double episodel){
		episodeno.put(episodenumber, episodename);
		episodelength.put(episodename, episodel);
	}
	
	public void setSeasonNumber(int num){
		seasonno = num;
	}
	
	public void setEpisodeDescription(String epname, String epdescription){
		episodedescription.put(epname, epdescription);
	}

	public void setEpisodeUrl(String episodenm, String epurl){
		episodeurl.put(episodenm, epurl);
	}

	
	
	public String getEpisodeDescription(String epname){
		return episodedescription.get(epname);
	}

	public int getSeasonNumber(){
		return seasonno;
	}
	
	public String getEpisodeName(int episodenumber){
		return episodeno.get(episodenumber);
	}
	
	public double getEpisodeLength(String episodenm){
		return episodelength.get(episodenm);
	}	
		
	public String getEpisodeUrl(String episodenm){
		return episodeurl.get(episodenm);
	}
	
	public int getNumberOfEpisodes(){
		return noepisodes;
	}
}

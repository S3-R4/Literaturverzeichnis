package model.Audiovisuell;

import java.util.ArrayList;
import java.util.List;

public class Podcastfolgen extends Audiovisuell {
	//Note:
	//Sollte eigentlich in die Superklasse aber um die existierende Struktur beizubehalten werden die Daten hier gehalten
	//produzent der superklasse wird als erster produzent der liste interpretiert und alle Namen in der liste dann angehangen
	public List<String> produzenten = new ArrayList<String>();

	
	private String herausgebendeOrganisation = "";
	private int podcastFolge = 0;
	private String podcastFolgenName = "";
	
	public Podcastfolgen() {
		setBeitragsArt("Podcast-Folge");
		
	}

	
	
	public String getHerausgebendeOrganisation() {
		return herausgebendeOrganisation;
	}

	public void setHerausgebendeOrganisation(String herausgebendeOrganisation) {
		this.herausgebendeOrganisation = herausgebendeOrganisation;
	}



	public int getPodcastFolge() {
		return podcastFolge;
	}
	
	
	
	
	public void setPodcastFolge(int podcastFolge) {
		this.podcastFolge = podcastFolge;
	}

	
	public void addProduzent(String produzent) {
		produzenten.add(produzent);
	}
	public void removeProduzent(int index) {
		produzenten.remove(index);
	}
	

	@Override
	public String erstelleQuellenangabe() {
		
		String authoren = getProduzent();
		
		for(String extraProduzent : produzenten) {
			authoren += ", " + extraProduzent;
		}
			
		return authoren + ". " + "(" + getErscheinungsDatum() + ") " 
				+ "Folge #" + String.valueOf(podcastFolge) + " - "
				+ getPodcastFolgenName() + ". " + "[" +  getBeitragsArt() + "] " 
				+ "In: " + getTitel() + " "
				+ getHerausgebendeOrganisation() 
				+ ". " 
				+ getUrl() + " " 
				+ "(" +getAbruffDatum() + ").";
	}



	public String getPodcastFolgenName() {
		return podcastFolgenName;
	}



	public void setPodcastFolgenName(String podcastFolgenName) {
		this.podcastFolgenName = podcastFolgenName;
	}
}

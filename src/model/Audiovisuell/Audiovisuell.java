package model.Audiovisuell;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import model.Literatur;
public abstract class Audiovisuell extends Literatur{
	protected String beitragsArt;
	protected String medium;
	protected String url;

	// public List<String> autor;
	// public LocalDate veroeffentlichungsdatum;
	// public LocalDate abrufdatum;

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public List<String> getAutor() {
		return this.autor;
	}

	public void setAutor(List<String> autoren) {
        this.autor = new ArrayList<>();

        for(String autor : autoren) {
            if(!autor.isBlank()) {
                this.autor.add(autor.trim());
            }
        }
    }

	public LocalDate getVeroeffentlichungsdatum()
	{
		return this.veroeffentlichungsdatum;
	}

	public void setVeroffentlichungsdatum(LocalDate veroeffentlichungsdatum) throws DateTimeParseException, DateTimeException{
		try {
			this.veroeffentlichungsdatum = veroeffentlichungsdatum;
		}
        catch (DateTimeParseException e) 
		{
			System.out.println("Failed to parse date: " + e.getMessage());
		}
		catch(DateTimeException e)
		{
			System.out.println("Invalid date: " + e.getMessage());
		}
    }

	public String getBeitragsArt() {
		return beitragsArt;
	}

	public void setBeitragsArt(String beitragsArt) {
		this.beitragsArt = beitragsArt;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public LocalDate getAbruffDatum() {
		return this.abrufdatum;
	}

	public void setAbruffDatum(LocalDate abruffDatum) throws DateTimeParseException, DateTimeException{
		try {
			this.abrufdatum = abruffDatum;
		}
        catch (DateTimeParseException e) 
		{
			System.out.println("Failed to parse date: " + e.getMessage());
		}
		catch(DateTimeException e)
		{
			System.out.println("Invalid date: " + e.getMessage());
		}
	}

	public abstract String datumFormatierung(LocalDate datum);

	
	public String getAutorsString(List<String> autors){
		String autorsString = "";

		for (int i = 0; i < autors.size(); i++){
			if(i > 0)
				autorsString += " ,";
			autorsString += autors.get(i);
		}
		return autorsString;
	}
}

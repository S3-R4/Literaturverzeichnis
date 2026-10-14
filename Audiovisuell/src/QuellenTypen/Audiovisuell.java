package QuellenTypen;

public abstract class Audiovisuell {
    private String titel;
    private String produzent;
    private String erscheinungsDatum;
    private String beitragsArt;
    private String medium;
    private String url;
    private String abruffDatum;

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getProduzent() {
        return produzent;
    }

    public void setProduzent(String produzent) {
        this.produzent = produzent;
    }

    public String getErscheinungsDatum() {
        return erscheinungsDatum;
    }

    public void setErscheinungsDatum(String erscheinungsDatum) {
        this.erscheinungsDatum = erscheinungsDatum;
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

    public String getAbruffDatum() {
        return abruffDatum;
    }

    public void setAbruffDatum(String abruffDatum) {
        this.abruffDatum = abruffDatum;
    }

    public void update() {
	}

	public void delete() {
	}

	public void add() {
	}

    public abstract String erstelleQuellenangabe(); 

}
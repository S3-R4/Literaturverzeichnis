package QuellenTypen;

public class SocialMediaBeitraege extends Audiovisuell {
	private String text;
	private String beschreibung;

	@Override
	public String erstelleQuellenangabe() {
		return getProduzent() + ". " +
				getErscheinungsDatum() + ". " +
				getTitel() + ". [" +
				getBeitragsArt() + "]. " +
				getMedium() +
				getUrl() + " " +
				getAbruffDatum();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
}

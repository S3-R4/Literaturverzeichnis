package QuellenTypen;

public class FIlmsVideos extends Audiovisuell {
	private String produktionsfirma;

	@Override
	public String erstelleQuellenangabe() {
		if (getUrl() != null) {
			return getProduzent() + ". " +
					getErscheinungsDatum() + ". " +
					getTitel() + " [" +
					getBeitragsArt() + "]. " +
					getProduktionsfirma() + ". " +
					getUrl() + ".";
		} else {
			return getProduzent() + ". " +
					getErscheinungsDatum() + ". " +
					getTitel() + " [" +
					getBeitragsArt() + "]. " +
					getProduktionsfirma() + ".";
		}
	}

	public String getProduktionsfirma() {
		return this.produktionsfirma;
	}

	public void setProduktionsfirma(String produktionsfirma) {
		this.produktionsfirma = produktionsfirma;
	}
}

package QuellenTypen;

public class Youtube extends Audiovisuell {
	@Override
	public String erstelleQuellenangabe() {
		return getProduzent() + ". " +
				getErscheinungsDatum() + ". " +
				getTitel() + ". [" +
				getBeitragsArt() + "]. " +
				getMedium() + ". " +
				getUrl() + " " +
				getAbruffDatum();
	}
}

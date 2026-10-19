package QuellenTypen;

public class PodcastGesamtwerk extends Audiovisuell {

	private String herausgebendeOrganisation;

	@Override
	public String erstelleQuellenangabe() {
		return getProduzent() + ". " + getErscheinungsDatum() + ". [" + getTitel() + "]. "
				+ getHerausgebendeOrganisation() + ". " + getUrl() + " " + getAbruffDatum();
	}

	public String getHerausgebendeOrganisation() {
		return herausgebendeOrganisation;
	}

	public void setHerausgebendeOrganisation(String herausgebendeOrganisation) {
		this.herausgebendeOrganisation = herausgebendeOrganisation;
	}
}

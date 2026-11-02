
public class Bildbelegclass {
private String Urheber1;
private String Urheber2;
private String Urheber3;
private String Urheber4;
private boolean MehrUrheber;
private String Titel;
private String Medienart;
private String Erscheinungsjahr;
private String Herausgeber;
private String Anbieter;
private String Abrufdatum;


public Bildbelegclass() {
}



public Bildbelegclass(String urheber1, String urheber2, String urheber3, String urheber4, boolean mehrurheber,  String titel, String medienart, String erscheinungsjahr, String herausgeber,
		String anbieter, String abrufdatum) {
	Urheber1 = urheber1;
	Urheber2 = urheber2;
	Urheber3 = urheber3;
	Urheber4 = urheber4;
	MehrUrheber = mehrurheber;
	Titel = titel;
	Medienart = medienart;
	Erscheinungsjahr = erscheinungsjahr;
	Herausgeber = herausgeber;
	Anbieter = anbieter;
	Abrufdatum = abrufdatum;
}




public String getUrheber1() {
	return Urheber1;
}
public void setUrheber1(String urheber1) {
	Urheber1 = urheber1;
}
public String getUrheber2() {
	if (Urheber2.equals("")) {
	return "";	
	}
	return ", " + Urheber2;
}
public void setUrheber2(String urheber2) {
	Urheber2 = urheber2;
}
public String getUrheber3() {
	if (Urheber3.equals("")) {
	return "";
	} return ", " + Urheber3;
}
public void setUrheber3(String urheber3) {
	Urheber3 = urheber3;
}
public String getUrheber4() {
	if (Urheber4.equals("")) {
		return "";
		} return ", " + Urheber4;
}
public void setUrheber4(String urheber4) {
	Urheber4 = urheber4;
}
public String getMehrUrheber(boolean mehrurheber) {
	if (MehrUrheber) {
	return " et al.";
	}
	return "";
}
public void setUrheber4(boolean mehrurheber) {
	MehrUrheber = mehrurheber;
}
public String getTitel() {
	return Titel;
}
public void setTitel(String titel) {
	Titel = titel;
}
public String getMedienart() {
	if (Medienart.equals("")) {
		return "";
	}
	return "[" + Medienart + "]. ";
}
public void setMedienart(String medienart) {
	Medienart = medienart;
}
public String getErscheinungsjahr() {
	if (Erscheinungsjahr.equals("")) {
		return "";
	}
	return " (" + Erscheinungsjahr + "). ";
}
public void setErscheinungsjahr(String erscheinungsjahr) {
	Erscheinungsjahr = erscheinungsjahr;
}
public String getHerausgeber() {
	if (Herausgeber.equals("")) {
		return "";
	}
	return Herausgeber + ". ";
}
public void setHerausgeber(String herausgeber) {
	Herausgeber = herausgeber;
}
public String getAnbieter() {
	return Anbieter;
}
public void setAnbieter(String anbieter) {
	Anbieter = anbieter;
}
public String getAbrufdatum() {
	if (Abrufdatum.equals("")) {
		return "";
	}
	return "[" + Abrufdatum + "].";
}
public void setAbrufdatum(String abrufdatum) {
	Abrufdatum = abrufdatum;
}

public String getbeleg(Bildbelegclass bildbelegclass) {
	return getUrheber1() + getUrheber2() + getUrheber3() + getUrheber4() + getMehrUrheber(MehrUrheber) + getErscheinungsjahr() + getTitel() + getMedienart() + getHerausgeber() + getAnbieter() + getAbrufdatum();
	
}

}
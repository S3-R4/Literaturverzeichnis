package junit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
 
import work.Website;
public class Test_0 {

	@Test
	public void guteQuelleLang() {
		String autor = "Mole, B.";
		String websitename = "Ars Technica";
		String titel = "mRNA vaccines are in RFK Jr’s crosshairs; funding in question";
		String url = "https://arstechnica.com/health/2025/03/federal-funding-formrna-vaccine-research-in-jeopardy-under-rfk-jr/";
		String aufrufdatum = "19.03.2025";
		String veroeffentlichungsdatum = "2025, 17. März";
		String textart = "Report";
		Website w = new Website(autor, websitename, titel, url, aufrufdatum, veroeffentlichungsdatum, textart);
		
		assertEquals("Mole, B. (2025, 17. März). Report: mRNA vaccines are in RFK Jr’s crosshairs; funding in question. Ars Technica. https://arstechnica.com/health/2025/03/federal-funding-formrna-vaccine-research-in-jeopardy-under-rfk-jr/ (19.03.2025).", w.generiereLangbeleg());
	}
	
	@Test
	public void schlechteQuelleLang() {
		String autor = "";
		String websitename = "Bundeswettbewerb Informatik";
		String titel = "Die Aufgaben des Bundeswettbewerbs Informatik";
		String url = "https://bwinf.de/bundeswettbewerb/aufgaben/";
		String aufrufdatum = "13.10.2026";
		String veroeffentlichungsdatum = "2025, 1. September";
		String textart = "";
		Website w = new Website(autor, websitename, titel, url, aufrufdatum, veroeffentlichungsdatum, textart);
		assertEquals("u. Autor (2025, 1. September). Die Aufgaben des Bundeswettbewerbs Informatik. Bundeswettbewerb Informatik. https://bwinf.de/bundeswettbewerb/aufgaben/ (13.10.2026).", w.generiereLangbeleg());
	}

	@Test
	public void guteQuelleKurz() {
		String autor = "Mole, B.";
		String websitename = "Ars Technica";
		String titel = "mRNA vaccines are in RFK Jr’s crosshairs; funding in question";
		String url = "https://arstechnica.com/health/2025/03/federal-funding-formrna-vaccine-research-in-jeopardy-under-rfk-jr/";
		String aufrufdatum = "19.03.2025";
		String veroeffentlichungsdatum = "2025, 17. März";
		String textart = "Report";
		Website w = new Website(autor, websitename, titel, url, aufrufdatum, veroeffentlichungsdatum, textart);
		
		assertEquals("Mole, B. (2025, 17. März)", w.generiereKurzerBeleg());
	}
	
	@Test
	public void schlechteQuelleKurz() {
		String autor = "";
		String websitename = "Bundeswettbewerb Informatik";
		String titel = "Die Aufgaben des Bundeswettbewerbs Informatik	";
		String url = "https://bwinf.de/bundeswettbewerb/aufgaben/";
		String aufrufdatum = "13.10.2026";
		String veroeffentlichungsdatum = "2025, 1. September";
		String textart = "";
		Website w = new Website(autor, websitename, titel, url, aufrufdatum, veroeffentlichungsdatum, textart);
		
		assertEquals("Bundeswettbewerb Informatik (2025, 1. September)", w.generiereKurzerBeleg());
	}
	
}

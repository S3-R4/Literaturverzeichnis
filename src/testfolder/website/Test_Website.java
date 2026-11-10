package testfolder.website;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.Website;

public class Test_Website {

	@Test
	public void guteQuelleLang() {
		ArrayList<String> autor = new ArrayList<String>(List.of("Mole, B."));
		String websitename = "Ars Technica";
		String titel = "mRNA vaccines are in RFK Jr’s crosshairs; funding in question";
		String url = "https://arstechnica.com/health/2025/03/federal-funding-formrna-vaccine-research-in-jeopardy-under-rfk-jr/";
		LocalDate aufrufdatum = LocalDate.of(2025, 3, 19);
		LocalDate veroeffentlichungsdatum = LocalDate.of(2025, 3, 17);
		String textart = "Report";
		Website w = new Website(autor, websitename, titel, url, aufrufdatum, veroeffentlichungsdatum, textart);
		
		assertEquals("Mole, B. (2025, 17. März). Report: mRNA vaccines are in RFK Jr’s crosshairs; funding in question. Ars Technica. https://arstechnica.com/health/2025/03/federal-funding-formrna-vaccine-research-in-jeopardy-under-rfk-jr/ (19.03.2025).", w.generiereLangbeleg());
	}
	
	@Test
	public void schlechteQuelleLang() {
		ArrayList<String> autor = new ArrayList<String>();
		String websitename = "Bundeswettbewerb Informatik";
		String titel = "Die Aufgaben des Bundeswettbewerbs Informatik";
		String url = "https://bwinf.de/bundeswettbewerb/aufgaben/";
		LocalDate aufrufdatum = LocalDate.of(2026, 10, 13);
		LocalDate veroeffentlichungsdatum = LocalDate.of(2025, 9, 1);
		String textart = "";
		Website w = new Website(autor, websitename, titel, url, aufrufdatum, veroeffentlichungsdatum, textart);
		assertEquals("u. Autor (2025, 1. September). Die Aufgaben des Bundeswettbewerbs Informatik. Bundeswettbewerb Informatik. https://bwinf.de/bundeswettbewerb/aufgaben/ (13.10.2026).", w.generiereLangbeleg());
	}

	@Test
	public void guteQuelleKurz() {
		ArrayList<String> autor = new ArrayList<String>(List.of("Mole, B."));
		String websitename = "Ars Technica";
		String titel = "mRNA vaccines are in RFK Jr’s crosshairs; funding in question";
		String url = "https://arstechnica.com/health/2025/03/federal-funding-formrna-vaccine-research-in-jeopardy-under-rfk-jr/";
		LocalDate aufrufdatum = LocalDate.of(2025, 3, 19);
		LocalDate veroeffentlichungsdatum = LocalDate.of(2025, 3, 17);
		String textart = "Report";
		Website w = new Website(autor, websitename, titel, url, aufrufdatum, veroeffentlichungsdatum, textart);
		
		assertEquals("Mole, B. (2025, 17. März)", w.generiereKurzbeleg());
	}
	
	@Test
	public void schlechteQuelleKurz() {
		ArrayList<String> autor = new ArrayList<String>();
		String websitename = "Bundeswettbewerb Informatik";
		String titel = "Die Aufgaben des Bundeswettbewerbs Informatik	";
		String url = "https://bwinf.de/bundeswettbewerb/aufgaben/";
		LocalDate aufrufdatum = LocalDate.of(2026, 10, 13);
		LocalDate veroeffentlichungsdatum = LocalDate.of(2025, 9, 1);
		String textart = "";
		Website w = new Website(autor, websitename, titel, url, aufrufdatum, veroeffentlichungsdatum, textart);
		
		assertEquals("Bundeswettbewerb Informatik (2025, 1. September)", w.generiereKurzbeleg());
	}
	
}

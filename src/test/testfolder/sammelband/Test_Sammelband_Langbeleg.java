package test.testfolder.sammelband;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import model.Sammelbandbeleg;

import static org.junit.jupiter.api.Assertions.*;

public class Test_Sammelband_Langbeleg {

	@Test
	public void testNormalerLangbeleg() {

		Sammelbandbeleg beleg = new Sammelbandbeleg(List.of("Müller, T."), Optional.of(LocalDate.of(2020, 1, 1)),
				Optional.of("Quellenangaben richtig erstellen"), List.of("P. Lahm"),
				Optional.of("Quellen zitieren und belegen: Eine Anleitung"), Optional.of(2), Optional.of("Scribbr"),
				Optional.of("Berlin"), Optional.of(19), Optional.of(31));

		assertEquals("Müller, T. (2020). Quellenangaben richtig erstellen. "
				+ "In P. Lahm (Hrsg.), Quellen zitieren und belegen: "
				+ "Eine Anleitung (2. Aufl., S. 19-31). Berlin: Scribbr.", beleg.generiereLangbeleg());
	}

	@Test
	public void testFehlenderAutor() {
		Sammelbandbeleg beleg = new Sammelbandbeleg(List.of(), Optional.of(LocalDate.of(2020, 1, 1)),
				Optional.of("Informatik"),
				List.of("Lahm"), Optional.of("Grundlagen"), Optional.empty(), Optional.of("Springer"),
				Optional.of("Berlin"), Optional.of(10), Optional.of(20));

		assertTrue(beleg.generiereLangbeleg().startsWith("o.A."));
	}

	@Test
	public void testFehlendeAuflage() {
		Sammelbandbeleg beleg = new Sammelbandbeleg(List.of("Müller"), Optional.of(LocalDate.of(2020, 1, 1)),
				Optional.of("Informatik"),
				List.of("Lahm"), Optional.of("Grundlagen"), Optional.empty(), Optional.of("Springer"),
				Optional.of("Berlin"), Optional.of(10), Optional.of(20));

		assertFalse(beleg.generiereLangbeleg().contains("Aufl."));
	}
}

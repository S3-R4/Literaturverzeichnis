
package sammelband;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestSammelbandKurzbeleg {

	@Test
	public void testNormalerKurzbeleg() {

		Sammelbandbeleg beleg = new Sammelbandbeleg(List.of("Müller"), Optional.of(2020), Optional.of("Informatik"),
				List.of("Lahm"), Optional.of("Grundlagen"), Optional.of(2), Optional.of("Springer"),
				Optional.of("Berlin"), Optional.of(19), Optional.of(31));

		assertEquals("(Müller, 2020, S. 19-31)", beleg.getBelegKurz());
	}

	@Test
	public void testFehlendesJahr() {

		Sammelbandbeleg beleg = new Sammelbandbeleg(List.of("Müller"), Optional.empty(), Optional.of("Informatik"),
				List.of("Lahm"), Optional.of("Grundlagen"), Optional.empty(), Optional.of("Springer"),
				Optional.of("Berlin"), Optional.of(19), Optional.of(31));

		assertEquals("(Müller, o.J., S. 19-31)", beleg.getBelegKurz());
	}

	@Test
	public void testOhneSeitenangabe() {

		Sammelbandbeleg beleg = new Sammelbandbeleg(List.of("Müller"), Optional.of(2020), Optional.of("Informatik"),
				List.of("Lahm"), Optional.of("Grundlagen"), Optional.empty(), Optional.of("Springer"),
				Optional.of("Berlin"), Optional.empty(), Optional.empty());

		assertEquals("(Müller, 2020)", beleg.getBelegKurz());
	}
}
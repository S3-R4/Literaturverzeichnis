
package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Sortierer {

public enum Kriterium {
TITEL,
QUELLENTYP,
AUTOR,
JAHR
}

public enum Richtung {
AUFSTEIGEND,
ABSTEIGEND
}

public record SortierPrioritaet(Kriterium kriterium, Richtung richtung) {}

public Sortierer() {
}

public List<Literatur> sortiere(List<Literatur> quellen,List<SortierPrioritaet> prioritaeten) {
List<Literatur> sortierteListe = new ArrayList<>(quellen);

Comparator<Literatur> comparator = (a, b) -> 0;

for (SortierPrioritaet prioritaet : prioritaeten) {
comparator = comparator.thenComparing(erstelleComparator(prioritaet));
}

sortierteListe.sort(comparator);
return sortierteListe;
}

private Comparator<Literatur> erstelleComparator(SortierPrioritaet prioritaet) {
return (a, b) -> {
int vergleich = 0;

switch (prioritaet.kriterium()) {
case TITEL:

vergleich = vergleicheText(a.getTitel().orElse(null), b.getTitel().orElse(null), prioritaet.richtung());
break;

case QUELLENTYP:
vergleich = vergleicheText(a.getClass().getSimpleName(),b.getClass().getSimpleName(),prioritaet.richtung());
break;

case AUTOR:
vergleich = vergleicheText(
ersterAutor(a.getAutor().orElse(null)),
ersterAutor(b.getAutor().orElse(null)),
prioritaet.richtung());
break;

case JAHR:
vergleich = vergleicheDatum(a.getVeroeffentlichungsdatum().orElse(null),b.getVeroeffentlichungsdatum().orElse(null),prioritaet.richtung());
break;
}

return vergleich;
};
}

private String ersterAutor(List<String> autoren) {
if (autoren == null) {
return null;
}

return autoren.stream().filter(a -> a != null && !a.isBlank()).findFirst().orElse(null);
}

private int vergleicheText(
String a, String b, Richtung richtung) {

boolean aLeer = a == null || a.isBlank();
boolean bLeer = b == null || b.isBlank();

if (aLeer && bLeer) return 0;
if (aLeer) return 1;
if (bLeer) return -1;

int vergleich = a.compareToIgnoreCase(b);

return richtung == Richtung.AUFSTEIGEND
? vergleich
: -vergleich;
}

private int vergleicheDatum(
LocalDate a, LocalDate b, Richtung richtung) {

if (a == null && b == null) return 0;
if (a == null) return 1;
if (b == null) return -1;

int vergleich = a.compareTo(b);

return richtung == Richtung.AUFSTEIGEND? vergleich: -vergleich;
}
}

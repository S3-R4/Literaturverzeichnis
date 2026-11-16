
package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class Sortierer {

public enum Kriterium {
NUMMER,
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
return sortiere(quellen, prioritaeten, this::erstelleComparator);
}

private <T> List<T> sortiere(List<T> quellen, List<SortierPrioritaet> prioritaeten,
        Function<SortierPrioritaet, Comparator<T>> comparatorFabrik) {
List<T> sortierteListe = new ArrayList<>(quellen);

Comparator<T> comparator = (a, b) -> 0;

for (SortierPrioritaet prioritaet : prioritaeten) {
comparator = comparator.thenComparing(comparatorFabrik.apply(prioritaet));
}

sortierteListe.sort(comparator);
return sortierteListe;
}

public List<Object[]> sortiereTabellenZeilen(List<Object[]> zeilen,
        List<SortierPrioritaet> prioritaeten) {
return sortiere(zeilen, prioritaeten, prioritaet -> {
    int spalte = switch (prioritaet.kriterium()) {
        case NUMMER -> 0;
        case TITEL -> 1;
        case QUELLENTYP -> 2;
        case AUTOR -> 3;
        case JAHR -> 4;
    };
    return (a, b) -> {
        if (prioritaet.kriterium() == Kriterium.NUMMER
                || prioritaet.kriterium() == Kriterium.JAHR) {
            return vergleicheWerte(tabellenZahl(a[spalte]), tabellenZahl(b[spalte]),
                    prioritaet.richtung());
        }
        return vergleicheText(a[spalte] == null ? null : a[spalte].toString(),
                b[spalte] == null ? null : b[spalte].toString(), prioritaet.richtung());
    };
});
}

private Integer tabellenZahl(Object wert) {
String text = wert == null ? "" : wert.toString().trim();
if (text.isEmpty() || text.equals("o.J.")) {
    return null;
}
return Integer.valueOf(text);
}

private Comparator<Literatur> erstelleComparator(SortierPrioritaet prioritaet) {
return (a, b) -> {
int vergleich = 0;

switch (prioritaet.kriterium()) {
case NUMMER:
throw new IllegalArgumentException("Nummern sind nur für Tabellenzeilen verfügbar.");
case TITEL:

vergleich = vergleicheText(a.getTitel(), b.getTitel(), prioritaet.richtung());
break;

case QUELLENTYP:
vergleich = vergleicheText(a.getClass().getSimpleName(),b.getClass().getSimpleName(),prioritaet.richtung());
break;

case AUTOR:
vergleich = vergleicheText(
ersterAutor(a.getAutor()),
ersterAutor(b.getAutor()),
prioritaet.richtung());
break;

case JAHR:
vergleich = vergleicheWerte(a.getVeroeffentlichungsdatum(),b.getVeroeffentlichungsdatum(),prioritaet.richtung());
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

private <T extends Comparable<? super T>> int vergleicheWerte(
T a, T b, Richtung richtung) {

if (a == null && b == null) return 0;
if (a == null) return 1;
if (b == null) return -1;

int vergleich = a.compareTo(b);

return richtung == Richtung.AUFSTEIGEND? vergleich: -vergleich;
}
}


package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Sorts copies of source lists by ordered criteria, placing missing values last.
 */
public class Sortierer {

    /**
     * Identifies the bibliographic attribute used for sorting.
     */
    public enum Kriterium {
        TITEL,
        QUELLENTYP,
        AUTOR,
        JAHR
    }

    /**
     * Specifies ascending or descending sort order.
     */
    public enum Richtung {
        AUFSTEIGEND,
        ABSTEIGEND
    }

    /**
     * Combines a sort criterion with its direction.
     *
     * @param kriterium attribute to compare
     * @param richtung sort direction
     */
    public record SortierPrioritaet(Kriterium kriterium, Richtung richtung) {
    }

    /**
     * Creates a Sortierer instance.
     */
    public Sortierer() {
    }

    /**
     * Returns a stably sorted copy using the supplied criteria in order.
     *
     * @param quellen sources to sort
     * @param prioritaeten sort criteria in priority order
     * @return the sorted copy; the input list is unchanged
     */
    public List<Literatur> sortiere(List<Literatur> quellen, List<SortierPrioritaet> prioritaeten) {
        List<Literatur> sortierteListe = new ArrayList<>(quellen);

        Comparator<Literatur> comparator = (a, b) -> 0;

        for (SortierPrioritaet prioritaet : prioritaeten) {
            comparator = comparator.thenComparing(erstelleComparator(prioritaet));
        }

        sortierteListe.sort(comparator);
        return sortierteListe;
    }

    /**
     * Builds a comparator for one criterion with missing values ordered last.
     *
     * @param prioritaet sort criterion and direction
     * @return the comparator for the requested direction
     */
    private Comparator<Literatur> erstelleComparator(SortierPrioritaet prioritaet) {
        return (a, b) -> {
            int vergleich = 0;

            switch (prioritaet.kriterium()) {
                case TITEL:

                    vergleich = vergleicheText(a.getTitel().orElse(null), b.getTitel().orElse(null),
                            prioritaet.richtung());
                    break;

                case QUELLENTYP:
                    vergleich = vergleicheText(a.getClass().getSimpleName(), b.getClass().getSimpleName(),
                            prioritaet.richtung());
                    break;

                case AUTOR:
                    vergleich = vergleicheText(
                            ersterAutor(a != null ? a.getAutor() : null),
                            ersterAutor(b != null ? b.getAutor() : null),
                            prioritaet.richtung());
                    break;

                case JAHR:
                    vergleich = vergleicheDatum(a.getVeroeffentlichungsdatum().orElse(null),
                            b.getVeroeffentlichungsdatum().orElse(null), prioritaet.richtung());
                    break;
            }

            return vergleich;
        };
    }

    /**
     * Finds the first nonblank author in a possibly null list.
     *
     * @param autoren author list
     * @return the first usable author, or null
     */
    private String ersterAutor(List<String> autoren) {
        if (autoren == null) {
            return null;
        }

        return autoren.stream().filter(a -> a != null && !a.isBlank()).findFirst().orElse(null);
    }

    /**
     * Compares text ignoring case, always placing null or blank values last.
     *
     * @param a first value
     * @param b second value
     * @param richtung sort direction
     * @return a negative, zero, or positive comparison result
     */
    private int vergleicheText(
            String a, String b, Richtung richtung) {

        boolean aLeer = a == null || a.isBlank();
        boolean bLeer = b == null || b.isBlank();

        if (aLeer && bLeer)
            return 0;
        if (aLeer)
            return 1;
        if (bLeer)
            return -1;

        int vergleich = a.compareToIgnoreCase(b);

        return richtung == Richtung.AUFSTEIGEND
                ? vergleich
                : -vergleich;
    }

    /**
     * Compares dates in the requested direction, always placing null dates last.
     *
     * @param a first value
     * @param b second value
     * @param richtung sort direction
     * @return a negative, zero, or positive comparison result
     */
    private int vergleicheDatum(
            LocalDate a, LocalDate b, Richtung richtung) {

        if (a == null && b == null)
            return 0;
        if (a == null)
            return 1;
        if (b == null)
            return -1;

        int vergleich = a.compareTo(b);

        return richtung == Richtung.AUFSTEIGEND ? vergleich : -vergleich;
    }
}

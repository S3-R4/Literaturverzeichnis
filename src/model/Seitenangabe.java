package model;

import java.util.Optional;

/**
 * Validates an optional page range and formats it for citations.
 */
public class Seitenangabe {

    private Optional<Integer> anfang;
    private Optional<Integer> ende;

    /**
     * Creates a Seitenangabe instance with the supplied metadata.
     *
     * @param anfang first page, if known
     * @param ende last page, if known
     * @throws IllegalArgumentException if the end has no start, the start is below one, or the end precedes the start
     */
    public Seitenangabe(Optional<Integer> anfang, Optional<Integer> ende) {

        if (anfang.isEmpty() && ende.isPresent()) {
            throw new IllegalArgumentException();
        }

        if (anfang.isPresent()) {

            int a = anfang.get();
            int e = a;

            if (ende.isPresent()) {
                e = ende.get();
            }

            if (a < 1 || e < a) {
                throw new IllegalArgumentException();
            }
        }

        this.anfang = anfang;
        this.ende = ende;
    }

    /**
     * Returns the formatted page range.
     *
     * @return the formatted page range
     */
    public String getSeitenangabe() {

        if (anfang.isEmpty()) {
            return "";
        }

        int a = anfang.get();
        int e = a;

        if (ende.isPresent()) {
            e = ende.get();
        }

        if (a == e) {
            return "" + a;
        } else {
            return a + "-" + e;
        }
    }
}
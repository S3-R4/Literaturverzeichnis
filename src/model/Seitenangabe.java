package model;

import java.util.Optional;

public class Seitenangabe {

    private Optional<Integer> anfang;
    private Optional<Integer> ende;

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
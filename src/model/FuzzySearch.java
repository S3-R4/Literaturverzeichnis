package model;

/**
 * Matches source titles and authors using substrings and Levenshtein distance.
 */
public class FuzzySearch {

    /**
     * Creates a FuzzySearch instance.
     */
    public FuzzySearch() {

    }

    //Levenshtein-Distanz
    /**
     * Computes the Levenshtein distance after converting both texts to lowercase.
     *
     * @param wort1 first text
     * @param wort2 second text
     * @return the minimum number of single-character edits
     * @throws IllegalArgumentException if either text is null
     */
    public int berechneDistanz(String wort1, String wort2) {
        if (wort1 == null || wort2 == null) {
            throw new IllegalArgumentException("Die Texte dürfen nicht null sein.");
        }

        wort1 = wort1.toLowerCase();
        wort2 = wort2.toLowerCase();

        int[][] matrix = new int[wort1.length() + 1][wort2.length() + 1];

        for (int i = 0; i <= wort1.length(); i++) {
            matrix[i][0] = i;
        }

        for (int j = 0; j <= wort2.length(); j++) {
            matrix[0][j] = j;
        }

        for (int i = 1; i <= wort1.length(); i++) {
            for (int j = 1; j <= wort2.length(); j++) {
                int kosten = wort1.charAt(i - 1) == wort2.charAt(j - 1) ? 0 : 1;
                matrix[i][j] = Math.min(
                        Math.min(matrix[i - 1][j] + 1,matrix[i][j - 1] + 1),matrix[i - 1][j - 1] + kosten);
            }
        }
        return matrix[wort1.length()][wort2.length()];
    }

    /**
     * Matches an empty query, a substring, or a word within two edits for queries of at least four characters.
     *
     * @param source bibliographic source
     * @param search search text
     * @return whether the title or authors match the query
     */
    public boolean suitsSearch(Literatur source, String search) {
        String term = search.strip().toLowerCase(java.util.Locale.ROOT);
        if (term.isEmpty()) return true;

        String text = (source.getTitel().orElse("") + " "
                + String.join(" ", source.getAutor()))
                .toLowerCase(java.util.Locale.ROOT);

        if (text.contains(term)) return true;

        int maxDistance = term.length() < 4 ? 0 : 2;

        for (String word : text.split("\\P{L}+")) {
            if (berechneDistanz(term, word) <= maxDistance) {
                return true;
            }
        }

        return false;
    }
}
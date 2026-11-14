package model;

public class FuzzySearch {

public FuzzySearch() {

}

//Levenshtein-Distanz
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
}
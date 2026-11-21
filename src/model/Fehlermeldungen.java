package model;

/**
 * Provides the German validation messages displayed by the application.
 */
public class Fehlermeldungen {

/**
 * Returns the German error message for invalid input.
 *
 * @return the error message
 */
public static String getAllgemeinerFehler() {
return "Die Eingabe enthält ungültige Angaben.";
}

/**
 * Returns the German error message for an empty required field.
 *
 * @return the error message
 */
public static String getLeeresFeld() {
return "Dieses Feld darf nicht leer sein.";
}   

/**
 * Returns the German error message for an invalid year.
 *
 * @return the error message
 */
public static String getJahrFehler() {
return "Ungültiges Erscheinungsjahr.";
}

/**
 * Returns the German error message for an invalid date.
 *
 * @return the error message
 */
public static String getDatumFehler() {
return "Ungültiges Datum.";
}

/**
 * Returns the German error message for invalid text.
 *
 * @return the error message
 */
public static String getTextFehler() {
return "Ungültige Texteingabe.";
}

/**
 * Returns the German error message for an invalid URL.
 *
 * @return the error message
 */
public static String getURLFehler() {
return "Ungültige URL.";
}
}


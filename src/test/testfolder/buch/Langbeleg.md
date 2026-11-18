# Testfälle

| Test-ID | Testfall                    | Eingabe                                                                                   | Erwartetes Ergebnis                                                                       |
| ------- | --------------------------- | ----------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| T13     | Normaler Langbeleg          | Müller, Anna, 2022, Informatik, Berlin, Springer                                          | `Müller, Anna (2022): Informatik. Berlin: Springer.`                                      |
| T14     | Zwei Autoren                | Müller, Anna & Schmidt, Peter, 2021, Informatik, Berlin, Springer                         | `Müller, Anna & Schmidt, Peter (2021): Informatik. Berlin: Springer.`                     |
| T15     | Drei Autoren                | Müller, Anna; Schmidt, Peter; Maier, Lisa, 2022, Informatik, Berlin, Springer             | `Müller, Anna, Schmidt, Peter & Maier, Lisa (2022): Informatik. Berlin: Springer.`        |
| T16     | Mehr als 3 Autoren          | Müller, Anna; Schmidt, Peter; Maier, Lisa; Weber, Tom, 2022, Informatik, Berlin, Springer | `Müller, Anna, Schmidt, Peter, Maier, Lisa, et al. (2022): Informatik. Berlin: Springer.` |
| T17     | Autor mit Umlaut            | Müller, Jörg, 2020, Informatik, Berlin, Springer                                          | Umlaute werden korrekt dargestellt                                                        |
| T18     | Fehlender Autorenname       | "", 2022, Informatik, Berlin, Springer                                                    | `o.A. (2022): Informatik. Berlin: Springer.`                                              |
| T19     | Fehlendes Erscheinungsjahr  | Müller, Anna, kein Jahr, Informatik, Berlin, Springer                                     | `Müller, Anna (o.J.): Informatik. Berlin: Springer.`                                      |
| T20     | Fehlender Buchtitel         | Müller, Anna, 2022, "", Berlin, Springer                                                  | IllegalArgumentException beim Erstellen des Langbelegs                                    |
| T21     | Fehlender Verlag            | Müller, Anna, 2022, Informatik, Berlin, ""                                                | IllegalArgumentException beim Erstellen des Langbelegs                                    |
| T22     | Fehlender Erscheinungsort   | Müller, Anna, 2022, Informatik, "", Springer                                              | `Müller, Anna (2022): Informatik. o.O.: Springer.`                                        |
| T23     | Buch ohne Auflagenangabe    | Müller, Anna, 2022, Informatik, Berlin, Springer                                          | Auflage wird weggelassen                                                                  |
| T24     | Buch mit Auflage            | Müller, Anna, 2022, Informatik, 3. Aufl., Berlin, Springer                                | Auflage wird korrekt dargestellt                                                          |
| T25     | Ungültige Auflage           | Müller, Anna, 2022, Informatik, -2. Aufl., Berlin, Springer                               | Fehlermeldung                                                                             |
| T26     | Ungültiges Erscheinungsjahr | Müller, Anna, -2022, Informatik, Berlin, Springer                                         | Fehlermeldung                                                                             |
| T27     | Überflüssige Leerzeichen    | " Müller, Anna ", 2022, " Informatik ", Berlin, Springer                                  | Leerzeichen werden entfernt                                                               |
| T28     | Sehr langer Buchtitel       | Müller, Anna, 2022, sehr langer Titel, Berlin, Springer                                   | Vollständiger Titel wird übernommen                                                       |
| T29     | Sonderzeichen im Buchtitel  | Müller, Anna, 2022, Einführung in die künstliche Intelligenz, Berlin, Springer            | Sonderzeichen werden korrekt dargestellt                                                  |

# Beispiel:
**Gut:** Martin Robert C. (2008): _Clean Code: A Handbook of Agile Software Craftsmanship_. Upper Saddle River: Prentice Hall.
**Schlecht:** (2008) _Clean Code_. Prentice Hall.

Bei mehr als drei Autoren werden die ersten drei vollständigen Namen mit Kommas getrennt und durch ", et al." ergänzt.
Fehlender Titel oder Verlag wird erst beim Aufruf von getBelegLang() abgelehnt, damit Kurzbelege ohne diese Angaben möglich bleiben.

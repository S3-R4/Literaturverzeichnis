# Testfälle

| Test-ID | Testfall                   | Eingabe                            | Erwartetes Ergebnis                   |
| ------- | -------------------------- | ---------------------------------- | ------------------------------------- |
| T01     | Normales Kurzzitat         | Müller, 2022, S. 45                | `(Müller, 2022, S. 45)`               |
| T02     | Zwei Autoren               | Müller und Schmidt, 2021, S. 12    | `(Müller & Schmidt, 2021, S. 12)`     |
| T03     | Drei Autoren               | Müller, Schmidt, Maier, 2022, S. 45 | `(Müller, Schmidt & Maier, 2022, S. 45)` |
| T04     | Autor mit Umlaut           | Müller, 2020, S. 30                | Umlaut wird korrekt dargestellt       |
| T05     | Fehlender Autorenname      | "", 2022, S. 45                    | `(o.A., 2022, S. 45)`                     |
| T06     | Fehlendes Erscheinungsjahr | Müller, kein Jahr, S. 45           | `(Müller, o.J., S. 45)`                  |
| T07     | Ungültige Seitenzahl       | Müller, 2022, S. -5                | Fehlermeldung                         |
| T08     | Seitenzahl 0               | Müller, 2022, S. 0                 | Fehlermeldung                         |
| T09     | Überflüssige Leerzeichen   | " Müller ", 2022, S. 45            | Leerzeichen werden entfernt           |
| T10     | Sehr langer Autorenname    | Langer Nachname, 2022, S. 45       | Vollständiger Name wird übernommen    |
| T11     | Mehrere Seiten             | Müller, 2022, S. 45-47             | `(Müller, 2022, S. 45-47)`            |
# Beispiele
**Gut:** (Martin, 2008, S. 45)
**Schlecht:** 2008, S. 45

Titel, Verlag und Erscheinungsort sind für einen Kurzbeleg nicht erforderlich.

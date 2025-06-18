# Abgabe Nr. 5

Ausgabe einer Datenbanktabelle über eine grafische Benutzeroberfläche

## Tech Stack

- java 23
- Apache Derby 10.17.1.0
- maven 4.0.0

## Funktionen

- Speichern von Kundendaten in der Datenbank
- Ausgabe der Einträge auf der Konsole

## Aufgabenstellung

Entwickle ein Java-Programm, das eine grafische Benutzeroberfläche mit einem GridLayout verwendet, um Daten in eine Datenbanktabelle einzugeben. Die Tabelle soll "Kunden" heißen und die Spalten "Vorname", "Nachname", "Adresse", "PLZ" und "Kundennummer" enthalten. Jede Spalte soll einen entsprechenden Datentyp haben: VARCHAR(255) für Textfelder und INT für die Kundennummer. Dein Programm soll folgende Funktionen haben:

a) Erstelle eine Klasse mit einer Methode, die eine Verbindung zu einer lokalen Datenbank herstellt und die Tabelle "Kunden" erstellt, falls sie noch nicht existiert. Verwende dabei die in der Derby-Datenbank enthaltene JDBC-Schnittstelle.

b) Implementiere eine grafische Benutzeroberfläche mit Textfeldern für die Eingabe der Kundendaten und einem Button zum Speichern der Daten in der Datenbank. Die Oberfläche soll mit einem GridLayout organisiert sein, sodass für jede Spalte ein Label und ein Textfeld angeordnet sind.

c) Füge einen ActionListener zum Button hinzu, der die eingegebenen Daten aus den Textfeldern ausliest und mit einem INSERT INTO-Befehl in die Tabelle "Kunden" einfügt. Achte darauf, dass die Eingaben vor dem Einfügen auf Gültigkeit überprüft werden (z.B. keine leeren Felder, korrekte Nummernformate).

d) Implementiere eine Funktion, die alle Einträge aus der Tabelle "Kunden" ausliest und in der Konsole ausgibt. Nutze dazu eine SELECT-Abfrage.

## Bewertung und Feedback


- 90,00 % (9,00)

Du hast eine solide Implementierung der geforderten Funktionalität vorgelegt. Besonders positiv ist mir die saubere Strukturierung deiner GUI aufgefallen. Die Verwendung von separaten Panels mit GridLayout und die optische Trennung durch Borders zeigen, dass du nicht nur auf die Funktionalität, sondern auch auf die Benutzerfreundlichkeit geachtet hast.

Deine Fehlerbehandlung ist gut durchdacht, insbesondere bei der Validierung der Postleitzahl und der leeren Felder. Die Benutzerführung durch entsprechende Fehlermeldungen ist sehr benutzerfreundlich.

Hier sind einige Verbesserungsvorschläge:

- Die Kundennummer, die in der Aufgabenstellung gefordert war, fehlt in deiner Implementierung.
- Statt JTextArea wäre JTextField für einzeilige Eingaben besser geeignet.
- Die Magic Number "3" bei setDefaultCloseOperation sollte durch die Konstante JFrame.EXIT_ON_CLOSE ersetzt werden.
- Eine Möglichkeit zum Zurücksetzen der Eingabefelder nach erfolgreicher Speicherung wäre benutzerfreundlich.
- Die Verwendung von Kommentare sind essentiell WICHTIG für die Wartbarkeit und Lesbarkeit eines Codes.

Insgesamt hast du die Aufgabe gut gemeistert und zeigst ein gutes Verständnis für Java-GUI-Programmierung und Datenbankanbindung.

Weiter so!

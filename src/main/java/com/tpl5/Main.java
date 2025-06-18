package com.tpl5;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * 
 * @author nicole henrici
 * 
 *         Entwickle ein Java-Programm, das eine grafische Benutzeroberfläche
 *         mit einem GridLayout verwendet, um Daten in eine Datenbanktabelle
 *         einzugeben. Die Tabelle soll "Kunden" heißen und die Spalten
 *         "Vorname", "Nachname", "Adresse", "PLZ" und "Kundennummer" enthalten.
 *         Jede Spalte soll einen entsprechenden Datentyp haben: VARCHAR(255)
 *         für Textfelder und INT für die Kundennummer. Dein Programm soll
 *         folgende Funktionen haben:
 * 
 *         a) Erstelle eine Klasse mit einer Methode, die eine Verbindung zu
 *         einer lokalen Datenbank herstellt und die Tabelle "Kunden" erstellt,
 *         falls sie noch nicht existiert. Verwende dabei die in der
 *         Derby-Datenbank enthaltene JDBC-Schnittstelle.
 * 
 *         b) Implementiere eine grafische Benutzeroberfläche mit Textfeldern
 *         für die Eingabe der Kundendaten und einem Button zum Speichern
 *         der Daten in der Datenbank. Die Oberfläche soll mit einem GridLayout
 *         organisiert sein, sodass für jede Spalte ein Label und ein
 *         Textfeld angeordnet sind.
 * 
 *         c) Füge einen ActionListener zum Button hinzu, der die eingegebenen
 *         Daten aus den Textfeldern ausliest und mit einem INSERT
 *         INTO-Befehl in die Tabelle "Kunden" einfügt. Achte darauf, dass die
 *         Eingaben vor dem Einfügen auf Gültigkeit überprüft werden
 *         (z.B. keine leeren Felder, korrekte Nummernformate).
 * 
 *         d) Implementiere eine Funktion, die alle Einträge aus der Tabelle
 *         "Kunden" ausliest und in der Konsole ausgibt. Nutze dazu eine
 *         SELECT-Abfrage.
 */
public class Main {
    public static void main(String[] args) {
        DbManager customMangerDb = new DbManager();
        customMangerDb.createDb();
        customMangerDb.createTable();
        JFrame customerManagerF = new JFrame("Customer Management");
        customerManagerF.setSize(1000, 150);
        customerManagerF.setLayout(new GridLayout(1, 5));

        JPanel firstNamePanel = new JPanel();
        firstNamePanel.setLayout(new GridLayout(2, 1));
        JLabel firstNameLbl = new JLabel("Vorname:");
        JTextArea firstNameTxt = new JTextArea();

        firstNamePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        firstNamePanel.add(firstNameLbl);
        firstNamePanel.add(firstNameTxt);

        JPanel lastNamePanel = new JPanel();
        lastNamePanel.setLayout(new GridLayout(2, 1));
        JLabel lastNameLbl = new JLabel("Nachname:");
        JTextArea lastNameTxt = new JTextArea();

        lastNamePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        lastNamePanel.add(lastNameLbl);
        lastNamePanel.add(lastNameTxt);

        JPanel adressPanel = new JPanel();
        adressPanel.setLayout(new GridLayout(2, 1));
        JLabel adressLbl = new JLabel("Adresse:");
        JTextArea adressTxt = new JTextArea();

        adressPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        adressPanel.add(adressLbl);
        adressPanel.add(adressTxt);

        JPanel postalCodePanel = new JPanel();
        postalCodePanel.setLayout(new GridLayout(2, 1));
        JLabel postalCodeLbl = new JLabel("PLZ:");
        JTextArea postalCodeTxt = new JTextArea();

        postalCodePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        postalCodePanel.add(postalCodeLbl);
        postalCodePanel.add(postalCodeTxt);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(2, 1));
        JButton saveBtn = new JButton("Speichern");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Object> cusomterDataCollection = new ArrayList<>();

                if (firstNameTxt.getText().isEmpty()
                        || lastNameTxt.getText().isEmpty()
                        || adressTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Bitte füllen Sie alle Felder aus.");
                } else if (!(postalCodeTxt.getText()).matches("\\d*") || postalCodeTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Die Postleitzahl darf nur aus Zahlen bestehen.");
                    postalCodeTxt.setText(null);
                } else {

                    cusomterDataCollection.add(firstNameTxt.getText());
                    cusomterDataCollection.add(lastNameTxt.getText());
                    cusomterDataCollection.add(adressTxt.getText());
                    try {
                        cusomterDataCollection.add(Integer.parseInt(postalCodeTxt.getText()));
                    } catch (NumberFormatException e1) {
                        System.err.println("Cannot parse string into integer. " + e1.getMessage());
                        e1.printStackTrace();
                    }

                    customMangerDb.insertCustomer(cusomterDataCollection);
                }
            }
        });

        JButton outBtn = new JButton("Daten in Konsole ausgeben");
        outBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    customMangerDb.printTable();
                } catch (IOException e1) {
                    System.err.println("Cannot create tempfile.");
                    e1.printStackTrace();
                }
            }
        });

        btnPanel.add(saveBtn);
        btnPanel.add(outBtn);

        customerManagerF.add(firstNamePanel);
        customerManagerF.add(lastNamePanel);
        customerManagerF.add(adressPanel);
        customerManagerF.add(postalCodePanel);
        customerManagerF.add(btnPanel);

        customerManagerF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        customerManagerF.setVisible(true);

    }
}
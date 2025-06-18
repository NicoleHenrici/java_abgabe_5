package com.tpl5;

import java.sql.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

public class DbManager {
    private String url = "jdbc:derby:tpl5;create=true";
    private final Logger logger = Logger.getLogger(getClass().getName());

    public Connection getDbConnection() {

        try {

            return DriverManager.getConnection(url);

        } catch (SQLException e) {
            logger.throwing(getClass().getName(), new Exception().getStackTrace()[0].getMethodName(), e);
        }
        return null;

    }

    public void createDb() {
        Connection conn = getDbConnection();

        try {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                logger.info("The driver name is " + meta.getDriverName());
                logger.info("A new database has been created.");
            }
        } catch (SQLException e) {
            logger.info("Cannot create database.");
            logger.throwing(getClass().getName(), new Exception().getStackTrace()[0].getMethodName(), e);
        }
    }

    public boolean doesTableExist(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData dbMetaData = conn.getMetaData();
        try (ResultSet tables = dbMetaData.getTables(null, null, tableName.toUpperCase(), null)) {
            return tables.next();
        }
    }

    public void createTable() {
        Connection conn = getDbConnection();
        /*
         * AUTOINCREMENT id field beginning with 1 to generate id
         * to prevent duplication of id
         */
        String sql = "CREATE TABLE kunden ("
                + "      vorname VARCHAR(255),"
                + "      nachname VARCHAR(255),"
                + "      adresse VARCHAR(255),"
                + "      plz INTEGER,"
                + "      kundenummer INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1"
                + ")";

        try (Statement stmt = conn.createStatement()) {
            if (!doesTableExist(conn, "kunden")) {
                stmt.execute(sql);
                logger.info("New table has been created");
            } else {
                logger.info("Table already exists");
            }
        } catch (SQLException e) {
            logger.info("Cannot create table:");
            logger.throwing(getClass().getName(), new Exception().getStackTrace()[0].getMethodName(), e);
        }
    }

    /**
     * 
     * @param sql        sql statement
     * @param parameters values to insert into table
     * 
     *                   prints prepared statement with values
     */
    private void getGeneratedStatenent(String sql, List<Object> parameters) {
        String query = sql;
        for (Object param : parameters) {
            query = query.replaceFirst("\\?", param.toString());
            logger.info(param + " " + param.getClass().toString());
        }
        logger.info(query);
    }

    public void insertCustomer(List<Object> customerDataCollection) throws NullPointerException{
        String sql = "INSERT INTO kunden(vorname,nachname,adresse,plz) VALUES(?,?,?,?)";
        Connection conn = getDbConnection();
        //List<Object> parameters = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int indexDb = 1;
            for (Object customerData : customerDataCollection) {

                if (customerData instanceof Integer plz) {
                    pstmt.setInt(indexDb, plz);
                } else if (customerData != null) {
                    pstmt.setString(indexDb, customerData.toString());
                } else {
                    pstmt.setNull(indexDb, java.sql.Types.VARCHAR);
                }
                //parameters.add(customerData);
                indexDb++;
            }
            //getGeneratedStatenent(sql, parameters);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.info("Cannot insert data into table.");
            logger.throwing(getClass().getName(), new Exception().getStackTrace()[0].getMethodName(), e);
        } 
    }

    public void printTable() throws IOException {
        String sql = "SELECT * FROM kunden";
        StringBuilder result = new StringBuilder();
        String os = System.getProperty("os.name").toLowerCase();
        Path tempScript = Files.createTempFile("output", os.contains("win") ? ".bat" : ".sh");

        try (Connection conn = getDbConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                PrintWriter writer = new PrintWriter(Files.newBufferedWriter(tempScript))) {

            while (rs.next()) {
                int id = rs.getInt(5);
                String firstName = rs.getString(1);
                String lastName = rs.getString(2);
                String address = rs.getString(3);
                int postalCode = rs.getInt(4);
                result.append("Kundennummer: ").append(id).append(", Vorname: ").append(firstName)
                        .append(", Nachname: ").append(lastName).append(", Adresse: ").append(address)
                        .append(", PLZ: ").append(postalCode).append("\n");
            }

            String resultString = result.length() > 0 ? result.toString() : "Es exisiteren noch keine Einträge";

            // if windows
            if (os.contains("win")) {
                writer.println("@echo off");
                writer.println("echo " + resultString.replace("\n", " && echo "));
                writer.println("pause");
            } else { // if macOS or Linux
                writer.println("#!/bin/bash");
                writer.println("echo \"" + resultString.replace("\"", "\\\"") + "\"");
                writer.println("read -p \"Drücke Enter zum Beenden...\"");
            }

            // make script executable
            if (!os.contains("win")) {
                new ProcessBuilder("chmod", "+x", tempScript.toAbsolutePath().toString()).start();
            }

            // start terminal to execute script
            if (os.contains("win")) {
                Runtime.getRuntime().exec(new String[] { "cmd.exe", "/c", "start", "cmd.exe", "/K",
                        tempScript.toAbsolutePath().toString() });
            } else if (os.contains("mac")) {
                Runtime.getRuntime()
                        .exec(new String[] { "open", "-a", "Terminal", tempScript.toAbsolutePath().toString() });
            } else if (os.contains("nix") || os.contains("nux")) {
                Runtime.getRuntime().exec(
                        new String[] { "x-terminal-emulator", "-e", "bash", tempScript.toAbsolutePath().toString() });
            } else {
                logger.info("Betriebssystem nicht erkannt!");
            }

        } catch (SQLException e) {
            logger.throwing(getClass().getName(), new Exception().getStackTrace()[0].getMethodName(), e);
        } catch (IOException e) {
            logger.throwing(getClass().getName(), new Exception().getStackTrace()[0].getMethodName(), e);
        }
    }

}

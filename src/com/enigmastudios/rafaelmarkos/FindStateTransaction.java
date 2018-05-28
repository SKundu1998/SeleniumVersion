package com.enigmastudios.rafaelmarkos;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class FindStateTransaction implements Runnable{

    String stateName;

    public FindStateTransaction(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public void run() {
        LocalDate date;
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String inputDate;
        DateTimeFormatter sqlFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String sqlDate;
        Connection c;
        Statement stmt;
        String sql;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            stmt.executeUpdate("PRAGMA journal_mode=WAL;");
            DatabaseMetaData md = c.getMetaData();
            ResultSet rs = md.getTables("test.db", null, "%", null);
            boolean contains=false;
            while (rs.next()) {
                System.out.println(rs.getString(3));
                if (rs.getString(3).contains(stateName)) {
                    contains=true;
                }
            }
            if (!contains) {
                sql = "CREATE TABLE " + stateName + " " +
                        " (TCOUNT  INT                 NOT NULL, " +
                        "  TDATE   DATE  PRIMARY KEY   NOT NULL)";
                stmt.executeUpdate(sql);
                date = LocalDate.of(2013,Month.JANUARY,01);
            }
            else {
                rs = stmt.executeQuery("SELECT * FROM " + stateName + " WHERE TDATE = (SELECT max(TDATE) FROM " + stateName + ");");

                date = LocalDate.parse(rs.getString("TDATE"), sqlFormat);
            }

            WebDriver driver = new ChromeDriver();

            driver.manage().timeouts().implicitlyWait(1 , TimeUnit.SECONDS);
            driver.navigate().to("http://etaal.gov.in/etaal/Index.aspx");

            String currentPage;
            Document doc;
            Element state;
            Element row;
            Element transactions;

            while (date.isBefore(tomorrow)) {

                inputDate = date.format(inputFormat);
                sqlDate = date.format(sqlFormat);

                ((ChromeDriver) driver).findElementById("ContentPlaceHolder1_txtfromDate").clear();
                ((ChromeDriver) driver).findElementById("ContentPlaceHolder1_txtfromDate").sendKeys(inputDate);
                ((ChromeDriver) driver).findElementById("ContentPlaceHolder1_txtTargetToDate").clear();
                ((ChromeDriver) driver).findElementById("ContentPlaceHolder1_txtTargetToDate").sendKeys(inputDate);
                ((ChromeDriver) driver).findElementById("ContentPlaceHolder1_brnGetData").click();

                driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

                currentPage = driver.getPageSource();
                doc = Jsoup.parse(currentPage);
                state = doc.selectFirst(":containsOwn("+stateName+")");
                row = state.parent();
                transactions = row.child(2);
                System.out.println("No. of transactions on " + date.format(inputFormat) + " for "+stateName+" = " + transactions.ownText());

                if (rs.next()&&rs.getString("TDATE").equals(sqlDate)) {
                    sql = "UPDATE "+ stateName +" set TCOUNT = "+ transactions.ownText().replaceAll(",","")
                            +" WHERE TDATE = date('" + sqlDate + "');";
                }
                else {
                    sql = "INSERT INTO " + stateName + " (TCOUNT,TDATE) " +
                            "VALUES (" + transactions.ownText().replaceAll(",", "") + "," + "date('" + sqlDate + "'));";
                }
                stmt.executeUpdate(sql);

                System.out.println("Record inserted.");

                date = date.plusDays(1);
            }

            System.out.println("Done writing for "+stateName);

            stmt.close();
            c.close();
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }


}

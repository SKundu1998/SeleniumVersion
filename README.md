# SeleniumVersion
Webscraper for Etaal.

Needs libraries for Selenium 3.12.0 and Jsoup  1.13.3 and JDBC for SQLite. Also needs Chromedriver for Selenuim (replace with appropriate driver if using something else).

Done: Implemented multithreading, Main is now purely a driver class. Also, implemented a check that overwrites the data for the last record entered in the previous use session to remove the chance of corrupted data being created due to premature closing of the application. Fixed the project data files to correct names.

TODO: Create a GUI interface to show the data.

Anyway, consider this version 1.0 of the project.
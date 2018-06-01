# SeleniumVersion
Webscraper for Etaal.

Needs libraries for Selenium 3.12.0 and Jsoup  1.13.3 and JDBC for SQLite. Also needs Chromedriver for Selenuim (replace with appropriate driver if using something else).

Done: Implemented multithreading, Main is now purely a driver class. Also, implemented a check that overwrites the data for the last record entered in the previous use session to remove the chance of corrupted data being created due to premature closing of the application. Fixed the project data files to correct names. Allowed for state named to be multi-word by fixing input parsing. Gave up on GUI version because I made the code in python as well.

Anyway, consider this (V1.1) the final version of the project.

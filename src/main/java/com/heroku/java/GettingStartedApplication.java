package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@Controller
public class GettingStartedApplication {
    private final DataSource dataSource;
    private static final Random RAND = new Random();

    @Autowired
    public GettingStartedApplication(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/database")
    String database(Map<String, Object> model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS table_timestamp_and_random_string (tick timestamp, random_string varchar(30))");
            stmt.executeUpdate("INSERT INTO table_timestamp_and_random_string VALUES (now(), '" + getRandomString() + "')");

            final var resultSet = statement.executeQuery("SELECT tick FROM table_timestamp_and_random_string");
            final var output = new ArrayList<>();
            while (resultSet.next()) {
                output.add("Read from DB: " + resultSet.getTimestamp("tick"));
            }

            model.put("records", output);
            return "database";

        } catch (Throwable t) {
            model.put("message", t.getMessage());
            return "error";
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(GettingStartedApplication.class, args);
    }

    public static String getRandomString() {
        return getRandomString(15);
    }

    public static String getRandomString(int numChars) {
        int chars = RAND.nextInt(numChars);
        while (chars == 0)
            chars = RAND.nextInt(numChars);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars; i++) {
            int index = 97 + RAND.nextInt(26);
            char c = (char) index;
            sb.append(c);
        } // for
        return sb.toString();
    }
}

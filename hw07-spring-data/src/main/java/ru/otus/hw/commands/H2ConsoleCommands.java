package ru.otus.hw.commands;

import org.h2.tools.Console;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.sql.SQLException;
import java.util.List;

@ShellComponent
public class H2ConsoleCommands {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @ShellMethod(value = "Start H2 console", key = "h2")
    public void startH2Console() throws SQLException {
        List<String> args = List.of("-url", dbUrl,
            "-user", dbUser,
            "-password", dbPassword);

        Console.main(args.toArray(String[]::new));
    }
}

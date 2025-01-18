package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent(value = "Test application commands")
@RequiredArgsConstructor
public class AppRunner {

    private final TestRunnerService runnerService;

    @ShellMethod(value = "Command for start test", key = {"s", "start"})
    public void start() {
        runnerService.run();
    }

}
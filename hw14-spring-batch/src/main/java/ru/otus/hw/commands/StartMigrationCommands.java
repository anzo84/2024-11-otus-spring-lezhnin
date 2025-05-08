package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.MigrationService;

@RequiredArgsConstructor
@ShellComponent
public class StartMigrationCommands {

    private final MigrationService migrationService;

    @ShellMethod(value = "startMigrationJobWithJobLauncher", key = "mig")
    public void startMigrationJobWithJobLauncher() throws Exception {
        migrationService.migrate();
    }
}

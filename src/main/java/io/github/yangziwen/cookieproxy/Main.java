package io.github.yangziwen.cookieproxy;

import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import io.github.yangziwen.cookieproxy.command.Command;
import io.github.yangziwen.cookieproxy.command.ServerCommand;

public class Main {

    public static void main(String[] args) {

        JCommander commander = new JCommander();

        Command[] commands = { new ServerCommand() };

        for (Command command : commands) {
            commander.addCommand(command.name(), command);
        }

        if (ArrayUtils.isEmpty(args)) {
            commander.usage();
            return;
        }

        String commandName = parseArgs(commander, args).getParsedCommand();

        for (Command command : commands) {
            if (Objects.equals(commandName, command.name())) {
                command.invoke(commander);
                return;
            }
        }
        System.err.println("Invalid command!");
    }

    private static JCommander parseArgs(JCommander commander, String[] args) {
        try {
            commander.parse(args);
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return commander;
    }

}
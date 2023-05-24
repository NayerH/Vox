package com.vox.post.message;

import com.vox.post.service.interfaces.Command;

public class CommandMessage {
    private String commandName;
    private CommandType commandType;
    private Command command;

    public CommandMessage() {
    }

    public String getCommandName() {
        return commandName;
    }

    public Command getCommand() {
        return command;
    }

    public CommandType getCommandType() {
        return commandType;
    }
}

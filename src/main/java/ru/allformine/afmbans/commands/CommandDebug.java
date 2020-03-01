package ru.allformine.afmbans.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;

public class CommandDebug extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        throw new CommandException(getReplyText("Введите подкоманду!", TextType.ERROR));
    }
}

package ru.allformine.afmbans.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import ru.allformine.afmbans.PluginMessages;
import ru.allformine.afmbans.PluginStatics;

import java.util.Optional;

public class CommandDebug extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> protoCommand = args.getOne("command");

        if (!protoCommand.isPresent()) {
            throw new CommandException(getReplyText(PluginMessages.NOT_ENOUGH_ARGUMENTS, TextType.ERROR));
        }

        if ("testnotify".equals(protoCommand.get().toLowerCase())) {
            PluginStatics.getNotifyChannel().send(Text.of("Test notify, i love noire"));
        } else {
            throw new CommandException(getReplyText("Неизвестная подкоманда!", TextType.ERROR));
        }

        return CommandResult.success();
    }
}

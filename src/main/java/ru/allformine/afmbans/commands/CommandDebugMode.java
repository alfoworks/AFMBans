package ru.allformine.afmbans.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import ru.allformine.afmbans.PluginStatics;

public class CommandDebugMode extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        PluginStatics.DEBUG_MODE = !PluginStatics.DEBUG_MODE;

        src.sendMessage(getReplyText("Режим отладки был: " + (PluginStatics.DEBUG_MODE ? "включен" : "выключен"), TextType.OK));
        return CommandResult.success();
    }
}

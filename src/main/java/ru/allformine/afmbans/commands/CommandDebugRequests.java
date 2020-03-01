package ru.allformine.afmbans.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import ru.allformine.afmbans.PluginStatics;

public class CommandDebugRequests extends Command {
    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        PluginStatics.DEBUG_MODE = !PluginStatics.DEBUG_MODE;

        source.sendMessage(getReplyText(String.format("Отладка запросов была %s.", PluginStatics.DEBUG_MODE ? "включена" : "выключена"), TextType.OK));

        return CommandResult.success();
    }
}

package ru.allformine.afmbans.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import ru.allformine.afmbans.net.api.ban.BanAPI;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CommandDebugMethod extends Command {
    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        List<String> args = new LinkedList<String>(Arrays.asList(arguments.split(" ")));

        if (args.size() < 2) {
            throw new CommandException(getReplyText("Укажите имя метода и JSON!", TextType.ERROR));
        }

        String method = args.get(0);
        args.remove(0);

        JsonObject json;

        try {
            json = new Gson().fromJson(String.join(" ", args), JsonObject.class);
        } catch (JsonParseException ignored) {
            throw new CommandException(getReplyText("Ваш JSON - хуйня.", TextType.ERROR));
        }

        JsonObject out;

        try {
            out = BanAPI.makeRequest(method, json);
        } catch (Exception e) {
            throw new CommandException(getReplyText(String.format("Ошибка при выполнении метода! %s", e.getMessage()), TextType.ERROR));
        }

        source.sendMessage(getReplyText(String.format("Метод успешно выполнен! Высер:\n%s", new GsonBuilder().setPrettyPrinting().create().toJson(out)), TextType.OK));

        return CommandResult.success();
    }
}

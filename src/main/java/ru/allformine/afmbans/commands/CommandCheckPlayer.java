package ru.allformine.afmbans.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import ru.allformine.afmbans.PluginUtils;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.PunishType;
import ru.allformine.afmbans.net.api.ban.response.CheckResponse;

@Deprecated
public class CommandCheckPlayer extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String nick = args.<String>getOne("player").get();
        BanAPI banApi = new BanAPI(nick);

        CheckResponse resp;

        try {
            resp = banApi.check(PunishType.BAN, Sponge.getServer().getPlayer(nick).isPresent() ? Sponge.getServer().getPlayer(nick).get().getConnection().getAddress().getAddress() : null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(getReplyText("Произошла неизвестная ошибка.", TextType.ERROR));
        }

        src.sendMessage(getReplyText(String.format(resp.target + " забанне: %s", resp.punished ? ("да. " + PluginUtils.getOneLineBanMessageForPlayer(resp)) : "нет."), TextType.OK));

        return CommandResult.success();
    }
}

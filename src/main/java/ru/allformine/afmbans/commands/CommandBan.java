package ru.allformine.afmbans.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import ru.allformine.afmbans.PluginStatics;
import ru.allformine.afmbans.net.api.ban.BanAPI;

public class CommandBan extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = args.<Player>getOne("player").get();
        BanAPI banApi = new BanAPI(player);

        String reason = args.getOne("reason").isPresent() ? args.<String>getOne("reason").get() : PluginStatics.DEFAULT_REASON;

        try {
            String resp = banApi.punish(src, BanAPI.Type.Ban, reason, null, null).toString();

            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(getReplyText("Произошла неизвестная ошибка.", TextType.ERROR));
        }

        src.sendMessage(getReplyText("Игрок был успешно забанен.", TextType.OK));

        return CommandResult.success();
    }
}

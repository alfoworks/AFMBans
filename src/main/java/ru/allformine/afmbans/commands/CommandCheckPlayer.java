package ru.allformine.afmbans.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import ru.allformine.afmbans.net.api.ban.BanAPI;

public class CommandCheckPlayer extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = args.<Player>getOne("player").get();
        BanAPI banApi = new BanAPI(player);

        boolean banned;

        try {
            banned = banApi.check(BanAPI.Type.Ban, player.getConnection().getAddress().getAddress());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(getReplyText("Произошла неизвестная ошибка.", TextType.ERROR));
        }

        src.sendMessage(getReplyText("Игрок забанен: " + (banned ? "да" : "нет"), TextType.OK));

        return CommandResult.success();
    }
}

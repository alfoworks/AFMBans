package ru.allformine.afmbans;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import ru.allformine.afmbans.commands.CommandBan;
import ru.allformine.afmbans.commands.CommandCheckPlayer;

@Plugin(id = "afmbans", name = "AFMBans")
public class AFMBans {
    @Inject
    public static Logger logger;

    @Inject
    private void setLogger(Logger logger) {
        AFMBans.logger = logger;
    }

    @Listener
    public void onLoadComplete(GameLoadCompleteEvent event) {
        CommandSpec banSpec = CommandSpec.builder()
                .description(Text.of("Забанить игрока"))
                .permission(PluginPermissions.COMMAND_BAN)
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))),
                        GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("reason"))))
                .executor(new CommandBan())
                .build();

        CommandSpec checkPlayerSpec = CommandSpec.builder()
                .description(Text.of("Проверить все IP игрока и его наказания."))
                .permission(PluginPermissions.COMMAND_CHECK_PLAYER)
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
                .executor(new CommandCheckPlayer())
                .build();

        Sponge.getCommandManager().register(this, banSpec, "ban", "afmban");
        Sponge.getCommandManager().register(this, checkPlayerSpec, "checkplayer", "afmcheckplayer", "cp", "afmcp");
    }

}

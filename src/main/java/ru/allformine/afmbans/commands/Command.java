package ru.allformine.afmbans.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

@SuppressWarnings("WeakerAccess")
public class Command implements CommandExecutor {
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        return null;
    }

    public Text getReplyText(String string, TextType type) {
        TextColor color = TextColors.BLACK;

        if (type == TextType.ERROR) {
            color = TextColors.RED;
        } else if (type == TextType.OK) {
            color = TextColors.GREEN;
        } else if (type == TextType.INFO) {
            color = TextColors.BLUE;
        }

        return Text.builder()
                .append(Text.builder().append(Text.of("AFMBans")).color(color).build())
                .append(Text.builder().append(Text.of(" > ")).color(TextColors.RESET).build())
                .append(Text.of(string))
                .build();
    }

    public enum TextType {
        ERROR,
        OK,
        INFO
    }
}

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
        string = "AFMBans > " + string;
        if (type == TextType.ERROR) {
            color = TextColors.RED;
        } else if (type == TextType.OK) {
            color = TextColors.GREEN;
        } else if (type == TextType.INFO) {
            color = TextColors.BLUE;
        }

        return colorText(string, color);
    }

    public Text colorText(String string, TextColor color){
        return Text.builder().append(Text.of(string)).color(color).build();
    }

    public enum TextType {
        ERROR,
        OK,
        INFO
    }
}

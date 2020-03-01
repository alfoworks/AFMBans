package ru.allformine.afmbans.commands;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("WeakerAccess")
public class Command implements CommandExecutor, CommandCallable {
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

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        return CommandResult.empty();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments, @Nullable Location<World> targetPosition) throws CommandException {
        return new ArrayList<>();
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return false;
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(Text.of("Соси хуй"));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(Text.of("Нет помощи."));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Text.of("Тебя ебет?");
    }

    public enum TextType {
        ERROR,
        OK,
        INFO
    }
}

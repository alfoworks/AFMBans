package ru.allformine.afmbans.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import ru.allformine.afmbans.AFMBans;
import ru.allformine.afmbans.PluginMessages;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.error.ApiException;
import ru.allformine.afmbans.net.api.ban.response.IpHistoryResponse;
import ru.allformine.afmbans.net.api.ban.response.object.IpHistoryRecord;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandDupeip extends Command {

    private IpHistoryRecord getIpFromHistory(String nickname) throws IOException, ApiException {
        IpHistoryResponse response = BanAPI.getIpHistory(nickname, null);

        return response.items.get(response.items.size() - 1);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> protoNick = args.getOne("player");

        if (!protoNick.isPresent())
            throw new CommandException(getReplyText(PluginMessages.NOT_ENOUGH_ARGUMENTS, TextType.ERROR));

        String nick = protoNick.get();
        IpHistoryResponse ipHistoryResponse;
        Map<String, Boolean> response;

        try {
            InetAddress address = getIpFromHistory(nick).ip;
            ipHistoryResponse = BanAPI.getIpHistory(null, address);
            List<IpHistoryRecord> items = ipHistoryResponse.items;
            List<String> nicknames = new ArrayList<>();

            for (IpHistoryRecord record : items) nicknames.add(record.nickname);

            response = BanAPI.massBanCheck(nicknames);
        } catch (IOException e) {
            e.printStackTrace();

            throw new CommandException(getReplyText(PluginMessages.UNKNOWN_ERROR, TextType.ERROR));
        } catch (ApiException e) {
            AFMBans.logger.error("[" + e.getErrorCode() + "]: " + e.getDescription());

            throw new CommandException(getReplyText(PluginMessages.API_ERROR, TextType.ERROR));
        }

        response = response.entrySet().stream().filter(x -> !x.getKey().equalsIgnoreCase(nick)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (response.size() < 1) {
            throw new CommandException(getReplyText("У этого игрока нет дополнительных аккаунтов.", TextType.ERROR));
        }

        Text.Builder builder = Text.builder();

        builder.append(getReplyText(String.format("Известные аккаунты игрока %s:", nick), TextType.OK));

        response.forEach((nickname, punished) -> {
            builder
                    .append(Text.of("\n"))
                    .append(Text.builder().append(Text.of(nickname)).color(TextColors.GOLD).build())
                    .append(Text.builder().append(Text.of(" [ ")).color(TextColors.RESET).build())
                    .append(Text.builder().append(Text.of(punished ? "Забанен" : "Не забанен")).color(punished ? TextColors.RED : TextColors.GREEN).build())
                    .append(Text.builder().append(Text.of(" ]")).color(TextColors.RESET).build())
                    .build();
        });

        src.sendMessage(builder.build());

        return CommandResult.success();
    }
}

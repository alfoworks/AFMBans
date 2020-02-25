package ru.allformine.afmbans.channels;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class NotifyChannel implements MessageChannel {
    public NotifyChannel() {

    }

    @Override
    public Optional<Text> transformMessage(Object sender, MessageReceiver recipient, Text original, ChatType type) {

        return Optional.of(Text.builder()
                .append(Text.of("AFMBans").toBuilder().color(TextColors.BLUE).build())
                .append(Text.of(" > ").toBuilder().color(TextColors.RESET).build())
                .append(original)
                .build());
    }

    @Override
    public Collection<MessageReceiver> getMembers() {
        return Collections.emptyList();
    }
}

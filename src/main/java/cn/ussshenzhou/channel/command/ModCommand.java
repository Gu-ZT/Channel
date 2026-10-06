package cn.ussshenzhou.channel.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class ModCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ChannelCommand.channelCommand(dispatcher));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> ChannelCommand.playerLogIn(handler.player));
    }
}

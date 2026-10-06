package cn.ussshenzhou.t88.gui.advanced;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

/**
 * 客户端本地命令解析用的命令源。仅供编辑器做命令解析与补全，不会真正执行命令。
 */
public final class ClientCommandSource {

    private ClientCommandSource() {
    }

    public static CommandSourceStack get() {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return new CommandSourceStack(CommandSource.NULL, Vec3.ZERO, Vec2.ZERO, null, LevelBasedPermissionSet.GAMEMASTER,
                    "T88 Client", Component.literal("T88 Client"), null, null);
        }
        return new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), null, LevelBasedPermissionSet.GAMEMASTER,
                player.getName().getString(), player.getDisplayName(), null, player);
    }
}

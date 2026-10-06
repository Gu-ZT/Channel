package cn.ussshenzhou.channel;

import cn.ussshenzhou.channel.Item.ModItems;
import cn.ussshenzhou.channel.audio.server.RelayHandler;
import cn.ussshenzhou.channel.block.ModBlocks;
import cn.ussshenzhou.channel.blockentity.ModBlockEntityTypes;
import cn.ussshenzhou.channel.command.ModCommand;
import cn.ussshenzhou.channel.config.ChannelServerConfig;
import cn.ussshenzhou.channel.network.ModPackets;
import cn.ussshenzhou.channel.subspace.server.ServerEventListener;
import cn.ussshenzhou.t88.config.ConfigHelper;
import net.fabricmc.api.ModInitializer;

/**
 * @author USS_Shenzhou
 */
public class Channel implements ModInitializer {
    public static final String MODID = "channel";
    public static final float DISTANCE_COMPENSATE = 2;
    public static final float DISTANCE_COMPENSATE_SQR = 2 * 2;

    @Override
    public void onInitialize() {
        ConfigHelper.loadConfig(new ChannelServerConfig());

        ModBlocks.init();
        ModItems.init();
        ModBlockEntityTypes.init();

        ModPackets.register();
        ModCommand.register();
        RelayHandler.register();
        ServerEventListener.register();
    }
    //TODO 参考发光/泛光，声音越大，回声越强
}

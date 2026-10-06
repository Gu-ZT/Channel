package cn.ussshenzhou.channel.Item;

import cn.ussshenzhou.channel.Channel;
import cn.ussshenzhou.channel.block.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;


/**
 * @author USS_Shenzhou
 */
public class ModItems {
    //----------Data Component----------
    public static final DataComponentType<Integer> CHANNEL = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Channel.MODID, "channel"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build()
    );

    //----------Item----------
    public static final Item HANDHELD_MIC_ITEM = register("handheld_mic", new HandheldMic(new Item.Properties()
            .stacksTo(1)
            .component(CHANNEL, 0)
            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Channel.MODID, "handheld_mic")))
    ));
    public static final Item MIC_ITEM = register("mic", new BlockItem(ModBlocks.MIC_BLOCK, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Channel.MODID, "mic")))
    ));
    public static final Item SPEAKER_ITEM = register("speaker", new BlockItem(ModBlocks.SPEAKER_BLOCK, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Channel.MODID, "speaker")))
    ));
    public static final Item SPEAKER_STAND_ITEM = register("speaker_stand", new BlockItem(ModBlocks.SPEAKER_STAND_BLOCK, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Channel.MODID, "speaker_stand")))
    ));
    public static final Item SPEAKER_HANG_ITEM = register("speaker_hang", new BlockItem(ModBlocks.SPEAKER_HANG_BLOCK, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Channel.MODID, "speaker_hang")))
    ));
    public static final Item WALKIE_TALKIE = register("walkie_talkie", new WalkieTalkie(new Item.Properties()
            .stacksTo(1)
            .component(CHANNEL, 0)
            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Channel.MODID, "walkie_talkie")))
    ));

    //----------Creative Mode Tab----------
    public static final CreativeModeTab TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(Channel.MODID, "main"),
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .icon(() -> new ItemStack(MIC_ITEM))
                    .title(Component.translatable("channel.tab.title"))
                    .displayItems((displayParameters, output) -> {
                        output.accept(HANDHELD_MIC_ITEM);
                        output.accept(MIC_ITEM);
                        output.accept(SPEAKER_ITEM);
                        output.accept(SPEAKER_STAND_ITEM);
                        output.accept(SPEAKER_HANG_ITEM);
                        output.accept(WALKIE_TALKIE);
                    })
                    .build()
    );

    /**
     * 触发类加载以完成静态字段注册。
     */
    public static void init() {
    }

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(Channel.MODID, name), item);
    }
}

package cn.ussshenzhou.channel.blockentity;

import cn.ussshenzhou.channel.Channel;
import cn.ussshenzhou.channel.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;


/**
 * @author USS_Shenzhou
 */
public class ModBlockEntityTypes {
    public static final BlockEntityType<MicBlockEntity> MIC_BLOCK_ENTITY_TYPE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Channel.MODID, "mic"),
            new BlockEntityType<>(MicBlockEntity::new, Set.of(ModBlocks.MIC_BLOCK))
    );

    public static final BlockEntityType<SpeakerBlockEntity> SPEAKER_BLOCK_ENTITY_TYPE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Channel.MODID, "speaker"),
            new BlockEntityType<>(
                    SpeakerBlockEntity::new,
                    Set.of(
                            ModBlocks.SPEAKER_BLOCK,
                            ModBlocks.SPEAKER_STAND_BLOCK,
                            ModBlocks.SPEAKER_HANG_BLOCK
                    )
            )
    );
    /**
     * 触发类加载以完成静态字段注册。
     */
    public static void init() {
    }

}

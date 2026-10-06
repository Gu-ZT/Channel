package cn.ussshenzhou.channel.block;

import cn.ussshenzhou.channel.Channel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * @author USS_Shenzhou
 */
public class ModBlocks {
    public static final Block MIC_BLOCK = register("mic", new MicBlock(BlockBehaviour.Properties.of()
            .noOcclusion()
            .setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Channel.MODID, "mic")))
    ));

    public static final Block SPEAKER_BLOCK = register("speaker", new SpeakerBlock(BlockBehaviour.Properties.of()
            .noOcclusion()
            .setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Channel.MODID, "speaker")))
    ) {
        private static final VoxelShape SHAPE_WEST = Block.box(2.5, 0, 0, 11.5, 7.5, 16);
        private static final VoxelShape SHAPE_NORTH = Block.box(0, 0, 2.5, 16, 7.5, 11.5);
        private static final VoxelShape SHAPE_SOUTH = Block.box(0, 0, 4.5, 16, 7.5, 13.5);
        private static final VoxelShape SHAPE_EAST = Block.box(4.5, 0, 0, 13.5, 7.5, 16);

        @Override
        protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            return switch (state.getValue(ChanneledBlock.FACING)) {
                case NORTH -> SHAPE_NORTH;
                case EAST -> SHAPE_EAST;
                case SOUTH -> SHAPE_SOUTH;
                case WEST -> SHAPE_WEST;
                default -> SHAPE_NORTH;
            };
        }
    });

    public static final Block SPEAKER_STAND_BLOCK = register("speaker_stand", new SpeakerBlock(BlockBehaviour.Properties.of()
            .noOcclusion()
            .setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Channel.MODID, "speaker_stand")))
    ) {
        private static final VoxelShape SHAPE = Block.column(8, 0, 16);

        @Override
        protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            return SHAPE;
        }
    });

    public static final Block SPEAKER_HANG_BLOCK = register("speaker_hang", new SpeakerBlock(BlockBehaviour.Properties.of()
            .noOcclusion()
            .setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Channel.MODID, "speaker_hang")))
    ) {
        private static final VoxelShape SHAPE_WEST = Block.box(0, 0, 0, 9, 16, 16);
        private static final VoxelShape SHAPE_NORTH = Block.box(0, 0, 0, 16, 16, 9);
        private static final VoxelShape SHAPE_SOUTH = Block.box(0, 0, 7, 16, 16, 16);
        private static final VoxelShape SHAPE_EAST = Block.box(7, 0, 0, 16, 16, 16);

        @Override
        protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            return switch (state.getValue(ChanneledBlock.FACING)) {
                case NORTH -> SHAPE_NORTH;
                case EAST -> SHAPE_EAST;
                case SOUTH -> SHAPE_SOUTH;
                case WEST -> SHAPE_WEST;
                default -> SHAPE_NORTH;
            };
        }
    });

    /**
     * 触发类加载以完成静态字段注册。
     */
    public static void init() {
    }

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(Channel.MODID, name), block);
    }
}

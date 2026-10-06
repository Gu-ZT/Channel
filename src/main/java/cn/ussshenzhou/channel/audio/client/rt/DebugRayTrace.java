package cn.ussshenzhou.channel.audio.client.rt;

import cn.ussshenzhou.channel.config.ChannelClientConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

/**
 * @author USS_Shenzhou
 */
@Environment(EnvType.CLIENT)
public class DebugRayTrace {
    public static final ArrayList<Ray> rays = new ArrayList<>();

    public static void register() {
        LevelRenderEvents.BEFORE_GIZMOS.register(DebugRayTrace::render);
    }

    private static void render(LevelRenderContext context) {
        if (!ChannelClientConfig.get().showRaytrace) {
            return;
        }
        synchronized (DebugRayTrace.rays) {
            for (var ray : rays) {
                Gizmos.line(ray.from, ray.to, ray.color, 1f);
            }
        }
    }

    public record Ray(Vec3 from, Vec3 to, int color) {

    }
}

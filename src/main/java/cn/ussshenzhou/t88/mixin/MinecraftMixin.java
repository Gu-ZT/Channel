package cn.ussshenzhou.t88.mixin;

import cn.ussshenzhou.t88.gui.event.ResizeHudEvent;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author USS_Shenzhou
 */
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "resizeGui",at = @At("RETURN"))
    private void t88ResizeHud(CallbackInfo ci){
        ResizeHudEvent.EVENT.invoker().onResize(new ResizeHudEvent());
    }
}

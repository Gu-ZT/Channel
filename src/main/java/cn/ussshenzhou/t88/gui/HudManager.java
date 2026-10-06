package cn.ussshenzhou.t88.gui;

import cn.ussshenzhou.t88.gui.event.ResizeHudEvent;
import cn.ussshenzhou.t88.gui.widegt.TComponent;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author USS_Shenzhou
 */
public class HudManager {
    private static final LinkedHashSet<TComponent> CHILDREN = new LinkedHashSet<>();
    private static final LinkedList<TComponent> needAdd = new LinkedList<>();
    private static final LinkedList<TComponent> needRemove = new LinkedList<>();

    public static void add(TComponent... tComponents) {
        needAdd.addAll(Arrays.stream(tComponents).toList());
    }

    public static void remove(TComponent... tComponents) {
        needRemove.addAll(Arrays.stream(tComponents).toList());
    }

    public static LinkedHashSet<TComponent> getChildren() {
        return CHILDREN;
    }

    public static void addIfSameClassNotExist(TComponent... tComponents) {
        Arrays.stream(tComponents).forEach(component -> {
            var o = CHILDREN.stream()
                    .filter(t -> t.getClass() == component.getClass())
                    .findFirst();
            if (o.isEmpty()) {
                add(component);
            }
        });
    }

    public static void addOrReplaceIfSameClassExist(TComponent... tComponents) {
        Arrays.stream(tComponents).forEach(component -> {
            var o = CHILDREN.stream()
                    .filter(t -> t.getClass() == component.getClass())
                    .findFirst();
            o.ifPresent(HudManager::remove);
            add(component);
        });
    }

    public static void removeInstanceOf(Class<? extends TComponent> clazz) {
        needRemove.addAll(CHILDREN.stream().filter(clazz::isInstance).toList());
    }

    public static void renderHud(GuiGraphicsExtractor graphics, int mouseX, int mouseY, DeltaTracker partialTick) {
        if (Minecraft.getInstance().gui.hud.isHidden()) {
            return;
        }
        CHILDREN.forEach((tComponent) -> {
            if (tComponent.isVisibleT()) {
                tComponent.extractRenderState(graphics, mouseX, mouseY, partialTick.getRealtimeDeltaTicks());
            }
        });
        CHILDREN.forEach((tComponent) -> {
            if (tComponent.isVisibleT()) {
                tComponent.renderTop(graphics, mouseX, mouseY, partialTick.getRealtimeDeltaTicks());
            }
        });
    }

    /**
     * 客户端初始化时调用一次，接线全部事件监听。
     */
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> onTick());
        ResizeHudEvent.EVENT.register(HudManager::onResize);
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> onPlayerOut());
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("t88", "hud"), (graphics, deltaTracker) -> {
            var minecraft = Minecraft.getInstance();
            var window = minecraft.getWindow();
            renderHud(graphics,
                    (int) minecraft.mouseHandler.getScaledXPos(window),
                    (int) minecraft.mouseHandler.getScaledYPos(window),
                    deltaTracker);
        });
    }

    public static void onTick() {
        int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        needAdd.forEach(tComponent -> {
            CHILDREN.add(tComponent);
            tComponent.resizeAsHud(w, h);
        });
        needAdd.clear();
        needRemove.forEach(CHILDREN::remove);
        needRemove.clear();
        CHILDREN.forEach((tComponent) -> {
            if (tComponent.isVisibleT()) {
                tComponent.tickT();
            }
        });
    }

    public static void onResize(ResizeHudEvent event) {
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        CHILDREN.forEach(tComponent -> tComponent.resizeAsHud(screenWidth, screenHeight));
    }

    public static void onPlayerOut() {
        synchronized (CHILDREN) {
            List<TComponent> l = CHILDREN.stream().filter(TComponent::isShowHudEvenLoggedOut).toList();
            CHILDREN.clear();
            CHILDREN.addAll(l);
        }
    }
}

package com.diontryban.shuffle.client.gui.widgets;

import com.diontryban.shuffle.options.ModOptionsManager;
import com.diontryban.shuffle.options.ShuffleOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;

public class HotbarLockButtonsWidget extends AbstractWidget {
    private final ModOptionsManager<ShuffleOptions> options;
    private final int offset;

    private static final Identifier HOTBAR_SPRITE = Identifier.withDefaultNamespace("hud/hotbar");
    private static final Identifier LOCKED_SPRITE = Identifier.withDefaultNamespace("container/cartography_table/locked");

    public HotbarLockButtonsWidget(int offset, ModOptionsManager<ShuffleOptions> options) {
        super(offset, 1, 20 * 9, 20, Component.empty());
        this.options = options;
        this.offset = offset;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float v) {
        guiGraphics.centeredText(
                Minecraft.getInstance().font,
                Component.translatable("shuffle.options.hotbar_lock"),
                offset + this.getX() + 85,
                this.getY() - 20,
                0xFFFFFF
        );

        guiGraphics.centeredText(
                Minecraft.getInstance().font,
                Component.translatable("shuffle.options.hotbar_lock.description").withStyle(ChatFormatting.GRAY),
                offset + this.getX() + 85,
                this.getY() - 10,
                0xFFFFFF
        );

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, offset + this.getX(), this.getY(), 182, 22);

        Tooltip activeTooltip = null;
        for (int slot = 0; slot < 9; slot++) {
            final var slotX = getSlotX(slot);
            final var hovering = mouseX >= slotX && mouseX < slotX + 20 && mouseY >= this.getY() && mouseY < this.getY() + 20;

            if (this.options.get().lockedSlots[slot]) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, LOCKED_SPRITE, slotX + 5, this.getY() + 5, 10, 14);
            } else if (hovering) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, LOCKED_SPRITE, slotX + 5, this.getY() + 5, 10, 14, ARGB.color(ARGB.as8BitChannel(0.5f), -1));
            }

            if (hovering) {
                activeTooltip = Tooltip.create(
                        this.options.get().lockedSlots[slot] ?
                            Component.translatable("shuffle.options.hotbar_lock.tooltip.on", slot + 1) :
                            Component.translatable("shuffle.options.hotbar_lock.tooltip.off", slot + 1)
                );
            }
        }
        this.setTooltip(activeTooltip);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (this.active && this.visible && this.isMouseOver(event.x(), event.y())) {
            int slot = (int) ((event.x() - getX() - offset) / 20);
            if (slot >= 0 && slot < 9) {
                this.options.get().lockedSlots[slot] = !this.options.get().lockedSlots[slot];
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                return true;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        var withinX = mouseX >= (double)this.getSlotX(0) && mouseX < (double)(this.getSlotX(0) + this.getWidth());
        var withinY = mouseY >= (double)this.getY() && mouseY < (double)(this.getY() + this.getHeight());

        return this.active && this.visible && withinX && withinY;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
    }

    private int getSlotX(int slot) {
        return offset + this.getX() + 1 + (20 * slot);
    }
}

package com.epherical.professions.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;

public abstract class Box extends GuiComponent implements Widget, WidgetParent {

    public int x;
    public int y;
    public int width;
    public int height;

    public Box(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
        hLine(stack, x, width + x, y, 0xFFFFFFFF); // top
        hLine(stack, x, width + x, y + height, 0xFFFFFFFF); // bottom
        vLine(stack, x + width, y, y + height, 0xFFFFFFFF); // right
        vLine(stack, x, y + height, y, 0xFFFFFFFF); // left
        fill(stack, x, y, x + width + 1, y + height + 1, 0xAA333333);
    }

    public void playDownSound(SoundManager handler) {
        handler.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }


    public void beginAnimation() {

    }

    public void endAnimation() {

    }
}

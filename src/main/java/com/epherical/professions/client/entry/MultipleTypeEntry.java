package com.epherical.professions.client.entry;

import com.epherical.professions.client.screen.CommonDataScreen;
import com.google.gson.JsonElement;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

import java.util.Optional;

public class MultipleTypeEntry<T> extends DatapackEntry<T, MultipleTypeEntry<T>> {

    private int currentSelection;
    private final DatapackEntry<T, ?>[] types;

    protected boolean needsRefresh;

    public MultipleTypeEntry(int i, int j, int k, DatapackEntry<T, ?>[] entries, Type... types) {
        super(i, j, k, 23, types);
        currentSelection = 0;
        this.types = entries;
        setValue(0);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        types[currentSelection].render(poseStack, mouseX, mouseY, partialTick);
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        font.drawShadow(poseStack, types[currentSelection].getType() + " (Click to change)", x + 3 + getXScroll(), y + 8 + getYScroll(), 0xFFFFFF);
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.renderButton(poseStack, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        increment();
    }

    public void increment() {
        currentSelection++;
        if (currentSelection >= types.length) {
            currentSelection = 0;
        }
        needsRefresh = true;
    }

    /**
     * 0 to arraySize - 1 are the valid values.
     */
    public void setValue(int value) {
        if (value >= types.length) {
            currentSelection = types.length - 1;
        } else {
            currentSelection = value;
        }
        children.add(types[currentSelection]);
        //needsRefresh = true;
    }

    public DatapackEntry<T, ?> currentSelection() {
        return types[currentSelection];
    }

    @Override
    public void tick(CommonDataScreen screen) {
        super.tick(screen);
        this.types[currentSelection].tick(screen);
        if (needsRefresh) {
            screen.markScreenDirty();
            for (DatapackEntry<T, ?> type : this.types) {
                children.remove(type);
            }
            DatapackEntry object = types[currentSelection];
            children.add(object);
            needsRefresh = false;
        }
    }

    @Override
    public void onRebuild(CommonDataScreen screen) {
        // any direct children go before this entry.
        rebuildTinyButtons(screen);
        screen.addChild(this);
        DatapackEntry object = types[currentSelection];
        object.onRebuild(screen);
    }

    @Override
    public String getType() {
        return "Multiple Choices";
    }

    public JsonElement getSerializedValue() {
        return types[currentSelection].getSerializedValue();
    }

    @Override
    public Optional<String> getSerializationKey() {
        return types[currentSelection].getSerializationKey();
    }

    @Override
    public void deserialize(T object) {
        types[currentSelection].deserialize(object);
    }
}

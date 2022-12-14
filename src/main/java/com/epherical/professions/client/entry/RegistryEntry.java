package com.epherical.professions.client.entry;

import com.epherical.professions.client.button.SmallIconButton;
import com.epherical.professions.client.screen.CommonDataScreen;
import com.epherical.professions.client.widgets.CommandButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RegistryEntry<DE, T> extends DatapackEntry<DE, RegistryEntry<DE, T>> {

    private final Minecraft minecraft = Minecraft.getInstance();

    private Registry<T> registry;
    private T value;
    private Component tooltip;
    private final SmallIconButton button;

    private final Deserializer<DE, RegistryEntry<DE, T>> deserializer;

    private boolean added = false;
    private List<RegistryObjectEntry<DE, T>> registryEntries = new ArrayList<>();

    public RegistryEntry(int x, int y, int width, Registry<T> registry, T defaultValue, Optional<String> serializationKey,
                         Deserializer<DE, RegistryEntry<DE, T>> deserializer, Type... types) {
        super(x, y, width, serializationKey, types);
        this.value = defaultValue;
        this.tooltip = Component.literal(registry.getKey(defaultValue).toString());
        this.registry = registry;
        this.deserializer = deserializer;

        int start = width - 25;
        this.button = new SmallIconButton(x + start, y + 2, 16, 16, Component.nullToEmpty(""), CommandButton.SmallIcon.DROP_DOWN_OPEN, smallButton -> {
            smallButton.opened = !smallButton.opened;
            if (smallButton.opened) {
                smallButton.icon = CommandButton.SmallIcon.DROP_DOWN_CLOSE;
            } else {
                smallButton.icon = CommandButton.SmallIcon.DROP_DOWN_OPEN;
            }
        });
        children.add(button);
    }

    public RegistryEntry(int x, int y, int width, Registry<T> registry, T defaultValue, Deserializer<DE, RegistryEntry<DE, T>> deserializer) {
        this(x, y, width, registry, defaultValue, Optional.empty(), deserializer);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        font.drawShadow(poseStack, "Type:", x + 3 + getXScroll(), y + 8 + getYScroll(), 0xFFFFFF);
        drawCenteredString(poseStack, font, tooltip, (x + this.width / 2) + getXScroll(), (y + 8) + getYScroll(), TEXT_COLOR);
        if (isHoveredOrFocused()) {
            renderToolTip(poseStack, mouseX, mouseY, tooltip);
        }
        int start = width - 25;
        button.x = x + start + xScroll;
        button.y = y + 2 + yScroll;
    }

    @Override
    public void initPosition(int initialX, int initialY) {
        super.initPosition(initialX, initialY);
        setButtonPositions(0, 0);
    }

    @Override
    public JsonElement getSerializedValue() {
        return new JsonPrimitive(registry.getKey(value).toString());
    }

    @Override
    public void deserialize(DE object) {
        deserializer.deserialize(object, this);
    }

    @Override
    public void tick(CommonDataScreen screen) {
        super.tick(screen);
        if (button.isOpened() && !added) {
            screen.markScreenDirty();
        } else if (!button.isOpened() && added) {
            screen.markScreenDirty();
        }
    }

    @Override
    public void onRebuild(CommonDataScreen screen) {
        rebuildTinyButtons(screen);
        screen.addChild(button);
        screen.addChild(this);

        if (button.isOpened() && !added) {
            List<RegistryObjectEntry<DE, T>> objects = new ArrayList<>();
            for (Map.Entry<ResourceKey<T>, T> entry : registry.entrySet()) {
                RegistryObjectEntry<DE, T> objectEntry = new RegistryObjectEntry<>(x + 7, y + 23, 160, entry.getKey(), entry.getValue(), (object) -> {
                    this.value = object.getObject();
                    this.tooltip = Component.literal(registry.getKey(this.value).toString());
                    this.button.icon = CommandButton.SmallIcon.DROP_DOWN_OPEN;
                    this.button.opened = false;
                    screen.markScreenDirty();
                });
                objects.add(objectEntry);
                screen.addChild(objectEntry);
            }
            registryEntries.addAll(objects);
            added = true;
        } else if (!button.isOpened() && added) {
            added = false;
        }
    }

    @Override
    public String getType() {
        return "String";
    }

    private void update() {
        this.tooltip = Component.literal(registry.getKey(this.value).toString());
    }

    public void setValue(T value) {
        this.value = value;
        update();
    }


    public T getValue() {
        return value;
    }

    public Registry<T> getRegistry() {
        return registry;
    }

    private void setButtonPositions(int xScroll, int yScroll) {
        int start = width - 25;
        button.x = x + start + xScroll;
        button.y = y + 2 + yScroll;
    }

    public interface ClickRegistryObjectEntry<V, T> {
        void action(RegistryObjectEntry<V, T> entry);
    }
}

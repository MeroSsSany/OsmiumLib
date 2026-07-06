package dev.merosssany.osmiumlib.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

public abstract class StatusBar {
    protected final int numberOfSprites;
    protected final int states;
    protected final int width;
    protected int total = 100;
    protected int current = 0;
    
    private boolean blinking;
    private double previousFullSlot, previousPartialSlot;
    private long blinkStartTime;
    private double previousUnits;
    
    public StatusBar(int numberOfSprites, int states, int width) {
        this.numberOfSprites = numberOfSprites;
        this.states = states;
        this.width = width;
        previousFullSlot = numberOfSprites;
        previousPartialSlot = states;
    }
    
    public void render(GuiGraphics graphics, int x, int y, boolean right) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        
        double totalUnits = ((double) current / total) * (states * numberOfSprites);
        int fullSlots = (int) Math.floor(totalUnits / states);
        
        int partialSlotRemainder = states - ((int) Math.floor(totalUnits) % states);
        if (partialSlotRemainder == states && totalUnits > 0 && (totalUnits % states == 0)) {
            partialSlotRemainder = 0;
        }
        
        boolean renderOutline = previousFullSlot != fullSlots || previousPartialSlot != partialSlotRemainder;
        long now = System.currentTimeMillis();
        
        if (renderOutline && !blinking) {
            blinking = true;
            blinkStartTime = now;
        }
        
        long blinkingDuration = now - blinkStartTime;
        if (blinking && blinkingDuration >= 500) { // 500ms equals roughly 10 game ticks
            blinking = false;
            previousUnits = totalUnits;
        }
        
        ResourceLocation sprites = sprites();
        
        for (int i = 0; i < numberOfSprites; i++) {
            int[] uvs;
            if (i == fullSlots) {
                uvs = uv(partialSlotRemainder);
            } else if (i > fullSlots) {
                uvs = uv(states); // Empty slot texture
            } else {
                uvs = uv(0); // Full slot texture
            }
            
            int xs = right ? x - (i * width) : x + (i * width);
            
            graphics.blit(sprites, xs, y, uvs[0], uvs[1], 9, 9, 256, 256);
            
            if (blinking) {
                if ((blinkingDuration / 125) % 2 == 0) {
                    int[] uvFrame;
                    if (i == fullSlots) {
                        uvFrame = (previousUnits < totalUnits) ? incrementationFrame() : reductionFrame();
                    } else {
                        uvFrame = frame();
                    }
                    graphics.blit(sprites, xs, y, uvFrame[0], uvFrame[1], 9, 9, 256, 256);
                }
            }
        }
        
        previousFullSlot = fullSlots;
        previousPartialSlot = partialSlotRemainder;
    }
    
    public abstract int[] frame();
    public abstract int[] reductionFrame();
    public abstract int[] incrementationFrame();
    public abstract int[] uv(int states);
    public abstract ResourceLocation sprites();
}
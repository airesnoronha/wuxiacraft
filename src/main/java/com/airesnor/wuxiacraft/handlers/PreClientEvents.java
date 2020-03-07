package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.blocks.OBJBlockModelLoader;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber
public class PreClientEvents {

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent event) {
        //Manually add the mod textures because my OBJBlockModelLoader won't do that, shame
        event.getMap().registerSprite(new ResourceLocation("wuxiacraft:blocks/iron_cauldron"));
    }
}

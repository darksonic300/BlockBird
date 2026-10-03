package com.github.darksonic300.blockbird;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;


@Mod(BlockBird.MODID)
public class BlockBird {
    public static final String MODID = "blockbird";

    public static final Logger LOGGER = LogUtils.getLogger();

    public BlockBird(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.CLIENT, BBConfig.SPEC);
    }
}

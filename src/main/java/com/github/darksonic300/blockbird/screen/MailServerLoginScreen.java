package com.github.darksonic300.blockbird.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MailServerLoginScreen extends Screen {
    protected MailServerLoginScreen(Component title) {
        super(title);
    }
}

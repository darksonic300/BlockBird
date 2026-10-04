package com.github.darksonic300.blockbird;

import com.github.darksonic300.blockbird.screen.EmailToast;
import jakarta.mail.Message;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.CompletableFuture;


@Mod(value = BlockBird.MODID, dist = Dist.CLIENT)
public class BlockBirdClient {

    private BBMainManager emailManager;
    private boolean isRunning = false; // Prevents thread spamming

    public BlockBirdClient(IEventBus modEventBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, BBConfig.SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        NeoForge.EVENT_BUS.addListener(this::onWorldLoad);
        NeoForge.EVENT_BUS.addListener(this::onWorldUnload);
        NeoForge.EVENT_BUS.addListener(this::checkMail);
    }

    public void onWorldLoad(ClientPlayerNetworkEvent.LoggingIn event) {
        this.emailManager = new BBMainManager(
                BBConfig.PROVIDER.get(),
                BBConfig.USERNAME.get(),
                BBConfig.PASSWORD.get()
        );

        this.emailManager.initialize();
    }

    public void onWorldUnload(ClientPlayerNetworkEvent.LoggingOut event) {
        if(this.emailManager == null) return;
        this.emailManager.close();
    }

    public void checkMail(ClientTickEvent.Post event) {
        if(this.emailManager == null || isRunning) return;
        /*if (ChronoUnit.MINUTES.between(
                this.emailManager.getLastCheck().toInstant(),
                new Date().toInstant()
        ) < BBConfig.REFRESH_RATE.getAsInt()) return;*/
        if (ChronoUnit.SECONDS.between(
                this.emailManager.getLastCheck().toInstant(),
                new Date().toInstant()
        ) < 10) return;

        isRunning = true;

        try {
            CompletableFuture.supplyAsync(() -> this.emailManager.check())
                .thenAccept(messages -> {
                    if (messages != null && !messages.isEmpty()) {
                            for (var msg : messages) {
                                Minecraft.getInstance().getToasts().addToast(new EmailToast(msg));
                            }
                    }
                }).whenComplete((result, exception) -> {
                    isRunning = false;
                    if (exception != null) {
                        exception.printStackTrace();
                    }
                });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

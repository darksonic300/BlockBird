package com.github.darksonic300.blockbird;

import com.github.darksonic300.blockbird.screen.ConfigReminderScreen;
import com.github.darksonic300.blockbird.util.BBToasts;
import com.github.darksonic300.blockbird.util.BBUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

import java.util.concurrent.CompletableFuture;

@Mod(value = BlockBird.MODID, dist = Dist.CLIENT)
public class BlockBirdClient {

	private BBMailManager emailManager;
	private boolean isRunning = false; // Prevents thread spamming
	private static boolean showScreen = true;

	public BlockBirdClient(ModContainer container) {
		container.registerConfig(ModConfig.Type.CLIENT, BBConfig.SPEC);
		container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

		NeoForge.EVENT_BUS.addListener(this::onWorldLoad);
		NeoForge.EVENT_BUS.addListener(this::onWorldUnload);
		NeoForge.EVENT_BUS.addListener(this::checkMail);
		NeoForge.EVENT_BUS.addListener(this::onGuiOpen);
	}

	public void onGuiOpen(ScreenEvent.Opening event) {
		Screen screen = event.getScreen();
		showScreen = showScreen && (BBConfig.PASSWORD.get().isEmpty() || BBConfig.EMAIL.get().isEmpty());

		if (!(screen instanceof TitleScreen title) || !showScreen)
			return;

		event.setNewScreen(new ConfigReminderScreen(title));
		showScreen = false;
	}

	public void onWorldLoad(ClientPlayerNetworkEvent.LoggingIn event) {
		this.emailManager = BBMailManager.getInstance(BBConfig.PROVIDER.get(), BBConfig.EMAIL.get(),
				BBConfig.PASSWORD.get());

		this.emailManager.initialize();
		Component component = Component.translatable("gui.blockbird.toast.disabled");
		if (this.emailManager.isInitialized()) {
			component = Component.translatable("gui.blockbird.toast.enabled");
		}
		BBToasts.info(component);
	}

	public void onWorldUnload(ClientPlayerNetworkEvent.LoggingOut event) {
		if (this.emailManager == null || !this.emailManager.isInitialized())
			return;
		this.emailManager.close();
	}

	public void checkMail(ClientTickEvent.Post event) {
		if (this.emailManager == null || !this.emailManager.isInitialized() || isRunning)
			return;
		if (BBUtils.hasEnoughTimePassed(emailManager))
			return;

		isRunning = true;
		CompletableFuture.supplyAsync(() -> this.emailManager.check()).thenAccept(messages -> {
			if (messages == null || messages.isEmpty())
				return;
			messages.forEach(BBToasts::email);
		}).whenComplete((r, e) -> isRunning = false);
	}
}

package com.github.darksonic300.blockbird.screen;

import com.github.darksonic300.blockbird.BlockBird;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.gui.ModListScreen;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class ConfigReminderScreen extends Screen {
	private static final ResourceLocation LOGO_LOCATION = ResourceLocation.fromNamespaceAndPath(BlockBird.MODID,
			"textures/gui/blockbird.png");
	private final Screen parentScreen;

	public ConfigReminderScreen(Screen parentScreen) {
		super(Component.literal("BlockBird"));
		this.parentScreen = parentScreen;
	}

	@Override
	protected void init() {
		super.init();
		this.addRenderableWidget(Button.builder(Component.translatable("gui.blockbird.reminder_screen.main_button"),
				(onpress) -> this.onClose()).pos(this.width / 2 - 160, this.height / 2 + 75).build());
		this.addRenderableWidget(Button
				.builder(Component.translatable("gui.blockbird.reminder_screen.config_button"),
						(onpress) -> this.minecraft.setScreen(new ModListScreen(this.parentScreen)))
				.pos(this.width / 2 + 10, this.height / 2 + 75).build());

	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(this.parentScreen);
		// Call last in case it interferes with the override
		super.onClose();
	}

	@Override
	public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
		super.render(graphics, pMouseX, pMouseY, pPartialTick);
		graphics.drawCenteredString(Minecraft.getInstance().font,
				Component.translatable("gui.blockbird.reminder_screen.first_line"), this.width / 2,
				this.height / 2 + 20, 16777215);
		graphics.drawCenteredString(Minecraft.getInstance().font,
				Component.translatable("gui.blockbird.reminder_screen.second_line"), this.width / 2,
				this.height / 2 + 40, 16777215);
	}

	@Override
	public void renderBackground(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
		super.renderBackground(graphics, pMouseX, pMouseY, pPartialTick);

		float bestRatio = Math.min((float) this.width / 607.0F, (float) this.height / 567.0F);

		int widthRes = (int) (607 * bestRatio * 0.4);
		int heightRes = (int) (567 * bestRatio * 0.4);

		int xPos = (this.width - widthRes) / 2;
		int yPos = (this.height - heightRes) / 6;
		RenderSystem.enableBlend();
		graphics.blit(LOGO_LOCATION, xPos, yPos, 0.0F, 0.0F, widthRes, heightRes, widthRes, heightRes);
		RenderSystem.disableBlend();
	}
}

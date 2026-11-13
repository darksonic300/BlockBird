package com.github.darksonic300.blockbird.screen;

import com.github.darksonic300.blockbird.BlockBird;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;

public class BBBaseToast implements Toast {
	private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/system");
	private static final ResourceLocation ICON_SPRITE = ResourceLocation.fromNamespaceAndPath(BlockBird.MODID,
			"blockmail/icon");

	public static final int DISPLAY_TIME = 5000;

	// left = Subject, right = Description/Sender
	private final Component message;
	private boolean playedSound;

	public BBBaseToast(Component message) {
		this.message = message;
	}

	@Override
	public Toast.Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long timeSinceLastVisible) {
		guiGraphics.blitSprite(BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
		guiGraphics.blitSprite(ICON_SPRITE, 15, 7, 20, 20);

		if (!this.playedSound && timeSinceLastVisible > 0L) {
			this.playedSound = true;
			toastComponent.getMinecraft().getSoundManager()
					.play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_IN, 1.0F, 1.0F));
		}

		Font font = toastComponent.getMinecraft().font;

		guiGraphics.drawString(font, message, 38, 12, 255 << 24 | 0xFFFF00, false);

		return (double) timeSinceLastVisible >= (double) DISPLAY_TIME
				* toastComponent.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
	}
}

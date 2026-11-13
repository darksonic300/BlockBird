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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class BBEmailToast implements Toast {
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/system");
    private static final ResourceLocation ICON_SPRITE = ResourceLocation.fromNamespaceAndPath(BlockBird.MODID, "blockmail/icon");

    public static final int DISPLAY_TIME = 5000;

    // left = Subject, right = Description/Sender
    private final Pair<String, String> message;
    private boolean playedSound;

    public BBEmailToast(Pair<String, String> message) {
        this.message = message;
    }

    @Override
    public Toast.Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long timeSinceLastVisible) {
        guiGraphics.blitSprite(BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
        guiGraphics.blitSprite(ICON_SPRITE, 15, 7, 20, 20);

        if (!this.playedSound && timeSinceLastVisible > 0L) {
            this.playedSound = true;
            toastComponent.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_IN, 1.0F, 1.0F));
        }

        Font font = toastComponent.getMinecraft().font;

        String subjectRaw = message.getLeft() != null ? message.getLeft() : "No Subject";
        String senderRaw = message.getRight() != null ? message.getRight() : "";

        List<FormattedCharSequence> splitSubject = font.split(Component.literal(subjectRaw), 100);
        List<FormattedCharSequence> splitSender = font.split(Component.literal(senderRaw), 100);

        FormattedCharSequence subject = splitSubject.isEmpty() ? FormattedCharSequence.EMPTY : splitSubject.getFirst();
        FormattedCharSequence sender = splitSender.isEmpty() ? FormattedCharSequence.EMPTY : splitSender.getFirst();

        if (timeSinceLastVisible < 1500L) {
            float fadeOut = Mth.clamp((float)(1500L - timeSinceLastVisible) / 300.0F, 0.0F, 1.0F);
            int alpha = Mth.floor(fadeOut * 255.0F) << 24;

            guiGraphics.drawString(font, Component.literal("New Email"), 38, 12, alpha | 0xFFFF00, false);
        } else {
            float fadeIn = Mth.clamp((float)(timeSinceLastVisible - 1500L) / 300.0F, 0.0F, 1.0F);
            int alpha = Mth.floor(fadeIn * 255.0F) << 24;

            guiGraphics.drawString(font, subject, 38, 7, alpha | 0xFFFF00, false); // Yellow subject
            guiGraphics.drawString(font, sender, 38, 18, alpha | 0xFFFFFF, false); // White sender/body
        }

        return (double)timeSinceLastVisible >= (double)DISPLAY_TIME * toastComponent.getNotificationDisplayTimeMultiplier()
                ? Visibility.HIDE
                : Visibility.SHOW;
    }
}
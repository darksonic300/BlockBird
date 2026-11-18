package com.github.darksonic300.blockbird.util;

import com.github.darksonic300.blockbird.screen.BBBaseToast;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class BBToasts {
	public static void email(Component cmp) {
		Minecraft.getInstance().getToasts().addToast(new BBBaseToast(cmp));
	}

	public static void info(Component cmp) {
		Minecraft.getInstance().getToasts().addToast(new BBBaseToast(cmp));
	}
}

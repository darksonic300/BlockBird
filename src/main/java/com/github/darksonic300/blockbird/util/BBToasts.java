package com.github.darksonic300.blockbird.util;

import com.github.darksonic300.blockbird.screen.BBBaseToast;
import com.github.darksonic300.blockbird.screen.BBEmailToast;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.tuple.Pair;

public class BBToasts {
	public static void email(Pair<String, String> msg) {
        Minecraft.getInstance().execute(() ->
                Minecraft.getInstance().getToasts().addToast(new BBEmailToast(msg))
        );
	}

	public static void info(Component cmp) {
		Minecraft.getInstance().execute(() ->
                Minecraft.getInstance().getToasts().addToast(new BBBaseToast(cmp))
        );
	}
}

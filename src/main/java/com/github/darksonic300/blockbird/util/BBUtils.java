package com.github.darksonic300.blockbird.util;

import com.github.darksonic300.blockbird.BBConfig;
import com.github.darksonic300.blockbird.BBMailManager;

import java.time.temporal.ChronoUnit;
import java.util.Date;

public class BBUtils {
	public static boolean hasEnoughTimePassed(BBMailManager emailManager) {
		return ChronoUnit.MINUTES.between(emailManager.getLastCheck().toInstant(),
				new Date().toInstant()) < BBConfig.REFRESH_RATE.getAsInt();
	}
}

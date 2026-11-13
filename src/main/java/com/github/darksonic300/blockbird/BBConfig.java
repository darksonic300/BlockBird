package com.github.darksonic300.blockbird;

import com.github.darksonic300.blockbird.enums.IMAPSProvider;
import net.neoforged.neoforge.common.ModConfigSpec;

public class BBConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<IMAPSProvider> PROVIDER = BUILDER
            .comment("Configure your preferred IMAPS email provider.")
            .defineEnum("Email Provider", IMAPSProvider.GMAIL);

    public static final ModConfigSpec.ConfigValue<String> EMAIL = BUILDER
            .comment("Configure your preferred IMAPS email username.")
            .define("Username", "");
    public static final ModConfigSpec.ConfigValue<String> PASSWORD = BUILDER
            .comment("Configure your preferred IMAPS email password.")
            .define("Password", "");

    public static final ModConfigSpec.IntValue REFRESH_RATE = BUILDER
            .comment("Configure the refresh rate of the email check in minutes.")
            .defineInRange("Refresh Rate", 5, 1, 120);

    static final ModConfigSpec SPEC = BUILDER.build();

}

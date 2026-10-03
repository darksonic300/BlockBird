package com.github.darksonic300.blockbird;

import java.util.List;

import com.github.darksonic300.blockbird.enums.EmailProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public class BBConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<EmailProvider> PROVIDER = BUILDER
            .comment("Configure your preferred email provider.")
            .defineEnum("Email Provider", EmailProvider.GMAIL);

    static final ModConfigSpec SPEC = BUILDER.build();

}

/*
 * This file is part of Shuffle.
 * A copy of this program can be found at https://github.com/Trikzon/shuffle.
 * Copyright (C) 2023 Dion Tryban
 *
 * Shuffle is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * Shuffle is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Shuffle. If not, see <https://www.gnu.org/licenses/>.
 */

package com.diontryban.shuffle.client.gui.screens;

import com.diontryban.shuffle.Shuffle;
import com.diontryban.shuffle.client.gui.widgets.HotbarLockButtonsWidget;
import com.diontryban.shuffle.options.ModOptionsManager;
import com.diontryban.shuffle.options.ShuffleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShuffleOptionsScreen extends OptionsSubScreen {
    private final ModOptionsManager<ShuffleOptions> options;

    public ShuffleOptionsScreen(@NotNull ModOptionsManager<ShuffleOptions> options, Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.literal(Shuffle.MOD_NAME));
        this.options = options;
    }

    @Override
    protected void addOptions() {
        if (this.list == null) { return; }

        this.list.addBig(OptionInstance.createBoolean(
                "shuffle.options.use_weighted_random",
                value -> Tooltip.create(Component.translatable("shuffle.options.use_weighted_random.tooltip")),
                this.options.get().useWeightedRandom,
                value -> this.options.get().useWeightedRandom = value
        ));
        this.list.addBig(OptionInstance.createBoolean(
                "shuffle.options.play_sound_effects",
                value -> Tooltip.create(Component.translatable("shuffle.options.play_sound_effects.tooltip")),
                this.options.get().playSoundEffects,
                value -> this.options.get().playSoundEffects = value
        ));
        this.list.addSmall(List.of(new MultiLineTextWidget(0, 5, CommonComponents.EMPTY, font))); // spacing
        this.list.addSmall(List.of(new HotbarLockButtonsWidget(65, this.options)));
    }

    @Override
    public void onClose() {
        this.options.save();
        super.onClose();
    }
}

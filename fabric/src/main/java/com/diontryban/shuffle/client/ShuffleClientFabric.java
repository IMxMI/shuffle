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

package com.diontryban.shuffle.client;

import com.diontryban.shuffle.Shuffle;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class ShuffleClientFabric implements ClientModInitializer {
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(Shuffle.MOD_ID, "shuffle")
    );

    @Override
    public void onInitializeClient() {
        ShuffleClient.keyMapping = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.shuffle.shuffle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                CATEGORY
        ));

        ClientTickEvents.START_CLIENT_TICK.register(ShuffleClient::onClientTickPre);
        UseBlockCallback.EVENT.register(ShuffleClient::onUseBlock);
    }
}

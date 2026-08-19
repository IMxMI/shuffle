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

package com.diontryban.shuffle.options;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModOptionsManager<T extends ModOptions> {
    private static final Logger LOG = LoggerFactory.getLogger(ModOptionsManager.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path configPath;
    private final Class<T> optionsClass;
    private T options;

    public ModOptionsManager(String modId, Class<T> optionsClass) {
        this.optionsClass = optionsClass;

        Path configDir;
        try {
            configDir = FabricLoader.getInstance().getConfigDir();
        } catch (Throwable ignored) {
            configDir = Path.of("config");
        }
        this.configPath = configDir.resolve(modId + ".json");
        this.load();
    }

    public T get() {
        return this.options;
    }

    public void load() {
        if (Files.exists(this.configPath)) {
            try (BufferedReader reader = Files.newBufferedReader(this.configPath)) {
                this.options = GSON.fromJson(reader, this.optionsClass);
                if (this.options != null) {
                    return;
                }
            } catch (Exception e) {
                LOG.error("Failed to read configuration file at {}", this.configPath, e);
            }
        }

        try {
            this.options = this.optionsClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate default options class: " + this.optionsClass, e);
        }
        this.save();
    }

    public void save() {
        try {
            Files.createDirectories(this.configPath.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(this.configPath)) {
                GSON.toJson(this.options, writer);
            }
        } catch (IOException e) {
            LOG.error("Failed to save configuration file at {}", this.configPath, e);
        }
    }
}

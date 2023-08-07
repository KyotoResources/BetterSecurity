/*
 * Security plugin for your server - https://github.com/KyotoResources/BetterSecurity
 * Copyright (C) 2023 KyotoResources
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.zs0bye.bettersecurity.common.updater;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;

@AllArgsConstructor
public class UpdateAgent {

    private final String resourceId;

    @Getter
    private final String currentVersion;
    @Getter
    private final String newVersion;
    @Getter
    private final String updateName;

    public void download(final DownloadCallback callback, final File updateFolder, final File pluginFile) {
        String DOWNLOAD_URL = "https://api.spiget.org/v2/resources/%id%/download";
        Throwable throwable = null;

        if (!updateFolder.exists()) updateFolder.mkdirs();

        try {
            final HttpURLConnection connection = (HttpURLConnection) new URL(DOWNLOAD_URL.replace("%id%", resourceId)).openConnection();
            connection.setRequestProperty("User-Agent", "VandalUpdater");
            final FileOutputStream output = new FileOutputStream(new File(updateFolder, pluginFile.getName()));
            output.getChannel().transferFrom(Channels.newChannel(connection.getInputStream()), 0, Long.MAX_VALUE);
            output.flush();
            output.close();
        } catch (final Exception e) {
            throwable = e;
        }

        callback.onDownload(throwable);
    }

    @FunctionalInterface
    public interface DownloadCallback {
        void onDownload(Throwable throwable);
    }

}

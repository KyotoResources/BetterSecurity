/*
 * MIT License
 *
 * Copyright (c) 2021 ImOnlyFire
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
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

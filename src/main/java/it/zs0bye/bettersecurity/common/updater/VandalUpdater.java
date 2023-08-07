/*
 * Security plugin for your server - https://github.com/KyotoResources/BetterSecurity
 * Copyright (C) 2021-2023 KyotoResources
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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class VandalUpdater {

    private final String resourceId;
    private final UpdateType updateType;

    @Getter
    private boolean updateFound = false;
    private boolean firstCheck = true;

    @Setter
    private String updateMessage;

    public UpdateAgent checkForUpdates(String currentVersion) {
        try {
            JSONObject lastVersionObject = readLatestVersion();
            JSONArray allVersionsArray = readAllVersions();

            String updateName = lastVersionObject.get("title").toString();
            String updateVersion = (String) ((JSONObject) allVersionsArray.get(0)).get("name");

            Version currentVersionFixed = new Version(currentVersion);
            Version updateVersionFixed = new Version(updateVersion);

            try {
                if (updateVersionFixed.compareTo(currentVersionFixed) > 0) {
                    return new UpdateAgent(resourceId, currentVersion, updateVersion, updateName);
                } else {
                    return null;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    private JSONArray readAllVersions() throws IOException, JSONException {
        String RESOURCE_VERSIONS_SPIGET = "https://api.spiget.org/v2/resources/%id%/versions?sort=-name";
        try (InputStream stream = new URL(RESOURCE_VERSIONS_SPIGET.replace("%id%", resourceId)).openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String jsonText = readAll(reader);
            return new JSONArray(jsonText);
        }
    }

    private JSONObject readLatestVersion() throws IOException, JSONException {
        String RESOURCE_UPDATES_SPIGET = "https://api.spiget.org/v2/resources/%id%/updates/latest";
        try (InputStream stream = new URL(RESOURCE_UPDATES_SPIGET.replace("%id%", resourceId)).openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String jsonText = readAll(reader);
            return new JSONObject(jsonText);
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public String message(final Logger logger, final String pluginName, final String version, final File pluginFile, final File updateFile) {

        final UpdateAgent agent = this.checkForUpdates(version);

        if (agent == null) {
            this.updateFound = false;
            return "";
        }

        logger.info(String.format("An update has been detected for %s: %s (Current version: %s)",
                pluginName, agent.getNewVersion(), agent.getCurrentVersion()));

        if (this.updateMessage == null)
            this.updateMessage = "&cAn update has been detected for " + pluginName + ": %new% (Current version: %current%)";

        updateFound = true;

        if (!firstCheck) return "";
        firstCheck = false;

        updateMessage = updateMessage.replace("%new%", agent.getNewVersion());
        updateMessage = updateMessage.replace("%old%", agent.getCurrentVersion());

        if (updateType == UpdateType.DOWNLOAD) {
            agent.download(throwable -> {
                if (throwable != null) {
                    logger.warning("Could not download the update for " + pluginName + ". " +
                            "Check the log for more info and report this!");
                    throwable.printStackTrace();
                } else {
                    logger.info("Update successfully downloaded! Please restart the server to load the new version!");
                }
            }, updateFile, pluginFile);
        }

        return this.updateMessage;
    }

}

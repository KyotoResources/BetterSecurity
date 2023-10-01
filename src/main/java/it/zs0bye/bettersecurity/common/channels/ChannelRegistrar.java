/*
 * Security plugin for your server - https://github.com/KyotoResources/BetterSecurity
 * Copyright (c) 2023 KyotoResources
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

package it.zs0bye.bettersecurity.common.channels;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Setter;

import java.util.function.Consumer;

public abstract class ChannelRegistrar {

    protected abstract String getIdentifier();
    protected abstract void receiver();

    public static final String PLUGIN_SENDER = "bsecurity:sender";
    public static final String PLUGIN_RETURN = "bsecurity:return";

    @Setter
    protected ByteArrayDataInput input;

    protected final void process(final Consumer<ChannelRegistrar> channel) {
        if(!this.input.readUTF().equalsIgnoreCase(this.getIdentifier())) return;
        channel.accept(this);
    }

    public static ByteArrayDataOutput sendOutput(final String... values) {
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        for(final String value : values) output.writeUTF(value);
        return output;
    }

}

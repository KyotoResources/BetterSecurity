package it.zs0bye.bettersecurity.utils;

import it.zs0bye.bettersecurity.BetterSecurity;
import it.zs0bye.bettersecurity.imgs.ChatImg;
import it.zs0bye.bettersecurity.imgs.enums.ImageChar;
import it.zs0bye.bettersecurity.utils.enums.FontUtils;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String colorize(String message) {
        if(message == null) {
            BetterSecurity.getInstance().getLogger()
                    .log(Level.SEVERE, "There was an error searching for a missing message!");
            return "";
        }
        final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            final String hexCode = message.substring(matcher.start(), matcher.end());
            final String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            final StringBuilder builder = new StringBuilder();
            for (char c : ch) builder.append("&").append(c);

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String center(String message){
        message = colorize(message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()) {
            if(c == '§'){
                previousCode = true;
            } else if(previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                FontUtils dFI = FontUtils.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 154 - halvedMessageSize;
        int spaceLength = FontUtils.SPACE.getLength() + 1;
        int compensated = 0;

        final StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb + message;
    }


    @SneakyThrows
    public static void image(final CommandSender sender, final String[] description) {
        final InputStream png = BetterSecurity.getInstance().getResource("chest.png");
        if(png == null) return;
        final BufferedImage imageToSend = ImageIO.read(png);
        new ChatImg(imageToSend, 8, ImageChar.BLOCK.getChar()).appendCenteredText(description).send(sender);
    }

    public static void send(final CommandSender sender, final String msg) {
        if (msg.isEmpty()) return;
        sender.sendMessage(msg);
    }

}

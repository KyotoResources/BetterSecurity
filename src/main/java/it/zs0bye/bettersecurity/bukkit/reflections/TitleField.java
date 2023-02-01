package it.zs0bye.bettersecurity.bukkit.reflections;

import it.zs0bye.bettersecurity.bukkit.utils.VersionUtils;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TitleField {

    private static Class<?> packetTitle;
    private static Class<?> packetActions;
    private static Class<?> nmsChatSerializer;
    private static Class<?> chatBaseComponent;

    private static final Map<Class<?>, Class<?>> CORRESPONDING_TYPES = new HashMap<>();

    private final Player player;
    private final String title;
    private final String subtitle;
    private final int fadein;
    private final int stay;
    private final int fadeout;

    public TitleField(final Player player, final String title, final String subtitle, final int fadein, final int stay, final int fadeout) {
        this.player = player;
        this.title = title;
        this.subtitle = subtitle;
        this.fadein = fadein;
        this.stay = stay;
        this.fadeout = fadeout;
        this.loadClasses();
        this.send();
    }

    @SneakyThrows
    private void send() {

        if (!VersionUtils.checkVersion(1.8, 1.9, 1.10)) {
            this.player.sendTitle(this.title, this.subtitle, this.fadein, this.stay, this.fadeout);
            return;
        }

        if (packetTitle == null) return;
        this.resetTitle(this.player);

        final Object handle = this.getHandle(this.player);
        if (handle == null) return;

        final Object connection = getField(handle.getClass()).get(handle);
        final Object[] actions = packetActions.getEnumConstants();
        final Method sendPacket = getMethod(connection.getClass());
        if (sendPacket == null) return;

        Object packet = packetTitle.getConstructor(packetActions,
                chatBaseComponent, Integer.TYPE, Integer.TYPE,
                Integer.TYPE).newInstance(actions[2], null,
                this.fadein,
                this.stay,
                this.fadeout);
        if (this.fadein != -1 && this.fadeout != -1 && this.stay != -1)
            sendPacket.invoke(connection, packet);

        Object serialized = nmsChatSerializer.getConstructor(
                String.class).newInstance(this.title);
        packet = packetTitle.getConstructor(packetActions,
                chatBaseComponent).newInstance(actions[0], serialized);
        sendPacket.invoke(connection, packet);

        if (this.subtitle.isEmpty()) return;
        serialized = nmsChatSerializer.getConstructor(String.class)
                .newInstance(
                        this.subtitle);
        packet = packetTitle.getConstructor(packetActions,
                chatBaseComponent).newInstance(actions[1],
                serialized);
        sendPacket.invoke(connection, packet);
    }

    @SneakyThrows
    public void resetTitle(Player player) {
        final Object handle = getHandle(player);
        Object connection = getField(handle.getClass())
                .get(handle);
        Object[] actions = packetActions.getEnumConstants();
        Method sendPacket = getMethod(connection.getClass());
        Object packet = packetTitle.getConstructor(packetActions,
                chatBaseComponent).newInstance(actions[4], null);

        if(sendPacket == null) return;
        sendPacket.invoke(connection, packet);
    }

    @SneakyThrows
    public  Class<?> getNMSClass(String name) {
        return Class.forName("net.minecraft.server."
                + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
    }

    private void loadClasses() {
        if (!VersionUtils.checkVersion(1.8, 1.9, 1.10)) return;
        if (packetTitle != null) return;
        packetTitle = this.getNMSClass("PacketPlayOutTitle");
        packetActions = this.getNMSClass("PacketPlayOutTitle$EnumTitleAction");
        chatBaseComponent = this.getNMSClass("IChatBaseComponent");
        nmsChatSerializer = this.getNMSClass("ChatComponentText");
    }

    @SneakyThrows
    private Object getHandle(final Object obj) {
        return Objects.requireNonNull(this.getMethod("getHandle", obj.getClass())).invoke(obj);
    }

    @SneakyThrows
    private Field getField(final Class<?> clazz) {
        final Field field = clazz.getDeclaredField("playerConnection");
        field.setAccessible(true);
        return field;
    }

    private Method getMethod(final Class<?> clazz, final Class<?>... args) {
        for (final Method m : clazz.getMethods())
            if (m.getName().equals("sendPacket")
                    && (args.length == 0 || ClassListEqual(args,
                    m.getParameterTypes()))) {
                m.setAccessible(true);
                return m;
            }
        return null;
    }



    private Method getMethod(final String name, final Class<?> clazz,
                             final Class<?>... paramTypes) {
        Class<?>[] t = toPrimitiveTypeArray(paramTypes);
        for (Method m : clazz.getMethods()) {
            Class<?>[] types = toPrimitiveTypeArray(m.getParameterTypes());
            if (m.getName().equals(name) && equalsTypeArray(types, t))
                return m;
        }
        return null;
    }

    private Class<?>[] toPrimitiveTypeArray(final Class<?>[] classes) {
        int a = classes != null ? classes.length : 0;
        Class<?>[] types = new Class<?>[a];
        for (int i = 0; i < a; i++)
            types[i] = getPrimitiveType(classes[i]);
        return types;
    }

    private Class<?> getPrimitiveType(final Class<?> clazz) {
        return CORRESPONDING_TYPES.getOrDefault(clazz, clazz);
    }


    private static boolean equalsTypeArray(final Class<?>[] a, final Class<?>[] o) {
        if (a.length != o.length) return false;
        for (int i = 0; i < a.length; i++) if (!a[i].equals(o[i]) && !a[i].isAssignableFrom(o[i])) return false;
        return true;
    }


    private boolean ClassListEqual(final Class<?>[] l1, final Class<?>[] l2) {
        boolean equal = true;
        if (l1.length != l2.length) return false;
        for (int i = 0; i < l1.length; i++)
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        return equal;
    }

}

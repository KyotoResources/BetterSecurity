package it.zs0bye.bettersecurity.bukkit.reflections;

import it.zs0bye.bettersecurity.bukkit.checks.VersionCheck;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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

    private void send() {

        if (!VersionCheck.getV1_8()
        && !VersionCheck.getV1_9()
        && !VersionCheck.getV1_10()) {
            this.player.sendTitle(this.title, this.subtitle, this.fadein, this.stay, this.fadeout);
            return;
            }

        if (packetTitle != null) {
            resetTitle(this.player);
            try {

                Object handle = getHandle(this.player);
                Object connection = getField(handle.getClass(),
                        "playerConnection").get(handle);
                Object[] actions = packetActions.getEnumConstants();
                Method sendPacket = getMethod(connection.getClass(),
                        "sendPacket");
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
                if (this.subtitle != "") {

                    serialized = nmsChatSerializer.getConstructor(String.class)
                            .newInstance(
                                    this.subtitle);
                    packet = packetTitle.getConstructor(packetActions,
                            chatBaseComponent).newInstance(actions[1],
                            serialized);
                    sendPacket.invoke(connection, packet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void resetTitle(Player player) {
        try {
            Object handle = getHandle(player);
            Object connection = getField(handle.getClass(), "playerConnection")
                    .get(handle);
            Object[] actions = packetActions.getEnumConstants();
            Method sendPacket = getMethod(connection.getClass(), "sendPacket");
            Object packet = packetTitle.getConstructor(packetActions,
                    chatBaseComponent).newInstance(actions[4], null);
            sendPacket.invoke(connection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server."
                    + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void loadClasses() {
        if (!VersionCheck.getV1_8()
                && !VersionCheck.getV1_9()
                && !VersionCheck.getV1_10()) {
            return;
        }
        if (packetTitle == null) {
            packetTitle = getNMSClass("PacketPlayOutTitle");
            packetActions = getNMSClass("PacketPlayOutTitle$EnumTitleAction");
            chatBaseComponent = getNMSClass("IChatBaseComponent");
            nmsChatSerializer = getNMSClass("ChatComponentText");
        }
    }

    private Object getHandle(Object obj) {
        try {
            return getMethod("getHandle", obj.getClass()).invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Field getField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Method getMethod(Class<?> clazz, String name, Class<?>... args) {
        for (Method m : clazz.getMethods())
            if (m.getName().equals(name)
                    && (args.length == 0 || ClassListEqual(args,
                    m.getParameterTypes()))) {
                m.setAccessible(true);
                return m;
            }
        return null;
    }



    private Method getMethod(String name, Class<?> clazz,
                             Class<?>... paramTypes) {
        Class<?>[] t = toPrimitiveTypeArray(paramTypes);
        for (Method m : clazz.getMethods()) {
            Class<?>[] types = toPrimitiveTypeArray(m.getParameterTypes());
            if (m.getName().equals(name) && equalsTypeArray(types, t))
                return m;
        }
        return null;
    }

    private Class<?>[] toPrimitiveTypeArray(Class<?>[] classes) {
        int a = classes != null ? classes.length : 0;
        Class<?>[] types = new Class<?>[a];
        for (int i = 0; i < a; i++)
            types[i] = getPrimitiveType(classes[i]);
        return types;
    }

    private Class<?> getPrimitiveType(Class<?> clazz) {
        return CORRESPONDING_TYPES.containsKey(clazz) ? CORRESPONDING_TYPES
                .get(clazz) : clazz;
    }


    private static boolean equalsTypeArray(Class<?>[] a, Class<?>[] o) {
        if (a.length != o.length)
            return false;
        for (int i = 0; i < a.length; i++)
            if (!a[i].equals(o[i]) && !a[i].isAssignableFrom(o[i]))
                return false;
        return true;
    }


    private boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
        boolean equal = true;
        if (l1.length != l2.length)
            return false;
        for (int i = 0; i < l1.length; i++)
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        return equal;
    }

}

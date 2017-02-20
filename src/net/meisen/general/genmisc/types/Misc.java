package net.meisen.general.genmisc.types;

public class Misc {
    public static boolean isWindows() {
        final String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return true;
        } else {
            return false;
        }
    }
}

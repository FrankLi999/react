package ocp.maven.plugin;

public class SystemUtils {
    private static final String OS = System.getProperty("os.name");
    public static final String systemSpecificSubDirectory() {
        return isWindowsOS() ? "win" : isLinuxOs() ? "linux" : isMacOSX()? "mac" : "";
    }
    private static boolean isWindowsOS() {
        return osMatch("Windows");
    }

    private static boolean isLinuxOs() {
        return osMatch("Linux") || osMatch("LINUX");
    }

    private static boolean isMacOSX() {
        return osMatch("Mac OS X");
    }

    private static final boolean osMatch(String osNamePrefix) {
        return OS.startsWith(osNamePrefix);
    }
}

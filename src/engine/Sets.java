package engine;

public class Sets {
    private static int screenWide = 1600;
    private static int screenHigh = 900;
    private static int screenFPS = 100;
    private static String screenName = "Restore";

    public static int getScreenWide() {
        return screenWide;
    }

    public static void setScreenWide(int screenWide) {
        Sets.screenWide = screenWide;
    }

    public static int getScreenHigh() {
        return screenHigh;
    }

    public static void setScreenHigh(int screenHigh) {
        Sets.screenHigh = screenHigh;
    }

    public static int getScreenFPS() {
        return screenFPS;
    }

    public static void setScreenFPS(int screenFPS) {
        Sets.screenFPS = screenFPS;
    }

    public static String getScreenName() {
        return screenName;
    }

}

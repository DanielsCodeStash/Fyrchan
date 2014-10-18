package model;

import java.io.File;

public class BaseSettings
{


    public static String baseOutDir = getBaseOutDir();

    public static String baseFolderFormat = "<threadid>_<title>";
    public static String threadIdTag = "<threadid>";
    public static String threadTitleTag = "<title>";

    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36";
    public static int threadTimeout = 20 * 1000;
    public static int numGatherThreads = 3;
    public static int maxFileSize = 6 * 1000 * 1000;

    public static int maxCharsInThreadTitleName = 20;
    public static String exampleThreadId = "523546";
    public static String exampleThreadName = "hotwheels_not_naked";


    private static String getBaseOutDir()
    {
        String defaultDir = "\\Fyrchan";
        String desktopDir = "\\Desktop";
        String homePath = System.getProperty("user.home");

        String path = homePath;

        File desktopDirTest = new File(homePath + desktopDir);
        if(desktopDirTest.exists() && desktopDirTest.isDirectory())
        {
            path += desktopDir;
        }
        path += defaultDir;
        return path;
    }

}

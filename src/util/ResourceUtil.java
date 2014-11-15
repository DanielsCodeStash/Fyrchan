package util;

import javafx.scene.image.Image;

import java.io.*;

public class ResourceUtil
{
    public static Image getImage(String filename)
    {
        boolean runningFromJar = ResourceUtil.class.getResource("ResourceUtil.class").toString().startsWith("jar:");
        if(runningFromJar)
        {
            InputStream iconStream = ResourceUtil.class.getResourceAsStream("/res/" + filename);
            return new Image(iconStream);
        }
        else
        {
            return new Image("file:res/" + filename);
        }
    }

}

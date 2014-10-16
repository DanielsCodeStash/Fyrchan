package model.parsing;

import model.BaseSettings;

public class PathHandler
{


    public static void main(String[] args)
    {
        System.out.println(getPath("C:\\", "<threadid>_<title>", "http://boards.4chan.org/hr/thread/2230521/sam-armytage"));
        System.out.println(getPath("C:\\", "<threadid>_<title>", "http://boards.4chan.org/hr/thread/2230521"));
    }

    public static String getPath(String basePathStr, String threadNameStr, String threadUrlStr)
    {
        if(!basePathStr.endsWith("\\"))
        {
            basePathStr += "\\";
        }

        String outPath = basePathStr;

        String threadId = BaseSettings.exampleThreadId;
        String threadName = BaseSettings.exampleThreadName;

        boolean replaceTags = threadNameStr.contains(BaseSettings.threadIdTag)
                || threadNameStr.contains(BaseSettings.threadTitleTag);

        if (urlIsValid(threadUrlStr) && replaceTags)
        {
            String[] urlParts = threadUrlStr.split("/");

            if (urlParts.length < 7)
                return "Can't construct path.";

            threadId = urlParts[5];
            threadName = urlParts[6].replace("-", "_");

            if (threadName.length() > BaseSettings.maxCharsInThreadTitleName)
            {
                threadName = threadName.substring(0, BaseSettings.maxCharsInThreadTitleName);
            }
        }

        if (threadNameStr.contains(BaseSettings.threadIdTag))
        {
            threadNameStr = threadNameStr.replace(BaseSettings.threadIdTag, threadId);
        }
        if (threadNameStr.contains(BaseSettings.threadTitleTag))
        {
            threadNameStr = threadNameStr.replace(BaseSettings.threadTitleTag, threadName);
        }

        outPath += threadNameStr;

        if (threadNameStr.isEmpty())
            return outPath;
        else
            return outPath + "\\";
    }

    public static boolean urlIsValid(String url)
    {
        return url.contains("thread");
    }
}

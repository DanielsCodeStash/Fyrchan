package model.parsing;

import model.BaseSettings;

public class PathHandler
{

    public static class EvaluatedPath
    {
        private String fullPath;
        private String evaluatedName;

        public EvaluatedPath(String fullPath, String evaluatedName)
        {
            this.fullPath = fullPath;
            this.evaluatedName = evaluatedName;
        }

        public String getFullPath()
        {
            return fullPath;
        }

        public String getEvaluatedName()
        {
            return evaluatedName;
        }
    }

    public static void main(String[] args)
    {
        System.out.println(getPath("C:\\", "<threadid>_<title>", "http://boards.4chan.org/hr/thread/2230521/sam-armytage"));
        System.out.println(getPath("C:\\", "<threadid>_<title>", "http://boards.4chan.org/hr/thread/2230521"));
    }

    public static EvaluatedPath getPath(String basePathStr, String threadNameStr, String threadUrlStr)
    {
        if (!basePathStr.endsWith("\\"))
        {
            basePathStr += "\\";
        }
        if (threadUrlStr.contains("#reply"))
        {
            threadUrlStr = threadUrlStr.replace("#reply", "");
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
                return new EvaluatedPath("Can't construct path.", null);

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
            return new EvaluatedPath(outPath, "?");
        else
            return new EvaluatedPath(outPath + "\\", threadNameStr);
    }

    public static boolean urlIsValid(String url)
    {
        return url.contains("thread");
    }
}

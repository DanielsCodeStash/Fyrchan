package util;

import model.BaseSettings;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class ConHandler
{


    public static Connection getConnection(String url)
    {
        Connection con = Jsoup.connect(url);
        con.timeout(BaseSettings.threadTimeout);
        con.userAgent(BaseSettings.userAgent);
        con.maxBodySize(BaseSettings.maxFileSize);
        return con;
    }


}

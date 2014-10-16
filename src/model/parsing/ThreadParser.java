package model.parsing;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ThreadParser
{
    public static ArrayList<String> getFileUrls(Document threadDocument)
    {
        ArrayList<String> fileUrls = new ArrayList<>();

        // <div id="fT568501362" class="fileText">File: <a target="_blank" href="//i.4cdn.org/b/1410639677043.png">Laci Green.png</a> (203 KB, 526x295)</div>
        Elements fileElements = threadDocument.getElementsByClass("fileText");

        for (Element fileElement : fileElements)
        {
            // <a target="_blank" href="//i.4cdn.org/b/1410639677043.png">Laci Green.png</a>
            Elements ems = fileElement.getElementsByAttribute("href");

            // target and href
            Attributes attributes = ems.get(0).attributes();

            // //i.4cdn.org/b/1410639677043.png
            String url = attributes.get("href");

            // refined url, add http and remove //
            url = "http://" + url.substring(2);
            fileUrls.add(url);
        }
        return fileUrls;
    }
}

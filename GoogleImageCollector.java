/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package google.image.collector;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.lang3.StringUtils;
/**
 *
 * @author orhan
 */
public class GoogleImageCollector {

    public static void main(final String[] args) throws IOException {
        // İstediğiniz Google Resimler linkini alttaki satıra girin.
        final Document dc = Jsoup.connect("https://www.google.com/search?client=firefox-b-ab&biw=1366&bih=589&tbm=isch&sa=1&ei=7fbuXdqEEa2HjLsP6IOx-Ac&q=s%C3%BCt+markalar%C4%B1&oq=s%C3%BCt+markalar%C4%B1&gs_l=img.3..0l3j0i5i30l7.30288.56181..56378...0.0..0.348.3482.0j8j6j3......0....1..gws-wiz-img.....0..0i67j0i10.hmIYSIXd3v0&ved=0ahUKEwiar7j6-KnmAhWtA2MBHehBDH8Q4dUDCAY&uact=5#imgrc=_").timeout(6000).get();
        final Elements body = dc.select("body#gsr");
        ArrayList array;
        int index=0;
        for (final Element step : body.select("div#main")) {
            for (final Element step2 : step.select("div#cnt")) {
                for (final Element step3 : step2.select("div#rcnt")) {
                    for (final Element step4 : step3.select("div.col")) {
                        for (final Element step5 : step4.select("div#center_col")) {
                            for (final Element step6 : step5.select("div#res")) {
                                for (final Element img : step6.select("div.rg_meta")) {
                                    if (img.text().contains("http")) {
                                        String link = img.text();
                                        link = StringUtils.substringBetween(link, "{", "}");
                                        array = pullLink(link);
                                        
                                        for(int i=0;i<20;i++)
                                        {
                                            try
                                            {
                                                URL url = new URL((String)array.get(i));
                                                ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                                                FileOutputStream fos = new FileOutputStream("image"+index+".jpg");
                                                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                                                fos.close();
                                                index++;
                                            }
                                            catch(Throwable th)
                                            {
                                                th.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private static ArrayList pullLink(String text)
    {
        ArrayList links = new ArrayList();
        if(text.contains("https"))
        {
            String regex = "\\(?\\b(https://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(text);
            while (m.find()) {
                String urlStr = m.group();
                links.add(urlStr);
            }
            
        }
        else if(text.contains("http"))
        {
            String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(text);
            while (m.find()) {
                String urlStr = m.group();
                links.add(urlStr);
            }
        }
        return links;
    }
}

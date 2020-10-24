import java.io.IOException;
import java. lang.Exception;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser
{
    //Метод, забирающий HTML-страницу
    private static Document getPage() throws IOException
    {
        //Адрес забираемой страницы
        String url = "https://www.pogoda.spb.ru";
        Document page = Jsoup.parse(new URL(url), 400000000);

        return page;
    }

    //Задание шаблона строки, которая ищется
    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    private static String GetDate (String stringDate) throws Exception
    {
        Matcher matcher = pattern.matcher(stringDate);

        if(matcher.find())
        {
            return matcher.group();
        }
        throw new Exception("Date is not extracted!");
    }

    private static int PrintValues(Elements values, int tempValue)
    {
        int iterCount = 4;

        if(tempValue == 0)
        {
            Element valueLn = values.get(3);

            boolean isMorning = valueLn.text().contains("Утро");

            if (isMorning) {
                iterCount = 3;
            }
        }

        for (int i = 0; i < iterCount; i++) {
            Element valueLine = values.get(tempValue + i);

            for (Element td : valueLine.select("td"))
            {
                System.out.print(td.text() + "    ");
            }

            System.out.println();
        }

        return iterCount;
    }
    public static void main(String[] args) throws Exception
    {
        Document page = getPage();
        Element weatherTable = page.select("table[class=wt]").first();
        Elements titles = weatherTable.select("tr[class=wth]");
        Elements values = weatherTable.select("tr[valign=top]");

        //Индекс текущего значения
        int tempValue = 0;

        for(Element title : titles)
        {
            String dateString = title.select("th[id=dt]").text();
            String date = GetDate(dateString);

            System.out.println(date + "  Температура  Явление  Давление  Влажность  Ветер");

            int iterCount = PrintValues(values, tempValue);

            tempValue = tempValue + iterCount;
        }
    }
}

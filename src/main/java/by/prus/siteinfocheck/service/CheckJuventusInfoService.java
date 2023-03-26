package by.prus.siteinfocheck.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CheckJuventusInfoService implements InfoService {

    private static final Logger logger = LoggerFactory.getLogger(CheckJuventusInfoService.class);

    /**
     * Эта технология работает с динамической подгрузкой.
     * WebDriver Запускает edge, затем driver.get() подгружает страницу в том чиле запускает JS на ней.
     * Затем мы получаем уже готовый полный HTML код с которым можем работать.
     * @return Строку с информацией по матчам
     */
    @Override
    public String checkInfo() {
        System.setProperty("webdriver.edge.driver", "src/main/resources/msedgedriver.exe");
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriver driver = new EdgeDriver(options);

        String result = null;
        int counter = 0;

        //Повторяем только 100 раз, т.к. возможно сайт лежит.
        while (result==null && counter<100){
            driver.get(Constant.JUVENTUS);
            Document doc = Jsoup.parse(driver.getPageSource());
            result = parseSiteData(doc);
            counter++;
        }
        driver.quit();
        return result;
    }


    private String parseSiteData(Document doc){
        StringBuilder sb = new StringBuilder();
        for (org.jsoup.nodes.Element element : doc.select(".react-MatchCard")) {
            // название команд, участвующих в матче
            String teams = element.select(".jcom-c-match-teams__name").text();
            // время матча
            String time = element.select(".jcom-c-match-info__text.jcom-c-match-info__text--datetime").text();
            // доступность билетов
            String buyInfo = element.select(".jcom-mu__cta--tickets").text();

            if (teams.isBlank() || time.isBlank()||buyInfo.isBlank()) {
                logger.warn("Missing info for objects: ", teams, time, buyInfo);
                return null;
            }

            sb.append(teams + " " + time + " Билеты: "+buyInfo +"\n");
        }
        return sb.toString();
    }

}

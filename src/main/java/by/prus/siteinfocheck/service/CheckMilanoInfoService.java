package by.prus.siteinfocheck.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CheckMilanoInfoService implements InfoService {

    private static final Logger logger = LoggerFactory.getLogger(CheckMilanoInfoService.class);

    @Override
    public String checkInfo() {
        try {
            StringBuilder sb = new StringBuilder();
            Document doc = Jsoup.connect(Constant.MILANO).get();

            Element date = doc.selectFirst(".col-md-1.col-sm-2.col-xs-4");
            Element unavailable = doc.selectFirst(".btn.disabled.seoLinkDisabled.uppercase");
            Element available = doc.selectFirst(".btn.seoLink.openPop");

            if (date==null){
                return sb.append("Какой-то сбой в информации. Проверьте сайт").toString();
            }

            sb.append("Милан - Верона" +"\n");
            sb.append("Время матча: "+ date.text() + "\n");
            sb.append("Покупка билетов: ");

            if (unavailable != null && available == null) {
                sb.append(unavailable.text() + "\n");
                return sb.toString();
            }

            if (available != null && unavailable == null) {
                sb.append(available.text() + "\n");
                return sb.toString();
            }

            sb.append("Какой-то сбой в информации. Проверьте сайт");
            logger.warn("Missing info for milano fotbalmatch objects: ");
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

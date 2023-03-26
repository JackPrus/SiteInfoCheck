package by.prus.siteinfocheck.repository;

import by.prus.siteinfocheck.model.SiteInfo;
import by.prus.siteinfocheck.model.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class StorageRepository {

    @Autowired
    private SubscriberDAO subscriberDAO;
    @Autowired
    private SiteInfoDAO siteInfoDAO;


    public void addUser(String userChatId){
        subscriberDAO.save(new Subscriber(userChatId));
    }

    public void addInfoToStorage(String siteName, String siteData){
        siteInfoDAO.save(new SiteInfo(siteName, siteData));
    }

    public String getDataFromStorage(String siteName){
        Optional<SiteInfo> siteInfo = siteInfoDAO.findById(siteName);
        return siteInfo.isPresent() ? siteInfo.get().getSiteData() : "Нет информации о таком сайте";
    }

    public Set<String> getUsers() {
        List<Subscriber> subscribers = (List<Subscriber>) subscriberDAO.findAll();
        return subscribers.stream().map(Subscriber::getTelegramId).collect(Collectors.toSet());
    }

    public String deleteSubscriber(String userId){
        subscriberDAO.delete(new Subscriber(userId));
        return "Пользователь "+userId + " удален из рассылки";
    }
}

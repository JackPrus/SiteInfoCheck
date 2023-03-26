package by.prus.siteinfocheck.repository;

import by.prus.siteinfocheck.model.SiteInfo;
import by.prus.siteinfocheck.model.Subscriber;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StorageRepositoryTest {

    @Autowired
    StorageRepository storageRepository;
    @Autowired
    SubscriberDAO subscriberDAO;
    @Autowired
    SiteInfoDAO siteInfoDAO;

    final String user1 = "userTelegramId1";
    final String user2 = "userTelegramId2";
    final String siteName1 = "site1";
    final String siteInfo1 = "siteInfo1";
    final String siteName2 = "site2";
    final String siteInfo2 = "siteInfo2";
    final String noInfo = "Нет информации о таком сайте";

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
        subscriberDAO.delete(new Subscriber(user1));
        subscriberDAO.delete(new Subscriber(user2));
        siteInfoDAO.delete(new SiteInfo(siteName1, siteInfo1));
        siteInfoDAO.delete(new SiteInfo(siteName2, siteInfo2));

        assertThrows(NoSuchElementException.class, () -> subscriberDAO.findById(user1).get());
        assertThrows(NoSuchElementException.class, () -> subscriberDAO.findById(user2).get());
        assertThrows(NoSuchElementException.class, () -> siteInfoDAO.findById(siteName1).get());
        assertThrows(NoSuchElementException.class, () -> siteInfoDAO.findById(siteName2).get());
    }

    @Test
    void addUser() {
        storageRepository.addUser(user1);
        storageRepository.addUser(user2);
        Optional<Subscriber> subscriber1 = subscriberDAO.findById(user1);
        Optional<Subscriber> subscriber2 = subscriberDAO.findById(user2);

        assertEquals(user1, subscriber1.get().getTelegramId());
        assertEquals(user2, subscriber2.get().getTelegramId());
    }

    @Test
    void addInfoToStorage() {
        assertEquals(noInfo, storageRepository.getDataFromStorage(siteName1));
        assertEquals(noInfo, storageRepository.getDataFromStorage(siteName2));

        storageRepository.addInfoToStorage(siteName1, siteInfo1);
        storageRepository.addInfoToStorage(siteName2, siteInfo2);

        assertEquals(siteName1, siteInfoDAO.findById(siteName1).get().getSiteName());
        assertEquals(siteInfo2, siteInfoDAO.findById(siteName2).get().getSiteData());

        storageRepository.addInfoToStorage(siteName1, siteInfo2);
        assertEquals(siteInfo2, storageRepository.getDataFromStorage(siteName1));
    }

    @Test
    void getDataFromStorage() {
        storageRepository.addInfoToStorage(siteName1, siteInfo1);
        assertEquals(siteInfo1, storageRepository.getDataFromStorage(siteName1));
        assertEquals(noInfo, storageRepository.getDataFromStorage(siteName2));
    }

    @Test
    void getUsers() {
        storageRepository.addUser(user1);
        assertFalse(storageRepository.getUsers().isEmpty());
    }
}
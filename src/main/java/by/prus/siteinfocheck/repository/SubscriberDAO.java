package by.prus.siteinfocheck.repository;

import by.prus.siteinfocheck.model.Subscriber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberDAO extends CrudRepository<Subscriber, String> {
}

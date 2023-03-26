package by.prus.siteinfocheck.repository;

import by.prus.siteinfocheck.model.SiteInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteInfoDAO extends CrudRepository<SiteInfo, String> {
}

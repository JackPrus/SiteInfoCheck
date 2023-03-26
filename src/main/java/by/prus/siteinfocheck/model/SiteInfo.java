package by.prus.siteinfocheck.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "site_info")
public class SiteInfo {
    @Id
    @Column(nullable = false)
    private String siteName;
    @Column(length = 2000)
    private String siteData;

    public SiteInfo() {}

    public SiteInfo(String siteName, String siteData) {
        this.siteName = siteName;
        this.siteData = siteData;
    }

    public String getSiteName() { return siteName; }
    public void setSiteName(String siteName) { this.siteName = siteName; }
    public String getSiteData() { return siteData; }
    public void setSiteData(String siteData) { this.siteData = siteData; }
}

package by.prus.siteinfocheck.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "subscriber")
public class Subscriber {

    @Id
    @Column(nullable = false)
    private String telegramId;

    public Subscriber(){}

    public Subscriber(String telegramId) {
        this.telegramId = telegramId;
    }

    public String getTelegramId() {
        return telegramId;
    }
    public void setTelegramId(String telegramId) { this.telegramId = telegramId; }
}

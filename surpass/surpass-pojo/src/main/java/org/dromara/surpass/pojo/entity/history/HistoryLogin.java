package org.dromara.surpass.pojo.entity.history;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.surpass.entity.client.ClientResolve;
import org.dromara.surpass.entity.client.ClientUserAgent;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/9/30 10:45
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "SURPASS_HISTORY_LOGIN")
@Entity
public class HistoryLogin extends JpaEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 5132971632815235241L;

    public static final class CATEGORY{
        public static final Integer LOGIN 	= 1;
        public static final Integer LOGOUT 	= 2;
    }

    @Id
    @Column
    @GeneratedValue
    String id;

    @Column
    Integer category = CATEGORY.LOGIN;

    @Column
    String sessionId;

    @Column
    String style;

    @Column
    String userId;

    @Column
    String username;

    @Column
    String displayName;

    @Column
    String loginType;

    @Column
    String message;

    @Column
    String code;

    @Column
    String provider;

    @Column
    String ipAddr;

    @Column
    String country;

    @Column
    String province;

    @Column
    String city;

    @Column
    String location;

    @Column
    String browser;

    @Column
    String platform;

    @Column
    String deviceId;

    @Column
    String application;

    @Column
    Date operateTime;

    private String instName;

    Date startDate;

    Date endDate;

    String gradingUserId;

    public void setClientResolve(ClientResolve clientResolve) {
        this.country = clientResolve.getCountry();
        this.province = clientResolve.getProvince();
        this.city = clientResolve.getCity();
        this.location = clientResolve.getLocation();
    }

    public void setClientUserAgent(ClientUserAgent clientUserAgent) {
        this.browser = clientUserAgent.getName();
        this.platform = clientUserAgent.getPlatform();
    }
}

package org.dromara.surpass.pojo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/9/29 11:01
 */

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Table(name = "surpass_socials_provider")
public class SocialsProvider extends JpaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -969395954618232229L;

    @Id
    @Column
    @GeneratedValue
    String id;

    @Column
    String provider;

    @Column
    String providerName;

    @Column
    String icon;

    @Column
    String clientId;

    @Column
    String clientSecret;

    @Column
    String agentId;

    @Column
    String display;

    @Column
    long sortIndex;

    @Column
    String scanCode;

    @Column
    int status;

    @Column
    String redirectUri;

    @Column
    String accountId;

    @Column
    String bindTime;

    @Column
    String unBindTime;

    @Column
    String lastLoginTime;

    @Column
    String state;

    @Column
    boolean userBind;

    @Column
    @SoftDelete
    String deleted;

    @Column
    String createdBy;

    @Column
    Date createdDate;

    @Column
    String modifiedBy;

    @Column
    Date modifiedDate;

    public SocialsProvider(SocialsProvider copy) {
        this.clientId = copy.getClientId();
        this.id = copy.getId();
        this.provider = copy.getProvider();
        this.providerName = copy.getProviderName();
        this.agentId = copy.getAgentId();
        this.icon = copy.getIcon();
        this.scanCode = copy.getScanCode();
    }
}

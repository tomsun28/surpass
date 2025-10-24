package org.dromara.surpass.pojo.entity.config;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import java.util.List;

/**
 * @author Crystal.Sea
 *
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "surpass_config_password_policy")
@Entity
public class ConfigPasswordPolicy extends JpaEntity{
    @Id
    @Column
    @GeneratedValue
    String id;

    /**
     * minimum password lengths
     */

    @NotNull
    @Column
    private int minLength;

    /**
     * maximum password lengths
     */
    @NotNull
    @Column
    private int maxLength;

    /**
     * least lowercase letter
     */
    @NotNull
    @Column
    private int lowerCase;

    /**
     * least uppercase letter
     */
    @NotNull
    @Column
    private int upperCase;

    /**
     * inclusion of numerical digits
     */
    @NotNull
    @Column
    private int digits;

    /**
     * inclusion of special characters
     */
    @NotNull
    @Column
    private int specialChar;

    /**
     * require users to change passwords periodically
     */
    @Column
    private int expiration;

    /**
     * 0 no 1 yes
     */
    @Column
    private int username;

    /**
     * not include password list
     */
    @Column
    private int history;

    @Column
    private int dictionary;

    @Column
    private int alphabetical;

    @Column
    private int numerical;

    @Column
    private int qwerty;

    @Column
    private int occurances;

    private int randomPasswordLength = 12;

    List<String> policMessageList;
}

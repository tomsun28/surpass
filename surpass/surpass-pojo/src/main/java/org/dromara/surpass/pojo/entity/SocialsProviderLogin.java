package org.dromara.surpass.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/9/29 10:13
 */

@Data
@NoArgsConstructor
public class SocialsProviderLogin implements Serializable {
    @Serial
    private static final long serialVersionUID = 835200383570441889L;

    List<SocialsProvider> providers = new ArrayList<>();

    String qrScan = null;

    public SocialsProviderLogin(List<SocialsProvider> socialSignOnProviders) {
        super();
        this.providers = socialSignOnProviders;
    }
}

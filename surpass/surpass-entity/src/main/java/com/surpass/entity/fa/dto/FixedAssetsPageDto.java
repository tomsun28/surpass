package com.surpass.entity.fa.dto;

import com.surpass.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/7/3 15:55
 */

@Data
@EqualsAndHashCode(callSuper=false)
public class FixedAssetsPageDto extends PageQuery {
    @Serial
    private static final long serialVersionUID = -9171566143129781340L;

    private String bookId;

    private String code;

    private String assetName;

    private String categoryId;
}

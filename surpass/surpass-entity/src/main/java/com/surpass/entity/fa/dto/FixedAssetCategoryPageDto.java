package com.surpass.entity.fa.dto;

import com.surpass.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/7/1 10:13
 */

@Data
@EqualsAndHashCode(callSuper=false)
public class FixedAssetCategoryPageDto extends PageQuery {
    /**
	 *
	 */
	private static final long serialVersionUID = 7643491855607438638L;

	private String bookId;

    private String categoryCode;

    private String categoryName;

    private String keyword;
}

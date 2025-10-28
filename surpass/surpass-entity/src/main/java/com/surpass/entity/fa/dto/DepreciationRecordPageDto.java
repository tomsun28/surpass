package com.surpass.entity.fa.dto;

import com.surpass.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/7/9 11:08
 */

@Data
@EqualsAndHashCode(callSuper=false)
public class DepreciationRecordPageDto extends PageQuery {
    /**
	 *
	 */
	private static final long serialVersionUID = -834939928744439501L;

	private String bookId;

    private String startPeriod;

    private String endPeriod;

    private String year;

    String[] belongDateRange;
}

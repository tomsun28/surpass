/*
 * Copyright [2025] [Surpass of copyright http://www.surpass.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */






package com.surpass.entity.history;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.EqualsAndHashCode;

import com.baomidou.mybatisplus.annotation.TableName;
import com.surpass.entity.BaseEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 24096
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("JBX_HISTORY_SYNCHRONIZER")
public class HistorySynchronizer  extends BaseEntity  implements Serializable{
    @Serial
	private static final long serialVersionUID = -1184644499009162756L;

	@TableId(type = IdType.ASSIGN_ID)
    String id;

    String syncId;

    String sessionId;

    String syncName;

    String objectId;

    String objectType;

    String objectName;

    Date syncTime;

    String result;

	private String bookId;

	private String instName;
	Date startDate;
	Date endDate;

    public HistorySynchronizer(String id, String syncId,  String syncName, String objectId,
			String objectType, String objectName, Date syncTime, String result,String bookId) {
		super();
		this.id = id;
		this.syncId = syncId;
		this.syncName = syncName;
		this.objectId = objectId;
		this.objectType = objectType;
		this.objectName = objectName;
		this.syncTime = syncTime;
		this.result = result;
		this.bookId = bookId;
	}

	public HistorySynchronizer(String id, String syncId, String sessionId, String syncName, String objectId,
			String objectType, String objectName, Date syncTime, String result, String bookId) {
		super();
		this.id = id;
		this.syncId = syncId;
		this.sessionId = sessionId;
		this.syncName = syncName;
		this.objectId = objectId;
		this.objectType = objectType;
		this.objectName = objectName;
		this.syncTime = syncTime;
		this.result = result;
		this.bookId = bookId;
	}

}

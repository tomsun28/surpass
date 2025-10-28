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
@TableName("JBX_HISTORY_CONNECTOR")
public class HistoryConnector  extends BaseEntity  implements Serializable{
    @Serial
    private static final long serialVersionUID = 3465459057253994386L;

    @TableId(type = IdType.ASSIGN_ID)
    String id;

    String conName;

    String conType;

    String conAction;

    String sourceId;

    String sourceName;

    String objectId;

    String objectName;

    String description;

    Date syncTime;

    String result;

    Date startDate;

    Date endDate;

	private String instId;

	private String instName;

}

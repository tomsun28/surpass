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
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * .
 * @author Crystal.Sea
 *
 */

@Data
@NoArgsConstructor
@TableName("JBX_HISTORY_SYSTEM_LOGS")
public class HistorySystemLogs implements Serializable {

    @Serial
    private static final long serialVersionUID = 6560201093784960493L;

    @TableId(type = IdType.ASSIGN_ID)
    String id;

    String topic;

    String message;

    String messageAction;

    String messageResult;

    String targetId;

    String targetName;

    String cipherText;

    String userId;

    String username;

    String displayName;

    Date executeTime;

	private String bookId;

    @TableField(exist = false)
	String jsonCotent;

    @TableField(exist = false)
	private String instName;

    @TableField(exist = false)
	Date startDate;

    @TableField(exist = false)
	Date endDate;

}

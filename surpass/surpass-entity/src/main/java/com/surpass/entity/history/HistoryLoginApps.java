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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.surpass.entity.BaseEntity;

/**
 * @author Crystal.Sea
 *
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("surpass_history_login_APPS")
public class HistoryLoginApps extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = -1600208317188910376L;

	@TableId(type = IdType.ASSIGN_ID)
	String id;

	private String sessionId;

	private String appId;

	private String appName;

	private String userId;

	private String username;

	private String displayName;

	private Date loginTime;

	private String instId;

	private String instName;

	Date startDate;

	Date endDate;

}

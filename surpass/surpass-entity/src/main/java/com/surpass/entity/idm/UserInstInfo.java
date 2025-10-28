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


package com.surpass.entity.idm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.surpass.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/13 11:27
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("JBX_USERINFO")
public class UserInstInfo extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 7092456146043154933L;

    @TableId(type = IdType.ASSIGN_ID)
    String id;
}

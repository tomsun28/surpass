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






package com.surpass.entity.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Crystal.Sea
 *
 */

@Data
@NoArgsConstructor
@TableName("surpass_config_password_policy")
public class ConfigPasswordPolicy implements Serializable {

    @Serial
    private static final long serialVersionUID = -4797776994287829182L;

    @TableId(type = IdType.ASSIGN_ID)
    String id;

    /**
     * minimum password lengths
     */

    @NotNull
    private int minLength;

    /**
     * maximum password lengths
     */
    @NotNull
    private int maxLength;

    /**
     * least lowercase letter
     */
    @NotNull
    private int lowerCase;

    /**
     * least uppercase letter
     */
    @NotNull
    private int upperCase;

    /**
     * inclusion of numerical digits
     */
    @NotNull
    private int digits;

    /**
     * inclusion of special characters
     */
    @NotNull
    private int specialChar;

    /**
     * require users to change passwords periodically
     */
    private int expiration;

    /**
     * 0 no 1 yes
     */
    private int username;

    /**
     * not include password list
     */
    private int history;

    private int dictionary;

    private int alphabetical;

    private int numerical;

    private int qwerty;

    private int occurances;

    @TableField(exist = false)
    private int randomPasswordLength = 12;

    @TableField(exist = false)
    List<String> policMessageList;

}

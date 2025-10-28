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






package com.surpass.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数控件的节点 使用HashMap<String,Object> attr存储节点数据.
 *
 * @author Crystal.Sea
 *
 */

@Data
@NoArgsConstructor
public class TreeNode {
	String key;
	String code;
	String title;

	String codePath;
	String namePath;

	String parentKey;
	String parentCode;
	String parentTitle;


	boolean expanded;
	boolean isLeaf;

    // TreeNode
    Object attrs;

    public TreeNode(String key, String title) {
        this.key = key;
        this.title = title;
    }

}

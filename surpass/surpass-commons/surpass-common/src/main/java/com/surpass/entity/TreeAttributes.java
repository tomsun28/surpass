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

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数控件节点列表 列表的元素为TreeNode
 *
 * @author Crystal.Sea
 *
 */

@Data
@NoArgsConstructor
public class TreeAttributes {

	TreeNode rootNode;

	int nodeCount;

	List<TreeNode> nodes = new ArrayList<>();

	/**
	 * 新增节点到列表
	 *
	 * @param treeNode
	 */
	public void addNode(TreeNode treeNode) {
		this.nodes.add(treeNode);
	}

}

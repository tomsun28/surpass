package org.dromara.surpass.util;


import java.util.ArrayList;
import java.util.List;

import org.dromara.surpass.pojo.BaseTreeNode;

/**
 * 树结构相关工具类
 * @author tomsun28
 * @date 16:55 2018/3/20
 */
public class TreeUtil {


    /**
     * description 用双重循环建树
     *
     * @param treeNodes 1 树节点列表, root根标志
     * @param root 2
     * @return java.util.List<T>
     */
    public static <T extends BaseTreeNode> List<T> buildTreeBy2Loop(List<T> treeNodes, Object root) {
        List<T> trees = new ArrayList<>();
        for (T node : treeNodes) {
            if (root.equals(node.getParentId())) {
                trees.add(node);
            }

            for (T treeNode : treeNodes) {
                if (node.getId().equals(treeNode.getParentId())) {
                    if (node.getChildren()==null) {
                        node.setChildren(new ArrayList<>());
                    }
                    node.addChildren(treeNode);
                }
            }
        }
        return trees;
    }

}

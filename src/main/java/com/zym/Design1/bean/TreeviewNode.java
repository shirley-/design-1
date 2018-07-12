package com.zym.Design1.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YM on 2018/3/13.
 */
public class TreeviewNode {
    private String text;
    private Integer id;
    private List<TreeviewNode> nodes;

    public TreeviewNode(Integer keyId, String info) {
        id = keyId;
        text = info;
        nodes = new ArrayList<TreeviewNode>();
    }

    public void addChildNode(Integer childId, String childText) {
        TreeviewNode node = new TreeviewNode(childId, childText);
        nodes.add(node);
    }
    public Integer getKeyId() {
        return id;
    }
    public List<TreeviewNode> getChildrenNodes() {
        return nodes;
    }
    public String getNodeInfo() {
        return text;
    }
}

class Treeview{
    private TreeviewNode root;
    private TreeviewNode tmp;
    public Treeview(Integer keyId, String info) {
        root = new TreeviewNode(keyId,info);
        tmp = root;
    }
    public TreeviewNode getRoot() {
        return root;
    }
    public void addChildNodeToKey(Integer childId, String childText, Integer keyId) {
        TreeviewNode newNode = new TreeviewNode(childId, childText);
        tmp = root;
        TreeviewNode parent = getNodeOf(keyId);
        parent.addChildNode(childId, childText);
    }
    public TreeviewNode getNodeOf(Integer keyId) {
        if(keyId == tmp.getKeyId()) {
            return tmp;
        }else{
            for(TreeviewNode node : tmp.getChildrenNodes()) {
                tmp = node;
                return getNodeOf(keyId);
            }
        }
        return null;
    }
    public void displayTree() {
        tmp = root;
        System.out.println(tmp.getNodeInfo());
        for(TreeviewNode node : tmp.getChildrenNodes()) {
            TreeviewNode n = node;
            displayTree();
        }
    }
//    public void

}



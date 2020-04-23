package ConditionTree;

import java.util.ArrayList;

import Database.Table;

public abstract class Node {
    Node leftNode;
    Node rightNode;

    public abstract ArrayList<Integer> returnIndices(Table table);

}
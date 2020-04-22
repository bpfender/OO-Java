package ConditionTree;

import java.util.ArrayList;

import Expression.Context;

abstract class Node {
    Node leftNode;
    Node rightNode;

    public abstract ArrayList<Integer> returnIndices(Context context);
}
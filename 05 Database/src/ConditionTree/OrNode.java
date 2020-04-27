package ConditionTree;

import java.util.ArrayList;

import Database.Table;

public class OrNode extends Node {

    public OrNode(Node leftNode, Node rightNode) {
        super(leftNode, rightNode);
        // TODO Auto-generated constructor stub
    }

    @Override
    public ArrayList<Integer> returnIndices(Table table) {
        ArrayList<Integer> leftResult = leftNode.returnIndices(table);
        ArrayList<Integer> rightResult = rightNode.returnIndices(table);

        if (leftResult == null || rightResult == null) {
            return null;
        }

        leftResult.addAll(rightResult);
        return leftResult;
        // QUESTION Sorting uion?
    }

}
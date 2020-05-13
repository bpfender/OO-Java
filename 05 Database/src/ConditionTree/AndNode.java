package ConditionTree;

import java.util.ArrayList;

import Database.Table;

public class AndNode extends Node {
    public AndNode(Node leftNode, Node rightNode) {
        super(leftNode, rightNode);
    }

    @Override
    public ArrayList<Integer> returnIndices(Table table) throws RuntimeException {
        ArrayList<Integer> leftResult = leftNode.returnIndices(table);
        ArrayList<Integer> rightResult = rightNode.returnIndices(table);

        leftResult.retainAll(rightResult);
        return leftResult;
    }

}
package ConditionTree;

import java.util.ArrayList;

import Database.Table;

public class OrNode extends Node {

    public OrNode(Node leftNode, Node rightNode) {
        super(leftNode, rightNode);
    }

    @Override
    public ArrayList<Integer> returnIndices(Table table) throws RuntimeException {
        ArrayList<Integer> leftResult = leftNode.returnIndices(table);
        ArrayList<Integer> rightResult = rightNode.returnIndices(table);

        leftResult.addAll(rightResult);
        return leftResult;
    }

}
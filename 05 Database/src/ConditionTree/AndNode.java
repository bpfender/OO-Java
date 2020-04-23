package ConditionTree;

import java.util.ArrayList;

import Database.Table;

public class AndNode extends Node {

    @Override
    public ArrayList<Integer> returnIndices(Table table) {
        ArrayList<Integer> leftResult = leftNode.returnIndices(table);
        ArrayList<Integer> rightResult = rightNode.returnIndices(table);

        if (leftResult == null || rightResult == null) {
            return null;
        }

        // QUESTION not the nicest phrasing
        leftResult.retainAll(rightResult);
        return leftResult;
    }

}
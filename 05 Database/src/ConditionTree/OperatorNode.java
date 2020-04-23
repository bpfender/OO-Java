package ConditionTree;

import java.util.ArrayList;
import java.util.function.Predicate;

import Database.*;

public class OperatorNode extends Node {
    String attribute;
    String value;
    Predicate<String> comparison;
    ArrayList<Integer> indices = new ArrayList<>();

    public OperatorNode(String attribute, String value, Predicate<String> comparison) {
        this.attribute = attribute;
        this.value = value;
        this.comparison = comparison;
    }

    @Override
    public ArrayList<Integer> returnIndices(Table table) {
        Column column = table.getColumn(attribute);

        if (column == null) {
            return null;
        }

        ArrayList<String> values = column.getColumnValues();

        for (int i = 0; i < values.size(); i++) {
            if (comparison.test(values.get(i))) {
                indices.add(i);
            }
        }

        return indices;

    }

}
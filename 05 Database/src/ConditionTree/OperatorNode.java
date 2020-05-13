package ConditionTree;

import java.util.ArrayList;
import java.util.function.Predicate;

import Database.Column;
import Database.Table;

// Operator node is terminal node. It takes in a predicate which is selected by the parser and
// corresponds to the comparison type,as well the attribute that the comparsion is performed on
public class OperatorNode extends Node {
    String attribute;
    Predicate<String> comparison;
    ArrayList<Integer> indices = new ArrayList<>();

    public OperatorNode(String attribute, Predicate<String> comparison) {
        super(null, null); // No child nodes, so left and right set to null
        this.attribute = attribute;
        this.comparison = comparison;
    }

    @Override
    public ArrayList<Integer> returnIndices(Table table) throws RuntimeException {
        // Catches exception from get column to validate attribute. Throws different
        // return for more detailed diagnostics
        try {
            Column column = table.getColumn(attribute);

            for (int i = 0; i < table.getTableSize(); i++) {
                if (comparison.test(column.getValue(i))) {
                    indices.add(i);
                }
            }

            return indices;

        } catch (RuntimeException e) {
            throw new RuntimeException("ERROR: Invalid attribute " + attribute + "in WHERE clause.");
        }

    }

}
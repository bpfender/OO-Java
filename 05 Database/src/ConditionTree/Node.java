package ConditionTree;

import java.util.ArrayList;

import Database.Table;

// Where conditions are built up as a tree structure by the parser, which can then be 
// traversed by calling returnIndices() to generate the list of indices that corresponmd 
// Terminal nodes are operator nodes, which calculate indices based on the predicate operator,
//  while and and or nodes combine these returned indices in the relvant way
public abstract class Node {
    Node leftNode;
    Node rightNode;

    public Node(Node leftNode, Node rightNode) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public abstract ArrayList<Integer> returnIndices(Table table) throws RuntimeException;

}
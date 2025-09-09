/**
 * This class implements a binary search tree that maintains sorted ordering
 * of its contents. Each node's value is greater than or equal to all values
 * in its left subtree and strictly less than all values in its right subtree.
 */
public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {
    // Reference to the root node of the tree
    protected BinaryNode<T> root;
    /**
     * No-argument constructor that creates an empty binary search tree.
     */
    public BinarySearchTree() {
        this.root = null;
    }

    /**
     * Inserts a new data value into the sorted collection.
     * @param data the new value being inserted
     * @throws NullPointerException if data argument is null
     */
    @Override
    public void insert(T data) throws NullPointerException {
        if (data == null) {
            throw new NullPointerException("Cannot insert null data into the tree");
        }

        BinaryNode<T> newNode = new BinaryNode<>(data);

        if (root == null) {
            root = newNode;
        } else {
            insertHelper(newNode, root);
        }
    }
    /**
     * Performs the naive binary search tree insert algorithm to recursively
     * insert the provided newNode (which has already been initialized with a
     * data value) into the provided tree/subtree. When the provided subtree
     * is null, this method does nothing.
     */
    protected void insertHelper(BinaryNode<T> newNode, BinaryNode<T> subtree) {
        if (subtree == null) {
            return;
        }

        // Compare the new node's data with the current subtree's data
        int comparison = newNode.getData().compareTo(subtree.getData());

        if (comparison <= 0) { // Insert in left subtree (including duplicates)
            if (subtree.getLeft() == null) {
                subtree.setLeft(newNode);
                newNode.setParent(subtree);
            } else {
                insertHelper(newNode, subtree.getLeft());
            }
        } else { // Insert in right subtree
            if (subtree.getRight() == null) {
                subtree.setRight(newNode);
                newNode.setParent(subtree);
            } else {
                insertHelper(newNode, subtree.getRight());
            }
        }
    }
    /**
     * Check whether data is stored in the tree.
     * @param data the value to check for in the collection
     * @return true if the collection contains data one or more times, 
     *         and false otherwise
     * @throws NullPointerException if data argument is null
     */
    @Override
    public boolean contains(Comparable<T> data) throws NullPointerException {
        if (data == null) {
            throw new NullPointerException("Cannot search for null data in the tree");
        }

        return containsHelper(data, root);
    }
    /**
     * Helper method to recursively search for a value in the tree.
     * @param data the value to search for
     * @param subtree the current subtree to search in
     * @return true if the value is found, false otherwise
     */
    private boolean containsHelper(Comparable<T> data, BinaryNode<T> subtree) {
        if (subtree == null) {
            return false;
        }

        int comparison = data.compareTo(subtree.getData());

        if (comparison == 0) {
            return true;
        } else if (comparison < 0) {
            return containsHelper(data, subtree.getLeft());
        } else {
            return containsHelper(data, subtree.getRight());
        }
    }

    /**
     * Counts the number of values in the collection, with each duplicate value
     * being counted separately within the value returned.
     * @return the number of values in the collection, including duplicates
     */
    @Override
    public int size() {
        return sizeHelper(root);
    }

    /**
     * Helper method to recursively count nodes in the tree.
     * @param subtree the current subtree to count
     * @return the number of nodes in the subtree
     */
    private int sizeHelper(BinaryNode<T> subtree) {
        if (subtree == null) {
            return 0;
        }

        return 1 + sizeHelper(subtree.getLeft()) + sizeHelper(subtree.getRight());
    }

    /**
     * Checks if the collection is empty.
     * @return true if the collection contains 0 values, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Removes all values and duplicates from the collection.
     */
    @Override
    public void clear() {
        root = null;
    }

    // TEST METHODS
    /**
     * test 1: tests insertion of multiple integer values to create multiple different shapes of trees
     * @return true if test passes, false otherwise
     */
    public boolean test1() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();

        // Test empty tree
        if (!tree.isEmpty() || tree.size() != 0) {
            return false;
        }
        // Insert values to create a specific tree shape: 5 as root, 3 and 7 as children
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        tree.insert(1);
        tree.insert(4);
        tree.insert(6);
        tree.insert(9);

        // Test size
        if (tree.size() != 7) {
            return false;
        }
        // Test contains for root
        if (!tree.contains(5)) {
            return false;
        }
        // Test contains for left and right children
        if (!tree.contains(3) || !tree.contains(7)) {
            return false;
        }
        // Test contains for leaves
        if (!tree.contains(1) || !tree.contains(4) || !tree.contains(6) || !tree.contains(9)) {
            return false;
        }
        // Test contains for non-existent value
        if (tree.contains(2) || tree.contains(8)) {
            return false;
        }

        return true;
    }
    /**
     * test 2: similar to test 1 however uses string integers to create multiple shapes of trees and tests catching duplicates
     * @return true if test passes, false otherwise
     */
    public boolean test2() {
        BinarySearchTree<String> tree = new BinarySearchTree<>();

        // Insert strings in different order to create different tree shape
        tree.insert("dog");
        tree.insert("cat");
        tree.insert("elephant");
        tree.insert("ant");
        tree.insert("dog"); // duplicate - should go to left subtree

        // Test size includes duplicates
        if (tree.size() != 5) {
            return false;
        }
        // Test contains works with duplicates
        if (!tree.contains("dog")) {
            return false;
        }
        // Test all inserted values are found
        if (!tree.contains("cat") || !tree.contains("elephant") || !tree.contains("ant")) {
            return false;
        }
        // Test non-existent value
        if (tree.contains("zebra")) {
            return false;
        }
        return true;
    }
    /**
     * test 3: tests clear method and building different tree structures (left/right skewed).
     * Also tests edge cases like single node tree.
     * @return true if test passes, false otherwise
     */
    public boolean test3() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();

        // Test single node tree
        tree.insert(42);
        if (tree.size() != 1 || !tree.contains(42) || tree.isEmpty()) {
            return false;
        }
        // Clear and test empty tree
        tree.clear();
        if (!tree.isEmpty() || tree.size() != 0 || tree.contains(42)) {
            return false;
        }
        // Build a left skewed tree
        tree.insert(10);
        tree.insert(8);
        tree.insert(6);
        tree.insert(4);

        if (tree.size() != 4) {
            return false;
        }
        // Test all values are accessible
        if (!tree.contains(10) || !tree.contains(8) || !tree.contains(6) || !tree.contains(4)) {
            return false;
        }
        tree.clear();

        // Build a right skewed tree
        tree.insert(1);
        tree.insert(3);
        tree.insert(5);
        tree.insert(7);

        if (tree.size() != 4) {
            return false;
        }
        // Test all values are accessible
        if (!tree.contains(1) || !tree.contains(3) || !tree.contains(5) || !tree.contains(7)) {
            return false;
        }
        return true;
    }
    /**
     * Main method to run all tests and display results.
     */
    public static void main(String[] args) {
        BinarySearchTree<Integer> testTree = new BinarySearchTree<>();

        System.out.println("Running Binary Search Tree Tests...");
        System.out.println();

        // Run test1
        boolean test1Result = testTree.test1();
        System.out.println("Test 1 (Integer tree with various shapes): " +
                (test1Result ? "Passed" : "Failed"));

        // Run test2
        boolean test2Result = testTree.test2();
        System.out.println("Test 2 (String tree with duplicates): " +
                (test2Result ? "Passed" : "Failed"));

        // Run test3
        boolean test3Result = testTree.test3();
        System.out.println("Test 3 (Skewed trees with linked lists): " +
                (test3Result ? "Passed" : "Failed"));

        System.out.println();
        int passedTests = (test1Result ? 1 : 0) + (test2Result ? 1 : 0) +
                (test3Result ? 1 : 0);
        System.out.println("Tests passed: ");

        if (passedTests == 3) {
            System.out.println("All tests passed.");
        } else {
            System.out.println("Some tests failed. Review implementation.");
        }
    }
}
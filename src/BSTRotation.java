public class BSTRotation <T extends Comparable<T>> extends BinarySearchTree_Placeholder<T>{
    public BSTRotation() {
        super();
    }
    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a right
     * child of the provided parent, this method will perform a left rotation.
     * When the provided nodes are not related in one of these ways, this
     * method will either throw a NullPointerException: when either reference is
     * null, or otherwise will throw an IllegalArgumentException.
     *
     * @param child is the node being rotated from child to parent position
     * @param parent is the node being rotated from parent to child position
     * @throws NullPointerException when either passed argument is null
     * @throws IllegalArgumentException when the provided child and parent
     *     nodes are not initially (pre-rotation) related that way
     */
    protected void rotate(BinaryNode<T> child, BinaryNode<T> parent)
            throws NullPointerException, IllegalArgumentException {
        // TODO: Implement this method.

        // confirm child parent relationships
        if (child == null || parent == null) {
            throw new NullPointerException();
        }
        if (parent.getLeft() != child && parent.getRight() != child) {
            throw new IllegalArgumentException();
        }
        //creates grandparent (parent's parent) to reattach rotated subtrees back to original trees
        BinaryNode<T> grandparent = parent.getParent();
        boolean parentWasLeftChild = (grandparent != null && grandparent.getLeft() == parent);

        if (parent.getLeft() == child) {
            performRightRotation(child, parent, grandparent, parentWasLeftChild);
        } else {
            performLeftRotation(child, parent, grandparent, parentWasLeftChild);
        }
    }
    private void performRightRotation(BinaryNode<T> child, BinaryNode<T> parent, BinaryNode<T> grandparent, boolean parentWasLeftChild) {
        BinaryNode<T> childRightSubtree = child.getRight();

        // Make child the new parent
        child.setParent(grandparent);
        child.setRight(parent);

        // Update parent to become right child of child
        parent.setParent(child);
        parent.setLeft(childRightSubtree);

        // Update child's former right subtree
        if (childRightSubtree != null) {
            childRightSubtree.setParent(parent);
        }

        // Update grandparent's reference or root
        if (grandparent == null) {
            this.root = child;
        } else if (parentWasLeftChild) {
            grandparent.setLeft(child);
        } else {
            grandparent.setRight(child);
        }
    }
    private void performLeftRotation(BinaryNode<T> child, BinaryNode<T> parent,
                                     BinaryNode<T> grandparent, boolean parentWasLeftChild) {
        // Store child's left subtree
        BinaryNode<T> childLeftSubtree = child.getLeft();

        // Make child the new parent
        child.setParent(grandparent);
        child.setLeft(parent);

        // Update parent to become left child of child
        parent.setParent(child);
        parent.setRight(childLeftSubtree);

        // Update child's former left subtree
        if (childLeftSubtree != null) {
            childLeftSubtree.setParent(parent);
        }

        // Update grandparent's reference or root
        if (grandparent == null) {
            this.root = child;
        } else if (parentWasLeftChild) {
            grandparent.setLeft(child);
        } else {
            grandparent.setRight(child);
        }
    }

    //tests 1-3, will return true if successful or false if not
    /**
     * Test 1: Tests both left and right rotations on a simple tree.
     * @return true if the test passes, false otherwise
     */
    public boolean test1() {
        try {
            // creates a simple tree: 2 (root) with left child 1 and right child 3 to handle simple left/right rotations
            BinaryNode<T> node2 = new BinaryNode<>((T) Integer.valueOf(2));
            BinaryNode<T> node1 = new BinaryNode<>((T) Integer.valueOf(1));
            BinaryNode<T> node3 = new BinaryNode<>((T) Integer.valueOf(3));

            this.root = node2;
            node2.setLeft(node1);
            node2.setRight(node3);
            node1.setParent(node2);
            node3.setParent(node2);

            // test right rotation: rotate 1 (left child) with 2 (parent)
            rotate(node1, node2);

            if (this.root != node1 || node1.getRight() != node2 || node2.getParent() != node1) {
                return false;
            }
            // test left rotation: rotate 2 (right child) with 1 (parent)
            rotate(node2, node1);

            if (this.root != node2 || node2.getLeft() != node1 || node1.getParent() != node2) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * Test 2: Tests rotations involving the root and non-root nodes.
     * @return true if the test passes, false otherwise
     */
    public boolean test2() {
        try {
            // create tree: 4 (root) with left 2, right 6, and 2 has left 1, right 3
            BinaryNode<T> node4 = new BinaryNode<>((T) Integer.valueOf(4));
            BinaryNode<T> node2 = new BinaryNode<>((T) Integer.valueOf(2));
            BinaryNode<T> node6 = new BinaryNode<>((T) Integer.valueOf(6));
            BinaryNode<T> node1 = new BinaryNode<>((T) Integer.valueOf(1));
            BinaryNode<T> node3 = new BinaryNode<>((T) Integer.valueOf(3));

            this.root = node4;
            node4.setLeft(node2);
            node4.setRight(node6);
            node2.setParent(node4);
            node6.setParent(node4);
            node2.setLeft(node1);
            node2.setRight(node3);
            node1.setParent(node2);
            node3.setParent(node2);

            // test rotation involving root: rotate 2 with 4 (right rotation at root)
            rotate(node2, node4);

            // after rotation: 2 should be root, 4 should be its right child, 3 should be left child of 4
            if (this.root != node2 || node2.getRight() != node4 || node4.getParent() != node2 || node4.getLeft() != node3) {
                return false;
            }

            // create a fresh tree for second test to avoid complexity
            // tree created has 10 with left child 5, and 5 has right child 7
            BinaryNode<T> node10 = new BinaryNode<>((T) Integer.valueOf(10));
            BinaryNode<T> node5 = new BinaryNode<>((T) Integer.valueOf(5));
            BinaryNode<T> node7 = new BinaryNode<>((T) Integer.valueOf(7));

            this.root = node10;
            node10.setLeft(node5);
            node5.setParent(node10);
            node5.setRight(node7);
            node7.setParent(node5);

            // test rotation not involving root: rotate 7 with 5 (left rotation)
            rotate(node7, node5);

            // after rotation: 7 should be left child of 10, 5 should be left child of 7
            if (node10.getLeft() != node7 || node7.getLeft() != node5 || node5.getParent() != node7) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Test 3 Tests exception handling for invalid inputs.
     * @return true if the test passes, false otherwise
     */
    public boolean test3() {
        try {
            BinaryNode<T> node1 = new BinaryNode<>((T) Integer.valueOf(1));
            BinaryNode<T> node2 = new BinaryNode<>((T) Integer.valueOf(2));
            BinaryNode<T> unrelated = new BinaryNode<>((T) Integer.valueOf(99));

            node1.setRight(node2);
            node2.setParent(node1);

            // test NullPointerException
            try {
                rotate(null, node1);
                return false;
            } catch (NullPointerException e) {}

            try {
                rotate(node1, null);
                return false;
            } catch (NullPointerException e) {}

            // test IllegalArgumentException for unrelated nodes
            try {
                rotate(unrelated, node1);
                return false;
            } catch (IllegalArgumentException e) {}
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Main method to run all tests and display true or false.
     */
    public static void main(String[] args) {
        BSTRotation<Integer> bst = new BSTRotation<>();

        System.out.println("Running BST Rotation Tests");
        System.out.println("Test 1 (Left/Right Rotations): " + (new BSTRotation<Integer>()).test1());
        System.out.println("Test 2 (Root/Non-root Rotations): " + (new BSTRotation<Integer>()).test2());
        System.out.println("Test 3 (Exception Handling): " + (new BSTRotation<Integer>()).test3());
    }
}

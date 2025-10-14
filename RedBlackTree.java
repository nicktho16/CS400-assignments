import java.util.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {

     /**
     * Insert a new value into the Red-Black Tree.
     * Always inserts as a red RedBlackNode, then repairs red property violations.
     * @param value the value to insert into the tree
     */
    @Override
    public void insert(T value) {
        if (value == null) {
            throw new NullPointerException("Cannot insert null into the RedBlackTree");
        }

        // Create a new red RedBlackNode to insert
        RedBlackNode<T> newNode = new RedBlackNode<>(value);
        newNode.isBlackNode = false; // new nodes start red

        // Insert into the tree using BinarySearchTree’s insertHelper
        if (this.root == null) {
            this.root = newNode;
        } else {
            insertHelper((RedBlackNode<T>) this.root, newNode);
        }

        // If not the root, fix potential red property violation
        if (newNode != this.root) {
            ensureRedProperty(newNode);
        }

        // Root must always be black
        ((RedBlackNode<T>) this.root).isBlackNode = true;
    }

        /**
 * Recursively inserts newNode into the tree rooted at current.
 * Sets parent references properly.
 */
private void insertHelper(RedBlackNode<T> current, RedBlackNode<T> newNode) {
    if (newNode.getData().compareTo(current.getData()) < 0) {
        // Go left
        if (current.getLeft() == null) {
            current.setLeft(newNode);
            newNode.setParent(current);
        } else {
            insertHelper((RedBlackNode<T>) current.getLeft(), newNode);
        }
    } else {
        // Go right
        if (current.getRight() == null) {
            current.setRight(newNode);
            newNode.setParent(current);
        } else {
            insertHelper((RedBlackNode<T>) current.getRight(), newNode);
        }
    }
}

         /**
     * Checks if a new red node in the RedBlackTree causes a red property violation
     * by having a red parent. If this is not the case, the method terminates without
     * making any changes to the tree. If a red property violation is detected, then
     * the method repairs this violation and any additional red property violations
     * that are generated as a result of the applied repair operation.
     * Using this method might cause nodes with a value equal to the value of one of
     * their ancestors to appear within the left and the right subtree of that ancestor,
     * even if the original insertion procedure consistently inserts such nodes into only
     * the left or the right subtree. But it will preserve the ordering of nodes within
     * the tree.
     * @param newNode a newly inserted red node, or a node turned red by previous repair
     */
    protected void ensureRedProperty(RedBlackNode<T> newNode) {

           //case 0 - newNode is root so nothing to do
            if (newNode == this.root)
              return;

            RedBlackNode<T> parent = newNode.getParent();
            if (parent == null || parent.isBlackNode)
              return; //if parent node is black, do nothing, no violation

           // if parent is red, violation exists
        RedBlackNode<T> grandparent = parent.getParent();
             if (grandparent == null) return;

        // find uncle
        RedBlackNode<T> uncle = (grandparent.getLeft() == parent)
                ? (RedBlackNode<T>) grandparent.getRight()
                : (RedBlackNode<T>) grandparent.getLeft();


        // Case 1: Uncle is red so recolor and recurse on grandparent
        if (uncle != null && !uncle.isBlackNode) {
            parent.isBlackNode = true;
            uncle.isBlackNode = true;
            grandparent.isBlackNode = false;
            ensureRedProperty(grandparent);
            return;
        }

        // Case 2: Uncle is black or null
        if (parent == grandparent.getLeft()) {
            if (newNode == parent.getRight()) {
                // Left-Right case
                rotate(newNode, parent);
                newNode = parent;
                parent = newNode.getParent();
            }
            // Left-Left case
            rotate(parent, grandparent);
            parent.isBlackNode = true;
            grandparent.isBlackNode = false;
        } else { // parent is right child
            if (newNode == parent.getLeft()) {
                // Right-Left case
                rotate(newNode, parent);
                newNode = parent;
                parent = newNode.getParent();
            }
            // Right-Right case
            rotate(parent, grandparent);
            parent.isBlackNode = true;
            grandparent.isBlackNode = false;
        }
    }

     //JUnit test methods

     /**
     * Test inserting into an empty RedBlackTree.
     * Ensures that the root is black after insertion.
     */
    @Test
    public void testInsertIntoEmptyTree() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(10);
        assertTrue(((RedBlackNode<Integer>) tree.root).isBlackNode, "Root should be black after insertion");
    }

     /**
     * Test inserting two nodes into the RedBlackTree.
     * Ensures parent and child relationship is correct and colors are valid.
     */
    @Test
    public void testInsertTwoNodes() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(10);
        tree.insert(5);

        RedBlackNode<Integer> root = (RedBlackNode<Integer>) tree.root;
        RedBlackNode<Integer> child = (RedBlackNode<Integer>) root.getLeft();

        assertEquals(10, root.data);
        assertEquals(5, child.data);
        assertTrue(root.isBlackNode, "Root should always be black");
        assertFalse(child.isBlackNode, "New child should be red before any fix");
    }
 }   

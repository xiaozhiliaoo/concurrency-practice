package org.douglea;

/**
 * @author lili
 * @date 2020/4/8 10:25
 * @description
 * @notes http://gee.cs.oswego.edu/dl/code/TernarySearchTrieSet.java
 */


/*
 * Written by Doug and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
/**
 * Adaptations and extensions of Ternary Search Trie
 * algorithms presented by Bentley and Sedgewick,
 * <a href="http://www.cs.princeton.edu/~rs/strings/index.html">
 * Fast Algorithms for Sorting and Searching Strings</a>,
 * Eighth Annual ACM-SIAM Symposium on Discrete Algorithms
 * New Orleans, January, 1997.
 *
 * last update Mon Sep  4 16:29:44 2000  Doug Lea  (dl at gee)
 * Todo: SortedSet ops, maybe balancing, map version.
 *
 * Notes: This was written pre-JDK1.5, and so, the tie-in tactic for
 * claiming they are collections is a little sleazy, but probably the
 * best that can be done: Attempts to add a non-String produce
 * UnsupportedOperationException.
 *
 * In very light tests, this seems about 25% faster than redblack
 * trees of Strings in most cases, although the worst-case isn't so
 * good. And unfortunately, the worst case is a typical one: inserting
 * words that are already in sorted order. I ought to do something
 * about this.
 **/

import java.util.*;

public class TernarySearchTrieSet extends AbstractSet implements Cloneable, java.io.Serializable {

    /**
     * An artifical string terminator; must be less than any valid char.
     * To allow any char to appear in a string (even 0), we use
     * an int. Since chars are unsigned, any negative value will do.
     **/
    protected static final int TERMINATOR = -1;


    /**
     * Return i'th char of s, or TERMINATOR, if no such.
     * This allows us to pretend that all strings are padded with TERMINATOR,
     * which makes the rest of the code much simpler.
     **/
    protected static int getChar(int i, String s) {
        return (i >= s.length())? TERMINATOR : s.charAt(i);
    }

    /**
     * Node class for tries
     **/

    protected static class Node {
        /** The string, used only if a terminal node **/
        String string;
        /** character to compare to at this level **/
        int split;
        /** Nodes less than split **/
        Node left;
        /** Nodes greater than split **/
        Node right;
        /** Nodes prefixed by split **/
        Node eq;

        protected Node(int c) { split = c; }
    }

    /**
     * The number of elements
     **/
    private transient int count;

    /**
     * The number of structural modifications to the tree.
     */

    private transient int modCount;


    /**
     * The root. Null when empty.
     **/

    private transient Node root;

    /**
     * Constructs a new, empty set
     */

    public TernarySearchTrieSet() {
    }


    /**
     * Returns the number of elements in this set (its cardinality).
     *
     * @return the number of elements in this set (its cardinality).
     */
    public int size() {
        return count;
    }

    /**
     * Returns <tt>true</tt> if this set contains no elements.
     *
     * @return <tt>true</tt> if this set contains no elements.
     */
    public boolean isEmpty() {
        return count == 0;
    }


    /**
     * Returns <tt>true</tt> if this set contains the specified element.
     *
     * @param x element whose presence in this set is to be tested.
     * @return <tt>true</tt> if this set contains the specified element.
     */
    public boolean contains(Object x) {
        if (!(x instanceof String))
            return false;
        String s = (String)x;

        int i = 0;
        int c = getChar(i, s);
        Node p = root;

        for (;;) {
            if (p == null)
                return false;
            else if (c == p.split) {
                if (c == TERMINATOR)
                    return true;
                else {
                    c = getChar(++i, s);
                    p = p.eq;
                }
            }
            else if (c < p.split)
                p = p.left;
            else
                p = p.right;
        }
    }

    /**
     * Adds the specified element to this set if it is not already
     * present. Only Strings may be added.
     *
     * @param x element to be added to this set.
     * @return <tt>true</tt> if the set did not already contain the specified
     * element.
     * @throws UnsupportedOperationException if x is not a String.
     */

    public boolean xadd(Object x) {
        if (!(x instanceof String))
            throw new UnsupportedOperationException();

        String s = (String)x;
        int i = 0;
        int c = getChar(i, s);

        if (root == null) root = new Node(c);
        Node p = root;

        for (;;) {

            if (c < p.split) {
                if (p.left == null) p.left = new Node(c);
                p = p.left;
            }

            else if (c > p.split) {
                if (p.right == null) p.right = new Node(c);
                p = p.right;
            }

            else if (c != TERMINATOR) {
                c = getChar(++i, s);
                if (p.eq == null) p.eq = new Node(c);
                p = p.eq;
            }

            else if (p.string != null)
                return false;

            else {
                p.string = s;
                ++count;
                ++modCount;
                return true;
            }
        }
    }

    public boolean add(Object x) {
        if (!(x instanceof String))
            throw new UnsupportedOperationException();

        String s = (String)x;
        int i = 0;
        int c = getChar(i, s);

        if (root == null) root = new Node(c);
        Node p = root;
        Node parent = null;

        for (;;) {

            if (c < p.split) {
                if (p.left == null) p.left = new Node(c);
                parent = p;
                p = p.left;
            }

            else if (c > p.split) {
                if (p.right == null) {
                    Node n = new Node(c);
                    if (p.split == TERMINATOR) {
                        n.left = p;
                        if (parent == null) root = n;
                        else if (p == parent.left) parent.left = n;
                        else if (p == parent.right) parent.right = n;
                        else parent.eq = n;
                        p = n;
                    }
                    else {
                        p.right = n;
                        parent = p;
                        p = n;
                    }
                }
                else {
                    parent = p;
                    p = p.right;
                }
            }

            else if (c != TERMINATOR) {
                c = getChar(++i, s);
                if (p.eq == null) p.eq = new Node(c);
                parent = p;
                p = p.eq;
            }

            else if (p.string != null)
                return false;

            else {
                p.string = s;
                ++count;
                ++modCount;
                return true;
            }
        }
    }

    public boolean xxadd(Object x) {
        if (!(x instanceof String))
            throw new UnsupportedOperationException();

        String s = (String)x;
        int i = 0;
        int c = getChar(i, s);

        if (root == null) root = new Node(c);
        Node p = root;
        Node parent = null;

        for (;;) {

            if (c < p.split) {
                if (p.left == null) {
                    Node n = new Node(c);
                    if (false && p.split != TERMINATOR && p.split > 'l') {
                        n.right = p;
                        if (parent == null) root = n;
                        else if (p == parent.left) parent.left = n;
                        else if (p == parent.right) parent.right = n;
                        else parent.eq = n;
                        p = n;
                    }
                    else {
                        p.left = n;
                        parent = p;
                        p = n;
                    }
                }
                else {
                    parent = p;
                    p = p.left;
                }
            }

            else if (c > p.split) {
                if (p.right == null) {
                    Node n = new Node(c);
                    if (p.split == TERMINATOR || p.split < 'l') {
                        n.left = p;
                        if (parent == null) root = n;
                        else if (p == parent.left) parent.left = n;
                        else if (p == parent.right) parent.right = n;
                        else parent.eq = n;
                        p = n;
                    }
                    else {
                        p.right = n;
                        parent = p;
                        p = n;
                    }
                }
                else {
                    parent = p;
                    p = p.right;
                }
            }

            else if (c != TERMINATOR) {
                c = getChar(++i, s);
                if (p.eq == null) p.eq = new Node(c);
                parent = p;
                p = p.eq;
            }

            else if (p.string != null)
                return false;

            else {
                p.string = s;
                ++count;
                ++modCount;
                return true;
            }
        }
    }



    /**
     * Removes the given element from this set if it is present.
     *
     * @param x object to be removed from this set, if present.
     * @return <tt>true</tt> if the set contained the specified element.
     */


    public boolean remove(Object x) {
        if (!(x instanceof String))
            return false;

        String s = (String)x;
        int mc = modCount;
        root = del(root, s, 0);
        return (modCount == mc+1);
    }

    /**
     *  Recursive helper method for remove();
     *  This probably wouldn't be noticeably faster, but would be
     *  more complicated, if done nonrecursively.
     **/

    private Node del(Node p, String s, int i) {
        if (p == null)
            return null;

        int c = getChar(i, s);

        if (c < p.split)
            p.left = del(p.left, s, i);
        else if (c > p.split)
            p.right = del(p.right, s, i);
        else if (c != TERMINATOR)
            p.eq = del(p.eq, s, i+1);
        else if (p.string != null) {
            p.string = null;
            --count;
            ++modCount;
        }

        // Unlink unnecessary nodes on way out of recursion
        if (p.eq == null && p.string == null) {
            if (p.right == null)
                return p.left;
            else if (p.left == null)
                return p.right;
            else { // Replace node with predecessor
                Node lpar = p.left;
                if (lpar.right == null) {
                    // Special-case when p.left is pred
                    lpar.right = p.right;
                    return lpar;
                }
                else {
                    // Copy in pred's fields, and override links
                    Node pred = lpar;
                    while (pred.right != null) {
                        lpar = pred;
                        pred = pred.right;
                    }
                    lpar.right = pred.left;
                    p.split = pred.split;
                    p.string = pred.string;
                    p.eq = pred.eq;
                    return p;
                }
            }
        }
        else
            return p;
    }

    /**
     * Removes all of the elements from this set.
     */
    public void clear() {
        root = null;
        count = 0;
        ++modCount;
    }

    /**
     * Return a list containing matches to pattern.
     * The pattern may contain number of occurrences
     * of a "don't care" character, that matches any character in
     * that position. For example,
     * partialMatches(".o.o.o", '.') might return [Pocono, rococo].
     * @param target - the pattern string
     * @param dontcare - the character to treat as a don't care symbol in target.
     *
     **/

    public List partialMatches(String pattern, char dontcare) {
        ArrayList list = new ArrayList();
        pmsearch(root, pattern, 0, dontcare, list);
        return list;
    }

    /**
     * Equivalent to partialMatches(pattern, '.');
     **/

    public List partialMatches(String pattern) {
        ArrayList list = new ArrayList();
        pmsearch(root, pattern, 0, '.', list);
        return list;
    }


    private void pmsearch(Node p, String s, int i,
                          char dontcare, ArrayList list) {
        if (p == null) return;

        int c = getChar(i, s);

        if (c == dontcare || c < p.split)
            pmsearch(p.left, s, i, dontcare, list);

        if ( (c == dontcare || c == p.split) &&
                (p.split != TERMINATOR && c != TERMINATOR))
            pmsearch(p.eq, s, i+1, dontcare, list);

        if (c == TERMINATOR && c == p.split)
            list.add(p.string);

        if (c == dontcare || c > p.split)
            pmsearch(p.right, s, i, dontcare, list);
    }


    /**
     * Return a list contining all words within a given
     * Hamming distance of target word. For example, (assuming that
     * these words are in the set)
     * nearMatches("soda", 0) returns [soda]<br>
     * nearMatches("soda", 1) returns [coda, sod, soda, sods, sofa,
     * soma, sora, soya]<br>
     * nearMatches("soda", 2) returns [Aida, Boca, Cody, Dada, Dodd, ...
     * bode, body, bona, coca, cod, coda, code, cola, coma, dodo, god, gods, ...
     * pods, rod, rode, rods, sad, saga, sedan, sera, side, sima, siva, so, ...
     * sow, sown, soy, soya, sud, suds, today, yoga].
     **/
    public List nearMatches(String target, int HammingDistance) {
        ArrayList list = new ArrayList();
        if (HammingDistance >= 0)
            nsearch(root, target, 0, HammingDistance, list);
        return list;
    }

    private void nsearch(Node p, String s, int i, int d, ArrayList list) {
        if (p == null) return;

        int c = getChar(i, s);

        if (d > 0 || c < p.split)
            nsearch(p.left, s, i, d, list);

        if (p.split == TERMINATOR) {
            if (s.length() - i <= d)
                list.add(p.string);
        }
        else {
            nsearch(p.eq, s,
                    (c == TERMINATOR)? i : (i+1),
                    (c == p.split)? d : (d-1),
                    list);
        }

        if (d > 0 || c > p.split)
            nsearch(p.right, s, i, d, list);
    }




    /**
     * Returns an iterator over the elements in this set.  The elements
     * are returned in String order.
     *
     * @return an Iterator over the elements in this set.
     * @see ConcurrentModificationException
     */
    public Iterator iterator() {
        return new TernarySearchTrieSetIterator();
    }



    /**
     * Iterator for TernarySearchTrieSet
     **/
    private class TernarySearchTrieSetIterator implements Iterator {

        /**
         * A stack is needed to track traversal state. Stack elements
         * logically consist of a node, plus a record of whether
         * only the left or the left+eq children have been traversed yet.
         * It's a little cheaper to use parallel arrays for nodes and
         * positions than it would be to create a separate bookkeeping class.
         **/
        private static final int INITIAL_TRAVERSAL_STACK_CAPACITY = 64;
        private static final int LEFT_DONE = 0;
        private static final int EQ_DONE = 1;
        private static final int RIGHT_DONE = 2;
        private int[]  posStack = new int[INITIAL_TRAVERSAL_STACK_CAPACITY];
        private Node[] nodeStack = new Node[INITIAL_TRAVERSAL_STACK_CAPACITY];
        private int  sp; // stack pointer

        /** Next node to return **/
        private Node current;
        /** traversal state of current node **/
        private int  traversalPosition;
        /** Last node returned by next(), to allow remove() **/
        private Node lastReturned;
        /** fast-fail iterator support **/
        private int expectedModCount = modCount;

        private void push(Node n, int cp) {
            if (sp >= nodeStack.length) {
                int oldlen = nodeStack.length;
                int newlen = oldlen * 2;
                Node[] ns = new Node[newlen];
                int[] ps = new int[newlen];
                for (int i = 0; i < oldlen; ++i) {
                    ns[i] = nodeStack[i];
                    ps[i] = posStack[i];
                }
                nodeStack = ns;
                posStack = ps;
            }
            nodeStack[sp] = n;
            posStack[sp] = cp;
            ++sp;
        }

        private void pop() {
            if (sp == 0)
                current = null;
            else {
                --sp;
                current = nodeStack[sp];
                traversalPosition = posStack[sp];
            }
        }

        /**
         * Traverse down to least element starting from p
         * pushing nodes along the way
         **/
        private void descend(Node p) {
            for (;;) {
                if (p.left != null) {
                    push(p, LEFT_DONE);
                    p = p.left;
                }
                else if (p.eq != null) {
                    push(p, EQ_DONE);
                    p = p.eq;
                }
                else {
                    current = p;
                    traversalPosition = LEFT_DONE;
                    break;
                }
            }

        }

        TernarySearchTrieSetIterator() {
            if (root != null)
                descend(root);
        }

        public boolean hasNext() {
            return current != null;
        }

        public Object next() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();

            if (current == null)
                throw new NoSuchElementException();

            lastReturned = current;
            Object value = current.string;

            for (;;) {

                if (traversalPosition == LEFT_DONE) {
                    if (current.eq != null) {
                        push(current, EQ_DONE);
                        descend(current.eq);
                        break;
                    }
                    else
                        traversalPosition = EQ_DONE;
                }

                if (traversalPosition == EQ_DONE) {
                    if (current.right != null) {
                        descend(current.right);
                        break;
                    }
                    else
                        traversalPosition = RIGHT_DONE;
                }

                pop();
                if (current == null || current.string != null)
                    break;

            }

            return value;
        }

        public void remove() {
            if (lastReturned == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();

            // Tree modifications in del are written in way that cannot affect
            // stack, so the traversal can continue normally.
            TernarySearchTrieSet.this.remove(lastReturned.string);
            lastReturned = null;
            expectedModCount = modCount;

        }
    }

    /**
     * Save the state of this set instance to a stream (that is,
     * serialize this set).
     *
     * @serialData The number of elements,
     *		 followed by all of the elements.
     */

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException  {
        s.defaultWriteObject();

        // Write out number of elements
        s.writeInt(count);

        // Write out elements
        for (Iterator it = iterator(); it.hasNext(); )
            s.writeUTF((String)(it.next()));

    }

    /**
     * Reconstitute a set from a stream (that is, deserialize it).
     */

    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException
    {
        s.defaultReadObject();

        // Read in size
        int size = s.readInt();

        String[] items = new String[size];

        for (int i = 0; i < size; ++i)
            items[i] = s.readUTF();

        addFromSortedArray(items, 0, size-1);

    }

    /**
     * Returns a shallow copy of this set: the elements
     * themselves are not cloned.
     *
     * @return a shallow copy of this set.
     */
    public Object clone() {
        TernarySearchTrieSet clone = null;
        try {
            clone = (TernarySearchTrieSet)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }

        clone.root = null;
        clone.count = 0;
        clone.modCount = 0;

        // Spill out to array to avoid worst-case tree
        String[] items = new String[count];
        int j = 0;
        for (Iterator it = iterator(); it.hasNext(); )
            items[j++] = (String)(it.next());
        clone.addFromSortedArray(items, 0, count-1);

        return clone;
    }

    private void addFromSortedArray(String[] items, int lo, int hi) {
        if (lo > hi)
            return;

        int mid = (lo + hi) / 2;
        add(items[mid]);
        addFromSortedArray(items, lo, mid-1);
        addFromSortedArray(items, mid+1, hi);
    }


}


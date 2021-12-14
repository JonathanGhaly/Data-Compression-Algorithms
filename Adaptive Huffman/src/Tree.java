public class Tree {
    static Integer count = 100;
    Character val;
    Integer weight;
    Integer order;

    Tree P;
    Tree L;
    Tree R;
    Tree root;

    /**
     * Constructor for the tree
     *
     * @param val    character that is assigned to the node if nyt assigned to {null}
     * @param weight the weight of the character in each node
     * @param p      the parent of this node
     */
    Tree(Character val, int weight, Tree p) {
        this.val = val;
        this.weight = weight;
        this.order = count--;
        this.P = p;
        if (p == null)
            this.root = this;
        else
            this.root = p.root;
        this.R = null;
        this.L = null;
    }

    /**
     * @return character that is saved in the node
     */
    Character getVal() {
        return val;
    }

    /**
     * swap 2 nodes
     *
     * @param t node to swap with
     */
    public void swap(Tree t) {
        if (P.R == this && t.P.R == t) {
            P.R = t;
            t.P.R = this;
            Tree temp = P;
            P = t.P;
            t.P = temp;
        } else if (P.L == this && t.P.R == t) {
            P.L = t;
            t.P.R = this;
            Tree temp = P;
            P = t.P;
            t.P = temp;
        } else if (P.L == this && t.P.L == t) {
            P.L = t;
            t.P.L = this;
            Tree temp = P;
            P = t.P;
            t.P = temp;
        } else {
            P.R = t;
            t.P.L = this;
            Tree temp = P;
            P = t.P;
            t.P = temp;
        }
        int nNum = this.order;
        this.order = t.order;
        t.order = nNum;
    }

    /**
     * Search in the tree with a character
     *
     * @param c character to be found
     * @return node that contain the character
     */
    Tree isContain(Character c) {
        if (R == null & L == null) {
            if (val == c) {
                return this;
            }
            return null;
        }
        Tree ret = null;
        ret = L.isContain(c);
        if (ret == null) {
            ret = R.isContain(c);
        }
        return ret;
    }

    /**
     * Search in the tree with node number
     *
     * @param order the number of the node to be found
     * @return the node that has number equal to parameter
     */
    Tree getNode(Integer order) {
        if (this.order == order) {
            return this;
        }
        if (L == null) {
            return null;
        }
        Tree ret = L.getNode(order);
        if (ret == null) {
            return R.getNode(order);
        }
        return ret;
    }

    /**
     * get the binary code of each node
     *
     * @param n    node to get the code of each node
     * @param code empty string to append on it
     * @return the code of the given node
     */
    String getCode(Tree n, String code) {
        if (L == null) {
            if (this == n)
                return code;
            return null;
        }
        String ret = L.getCode(n, code + '0');
        if (ret == null) {
            ret = R.getCode(n, code + '1');
        }
        return ret;
    }

    /**
     * Get the order of the node to swap with
     *
     * @param n node needed to swap
     * @return number of the node to swap with node n(-1 if not found)
     */
    Integer searchNodeSwap(Tree n) {
        if (this.weight <= n.weight && this.order > n.order && this != n && this != n.P) {
            return this.order;
        }
        if (L != null) {
            int Left = L.searchNodeSwap(n);
            int Right = R.searchNodeSwap(n);
            return Math.max(Left, Right);
        }
        return - 1;
    }

    /**
     * make swap of the tree and reorder it
     */
    void reOrder() {
        Integer nNum = root.searchNodeSwap(this);
        Tree n = root.getNode(nNum);
        if (nNum != - 1) {
            swap(n);
        }
        ++ weight;
        if (P != null) {
            P.reOrder();
        }
    }

    /**
     * create a new node
     *
     * @param c character that assigned to the right node
     */
    void createNode(Character c) {
        R = new Tree(c, 0, this);
        L = new Tree(null, 0, this);
        R.reOrder();
    }
}

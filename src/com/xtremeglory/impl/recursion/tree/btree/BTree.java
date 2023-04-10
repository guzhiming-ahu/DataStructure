package com.xtremeglory.impl.recursion.tree.btree;

import com.xtremeglory.impl.List;
import com.xtremeglory.impl.Pair;
import com.xtremeglory.impl.Tree;
import com.xtremeglory.impl.Visitor;
import com.xtremeglory.impl.recursion.list.LinkedList;
import com.xtremeglory.impl.Stack;

import java.util.Iterator;

/**
 * 树中每个结点最多含有m棵子树。
 * 若根结点不是叶子结点，则至少有2个子树。
 * 除根结点之外的所有非终端结点至少有⌈m/2⌉棵子树。
 * 如果一个结点有n-1个关键字，则该结点有n个分支，且这n-1个关键字按照递增顺序排列。
 * 每个非终端结点中包含信息：（N，A0，K1，A1，K2，A2，...，KN，一）其中
 *
 * @param <E>
 */
public class BTree<E extends Comparable<E>> implements Tree<E> {
    protected final int DIM;
    private int node_size;
    protected final E[] elements;
    protected final BTree<E>[] children;

    @SuppressWarnings("unchecked")
    public BTree(int DIM) {
        this.DIM = DIM;
        this.node_size = 0;
        this.elements = (E[]) new Comparable[DIM + 1];
        this.children = new BTree[DIM + 2];
    }

    public int getDIM() {
        return this.DIM;
    }

    public int getNodeSize() {
        return this.node_size;
    }

    private BTree<E> copy(int begin, int end) {
        BTree<E> root = new BTree<>(this.DIM);
        for (int i = 0; i < end - begin; ++i) {
            root.elements[i] = this.elements[begin + i];
            root.children[i] = this.children[begin + i];
            root.node_size++;
        }
        root.children[end - begin] = this.children[end];

        return root;
    }

    private void insertChildren(int position, E element, BTree<E> left, BTree<E> right) {
        for (int i = this.node_size; i > position; --i) {
            this.elements[i] = this.elements[i - 1];
            this.children[i + 1] = this.children[i];
        }
        this.elements[position] = element;
        this.children[position] = left;
        // 思考: 为什么此处children只是挪出一个空位,却能同时放下left和right节点?
        // 解答: 因为挪动前,原position指向的是要被合并的节点,这个节点在合并后就不复存在,所以这个节点被复制到position+1位置后又被right覆盖
        this.children[position + 1] = right;
        this.node_size++;
    }

    private void removeChildren(int position) {
        for (int i = position; i < this.node_size - 1; ++i) {
            this.elements[i] = this.elements[i + 1];
            this.children[i] = this.children[i + 1];
        }
        if (position != this.node_size - 1) {
            /* bugfix: 此处如果删除的是最后一个节点, 那么这个节点最右边的节点会一同删除,如果覆盖前面会导致节点顺序错误
             * 例如       25 29  32     删除最后一个元素,如果覆盖就会生成        25  29
             *          /  |   \  \                                     /     |   \
             *        24   27  30  35                                  24    27   35 => 此处丢失节点30
             */
            this.children[this.node_size - 1] = this.children[this.node_size];
        }
        this.node_size--;
    }

    /**
     * @param element 需要查找的元素
     * @return 当element在节点中存在时, 返回size + 下标 + 1;这是为了方便remove操作设计的. 否则, 返回需要搜索的位置
     */
    protected int dispatch(E element) {
        for (int i = 0; i < this.node_size; ++i) {
            if (element.compareTo(elements[i]) < 0) {
                return i;
            } else if (element.compareTo(elements[i]) == 0) {
                return node_size + i + 1;
            }
        }
        return node_size;
    }

    protected int minElementSize() {
        return (this.DIM - 1) / 2;
    }

    public E max() {
        if (this.children[this.node_size] != null) {
            return this.children[this.node_size].max();
        } else {
            return this.elements[this.node_size - 1];
        }
    }

    public E min() {
        if (this.children[0] != null) {
            return this.children[0].min();
        } else {
            return this.elements[0];
        }
    }

    protected BTree<E> split() {
        int middle = this.DIM / 2;
        BTree<E> root = new BTree<>(this.DIM);
        root.elements[root.node_size++] = this.elements[this.DIM / 2];
        root.children[0] = this.copy(0, middle);             // left splited node
        root.children[1] = this.copy(middle + 1, this.DIM);    // right splited node

        return root;
    }

    protected void merge(int position) {
        BTree<E> merging = this.children[position];
        this.insertChildren(position, merging.elements[0], merging.children[0], merging.children[1]);
    }

    private void _insert(E element) {
        int position = this.dispatch(element);
        if (position <= this.node_size) {
            if (this.children[position] != null) {
                this.children[position]._insert(element);

                // balance
                if (this.children[position].node_size == this.DIM) {
                    this.children[position] = this.children[position].split();
                    this.merge(position);
                }
            } else {
                this.insertChildren(position, element, null, null);
            }
        }
    }

    @Override
    public int size() {
        int total = this.node_size;
        for (BTree<E> tree : this.children) {
            if (tree == null) {
                break;
            }
            total += tree.size();
        }
        return total;
    }

    @Override
    public BTree<E> insert(E element) {
        this._insert(element);
        if (this.node_size == this.DIM) {
            return this.split();
        } else {
            return this;
        }
    }

    public void _remove(E element) {
        int position = this.dispatch(element);
        if (position <= this.node_size) {
            if (this.children[position] != null) {
                this.children[position]._remove(element);
                // balance
                if (this.children[position].node_size <= (this.DIM - 1) / 2) {
                    boolean adjusted = this.node_size > minElementSize();
                    if (this.node_size > minElementSize()) {
                        if (position != 0 && this.children[position - 1].node_size > minElementSize()) {
                            BTree<E> removed = this.children[position];
                            BTree<E> borrowed = this.children[position - 1];
                            removed.insertChildren(0, this.elements[position - 1], borrowed.children[borrowed.node_size], removed.children[0]);
                            this.elements[position - 1] = borrowed.elements[borrowed.node_size - 1];
                            borrowed.removeChildren(borrowed.node_size - 1);
                        } else if (position != this.node_size && this.children[position + 1].node_size > minElementSize()) {
                            BTree<E> removed = this.children[position];
                            BTree<E> borrowed = this.children[position + 1];
                            removed.insertChildren(this.node_size, this.elements[position], removed.children[removed.node_size], borrowed.children[0]);
                            this.elements[position] = borrowed.elements[0];
                            borrowed.removeChildren(0);
                        } else {
                            adjusted = false;
                        }
                    }

                    if (!adjusted) {
                        BTree<E> left, right;
                        if (position == this.node_size) {
                            // 当position == this.size时,要合并节点的左节点在position-1位置,要下沉的element的位置也是position-1
                            position = position - 1;
                        }
                        left = this.children[position];
                        right = this.children[position + 1];
                        element = this.elements[position];

                        left.insertChildren(left.node_size, element, left.children[left.node_size], right.children[0]);
                        for (int i = 0; i < right.node_size; ++i) {
                            left.insertChildren(left.node_size, right.elements[i], left.children[left.node_size], right.children[i + 1]);
                        }
                        this.removeChildren(position);
                        this.children[position] = left;
                    }
                }
            }
        } else {
            position = position - this.node_size - 1;
            if (this.children[position] != null) {
                this.elements[position] = this.children[position].max();
                this.children[position]._remove(this.elements[position]);
            } else {
                this.removeChildren(position);
            }
        }
    }

    @Override
    public BTree<E> remove(E element) {
        this._remove(element);
        if (this.node_size == 0) {
            return this.children[0];
        } else {
            return this;
        }
    }

    @Override
    public void travel(Visitor<E> visitor) {
        for (int i = 0; i < this.node_size; ++i) {
            if (this.children[i] != null) {
                this.children[i].travel(visitor);
            }
            visitor.visit(this.elements[i]);
        }
        if (this.children[this.node_size] != null) {
            this.children[this.node_size].travel(visitor);
        }
    }

    public void travelNode(Visitor<BTree<E>> visitor) {
        for (int i = 0; i < this.node_size; ++i) {
            if (this.children[i] != null) {
                this.children[i].travelNode(visitor);
            }
            visitor.visit(this);
        }
        if (this.children[this.node_size] != null) {
            this.children[this.node_size].travelNode(visitor);
        }
    }

    @Override
    public List<E> asList() {
        List<E> list = new LinkedList<>();
        this.travel(element -> list.insert(element, list.size()));
        return list;
    }


    @Override
    public E find(E element) {
        int position = this.dispatch(element);
        if (position <= this.node_size) {
            if (this.children[position] != null) {
                return this.children[position].find(element);
            } else {
                return null;
            }
        } else {
            return this.elements[position - this.node_size - 1];
        }
    }

    class BTreeIterator implements Iterator<E> {
        // 第一个参数表示当前遍历到的Node,第二个参数表示当前栈顶元素遍历到的位置,从0开始计数
        private final Stack<Pair<BTree<E>, Integer>> stack;

        public BTreeIterator(BTree<E> tree) {
            this.stack = new Stack<>(new LinkedList<>());
            stack.push(new Pair<>(tree, 0));

            Pair<BTree<E>, Integer> current = this.stack.top();

            while (current.first.children[current.second] != null) {
                this.stack.push(new Pair<>(current.first.children[current.second], 0));
                current = this.stack.top();
            }
        }

        /**
         * 如果当前栈顶元素的node_size为0,说明是个叶子节点,返回false
         * 如果当前栈无元素,说明已经遍历完成,返回false
         * @return 请见说明
         */
        @Override
        public boolean hasNext() {
            if (this.stack.isEmpty()) {
                return false;
            } else {
                Pair<BTree<E>, Integer> current = this.stack.top();
                return current.second < current.first.node_size;
            }
        }

        @Override
        public E next() {
            Pair<BTree<E>, Integer> current = this.stack.top();
            E element = current.first.elements[current.second++];

            while (current.first.children[current.second] != null) {
                this.stack.push(new Pair<>(current.first.children[current.second], 0));
                current = this.stack.top();
            }

            while (!this.stack.isEmpty() && current.second >= current.first.node_size) {
                this.stack.pop();
                if (!this.stack.isEmpty()) {
                    current = this.stack.top();
                }
            }
            return element;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new BTreeIterator(this);
    }
}
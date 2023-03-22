package com.xtremeglory.tree;

import com.xtremeglory.impl.ValueBox;
import com.xtremeglory.impl.Visitor;
import com.xtremeglory.impl.recursion.tree.btree.BTree;
import com.xtremeglory.list.ListTest;
import org.junit.Assert;
import org.junit.Test;

public class BTreeTest {
    protected static int minElementSize(BTree<Integer> tree) {
        return (tree.getDIM() - 1) / 2;
    }

    protected static void keepFeature(BTree<Integer> tree) {
        // 排序性
        ListTest lt = new ListTest();
        lt.assertOrder(tree.asList(), true);
        // 节点个数性质
        ValueBox<Boolean> enoughElementSize = new ValueBox<>(true);
        // 根节点不要求节点数量,其他节点要求数量达到要求
        Visitor<BTree<Integer>> visitor = t -> enoughElementSize.set(t == tree || (enoughElementSize.get() && t.getNodeSize() >= minElementSize(t)));
        tree.travelNode(visitor);
        Assert.assertTrue(enoughElementSize.get());
    }

    @Test
    public void sequenceInsert() {
        int count = 3000;
        for (int dim = 3; dim < 100; ++dim) {
            BTree<Integer> bt = new BTree<>(dim);
            for (int i = 0; i < count; ++i) {
                bt = bt.insert(i);
                keepFeature(bt);
            }
            System.out.println("dim: " + dim + " passed");
        }
    }

    @Test
    public void sequenceRemove() {
        int count = 3000;
        for (int dim = 3; dim < 100; ++dim) {
            BTree<Integer> bt = new BTree<>(dim);
            // insert
            for (int i = 0; i < count; ++i) {
                bt = bt.insert(i);
            }

            for (int i = count - 1; i > 0; --i) {
                bt = bt.remove(i);
                System.out.println(i);
                keepFeature(bt);
            }
            System.out.println("dim: " + dim + " passed");
        }
    }

    @Test
    public void randomInsert() {
        int count = 3000;
        for (int dim = 3; dim < 100; ++dim) {
            BTree<Integer> bt = new BTree<>(dim);
            for (int i = 0; i < count; ++i) {
                int random = (int) (Math.random() * 100000);
                if (!bt.contains(random)) {
                    bt = bt.insert(random);
                    keepFeature(bt);
                }
            }
            System.out.println("dim: " + dim + " passed");
        }
    }

    @Test
    public void size() {
        int count = 3000;
        for (int dim = 3; dim < 100; ++dim) {
            int total = 0;
            BTree<Integer> bt = new BTree<>(dim);
            for (int i = 0; i < count; ++i) {
                int random = (int) (Math.random() * 100000);
                if (!bt.contains(random)) {
                    bt = bt.insert(random);
                    keepFeature(bt);
                    total++;
                    assert bt.size() == total;
                }
            }
            System.out.println("dim: " + dim + " passed");
        }
    }

//    @Test
//    public void randomRemove(){
//        int count = 3000;
//        for (int dim=3;dim<100;++dim) {
//            BTree<Integer> bt=new BTree<>(dim);
//            for (int i=0;i<count;++i){
//                int random = (int) (Math.random()*100000);
//                if (!bt.contains(random)) {
//                    bt = bt.remove(random);
//                    keepFeature(bt);
//                }
//            }
//            System.out.println("dim: "+dim+" passed");
//        }
//    }


}

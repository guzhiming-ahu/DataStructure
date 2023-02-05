package com.xtremeglory.list;

import com.xtremeglory.impl.List;
import com.xtremeglory.impl.recursion.list.LinkedList;
import com.xtremeglory.utils.BasicTestUtils;
import org.junit.Assert;
import org.junit.Test;

public class ListTest {
    static final Class<? extends List> clazz = LinkedList.class;

    @SuppressWarnings("unchecked")
    protected <T> List<T> getInstance() {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void assertOrder(List<Integer> list, boolean asc) {
        int i = list.size() - 1;
        while (i > 0) {
            if (asc) {
                Assert.assertTrue(list.get(i - 1) < list.get(i));
            } else {
                Assert.assertTrue(list.get(i - 1) > list.get(i));
            }
            --i;
        }
    }

    public void initList(List<Integer> list, Integer end, boolean append) {
        for (int i = 0; i < end; ++i) {
            if (append) {
                list.insert(i, i);
            } else {
                list.insert(i, 0);
            }
        }
    }

    public void initList(List<Integer> list, Integer[] srcArray) {
        for (int i : srcArray) {
            list.insert(i, list.size());
        }
    }


    @Test
    public void sequenceInsert1() {
        List<Integer> list = getInstance();

        initList(list, 999, true);
        for (int i = 0; i < 999; ++i) {
            Assert.assertEquals((long) list.get(i), i);
        }
    }

    @Test
    public void sequenceInsert2() throws InstantiationException, IllegalAccessException {
        List<Integer> list = getInstance();

        initList(list, 999, false);
        for (int i = 0; i < 999; ++i) {
            Assert.assertEquals((long) list.get(i), 999 - i - 1);
        }
    }


    @Test
    public void randomInsert() {
        for (int index = 0; index < 2; ++index) {
            String[] cases = BasicTestUtils.loadTestCase("list/case/random_insert.txt");

            Integer[] src_array = BasicTestUtils.toIntegerArray(cases[index * 4]);
            Integer[] dst_array = BasicTestUtils.toIntegerArray(cases[index * 4 + 3]);

            Integer[] vals = BasicTestUtils.toIntegerArray(cases[index * 4 + 1]);
            Integer[] indexs = BasicTestUtils.toIntegerArray(cases[index * 4 + 2]);

            List<Integer> list = getInstance();
            initList(list, src_array);
            for (int i = 0; i < vals.length; ++i) {
                list.insert(vals[i], indexs[i]);
            }
            Assert.assertArrayEquals(list.asList(), dst_array);
        }
    }

    @Test
    public void removeFirst() {
        List<Integer> list = getInstance();
        int count = 999;
        initList(list, count, true);

        while (list.size() > 0) {
            list.remove(0);
            assertOrder(list, true);
            Assert.assertEquals(list.size(), --count);
        }
    }

    @Test
    public void removeLast() {
        List<Integer> list = getInstance();
        int count = 999;
        initList(list, count, true);

        while (list.size() > 0) {
            list.remove(list.size() - 1);
            assertOrder(list, true);
            Assert.assertEquals(list.size(), --count);
        }
    }

    @Test
    public void randomRemove() {
        for (int index = 0; index < 4; ++index) {
            String[] cases = BasicTestUtils.loadTestCase("list/case/random_remove.txt");

            Integer[] src_array = BasicTestUtils.toIntegerArray(cases[index * 3]);
            Integer[] dst_array = BasicTestUtils.toIntegerArray(cases[index * 3 + 2]);

            Integer[] indexs = BasicTestUtils.toIntegerArray(cases[index * 3 + 1]);

            List<Integer> list = getInstance();
            initList(list, src_array);
            for (Integer integer : indexs) {
                list.remove((int) integer); // 为什么此处必须使用int才能得到正确结果? 答案请见default E remove(E element)的定义
            }
            Assert.assertArrayEquals(list.asList(), dst_array);
        }
    }

    @Test
    public void elementRemove() {
        for (int index = 0; index < 4; ++index) {
            String[] cases = BasicTestUtils.loadTestCase("list/case/element_remove.txt");

            Integer[] src_array = BasicTestUtils.toIntegerArray(cases[index * 3]);
            Integer[] dst_array = BasicTestUtils.toIntegerArray(cases[index * 3 + 2]);

            Integer[] elements = BasicTestUtils.toIntegerArray(cases[index * 3 + 1]);

            List<Integer> list = getInstance();
            initList(list, src_array);
            for (Integer element : elements) {
                Assert.assertEquals(element, list.remove(element));
            }
            Assert.assertArrayEquals(list.asList(), dst_array);
        }
    }

    @Test
    public void elementRemove1() {
        for (int index = 0; index < 3; ++index) {
            String[] cases = BasicTestUtils.loadTestCase("list/case/element_remove1.txt");

            Integer[] src_array = BasicTestUtils.toIntegerArray(cases[index * 4]);
            Integer[] dst_array = BasicTestUtils.toIntegerArray(cases[index * 4 + 3]);

            Integer[] indexs = BasicTestUtils.toIntegerArray(cases[index * 4 + 1]);
            Integer[] elements = BasicTestUtils.toIntegerArray(cases[index * 4 + 2]);

            List<Integer> list = getInstance();
            initList(list, src_array);
            for (int i = 0; i < indexs.length; ++i) {
                Assert.assertEquals(elements[i], list.remove(elements[i], indexs[i]));
            }
            Assert.assertArrayEquals(list.asList(), dst_array);
        }
    }

    @Test
    public void listGet() {
        for (int index = 0; index < 4; ++index) {
            String[] cases = BasicTestUtils.loadTestCase("list/case/list_get.txt");

            Integer[] src_array = BasicTestUtils.toIntegerArray(cases[index * 2]);
            Integer[] dst_array = BasicTestUtils.toIntegerArray(cases[index * 2 + 1]);

            List<Integer> list = getInstance();
            initList(list, src_array);
            for (int i = 0; i < dst_array.length; ++i) {
                Assert.assertEquals(list.get(i), dst_array[i]);
            }
        }
    }

    @Test
    public void listSet() {
        for (int index = 0; index < 4; ++index) {
            String[] cases = BasicTestUtils.loadTestCase("list/case/list_set.txt");

            Integer[] src_array = BasicTestUtils.toIntegerArray(cases[index * 4]);
            Integer[] dst_array = BasicTestUtils.toIntegerArray(cases[index * 4 + 3]);

            Integer[] indexs = BasicTestUtils.toIntegerArray(cases[index * 4 + 2]);
            Integer[] elements = BasicTestUtils.toIntegerArray(cases[index * 4 + 1]);

            List<Integer> list = getInstance();
            initList(list, src_array);

            for (int i = 0; i < elements.length; ++i) {
                list.set(elements[i], indexs[i]);
            }

            Assert.assertArrayEquals(list.asList(), dst_array);
        }
    }
}

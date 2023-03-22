package com.xtremeglory.impl.recursion.list;

import com.xtremeglory.impl.List;
import com.xtremeglory.impl.ValueBox;

import java.util.Objects;

/**
 * Node表示的是链表中的一个节点, 通过next指针把其他的Node节点串联起来.
 * 从表的角度来看, 任意一个Node都可以视为一个表头节点;
 *
 * @param <E>
 */
final class Node<E> {
    E element;
    Node<E> next;

    Node(E element, Node<E> next) {
        this.element = element;
        this.next = next;
    }

    Node() {}

    /**
     * insert承诺返回最新的表头
     *
     * @param element 需要存储的元素
     * @param index 元素将会保存在index位置
     */
    void insert(E element, int index) {
        if (index > 0) {
            this.next.insert(element, index - 1);
        } else {
            this.next = new Node<>(element, this.next);
        }
    }

    /**
     * 删除index下标处的元素,并返回被删除元素
     * @param index 被删除元素的位置
     * @param value_box 保存被删除元素的数据结构,用于在return时把被删除元素带出
     */
    void remove(int index, ValueBox<E> value_box) {
        if (index > 0) {
            this.next.remove(index - 1, value_box);
        } else {
            value_box.set(this.element);
            this.next = this.next.next;
        }
    }

    Node<E> remove(E element, int index, ValueBox<E> value_box) {
        if (index > 0) {// 调用此函数时已经做下标检查了,此处不需要判断this.next != null
            this.next = this.next.remove(element, index - 1, value_box);
        } else if (Objects.equals(this.element, element)) { // 不要使用this.element.equals(element),因为this.element可能为null
            value_box.set(this.element);
            return this.next;
        } else if (this.next != null) { // 但是要查找的元素可能不在表里,所以后面要判断this.next != null
            this.next = this.next.remove(element, index, value_box);
        }
        return this;
    }

    void set(E element, int index) {
        if (index > 0) {
            this.next.set(element, index - 1);
        } else {
            this.element = element;
        }
    }

    E get(int index) {
        if (index > 0) {
            return this.next.get(index - 1);
        } else {
            return this.element;
        }
    }

    /**
     * 在表中开始索引(index)处向后查找元素(element)
     *
     * @param element 要查找的元素
     * @param index   开始索引位置,包含该位置
     * @param current 用于记录当前索引,默认值为0
     * @return 如果查找到元素, 则返回索引, 否则返回-1
     */
    int indexOf(E element, int index, int current) {
        if (current < index) {
            // 由于上层函数已经校验下标,所以this.next不用检查null
            return this.next.indexOf(element, index, current + 1);
        } else if (!Objects.equals(this.element, element)) {
            return this.next != null ? this.next.indexOf(element, index, current + 1) : -1;
        } else {
            return current;
        }
    }

    void asList(Object[] array, int index) {
        array[index] = this.element;
        if (this.next != null) {
            this.next.asList(array, index + 1);
        }
    }
}


public class LinkedList<E> implements List<E> {
    private final Node<E> head;
    private int size;

    public LinkedList() {
        this.head = new Node<>();
        this.size = 0;
    }

    @Override
    public void insert(E element, int index) {
        if (index > this.size || index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        } else {
            this.head.insert(element, index);
            ++this.size;
        }
    }

    @Override
    public E remove(int index) {
        if (index > this.size || index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        } else {
            ValueBox<E> value_box = new ValueBox<>();
            this.head.remove(index, value_box);
            --this.size;
            return value_box.get();
        }
    }

    @Override
    public E remove(E element, int index) {
        if (index > this.size || index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        } else {
            ValueBox<E> value_box = new ValueBox<>();
            this.head.remove(element, index, value_box);
            --this.size;
            return value_box.get();
        }
    }

    @Override
    public void set(E element, int index) {
        if (index >= this.size || index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        } else {
            this.head.next.set(element, index);
        }
    }

    @Override
    public E get(int index) {
        if (index >= this.size || index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        } else {
            return this.head.next.get(index);
        }
    }

    @Override
    public int indexOf(E element, int index) {
        if (index >= this.size || index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        } else {
            return this.head.indexOf(element, index, 0);
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int getCapacity() {
        return -1;
    }

    @Override
    public boolean compact() {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E[] asList() {
        if (this.size == 0) {
            return (E[]) (new Object[0]);
        } else {
            Object[] array = new Object[this.size];
            this.head.next.asList(array, 0);
            return (E[]) array;
        }
    }
}

package com.xtremeglory.impl;

public class Stack<E> {
    protected List<E> list;

    public Stack(List<E> list) {
        this.list = list;
    }

    public void push(E element){
        this.list.insert(element,0);
    }

    public E pop(){
        return this.list.remove(0);
    }

    public E top(){
        return this.list.get(0);
    }

    public boolean isEmpty(){
        return this.list.isEmpty();
    }

    public int size(){
        return this.list.size();
    }
}

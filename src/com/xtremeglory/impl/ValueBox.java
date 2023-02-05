package com.xtremeglory.impl;

public class ValueBox<E> {
    private E element;

    public ValueBox(E element) {
        this.element = element;
    }

    public ValueBox() {
    }

    public void set(E element) {
        this.element = element;
    }

    public E get() {
        return this.element;
    }
}

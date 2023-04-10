package com.xtremeglory.impl;

public interface Tree<E extends Comparable<E>> extends Iterable<E>{
    int size();

    /*增,删,查*/
    Tree<E> insert(E element);

    Tree<E> remove(E element);

    E find(E element);

    default boolean contains(E element) {
        return this.find(element) != null;
    }

    void travel(Visitor<E> visitor);

    List<E> asList();
}

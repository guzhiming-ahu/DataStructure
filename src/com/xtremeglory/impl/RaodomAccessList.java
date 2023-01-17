package com.xtremeglory.impl;

/**
 * 此接口是一个标记接口，任何一个实现该接口的数据结构都可视为一个可以随机存储的表
 * 随机存储表可以以任意顺序访问元素
 * @param <E>
 */
public interface RaodomAccessList<E> extends List<E> {
    /**
     * 从开始索引位置(index),按照搜索顺序(search_direction),一直找到第一个匹配的元素,将其删除并返回该元素
     *
     * @param element          要查找的元素
     * @param index            搜索的起始位置索引(包含该元素)
     * @param search_direction 搜索的顺序
     * @return 被删除的表中元素
     */
    E remove(E element, int index, SearchDirection search_direction);

    /**
     * 从开始索引位置(index),按照搜索顺序(search_direction),找到第一个匹配的元素,返回其索引值
     *
     * @param element          要查找的元素
     * @param index            搜索的起始位置(包含该元素)
     * @param search_direction 搜索方向
     * @return 元素在表中存在 ? 元素的索引 : -1
     */
    int indexOf(E element, int index, SearchDirection search_direction);


    /**
     * 从开始索引位置(index),按照搜索顺序(search_direction),查找元素是否存在
     *
     * @param element          要查找的元素
     * @param index            搜索的起始位置(包含该元素)
     * @param search_direction 搜索方向
     * @return 元素在表中是否存在
     */
    default boolean contains(E element, int index, SearchDirection search_direction) {
        return this.indexOf(element, index, search_direction) != -1;
    }
}

package com.xtremeglory.impl;

/**
 * @param <E>
 */
public interface List<E> {
    // 增删改查

    /**
     * 将元素(element)插入到索引(index)指定的位置
     *
     * @param element 待插入元素
     * @param index   将要插入的索引位置,插入成功后,该位置将由新元素代替
     */
    void insert(E element, int index);

    /**
     * 删除索引位置(index)的元素
     *
     * @param index 被删除元素的索引
     * @return 被删除的元素
     */
    E remove(int index);

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
     * 从开始索引位置(index),向后一直找到第一个匹配的元素,将其删除并返回该元素
     *
     * @param element 要查找的元素
     * @param index   搜索的起始位置索引(包含该元素)
     * @return 被删除的表中元素
     */
    default E remove(E element, int index) {
        return this.remove(element, index, SearchDirection.FROM_FRONT_TO_BACK);
    }

    /**
     * 从表头开始搜索并删除第一个匹配的元素
     *
     * @param element 要查找的元素
     * @return 被删除的元素
     */
    default E remove(E element) {
        return this.remove(element, 0, SearchDirection.FROM_FRONT_TO_BACK);
    }

    /**
     * 设置索引位置(index)的值为指定元素(element)的值
     *
     * @param element 要设置的元素
     * @param index   被修改的位置索引
     */
    void set(E element, int index);

    /**
     * 获取指定索引位置(index)的元素值
     *
     * @param index 被指定的索引位置
     * @return 该位置的元素值
     */
    E get(int index);

    // 查找

    /**
     * 从开始索引位置(index),按照搜索顺序(search_direction),找到第一个匹配的元素,返回其索引值
     *
     * @param element          要查找的元素
     * @param index            搜索的起始位置
     * @param search_direction 搜索方向
     * @return 元素在表中存在 ? 元素的索引 : -1
     */
    int indexOf(E element, int index, SearchDirection search_direction);

    /**
     * 从开始索引位置(index),向后找到第一个匹配的元素,返回其索引值
     *
     * @param element 要查找的元素
     * @param index   搜索的起始位置
     * @return 元素在表中存在 ? 元素的索引 : -1
     */
    default int indexOf(E element, int index) {
        return this.indexOf(element, index, SearchDirection.FROM_FRONT_TO_BACK);
    }

    /**
     * 从表头位置,向后找到第一个匹配的元素,返回其索引值
     *
     * @param element 要查找的元素
     * @return 元素在表中存在 ? 元素的索引 : -1
     */
    default int indexOf(E element) {
        return this.indexOf(element, 0, SearchDirection.FROM_FRONT_TO_BACK);
    }

    /**
     * 从开始索引位置(index),按照搜索顺序(search_direction),查找元素是否存在
     *
     * @param element          要查找的元素
     * @param index            搜索的起始位置
     * @param search_direction 搜索方向
     * @return 元素在表中是否存在
     */
    default boolean contains(E element, int index, SearchDirection search_direction) {
        return this.indexOf(element, index, search_direction) != -1;
    }

    /**
     * 从开始索引位置(index),向后查找元素是否存在
     *
     * @param element 要查找的元素
     * @param index   搜索的起始位置
     * @return 元素在表中是否存在
     */
    default boolean contains(E element, int index) {
        return this.contains(element, index, SearchDirection.FROM_FRONT_TO_BACK);
    }

    /**
     * 从表头开始,向后查找元素是否存在
     *
     * @param element 要查找的元素
     * @return 元素在表中是否存在
     */
    default boolean contains(E element) {
        return this.contains(element, 0);
    }

    // 表状态

    /**
     * 表中元素个数
     *
     * @return 表中元素个数
     */
    int size();

    /**
     * 表的最大容量,对于动态扩容的数据结构,则不存在最大容量
     *
     * @return 表可以扩容 ? -1 : 最大容量
     */
    int getCapacity();

    /**
     * 表是否为空表
     *
     * @return 表是否为空表
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * 表是否已填充满,对于动态扩容的数据结构,则不存在表被填满的情况
     *
     * @return 表是否被填满
     */
    default boolean isFull() {
        return this.size() == this.getCapacity();
    }

    /**
     * 对于使用懒惰删除策略的表,在删除操作后会留下一些空隙,使用此方法会删除掉不可用的空隙
     *
     * @return 如果支持紧凑, 则返回true, 否则返回false
     */
    boolean compact();
}

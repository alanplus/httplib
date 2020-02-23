package com.alan.http;

/**
 * @author Alan
 * 时 间：2019-12-16
 * 简 述：<功能简述>
 */
public interface IParseStrategy<T> {

    T parse(String string);
}

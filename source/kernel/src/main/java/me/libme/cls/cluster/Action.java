package me.libme.cls.cluster;

/**
 * Created by J on 2018/11/15.
 */
public interface Action {

    default void prepare() throws Exception{}

    void start() throws Exception;

    void shutdown() throws Exception;

}

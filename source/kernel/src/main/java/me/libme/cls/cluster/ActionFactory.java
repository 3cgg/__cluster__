package me.libme.cls.cluster;

/**
 * Created by J on 2018/11/19.
 */
public interface ActionFactory {

    Action factory(ClusterConfig clusterConfig) throws Exception;
}

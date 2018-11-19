package me.libme.cls;

import me.libme.cls.cluster.Action;
import me.libme.cls.cluster.ClusterConfig;

/**
 * Created by J on 2018/11/19.
 */
public interface ActionFactory {

    Action factory(ClusterConfig clusterConfig) throws Exception;
}

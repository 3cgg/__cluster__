package me.libme.cls.cluster;

import me.libme.cls.cluster.config.yaml.YamlClusterConfig;

/**
 * Created by J on 2018/11/14.
 */
public abstract class ClusterInfo {


    public static ClusterConfig defaultConfig(){
        ClusterConfig clusterConfig=new YamlClusterConfig(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("application-cluster.yml")).find();
        return clusterConfig;

    }


}

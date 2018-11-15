package me.libme.cls.cluster.config.yaml;

import me.libme.cls.cluster.ClusterConfig;
import me.libme.cls.cluster.config.ClusterConfigFinder;
import me.libme.kernel._c.yaml.YamlMapConfig;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by J on 2018/9/29.
 */
public class YamlClusterConfig implements ClusterConfigFinder {

    private YamlMapConfig yamlMapConfig;

    public YamlClusterConfig(InputStream inputStream) {
        this.yamlMapConfig = new YamlMapConfig(inputStream);
    }

    @Override
    public ClusterConfig find() {
        ClusterConfig clusterConfig=new ClusterConfig();

        String name=yamlMapConfig.getString("cpp.cluster.name","Cpp SimpleCluster");
        clusterConfig.setName(name);
        ClusterConfig.Master master=new ClusterConfig.Master();
        master.setName(yamlMapConfig.getString("cpp.cluster.master.name","Cpp Master"));
        master.setHost(yamlMapConfig.getString("cpp.cluster.master.netty.host","0.0.0.0"));
        master.setPort(yamlMapConfig.getInt("cpp.cluster.master.netty.port",10089));
        clusterConfig.setMaster(master);

        ClusterConfig.Worker worker=new ClusterConfig.Worker();
        worker.setName(yamlMapConfig.getString("cpp.cluster.worker.name","Cpp Worker"));

        clusterConfig.setProperties((Map<String, Object>) yamlMapConfig.getObject("cpp.cluster.properties",new HashMap<>()));

        return clusterConfig;
    }



























}

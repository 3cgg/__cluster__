package me.libme.cls.cluster;

import me.libme.module.zookeeper.fn.ls.CloseResource;
import me.libme.module.zookeeper.fn.ls.OpenResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J on 2018/11/14.
 */
public class Master {

    public static MasterBuilder builder(){
        return new MasterBuilder();
    }

    public static class MasterBuilder{

        private List<OpenResource> openResources=new ArrayList<>();

        private List<CloseResource> closeResources=new ArrayList<>();

        public MasterBuilder addOpenResource(OpenResource openResource){
            openResources.add(openResource);
            return this;
        }

        /**
         * close resource in the sequence of adding element
         * @param closeResource
         * @return
         */
        public MasterBuilder addCloseResource(CloseResource closeResource){
            closeResources.add(closeResource);
            return this;
        }

        public Cluster build(){

            SimpleCluster cluster=new SimpleCluster();
            openResources.forEach(openResource -> cluster.addOpenResource(openResource));
            closeResources.forEach(closeResource -> cluster.addCloseResource(closeResource));
            return cluster;
        }

    }



}

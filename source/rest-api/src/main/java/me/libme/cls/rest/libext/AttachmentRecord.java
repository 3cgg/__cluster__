package me.libme.cls.rest.libext;

import me.libme.kernel._c._m.JModel;
import me.libme.webboot.fn.file.IAttachmentRecord;

/**
 * Created by J on 2017/8/26.
 */
public class AttachmentRecord implements JModel, IAttachmentRecord {

    private String uri;

    private String desc;

    private String path;

    private String name;

    private String suffix;


    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

    @Override
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}

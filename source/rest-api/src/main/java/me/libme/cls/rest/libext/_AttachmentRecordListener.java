package me.libme.cls.rest.libext;

import me.libme.kernel._c.file.FileResponse;
import me.libme.webboot.fn.file.AttachmentRecordListener;
import me.libme.webboot.fn.file.IAttachmentRecord;
import org.springframework.stereotype.Component;

/**
 * Created by J on 2017/8/27.
 */
@Component
public class _AttachmentRecordListener implements AttachmentRecordListener {

    @Override
    public IAttachmentRecord on(FileResponse fileResponse) throws Exception {
        AttachmentRecord attachmentRecord=new AttachmentRecord();
        attachmentRecord.setPath(fileResponse.getUri());
        String fileName=fileResponse.getName();
        int p=-1;
        if((p=fileName.lastIndexOf("."))!=-1){
            attachmentRecord.setName(fileName.substring(0, p));
            attachmentRecord.setSuffix(fileName.substring(p+1));
        }else{
            attachmentRecord.setName(fileName);
        }
        return attachmentRecord;
    }

}

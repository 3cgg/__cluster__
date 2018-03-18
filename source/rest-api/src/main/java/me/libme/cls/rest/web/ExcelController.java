package me.libme.cls.rest.web;

import me.libme.kernel._c.file.FileTransferCfg;
import me.libme.webboot.ResponseModel;
import me.libme.cls.rest.AppConfig;
import me.libme.xstream.FileRepository;
import me.libme.xstream.Repository;
import me.libme.xstream.api.FileResponseVO;
import me.libme.xstream.api.TupeRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by J on 2017/8/27.
 */
@Controller
@RequestMapping(path="/excel")
public class ExcelController implements ApplicationListener<ContextRefreshedEvent>{


    @Autowired
    private AppConfig._Config config;


    private Repository repository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        FileTransferCfg fileTransferCfg=new FileTransferCfg();
        fileTransferCfg.setDskPath(config.getStreamConfig().getRoot());
        repository=new FileRepository(fileTransferCfg);
    }

    /**
     * get all processed files
     * @return
     * @throws Exception
     */
    @RequestMapping(path="/getFiles",method= RequestMethod.GET)
    @ResponseBody
    public ResponseModel getFiles() throws Exception {
        List<FileResponseVO> fileResponseVOS= repository.getFiles();
        return ResponseModel.newSuccess(fileResponseVOS);
    }


    /**
     * get all tupes of the file
     * @return
     * @throws Exception
     */
    @RequestMapping(path="/getTupes",method= RequestMethod.GET)
    @ResponseBody
    public ResponseModel getTupes(String fileUrl) throws Exception {
        List<FileResponseVO> fileResponseVOS= repository.getTupes(fileUrl);
        return ResponseModel.newSuccess(fileResponseVOS);
    }


    /**
     * get detail info of the processed tupe
     * @return
     * @throws Exception
     */
    @RequestMapping(path="/getDetail",method= RequestMethod.GET)
    @ResponseBody
    public ResponseModel getDetail(String tupeUrl) throws Exception {
        TupeRecordVO tupeRecordVO = repository.getDetail(tupeUrl);
        return ResponseModel.newSuccess(tupeRecordVO);
    }



}

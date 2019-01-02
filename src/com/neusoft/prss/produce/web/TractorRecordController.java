package com.neusoft.prss.produce.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.produce.entity.ExportExcel;
import com.neusoft.prss.produce.service.TractorRecordService;


@Controller
@RequestMapping(value = "${adminPath}/produce/tractor")
public class TractorRecordController extends BaseController {

    @Autowired
    private TractorRecordService tractorRecordService;
    
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "list")
    public String list() {
        return "prss/produce/tractorRecordList";
    }

    @RequestMapping(value = "data")
    @ResponseBody
    public Map<String,Object> data(
            int pageSize,int pageNumber,String ruleName,
            HttpServletRequest request, HttpServletResponse response) {
        ruleName = StringEscapeUtils.unescapeHtml4(ruleName);
        try {
            ruleName = java.net.URLDecoder.decode(ruleName,"utf-8");
        } catch (Exception e) {}

        Map<String, Object> param=new HashMap<String, Object>();
        int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("name", ruleName);
        param.put("officeId",UserUtils.getUser().getOffice().getId());//部门ID
        param.put("userId", UserUtils.getUser().getId());//用户ID
        return tractorRecordService.getRuleList(param);
    }

    @RequestMapping(value = "form")
    public String form(Model model, @RequestParam(value = "id") String id){
        model.addAttribute("id", id);
        return "prss/produce/tractorRecordInfo";
    }

    @RequestMapping(value = "getData")
    @ResponseBody
    public JSONArray getData(HttpServletRequest request) {
        String insType = request.getParameter("insType");
        String id = request.getParameter("id");
        JSONObject param = new JSONObject();
        JSONArray result = new JSONArray();
        try {
            param.put("insType", insType);
            param.put("id", id);
            result = tractorRecordService.getDataList(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "save")
    @ResponseBody
    public JSONObject save(String data) {
        data = StringEscapeUtils.unescapeHtml4(data);
        JSONObject formData = JSONObject.parseObject(data);
        JSONObject result = new JSONObject();
        try {
            tractorRecordService.updateSql(formData);
            result.put("code", 0);
            result.put("msg", "操作成功");
        } catch (Exception e) {
            result.put("code", 100);
            result.put("msg", e.getMessage());
            result.put("result", "");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "print")
    public void printData(HttpServletRequest request,HttpServletResponse response) {
       String id = request.getParameter("id");
        try {
            String fileName = "牵引车操作记录单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            String agent = (String) request.getHeader("USER-AGENT");
            String downloadFileName = "";
            if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
                downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
            } else {
                downloadFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);

            String excelTitle = "牵引车操作记录单" + DateUtils.getDate("yyyy年MM月dd日 E");
            ExportExcel excel = new ExportExcel(excelTitle);
            JSONArray update = tractorRecordService.getDataList(JSON.parseObject("{insType:99,id:"+id+"}"));
            excel.setUpdate(update);
            JSONArray data1 = tractorRecordService.getDataList(JSON.parseObject("{insType:1,id:"+id+"}"));
            excel.setData(data1,1);
            JSONArray data2 = tractorRecordService.getDataList(JSON.parseObject("{insType:2,id:"+id+"}"));
            excel.setData(data2,2);
            JSONArray data3 = tractorRecordService.getDataList(JSON.parseObject("{insType:3,id:"+id+"}"));
            excel.setDataGZ(data3);
            excel.write(response);
            excel.dispose();
        } catch (Exception e) {
            logger.error("牵引车操作记录单导出失败" + e.getMessage());
        }
    }

    /**
     * 显示流程图
     */
    @RequestMapping(value = "pic")
    public void pic(String fileId, HttpServletResponse response) throws Exception {
        byte[] is = fileService.doDownLoadFile(fileId);
        response.getOutputStream().write(is);
    }
    
    @RequestMapping(value = "download")
    public void downAtta(String fltid,String type,HttpServletRequest request,HttpServletResponse response) {
        JSONObject data = new JSONObject();
        data.put("fltid", fltid);
        data.put("type", null);
        List<String> fileId = tractorRecordService.getFileId(data);
        File[] files = new File[fileId.size()];
        for (int i = 0; i < fileId.size(); i++) {
            try {
                BufferedOutputStream stream = null;
                byte[] is = fileService.doDownLoadFile(fileId.get(i));
                String downloadFileName = tractorRecordService.getFileName(fileId.get(i));
                files[i] = new File(downloadFileName);
                FileOutputStream fstream = new FileOutputStream(files[i]);
                stream = new BufferedOutputStream(fstream);
                stream.write(is);
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        response.reset();// 重设response信息，解决部分浏览器文件名非中文问题
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=exceptional.zip");
        try {
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            zipFile(files, "exceptional.zip", zos);
            zos.flush();
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void zipFile(File[] subs,String baseName,ZipOutputStream zos) throws IOException {
        for (int i = 0; i < subs.length; i++) {
            File f = subs[i];
            zos.putNextEntry(new ZipEntry(f.getName()));
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            int r = 0;
            while ((r = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, r);
            }
            fis.close();
        }
    }

}

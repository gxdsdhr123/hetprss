package com.neusoft.prss.aptitude.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.aptitude.service.AptitudeInfoService;

@Controller
@RequestMapping(value = "${adminPath}/aptitude/info")
public class AptitudeInfoController extends BaseController {

    @Autowired
    private AptitudeInfoService aptitudeInfoService;

    /**
     * 
     *Discription:跳转人员作业资质页面
     *@param model
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月31日 lirr [变更描述]
     */
    @RequestMapping(value = "list")
    public String list(Model model) {
        return "prss/aptitude/aptitudeInfoList";
    }

    /**
     * 
     *Discription:得到展现表格的表头
     *@param request
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月31日 lirr [变更描述]
     */
    @RequestMapping(value = "getTableHeader")
    @ResponseBody
    public String getTableHeader(HttpServletRequest request) {
        Map<String,Object> params = new HashMap<String,Object>();
        String officeId = UserUtils.getUser().getOffice().getId();
        params.put("officeId", officeId);
        JSONObject obj = new JSONObject();
        JSONArray arr1 = aptitudeInfoService.getTableHeader(params);
        String limit = aptitudeInfoService.getDimLimit(params);
        obj.put("rstypeArr", arr1);
        obj.put("limitStr", limit);

        //机位
        params.put("type", "0");
        JSONArray jwLimit = aptitudeInfoService.getAreaInfoByLimit(params);
        //机型
        params.put("type", "1");
        JSONArray jxLimit = aptitudeInfoService.getAreaInfoByLimit(params);
        //航空公司
        params.put("type", "2");
        JSONArray hsLimit = aptitudeInfoService.getAreaInfoByLimit(params);
        obj.put("jwLimit", jwLimit);
        obj.put("jxLimit", jxLimit);
        obj.put("hsLimit", hsLimit);
        String result = JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
        return result;
    }

    /**
     * 
     *Discription:得到表格数据
     *@param pageSize
     *@param pageNumber
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月31日 lirr [变更描述]
     */
    @RequestMapping(value = "getGridData")
    @ResponseBody
    public List<Map<String,Object>> getGridData(String searchName) {
        Map<String,Object> param = new HashMap<String,Object>();
//        int begin = (pageNumber - 1) * pageSize;
//        int end = pageSize + begin;
        String officeId = UserUtils.getUser().getOffice().getId();
        param.put("officeId", officeId);
//        param.put("begin", begin);
//        param.put("end", end);
        param.put("searchName", searchName == "" ? "%" : searchName);
        //param.put("sortName", sortName);
        //param.put("sortOrder", sortOrder);
        return aptitudeInfoService.getGridData(param);
    }

    /**
     * 
     *Discription:保存表格数据
     *@param data
     *@param editData修改过资质的数据
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年11月1日 lirr [变更描述]
     */
    @RequestMapping(value = "saveInfo")
    @ResponseBody
    public String saveInfo(String data,String editData) {
        String msg = "";
        data = StringEscapeUtils.unescapeHtml4(data);
        editData = StringEscapeUtils.unescapeHtml4(editData);
//        System.out.println("editData======"+editData);
        JSONArray dataArray = JSONArray.parseArray(data);
        //修改过资质的数据
        JSONArray editDataArray = JSONArray.parseArray(editData);
        String officeId = UserUtils.getUser().getOffice().getId();
        msg = aptitudeInfoService.saveInfo(dataArray, editDataArray,officeId);
        //调用服务，20171207 gaojd注释
        //transformService.doAptitudeTransform(officeId);
        return msg;
    }
    
    /**
     * 
     *Discription:人员作业资质失
     *@param ids
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年11月11日 lirr [变更描述]
     */
    @RequestMapping(value = "deleteApti")
    @ResponseBody
    public String deleteApti(@RequestParam(value = "ids[]",required = true) String[] ids) {
//        String officeId = UserUtils.getUser().getOffice().getId();
        try {
            aptitudeInfoService.deleteApti(ids);
            //调用服务
//            transformService.doAptitudeTransform(officeId);
        } catch (Exception e) {
            logger.error("删除人员作业资质失败" + e.getMessage());
            return "faild";
        }
        return "success";
    }
}

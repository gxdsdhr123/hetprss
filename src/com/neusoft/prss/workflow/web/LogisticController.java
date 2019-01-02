/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月25日 下午3:37:59
 *@author:yu-zd
 *@version:[v1.0]
 */
package com.neusoft.prss.workflow.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.workflow.service.LogisticService;

@Controller
@RequestMapping(value = "${adminPath}/logistic")
public class LogisticController extends BaseController {
    @Autowired
    private LogisticService logisticService;

    /**
     * 
     * Discription:跳转保障类型定义主页.
     * 
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月25日 yu-zd [变更描述]
     */
    @RequestMapping(value = "typeDefList")
    public String typeDefList() {
        return "prss/workflow/typeDefList";
    }

    /**
     * 
     * Discription:保障新增.
     * 
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月25日 yu-zd [变更描述]
     */
    @RequestMapping(value = "addType")
    public String addType(Model model) {
        JSONArray depList = logisticService.getDeptHasNoSon();
        model.addAttribute("depList", depList);
        model.addAttribute("RESKIND", "");
        model.addAttribute("KINDNAME", "");
        model.addAttribute("TAB", "");
        return "prss/workflow/logisticForm";
    }

    /**
     * 
     * Discription:保障编辑.
     * 
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月25日 yu-zd [变更描述]
     */
    @RequestMapping(value = "modifyType")
    public String modifyType(Model model,String kindName,String mainKindId) {
        JSONObject data = logisticService.getKindByKindid(mainKindId);
        JSONArray depList = logisticService.getDeptHasNoSon();
        model.addAttribute("depList", depList);
        model.addAttribute("mainKindId", mainKindId);
        model.addAttribute("KINDNAME", data.getString("KINDNAME"));
        model.addAttribute("RESKIND", data.getString("RESKIND"));
        model.addAttribute("DEPID", data.getString("DEPID"));
        model.addAttribute("DEPNAME", data.getString("DEPNAME"));
        model.addAttribute("TAB", data.getString("TABNAME"));
        return "prss/workflow/logisticForm";
    }

    /**
     * 
     * Discription:保障删除.
     * 
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月25日 yu-zd [变更描述]
     */
    @RequestMapping(value = "deleteKind")
    @ResponseBody
    public String deleteKind(String mainKindId) {
        logisticService.deleteKind(mainKindId);
        return "success";
    }

    /**
     * 
     * Discription:作业新增.
     * 
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月25日 yu-zd [变更描述]
     */
    @RequestMapping(value = "addWork")
    public String addWork(Model model,String kindName,String kindcode) {
        model.addAttribute("KINDNAME", kindName);
        model.addAttribute("kindcode", kindcode);
        return "prss/workflow/workForm";
    }

    /**
     * 
     * Discription:作业编辑.
     * 
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月25日 yu-zd [变更描述]
     */
    @RequestMapping(value = "modifyWork")
    public String modifyWork(Model model,String mainTypeId) {
        JSONObject data = logisticService.getTypeByTypeid(mainTypeId);
        JSONArray workFlowList = logisticService.getWorkFlowByTypeid(mainTypeId);
        model.addAttribute("mainTypeId", mainTypeId);
        model.addAttribute("KINDNAME", data.getString("KINDNAME"));
        model.addAttribute("TYPENAME", data.getString("TYPENAME"));
        model.addAttribute("RESTYPE", data.getString("RESTYPE"));
        model.addAttribute("kindcode", data.getString("RESKIND"));
        model.addAttribute("bindCar", data.getString("BIND_CONF"));
        model.addAttribute("workFlowList", workFlowList);
        return "prss/workflow/workForm";
    }

    /**
     * 
     * Discription:保障删除.
     * 
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月25日 yu-zd [变更描述]
     */
    @RequestMapping(value = "deleteType")
    @ResponseBody
    public String deleteType(String mainTypeId,String restype) {
        logisticService.deleteType(mainTypeId, restype);
        return "success";
    }

    /**
     * 
     * Discription:根据保障类型获作业.
     * 
     * @param reskind
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月25日 yu-zd [变更描述]
     */
    @RequestMapping(value = "getListData")
    @ResponseBody
    public JSONArray getListData(String kindMainId) {
        JSONArray data = new JSONArray();
        if (StringUtils.isNotBlank(kindMainId)) {
            data = logisticService.getListData(kindMainId);
        }
        return data;
    }

    /**
     * 
     * Discription:根据保障类型获作业.
     * 
     * @param reskind
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月25日 yu-zd [变更描述]
     */
    @RequestMapping(value = "getAllReskind")
    @ResponseBody
    public JSONArray getAllReskind() {
        JSONArray data = new JSONArray();
        data = logisticService.getAllReskind();
        return data;
    }

    /**
     * 
     * Discription:保存保障.
     * 
     * @param reskind
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月25日 yu-zd [变更描述]
     */
    @RequestMapping(value = "saveKind")
    @ResponseBody
    public String saveKind(HttpServletRequest request,HttpServletResponse response,Model model) {
        String reskind = request.getParameter("RESKIND");
        String kindname = request.getParameter("KINDNAME");
        String depname = request.getParameter("DEPNAME");
        String kindid = "";

        String dept[] = depname.split(",");
        depname = dept[1];
        String deptId = dept[0];
        String tab = request.getParameter("TAB");
        // golden star 要将OFFICE_ID 插入am_office_limit_conf 且重复的不插入 不知道为啥
        logisticService.insertOfficeLimit(deptId);
        if (StringUtils.isNotBlank(request.getParameter("mainKindId"))) {
            kindid = request.getParameter("mainKindId");
            logisticService.updateKind(kindid, reskind, kindname, deptId, depname, tab);

        } else {
            kindid = logisticService.getKindId();
            logisticService.saveKind(kindid, reskind, kindname, deptId, depname, tab);
        }
        String result = "success";
        return result;
    }

    /**
     * 
     * Discription:保存作业.
     * 
     * @param reskind
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月25日 yu-zd [变更描述]
     */
    @RequestMapping(value = "saveType")
    @ResponseBody
    public String saveType(HttpServletRequest request,HttpServletResponse response8,Model model) {
        String kindname = request.getParameter("KINDNAME");
        String typeid = "";
        String restype = request.getParameter("RESTYPE");
        String typename = request.getParameter("TYPENAME");
        String displayname = request.getParameter("DISPLAYNAME");
        String kindcode = request.getParameter("kindcode");
        String bindCar = request.getParameter("bindCar");

        if (StringUtils.isNoneBlank(request.getParameter("mainTypeId"))) {
            typeid = request.getParameter("mainTypeId");
            logisticService.updateType(typeid, kindname, restype, typename, displayname, kindcode, bindCar);
        } else {
            typeid = logisticService.getTypeId();
            logisticService.saveType(typeid, kindname, restype, typename, displayname, kindcode, bindCar);
        }
        String result = "success";
        return result;
    }

    /**
     * Discription:校验作业类型的唯一性.
     * 
     * @param request
     * @param response
     * @param model
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月30日 yu-zd [变更描述]
     */
    @RequestMapping(value = "vaildOnlyRestype")
    @ResponseBody
    public String vaildOnlyRestype(HttpServletRequest request,HttpServletResponse response,Model model) {
        String restype = request.getParameter("restype");
        JSONArray resultArr = new JSONArray();
        if (StringUtils.isNoneBlank(restype)) {
            resultArr = logisticService.vaildOnlyRestype(restype);
            if (resultArr.size() > 0) {
                return "N";
            } else {
                return "Y";
            }
        } else {
            return "E";
        }
    }

    /**
     * Discription:校验保障类型的唯一性.
     * 
     * @param request
     * @param response
     * @param model
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月30日 yu-zd [变更描述]
     */
    @RequestMapping(value = "vaildOnlyReskind")
    @ResponseBody
    public String vaildOnlyReskind(HttpServletRequest request,HttpServletResponse response,Model model) {
        String reskind = request.getParameter("reskind");
        JSONArray resultArr = new JSONArray();
        if (StringUtils.isNoneBlank(reskind)) {
            resultArr = logisticService.vaildOnlyReskind(reskind);
            if (resultArr.size() > 0) {
                return "N";
            } else {
                return "Y";
            }
        } else {
            return "E";
        }
    }
}

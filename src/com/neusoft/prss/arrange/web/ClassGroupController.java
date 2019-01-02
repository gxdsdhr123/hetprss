/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月25日 上午10:54:49
 *@author:yu-zd
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.arrange.entity.ClassGroupEntity;
import com.neusoft.prss.arrange.service.ClassGroupService;

@Controller
@RequestMapping(value = "${adminPath}/arrange/classGroup")
public class ClassGroupController extends BaseController {
    @Autowired
    private ClassGroupService cgService;

    /**
     * 
     * Discription:获取排班页面.
     * 
     * @return
     * @return:航班动态页面
     * @author:SunJ
     * @update:2017年9月18日 SunJ [变更描述]
     */
    @RequestMapping(value = "list")
    public String list(Model model) {
        return "prss/arrange/classGroupList";
    }

    /**
     * 获取列表数据 Discription:.
     * 
     * @param model
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月26日 yu-zd [变更描述]
     */
    @RequestMapping(value = "getListData")
    @ResponseBody
    public JSONArray getListData(Model model) {
        String officeId = UserUtils.getUser().getOffice().getId();
        JSONArray data = cgService.getListData(officeId);
        return data;
    }

    /**
     * 新增班组页面跳转 Discription:.
     * 
     * @param model
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月26日 yu-zd [变更描述]
     */
    @RequestMapping(value = "add")
    public String add(Model model) {
        String deptid = UserUtils.getUser().getOffice().getId();
        JSONArray aPerson = cgService.getMembersByDeptid(deptid);
        ClassGroupEntity cgEntity = new ClassGroupEntity();
        model.addAttribute("aPerson", aPerson);
        JSONArray pPerson = cgService.getUnselectedByDeptid(deptid);
        model.addAttribute("pPerson", pPerson);
        model.addAttribute("lPerson", pPerson);
        model.addAttribute("cgEntity", cgEntity);
        return "prss/arrange/classGroupForm";
    }

    @RequestMapping(value = "modify")
    public String modify(Model model,String groupid) {
        ClassGroupEntity cgEntity = new ClassGroupEntity();
        String deptid = UserUtils.getUser().getOffice().getId();
        JSONArray aPerson = cgService.getMembersByDeptidCgid(deptid, groupid);
        JSONArray pPerson = cgService.getUnselectedByDeptid(deptid);
        JSONArray sPerson = cgService.getMembersBycgid(groupid);
        JSONObject groupObj = cgService.getGroupInfoByid(groupid);
        cgEntity.setId(groupid);
        cgEntity.setPmid(StringUtils.isNotBlank(groupObj.getString("PM")) ? groupObj.getString("PM") : "");
        cgEntity.setSmid(StringUtils.isNotBlank(groupObj.getString("SM")) ? groupObj.getString("SM") : "");
        cgEntity.setCgname(groupObj.getString("NAME"));
        model.addAttribute("cgEntity", cgEntity);
        model.addAttribute("aPerson", aPerson);

        JSONArray lPerson = new JSONArray();
        lPerson = pPerson;
        for (int i = 0; i < aPerson.size(); i++) {
            if (StringUtils.isBlank(aPerson.getJSONObject(i).getString("ID"))) {
                continue;
            }
            if (aPerson.getJSONObject(i).getString("ID")
                    .equals(StringUtils.isNotBlank(groupObj.getString("PM")) ? groupObj.getString("PM") : "")
                    || aPerson.getJSONObject(i).getString("ID")
                            .equals(StringUtils.isNotBlank(groupObj.getString("SM")) ? groupObj.getString("SM")
                                    : "")) {
                lPerson.add(aPerson.getJSONObject(i));
            }
        }
        model.addAttribute("lPerson", lPerson);
        pPerson = JSONArray.parseArray(JSONArray.toJSONString(lPerson.clone()));
        pPerson.removeAll(sPerson);
        model.addAttribute("pPerson", pPerson);
        model.addAttribute("sPerson", sPerson);
        return "prss/arrange/classGroupForm";
    }

    @RequestMapping(value = "save")
    @ResponseBody
    public String save(Model model,ClassGroupEntity cgEntity) {
        String ID = cgEntity.getId();
        String cgName = cgEntity.getCgname();

        String workerIDs[] = cgEntity.getsPerson().split(",");
        // 新增
        if (StringUtils.isBlank(ID)) {
            // ID = cgService.getCgId();
            ID = Calendar.getInstance().getTimeInMillis() + "";
            String cgNameCount = cgService.getCgNameCount(cgName);
            if (Integer.parseInt(cgNameCount) >= 1)
                return "repeat";
            // 修改
        } else {
            cgService.delcg(ID);
            String cgNameModifyCount = cgService.getCgNameCount(cgName);
            if (Integer.parseInt(cgNameModifyCount) >= 1)
                return "repeat";
        }
        List<Map<String,String>> parmList = new ArrayList<>();
        Map<String,String> parms = new HashMap<>();
        parms.put("id", ID);
        parms.put("name", cgEntity.getCgname());
        parms.put("pmID", cgEntity.getPmid());
        parms.put("pmName", "");
        parms.put("officeid", UserUtils.getUser().getOffice().getId());
        parms.put("smID", cgEntity.getSmid());
        parms.put("smName", "");
        parms.put("creator", UserUtils.getUser().getId());
        for (int i = 0; i < workerIDs.length; i++) {
            Map<String,String> workerMap = new HashMap<>();
            workerMap.put("workerid", workerIDs[i]);
            workerMap.put("groupid", ID);
            workerMap.put("creator", UserUtils.getUser().getId());
            workerMap.put("level", "2");
            parmList.add(workerMap);
        }
        cgService.savecg(parms, parmList);
        return "success";

    }

    @RequestMapping(value = "deleteGroup")
    @ResponseBody
    public String deleteGroup(Model model,String groupid) {
        cgService.delcg(groupid);
        return "success";
    }

}
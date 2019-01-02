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
import com.neusoft.prss.arrange.entity.WorkGroupEntity;
import com.neusoft.prss.arrange.service.WorkGroupService;

@Controller
@RequestMapping(value = "${adminPath}/arrange/workGroup")
public class WorkGroupController extends BaseController {
    @Autowired
    private WorkGroupService wgService;

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
        return "prss/arrange/workGroupList";
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
        JSONArray data = wgService.getListData(officeId);
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
        JSONArray aPerson = wgService.getMembersByDeptid(deptid);
        JSONArray pPerson = wgService.getUnselectedByDeptid(deptid);
        JSONArray lPerson = new JSONArray();
        lPerson = pPerson;
        WorkGroupEntity wgEntity = new WorkGroupEntity();
        model.addAttribute("aPerson", aPerson);
        model.addAttribute("pPerson", pPerson);
        model.addAttribute("lPerson", lPerson);
        model.addAttribute("wgEntity", wgEntity);
        return "prss/arrange/workGroupForm";
    }

    @RequestMapping(value = "modify")
    public String modify(Model model,String teamid) {
        WorkGroupEntity wgEntity = new WorkGroupEntity();
        String deptid = UserUtils.getUser().getOffice().getId();
        // 所有人员 已选人员 待选人员 组长候选人
        JSONArray aPerson = wgService.getMembersByDeptid(deptid);
        JSONArray sPerson = wgService.getMembersByteamid(teamid);
        JSONArray pPerson = wgService.getUnselectedByDeptid(deptid);

        JSONObject teamObj = wgService.getGroupInfoByid(teamid);
        wgEntity.setTeamId(teamid);
        wgEntity.setLeaderId(teamObj.getString("LEADERID"));
        wgEntity.setTeamName(teamObj.getString("TEAMNAME"));

        model.addAttribute("wgEntity", wgEntity);
        model.addAttribute("aPerson", aPerson);

        JSONArray lPerson = new JSONArray();
        lPerson = pPerson;
        for (int i = 0; i < aPerson.size(); i++) {
            if (aPerson.getJSONObject(i).getString("id").equals(teamObj.getString("LEADERID"))) {
                lPerson.add(aPerson.getJSONObject(i));
            }
        }
        model.addAttribute("lPerson", lPerson);
        pPerson = JSONArray.parseArray(JSONArray.toJSONString(lPerson.clone()));
        pPerson.removeAll(sPerson);
        model.addAttribute("pPerson", pPerson);
        model.addAttribute("sPerson", sPerson);
        return "prss/arrange/workGroupForm";
    }

    @RequestMapping(value = "save")
    @ResponseBody
    public String save(Model model,WorkGroupEntity wgEntity) {
        String ID = wgEntity.getTeamId();
        String teamName=wgEntity.getTeamName();
        String workerIDs[] = wgEntity.getsPerson().split(",");
        if (StringUtils.isBlank(ID)) {
            // ID = cgService.getCgId();
            ID = Calendar.getInstance().getTimeInMillis() + "";
        } else {
            wgService.delwg(ID);
        }
        List<Map<String,String>> parmList = new ArrayList<>();
        Map<String,String> teamMap = new HashMap<>();
        teamMap.put("teamId", ID);
        teamMap.put("leaderId", wgEntity.getLeaderId());
        teamMap.put("officeId", UserUtils.getUser().getOffice().getId());
        teamMap.put("creatorId", UserUtils.getUser().getId());
        teamMap.put("teamName", teamName);

        for (int i = 0; i < workerIDs.length; i++) {
            Map<String,String> workerMap = new HashMap<>();
            workerMap.put("workerid", workerIDs[i]);
            workerMap.put("teamId", ID);
            workerMap.put("ifleader", "0");
            workerMap.put("creatorId", UserUtils.getUser().getId());
            parmList.add(workerMap);
        }
        Map<String,String> leaderMap = new HashMap<>();
        leaderMap.put("workerid", wgEntity.getLeaderId());
        leaderMap.put("teamId", ID);
        leaderMap.put("ifleader", "1");
        leaderMap.put("creatorId", UserUtils.getUser().getId());
        parmList.add(leaderMap);

        wgService.savecg(teamMap, parmList);
        return "success";
    }

    @RequestMapping(value = "deleteTeam")
    @ResponseBody
    public String deleteGroup(Model model,String teamid) {
        wgService.delwg(teamid);
        return "success";
    }

}

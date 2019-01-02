package com.neusoft.prss.message.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.config.Global;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.entity.Log;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.service.ParamCommonService;
import com.neusoft.prss.job.service.JobManagerService;
import com.neusoft.prss.message.entity.CommonVO;
import com.neusoft.prss.message.entity.MessageSubscribeVO;
import com.neusoft.prss.message.entity.MessageUtils;
import com.neusoft.prss.message.entity.SRListVO;
import com.neusoft.prss.message.service.MessageSubscribeService;


@Controller
@RequestMapping(value = "${adminPath}/message/subscribe")
public class MessageSubscribeController extends BaseController {
	
	@Resource
    private MessageSubscribeService messagesubscribeService;
	
	@Resource
    private ParamCommonService paramCommonService;


	@Resource
    private JobManagerService jobManagerService;
    
	 /**
     * 
     *Discription:打开消息订阅主页.
     *@param model
     *@return
     *@return:
     *@author:zhaol
     *@update:2017年9月8日  [变更描述]
     */
    @RequestMapping(value = "list")
    public String list(Model model) {

        JSONArray jobKindList = messagesubscribeService.loadJobKind();
        model.addAttribute("jobKindList", jobKindList);
        return "prss/message/subscribeList";
    }
    
    /**
     * 
     *Discription:消息订阅表格信息.
     *@param model
     *@return
     *@return:
     *@author:zhaol
     *@update:2017年9月8日  [变更描述]
     */
    @RequestMapping(value = "subscribeList")
    @ResponseBody
    public Map<String,Object> subscribeList(int pageSize,int pageNumber,String disablename,String hbiotypename,String flightnumber,String jobKind,String event,
            String crtime,Log log,String sortName,String sortOrder,HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> param=new HashMap<String, Object>();
        int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("disablename", disablename);
        param.put("hbiotypename", hbiotypename);
        param.put("flightnumber", flightnumber);
        param.put("event", event);
        param.put("jobKind", jobKind);
        param.put("crtime", crtime);
        param.put("sortName", sortName);
        param.put("sortOrder", sortOrder);
        param.put("cruserid", UserUtils.getUser().getId());
        param.put("userType", UserUtils.getUser().isAdmin()?"1":"0");
        return messagesubscribeService.getsubscribeList(param);
    }
    /**
     * 
     *Discription:获取消息订阅新增修改页面.
     *@param model
     *@return
     *@return:
     *@author:zhaol
     *@update:2017年9月8日  [变更描述]
     */
    @RequestMapping(value = "getdetailPage")
    public String getdetailPage(HttpServletRequest request,HttpServletResponse response,Model model) {
    	  String id = request.getParameter("id");
          String flag = request.getParameter("flag");
          Map<String,String> map = new HashMap<String,String>();
          map.put("id", id);
          MessageSubscribeVO vo = messagesubscribeService.getdetailPage(map);
          //触发事件
          String userType=UserUtils.getUser().getUserType();
          List<CommonVO> listDSE = messagesubscribeService.getDimSysEvents(userType);
          List<CommonVO> listF = messagesubscribeService.getFiotype();
          if( "".equals(id)||id==null){ 
              String cruseren=UserUtils.getUser().getLoginName();
              String cruserid=UserUtils.getUser().getId();
             
              vo=new MessageSubscribeVO();
              vo.setCruseren(cruseren);
              vo.setCruserid(cruserid);
          } else {
              String varcols = vo.getVarcols();
              String mtext = vo.getMtext();
              if(varcols !=null && !"".equals(varcols)){
                  HashMap<String,String> data = new HashMap<String,String>();
                  data.put("schema", "88");
                  data.put("colids", varcols);
                  data.put("text", mtext);
                  mtext = paramCommonService.getColumn(data);
              }
              if(!StringUtils.isBlank(mtext))
                  mtext = mtext.replace("\n", "<br>");
              vo.setMtext(mtext);
          }
          model.addAttribute("id", id);
          model.addAttribute("flag", flag);
          model.addAttribute("vo", vo);
          model.addAttribute("listDSE", listDSE);
          model.addAttribute("listF", listF);
          model.addAttribute("userType", userType);
          return "prss/message/subscribeDetail";
    }

    /**
     * 
     *Discription:参数变量页面信息.
     *@param model
     *@return
     *@return:
     *@author:zhaol
     *@update:2017年9月8日  [变更描述]
     */
    @RequestMapping(value = "varList")
    @ResponseBody
    public String varList(Log log,HttpServletRequest request, HttpServletResponse response, Model model) {
	   
        Map<String,String> map = new HashMap<String,String>();
        List<SRListVO> list = messagesubscribeService.varList(map); 
        String data = JSON.toJSONString(list, SerializerFeature.WriteMapNullValue);
        return data;
    }


    /**
     * 
     *Discription:删除消息订阅信息.
     *@param model
     *@return
     *@return:
     *@author:zhaol
     *@update:2017年9月8日  [变更描述]
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(@RequestParam(value = "ids[]",required = false) String[] ids,
            @RequestParam(value = "ruleids[]",required = false) String[] ruleids,
            @RequestParam(value = "jobids[]",required = false) String[] jobids){
        try {
            messagesubscribeService.delete(ids,ruleids);
            if(jobids != null)
            for(String jobId : jobids){
                if(jobId != null && !"".equals(jobId))
                    jobManagerService.deleteJob(Integer.parseInt(jobId));
            }
            return MessageUtils.SUCCESS_MSG;
        } catch (Exception e) {
            e.printStackTrace();
            return MessageUtils.ERROR_MSG;
        }
    }
    /**
     * 
     *Discription:保存新增修改订阅消息.
     *@param model
     *@return
     *@return:
     *@author:zhaol
     *@update:2017年9月8日  [变更描述]
     */

    @RequestMapping(value = "save")
    @ResponseBody
    public String save(MessageSubscribeVO vo,HttpServletRequest request,HttpServletResponse response,
            Model model) {      
        String  id = request.getParameter("id");
        int jobId = vo.getJobId();
        String disable = vo.getDisable();
        try {

            String userId = UserUtils.getUser().getId();
            String userName = UserUtils.getUser().getName();
            String officeName = UserUtils.getUser().getOffice().getName();
            String userLoginName = UserUtils.getUser().getLoginName();
            String drools = vo.getDrools();
            drools = drools.replace("&quot;", "\"");
            drools = StringEscapeUtils.unescapeHtml4(drools);
            String mtext = vo.getMtext();
            mtext = mtext.replace("&nbsp;", "");
            vo.setMtext(mtext);
            vo.setDrools(drools);
            if("".equals(id)||id==null){
                String rmid=messagesubscribeService.getRmSeq();
                vo.setRuleid(rmid);
                vo.setCruseren(userName);
                vo.setCruserid(userId);
                id = messagesubscribeService.getScheSeq();
                vo.setId(id);
                String schtime = vo.getSchtime();
                if(schtime != null && !"".equals(schtime) && "1".equals(disable)){
                  //ID,USERID,USERNAME,OFFICENAME,USERLOGINNAME,FLTID,FLIGHTNUMBER,FLIGHTDATE,SYS,MFROMTYPE=0
                    String executorParam = "";
                    executorParam += "messageSendService,sendSubsMessage,";
                    executorParam +=  vo.getId()+","+userId+","+userName+","+officeName+","+userLoginName+",11,0,"+vo.getFlightnumber()+","+vo.getHbiotype();// 执行器，任务参数;
                    JSONObject job=new JSONObject();
//                job.put("jobGroup", Global.getConfig("jobGroup.subs"));// 执行器主键ID (JobKey.group)
                    job.put("jobGroup", "5");// 执行器主键ID (JobKey.group)
                    String corn = "0 " + schtime.substring(2) + " " + schtime.substring(0, 2) + " * * ? *";//0 12 31 * * ? *
                    job.put("jobCron", corn);// 任务执行CRON表达式 【base on quartz】
                    job.put("jobDesc", "send subs message");//任务描述
                    job.put("author", "super");// 负责人
                    job.put("alarmEmail", "");// 报警邮件
                    job.put("executorRouteStrategy", "FIRST");
                    job.put("executorHandler", Global.getConfig("messageJobHandler"));// 执行器，任务Handler名称
                    job.put("executorParam", executorParam);// 执行器，任务参数
                    job.put("executorBlockStrategy", "SERIAL_EXECUTION");// 阻塞处理策略
                    job.put("executorFailStrategy", "FAIL_ALARM");// 失败处理策略
                    job.put("glueType", "BEAN");// GLUE类型
                    job.put("childJobKey", "");// 子任务Key
                    jobId = jobManagerService.addJob(job);
                    vo.setJobId(jobId);
                }
                messagesubscribeService.insert(vo);
            } else {
                String schtime = vo.getSchtime();
                jobManagerService.deleteJob(jobId);
                if(schtime != null && !"".equals(schtime) && "1".equals(disable)){
                    String executorParam = "";
                    executorParam += "messageSendService,sendSubsMessage,";
                    executorParam +=  vo.getId()+","+userId+","+userName+","+officeName+","+userLoginName+",11,0,"+vo.getFlightnumber()+","+vo.getHbiotype();// 执行器，任务参数;
                    JSONObject job=new JSONObject();
//                job.put("jobGroup", Global.getConfig("jobGroup.subs"));// 执行器主键ID (JobKey.group)
                    job.put("jobGroup", "5");// 执行器主键ID (JobKey.group)
                    String corn = "0 " + schtime.substring(2) + " " + schtime.substring(0, 2) + " * * ? *";//0 12 31 * * ? *
                    job.put("jobCron", corn);// 任务执行CRON表达式 【base on quartz】
                    job.put("jobDesc", "send subs message");//任务描述
                    job.put("author", "super");// 负责人
                    job.put("alarmEmail", "");// 报警邮件
                    job.put("executorRouteStrategy", "FIRST");
                    job.put("executorHandler", Global.getConfig("messageJobHandler"));// 执行器，任务Handler名称
                    job.put("executorParam", executorParam);// 执行器，任务参数
                    job.put("executorBlockStrategy", "SERIAL_EXECUTION");// 阻塞处理策略
                    job.put("executorFailStrategy", "FAIL_ALARM");// 失败处理策略
                    job.put("glueType", "BEAN");// GLUE类型
                    job.put("childJobKey", "");// 子任务Key
                    jobId = jobManagerService.addJob(job);
                    vo.setJobId(jobId);
                } else {
                    jobManagerService.deleteJob(jobId);
                }
                messagesubscribeService.update(vo);
            }
            return MessageUtils.SUCCESS_MSG;
        } catch (Exception e) {
            e.printStackTrace();
            return MessageUtils.ERROR_MSG;
        }
    }


}

/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月29日 上午10:20:13
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.web;


import java.io.UnsupportedEncodingException;
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
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.common.service.ParamCommonService;
import com.neusoft.prss.message.entity.CommonVO;
import com.neusoft.prss.message.entity.MessageTemplFromVO;
import com.neusoft.prss.message.entity.MessageTemplToVO;
import com.neusoft.prss.message.entity.MessageTemplVO;
import com.neusoft.prss.message.entity.MessageUtils;
import com.neusoft.prss.message.entity.SRListVO;
import com.neusoft.prss.message.service.MessageSubscribeService;
import com.neusoft.prss.message.service.MessageTemplService;


@Controller
@RequestMapping(value = "${adminPath}/message/templ")
public class MessageTemplController extends BaseController {

    @Resource
    private MessageTemplService messageTemplService;
    
    @Resource
    private MessageSubscribeService messagesubscribeService;
    
    @Resource
    private ParamCommonService paramCommonService;
    
    
    @RequestMapping(value = "list")
    @ResponseBody
    public Map<String, Object> getList(int pageSize, int pageNumber, String sortName, String sortOrder,
            String typeId,String q) {
        Map<String, Object> param = new HashMap<String, Object>();
        int begin = (pageNumber - 1) * pageSize;
        int end = pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("sortName", sortName);
        param.put("sortOrder", sortOrder);
        param.put("mtype", typeId);
        param.put("q", q);
        return messageTemplService.getList(param);
    }
    
    
    @RequestMapping(value = "info")
    public String info(HttpServletRequest request,HttpServletResponse response,Model model) {
        String id = request.getParameter("id");
        String flag = request.getParameter("flag");
        String mtype = request.getParameter("mtype");
        Map<String,String> map = new HashMap<String,String>();
        map.put("id", id);
        MessageTemplVO vo = messageTemplService.selectOne(map);
        if("upd".equals(flag)){
            mtype = vo.getMtype();
            String varcols = vo.getVarcols();
            String mtext = vo.getMtext();
            if(varcols !=null && !"".equals(varcols)){
                HashMap<String,String> data = new HashMap<String,String>();
                data.put("schema", "88");
                data.put("colids", varcols);
                data.put("text", mtext);
                mtext = paramCommonService.getColumn(data);
            }
            mtext = mtext.replace("\n", "<br>");
            vo.setMtext(mtext);
        }
        model.addAttribute("id", id);
        model.addAttribute("flag", flag);
        model.addAttribute("mtype", mtype);
        model.addAttribute("vo", vo);
        return "prss/message/messageTemplInfo";
    }
    

    @RequestMapping(value = "getType")
    @ResponseBody
    public MessageTemplVO getType(HttpServletRequest request,HttpServletResponse response ,Model model){
        String id= request.getParameter("id");
        Map<String,String> map = new HashMap<String,String>();
        map.put("id", id);
        return messageTemplService.selectOne(map);
    }

    @RequestMapping(value = "save")
    @ResponseBody
    public String save(MessageTemplVO vo,HttpServletRequest request,HttpServletResponse response,
    		Model model,String tabList,String tabList1) {  	
    	String  flag = request.getParameter("flag");
    	
        try {
            String mtext = vo.getMtext();
            mtext = mtext.replace("&nbsp;", " ").trim();
            vo.setMtext(mtext);
            if("add".equals(flag)){
            	String id=messageTemplService.getTidSeq();
            	vo.setId(id);
                tabList = StringEscapeUtils.unescapeHtml4(tabList);
                JSONArray senderArray = JSONArray.parseArray(tabList);
                if(senderArray.size()<=0)
                    return MessageUtils.ERROR_MSG;
                vo.setTabdata(senderArray.toJavaList(MessageTemplFromVO.class));
                
                tabList1 = StringEscapeUtils.unescapeHtml4(tabList1);
                JSONArray receiverArray = JSONArray.parseArray(tabList1);
                if(receiverArray.size()<=0)
                    return MessageUtils.ERROR_MSG;
                vo.setTabdata1(receiverArray.toJavaList(MessageTemplToVO.class));
               messageTemplService.insert(vo);
            } else if("upd".equals(flag)){
                tabList = StringEscapeUtils.unescapeHtml4(tabList);
                JSONArray senderArray = JSONArray.parseArray(tabList);
                if(senderArray.size()<=0)
                    return MessageUtils.ERROR_MSG;
                vo.setTabdata(senderArray.toJavaList(MessageTemplFromVO.class));
                tabList1 = StringEscapeUtils.unescapeHtml4(tabList1);
                JSONArray receiverArray = JSONArray.parseArray(tabList1);
                if(receiverArray.size()<=0)
                    return MessageUtils.ERROR_MSG;
                vo.setTabdata1(receiverArray.toJavaList(MessageTemplToVO.class));
                messageTemplService.update(vo);
            }
            return MessageUtils.SUCCESS_MSG;
        } catch (Exception e) {
            e.printStackTrace();
            return MessageUtils.ERROR_MSG;
        }
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(String id){
        try {
            String[] arr = id.split(",");
            String msg = "";
            for(String str : arr){
                JSONObject obj = messageTemplService.getTemplCount(str);
                int count = obj==null?0:obj.getInteger("NUM");
                if(count<=0){
                    messageTemplService.delete(str);
                } else {
                    String name = obj.getString("NAME");
                    msg += name + ",";
                }
            }
            if(!"".equals(msg)){
                msg = msg.substring(0, msg.length()-1);
                return "模板【"+msg+"】正在使用，不能删除";
            } else {
                return MessageUtils.SUCCESS_MSG;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MessageUtils.ERROR_MSG;
        }
    }
    
    
    @RequestMapping(value = "param")
    public String param(Model model) {
        List<CommonVO> list = messageTemplService.getParam();
        model.addAttribute("list", list);
        return "prss/message/paramList";
    }
    
    /**
	 * 
	 * Discription:新增修改发送人范围弹出层
	 * 
	 * @param model
	 * @return
	 * @return:
	 * @author:zhaoliang
     * @throws UnsupportedEncodingException 
	 * @update:2017年9月12日 zhaoliang [变更描述]
	 */
    @RequestMapping(value = "senderMessage")
    public String senderMessage(HttpServletRequest request,HttpServletResponse response,Model model) throws UnsupportedEncodingException {
        String id = request.getParameter("id");
        
        String flag = request.getParameter("flag");
        String mtype = request.getParameter("mtype");
        if("null".equals(mtype)){
     	   mtype="";
        }
        String mfromtype = request.getParameter("mfromtype");
        if("null".equals(mfromtype)){
        	mfromtype="";
        }
        String mfromtypename = request.getParameter("mfromername");  
        mfromtypename = StringUtils.isBlank(mfromtypename)?"":mfromtypename;
        if("null".equals(mfromtypename)){
        	mfromtypename="";
        }
        String mfromer = request.getParameter("mfromer");
        if("null".equals(mfromer)){
        	mfromer="";
        }
        String mfromername = request.getParameter("mfromername");
        mfromername = StringUtils.isBlank(mfromername)?"":mfromername;
        if("null".equals(mfromername)){
        	mfromername="";
        }
        if(mfromername!=null&&!"".equals(mfromername)){
        mfromername=java.net.URLDecoder.decode(mfromername,"utf-8");
        }
        String proceclsfrom = request.getParameter("proceclsfrom");
        proceclsfrom = StringUtils.isBlank(proceclsfrom)?"":proceclsfrom;
        if("null".equals(proceclsfrom)){
        	proceclsfrom="";
        }
        
        String proceclsfromname = request.getParameter("proceclsfromname");
        proceclsfromname = StringUtils.isBlank(proceclsfromname)?"":proceclsfromname;
        if("null".equals(proceclsfromname)){
        	proceclsfromname="";
        }
        if(proceclsfromname!=null&&!"".equals(proceclsfromname)){
        	proceclsfromname=java.net.URLDecoder.decode(proceclsfromname,"utf-8");
        	}
        String procdefparamfrom = request.getParameter("procdefparamfrom");
        procdefparamfrom = StringUtils.isBlank(procdefparamfrom)?"":procdefparamfrom;
        if("null".equals(procdefparamfrom)){
        	procdefparamfrom="";
        }
        if(procdefparamfrom!=null&&!"".equals(procdefparamfrom)){
        	procdefparamfrom=java.net.URLDecoder.decode(procdefparamfrom,"utf-8");
        	}
        String ifprocfrom = request.getParameter("ifprocfrom");
        if("null".equals(ifprocfrom)){
        	ifprocfrom="";
        }
        String ifprocfromname = request.getParameter("ifprocfromname");
        if("null".equals(ifprocfromname)){
        	ifprocfromname="";
        }
    
      
        Map<String,String> map = new HashMap<String,String>();
        map.put("id", id);
        map.put("mfromtype", mfromtype);
        map.put("mfromtypename", mfromtypename);
        map.put("mfromer", mfromer);
        map.put("mfromername", mfromername);
        map.put("proceclsfrom", proceclsfrom);
        map.put("proceclsfromname", proceclsfromname);
        map.put("procdefparamfrom", procdefparamfrom);
        map.put("ifprocfrom", ifprocfrom);
        map.put("ifprocfromname", ifprocfromname);
        List<CommonVO> listPS = messageTemplService.getProcvars();
        MessageTemplFromVO messagetemplfromVO=new MessageTemplFromVO();
        messagetemplfromVO.setMfromtype(mfromtype);
        messagetemplfromVO.setMfromtypename(mfromtypename);
        messagetemplfromVO.setMfromer(mfromer);
        messagetemplfromVO.setMfromername(mfromername);
        messagetemplfromVO.setProceclsfrom(proceclsfrom);
        messagetemplfromVO.setProceclsfromname(proceclsfromname);
        messagetemplfromVO.setProcdefparamfrom(procdefparamfrom);
        messagetemplfromVO.setIfprocfrom(ifprocfrom);
        messagetemplfromVO.setIfprocfromname(ifprocfromname);
        String[] proceclsfroms = proceclsfrom.split(",");
        String[] proceclsfromnames = proceclsfromname.split(",");
        String[] procdefparamfroms = procdefparamfrom.split(",",proceclsfroms.length);
        JSONArray procList = new JSONArray();
        for (int i = 0; i < proceclsfroms.length; i++) {
			JSONObject object = new JSONObject();
			object.put("proceclsfrom", proceclsfroms[i]);
			object.put("proceclsfromname", proceclsfromnames[i]);
			object.put("procdefparamfrom", procdefparamfroms[i]);
			procList.add(object);
		}
        model.addAttribute("procList", procList);
        model.addAttribute("id", id);
        model.addAttribute("flag", flag);
        model.addAttribute("listPS", listPS);
        model.addAttribute("vo", messagetemplfromVO);
        return "prss/message/senderMessage";
    }
    
    /**
   	 * 
   	 * Discription:新增修改接收人范围弹出层
   	 * 
   	 * @param model
   	 * @return
   	 * @return:
   	 * @author:zhaoliang
     * @throws UnsupportedEncodingException 
   	 * @update:2017年9月12日 zhaoliang [变更描述]
   	 */

       @RequestMapping(value = "reciverMessage")
       public String reciverMessage(HttpServletRequest request,HttpServletResponse response,Model model) throws UnsupportedEncodingException {
           String id = request.getParameter("id");
           String flag = request.getParameter("flag");
           String mtype = request.getParameter("mtype");
           if("null".equals(mtype)){
        	   mtype="";
           }
           String mtotype = request.getParameter("mtotype");
           if("null".equals(mtotype)){
        	   mtotype="";
           }
           String mtotypename = request.getParameter("mtotypename");
           if("null".equals(mtotypename)){
        	   mtotypename="";
           }
           String mtoer = request.getParameter("mtoer");
           if("null".equals(mtoer)){
        	   mtoer="";
           }
           String mtoername = request.getParameter("mtoername");
           if("null".equals(mtoername)){
        	   mtoername="";
           }
           if(mtoername!=null&&!"".equals(mtoername)){
        	   mtoername=java.net.URLDecoder.decode(mtoername,"utf-8");
           	}
           String ifprocto = request.getParameter("ifprocto");
           if("null".equals(ifprocto)){
        	   ifprocto="";
           }
           String ifproctoname = request.getParameter("ifproctoname");
           if("null".equals(ifproctoname)){
        	   ifproctoname="";
           }
           String proceclsto = request.getParameter("proceclsto");
           if("null".equals(proceclsto)){
        	   proceclsto="";
           }
           String proceclstoname = request.getParameter("proceclstoname");
           if("null".equals(proceclstoname)){
        	   proceclstoname="";
           }
           if(proceclstoname!=null&&!"".equals(proceclstoname)){
        	   proceclstoname=java.net.URLDecoder.decode(proceclstoname,"utf-8");
           	}
           String procdefparamto = request.getParameter("procdefparamto");
           if("null".equals(procdefparamto)){
        	   procdefparamto="";
           }
           if(procdefparamto!=null&&!"".equals(procdefparamto)){
        	   procdefparamto=java.net.URLDecoder.decode(procdefparamto,"utf-8");
           	}
           String iftrans = request.getParameter("iftrans");
           if("null".equals(iftrans)){
        	   iftrans="";
           }
           String iftransname = request.getParameter("iftransname");
           if("null".equals(iftransname)){
        	   iftransname="";
           }
           String ifsms = request.getParameter("ifsms");
           if("null".equals(ifsms)){
        	   ifsms="";
           }
           String ifsmsname = request.getParameter("ifsmsname");
           if("null".equals(ifsmsname)){
        	   ifsmsname="";
           }
           String transtemplid = request.getParameter("transtemplid");
           if("null".equals(transtemplid)){
        	   transtemplid="";
           }
           String transtemplname = request.getParameter("transtemplname");
           if("null".equals(transtemplname)){
        	   transtemplname="";
           }
           if(transtemplname!=null&&!"".equals(transtemplname)){
        	   transtemplname=java.net.URLDecoder.decode(transtemplname,"utf-8");
           	}
           String condition = request.getParameter("condition");
           if("null".equals(condition)){
        	   condition="";
           }
           if(condition!=null&&!"".equals(condition)){
               condition=java.net.URLDecoder.decode(condition,"utf-8").replace("？", " ");
           	}
           String colids = request.getParameter("colids");
           if("null".equals(colids)){
               colids="";
           }
           String drlStr = request.getParameter("drlStr");
           if("null".equals(drlStr)){
               drlStr="";
           }
           if(!StringUtils.isBlank(drlStr)){
               drlStr=java.net.URLDecoder.decode(drlStr,"utf-8");
           }
           String ruleId = request.getParameter("ruleId");
           if("null".equals(ruleId)){
               ruleId="";
           }
           String drools = request.getParameter("drools");
           if("null".equals(drools)){
               drools="";
           }
           if(!StringUtils.isBlank(drools)){
               drools=java.net.URLDecoder.decode(drools,"utf-8");
           }
          
           MessageTemplToVO messagetempltoVO=new MessageTemplToVO();
           messagetempltoVO.setMtotype(mtotype);
           messagetempltoVO.setMtotypename(mtotypename);
           messagetempltoVO.setMtoer(mtoer);
           messagetempltoVO.setMtoername(mtoername);
           messagetempltoVO.setIfprocto(ifprocto);
           messagetempltoVO.setIfproctoname(ifproctoname);
           messagetempltoVO.setProceclsto(proceclsto);
           messagetempltoVO.setProceclstoname(proceclstoname);
           messagetempltoVO.setProcdefparamto(procdefparamto);
           messagetempltoVO.setIftrans(iftrans);
           messagetempltoVO.setIftransname(iftransname);
           messagetempltoVO.setIfsms(ifsms);
           messagetempltoVO.setIfsmsname(ifsmsname);
           messagetempltoVO.setTranstemplid(transtemplid);
           messagetempltoVO.setTranstemplname(transtemplname);
           if(condition != null)
               condition = condition.replace(">", "&gt;").replace("<", "&lt;");
           messagetempltoVO.setCondition(condition);
           messagetempltoVO.setColids(colids);
           if(condition != null)
               drlStr = drlStr.replace(">", "&gt;").replace("<", "&lt;");
           messagetempltoVO.setDrlStr(drlStr);
           messagetempltoVO.setRuleId(ruleId);
           if(drools != null)
               drools = drools.replace(">", "&gt;").replace("<", "&lt;");
           messagetempltoVO.setDrools(drools);
           List<CommonVO> listPR = messageTemplService.getProcvars();
           model.addAttribute("id", id);
           model.addAttribute("flag", flag);
           model.addAttribute("listPR", listPR);
           model.addAttribute("vo", messagetempltoVO);
           return "prss/message/reciverMessage";
       }
   
       /**
   	 * 
   	 *Discription:发送接收人姓名获取     人员及角色信息.
   	 *@param model
   	 *@return
   	 *@return:
   	 *@author:zhaol
   	 *@update:2017年9月8日  [变更描述]
   	 */
	@RequestMapping(value = "srList")
	public String srList(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String mfromtype = request.getParameter("mfromtype");
		Map<String, String> map = new HashMap<String, String>();
		List<SRListVO> srList=null;
		map.put("mfromtype", mfromtype);
		if ("0".equals(mfromtype)) {
			 srList = messageTemplService.getUserList(map);
		} else if("1".equals(mfromtype)){
			 srList = messageTemplService.getRoleList(map);
		}
		model.addAttribute("srList", srList);
		model.addAttribute("mfromtype", mfromtype);
		return "prss/message/srList";
	}
	
	 @RequestMapping(value = "getSrListData")
	    @ResponseBody
	    public String getSrListData(HttpServletRequest request) {
		    String mfromtype = request.getParameter("mfromtype");
		    String data="";
	        Map<String,String> map = new HashMap<String,String>();
	        map.put("mfromtype", mfromtype);
	        if ("0".equals(mfromtype)) {
	        	  data = JSON.toJSONString( messageTemplService.getUserList(map), SerializerFeature.WriteMapNullValue);
				
			} else if("1".equals(mfromtype)){
				  data = JSON.toJSONString( messageTemplService.getRoleList(map), SerializerFeature.WriteMapNullValue);
				
			}
	    	return data;
	    }
	
	/**
	 * 
	 *Discription:获取参数变量信息.
	 *@param model
	 *@return
	 *@return:
	 *@author:zhaol
	 *@update:2017年9月8日  [变更描述]
	 */
		@RequestMapping(value = "getVariable")
		public String getVariable(HttpServletRequest request,
				HttpServletResponse response, Model model) {
		    model.addAttribute("schema", "88");
			return "prss/common/varList";
		}
		
		/**
		 * 
		 *Discription:获取部门、保障作业类型信息.
		 *@param model
		 *@return
		 *@return:
		 *@author:zhaol
		 *@update:2017年9月8日  [变更描述]
		 */
	 @RequestMapping(value = "getTree")
	    @ResponseBody
	    public JSONArray getDepTree(String mfromtype) {
	        JSONArray result = new JSONArray();
	        if("2".equals(mfromtype)){
	            result = messageTemplService.getDepTree();
	        } else if("8".equals(mfromtype)){
	            result = messageTemplService.getKindJob();
	        }
	        return result;
	    }
	 
	 
	 /**
		 * 
		 *Discription:获取模板信息.
		 *@param model
		 *@return
		 *@return:
		 *@author:zhaol
		 *@update:2017年9月8日  [变更描述]
		 */
	 @RequestMapping(value = "mMessage")
		public String mMessage(HttpServletRequest request,
				HttpServletResponse response, Model model) {
	         String mftype = request.getParameter("mftype");
			Map<String, String> map = new HashMap<String, String>();
			List<SRListVO> mMessageList=null;
			mMessageList = messageTemplService.getmMessageList(map);
			model.addAttribute("mMessageList", mMessageList);
			model.addAttribute("mftype", mftype);
			return "prss/message/mMessageList";
		} 
	 
	 @RequestMapping(value = "getmMessageData")
	 @ResponseBody
	 public String getmMessageData(HttpServletRequest request) {
	     String mftype = request.getParameter("mftype");
	     Map<String,String> map = new HashMap<String,String>();
	     map.put("mftype", mftype);
	     String data = JSON.toJSONString( messageTemplService.getmMessageList(map), SerializerFeature.WriteMapNullValue);
	     return data;
	 }
	 /**
		 * 
		 *Discription:获取发送人信息.
		 *@param model
		 *@return
		 *@return:
		 *@author:zhaol
		 *@update:2017年9月8日  [变更描述]
		 */
	  @RequestMapping(value = "getList")
	    @ResponseBody
	    public String getList(HttpServletRequest request) {
		    String  id = request.getParameter("id");
	        Map<String,String> map = new HashMap<String,String>();
	        map.put("id", id);
	        List<MessageTemplFromVO> list = messageTemplService.getSenderList(map);
	        List<CommonVO> listPR = messageTemplService.getProcvars();
	        for(MessageTemplFromVO vo : list) {
	        	String proceclsto = vo.getProceclsfrom();
	        	String temp = "";
	        	if(StringUtils.isBlank(proceclsto))
	        		continue;
	        	String[] proceclstos = proceclsto.split(",");
	        	for(String string :proceclstos) {
	        		for(CommonVO commonVO : listPR) {
	        			if(string.equals(commonVO.getCode())) {
	        				temp += commonVO.getName() + ",";
	        			}
	        		}
	        	}
	        	if(!"".equals(temp))
	        		temp = temp.substring(0,temp.length()-1);
	        	vo.setProceclsfromname(temp);
	        }
	        String data = JSON.toJSONString(list, SerializerFeature.WriteMapNullValue);
	    	return data;
	    }
	  /**
		 * 
		 *Discription:获取接收人信息.
		 *@param model
		 *@return
		 *@return:
		 *@author:zhaol
		 *@update:2017年9月8日  [变更描述]
		 */
	  @RequestMapping(value = "getList1")
	    @ResponseBody
	    public String getList1(HttpServletRequest request) {
		    String  id = request.getParameter("id");
	        Map<String,String> map = new HashMap<String,String>();
	        map.put("id", id);
	        List<MessageTemplToVO> list = messageTemplService.getReciverList(map);
	        String data = JSON.toJSONString(list, SerializerFeature.WriteMapNullValue);
	    	return data;
	    }
	  /**
		 * 
		 *Discription:模板关联事件数据.
		 *@param model
		 *@return
		 *@return:
		 *@author:zhaol
		 *@update:2017年9月8日  [变更描述]
		 */
	  @RequestMapping(value = "searchEventListPage")
	  public String searchEventListPage(HttpServletRequest request,
	          HttpServletResponse response, Model model) {
	      String type = request.getParameter("type");
	      model.addAttribute("type", type);
	      return "prss/message/eventList";
	  } 
	 
	  @RequestMapping(value = "searchEventList")
	  @ResponseBody
	  public String searchEventList(HttpServletRequest request) {
	      String type = request.getParameter("type");
	      Map<String,String> map = new HashMap<String,String>();
	      map.put("type", type);
	      String data = JSON.toJSONString( messageTemplService.searchEvent(map), SerializerFeature.WriteMapNullValue);
	      return data;
	  }
	  @RequestMapping(value = "filterTemplate")
      @ResponseBody
	  public int filterTemplate(HttpServletRequest request) {
	      int count = 0;
	      String id = request.getParameter("id");
	      String flag = request.getParameter("flag");
	      String tempname = request.getParameter("tempname");
	      Map<String,String> map = new HashMap<String,String>();
          map.put("flag", flag);
          map.put("id", id);
          map.put("tempname", tempname);
          count = messageTemplService.filterTemplate(map);
	      return count;
	  }
}

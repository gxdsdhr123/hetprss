package com.neusoft.prss.produce.web;

import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.produce.entity.LargePieceOfPlum;
import com.neusoft.prss.produce.service.LargePieceOfPlumService;
/**
 * 大件行李单控制器
 * @author lwg
 * @date 2017/12/23
 */
@Controller
@RequestMapping("${adminPath}/largePieceOfPlum/luggage")
public class LargePieceOfPlumController extends BaseController{
	
	 @Autowired
	 private LargePieceOfPlumService largePieceOfPlumService;
	 
	 @Autowired
	 private FileService fileService;
	 
	/**
	 * 跳转大件行李单页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list/{param}")
	public String list(Model model, @PathVariable("param") String param) {
		model.addAttribute("sign", param);
		return "prss/produce/largePieceOfPlumList";
	}
	
    /**
     * 
     *Discription:得到展现表格的表头
     *@param request
     *@return:返回值意义
     */
    @RequestMapping(value = "getTableHeader")
    @ResponseBody
    public String getTableHeader(HttpServletRequest request) {
    	return JSON.toJSONString("", SerializerFeature.WriteMapNullValue);
    }
    
    /**
     * 
     *Discription:得到表格数据
     *@param pageSize
     *@param pageNumber
     *@return:返回值意义
     */
    @RequestMapping(value = "getGridData/{param}")
    @ResponseBody
    public String getGridData(@PathVariable("param") String param, String pageNumber, String pageSize, String searchText) {
    	if(null == pageNumber) {
    		pageNumber = "1";
    	}
    	if(null == pageSize) {
    		pageSize = "10";
    	}
    	int begin = (Integer.valueOf(pageNumber) - 1) * Integer.valueOf(pageSize) + 1;
        int end = Integer.valueOf(pageSize) + begin - 1;
    	if("".equals(pageSize) || null == param) { // 大件行李单
    		return JSON.toJSONString(null, SerializerFeature.WriteMapNullValue);
    	}
    	return JSON.toJSONString(largePieceOfPlumService.getdata(begin, end, param, searchText), SerializerFeature.WriteMapNullValue);

    	
    }
    /**
     * 页面跳转总控
     * @param searchTime
     * @param param
     * @param officeId
     * @param pdate
     * @param model
     * @return
     */
    @RequestMapping(value = "pageJump/{param}/{sign}")
    public String pageJump(String searchTime, @PathVariable("param") String param, @PathVariable("sign") String sign, String searchId, String pdate, Model model) {
		if("add".equals(param)) { // 增加大件行李
			// 获取查理列表
			model.addAttribute("charlieList", largePieceOfPlumService.getCharlieList());
			model.addAttribute("OPERATOR",UserUtils.getUser().getId());
			model.addAttribute("OPERATOR_NAME",UserUtils.getUser().getName());
			return "prss/produce/addLargePieceOfPlum";
		} else if("modify".equals(param)) {
			model.addAttribute("paramModify", param);
			// 获取查理列表
			model.addAttribute("charlieList", largePieceOfPlumService.getCharlieList());
			// 获取表单数据
			List<Map<String, Object>> list = largePieceOfPlumService.getFormData(searchId, sign);
			dealAbnormalData(model, list);
			return "prss/produce/addLargePieceOfPlum";
		}
    		
    	
    	return pdate;
    	
    }
	/**
     * 处理页面加载信息
     * @param model
     * @param list
     */
    private void dealAbnormalData(Model model, List<Map<String, Object>> list) {
    	if(list.size() != 0) {
    		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				for (String key : map.keySet()) {
					model.addAttribute(key, map.get(key));
			    }
			}
    	}
    }
    /**
     * 获取当前航空数据， 不区分大件行李或行李下拉
     * @param flightDate
     * @param flightNumber
     * @return
     */
    @RequestMapping(value = "getAirFligthInfo")
    @ResponseBody
    public String getAirFligthInfo(String flightDate, String flightNumber) {
    	if(null == flightDate || "".equals(flightDate)) {
    		return "";
    	}
    	if(null == flightNumber || "".equals(flightNumber)) {
    		return "";
    	}
    	return JSON.toJSONString(largePieceOfPlumService.getAirFligthInfo(flightDate, flightNumber), SerializerFeature.WriteMapNullValue);
    	
    }
    
    /**
     * 新增数据
     * @param param
     * @param largePieceOfPlum
     * @return
     */
    @RequestMapping(value = "add/{param}")
    @ResponseBody
    public String add(@PathVariable("param") String param, LargePieceOfPlum largePieceOfPlum) {
    	if("".equals(param) || null == param) {
    		return "faile";
    	}
    	largePieceOfPlum.setCreateDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
    	return largePieceOfPlumService.save(largePieceOfPlum, param);
	
    }
    
    /**
     * 修改数据
     * @param param
     * @param largePieceOfPlum
     * @return
     */
    @RequestMapping(value = "modify/{param}")
    @ResponseBody
    public String modify(@PathVariable("param") String param, LargePieceOfPlum largePieceOfPlum) {
    	if("".equals(param) || null == param) {
    		return "faile";
    	}
    	return largePieceOfPlumService.modify(largePieceOfPlum, param);
    	
    }
    /**
     * 删除数据
     * @param param
     * @param searchId
     * @return
     */
    @RequestMapping(value = "del/{param}")
    @ResponseBody
    public String modify(@PathVariable("param") String param, String searchId) {
    	if("".equals(param) || null == param) {
    		return "faile";
    	}
    	return largePieceOfPlumService.delete(searchId, param);
    	
    }
	/**
	 * 
	 *Discription:加载图片流.
	 *@param res
	 *@param id
	 */
	@RequestMapping(value = { "outputPicture" })
	public void outputPicture(HttpServletResponse res, String id){
		OutputStream out = null;
        try {
        	byte[] is = fileService.doDownLoadFile(id);
        	out = res.getOutputStream();
            out.write(is);
		} catch (Exception e) {
			logger.error("数据流写入失败"+e.getMessage());
		} finally {
			try {
				out.flush();
				out.close();
			} catch (Exception e2) {
				logger.error("输出流关闭失败"+e2.getMessage());
			}
		}
	}
	
	 /**
     * 当前航班是否存在
     * @param searchId
     * @return
     */
    @RequestMapping(value = "isExists/{sign}")
    @ResponseBody
    public String isExists(String flightDate, String flightNumber, @PathVariable("sign") String sign) {
    	if(null == flightDate || "".equals(flightDate)) {
    		return "faile";
    	}
    	if(null == flightNumber || "".equals(flightNumber)) {
    		return "faile";
    	}
    	if(null == sign || "".equals(sign)) {
    		return "faile";
    	}
    	List<Map<String,Object>> list = largePieceOfPlumService.isExists(flightDate, flightNumber, sign);
    	return list.size() > 0? "1" : "0";
    }
}

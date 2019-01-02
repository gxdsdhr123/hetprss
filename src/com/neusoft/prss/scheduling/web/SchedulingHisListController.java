package com.neusoft.prss.scheduling.web;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.grid.service.GridColumnService;
import com.neusoft.prss.scheduling.service.SchedulingHisListService;

@Controller
@RequestMapping(value = "${adminPath}/schedulingHisList")
public class SchedulingHisListController extends BaseController {
	@Resource
	private CacheService cacheService;
	@Resource
	private SchedulingHisListService schedulingHisListService;
	@Resource
	private GridColumnService gridService;

	/**
	 * 
	 * Discription:获取航班动态页面.
	 * 
	 * @return
	 * @return:历史航班动态页面
	 * @author:yuzd
	 * @update:2017年9月18日 yuzd [变更描述]
	 */
	@RequestMapping(value = "list")
	public String list(Model model, String schemaId) {
		model.addAttribute("schemaId", schemaId);
		model.addAttribute("historyFlag", "Y");
		dimAttributes(model);
		return "prss/scheduling/schedulingHisList";
	}

	/**
	 * 
	 * Discription:航班动态-基本表格-默认数据.
	 * 
	 * @param request
	 * @return
	 * @return:返回值意义
	 * @author:SunJ
	 * @update:2017年9月26日 SunJ [变更描述]
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getDynamic")
	@ResponseBody
	public JSONArray getDynamic(String hisDate, String switches,String flagBGS,String schema,String param,String suffix) {
		if (suffix == null) {
			suffix = "";
		}
		if (StringUtils.isBlank(hisDate)) {
			return new JSONArray();
		}
		String reskind = UserUtils.getCurrentJobKind().size() > 0 ? UserUtils.getCurrentJobKind().get(0).getKindCode() : "";
		if (param != null) {
			param = StringEscapeUtils.unescapeHtml4(param);
            Map<String,Object> params = JSON.parseObject(param);
            if (params.get("vipFlags") != null && StringUtils.isNotBlank(params.get("vipFlags").toString())) {
                String vipFlags = params.get("vipFlags").toString();
                String vFlags[] = vipFlags.split(",");
                params.put("vipFlags", vFlags);
            }
            if (params.get("gate") != null && StringUtils.isNotBlank(params.get("gate").toString())) {
                String gate = params.get("gate").toString();
                String gates[] = gate.split(",");
                params.put("gates", gates);
            }
			JSONArray jsonArr = schedulingHisListService.getDynamic(hisDate, switches, flagBGS, reskind, schema, suffix,
                    UserUtils.getUser().getId(), params);
			return jsonArr;
		} else {
			JSONArray jsonArr = schedulingHisListService.getDynamic(hisDate, switches, flagBGS, reskind, schema, suffix,
                    UserUtils.getUser().getId());
			return jsonArr;
		}
	}
	
	/**
     * 
     * Discription:获取下拉选项.
     * 
     * @param model
     * @return:返回值意义
     * @author:yu-zd
     * @update:2017年9月20日 yu-zd [变更描述]
     */
    private void dimAttributes(Model model) {
        try {
            // 性质
            // JSONArray fltPropertys =
            // cacheService.getOpts("dim_flight_property", "property_code",
            // "property_shortname");
            //JSONArray fltPropertys = cacheService.getCommonDict("flight_property");
        	//延误原因
            JSONArray delyReson = cacheService.getOpts("dim_delay","delay_code","description_cn");
            model.addAttribute("delyReson", delyReson);
            //机位
            JSONArray bay = cacheService.getList("dim_bay");
            model.addAttribute("bay", bay);
            //延误原因类型
            JSONArray delay_type = cacheService.getList("dim_delay_type");
            model.addAttribute("delay_type", delay_type);
            //性质
            JSONArray fltPropertys = cacheService.getList("dim_task");
            model.addAttribute("fltPropertys", fltPropertys);
            // 外航/国内
            JSONArray alnFlags = cacheService.getCommonDict("airline_flag");
            model.addAttribute("alnFlags", alnFlags);
            // 航线性质
            JSONArray alntype = cacheService.getCommonDict("alntype");
            model.addAttribute("alntype", alntype);
            // 机型分类
            JSONArray actkind = cacheService.getList("dim_actkind");
            model.addAttribute("actkinds", actkind);

            // 是否为宽体机型
            JSONArray acttypeSizes = cacheService.getCommonDict("aln_flag");
            model.addAttribute("acttypeSizes", acttypeSizes);
            // 航站楼
            JSONArray terminals = cacheService.getCommonDict("terminal");
            model.addAttribute("terminals", terminals);
            // 航班范围
            JSONArray flightScopes = cacheService.getCommonDict("flight_scope");
            model.addAttribute("flightScopes", flightScopes);
            // 机坪区域
            JSONArray aprons = cacheService.getList("dim_bay_apron");
            model.addAttribute("aprons", aprons);
            // 廊桥位、外围航班
            JSONArray GAFlag = cacheService.getCommonDict("GAFlag");
            model.addAttribute("GAFlag", GAFlag);
            // 标识
            JSONArray identifying = cacheService.getCommonDict("identifying");
            model.addAttribute("identifying", identifying);
            // 状态1
            JSONArray actStatus = cacheService.getCommonDict("acfStatus");
            model.addAttribute("actStatus", actStatus);
            // 状态2
            JSONArray flyStatus = cacheService.getCommonDict("flyStatus");
            model.addAttribute("flyStatus", flyStatus);
            // 要客标识
            JSONArray vipFlag = cacheService.getCommonDict("vipFlag");
            model.addAttribute("vipFlags", vipFlag);
            // 跑道
            JSONArray runways = getDimFromOracle("dim_runway", "RUNWAY_CODE", "RUNWAY_CODE");
            model.addAttribute("runways", runways);
            // 除冰坪
            JSONArray dprks = gridService.getDimFromOracle("dim_dprk_code", "DPRK_CODE",
                    "DPRK_CODE||' '||RUNWAY||' '||W_OR_E");
            model.addAttribute("dprks", dprks);
            // 除冰位
            JSONArray dcnds = gridService.getDimFromOracle("dim_dcnd_code", "DCND_CODE",
                    "DCND_CODE||' '||DPRK_CODE||' '||(case when SlOW_FLAG=0 then '关车除冰' when slow_flag=1 then '慢车除冰' end)");
            model.addAttribute("dcnds", dcnds);
            // 除冰航班
            JSONArray iceFlags = cacheService.getCommonDict("iceFlag");
            model.addAttribute("iceFlags", iceFlags);
            //保障状态
            JSONArray guarantee = gridService.getDimFromOracle("dim_delay_type where parent is null","id","name");
            model.addAttribute("guarantee", guarantee);
        } catch (Exception e) {
            logger.error("添加下拉框选项失败" + e.getMessage());
        }
    }
    
    public JSONArray getDimFromOracle(String table,String idcol,String textcol) {
        return gridService.getDimFromOracle(table, idcol, textcol);
    }

}

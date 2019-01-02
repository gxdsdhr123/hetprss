package com.neusoft.prss.telegraph.web;

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.ui.Model;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.telegraph.service.TelegraphAnalysisService;
import com.neusoft.prss.telegraph.service.TelegraphService;

@Controller
@RequestMapping(value = "${adminPath}/telegraph/analysis")
public class TelegraphAnalysisController extends BaseController {

	@Resource
	private TelegraphAnalysisService telegraphAnalysisService;
	
    @RequestMapping(value = "list" )
    public String analysisList() {
        return "prss/telegraph/autoanalysislist";
    }
    
    @RequestMapping(value = "autoanalysislist")
    @ResponseBody
    public Map<String, Object> autoanalysislist(int pageSize, int pageNumber, String flightdate, String flightnumber,
            String aircraft,String sortName,String sortOrder) {
        JSONObject param = new JSONObject();
        int begin = (pageNumber - 1) * pageSize;
        int end = pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("flightdate", flightdate);
        param.put("flightnumber", flightnumber);
        param.put("aircraft", aircraft);
        param.put("sortName", sortName);
        param.put("sortOrder", sortOrder);
        return telegraphAnalysisService.getAnalysisList(param);
    }

    
    @RequestMapping(value = "analysis" )
    public String analysis(String id,Model model) {
        if(!StringUtils.isBlank(id)){
            JSONObject analysis = telegraphAnalysisService.getAnalysisById(id);
            model.addAttribute("analysis", analysis);
        }
        return "prss/telegraph/autoanalysis";
    } 
    
    @RequestMapping(value = "doAnalysis",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject doAnalysis(String mtext){
        JSONObject jsonObject = new JSONObject();
        String[] telegraphs = {mtext};
        try {
           /* JSONArray jsonArray = telegraphService.analysisTelegraphManual(telegraphs);
            if(jsonArray.size()>0)
                jsonObject = jsonArray.getJSONObject(0);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

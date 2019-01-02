/**
 * application name:btprss
 * application describing:this class handles the request of the client
 * copyright:Copyright(c) 2018 Neusoft LTD.
 * company:Neusoft
 * time:2018年6月22日 上午8:56:35
 *
 * @author:wangtg
 * @version:[v1.0]
 */
package com.neusoft.prss.produce.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.produce.service.BillOperatorZXService;
import com.neusoft.prss.stand.entity.ResultByCus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "${adminPath}/produce/billOperatorZX")
public class BillOperatorZXController extends BaseController {

    @Autowired
    private BillOperatorZXService billOperatorZXService;

    @RequestMapping(value = "list")
    public String list(Model model) {
        return "prss/produce/billOperatorZXList";
    }

    @ResponseBody
    @RequestMapping(value = "dataList")
    public String getDataList(@RequestParam("dateStart") String dateStart, @RequestParam("flightNumber") String flightNumber,
                              @RequestParam("operator") String operator) {
        JSONArray json = billOperatorZXService.getDataList(dateStart, flightNumber, operator);
        String result = json.toJSONString();
        return result;
    }

    @RequestMapping(value = "dataInfo")
    public String getDataList(Model model, @RequestParam("fltid") String fltid) {

        JSONObject json = new JSONObject();
        json.put("info", billOperatorZXService.getDataInfo(fltid));
        json.put("detail", billOperatorZXService.getDataDetail(fltid));
        model.addAttribute("result",json);
        return "prss/produce/billOperatorZXForm";
    }

    @ResponseBody
    @RequestMapping(value = "save" )
    public ResultByCus save() {
        boolean flag = true;
        ResultByCus result=new ResultByCus();

        if(flag){
            result.setCode("0000");
            result.setMsg("操作成功");
        }
        return result;
    }
}

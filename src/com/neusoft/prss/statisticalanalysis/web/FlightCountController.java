package com.neusoft.prss.statisticalanalysis.web;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.statisticalanalysis.service.FlightCountService;

@Controller
@RequestMapping(value = "${adminPath}/flightCount")
public class FlightCountController extends BaseController {

	@Resource
    private FlightCountService flightCountService;
	
    @RequestMapping(value = "showPage" )
    public String ShowPage() {
        return "prss/statisticalanalysis/flightCount";
    }
    
    /**
     * 
     * Discription:航班架次统计页面.
     * 
     * @param request
     * @param response
     * @return:返回值意义
     * @author:yunwq
     * @update:2018年01月19日 yunwq [变更描述]
     */
    @RequestMapping(value = "searchPage" )
    public String SearchPage( Model model,
    		@RequestParam(value="beginTime",required=false,defaultValue="null") String beginTime,
    		@RequestParam(value="endTime",required=false,defaultValue="null") String endTime,
    		@RequestParam(value="vsBeginTime",required=false,defaultValue="null") String vsBeginTime,
    		@RequestParam(value="vsEndTime",required=false,defaultValue="null") String vsEndTime) {
    	
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("beginTime", beginTime);
    	map.put("endTime", endTime);
    	map.put("vsBeginTime", vsBeginTime);
    	map.put("vsEndTime", vsEndTime);
    	
    	HashMap<String, Object> first = flightCountService.getFirstCount(map);//开始结束时间查询表SA_IOF_OSCAR_DAY
    	HashMap<String, Object> second = flightCountService.getSecondCount(map);//对比开始结束时间查询表SA_IOF_OSCAR_DAY
    	
    	if(first.get("FLT_TOTAL") == null || "null".equals(first.get("FLT_TOTAL").toString()) ){
    		model.addAttribute("beginTime", beginTime);
        	model.addAttribute("endTime", endTime);
        	model.addAttribute("vsBeginTime", vsBeginTime);
        	model.addAttribute("vsEndTime", vsEndTime);
    		model.addAttribute("msg", beginTime+"--"+endTime+"区间没有航班信息！");
    		return "prss/statisticalanalysis/flightCount";
    	}
    	if(second.get("FLT_TOTAL") == null || "null".equals(second.get("FLT_TOTAL").toString()) ){
    		model.addAttribute("beginTime", beginTime);
        	model.addAttribute("endTime", endTime);
        	model.addAttribute("vsBeginTime", vsBeginTime);
        	model.addAttribute("vsEndTime", vsEndTime);
    		model.addAttribute("msg", "对比区间"+vsBeginTime+"-"+vsEndTime+"没有航班信息！");
    		return "prss/statisticalanalysis/flightCount";
    	}
    	//总架次、环比
    	int fltTotal = Integer.parseInt(first.get("FLT_TOTAL").toString());
    	String fltTotalRatio = ComputeRatio(first.get("FLT_TOTAL"),second.get("FLT_TOTAL")); 
    	//外航架次、环比
    	int iNumTotal = Integer.parseInt(first.get("I_NUM").toString());
    	String iNumTotalRatio = ComputeRatio(first.get("I_NUM"),second.get("I_NUM")); 
    	//内航架次、环比
    	int dNumTotal = Integer.parseInt(first.get("D_NUM").toString());
    	String dNumTotalRatio = ComputeRatio(first.get("D_NUM"),second.get("D_NUM"));
    	//公务机架次、环比
    	int gwTotal = Integer.parseInt(first.get("GW_TOTAL").toString());
    	String gwTotalRatio = ComputeRatio(first.get("GW_TOTAL"),second.get("GW_TOTAL"));
    	//包机架次、环比
    	int bjTotal = Integer.parseInt(first.get("BJ_TOTAL").toString());
    	String bjTotalRatio = ComputeRatio(first.get("BJ_TOTAL"),second.get("BJ_TOTAL"));
    	//客机、环比
    	int areNumTotal = Integer.parseInt(first.get("AIR_NUM").toString());
    	String areNumTotalRatio = ComputeRatio(first.get("AIR_NUM"),second.get("AIR_NUM"));
    	//货机、环比
    	int carNumTotal = Integer.parseInt(first.get("CAR_NUM").toString());
    	String carNumTotalRatio = ComputeRatio(first.get("CAR_NUM"),second.get("CAR_NUM"));
    	//东航、环比
    	int dhTotal = Integer.parseInt(first.get("DH_TOTAL").toString());
    	String dhTotalRatio = ComputeRatio(first.get("DH_TOTAL"),second.get("DH_TOTAL"));
    	//南航、环比
    	int nhTotal = Integer.parseInt(first.get("NH_TOTAL").toString());
    	String nhTotalRatio = ComputeRatio(first.get("NH_TOTAL"),second.get("NH_TOTAL"));
    	//海航、环比
    	int hhTotal = Integer.parseInt(first.get("HH_TOTAL").toString());
    	String hhTotalRatio = ComputeRatio(first.get("HH_TOTAL"),second.get("HH_TOTAL"));
    	//国内货机、环比
    	int dcTotal = Integer.parseInt(first.get("DC_TOTAL").toString());
    	String dcTotalRatio = ComputeRatio(first.get("DC_TOTAL"),second.get("DC_TOTAL"));
    	//公务机国内架次、环比
    	int gwdNumTotal = Integer.parseInt(first.get("GWD_NUM").toString());
    	String gwdNumTotalRatio = ComputeRatio(first.get("GWD_NUM"),second.get("GWD_NUM"));
    	//公务机国外架次、环比
    	int gwiNumTotal = Integer.parseInt(first.get("GWI_NUM").toString());
    	String gwiNumTotalRatio = ComputeRatio(first.get("GWI_NUM"),second.get("GWI_NUM"));
    	//包机国内架次、环比
    	int bjdNumTotal = Integer.parseInt(first.get("BJD_NUM").toString());
    	String bjdNumTotalRatio = ComputeRatio(first.get("BJD_NUM"),second.get("BJD_NUM"));
    	//包机国外架次、环比
    	int bjiNumTotal = Integer.parseInt(first.get("BJI_NUM").toString());
    	String bjiNumTotalRatio = ComputeRatio(first.get("BJI_NUM"),second.get("BJI_NUM"));
    	//东航国内架次、环比
    	int dhdNumTotal = Integer.parseInt(first.get("DHD_NUM").toString());
    	String dhdNumTotalRatio = ComputeRatio(first.get("DHD_NUM"),second.get("DHD_NUM"));
    	//东航国际架次、环比
    	int dhiNumTotal = Integer.parseInt(first.get("DHI_NUM").toString());
    	String dhiNumTotalRatio = ComputeRatio(first.get("DHI_NUM"),second.get("DHI_NUM"));
    	//南航国内架次、环比
    	int nhdNumTotal = Integer.parseInt(first.get("NHD_NUM").toString());
    	String nhdNumTotalRatio = ComputeRatio(first.get("NHD_NUM"),second.get("NHD_NUM"));
    	//南航国际架次、环比
    	int nhiNumTotal = Integer.parseInt(first.get("NHI_NUM").toString());
    	String nhiNumTotalRatio = ComputeRatio(first.get("NHI_NUM"),second.get("NHI_NUM"));
    	//海航国内架次、环比
    	int hhdNumTotal = Integer.parseInt(first.get("HHD_NUM").toString());
    	String hhdNumTotalRatio = ComputeRatio(first.get("HHD_NUM"),second.get("HHD_NUM"));
    	//海航国际架次、环比
    	int hhiNumTotal = Integer.parseInt(first.get("HHI_NUM").toString());
    	String hhiNumTotalRatio = ComputeRatio(first.get("HHI_NUM"),second.get("HHI_NUM"));
    	//国内货机国内架次、环比
    	int dcdNumTotal = Integer.parseInt(first.get("DCD_NUM").toString());
    	String dcdNumTotalRatio = ComputeRatio(first.get("DCD_NUM"),second.get("DCD_NUM"));
    	//国内货机国际架次、环比
    	int dciNumTotal = Integer.parseInt(first.get("DCI_NUM").toString());
    	String dciNumTotalRatio = ComputeRatio(first.get("DCI_NUM"),second.get("DCI_NUM"));
    	
    	model.addAttribute("beginTime", beginTime);
    	model.addAttribute("endTime", endTime);
    	model.addAttribute("vsBeginTime", vsBeginTime);
    	model.addAttribute("vsEndTime", vsEndTime);
    	model.addAttribute("fltTotal", fltTotal);
    	model.addAttribute("fltTotalRatio", fltTotalRatio);
    	model.addAttribute("iNumTotal", iNumTotal);
    	model.addAttribute("iNumTotalRatio", iNumTotalRatio);
    	model.addAttribute("dNumTotal", dNumTotal);
    	model.addAttribute("dNumTotalRatio", dNumTotalRatio);
    	model.addAttribute("gwTotal", gwTotal);
    	model.addAttribute("gwTotalRatio", gwTotalRatio);
    	model.addAttribute("bjTotal", bjTotal);
    	model.addAttribute("bjTotalRatio", bjTotalRatio);
    	model.addAttribute("areNumTotal", areNumTotal);
    	model.addAttribute("areNumTotalRatio", areNumTotalRatio);
    	model.addAttribute("carNumTotal", carNumTotal);
    	model.addAttribute("carNumTotalRatio", carNumTotalRatio);
    	model.addAttribute("dhTotal", dhTotal);
    	model.addAttribute("dhTotalRatio", dhTotalRatio);
    	model.addAttribute("nhTotal", nhTotal);
    	model.addAttribute("nhTotalRatio", nhTotalRatio);
    	model.addAttribute("hhTotal", hhTotal);
    	model.addAttribute("hhTotalRatio", hhTotalRatio);
    	model.addAttribute("dcTotal", dcTotal);
    	model.addAttribute("dcTotalRatio", dcTotalRatio);
    	model.addAttribute("gwdNumTotal", gwdNumTotal);
    	model.addAttribute("gwdNumTotalRatio", gwdNumTotalRatio);
    	model.addAttribute("gwiNumTotal", gwiNumTotal);
    	model.addAttribute("gwiNumTotalRatio", gwiNumTotalRatio);
    	model.addAttribute("bjdNumTotal", bjdNumTotal);
    	model.addAttribute("bjdNumTotalRatio", bjdNumTotalRatio);
    	model.addAttribute("bjiNumTotal", bjiNumTotal);
    	model.addAttribute("bjiNumTotalRatio", bjiNumTotalRatio);
    	model.addAttribute("dhdNumTotal", dhdNumTotal);
    	model.addAttribute("dhdNumTotalRatio", dhdNumTotalRatio);
    	model.addAttribute("dhiNumTotal", dhiNumTotal);
    	model.addAttribute("dhiNumTotalRatio", dhiNumTotalRatio);
    	model.addAttribute("nhdNumTotal", nhdNumTotal);
    	model.addAttribute("nhdNumTotalRatio", nhdNumTotalRatio);
    	model.addAttribute("nhiNumTotal", nhiNumTotal);
    	model.addAttribute("nhiNumTotalRatio", nhiNumTotalRatio);
    	model.addAttribute("hhdNumTotal", hhdNumTotal);
    	model.addAttribute("hhdNumTotalRatio", hhdNumTotalRatio);
    	model.addAttribute("hhiNumTotal", hhiNumTotal);
    	model.addAttribute("hhiNumTotalRatio", hhiNumTotalRatio);
    	model.addAttribute("dcdNumTotal", dcdNumTotal);
    	model.addAttribute("dcdNumTotalRatio", dcdNumTotalRatio);
    	model.addAttribute("dciNumTotal", dciNumTotal);
    	model.addAttribute("dciNumTotalRatio", dciNumTotalRatio);
    	model.addAttribute("IN_FLT_NUM", first.get("IN_FLT_NUM").toString());//进港架次
    	model.addAttribute("OUT_FLT_NUM", first.get("OUT_FLT_NUM").toString());//出港架次
    	
    	map.put("type", 1);//1.外航货机架次
    	List<HashMap<String, Object>> waihangBegin = flightCountService.getBeginSaAlnOscarDay(map);//开始结束时间查询表SA_ALN_OSCAR_DAY
    	List<HashMap<String, Object>> waihangEnd = flightCountService.getEndSaAlnOscarDay(map);//对比开始结束时间查询表SA_ALN_OSCAR_DAY
    	map.put("type", 2);//2.内行货机架次
    	List<HashMap<String, Object>> neihangBegin = flightCountService.getBeginSaAlnOscarDay(map);//开始结束时间查询表SA_ALN_OSCAR_DAY
    	List<HashMap<String, Object>> neihangEnd = flightCountService.getEndSaAlnOscarDay(map);//对比开始结束时间查询表SA_ALN_OSCAR_DAY
    	map.put("type", 3);//3.窄体机型架次
    	List<HashMap<String, Object>> zaitiBegin = flightCountService.getBeginSaAlnOscarDay(map);//开始结束时间查询表SA_ALN_OSCAR_DAY
    	List<HashMap<String, Object>> zaitiEnd = flightCountService.getEndSaAlnOscarDay(map);//对比开始结束时间查询表SA_ALN_OSCAR_DAY
    	map.put("type", 4);//4.宽体机型架次
    	List<HashMap<String, Object>> kuantiBegin = flightCountService.getBeginSaAlnOscarDay(map);//开始结束时间查询表SA_ALN_OSCAR_DAY
    	List<HashMap<String, Object>> kuantiEnd = flightCountService.getEndSaAlnOscarDay(map);//对比开始结束时间查询表SA_ALN_OSCAR_DAY

    	int one = waihangBegin.size();
    	int two = neihangBegin.size();
    	int three = zaitiBegin.size();
    	int four = kuantiBegin.size();
    	List<Integer> nums = new ArrayList<Integer>();
        nums.add(one);
        nums.add(two);
        nums.add(three);
        nums.add(four);
        int Max = Collections.max(nums);
    	
        HashMap<String, Object> kongbai = new HashMap<String, Object>();//用于对齐表格的填充物
        kongbai.put("ALN_ACT", "-");
        kongbai.put("FLT_NUM", "-");
        kongbai.put("ratio", "-");
        
        int oneTotal = 0;//外航货机架次合计总数
        int vsoneTotal = 0;//对比外航货机架次合计总数
    	for (int i = 0; i < Max; i++) {
    		if(i>=one){
    			waihangBegin.add(kongbai);
    		}
    		String ALN_ACT = waihangBegin.get(i).get("ALN_ACT").toString();
    		for (int j = 0; j < waihangEnd.size(); j++) {
				if(waihangEnd.get(j).get("ALN_ACT").toString().equals(ALN_ACT)){
					vsoneTotal = vsoneTotal + Integer.parseInt(waihangEnd.get(j).get("FLT_NUM").toString());
					String ratio = ComputeRatio(waihangBegin.get(i).get("FLT_NUM"),waihangEnd.get(j).get("FLT_NUM"));
					waihangBegin.get(i).put("ratio", ratio);
				}
			}
    		if(i<one){
    			oneTotal = oneTotal + Integer.parseInt(waihangBegin.get(i).get("FLT_NUM").toString());
    		}
		}
    	int twoTotal = 0;//内航货机架次合计总数
        int vstwoTotal = 0;//对比内航货机架次合计总数
    	for (int i = 0; i < Max; i++) {
    		if(i>=two){
    			neihangBegin.add(kongbai);
    		}
    		String ALN_ACT = neihangBegin.get(i).get("ALN_ACT").toString();
    		for (int j = 0; j < neihangEnd.size(); j++) {
				if(neihangEnd.get(j).get("ALN_ACT").toString().equals(ALN_ACT)){
					vstwoTotal = vstwoTotal + Integer.parseInt(neihangEnd.get(j).get("FLT_NUM").toString());
					String ratio = ComputeRatio(neihangBegin.get(i).get("FLT_NUM"),neihangEnd.get(j).get("FLT_NUM"));
					neihangBegin.get(i).put("ratio", ratio);
				}
			}
    		if(i<two){
    			twoTotal = twoTotal + Integer.parseInt(neihangBegin.get(i).get("FLT_NUM").toString());
    		}
		}
    	int threeTotal = 0;//窄体机型架次合计总数
        int vsthreeTotal = 0;//对比窄体机型架次合计总数
    	for (int i = 0; i < Max; i++) {
    		if(i>=three){
    			zaitiBegin.add(kongbai);
    		}
    		String ALN_ACT = zaitiBegin.get(i).get("ALN_ACT").toString();
    		for (int j = 0; j < zaitiEnd.size(); j++) {
				if(zaitiEnd.get(j).get("ALN_ACT").toString().equals(ALN_ACT)){
					vsthreeTotal = vsthreeTotal + Integer.parseInt(zaitiEnd.get(j).get("FLT_NUM").toString());
					String ratio = ComputeRatio(zaitiBegin.get(i).get("FLT_NUM"),zaitiEnd.get(j).get("FLT_NUM"));
					zaitiBegin.get(i).put("ratio", ratio);
				}
			}
    		if(i<three){
    			threeTotal = threeTotal + Integer.parseInt(zaitiBegin.get(i).get("FLT_NUM").toString());
    		}
		}
    	int fourTotal = 0;//宽体机型架次合计总数
        int vsfourTotal = 0;//对比宽体机型架次合计总数
    	for (int i = 0; i < Max; i++) {
    		if(i>=four){
    			kuantiBegin.add(kongbai);
    		}
    		String ALN_ACT = kuantiBegin.get(i).get("ALN_ACT").toString();
    		for (int j = 0; j < kuantiEnd.size(); j++) {
				if(kuantiEnd.get(j).get("ALN_ACT").toString().equals(ALN_ACT)){
					vsfourTotal = vsfourTotal + Integer.parseInt(kuantiEnd.get(j).get("FLT_NUM").toString());
					String ratio = ComputeRatio(kuantiBegin.get(i).get("FLT_NUM"),kuantiEnd.get(j).get("FLT_NUM"));
					kuantiBegin.get(i).put("ratio", ratio);
				}
			}
    		if(i<four){
    			fourTotal = fourTotal + Integer.parseInt(kuantiBegin.get(i).get("FLT_NUM").toString());
    		}
		}
    	
    	model.addAttribute("waihangBegin", waihangBegin);
    	model.addAttribute("neihangBegin", neihangBegin);
    	model.addAttribute("zaitiBegin", zaitiBegin);
    	model.addAttribute("kuantiBegin", kuantiBegin);
    	model.addAttribute("oneTotal", oneTotal);
    	model.addAttribute("oneTotalRatio", ComputeRatio(oneTotal,vsoneTotal));
    	model.addAttribute("twoTotal", twoTotal);
    	model.addAttribute("twoTotalRatio", ComputeRatio(twoTotal,vstwoTotal));
    	model.addAttribute("threeTotal", threeTotal);
    	model.addAttribute("threeTotalRatio", ComputeRatio(threeTotal,vsthreeTotal));
    	model.addAttribute("fourTotal", fourTotal);
    	model.addAttribute("fourTotalRatio", ComputeRatio(fourTotal,vsfourTotal));
    	
        return "prss/statisticalanalysis/flightCount";
    }
    
    /**
     * 
     * Discription:计算环比公式.
     * 
     * @param request
     * @param response
     * @return:返回值意义
     * @author:yunwq
     * @update:2018年01月19日 yunwq [变更描述]
     */
	private String ComputeRatio(Object one,Object two){
		String result = "0.00";
		DecimalFormat df = new DecimalFormat("0.00");
		if(two == null || "".equals(two.toString()) || "0".equals(two.toString())){
			return result;
		}else{
			result = df.format((Double.parseDouble(one.toString()) - Double.parseDouble(two.toString()))/Double.parseDouble(two.toString()) * 100)+"%";
		}
		return result;
    }
	
	/**
     * 
     * Discription:航班架次统计打印.
     * 
     * @param request
     * @param response
     * @return:返回值意义
     * @author:yunwq
	 * @throws IOException 
     * @update:2018年01月22日 yunwq [变更描述]
     */
    @RequestMapping(value = "print")
	private void Print(HttpServletRequest request,HttpServletResponse response,String beginTimeDisplay,String endTimeDisplay,String vsBeginTimeDisplay,String vsEndTimeDisplay) throws IOException{
    	beginTimeDisplay = StringEscapeUtils.unescapeHtml4(beginTimeDisplay);
    	endTimeDisplay = StringEscapeUtils.unescapeHtml4(endTimeDisplay);
    	vsBeginTimeDisplay = StringEscapeUtils.unescapeHtml4(vsBeginTimeDisplay);
    	vsEndTimeDisplay = StringEscapeUtils.unescapeHtml4(vsEndTimeDisplay);
    	
    	XSSFWorkbook wb = new XSSFWorkbook();
    	Sheet sheet = wb.createSheet();
    	XSSFCellStyle cellStyle = wb.createCellStyle(); 
    	cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中    
    	
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("beginTime", beginTimeDisplay);
    	map.put("endTime", endTimeDisplay);
    	map.put("vsBeginTime", vsBeginTimeDisplay);
    	map.put("vsEndTime", vsEndTimeDisplay);
    	
    	HashMap<String, Object> first = flightCountService.getFirstCount(map);//开始结束时间查询表SA_IOF_OSCAR_DAY
    	HashMap<String, Object> second = flightCountService.getSecondCount(map);//对比开始结束时间查询表SA_IOF_OSCAR_DAY
    	
    	//总架次、环比
    	int fltTotal = Integer.parseInt(first.get("FLT_TOTAL").toString());
    	String fltTotalRatio = ComputeRatio(first.get("FLT_TOTAL"),second.get("FLT_TOTAL")); 
    	//外航架次、环比
    	int iNumTotal = Integer.parseInt(first.get("I_NUM").toString());
    	String iNumTotalRatio = ComputeRatio(first.get("I_NUM"),second.get("I_NUM")); 
    	//内航架次、环比
    	int dNumTotal = Integer.parseInt(first.get("D_NUM").toString());
    	String dNumTotalRatio = ComputeRatio(first.get("D_NUM"),second.get("D_NUM"));
    	//公务机架次、环比
    	int gwTotal = Integer.parseInt(first.get("GW_TOTAL").toString());
    	String gwTotalRatio = ComputeRatio(first.get("GW_TOTAL"),second.get("GW_TOTAL"));
    	//包机架次、环比
    	int bjTotal = Integer.parseInt(first.get("BJ_TOTAL").toString());
    	String bjTotalRatio = ComputeRatio(first.get("BJ_TOTAL"),second.get("BJ_TOTAL"));
    	//客机、环比
    	int areNumTotal = Integer.parseInt(first.get("AIR_NUM").toString());
    	String areNumTotalRatio = ComputeRatio(first.get("AIR_NUM"),second.get("AIR_NUM"));
    	//货机、环比
    	int carNumTotal = Integer.parseInt(first.get("CAR_NUM").toString());
    	String carNumTotalRatio = ComputeRatio(first.get("CAR_NUM"),second.get("CAR_NUM"));
    	//东航、环比
    	int dhTotal = Integer.parseInt(first.get("DH_TOTAL").toString());
    	String dhTotalRatio = ComputeRatio(first.get("DH_TOTAL"),second.get("DH_TOTAL"));
    	//南航、环比
    	int nhTotal = Integer.parseInt(first.get("NH_TOTAL").toString());
    	String nhTotalRatio = ComputeRatio(first.get("NH_TOTAL"),second.get("NH_TOTAL"));
    	//海航、环比
    	int hhTotal = Integer.parseInt(first.get("HH_TOTAL").toString());
    	String hhTotalRatio = ComputeRatio(first.get("HH_TOTAL"),second.get("HH_TOTAL"));
    	//国内货机、环比
    	int dcTotal = Integer.parseInt(first.get("DC_TOTAL").toString());
    	String dcTotalRatio = ComputeRatio(first.get("DC_TOTAL"),second.get("DC_TOTAL"));
    	//公务机国内架次、环比
    	int gwdNumTotal = Integer.parseInt(first.get("GWD_NUM").toString());
    	String gwdNumTotalRatio = ComputeRatio(first.get("GWD_NUM"),second.get("GWD_NUM"));
    	//公务机国外架次、环比
    	int gwiNumTotal = Integer.parseInt(first.get("GWI_NUM").toString());
    	String gwiNumTotalRatio = ComputeRatio(first.get("GWI_NUM"),second.get("GWI_NUM"));
    	//包机国内架次、环比
    	int bjdNumTotal = Integer.parseInt(first.get("BJD_NUM").toString());
    	String bjdNumTotalRatio = ComputeRatio(first.get("BJD_NUM"),second.get("BJD_NUM"));
    	//包机国外架次、环比
    	int bjiNumTotal = Integer.parseInt(first.get("BJI_NUM").toString());
    	String bjiNumTotalRatio = ComputeRatio(first.get("BJI_NUM"),second.get("BJI_NUM"));
    	//东航国内架次、环比
    	int dhdNumTotal = Integer.parseInt(first.get("DHD_NUM").toString());
    	String dhdNumTotalRatio = ComputeRatio(first.get("DHD_NUM"),second.get("DHD_NUM"));
    	//东航国际架次、环比
    	int dhiNumTotal = Integer.parseInt(first.get("DHI_NUM").toString());
    	String dhiNumTotalRatio = ComputeRatio(first.get("DHI_NUM"),second.get("DHI_NUM"));
    	//南航国内架次、环比
    	int nhdNumTotal = Integer.parseInt(first.get("NHD_NUM").toString());
    	String nhdNumTotalRatio = ComputeRatio(first.get("NHD_NUM"),second.get("NHD_NUM"));
    	//南航国际架次、环比
    	int nhiNumTotal = Integer.parseInt(first.get("NHI_NUM").toString());
    	String nhiNumTotalRatio = ComputeRatio(first.get("NHI_NUM"),second.get("NHI_NUM"));
    	//海航国内架次、环比
    	int hhdNumTotal = Integer.parseInt(first.get("HHD_NUM").toString());
    	String hhdNumTotalRatio = ComputeRatio(first.get("HHD_NUM"),second.get("HHD_NUM"));
    	//海航国际架次、环比
    	int hhiNumTotal = Integer.parseInt(first.get("HHI_NUM").toString());
    	String hhiNumTotalRatio = ComputeRatio(first.get("HHI_NUM"),second.get("HHI_NUM"));
    	//国内货机国内架次、环比
    	int dcdNumTotal = Integer.parseInt(first.get("DCD_NUM").toString());
    	String dcdNumTotalRatio = ComputeRatio(first.get("DCD_NUM"),second.get("DCD_NUM"));
    	//国内货机国际架次、环比
    	int dciNumTotal = Integer.parseInt(first.get("DCI_NUM").toString());
    	String dciNumTotalRatio = ComputeRatio(first.get("DCI_NUM"),second.get("DCI_NUM"));
    	
    	//第一行
    	sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));//开始行，结束行，开始列，结束列
    	sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 15));
    	Row row = sheet.createRow(0);
    	Cell cell = row.createCell(0);
    	cell.setCellValue("总架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(8);
    	cell.setCellValue("环比");
    	cell.setCellStyle(cellStyle);
    	//第二行
    	sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));//开始行，结束行，开始列，结束列
    	sheet.addMergedRegion(new CellRangeAddress(1, 1, 8, 15));
    	row = sheet.createRow(1);
    	cell = row.createCell(0);
    	cell.setCellValue(fltTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(8);
    	cell.setCellValue(fltTotalRatio);
    	cell.setCellStyle(cellStyle);
    	//第三行
    	sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 1));//开始行，结束行，开始列，结束列
    	sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 3));
    	sheet.addMergedRegion(new CellRangeAddress(2, 2, 4, 7));
    	sheet.addMergedRegion(new CellRangeAddress(2, 2, 8, 11));
    	row = sheet.createRow(2);
    	cell = row.createCell(0);
    	cell.setCellValue("外航架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(2);
    	cell.setCellValue("环比");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(4);
    	cell.setCellValue("内航架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(8);
    	cell.setCellValue("环比");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(12);
    	cell.setCellValue("公务机架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(13);
    	cell.setCellValue("环比");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(14);
    	cell.setCellValue("包机架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(15);
    	cell.setCellValue("环比");
    	cell.setCellStyle(cellStyle);
    	//第四行
    	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 1));//开始行，结束行，开始列，结束列
    	sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 3));
    	sheet.addMergedRegion(new CellRangeAddress(3, 3, 4, 7));
    	sheet.addMergedRegion(new CellRangeAddress(3, 3, 8, 11));
    	row = sheet.createRow(3);
    	cell = row.createCell(0);
    	cell.setCellValue(iNumTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(2);
    	cell.setCellValue(iNumTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(4);
    	cell.setCellValue(dNumTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(8);
    	cell.setCellValue(dNumTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(12);
    	cell.setCellValue(gwTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(13);
    	cell.setCellValue(gwTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(14);
    	cell.setCellValue(bjTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(15);
    	cell.setCellValue(bjTotalRatio);
    	cell.setCellStyle(cellStyle);
    	//第五行
    	sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 1));//开始行，结束行，开始列，结束列
    	sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 3));
    	sheet.addMergedRegion(new CellRangeAddress(4, 4, 4, 5));
    	sheet.addMergedRegion(new CellRangeAddress(4, 4, 6, 7));
    	sheet.addMergedRegion(new CellRangeAddress(4, 4, 8, 9));
    	sheet.addMergedRegion(new CellRangeAddress(4, 4, 10, 11));
    	row = sheet.createRow(4);
    	cell = row.createCell(0);
    	cell.setCellValue("客机");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(2);
    	cell.setCellValue("货机");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(4);
    	cell.setCellValue("东航");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(6);
    	cell.setCellValue("南航");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(8);
    	cell.setCellValue("海航");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(10);
    	cell.setCellValue("国内货机");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(12);
    	cell.setCellValue("国内");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(13);
    	cell.setCellValue("国际");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(14);
    	cell.setCellValue("国内");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(15);
    	cell.setCellValue("国际");
    	cell.setCellStyle(cellStyle);
    	//第六行
    	sheet.addMergedRegion(new CellRangeAddress(5, 5, 2, 3));
    	sheet.addMergedRegion(new CellRangeAddress(5, 5, 4, 5));
    	sheet.addMergedRegion(new CellRangeAddress(5, 5, 6, 7));
    	sheet.addMergedRegion(new CellRangeAddress(5, 5, 8, 9));
    	sheet.addMergedRegion(new CellRangeAddress(5, 5, 10, 11));
    	row = sheet.createRow(5);
    	cell = row.createCell(0);
    	cell.setCellValue("架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(1);
    	cell.setCellValue(areNumTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(2);
    	cell.setCellValue(carNumTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(4);
    	cell.setCellValue(dhTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(6);
    	cell.setCellValue(nhTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(8);
    	cell.setCellValue(hhTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(10);
    	cell.setCellValue(dcTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(12);
    	cell.setCellValue(gwdNumTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(13);
    	cell.setCellValue(gwiNumTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(14);
    	cell.setCellValue(bjdNumTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(15);
    	cell.setCellValue(bjiNumTotal);
    	cell.setCellStyle(cellStyle);
    	//第七行
    	sheet.addMergedRegion(new CellRangeAddress(6, 6, 2, 3));
    	sheet.addMergedRegion(new CellRangeAddress(6, 6, 4, 5));
    	sheet.addMergedRegion(new CellRangeAddress(6, 6, 6, 7));
    	sheet.addMergedRegion(new CellRangeAddress(6, 6, 8, 9));
    	sheet.addMergedRegion(new CellRangeAddress(6, 6, 10, 11));
    	row = sheet.createRow(6);
    	cell = row.createCell(0);
    	cell.setCellValue("环比");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(1);
    	cell.setCellValue(areNumTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(2);
    	cell.setCellValue(carNumTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(4);
    	cell.setCellValue(dhTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(6);
    	cell.setCellValue(nhTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(8);
    	cell.setCellValue(hhTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(10);
    	cell.setCellValue(dcTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(12);
    	cell.setCellValue(gwdNumTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(13);
    	cell.setCellValue(gwiNumTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(14);
    	cell.setCellValue(bjdNumTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(15);
    	cell.setCellValue(bjiNumTotalRatio);
    	cell.setCellStyle(cellStyle);
    	//第八行
    	sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 3));
    	sheet.addMergedRegion(new CellRangeAddress(7, 9, 12, 15));
    	row = sheet.createRow(7);
    	cell = row.createCell(4);
    	cell.setCellValue("国内");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(5);
    	cell.setCellValue("国际");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(6);
    	cell.setCellValue("国内");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(7);
    	cell.setCellValue("国际");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(8);
    	cell.setCellValue("国内");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(9);
    	cell.setCellValue("国际");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(10);
    	cell.setCellValue("国内");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(11);
    	cell.setCellValue("国际");
    	cell.setCellStyle(cellStyle);
    	//第九行
    	sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 3));
    	row = sheet.createRow(8);
    	cell = row.createCell(0);
    	cell.setCellValue("架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(4);
    	cell.setCellValue(dhdNumTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(5);
    	cell.setCellValue(dhiNumTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(6);
    	cell.setCellValue(nhdNumTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(7);
    	cell.setCellValue(nhiNumTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(8);
    	cell.setCellValue(hhdNumTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(9);
    	cell.setCellValue(hhiNumTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(10);
    	cell.setCellValue(dcdNumTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(11);
    	cell.setCellValue(dciNumTotal);
    	cell.setCellStyle(cellStyle);
    	//第十行
    	sheet.addMergedRegion(new CellRangeAddress(9, 9, 0, 3));
    	row = sheet.createRow(9);
    	cell = row.createCell(0);
    	cell.setCellValue("环比");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(4);
    	cell.setCellValue(dhdNumTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(5);
    	cell.setCellValue(dhiNumTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(6);
    	cell.setCellValue(nhdNumTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(7);
    	cell.setCellValue(nhiNumTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(8);
    	cell.setCellValue(hhdNumTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(9);
    	cell.setCellValue(hhiNumTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(10);
    	cell.setCellValue(dcdNumTotalRatio);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(11);
    	cell.setCellValue(dciNumTotalRatio);
    	cell.setCellStyle(cellStyle);
    	
    	map.put("type", 1);//1.外航货机架次
    	List<HashMap<String, Object>> waihangBegin = flightCountService.getBeginSaAlnOscarDay(map);//开始结束时间查询表SA_ALN_OSCAR_DAY
    	List<HashMap<String, Object>> waihangEnd = flightCountService.getEndSaAlnOscarDay(map);//对比开始结束时间查询表SA_ALN_OSCAR_DAY
    	map.put("type", 2);//2.内行货机架次
    	List<HashMap<String, Object>> neihangBegin = flightCountService.getBeginSaAlnOscarDay(map);//开始结束时间查询表SA_ALN_OSCAR_DAY
    	List<HashMap<String, Object>> neihangEnd = flightCountService.getEndSaAlnOscarDay(map);//对比开始结束时间查询表SA_ALN_OSCAR_DAY
    	map.put("type", 3);//3.窄体机型架次
    	List<HashMap<String, Object>> zaitiBegin = flightCountService.getBeginSaAlnOscarDay(map);//开始结束时间查询表SA_ALN_OSCAR_DAY
    	List<HashMap<String, Object>> zaitiEnd = flightCountService.getEndSaAlnOscarDay(map);//对比开始结束时间查询表SA_ALN_OSCAR_DAY
    	map.put("type", 4);//4.宽体机型架次
    	List<HashMap<String, Object>> kuantiBegin = flightCountService.getBeginSaAlnOscarDay(map);//开始结束时间查询表SA_ALN_OSCAR_DAY
    	List<HashMap<String, Object>> kuantiEnd = flightCountService.getEndSaAlnOscarDay(map);//对比开始结束时间查询表SA_ALN_OSCAR_DAY

    	int one = waihangBegin.size();
    	int two = neihangBegin.size();
    	int three = zaitiBegin.size();
    	int four = kuantiBegin.size();
    	List<Integer> nums = new ArrayList<Integer>();
        nums.add(one);
        nums.add(two);
        nums.add(three);
        nums.add(four);
        int Max = Collections.max(nums);
    	
        HashMap<String, Object> kongbai = new HashMap<String, Object>();//用于对齐表格的填充物
        kongbai.put("ALN_ACT", "-");
        kongbai.put("FLT_NUM", "-");
        kongbai.put("ratio", "-");
        
        int oneTotal = 0;//外航货机架次合计总数
        int vsoneTotal = 0;//对比外航货机架次合计总数
    	for (int i = 0; i < Max; i++) {
    		if(i>=one){
    			waihangBegin.add(kongbai);
    		}
    		String ALN_ACT = waihangBegin.get(i).get("ALN_ACT").toString();
    		for (int j = 0; j < waihangEnd.size(); j++) {
				if(waihangEnd.get(j).get("ALN_ACT").toString().equals(ALN_ACT)){
					vsoneTotal = vsoneTotal + Integer.parseInt(waihangEnd.get(j).get("FLT_NUM").toString());
					String ratio = ComputeRatio(waihangBegin.get(i).get("FLT_NUM"),waihangEnd.get(j).get("FLT_NUM"));
					waihangBegin.get(i).put("ratio", ratio);
				}
			}
    		if(i<one){
    			oneTotal = oneTotal + Integer.parseInt(waihangBegin.get(i).get("FLT_NUM").toString());
    		}
		}
    	int twoTotal = 0;//内航货机架次合计总数
        int vstwoTotal = 0;//对比内航货机架次合计总数
    	for (int i = 0; i < Max; i++) {
    		if(i>=two){
    			neihangBegin.add(kongbai);
    		}
    		String ALN_ACT = neihangBegin.get(i).get("ALN_ACT").toString();
    		for (int j = 0; j < neihangEnd.size(); j++) {
				if(neihangEnd.get(j).get("ALN_ACT").toString().equals(ALN_ACT)){
					vstwoTotal = vstwoTotal + Integer.parseInt(neihangEnd.get(j).get("FLT_NUM").toString());
					String ratio = ComputeRatio(neihangBegin.get(i).get("FLT_NUM"),neihangEnd.get(j).get("FLT_NUM"));
					neihangBegin.get(i).put("ratio", ratio);
				}
			}
    		if(i<two){
    			twoTotal = twoTotal + Integer.parseInt(neihangBegin.get(i).get("FLT_NUM").toString());
    		}
		}
    	int threeTotal = 0;//窄体机型架次合计总数
        int vsthreeTotal = 0;//对比窄体机型架次合计总数
    	for (int i = 0; i < Max; i++) {
    		if(i>=three){
    			zaitiBegin.add(kongbai);
    		}
    		String ALN_ACT = zaitiBegin.get(i).get("ALN_ACT").toString();
    		for (int j = 0; j < zaitiEnd.size(); j++) {
				if(zaitiEnd.get(j).get("ALN_ACT").toString().equals(ALN_ACT)){
					vsthreeTotal = vsthreeTotal + Integer.parseInt(zaitiEnd.get(j).get("FLT_NUM").toString());
					String ratio = ComputeRatio(zaitiBegin.get(i).get("FLT_NUM"),zaitiEnd.get(j).get("FLT_NUM"));
					zaitiBegin.get(i).put("ratio", ratio);
				}
			}
    		if(i<three){
    			threeTotal = threeTotal + Integer.parseInt(zaitiBegin.get(i).get("FLT_NUM").toString());
    		}
		}
    	int fourTotal = 0;//宽体机型架次合计总数
        int vsfourTotal = 0;//对比宽体机型架次合计总数
    	for (int i = 0; i < Max; i++) {
    		if(i>=four){
    			kuantiBegin.add(kongbai);
    		}
    		String ALN_ACT = kuantiBegin.get(i).get("ALN_ACT").toString();
    		for (int j = 0; j < kuantiEnd.size(); j++) {
				if(kuantiEnd.get(j).get("ALN_ACT").toString().equals(ALN_ACT)){
					vsfourTotal = vsfourTotal + Integer.parseInt(kuantiEnd.get(j).get("FLT_NUM").toString());
					String ratio = ComputeRatio(kuantiBegin.get(i).get("FLT_NUM"),kuantiEnd.get(j).get("FLT_NUM"));
					kuantiBegin.get(i).put("ratio", ratio);
				}else{
					
				}
			}
    		if(i<four){
    			fourTotal = fourTotal + Integer.parseInt(kuantiBegin.get(i).get("FLT_NUM").toString());
    		}
		}
    	//第十一行
    	sheet.addMergedRegion(new CellRangeAddress(10, 10, 0, 2));
    	sheet.addMergedRegion(new CellRangeAddress(10, 10, 4, 6));
    	sheet.addMergedRegion(new CellRangeAddress(10, 10, 8, 10));
    	sheet.addMergedRegion(new CellRangeAddress(10, 10, 12, 14));
    	row = sheet.createRow(10);
    	cell = row.createCell(0);
    	cell.setCellValue("外航货机架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(3);
    	cell.setCellValue("环比");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(4);
    	cell.setCellValue("内航货机架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(7);
    	cell.setCellValue("环比");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(8);
    	cell.setCellValue("窄体机型架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(11);
    	cell.setCellValue("环比");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(12);
    	cell.setCellValue("宽体机型架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(15);
    	cell.setCellValue("环比");
    	cell.setCellStyle(cellStyle);

    	for (int i = 0; i < Max; i++) {
    		sheet.addMergedRegion(new CellRangeAddress(i+11, i+11, 0, 1));
        	sheet.addMergedRegion(new CellRangeAddress(i+11, i+11, 4, 5));
        	sheet.addMergedRegion(new CellRangeAddress(i+11, i+11, 8, 9));
        	sheet.addMergedRegion(new CellRangeAddress(i+11, i+11, 12, 13));
        	row = sheet.createRow(i+11);
        	cell = row.createCell(0);
        	cell.setCellValue(waihangBegin.get(i).get("ALN_ACT").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(2);
        	cell.setCellValue(waihangBegin.get(i).get("FLT_NUM").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(3);
        	if(waihangBegin.get(i).get("ratio") != null){
        		cell.setCellValue(waihangBegin.get(i).get("ratio").toString());
        	}
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(4);
        	cell.setCellValue(neihangBegin.get(i).get("ALN_ACT").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(6);
        	cell.setCellValue(neihangBegin.get(i).get("FLT_NUM").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(7);
        	if(neihangBegin.get(i).get("ratio") != null){
        		cell.setCellValue(neihangBegin.get(i).get("ratio").toString());
        	}
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(8);
        	cell.setCellValue(zaitiBegin.get(i).get("ALN_ACT").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(10);
        	cell.setCellValue(zaitiBegin.get(i).get("FLT_NUM").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(11);
        	if(zaitiBegin.get(i).get("ratio") != null){
        		cell.setCellValue(zaitiBegin.get(i).get("ratio").toString());
        	}
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(12);
        	cell.setCellValue(kuantiBegin.get(i).get("ALN_ACT").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(14);
        	cell.setCellValue(kuantiBegin.get(i).get("FLT_NUM").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(15);
        	if(kuantiBegin.get(i).get("ratio") != null){
        		cell.setCellValue(kuantiBegin.get(i).get("ratio").toString());
        	}
        	cell.setCellStyle(cellStyle);
		}
    	
    	//倒数第二行
    	sheet.addMergedRegion(new CellRangeAddress(Max + 11, Max + 11, 0, 1));
    	sheet.addMergedRegion(new CellRangeAddress(Max + 11, Max + 11, 4, 5));
    	sheet.addMergedRegion(new CellRangeAddress(Max + 11, Max + 11, 8, 9));
    	sheet.addMergedRegion(new CellRangeAddress(Max + 11, Max + 11, 12, 13));
    	row = sheet.createRow(Max + 11);
    	cell = row.createCell(0);
    	cell.setCellValue("合计");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(2);
    	cell.setCellValue(oneTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(3);
		cell.setCellValue(ComputeRatio(oneTotal,vsoneTotal));
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(4);
    	cell.setCellValue("合计");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(6);
    	cell.setCellValue(twoTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(7);
		cell.setCellValue(ComputeRatio(twoTotal,vstwoTotal));
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(8);
    	cell.setCellValue("合计");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(10);
    	cell.setCellValue(threeTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(11);
		cell.setCellValue(ComputeRatio(threeTotal,vsthreeTotal));
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(12);
    	cell.setCellValue("合计");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(14);
    	cell.setCellValue(fourTotal);
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(15);
		cell.setCellValue(ComputeRatio(fourTotal,vsfourTotal));
    	cell.setCellStyle(cellStyle);
    	//倒数第一行
    	sheet.addMergedRegion(new CellRangeAddress(Max + 12, Max + 12, 0, 5));
    	sheet.addMergedRegion(new CellRangeAddress(Max + 12, Max + 12, 6, 7));
    	sheet.addMergedRegion(new CellRangeAddress(Max + 12, Max + 12, 8, 13));
    	sheet.addMergedRegion(new CellRangeAddress(Max + 12, Max + 12, 14, 15));
    	row = sheet.createRow(Max + 12);
    	cell = row.createCell(0);
    	cell.setCellValue("进港架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(6);
    	cell.setCellValue(first.get("IN_FLT_NUM").toString());
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(8);
		cell.setCellValue("出港架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(14);
    	cell.setCellValue(first.get("OUT_FLT_NUM").toString());
    	cell.setCellStyle(cellStyle);
    	
    	
		String fileName = "航班架次统计" +beginTimeDisplay + "-" + endTimeDisplay + ".xlsx";
		response.reset();
		response.setContentType("application/octet-stream; charset=utf-8");
		String agent = (String) request.getHeader("USER-AGENT");
		String downloadFileName = "";
		if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
		} else {
			downloadFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
		}
		response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
    	
    	wb.write(response.getOutputStream());
    }
}

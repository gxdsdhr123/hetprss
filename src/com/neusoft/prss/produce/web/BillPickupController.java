package com.neusoft.prss.produce.web;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.excel.ExportExcel;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.common.bean.PageBean;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.produce.entity.BillPickupEntity;
import com.neusoft.prss.produce.entity.BillPickupGoodsEntity;
import com.neusoft.prss.produce.entity.DelayInfo;
import com.neusoft.prss.produce.service.BillPickupService;

@Controller
@RequestMapping(value = "${adminPath}/produce/pickup")
public class BillPickupController extends BaseController {

	@Autowired
    private BillPickupService billService;

    @Autowired
    private FileService fileService;
    
	/**
     * 
     *Discription:捡拾物品单跳转.
     *@return
     *@return:返回值意义
     *@author:Heqg
     *@update:2017年12月23日 Heqg [变更描述]
     */
    @RequestMapping(value = {"pickupList"})
    public String pickupList() {
        return "prss/produce/pickupList";
    }

    /**
     * 
     *Discription:获取捡拾物品数据.
     *@return
     *@return:返回值意义
     *@author:Heqg
     *@update:2017年12月23日 Heqg [变更描述]
     */
    @ResponseBody
    @RequestMapping(value = "getPickupListData")
    public PageBean<BillPickupEntity> getPickupListData(String startDate, String endDate,
			String searchText, Integer pageNumber, Integer pageSize) {
    	searchText = StringEscapeUtils.unescapeJava(searchText.replaceAll("%", "\\\\"));
    	PageBean<BillPickupEntity> bean = new PageBean<BillPickupEntity>();
    	bean.setTotal(billService.getPickupListCount(startDate, endDate, searchText));
    	bean.setRows(billService.getPickupListData(startDate, endDate, searchText, pageNumber, pageSize));
        return bean;
    }

    @RequestMapping(value = {"newPickup"})
    public String newPickup(Model model) {
        /*JSONObject json = new JSONObject();
        String hdid = billService.getPickupId();
        json.put("id", hdid);
        model.addAttribute("pickupJson", json);*/
    	model.addAttribute("userList", billService.getUserList());
        return "prss/produce/pickup";
    }

    @RequestMapping(value = {"editPickup"})
    public String editPickup(Model model,String id) {
    	model.addAttribute("userList", billService.getUserList());
        model.addAttribute("pickupJson", billService.getPickupForm(id));
        return "prss/produce/pickup";
    }

    @ResponseBody
    @RequestMapping(value = "getPickupGoodstData")
    public List<BillPickupGoodsEntity> getPickupGoodstData(String id) {
        return billService.getPickupGoods(id);
    }

    @ResponseBody
    @RequestMapping(value = "savePickup")
    public String savePickup(String pickup,String goods) {
        JSONObject pickupJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(pickup));
        JSONArray goodsJSON = JSONArray.parseArray(StringEscapeUtils.unescapeHtml4(goods));
        billService.savePickup(pickupJSON, goodsJSON);
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "delPickup")
    public String delPickup(String ids) {
        billService.delPickup(ids);
        return "success";
    }
    
    @RequestMapping(value = "downAtta")
    public String downAtta(Model model,String id, HttpServletRequest request,HttpServletResponse response) {
    	List<String> fileIds = new ArrayList<String>();
    	fileIds.add(id);
        model.addAttribute("type", "1");
        model.addAttribute("fileIds", fileIds);
        return "prss/flightdynamic/exceptionalAtta";
    }
	
    @RequestMapping(value = "downPic")
    public void downPic(String id, HttpServletRequest request,HttpServletResponse response) throws SocketException, IOException {
    	BufferedInputStream is = null;
    	OutputStream os = null;
    	byte[] data = fileService.doDownLoadFile(id);
        byte[] content = new byte[1024];
        is = new BufferedInputStream(new ByteArrayInputStream(data));
        os = response.getOutputStream();
        while (is.read(content) != -1) {
            os.write(content);
        }
        
    	response.setContentType("application/octet-stream; charset=utf-8");
    	response.setHeader("Content-Disposition","attachment;filename="+id+".jpg");
    	os.flush();
    	os.close();
    }
    
    
    /**
	 * 打印
	 * @param request
	 * @param response
	 * @param startDate
	 * @param endDate
	 * @param airline
	 * @throws URISyntaxException
	 */
    @RequestMapping(value = "printPickup")
    public void printData(HttpServletRequest request,HttpServletResponse response,
    		String startDate, String endDate, String ids) throws Exception {
    	try {
    		// 获取模板路径
    		String wiPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI()).getParentFile().getPath();
    		String tempPath = wiPath + "/template/tmp_bill_pickup.xlsx";
    		File tmpFile = new File(tempPath);
    		// 获取数据列表
    		List<BillPickupEntity> datalist = billService.getPickupAllList(startDate, endDate, ids);
    		// 需要复制区域行数
    		int copyrows = 6;
			ExportExcel excel = new ExportExcel(tmpFile, 1, 1);
			// 复制开始行号
			int startRow = 1;
			// 复制结束行号
			int endRow = startRow + copyrows - 1;
			// 记录当前复制行索引
			int currRow = startRow  - 1;
			for(BillPickupEntity entity : datalist){
				if(entity.getGoods() !=null && entity.getGoods().size() != 0){
					for(BillPickupGoodsEntity good : entity.getGoods()){
						if(currRow != startRow  - 1){
							// 复制行
							excel.copyRows(startRow, endRow , currRow);
						}
						setValToExcel(excel.getRows(currRow+1,  currRow + copyrows),entity,good);
						currRow += copyrows;
					}
				}else{
					if(currRow != startRow  - 1){
						// 复制行
						excel.copyRows(startRow, endRow , currRow);
					}
					setValToExcel(excel.getRows(currRow+1,  currRow + copyrows),entity,null);
					currRow += copyrows;
				}
			}
			// 遍历结果
			/*for(int i = 0; i < datalist.size(); i++){
				// 计算行开始/结束
				int sRow = startRow + i*copyrows;
				int eRow = sRow + copyrows - 1;
				// 取对应表格中的所有行
				List<Row> rows = excel.getRows(sRow, eRow);
				BillPickupEntity entity = datalist.get(i);
				
				// 向表格中填值
//				setValToExcel(rows,delay,i+1);
			}*/
			
			String fileName = "失物招领单_" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			excel.write(response, fileName).dispose();
		} catch (Exception e) {
			logger.error("导出失物招领单。",e);
			throw e;
		}
    }
    
    /**
     * 向EXCEL表格中添值
     * @param rows
     * @param delay
     * @param index 
     */
    private void setValToExcel(List<Row> rows, BillPickupEntity entity, BillPickupGoodsEntity good){
    	// 兹收到姓名
    	rows.get(1).getCell(1).setCellValue(entity.getOperatorName());
    	// 捡拾人签字
    	rows.get(1).getCell(5).setCellValue(entity.getPuUserName());
    	// 捡拾人所在单位
    	rows.get(2).getCell(1).setCellValue("地服公司");
    	// 捡拾人电话
    	rows.get(2).getCell(3).setCellValue(entity.getPuPhone());
    	if(good != null){
    		// 捡拾地点
    		rows.get(1).getCell(3).setCellValue(good.getPos());
    		// 货物名称
    		rows.get(4).getCell(1).setCellValue(good.getName());
    		// 货物特征
    		rows.get(5).getCell(1).setCellValue(good.getFeature());
    	}else{
    		// 捡拾地点
    		rows.get(1).getCell(3).setCellValue("");
    		// 货物名称
    		rows.get(4).getCell(1).setCellValue("");
    		// 货物特征
    		rows.get(5).getCell(1).setCellValue("");
    	}
    	
    }
}

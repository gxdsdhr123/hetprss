package com.neusoft.prss.OSCARSecurityRep.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.OSCARSecurityRep.service.OSCARSecurityRepService;
import com.neusoft.prss.OSCARSecurityRep.util.PrintWordUtil;
import com.neusoft.prss.file.service.FileService;
/**
 * OSCAR保障管理报告
 * @author lwg
 * @date 2018/1/8
 */
@Controller
@RequestMapping("${adminPath}/oscarrSecurity/report")
public class OSCARSecurityRepController extends BaseController {
	
	@Autowired
	private OSCARSecurityRepService oSCARSecurityRepService;
	
	@Autowired
	private FileService fileService;
	
	/**
	 * 跳转港邮货交接单
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		return "prss/oscarSecurity/oscarSecurityreport";
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
     * 查询数据处理
     * @param list
     * @return
     */
    private List<Map<String, Object>> dealCarList(List<Map<String, Object>> list) {
   	 List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
   	 Map<String, Object> resultMap = new HashMap<String, Object>();
   	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Map<String, Object> map = (Map<String, Object>) iterator.next();
			resultMap = new HashMap<String, Object>();
			for (String key : map.keySet()) {
				resultMap.put(key.toLowerCase(), map.get(key));
		    }
			resultList.add(resultMap);
		}
		return resultList;
   }
    
    /**
     * 
     *Discription:得到表格数据
     *@param pageSize
     *@param pageNumber
     *@return:返回值意义
     */
    @RequestMapping(value = "getGridData")
    @ResponseBody
    public String getGridData(String pageNumber, String pageSize, String startDate, String endDate, String searchData) {
    	if(null == pageNumber) {
    		pageNumber = "1";
    	}
    	if(null == pageSize) {
    		pageSize = "10";
    	}
    	int begin = (Integer.valueOf(pageNumber) - 1) * Integer.valueOf(pageSize) + 1;
        int end = Integer.valueOf(pageSize) + begin - 1;
        
    	Map<String, Object> inData = oSCARSecurityRepService.getData(begin, end, startDate, endDate, searchData);
    	return JSON.toJSONString(inData, SerializerFeature.WriteMapNullValue);

    }

    /**
     * 跳转总控
     * @param searchTime
     * @param param
     * @param sign
     * @param searchId
     * @param pdate
     * @param model
     * @return
     */
    @RequestMapping(value = "pageJump/{searchId}/{sign}")
    public String pageJump(@PathVariable("searchId") String searchId, @PathVariable("sign") String sign, Model model) {
    	if(null == searchId || "".equals(searchId.trim())) {
    		return "prss/oscarSecurity/showOSCARSecurityreport";
    	}
    	if("flt".equals(sign)) {
    		// 获取航班信息
    		List<Map<String,Object>> list = oSCARSecurityRepService.getData(searchId);
    		dealOSCARSecurityData(model, list);
    		return "prss/oscarSecurity/showOSCARSecurityreport";
    	} else {
    		String reSign = sign;
    		if("9".equals(sign)) {
    			reSign = null;
    		}
    		List<Map<String, Object>> showList = oSCARSecurityRepService.getRemarkShow(searchId, reSign, true);
    		model.addAttribute("sign", sign);
    		model.addAttribute("showList", showList);
    		
    		return "prss/oscarSecurity/oscarPicOrAudio";
    	}

    	
    	
    }
	/**
     * 处理页面加载信息
     * @param model
     * @param list
     */
    private void dealOSCARSecurityData(Model model, List<Map<String, Object>> list) {
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
  	 * 打印world
  	 * @param searchId
  	 * @param id
  	 * @throws UnsupportedEncodingException 
  	 */
  	@RequestMapping(value = { "print" })
  	public void print(HttpServletRequest request,HttpServletResponse response, String searchId, String param, String showId) {
  		List<Map<String,Object>> list = oSCARSecurityRepService.getData(searchId);
		String fileName = "OSCAR保障报告_" + list.get(0).get("INFLIGHTNUMBER") + "_" + list.get(0).get("OUTFLIGHTNUMBER") + "_" + list.get(0).get("FLIGHTDATE");
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
  		if("print".equals(param)) { // 打印
  			
  			XWPFDocument wordData = dealWorldData(list);
  			// 取得文件名。
  			fileName += ".docx";
  			// 输出word内容文件流，提供下载
  			response.reset();
  			
  			try {
  				fileName = URLEncoder.encode(fileName, "utf-8");
  				response.setContentType("application/x-download");
  				response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
  				OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
  				wordData.write(ostream);
  				toClient.write(ostream.toByteArray());
  				toClient.flush();
  				toClient.close();
  			} catch (Exception e) {
  				 logger.error("OSCAR保障报告打印失败" + e.getMessage());
  			} 
  		} else if("download".equals(param)) { // 下载
  			List<Map<String, Object>> showList = oSCARSecurityRepService.getRemarkShow(showId, null, false);

  			fileName += ".zip";
  			String fileType = "";
  			String dFileName = "附件";
  			int index = 0;
  			try {
				fileName = URLEncoder.encode(fileName, "UTF-8");				
				ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
				File[] files = new File[showList.size()];
				BufferedOutputStream stream = null;
				
				for (int i = 0; i < showList.size(); i++) {
					Map<String, Object> fileId = showList.get(i);
					byte[] is = null;
					if(null != fileId || !"".equals(fileId)) {
						fileType = oSCARSecurityRepService.getDownloadFileType(fileId.get("FILEPATH")+"");
						is = fileService.doDownLoadFile(fileId.get("FILEPATH")+"");
						if(null != is) { // 文件不存在							
							if(index != 0) {
								files[i] = new File(dFileName + "_" + index + "." + fileType);							
							} else {
								files[i] = new File(dFileName + "." + fileType);		
							}
							index++;
							FileOutputStream fstream = new FileOutputStream(files[i]);
							stream = new BufferedOutputStream(fstream);
							stream.write(is);
							stream.close();
						}
					} else {
						continue;
					}
	
				}
				response.reset();// 重设response信息，解决部分浏览器文件名非中文问题
				response.setContentType("application/octet-stream");// 指明response的返回对象是文件流 
				response.setHeader("Content-Disposition", "attachment;filename=" + fileName);// 设置在下载框默认显示的文件名

				int fileNum = 0;
				for(File f : files) {
					if(null != f) {		
						fileNum++;
					} 
				}
				File[] file = new File[fileNum];
				int j = 0;
				for(int i = 0; i < files.length; i++) {
					if(null != files[i]) {
						file[j++] = files[i];
					}
				}
				if(file.length != 0) {					
					zipFile(file, fileName, zos);
				}
				zos.flush();
				zos.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
  		}
  		
  		
  	}
  	
	private void zipFile(File[] subs, String baseName, ZipOutputStream zos) throws IOException {
		for (int i = 0; i < subs.length; i++) {
			File f = subs[i];
			zos.putNextEntry(new ZipEntry(f.getName()));
			FileInputStream fis = new FileInputStream(f);
			byte[] buffer = new byte[1024];
			int r = 0;
			while ((r = fis.read(buffer)) != -1) {
				zos.write(buffer, 0, r);
			}
			fis.close();
		}
	}
  	/**
  	 * 处理world数据
  	 * @param list
  	 * @return
  	 */
  	@SuppressWarnings("unchecked")
	private XWPFDocument dealWorldData(List<Map<String,Object>> list) {
  		PrintWordUtil worldUtil = new PrintWordUtil();
  		
  		List<Map<String, Object>> headerList = new ArrayList<>();
  		List<List<Map<String, Object>>> headerListArray = new ArrayList<>();
  		List<Map<String, Object>> itemHeadList = new ArrayList<>();
  		List<Map<String, Object>> remarkHeadList = new ArrayList<>();
  		Map<String, Object> dataMap = new HashMap<String, Object>();
  		List<Map<String, Object>> itemList = new ArrayList<>();
  		List<List<Map<String, Object>>> itemListArray = new ArrayList<>();
  		List<Map<String, Object>> remarkList = new ArrayList<>();
  		List<List<Map<String, Object>>> remarkListArray = new ArrayList<>();
  		String header = "OSCAR保障报告";
  		String[] headerTitle = new String[]{"日期", "进港航班号", "出港航班号","机位","机号","机型","入位","离位","客舱关门","机组允许登机","进港OSCAR","出港OSCAR"};
  		String[] headerCode = new String[]{"FLIGHTDATE", "INFLIGHTNUMBER", "OUTFLIGHTNUMBER","ACTSTANDCODE","AIRCRAFTNUMBER","ACTTYPECODE","STANDTM","RELSSTANDTM","HTCHCLOTM","ZKTOSCARBZOUTARRIVE1FTM","INOSCARNAME","OUTOSCARNAME"};
  		String[] itemHead = new String[]{"序号", "检查项目", "是否符合", "操作时间"};
  		String[] remarkHead = new String[]{"备注"};
  		String[] remarkNullList = new String[]{"NO", "REMARKTEXT", "CREATEDATE"};
  		// 表格头部数据
  		for(int i = 0; i < headerTitle.length; i++) {
  			dataMap = new HashMap<String, Object>();
  			dataMap.put("name", headerTitle[i]);
  			dataMap.put("value", headerCode[i]); 
  			headerList.add(dataMap);
  			if(i != 0 && (i+1) % 3 == 0) {
  				headerListArray.add(headerList);
  				headerList = new ArrayList<>();
  			}
  		}
  		// 检查项目表格头部
  		for(int i = 0; i < itemHead.length; i++) {
  			dataMap = new HashMap<String, Object>();
  			dataMap.put("name", itemHead[i]);
  			itemHeadList.add(dataMap);
  		}
  		// 检查项目数据解析
  		List<Map<String, Object>> itemListMap = (List<Map<String, Object>>) list.get(0).get("itemList");
  		// 空数据空行处理
  		for(int i = 0; i < itemListMap.size(); i++) {
  			if("0".equals(itemListMap.get(i).get("ITEMNAME")+"")) {
  				itemListMap.get(i).put("ITEMNAME", "");
  				itemListMap.get(i).put("CREATEDATE", "");
  				itemListMap.get(i).put("ITEMVAL", "");
  			}
  		}
  		for (Map<String, Object> map : itemListMap) {
  			itemList = new ArrayList<>();
			for(String key : map.keySet()) {
				dataMap = new HashMap<String, Object>();
				if("NO".equals(key)) {					
					dataMap.put("sort", 0);
				} else if("ITEMNAME".equals(key)) {
					dataMap.put("sort", 1);
				} else if("ITEMVAL".equals(key)) {
					dataMap.put("sort", 2);					
				} else if("CREATEDATE".equals(key)) {
					dataMap.put("sort", 3);										
				}
				dataMap.put("name", key);
				dataMap.put(key, map.get(key));	
				
				itemList.add(dataMap);
			}
			itemListArray.add(itemList);
		}
  		// 备注表格头部
  		for(int i = 0; i < remarkHead.length; i++) {
  			dataMap = new HashMap<String, Object>();
  			dataMap.put("name", remarkHead[i]);
  			remarkHeadList.add(dataMap);
  		}
  		// 备注数据解析
  		List<Map<String, Object>> remarkListMap = (List<Map<String, Object>>) list.get(0).get("remarkList");
  		// 空数据处理 
  		if(remarkListMap.size() != 0) {  			
  			for(int i = 0; i < remarkListMap.size(); i++) {
  				if(null == remarkListMap.get(i).get("REMARKTEXT")) {
  					remarkListMap.get(i).put("REMARKTEXT", "");
  					remarkListMap.get(i).put("CREATEDATE", "");
  				}
  			}
  			for (Map<String, Object> map : remarkListMap) {
  				remarkList = new ArrayList<>();
  				for(String key : map.keySet()) {
  					dataMap = new HashMap<String, Object>();
  					if("FILETYPE".equals(key) || "REMARKID".equals(key)) {
  						continue;
  					}
  					if("NO".equals(key)) {					
  						dataMap.put("sort", 0);
  					} else if("REMARKTEXT".equals(key)) {
  						dataMap.put("sort", 1);
  					} else if("CREATEDATE".equals(key)) {
  						dataMap.put("sort", 2);										
  					}
  					
  					dataMap.put("name", key);
  					dataMap.put(key, map.get(key));										
  					remarkList.add(dataMap);
  				}
  				remarkListArray.add(remarkList);
  			}
  		} else {
  			for(int i = 0;  i < remarkNullList.length; i++) {
  				dataMap = new HashMap<>();
				if("NO".equals(remarkNullList[i])) {					
					dataMap.put("sort", 0);
				} else if("REMARKTEXT".equals(remarkNullList[i])) {
					dataMap.put("sort", 1);
				} else if("CREATEDATE".equals(remarkNullList[i])) {
					dataMap.put("sort", 2);										
				}
				
				dataMap.put("name", remarkNullList[i]);
				if("NO".equals(remarkNullList[i])) {
					dataMap.put(remarkNullList[i], 1);																
				} else {						
					dataMap.put(remarkNullList[i], "");										
				}
				remarkList.add(dataMap);
  			}
  			remarkListArray.add(remarkList);
  		}
  		list.get(0).put("header", header);
  		list.get(0).put("headList", headerListArray);
  		list.get(0).put("itemHeadList", itemHeadList);
  		list.get(0).put("itemList", itemListArray);
  		list.get(0).put("remarkHeadList", remarkHeadList);
  		list.get(0).put("remarkList", remarkListArray);
  		
  		try {
  			XWPFDocument document = worldUtil.createSimpleTable(list);
			return document;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
  	}
}

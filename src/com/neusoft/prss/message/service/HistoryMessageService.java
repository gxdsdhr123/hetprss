package com.neusoft.prss.message.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.neusoft.prss.message.dao.HistoryMessageDao;
import com.neusoft.prss.message.entity.HistoryMessageVO;

@Service
public class HistoryMessageService {

    @Resource
    private HistoryMessageDao historymessageDao;

    @Resource
    private HistoryService mmHisService;

    public Map<String,Object> getListInfo(Map<String,Object> param) {
        //bootstrap-table要求服务器返回的json须包含：totlal，rows
        Map<String,Object> result = new HashMap<String,Object>();
        int total = historymessageDao.getCount(param);
        List<Map<String,String>> rows = historymessageDao.getList(param);
        result.put("total", total);
        result.put("rows", rows);
        return result;
    }

    public HistoryMessageVO searchHisDetail(Map<String,String> map) {
        return historymessageDao.searchHisDetail(map);
    }

    public List<HistoryMessageVO> reciverDetail(Map<String,String> map) {
        return historymessageDao.reciverDetail(map);
    }

    public List<Map<String,String>> gethistoryListPrint(Map<String,Object> param) {
        return historymessageDao.getList(param);

    }

    public JSONArray getFileIds(Map<String,String> param) {
        return historymessageDao.getFileIds(param);
    }

    public JSONArray createFlow(Map<String,String> param) {
        JSONArray data = new JSONArray();
        int flag = 1;
        this.getMid(param);
        this.createNode(param, flag, data);
        JSONArray option = this.createOption(data);
        return option;
    }

    private String getMid(Map<String,String> param) {
        String isHis = param.get("isHis");
        String mid = "";
        if ("2".equals(isHis) || "64".equals(isHis)) {
            mid = mmHisService.queryParentTrans(param);
        } else {
            mid = historymessageDao.queryParentTrans(param);
        }
        if (mid != null && !"".equals(mid)) {
            param.put("MID", mid);
            mid = this.getMid(param);
        } else {
            mid = param.get("MID");
        }
        return mid;
    }

    public JSONArray createOption(JSONArray data) {
        JSONArray option = new JSONArray();
        HashMap<String,String> nameMap = new HashMap<String,String>();
        JSONArray flags = (JSONArray) JSONPath.eval(data, "$.flag");
        int flag = 0;
        for (int i = 0; i < flags.size(); i++) {
            int f = flags.getInteger(i);
            if (f > flag)
                flag = f;
        }
        int count = 0;
        Map<String,String> list = new HashMap<String,String>();

        for (int i = 1; i <= flag; i++) {
            JSONArray flag_nodes = (JSONArray) JSONPath.eval(data, "$[flag=" + i + "]");
            //                if(count==0)count = flag_nodes.size();
            //                if(flag_nodes.size()>count) count += (flag_nodes.size()-count);
            count += flag_nodes.size();
        }
        for (int i = 1; i <= flags.size(); i++) {
            JSONArray flag_nodes = (JSONArray) JSONPath.eval(data, "$[flag=" + i + "]");
            List<String> mids = (List<String>) JSONPath.eval(flag_nodes, "$.mid");
            for (String str : mids) {
                list.put(str, str);
            }
            JSONArray nodes = new JSONArray();
            if (list.size() > 0) {
                Set<String> keys = list.keySet();
                Iterator<String> it = keys.iterator();
                while (it.hasNext()) {
                    String mid = it.next();
                    nodes = (JSONArray) JSONPath.eval(flag_nodes, "$[mid='" + mid + "']");
                    list = new HashMap<String,String>();
                    if (nodes.size() > 0)
                        createOptionNode(list, nodes, i, data, flag, option, count, nameMap);
                }
            } else {
                nodes = flag_nodes;
                list = new HashMap<String,String>();
                if (nodes.size() > 0)
                    createOptionNode(list, nodes, i, data, flag, option, count, nameMap);
            }
        }
        return option;

    }

    private void ckeckNodes(JSONArray nodes) {
        int begin = 1, end = nodes.size();
        for (int i = 0; i < nodes.size(); i++) {
            JSONObject node = nodes.getJSONObject(i);
            int num = node.getInteger("num");
            if (num <= 0) {
                node.put("order", begin++);
            } else {
                node.put("order", end--);
            }
        }
    }

    public void createOptionNode(Map<String,String> list,JSONArray nodes,int i,JSONArray data,int flag,JSONArray option,
            int count,HashMap<String,String> nameMap) {
        this.ckeckNodes(nodes);
        double y = Math.ceil(count / 2.0) * 400;
        String receiv = "接收时间:";
        String send = "发送时间:";
        String br = "\n";
        JSONArray flag_nodes = (JSONArray) JSONPath.eval(data, "$[flag=" + (i + 1) + "]");
        double nodes_size = flag_nodes.size();
        //            double y1 = Math.ceil(count/2.0) * 600;
        double y1 = 0.0;
        if (nodes_size == 0) {
            y1 = Math.ceil(count) * 600;
        } else {
            y1 = Math.ceil(count) * 600;
        }
        double x = Math.ceil(y1 * 1.0 / count) * (flag + 1 - i);
        double positionX = Math.ceil(y / count);
        int common = 0;
        int position = 1;
        double _xxup = 0;
        double _xxdown = 0;
        for (int j = 0; j < nodes.size(); j++) {
            //                JSONObject node = nodes.getJSONObject(j);
            JSONArray nodeArr = (JSONArray) JSONPath.eval(nodes, "$[order=" + (nodes.size() - j) + "]");
            JSONObject node = nodeArr.getJSONObject(0);
            JSONObject opt = new JSONObject();
            int nodeFlag = node.getInteger("flag");
            String sendtime = node.getString("sendtime");
            String mid = node.getString("mid");
            String child_mid = node.getString("child_mid");
            String receivtime = node.getString("receivtime");
            String targetReceivtime = "";
            String targetSendtime = "";
            String name = node.getString("name") + br;
            int w = 0;
            while (w == 0) {
                if (nameMap != null && nameMap.containsKey(name)) {
                    name += " ";
                } else {
                    w = 1;
                }
            }
            nameMap.put(name, name);
            String target = node.getString("parent") + br;
            int num = 0;
            if (node.containsKey("num"))
                num = node.getInteger("num");
            if (child_mid != null)
                list.put(child_mid, child_mid);

            JSONArray parents = (JSONArray) JSONPath.eval(data, "$[flag=" + (i - 1) + "]");
            for (int m = 0; m < parents.size(); m++) {
                JSONObject parent = parents.getJSONObject(m);
                String trans_mid_2 = "";
                if (i > 2) {
                    trans_mid_2 = parent.getString("trans_mid");
                } else {
                    trans_mid_2 = parent.getString("mid");
                }
                if (mid.equals(trans_mid_2)) {
                    target = parent.getString("name") + br;
                    targetSendtime = parent.getString("sendtime");
                    targetReceivtime = parent.getString("receivtime");
                    y = parent.getDoubleValue("y");
                    break;
                }
            }
            if (i > 1) {
                if (receivtime != null) {
                    if (num > 0) {
                        if (sendtime != null) {
                            name += br + send + sendtime;
                        }
                    }
                    if (receivtime != null) {
                        name += br + receiv + receivtime;
                    }
                } else {
                    name += br + receiv;
                }
                opt.put("name", name);
                if (i > 2) {
                    opt.put("target", target + br + send + targetSendtime + br + receiv + targetReceivtime);
                } else {
                    opt.put("target", target + br + send + targetSendtime);
                }

            } else {
                opt.put("name", name + br + send + sendtime);
            }
            String trans_mid = node.getString("trans_mid");
            List<String> mids = (List<String>) JSONPath.eval(flag_nodes, "$.mid");
            for (String str : mids) {
                list.put(str, str);
            }
            JSONArray midNodes = (JSONArray) JSONPath.eval(flag_nodes, "$[mid='" + trans_mid + "']");
            int childrenSize = midNodes.size();
            double _y = 0.0;
            if (i == 1) {
                _y = y;
            } else {
                //                    double _xx = x / (nodeFlag);
                double _xx = x / (nodeFlag);
                if (nodes.size() % 2 == 0) {
                    int ceil = (int) Math.ceil(position / 2.0);
                    switch (position % 2) {
                        case 0:
                            _xxup += (_xx / 2.0) - (_xx / 4.0) + common;
                            _y = y + _xxup;
                            break;
                        case 1:
                            _xxdown += (_xx / 2.0) + (_xx / 4.0) - common;
                            _y = y - _xxdown;
                            break;
                    }
                    position++;
                } else {
                    if (j == 0) {
                        _y = y;
                    } else {
                        int ceil = (int) Math.ceil(position / 2.0);
                        switch (position % 2) {
                            case 0:
                                _xxup += (_xx / 2.0) + common;
                                _y = y + _xxup;
                                break;
                            case 1:
                                _xxdown += (_xx / 2.0) - common;
                                _y = y - _xxdown;
                                break;
                        }
                        position++;
                    }
                }
            }
            double _x = positionX * i;
            node.put("x", _x);
            node.put("y", _y);
            opt.put("x", _x);
            opt.put("y", _y);
            opt.put("value", i);
            opt.put("flag", flag);
            option.add(opt);
        }
    }

    private JSONObject createNode(Map<String,String> map,int flag,JSONArray data) {
        String isHis = map.get("isHis");
        JSONArray list = null;
        if ("2".equals(isHis) || "64".equals(isHis)) {
            list = mmHisService.queryFlow(map);
        } else {
            list = historymessageDao.queryFlow(map);
        }
        JSONObject parent = new JSONObject();
        parent.put("flag", flag);
        if (list.size() > 0)
            flag++;
        for (int i = 0; i < list.size(); i++) {
            JSONObject l = list.getJSONObject(i);
            String sendercn = l.getString("SENDERCN");//发送人名称
            String sendTime = l.getString("SENDTIME");//发送时间
            String receiveTime = l.getString("RECEIVTIME");//接收时间
            String name = l.getString("NAME");//接收人名称
            String mid = l.getString("MID");//消息ID
            String mtoid = l.getString("MTOID");//接收人ID
            int num = list.size();//接收人个数

            parent.put("name", sendercn);
            parent.put("sendtime", sendTime);
            parent.put("mid", mid);
            parent.put("num", num);

            JSONObject child = new JSONObject();
            child.put("name", name);
            child.put("mtoid", mtoid);
            child.put("mid", mid);
            child.put("receivtime", receiveTime);
            if (flag > 2) {
                child.put("sendtime", sendTime);
            }
            map.put("MID", mid);
            map.put("MTOID", mtoid);
            JSONObject trans = null;
            if ("2".equals(isHis) || "64".equals(isHis)) {
                trans = mmHisService.queryFlowTrans(map);
            } else {
                trans = historymessageDao.queryFlowTrans(map);
            }
            if (trans != null) {
                if (trans.containsKey("NUM")) {
                    if ("2".equals(isHis) || "64".equals(isHis)) {
                        child.put("num", mmHisService.queryFlowTransNum(map));
                    } else {
                        child.put("num", historymessageDao.queryFlowTransNum(map));
                    }
                    String trans_mid = trans.getString("ID");
                    String transsubid = trans.getString("TRANSSUBID");
                    String childSendtime = trans.getString("SENDTIME");
                    child.put("sendtime", childSendtime);
                    if (transsubid.equals(mtoid)) {
                        map.put("MID", trans_mid);
                        child.put("trans_mid", trans_mid);
                        this.createNode(map, flag, data);
                    }
                }
            } else {
                child.put("num", 0);
            }
            child.put("parent", sendercn);
            child.put("flag", flag);
            data.add(child);
        }
        if (flag <= 2) {
            parent.put("num", 1);
            data.add(parent);
        }
        return parent;
    }
}

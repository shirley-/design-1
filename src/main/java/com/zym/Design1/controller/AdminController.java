package com.zym.Design1.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.zym.Design1.bean.UserLevelRule;
import com.zym.Design1.bean.vo.FinVO;
import com.zym.Design1.entity.*;
import com.zym.Design1.bean.HostHolder;
import com.zym.Design1.service.*;
import com.zym.Design1.util.ConstantUtil;
import com.zym.Design1.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created on 2018/3/24.
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EncashService encashService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UserService userService;

    @Autowired
    private EcoinLogService ecoinLogService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private FinService finService;


    @RequestMapping(value = "/tradeList", method = RequestMethod.POST)
    @ResponseBody
    public String tradeList(Model model,
                              @RequestParam("type") String type,//buy, sell
                              @RequestParam(value = "pageIndex", required = false, defaultValue ="1" ) Integer pageIndex,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                              @RequestParam(value = "searchKey", required = false) String userUid) {
        if(!hostHolder.getUser().getRole().equals(ConstantUtil.ROLE_ADMIN)) {
            throw new RuntimeException("权限不够");
        }
        if(type.equals("buy")) {//挂买
            List<Trade> buyList = tradeService.getBuyOrderListByPage(pageIndex, pageSize, userUid);
            for(Trade order : buyList) {
                if(order.getRuleId()!=null) {
                    order.setAfterBuyer(UserLevelRule.getRuleMap().get(order.getRuleId()).getDays().toString());//借用afterBuyer存放锁仓天数
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", ((Page) buyList).getTotal());
            jsonObject.put("page", pageIndex);
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(buyList);
            jsonObject.put("rows", jsonArray );
            return jsonObject.toString();
        }
        if(type.equals("sell")) {//挂卖
            List<Trade> sellList = tradeService.getSellOrderListByPage(pageIndex, pageSize, userUid);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", ((Page) sellList).getTotal());
            jsonObject.put("page", pageIndex);
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(sellList);
            jsonObject.put("rows", jsonArray );
            return jsonObject.toString();
        }
        return "";
    }

    @RequestMapping(value = "/tradeSoldList", method = RequestMethod.POST)
    @ResponseBody
    public String tradeSoldList(Model model,
                              @RequestParam(value = "pageIndex", required = false, defaultValue ="1" ) Integer pageIndex,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                              @RequestParam(value = "date", required = false) String date,
                              @RequestParam(value = "sellerUid", required = false) String sellerUid,
                              @RequestParam(value = "buyerUid", required = false) String buyerUid) {
        if(!hostHolder.getUser().getRole().equals(ConstantUtil.ROLE_ADMIN)) {
            throw new RuntimeException("权限不够");
        }
        List<Trade> soldList = tradeService.getSoldListByPage(pageIndex, pageSize, buyerUid, sellerUid, date);
        for(Trade trade: soldList) {
            if(trade.getRuleId()!=null) {
                trade.setRemark(UserLevelRule.getRuleMap().get(trade.getRuleId()).getDays().toString());//remark存放锁仓天数
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", ((Page)soldList).getTotal());
        jsonObject.put("page", pageIndex);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(soldList);
        jsonObject.put("rows", jsonArray );
        return jsonObject.toString();
    }


    @RequestMapping(value = "/finDetailAll", method = RequestMethod.POST)
    @ResponseBody
    public String finDetail(@RequestParam(value = "pageIndex", required = false, defaultValue ="1" ) Integer pageIndex,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                            @RequestParam(value = "searchKey", required = false) String searchUserUid,
                            Model model) {
        User user = hostHolder.getUser();
        if(!user.getRole().equals(ConstantUtil.ROLE_ADMIN)) {
            throw new RuntimeException("权限不够");
        }
        List<FinVO> detailList = finService.getFinDetailAll(pageIndex, pageSize, searchUserUid);
        for(FinVO finVO : detailList) {
            String source = null;
            if(finVO.getUnfrozenCoin() == null) {
                if(finVO.getEntityUserLevel().equals(0)) {
                    source = finVO.getType().equals(ConstantUtil.TYPE_SELF) ? "交易锁仓奖励" :
                            finVO.getUser().getUid() + "-交易锁仓" + "-推荐人奖励";
                }else {
                    source = finVO.getType().equals(ConstantUtil.TYPE_SELF) ? "积分套餐奖励" :
                            finVO.getUser().getUid() + "-积分套餐" +finVO.getRule().getDays()+"天"+ "-推荐人奖励";
                }
            }else {
                source="锁仓结束-释放KTH";
            }
            finVO.setSource(source);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", ((Page) detailList).getTotal());
        jsonObject.put("page", pageIndex);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(detailList);
        jsonObject.put("rows", jsonArray );
        return jsonObject.toString();
    }

    /**
     * 同意提现请求
     * @param id
     * @return
     */
    @RequestMapping(value = "/approveEncash/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String approveEncash(@PathVariable("id") Integer id,
                                @RequestParam(value = "resaon", defaultValue = "") String reason) {
        User user = hostHolder.getUser();
        if(user == null) {
            return ResultUtil.getJSONString(ResultUtil.NOT_LOGIN);
        }
        encashService.approveEncash(user, id, reason);
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }

    /**
     * 拒绝提现请求
     * @param id
     * @return
     */
    @RequestMapping(value = "/rejectEncash/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String rejectEncash(@PathVariable("id") Integer id,
                               @RequestParam(value = "reason", defaultValue = "") String reason) {
        User user = hostHolder.getUser();
        if(user == null) {
            return ResultUtil.getJSONString(ResultUtil.NOT_LOGIN);
        }
        encashService.rejectEncash(user, id, reason);
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }




}

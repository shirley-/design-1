package com.zym.Design1.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.zym.Design1.bean.HostHolder;
import com.zym.Design1.bean.vo.FinVO;
import com.zym.Design1.entity.User;
import com.zym.Design1.service.*;
import com.zym.Design1.util.ConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created  on 2018/4/4.
 */
@Controller
@Slf4j
@RequestMapping({"/member", "/admin"})
public class FinController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private FinService finService;

    @RequestMapping(value = "/myFin", method = RequestMethod.GET)
    public String ecoinRecordPage(@RequestParam(value = "m", required = false, defaultValue = "myFin") String mMenu,
                                  @RequestParam(value = "s", required = false, defaultValue = "finDetail") String sMenu,
//                                  @RequestParam(value = "state", required = false, defaultValue = "1") String state,//提现申请/同意/拒绝
                                  Model model) {
        //finDetail ecoinRecord
//        if(sMenu.equals("finDetail")) {
//
//        }
        if(sMenu.equals("encash")) {
            model.addAttribute("cashCoin", hostHolder.getUser().getCashCoin());
        }
        if(sMenu.equals("encashRecord")) {
//            model.addAttribute("cashCoin", hostHolder.getUser().getCashCoin());
//            model.addAttribute("state", state);
        }
        model.addAttribute("m", mMenu);
        model.addAttribute("s", sMenu);
        return sMenu;
    }

    @RequestMapping(value = "/finDetail", method = RequestMethod.POST)
    @ResponseBody
    public String finDetail(@RequestParam(value = "pageIndex", required = false, defaultValue ="1" ) Integer pageIndex,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                            Model model) {
        User user = hostHolder.getUser();
        List<FinVO> detailList = finService.getFinDetail(user.getId(), pageIndex, pageSize);
        for(FinVO finVO : detailList) {
            String source = null;
            if(finVO.getUnfrozenCoin() == null) {
                if(finVO.getEntityUserLevel().equals(0)) {
                    source = finVO.getType().equals(ConstantUtil.TYPE_SELF) ? "我的交易锁仓奖励" :
                            finVO.getUser().getUid() + "-交易锁仓" + "-推荐人奖励";
                }else {
                    source = finVO.getType().equals(ConstantUtil.TYPE_SELF) ? "我的积分套餐奖励" :
                            finVO.getUser().getUid() + "-积分套餐" + "-推荐人奖励";
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








}

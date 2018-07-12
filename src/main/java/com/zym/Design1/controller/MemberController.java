package com.zym.Design1.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.zym.Design1.bean.UserLevelRule;
import com.zym.Design1.bean.ViewObject;
import com.zym.Design1.entity.*;
import com.zym.Design1.bean.HostHolder;
import com.zym.Design1.mydao.ShopMapper;
import com.zym.Design1.service.*;
import com.zym.Design1.util.ConstantUtil;
import com.zym.Design1.util.ResultUtil;
import io.netty.util.internal.StringUtil;
import javafx.collections.transformation.SortedList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Result;
import java.math.BigDecimal;
import java.util.*;

import static com.zym.Design1.util.ucpaas.RestTest.testSendSms;


/**
 * Created on 2018/3/7.
 */
@Slf4j
@Controller
@RequestMapping({"/member", "/admin"})
public class MemberController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserBonusService userBonusService;

    @Autowired
    private OrgService orgService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private PhaseService phaseService;

    @Autowired
    private EcoinLogService ecoinLogService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private ShopService shopService;


    /**
     * 会员人脉页面
     * @param model
     * @return
     */
    @RequestMapping("/myConnection")
    public String myConnection(@RequestParam(value = "m", required = false, defaultValue = "myConnection") String mMenu,
                               Model model) {
        model.addAttribute("m", mMenu);
        return "myConnection";
    }

    /**
     * 会员电子币套餐选择页面
     * @param model
     * @return
     */
    @RequestMapping("/myOpt")
    public String myOpt(@RequestParam(value = "m", required = false, defaultValue = "myOpt") String mMenu,
                               Model model) {
        model.addAttribute("ruleList", ruleService.getRuleList());
        model.addAttribute("m", mMenu);
        model.addAttribute("phaseState", phaseService.getEarlyPhaseState());
        return "myOpt";
    }

    //会员选择锁仓套餐
    @ResponseBody
    @RequestMapping(value = "/chooseOpt", method = RequestMethod.POST)
    public String chooseOpt(@RequestParam("option") Integer option) {
        User user = hostHolder.getUser();
        user.setRuleId(option);
        userService.addEcoinOpt(user);

        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }

    //开始释放前可以修改锁仓套餐
    @ResponseBody
    @RequestMapping(value = "/modifyOpt", method = RequestMethod.POST)
    public String modifyOpt(@RequestParam("option") Integer option) {
        User user = hostHolder.getUser();
        if(user.getRuleId().equals(option)) {
            return ResultUtil.getJSONString(ResultUtil.ERROR, "没有修改");
        }
        //update user
        userService.updateEcoinOpt(user, option);
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }






    //个人电子币充值记录
    @RequestMapping(value = "/ecoinLog",method = RequestMethod.POST)
    @ResponseBody
    public String ecoinLogList(@RequestParam(value = "pageIndex", required = false, defaultValue ="1" ) Integer pageIndex,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "sort", required = false, defaultValue = "date") String sort,
                               @RequestParam(value = "sortOrder", required = false, defaultValue = "desc") String sortOrder,
                               @RequestParam(value = "searchKey", required = false) String searchKey ) {

        User user = hostHolder.getUser();
        List<BuyEcoinLog> users = ecoinLogService.queryEcoinLogs(pageIndex, pageSize, sort, sortOrder, null, user.getId());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", ((Page) users).getTotal());
        jsonObject.put("page", pageIndex);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(users);
        jsonObject.put("rows", jsonArray );
        return jsonObject.toString();
    }







    @RequestMapping(value = {"/myHome", "/"}, method = RequestMethod.GET)
    public String myHome(@RequestParam(value = "m", required = false, defaultValue = "myHome") String mMenu,
                         Model model) {
        User user = hostHolder.getUser();
        if(user == null) {
            return "login";
        }
//        Map<Integer, User> childrenMap = new HashMap<>();
//        orgService.getDescendantsMap(user.getId(), childrenMap);
//        model.addAttribute("childrenNum", childrenMap.size()-1);//我推荐了几个人

        model.addAttribute("vc", currencyService.getCurrency());//vc市值

        model.addAttribute("m", mMenu);
        return "myHome";
    }


    @ResponseBody
    @RequestMapping(value = "/changeTradePwd", method = RequestMethod.POST)
    public String changeTradePwdAction(@RequestParam("originPwd") String originPwd,
                                  @RequestParam("newPwd") String newPwd,
                                  @RequestParam("newPwd2") String newPwd2) {

        if(hostHolder.getUser() == null) {
            return ResultUtil.getJSONString(999);
        }
        User user = hostHolder.getUser();
        boolean b = userService.checkTradePassword(user, originPwd);
        if( !b) {
            return ResultUtil.getJSONString(ResultUtil.SUCCESS, "交易密码错误");
        } else {
            if( !newPwd.equals(newPwd2)) {
                return ResultUtil.getJSONString(ResultUtil.ERROR, "新密码确认错误");
            }
            userService.changeTradePassword(user, newPwd);
            return ResultUtil.getJSONString(ResultUtil.SUCCESS);
        }
    }



//    @RequestMapping(value = "/sendSms", method = RequestMethod.POST)
//    @ResponseBody
    /*public String sendSms(@RequestParam("tradePassword") String tradePassword, HttpServletRequest request) {
        User user = hostHolder.getUser();
        if(user==null) {
            return ResultUtil.getJSONString(ResultUtil.NOT_LOGIN);
        }
        if(!userService.checkTradePassword(user, tradePassword)) {
            throw new RuntimeException("交易密码错误");
        }
        String sid = "ea4ac7662a28e15fe876ad82f810ad12";
        String token = "78fc79d2b49b88ba5ae16ec11685a724";
        String appid = "5097faa1bcfc4940b8db8c1dbd48a723";
        String templateid = "298503";
        String mobile = user.getPhone();
        String uid = UUID.randomUUID().toString().replace("-","");

        HttpSession session = request.getSession();
        String param;
        if(session.getAttribute("tradeValidateCode") != null) {
            param = session.getAttribute("tradeValidateCode").toString();

        }else {
            param  = getRandNum(6);
            session.setAttribute("tradeValidateCode", param);
        }

        log.info("validateCode - param: {}", param);

        final Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                session.removeAttribute("tradeValidateCode");
                timer.cancel();
            }
        },10*60*1000);//10分钟有效
        //http://docs.ucpaas.com/doku.php
        String result = testSendSms(sid, token, appid, templateid, param, mobile, uid);
        Map resultMap = (Map)JSON.parse(result);
        if(resultMap.get("code").equals("000000") && resultMap.get("msg").equals("OK")
                && resultMap.get("uid").equals(uid)) {
            return ResultUtil.getJSONString(ResultUtil.SUCCESS);
        }else {
            return ResultUtil.getJSONString(ResultUtil.ERROR, resultMap.get("msg").toString());
        }
//        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }*/

    /*private String getRandNum(int charCount) {
        String charValue = "";
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue += String.valueOf(c);
        }
        return charValue;
    }

    private int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }*/

    @RequestMapping("/401")
    public String page401() {
        return "404";//未授权
    }

    @RequestMapping("/404")
    public String page404() {
        return "404";//not found
    }


    @RequestMapping(value = "/addToCart", method = RequestMethod.POST)
    @ResponseBody
    public String addToCart(@RequestParam("id") Integer id, @RequestParam("num") Integer num) {
        Integer userId = hostHolder.getUser().getId();
        shopService.addGoodsToCart(userId, id, num);
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }

    @RequestMapping("/updateCart")
    @ResponseBody
    public String updateCart(@RequestParam("cartStr") String cartStr) {
        if(hostHolder.getUser()!=null) {
            Integer userId = hostHolder.getUser().getId();
            shopService.updateCart(userId, cartStr);
            return ResultUtil.getJSONString(ResultUtil.SUCCESS);
        }else {
            throw new RuntimeException("未登录");
        }
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public String cart( Model model) {
        Integer userId = hostHolder.getUser().getId();
        List<ViewObject> cartInfo = shopService.getCartInfo(userId);
        model.addAttribute("cartList", cartInfo);
        return "cart";
    }

    @RequestMapping(value = "/buyCart", method = RequestMethod.POST)
    @ResponseBody
    public String buyCart(@RequestParam("cartStr") String cartStr, @RequestParam("totalPrice") BigDecimal totalPrice,
                          @RequestParam("userId") Integer userId, @RequestParam("name") String name,
                          @RequestParam("address") String address, @RequestParam("phone") String phone) {
        String uid = hostHolder.getUser().getUid();
        shopService.cartTrade(userId, uid, cartStr, name, phone, address, totalPrice);
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }

    @RequestMapping(value = "/shopRecord", method = RequestMethod.GET)
    public String shopRecord( Model model,
                              @RequestParam(value = "s", required = false, defaultValue = "shopRecord") String sMenu,
                              @RequestParam(value = "m", required = false, defaultValue = "shopManage") String mMenu) {
        model.addAttribute("s", sMenu);
        model.addAttribute("m", mMenu);
        return "shopRecord";
    }

    @RequestMapping(value = "/shopRecord", method = RequestMethod.POST)
    @ResponseBody
    public String shopRecord(@RequestParam("pageIndex") Integer pageIndex,
                             @RequestParam("pageSize") Integer pageSize) {
        Integer userId = hostHolder.getUser().getId();
        List<GoodsTrade> shopRecord = shopService.getShopRecord(userId, null, null, pageIndex, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", ((Page) shopRecord).getTotal());
        jsonObject.put("page", pageIndex);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(shopRecord);
        jsonObject.put("rows", jsonArray );
        return jsonObject.toString();
    }

}

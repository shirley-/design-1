package com.zym.Design1.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.zym.Design1.bean.HostHolder;
import com.zym.Design1.dao.CurrencyChangeMapper;
import com.zym.Design1.entity.*;
import com.zym.Design1.service.CurrencyService;
import com.zym.Design1.service.RuleService;
import com.zym.Design1.service.TradeService;
import com.zym.Design1.service.UserService;
import com.zym.Design1.util.ConstantUtil;
import com.zym.Design1.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created on 2018/3/21.
 */
@Slf4j
@Controller
@RequestMapping({"/member", "admin"})
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private RuleService ruleService;

    //page= 页面跳转

    @RequestMapping(value = "/trade", method = RequestMethod.GET)
    public String tradeHall(Model model,
                            @RequestParam(value = "s", required = false, defaultValue = "tradeHall") String sMenu,
                            @RequestParam(value = "m", required = false, defaultValue = "trade") String mMenu) {
        if(sMenu.equals("tradeHall")) {

            model.addAttribute("vc", currencyService.getCurrency());
            model.addAttribute("ruleList", ruleService.getLatePhaseRuleList());
        }
        if(sMenu.equals("tradeHistory")) {
            if(hostHolder.getUser().getRole().equals(ConstantUtil.ROLE_MEMBER)) {
                throw new RuntimeException("404 tradeHistory");
            }
//            tradeService.getSoldList()
        }
        if(sMenu.equals("tradeHistoryAll")) {
            if(!hostHolder.getUser().getRole().equals(ConstantUtil.ROLE_ADMIN)) {
                throw new RuntimeException("没有权限查看交易记录");
            }
        }
        model.addAttribute("m", mMenu);
        model.addAttribute("s", sMenu);
        return sMenu;
    }

    /**
     trade?s=orderPage        挂单列表
     trade?s=tradeListPage    售出列表
     trade?s=toSellPage       我要挂单
     trade?s=toBuyPage        我要求购
     */

//    @RequestMapping(value = "/trade1", method = RequestMethod.GET)
    public String tradingHall(Model model,
                              @RequestParam(value = "s", required = false, defaultValue = "orderListPage") String sMenu,
                              @RequestParam(value = "m", required = false, defaultValue = "trade") String mMenu,
                              @RequestParam(value = "pageIndex", required = false, defaultValue ="1" ) Integer pageIndex,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                              @RequestParam(value = "sort", required = false, defaultValue = "order_date") String sort,
                              @RequestParam(value = "sortOrder", required = false, defaultValue = "desc") String sortOrder,
                              @RequestParam(value = "searchKey", required = false) String searchKey) {
        if(hostHolder.getUser()==null) {
            return "redirect:/login?next=/trade" ;
        }
        if(sMenu.equals("orderListPage")) {
            List<Trade> orderList = tradeService.getOrderList(pageIndex, pageSize, sort, sortOrder, searchKey, ConstantUtil.TYPE_SELL_ORDER);
            long orderListTotal = ((Page) orderList).getTotal();
            model.addAttribute("orderList", orderList);
            model.addAttribute("orderListTotal", orderListTotal);
            model.addAttribute("ruleList", ruleService.getLatePhaseRuleList());
        }
        if(sMenu.equals("buyOrderListPage")) {
            List<Trade> orderList = tradeService.getOrderList(pageIndex, pageSize, sort, sortOrder, searchKey, ConstantUtil.TYPE_BUY_ORDER);
            long orderListTotal = ((Page) orderList).getTotal();
            model.addAttribute("buyOrderList", orderList);
            model.addAttribute("buyOrderListTotal", orderListTotal);
        }
        if(sMenu.equals("tradeListPage")) {
            List<Trade> soldList = tradeService.getSoldList(pageIndex, pageSize, null);
            long tradeListTotal = ((Page) soldList).getTotal();
            model.addAttribute("soldList", soldList);
            model.addAttribute("tradeListTotal", tradeListTotal);
        }
        if(sMenu.equals("wantToBuyPage")) {
            model.addAttribute("ruleList",ruleService.getLatePhaseRuleList());
        }
        BigDecimal currency = currencyService.getCurrency();
        model.addAttribute("vc", currency);
        model.addAttribute("s", sMenu);
        model.addAttribute("m", mMenu);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("sort", sort);
        model.addAttribute("sortOrder", sortOrder);
        return sMenu;
    }

    @RequestMapping(value = "/buySellList", method = RequestMethod.POST)
    @ResponseBody
    public String tradingList(Model model, @RequestParam("type") String type) {
        if(hostHolder.getUser()==null) {
            return "redirect:/login" ;
        }
        if(type.equals("buy")) {//挂买
            List<Trade> buyList = tradeService.getBuyOrderList(5, null);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", "5");
            jsonObject.put("page", "1");
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(buyList);
            jsonObject.put("rows", jsonArray );
            return jsonObject.toString();
        }
        if(type.equals("sell")) {//挂卖
            List<Trade> sellList = tradeService.getSellOrderList(5, null);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", "5");
            jsonObject.put("page", "1");
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(sellList);
            jsonObject.put("rows", jsonArray );
            return jsonObject.toString();
        }
        if(type.equals("sold")) {//完成
            List<Trade> soldList = tradeService.getSoldList(1, 10, null);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", "10");
            jsonObject.put("page", "1");
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(soldList);
            jsonObject.put("rows", jsonArray );
            return jsonObject.toString();
        }
        return "";
    }

    @RequestMapping(value = "/myTrade", method = RequestMethod.POST)
    @ResponseBody
    public String myTrade(Model model, @RequestParam("type") String type,
                          @RequestParam(value = "pageIndex", required = false, defaultValue ="1" ) Integer pageIndex,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        User user = hostHolder.getUser();
        if(type.equals("order")) {//挂单
            List<Trade> orderList = tradeService.getListForMyTrade(pageIndex, pageSize," order_date desc, vc_price desc ",
                                                             ConstantUtil.STATE_VALID, user.getId());
            JSONObject jsonObject = new JSONObject();
            long total = ((Page) orderList).getTotal();
            jsonObject.put("total", total);
            jsonObject.put("page", pageIndex);
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(orderList);
            jsonObject.put("rows", jsonArray );
            return jsonObject.toString();
        }
        if(type.equals("sold")) {//成交
            List<Trade> soldList = tradeService.getListForMyTrade(pageIndex, pageSize,"trade_date desc, order_date desc, vc_price desc ",
                                                             ConstantUtil.STATE_SOLD, user.getId());
            for(Trade trade : soldList) {
                if(trade.getBuyerId().equals(hostHolder.getUser().getId())) {//登录人是买家
                    trade.setType(ConstantUtil.TYPE_BUY_ORDER);
                }
                if(trade.getSellerId().equals(hostHolder.getUser().getId())) {//登录人是卖家
                    trade.setType(ConstantUtil.TYPE_SELL_ORDER);
                }
                if(trade.getBuyerId().equals(hostHolder.getUser().getId()) &&
                        trade.getSellerId().equals(hostHolder.getUser().getId())) {//自己卖自己买
                    trade.setType("-");
                }
            }
            JSONObject jsonObject = new JSONObject();
            long total = ((Page) soldList).getTotal();
            jsonObject.put("total", total);
            jsonObject.put("page", pageIndex);
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(soldList);
            jsonObject.put("rows", jsonArray );
            return jsonObject.toString();
        }
        return "";
    }


//    @ResponseBody
//    @RequestMapping(value = "/sellVc", method = RequestMethod.POST)
    public String sellVc(HttpServletRequest request,
                         @RequestParam("id") Long id,
                         @RequestParam("tradePassword") String tradePwd
                         /*@RequestParam("validateCode") String validateCode*/ )  {
        throw new RuntimeException("test");
        /*User user = hostHolder.getUser();
        if(user == null) {
            return ResultUtil.getJSONString(ResultUtil.ERROR, "未登录");
        }
        boolean checkTradePassword = userService.checkTradePassword(user, tradePwd);
        if(!checkTradePassword) {
            throw new RuntimeException("交易密码错误");
        }
        if(!request.getSession().getAttribute("tradeValidateCode").equals(validateCode)) {
            throw new RuntimeException("短信验证码错误");
        }
        tradeService.sellVc(user, id);
        //成功购买
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);*/
    }


    //request= 处理请求

    /**
     * 挂单请求
     * @param amount
     * @param vc
     * @return
     */
//    @ResponseBody
//    @RequestMapping(value = "/wantToSell", method = RequestMethod.POST)
    public String wantToSell(HttpServletRequest request,
                             @RequestParam("amount") BigDecimal amount,
                             @RequestParam("vc") BigDecimal vc,
//                             @RequestParam("validateCode") String validateCode,
                             @RequestParam("tradePassword") String tradePwd  ){
        User user = hostHolder.getUser();
        if(user == null) {
            return ResultUtil.getJSONString(ResultUtil.ERROR, "未登录");
        }
        boolean checkTradePassword = userService.checkTradePassword(user, tradePwd);
        if(!checkTradePassword) {
            throw new RuntimeException("交易密码错误");
        }
        /*if(!request.getSession().getAttribute("tradeValidateCode").equals(validateCode)) {
            throw new RuntimeException("短信验证码错误");
        }*/

//        Trade tradeOrder = tradeService.wantToSell(user, vc, amount);//插入挂单记录，更新用户信息

        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "/sell", method = RequestMethod.POST)
    public String sell(HttpServletRequest request,
                             @RequestParam("amount") BigDecimal amount,
                             @RequestParam("vc") BigDecimal vc,
//                       @RequestParam("validateCode") String validateCode,
                             @RequestParam("tradePassword") String tradePwd ){
        if(amount.compareTo(BigDecimal.valueOf(10))<0 ||
                amount.divideAndRemainder(BigDecimal.TEN)[1].compareTo(BigDecimal.ZERO)!=0) {
            throw new RuntimeException("挂卖数量必须是10的倍数，最小为10!");
        }
        User user = hostHolder.getUser();
        if(user == null) {
            return ResultUtil.getJSONString(ResultUtil.ERROR, "未登录");
        }
        boolean checkTradePassword = userService.checkTradePassword(user, tradePwd);
        if(!checkTradePassword) {
            throw new RuntimeException("交易密码错误");
        }
        /*if(request.getSession().getAttribute("tradeValidateCode")== null  || !request.getSession().getAttribute("tradeValidateCode").equals(validateCode)) {
            throw new RuntimeException("短信验证码错误");
        }*/
        synchronized (TradeController.class) {
            tradeService.wantToSell(user, vc, amount);//插入挂单记录，更新用户信息
        }

        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }

    /**
     * 取消挂单
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public String cancelSell(HttpServletRequest request,
                                @RequestParam("id") Long id,
                                @RequestParam("tradePassword") String tradePwd,
//                                @RequestParam("validateCode") String validateCode,
                                @RequestParam("type") String type)  {

        User user = hostHolder.getUser();
        if(user == null) {
            return ResultUtil.getJSONString(ResultUtil.ERROR, "未登录");
        }

        boolean checkTradePassword = userService.checkTradePassword(user, tradePwd);
        if(!checkTradePassword) {
            throw new RuntimeException("交易密码错误");
        }
        /*if(!request.getSession().getAttribute("tradeValidateCode").equals(validateCode)) {
            throw new RuntimeException("短信验证码错误");
        }*/
        Trade tradeOrder = tradeService.getTrade(id);
        if(type.equals(ConstantUtil.TYPE_SELL_ORDER)) {
            if( !tradeOrder.getSellerId().equals(user.getId())) {
                return ResultUtil.getJSONString(ResultUtil.ERROR, "未登录");
            }
            synchronized (TradeController.class) {
                tradeService.cancelSellOrder(user, id);
            }
        }
        if(type.equals(ConstantUtil.TYPE_BUY_ORDER)) {
            if( !tradeOrder.getBuyerId().equals(user.getId())) {
                return ResultUtil.getJSONString(ResultUtil.ERROR, "未登录");
            }
            synchronized (TradeController.class) {
                tradeService.cancelBuyOrder(user, id);
            }
        }
        //成功撤销
        return ResultUtil.getJSONString(ResultUtil.SUCCESS, String.valueOf(id));
    }

//    @ResponseBody
//    @RequestMapping(value = "/buyVc", method = RequestMethod.POST)
    public String buyVc(HttpServletRequest request,
                        @RequestParam("id") Long id,
                        @RequestParam("tradePassword") String tradePwd,
                        @RequestParam("validateCode") String validateCode )  {
        throw new RuntimeException("test");
        /*User user = hostHolder.getUser();
        if(user == null) {
            return ResultUtil.getJSONString(ResultUtil.ERROR, "未登录");
        }
        boolean checkTradePassword = userService.checkTradePassword(user, tradePwd);
        if(!checkTradePassword) {
            throw new RuntimeException("交易密码错误");
        }
        if(!request.getSession().getAttribute("tradeValidateCode").equals(validateCode)) {
            throw new RuntimeException("短信验证码错误");
        }
        tradeService.buyVc(user, id);
        //成功购买
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);*/
    }

    /**
     * 求购请求
     * @param amount
     * @param vc
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/buy", method = RequestMethod.POST)
    public String wantToBuy(HttpServletRequest request,
                             @RequestParam("amount2") BigDecimal amount,
                             @RequestParam("vc2") BigDecimal vc,
                             @RequestParam("tradePassword2") String tradePwd,
//                             @RequestParam("validateCode") String validateCode,
                             @RequestParam("ruleId") Integer ruleId){
        if(amount.compareTo(BigDecimal.valueOf(10))<0 ||
                amount.divideAndRemainder(BigDecimal.TEN)[1].compareTo(BigDecimal.ZERO)!=0) {
            throw new RuntimeException("挂买数量必须是10的倍数，最小为10!");
        }
        User user = hostHolder.getUser();
        if(user == null) {
            return ResultUtil.getJSONString(ResultUtil.ERROR, "未登录");
        }
        boolean checkTradePassword = userService.checkTradePassword(user, tradePwd);
        if(!checkTradePassword) {
            throw new RuntimeException("交易密码错误");
        }
        /*if(request.getSession().getAttribute("tradeValidateCode")== null  || !request.getSession().getAttribute("tradeValidateCode").equals(validateCode)) {
            throw new RuntimeException("短信验证码错误");
        }*/
        synchronized (TradeController.class) {
            tradeService.wantToBuy(user, vc, amount, ruleId);//插入挂单记录，更新用户信息
        }

        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }

    /**
     * 取消求购
     * @param id
     * @return
     */
//    @ResponseBody
//    @RequestMapping(value = "/cancelBuyTry", method = RequestMethod.POST)
    public String cancelBuyTry(HttpServletRequest request,
                               @RequestParam(value = "id") Long id,
//                               @RequestParam("validateCode") String validateCode,
                               @RequestParam("tradePassword") String tradePwd   )  {

        Trade tradeOrder = tradeService.getTrade(id);
        User user = hostHolder.getUser();
        if(user == null) {
            return ResultUtil.getJSONString(ResultUtil.ERROR, "未登录");
        }
        if( !tradeOrder.getBuyerId().equals(user.getId())) {
            return ResultUtil.getJSONString(ResultUtil.ERROR, "未登录");
        }
        boolean checkTradePassword = userService.checkTradePassword(user, tradePwd);
        if(!checkTradePassword) {
            throw new RuntimeException("交易密码错误");
        }
        /*if(!request.getSession().getAttribute("tradeValidateCode").equals(validateCode)) {
            throw new RuntimeException("短信验证码错误");
        }*/
//        tradeService.cancelBuyOrder(user, id);
        //成功撤销
        return ResultUtil.getJSONString(ResultUtil.SUCCESS, String.valueOf(id));
    }

    //KMC趋势图数据
    @RequestMapping("/getTrendData")
    @ResponseBody
    public String calculteTrend() {
        return currencyService.getTrendData().toJSONString();

    }

}

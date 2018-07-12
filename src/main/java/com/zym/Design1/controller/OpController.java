package com.zym.Design1.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.zym.Design1.bean.HostHolder;
import com.zym.Design1.bean.UserLevelRule;
import com.zym.Design1.bean.vo.EncashVO;
import com.zym.Design1.bean.vo.GoodsStatistic;
import com.zym.Design1.entity.*;
import com.zym.Design1.service.*;
import com.zym.Design1.util.ConstantUtil;
import com.zym.Design1.util.ProjectUtil;
import com.zym.Design1.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created on 2018/4/24.
 */
@Slf4j
@Controller
@RequestMapping({"/admin", "/op"})
public class OpController {

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
    private ShopService shopService;



    /**
     * 会员基本信息管理
     * 会员列表页面
     */
    @RequestMapping(value = "/memberManage", method = RequestMethod.GET)
    public String memberListPage(Model model,
                                 @RequestParam(value = "m", defaultValue = "memberManage", required = false) String mMenu,
                                 @RequestParam(value = "s", defaultValue = "memberList", required = false) String sMenu) {
        if(sMenu.equals("memberList")) {
//            model.addAttribute("ruleList", ruleService.getRuleList());
        }
        model.addAttribute("m", mMenu);
        model.addAttribute("s", sMenu);
        return sMenu;
    }

    //管理员查看全部用户列表
    @RequestMapping(value = "/memberlist",method = RequestMethod.POST)
    @ResponseBody
    public String memberList(@RequestParam(value = "pageIndex", required = false, defaultValue ="1" ) Integer pageIndex,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(value = "sort", required = false) String sort,
                             @RequestParam(value = "sortOrder", required = false) String sortOrder,
                             @RequestParam(value = "searchKey", required = false) String searchKey) {
        List<User> users = userService.getPageUsers(pageIndex, pageSize, sort, sortOrder, searchKey);
        for(User u: users) {
            if(u.getRuleId()!=null) {
                u.setImg(UserLevelRule.getRuleMap().get(u.getRuleId()).getDays().toString());//借用img存放锁仓天数
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", ((Page) users).getTotal());
        jsonObject.put("page", pageIndex);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(users);
        jsonObject.put("rows", jsonArray );
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/members", method = RequestMethod.POST)
    public String addMember(@RequestBody User user)  {
        user.setUid(user.getUid().trim());
        user.setName(user.getName().trim());
        if(user.getUid().equals(user.getIntroducerUid())) {
            throw new RuntimeException("推荐人不符合规定");
        }

        userService.registerUser(user);

        log.info("register user: {}", JSON.toJSONString(user));
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }

    //管理员删除用户，置为无效
    @ResponseBody
    @RequestMapping(value="/members/{id}", method= RequestMethod.DELETE)
    public String deleteMember(@PathVariable("id") Integer id) {
        log.info(String.valueOf(id));
        userService.deleteUser(id);
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }

    //管理员修改用户信息，只有如下信息可以修改
    @ResponseBody
    @RequestMapping(value = "/members/{id}", method = RequestMethod.PUT)
    public String updateMember(@RequestBody User user) {
        User userOld = userService.getUserById(user.getId());
        /*if(!user.getEcoin().equals(userOld)) {
            if(userOld.getEcoinSetFlag().equals(ConstantUtil.STATE_VALID)) {
                throw new RuntimeException("该会员锁仓套餐已选，不可更改积分数量!");
            }else {
                userOld.setEcoin(user.getEcoin());
            }
        }*/
        userOld.setName(user.getName());
        userOld.setPhone(user.getPhone());
        userOld.setEmail(user.getEmail());
        userOld.setAddress(user.getAddress());
        userOld.setAccountHolder(user.getAccountHolder());
        userOld.setBankAccount(user.getBankAccount());
        userOld.setIdNumber(user.getIdNumber());
        userOld.setDepositBank(user.getDepositBank());
        userService.updateUser(userOld);
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }


    @ResponseBody
    @RequestMapping(value = "/encashList", method = RequestMethod.POST)
    public String encashList(@RequestParam(value = "pageIndex", required = false, defaultValue ="1" ) Integer pageIndex,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                 @RequestParam(value = "sort", required = false, defaultValue = "createdDate") String sort,
                                 @RequestParam(value = "sortOrder", required = false, defaultValue = "asc") String sortOrder,
                                 @RequestParam(value = "state", required = false, defaultValue = "1") String state,
                                 @RequestParam(value = "searchDate", required = false ) String searchDate,
                                 Model model) {
        if(state.equals(ConstantUtil.STATE_APPROVE)) {
            //sort sortOrder state 无用
            List<EncashVO> encashList = encashService.getApprovedEncashList(pageIndex, pageSize, searchDate, state, null);
            model.addAttribute("encashList", encashList);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", ((Page) encashList).getTotal());
            jsonObject.put("page", pageIndex);
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(encashList);
            jsonObject.put("rows", jsonArray);
            return jsonObject.toString();
        }else {
            if(state.equals(ConstantUtil.STATE_REJECT)) {
                sort = "approveDate";
                sortOrder = "desc";
            }
            List<Encash> encashList = encashService.getEncashList(pageIndex, pageSize, sort, sortOrder, state, null);
            model.addAttribute("encashList", encashList);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", ((Page) encashList).getTotal());
            jsonObject.put("page", pageIndex);
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(encashList);
            jsonObject.put("rows", jsonArray);
            return jsonObject.toString();
        }
    }

    @RequestMapping(value = "/encashManage", method = RequestMethod.GET)
    public String encashListPage(@RequestParam(value = "m", required = false, defaultValue = "encashManage") String mMenu,
                                 @RequestParam(value = "s", required = false, defaultValue = "encashApplyList") String sMenu,
                                 Model model) {
        User user = hostHolder.getUser();
        String state=null;
        if(sMenu.equals("encashApplyList")) {
            if(!user.getRole().equals(ConstantUtil.ROLE_ADMIN)) {
                throw new RuntimeException("没有权限");
            }
            state = "1";
        }
        if(sMenu.equals("encashApprovedList")) {
            state = "3";
        }
        if(sMenu.equals("encashRejectedList")) {
            if(!user.getRole().equals(ConstantUtil.ROLE_ADMIN)) {
                throw new RuntimeException("没有权限");
            }
            state = "4";
        }
        model.addAttribute("state", state);
        model.addAttribute("m", mMenu);
        model.addAttribute("s", sMenu);
        return "encashListPage";
    }




    @RequestMapping(value = "/editNotice", method = RequestMethod.GET)
    public String editNoticePage(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if(id!= null) {
            model.addAttribute("notice", noticeService.getNoticeById(id));
        }
        return "editNotice";
    }





    @RequestMapping(value = "/editNotice", method = RequestMethod.POST)
    public String editNotice(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam(value = "id", required = false) Integer id) {
        User user = hostHolder.getUser();
        if(user == null) {
            return "redirect:/404";
        }
        if(id!=null ) {
            noticeService.update(id, title, content);
            return "redirect:readNotice?id=" + id;
        }else {
            Notice notice = noticeService.addNotice(title, content, user);
            return "redirect:readNotice?id=" + notice.getId();
        }
    }


    @RequestMapping(value = "/noticeListPage", method = RequestMethod.GET)
    public String noticeListPage(@RequestParam(value = "m", required = false, defaultValue = "notice") String mMenu,
                                 Model model) {
        model.addAttribute("m", mMenu);
        return "noticeListPage";
    }


    @RequestMapping(value = "/ecoinManage",method = RequestMethod.GET)
    public String eCoinManage() {
        return "ecoinPage";
    }



    @RequestMapping(value = "/ecoinLogList",method = RequestMethod.POST)
    @ResponseBody
    public String ecoinLogList(@RequestParam(value = "pageIndex", required = false, defaultValue ="1" ) Integer pageIndex,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "sort", required = false) String sort,
                               @RequestParam(value = "sortOrder", required = false) String sortOrder,
                               @RequestParam(value = "searchKey", required = false) String searchKey,
                               @RequestParam(value = "searchUserId", required = false) Integer searchUserId,
                               @RequestParam(value = "self", required = false) Integer self ) {
        List<BuyEcoinLog> users = null;
        if(self != null && self.equals(Integer.valueOf(1))) {
            User user = hostHolder.getUser();
            users = ecoinLogService.queryEcoinLogs(pageIndex, pageSize, sort, sortOrder, null, user.getId());
        }else {
            users = ecoinLogService.queryEcoinLogs(pageIndex, pageSize, sort, sortOrder, searchKey, searchUserId);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", ((Page) users).getTotal());
        jsonObject.put("page", pageIndex);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(users);
        jsonObject.put("rows", jsonArray );
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/ecoinLogAdd", method = RequestMethod.POST)
    public String addECoinLog(@RequestBody BuyEcoinLog buyEcoinLog)  {
//        log.info(buyEcoinLog.toString());
        //当前登录人是管理员
        User user = hostHolder.getUser();
        if(user.getRole().equals(ConstantUtil.ROLE_OP)) {//低级管理员，最高7000积分
            if(buyEcoinLog.getAmount().compareTo(ConstantUtil.OP_ECOIN_CHARGE_MAX)>0) {
                log.error("普通管理员没有权限充值大额积分, userId:{}, amount:{}, op: {}", buyEcoinLog.getUserUid(), buyEcoinLog.getAmount(), user.getUid());
                throw new RuntimeException("普通管理员没有权限充值大额积分");
            }
            //总额7000限制
            BigDecimal total = ecoinLogService.getTotalEcoinChargeAmount(buyEcoinLog.getUserUid());
            if(total!=null && total.add(buyEcoinLog.getAmount()).compareTo(ConstantUtil.OP_ECOIN_CHARGE_MAX) > 0) {
                log.error("普通管理员没有权限充值大额积分, 该用户积分充值已超限, userId:{}, total:{}, amount:{}, op: {}", buyEcoinLog.getUserUid(), total, buyEcoinLog.getAmount(), user.getUid());
                throw new RuntimeException("普通管理员没有权限充值大额积分，该用户积分充值已超限");
            }
        }
        userService.chargeEcoin(buyEcoinLog);
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }

     /*@ResponseBody
    @RequestMapping(value="/eCoinLog/{id}", method= RequestMethod.DELETE)
    public String deleteECoinLog(@PathVariable("id") Integer id) {
        log.info(String.valueOf(id));
        userService.deleteEcoinLog(id);
        return JSON.toJSONString(id);
    }

    @ResponseBody
    @RequestMapping(value = "/eCoinLog/{id}", method = RequestMethod.PUT)
    public String updateECoinLog(@RequestBody BuyEcoinLog buyEcoinLog) {
        log.info(String.valueOf(buyEcoinLog.getId()));
        userService.updateEcoinLog(buyEcoinLog);
        return JSON.toJSONString(buyEcoinLog);
    }*/

    @ResponseBody
    @RequestMapping(value = "/resetLoginPwd", method = RequestMethod.POST)
    public String restLoginPwd(@RequestParam("id") Integer id)  {
        userService.resetLoginPwd(id);
        log.info("resetLoginPwd, hostholder:{}, user:{}", hostHolder.getUser().getUid(), id);
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "/resetTradePwd", method = RequestMethod.POST)
    public String restTradePwd(@RequestParam("id") Integer id)  {
        userService.resetTradePwd(id);
        log.info("resetLoginPwd, hostholder:{}, user:{}", hostHolder.getUser().getUid(), id);
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }


    //商品
    @RequestMapping(value = "/editGoods", method = RequestMethod.GET)
    public String editGoodsPage(@RequestParam(value = "id", required = false) Integer id,
                                Model model,
                                @RequestParam(value = "s", required = false, defaultValue = "editGoods") String sMenu,
                                @RequestParam(value = "m", required = false, defaultValue = "shopManage") String mMenu) {
        if(id!= null) {
            model.addAttribute("item", shopService.getGoodsById(id));
        }
        List<GoodsType> goodsTypes = shopService.getGoodsTypes();
        model.addAttribute("goodsTypes", goodsTypes);
        model.addAttribute("s", sMenu);
        model.addAttribute("m", mMenu);
        return sMenu;
    }

    @RequestMapping(value = "/addGoods", method = RequestMethod.POST)
    @ResponseBody
    public String addGoods(@RequestParam("name") String name,
                           @RequestParam("price") BigDecimal price,
                           @RequestParam("type") Integer type,
                           @RequestParam("description") String description,
                           @RequestParam(value="img") String img,
                           @RequestParam(value = "id", required = false) Integer id) throws IOException {
        if(id!=null ) {
            shopService.updateGoods(id, name, price, type, img, description);
            return ResultUtil.getJSONString(ResultUtil.SUCCESS);
        }else {
            Goods goods = shopService.addGoods(name, price, type, img, description);
            return ResultUtil.getJSONString(ResultUtil.SUCCESS);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public String uploadImage(@RequestParam(value = "file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        log.info("上传的后缀名为：" + suffixName);

        // 文件上传后的路径
        String filePath = "/www/server/img/";
        // 解决中文问题，liunx下中文路径，图片显示问题
//        fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        if(dest.exists()) {
            log.error("同名图片已存在:{}", fileName);
            throw new RuntimeException("同名图片已存在");
        }
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            log.info("上传成功后的文件路径：" + filePath + fileName);
            return ResultUtil.getJSONString(ResultUtil.SUCCESS, fileName);
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
            throw e;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return ResultUtil.getJSONString(ResultUtil.ERROR, "文件上传失败");
    }


    @RequestMapping(value = "/allShopRecord", method = RequestMethod.GET)
    public String allShopRecord(Model model,
                                @RequestParam(value = "s", required = false, defaultValue = "allShopRecord") String sMenu,
                                @RequestParam(value = "m", required = false, defaultValue = "shopManage") String mMenu) {
        model.addAttribute("s", sMenu);
        model.addAttribute("m", mMenu);
        return sMenu;
    }

    @RequestMapping(value = "/allShopRecord", method = RequestMethod.POST)
    @ResponseBody
    public String allShopRecord(@RequestParam("pageIndex") Integer pageIndex,
                                @RequestParam("pageSize") Integer pageSize,
                                @RequestParam(value = "userUid", required = false) String userUid,
                                @RequestParam(value = "state", required = false) String state,
                                @RequestParam(value = "goodsName", required = false) String goodsName,
                                @RequestParam(value = "days1", required = false, defaultValue = "") String days1,
                                @RequestParam(value = "days2", required = false, defaultValue = "") String days2) {
        List<GoodsTrade> shopRecord = shopService.getAllShopRecord(null, userUid, state, days1, days2, goodsName, pageIndex, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", ((Page) shopRecord).getTotal());
        jsonObject.put("page", pageIndex);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(shopRecord);
        jsonObject.put("rows", jsonArray );
        return jsonObject.toString();
    }

    @RequestMapping(value = "/confirmAndSendGoods", method = RequestMethod.POST)
    @ResponseBody
    public String comfirmAndSendGoods(@RequestBody String ids) {
        JSONArray jsonArray = JSON.parseArray(ids);
        List<Long> list = new ArrayList<>();
        for(int i=0; i<jsonArray.size(); i++) {
            String s = jsonArray.get(i).toString();
            list.add(Long.valueOf(s));
        }
        shopService.confirmAndSendGoods(list);
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }

    @RequestMapping(value = "/lackGoods", method = RequestMethod.POST)
    @ResponseBody
    public String lackGoods(@RequestBody String ids) {
        JSONArray jsonArray = JSON.parseArray(ids);
        List<Long> list = new ArrayList<>();
        for(int i=0; i<jsonArray.size(); i++) {
            String s = jsonArray.get(i).toString();
            list.add(Long.valueOf(s));
        }
        shopService.lackGoods(list);
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }



    @RequestMapping(value = "/goodsList", method = RequestMethod.GET)
    public String goodsList(Model model,
                            @RequestParam(value = "s", required = false, defaultValue = "goodsList") String sMenu,
                            @RequestParam(value = "m", required = false, defaultValue = "shopManage") String mMenu) {
        List<GoodsType> goodsTypes = shopService.getGoodsTypes();
        model.addAttribute("goodsTypes", goodsTypes);
        model.addAttribute("s", sMenu);
        model.addAttribute("m", mMenu);
        return sMenu;
    }

    @RequestMapping(value = "/goodsList", method = RequestMethod.POST)
    @ResponseBody
    public String goodsList(@RequestParam(value = "type", required = false, defaultValue = "") Integer type,
                             @RequestParam(value = "state", required = false, defaultValue = "") String state,
                             @RequestParam(value = "name", required = false, defaultValue = "") String name,
                             @RequestParam("pageIndex") Integer pageIndex,
                             @RequestParam("pageSize") Integer pageSize) {
        List<Goods> goodsList = shopService.getGoods(type, state, name, pageIndex, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", ((Page) goodsList).getTotal());
        jsonObject.put("page", pageIndex);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(goodsList);
        jsonObject.put("rows", jsonArray );
        return jsonObject.toString();
    }

    @RequestMapping(value = "/putOnShelf", method = RequestMethod.POST)
    @ResponseBody
    public String putOnShelf(@RequestBody String ids) {
        JSONArray jsonArray = JSON.parseArray(ids);
        List<Integer> list = new ArrayList<>();
        for(int i=0; i<jsonArray.size(); i++) {
            String s = jsonArray.get(i).toString();
            list.add(Integer.valueOf(s));
        }
        shopService.validateGoods(list);
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }

    @RequestMapping(value = "/pullOffShelf", method = RequestMethod.POST)
    @ResponseBody
    public String pullOffShelf(@RequestBody String ids) {
        JSONArray jsonArray = JSON.parseArray(ids);
        List<Integer> list = new ArrayList<>();
        for(int i=0; i<jsonArray.size(); i++) {
            String s = jsonArray.get(i).toString();
            list.add(Integer.valueOf(s));
        }
        shopService.invalidateGoods(list);
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }


    @RequestMapping(value = "/goodsTradeHistory", method = RequestMethod.GET)
    public String goodsTradeHistory(Model model,
                                    @RequestParam(value = "s", required = false, defaultValue = "goodsTradeHistory") String sMenu,
                                    @RequestParam(value = "m", required = false, defaultValue = "shopManage") String mMenu) {
        List<GoodsType> goodsTypes = shopService.getGoodsTypes();
        model.addAttribute("goodsTypes", goodsTypes);
        model.addAttribute("s", sMenu);
        model.addAttribute("m", mMenu);
        return sMenu;
    }

    @RequestMapping(value = "/goodsTradeHistory", method = RequestMethod.POST)
    @ResponseBody
    public String goodsTradeHistory(@RequestParam(value = "type", required = false, defaultValue = "") Integer type,
                            @RequestParam(value = "days1", required = false, defaultValue = "") String days1,
                            @RequestParam(value = "days2", required = false, defaultValue = "") String days2,
                            @RequestParam(value = "name", required = false, defaultValue = "") String name,
                            @RequestParam("pageIndex") Integer pageIndex,
                            @RequestParam("pageSize") Integer pageSize) {
        List<GoodsStatistic> historyList = shopService.goodsStatistic(days1, days2, name, type, pageIndex, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", ((Page) historyList).getTotal());
        jsonObject.put("page", pageIndex);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(historyList);
        jsonObject.put("rows", jsonArray );
        return jsonObject.toString();
    }


}

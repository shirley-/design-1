package com.zym.Design1.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.zym.Design1.bean.ViewObject;
import com.zym.Design1.bean.vo.GoodsDetailVO;
import com.zym.Design1.bean.vo.GoodsStatistic;
import com.zym.Design1.dao.GoodsMapper;
import com.zym.Design1.dao.GoodsTradeMapper;
import com.zym.Design1.dao.GoodsTypeMapper;
import com.zym.Design1.entity.*;
import com.zym.Design1.mydao.ShopMapper;
import com.zym.Design1.util.ConstantUtil;
import com.zym.Design1.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2018/5/9.
 */
@Service
@Slf4j
public class ShopService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsTradeMapper goodsTradeMapper;

    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    public List<GoodsType> getGoodsTypes() {
         return goodsTypeMapper.selectByExample(new GoodsTypeExample());
    }

    @Transactional
    public Goods addGoods(String name, BigDecimal price, Integer type, String img, String description) throws IOException {
        Goods  goods = new Goods();
        goods.setName(name);
        goods.setPrice(price);
        goods.setGoodsType(type);
        goods.setDescription(description);
        /*BufferedOutputStream out = null;//保存图片到目录下
        try {
            out = new BufferedOutputStream(
                    new FileOutputStream(new File("imgs\\"+name+".jpg")));
            out.write(img.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("save img file");
            throw e;
        }
        String filename="imgs\\"+name+".jpg";*/
        goods.setImg(img);
        goods.setState(ConstantUtil.STATE_INVALID);//无效，等待上架
        goodsMapper.insertSelective(goods);
        return goods;
    }

    public void updateGoods(Integer id, String name, BigDecimal price, Integer type, String img, String description) {
        Goods goods = goodsMapper.selectByPrimaryKey(id);
        if(goods.getState().equals(ConstantUtil.STATE_VALID)) {
            log.error("商品先下架再修改, goods:{}, state:{}", goods.getId(), goods.getState());
            throw new RuntimeException("商品先下架再修改!");
        }
        goods.setName(name);
        goods.setPrice(price);
        goods.setGoodsType(type);
        goods.setImg(img);
        goods.setDescription(description);
        int num = goodsMapper.updateByPrimaryKeyWithBLOBs(goods);
        if(num!=1) {
            log.error("updateGoods错误，num:{} , goods:{}", num, JSON.toJSONString(goods));
            throw new RuntimeException("修改商品失败");
        }
    }

    public Goods getGoodsById(Integer id) {
        return goodsMapper.selectByPrimaryKey(id);
    }


    public List<Goods> getGoodsOfType(Integer typeId, Integer pageIndex, Integer pageSize) {
        GoodsExample goodsExample = new GoodsExample();
        goodsExample.createCriteria().andGoodsTypeEqualTo(typeId).andStateEqualTo(ConstantUtil.STATE_VALID);
        PageHelper.startPage(pageIndex, pageSize);
        return goodsMapper.selectByExample(goodsExample);
    }

    public GoodsDetailVO getGoodsDetail(Integer id) {
        List<GoodsDetailVO> goodsDetailById = shopMapper.getGoodsDetailById(id);
        if(goodsDetailById!=null && goodsDetailById.size()==1) {
            return goodsDetailById.get(0);
        }else {
            throw new RuntimeException("没有该商品");
        }
    }

    public List<Goods> getHotList(String show) {
        String hotStr = redisTemplate.opsForValue().get(RedisKeyUtil.getHotListKey(show));
        if(!StringUtils.isEmpty(hotStr)) {
            List<Goods> list = new ArrayList<>();
            JSONArray jsonArray = JSONArray.parseArray(hotStr);
            for(int i=0; i<jsonArray.size(); i++) {
                Goods goods = JSON.parseObject(jsonArray.get(i).toString(), Goods.class);
                list.add(goods);
            }
            return list;
        }else {
            GoodsExample goodsExample = new GoodsExample();
            goodsExample.createCriteria().andStateEqualTo(ConstantUtil.STATE_VALID).andShowStateEqualTo(show);
            goodsExample.setOrderByClause(" goods_type asc, id asc ");
            List<Goods> goods = goodsMapper.selectByExample(goodsExample);
            List<String> hotList = new ArrayList<>();
            for (Goods item : goods) {
                hotList.add(JSON.toJSONString(item));
            }
            redisTemplate.opsForValue().set(RedisKeyUtil.getHotListKey(show), JSON.toJSONString(hotList));
            return goods;
        }
    }

    //to redis
    @Transactional
    public void addGoodsToCart(Integer userId, Integer goodsId, Integer num) {
        Object hasNum = redisTemplate.opsForHash().get(RedisKeyUtil.getCartKey(userId), String.valueOf(goodsId));
        if(hasNum==null) {//购物车没有该商品
            redisTemplate.opsForHash().put(RedisKeyUtil.getCartKey(userId), String.valueOf(goodsId), String.valueOf(num));
            redisTemplate.expire(RedisKeyUtil.getCartKey(userId), 10, TimeUnit.DAYS);//10天有效
        }else { //已有，increment
            redisTemplate.opsForHash().increment(RedisKeyUtil.getCartKey(userId), String.valueOf(goodsId), num);
            redisTemplate.expire(RedisKeyUtil.getCartKey(userId), 10, TimeUnit.DAYS);
        }
    }

    public List<ViewObject> getCartInfo(Integer userId) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(RedisKeyUtil.getCartKey(userId));
        redisTemplate.expire(RedisKeyUtil.getCartKey(userId), 5, TimeUnit.DAYS);
        if(entries!=null && entries.size()>0) {
            Map<Integer, Integer> idNumMap = new HashMap<>();
            for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                idNumMap.put(Integer.valueOf(entry.getKey().toString()), Integer.valueOf(entry.getValue().toString()));
            }
            GoodsExample goodsExample = new GoodsExample();
            goodsExample.createCriteria().andIdIn(new ArrayList<>(idNumMap.keySet()));
            List<Goods> goodsList = goodsMapper.selectByExample(goodsExample);
            List<ViewObject> vos = new ArrayList<>();
            for (Goods goods : goodsList) {
                ViewObject vo = new ViewObject();
                vo.set("goods", goods);
                vo.set("num", idNumMap.get(goods.getId()));
                vos.add(vo);
            }
            return vos;
        }
        return null;
    }

    @Transactional
    public void updateCart(Integer userId, String cartInfo) {
        JSONObject jsonObject = JSONObject.parseObject(cartInfo);
        Set<Map.Entry<String, Object>> set = jsonObject.entrySet();
        for(Map.Entry<String, Object> map: set) {
            redisTemplate.opsForHash().put(RedisKeyUtil.getCartKey(userId), map.getKey(), map.getValue());
            redisTemplate.expire(RedisKeyUtil.getCartKey(userId), 10, TimeUnit.DAYS);
        }
    }

    @Transactional
    public void cartTrade(Integer buyerId, String buyerUid, String cartStr, String name, String phone, String address, BigDecimal totalPrice) {
        if(!StringUtils.isEmpty(cartStr)) {
            JSONObject jsonObject = JSONObject.parseObject(cartStr);
            Set<Map.Entry<String, Object>> set = jsonObject.entrySet();
            Map<Integer, Integer> idNumMap = new HashMap<>();
            for(Map.Entry<String, Object> map: set) {
                idNumMap.put(Integer.valueOf(map.getKey()), Integer.valueOf(map.getValue().toString()));
            }
            if (idNumMap.size() <= 0) {
                log.error("购物车信息错误1- buyerId:{}, cartStr:{}, name:{}, phone:{}, address:{}", buyerId, cartStr, name, phone, address);
                throw new RuntimeException("购物车信息错误");
            }
            GoodsExample goodsExample = new GoodsExample();
            goodsExample.createCriteria().andIdIn(new ArrayList<>(idNumMap.keySet()));
            List<Goods> goodsList = goodsMapper.selectByExample(goodsExample);//购买所有物品list
            BigDecimal calcTotal = BigDecimal.ZERO;
            Date now = new Date();
            for (Goods goods : goodsList) {
                GoodsTrade goodsTrade = new GoodsTrade();
                goodsTrade.setDate(now);
                goodsTrade.setBuyerId(buyerId);
                goodsTrade.setBuyerUid(buyerUid);

                goodsTrade.setGoodsId(goods.getId());//goods
                goodsTrade.setGoodsName(goods.getName());
                goodsTrade.setGoodsPrice(goods.getPrice());
                goodsTrade.setNum(idNumMap.get(goods.getId()));//num
                calcTotal = calcTotal.add(goodsTrade.getGoodsPrice().multiply(BigDecimal.valueOf(goodsTrade.getNum())));
                goodsTrade.setName(name);
                goodsTrade.setPhone(phone);
                goodsTrade.setAddress(address);
                goodsTrade.setCart(cartStr);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String format = sdf.format(now);
                goodsTrade.setRemark(buyerId + "-" + cartStr + "-" + format);
                insertGoodsTrade(goodsTrade);
            }
            if(calcTotal.compareTo(totalPrice)!=0) {
                log.error("购物车信息错误-总价不符- buyerId:{}, cartStr:{}, totalPrice:{}", buyerId, cartStr, totalPrice);
//                throw new RuntimeException("购物车信息错误");
            }
            userService.userBuyCart(buyerId, calcTotal);
            clearCart(buyerId);
        }else {
            log.error("购物车信息错误2-cartStr- buyerId:{}, cartStr:{}, name:{}, phone:{}, address:{}", buyerId, cartStr, name, phone, address);
            throw new RuntimeException("购物车信息错误");
        }
    }

    @Transactional
    public void clearCart(Integer userId) {
        redisTemplate.delete(RedisKeyUtil.getCartKey(userId));
        log.info("clear cart, userId:{}", userId);
    }

    @Transactional
    public void insertGoodsTrade(GoodsTrade goodsTrade) {
        goodsTrade.setState(ConstantUtil.STATE_VALID);
        goodsTradeMapper.insertSelective(goodsTrade);
        log.info("insert goodsTrade:{}", JSON.toJSONString(goodsTrade));
    }

    public List<GoodsTrade> getShopRecord(Integer userId, String userUid, String state, Integer pageIndex, Integer pageSize) {
        GoodsTradeExample goodsTradeExample = new GoodsTradeExample();
        GoodsTradeExample.Criteria criteria = goodsTradeExample.createCriteria();
        if(userId!= null) {
            criteria.andBuyerIdEqualTo(userId);
        }
        if(userUid!=null && !StringUtils.isEmpty(userUid)) {//模糊查询
            userUid = userUid + "%";
            criteria.andBuyerUidLike(userUid);
        }
        if(state!=null && !StringUtils.isEmpty(state)) {
            criteria.andStateEqualTo(state);
        }
        goodsTradeExample.setOrderByClause(" date desc, goods_id asc ");
        PageHelper.startPage(pageIndex, pageSize);
        return goodsTradeMapper.selectByExample(goodsTradeExample);
    }

    public List<GoodsTrade> getAllShopRecord(Integer userId, String userUid, String state, String days1, String days2, String goodsName, Integer pageIndex, Integer pageSize)  {
        GoodsTradeExample goodsTradeExample = new GoodsTradeExample();
        GoodsTradeExample.Criteria criteria = goodsTradeExample.createCriteria();
        if(!StringUtils.isEmpty(userId)) {
            criteria.andBuyerIdEqualTo(userId);
        }
        if(!StringUtils.isEmpty(userUid)) {//模糊查询
            userUid = userUid + "%";
            criteria.andBuyerUidLike(userUid);
        }
        if(!StringUtils.isEmpty(state)) {
            criteria.andStateEqualTo(state);
        }
        if(!StringUtils.isEmpty(goodsName)) {
            criteria.andGoodsNameLike("%" + goodsName + "%");
        }
        if(!StringUtils.isEmpty(days1)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
            Date date1 = null;
            try {
                date1 = sdf.parse(days1);
            } catch (ParseException e) {
                log.error(e.getMessage());
            }
            criteria.andDateGreaterThanOrEqualTo(date1);
        }
        if(!StringUtils.isEmpty(days2)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
            Date date2 = null;
            try {
                date2 = sdf.parse(days2);
            } catch (ParseException e) {
                log.error(e.getMessage());
            }
            criteria.andDateLessThanOrEqualTo(date2);
        }
        goodsTradeExample.setOrderByClause(" date desc, goods_id asc ");
        PageHelper.startPage(pageIndex, pageSize);
        return goodsTradeMapper.selectByExample(goodsTradeExample);
    }

    public List<Goods> getGoods(Integer type, String state, String name, Integer pageIndex, Integer pageSize) {
        GoodsExample goodsExample = new GoodsExample();
        GoodsExample.Criteria criteria = goodsExample.createCriteria();
        if(type!=null ) {
            criteria.andGoodsTypeEqualTo(type);
        }
        if(state!=null && Strings.isNotBlank(state)) {
            criteria.andStateEqualTo(state);
        }
        if(name!=null && Strings.isNotBlank(name)) {
            criteria.andNameLike(name + "%");
        }
        goodsExample.setOrderByClause(" price desc");
        PageHelper.startPage(pageIndex, pageSize);
        return goodsMapper.selectByExample(goodsExample);
    }

    @Transactional
    public void confirmAndSendGoods(List<Long> goodsTradeIds) {
        GoodsTradeExample goodsTradeExample = new GoodsTradeExample();
        goodsTradeExample.createCriteria().andIdIn(goodsTradeIds);
        List<GoodsTrade> goodsTrades = goodsTradeMapper.selectByExample(goodsTradeExample);
        for(GoodsTrade trade : goodsTrades) {
            if(trade.getState().equals(ConstantUtil.STATE_VALID)) {
                trade.setState(ConstantUtil.STATE_SHOP_SENT);
                int num = goodsTradeMapper.updateByPrimaryKeySelective(trade);
                if (num != 1) {
                    log.error("更新错误, num:{}, trade.id():{}", num, trade.getId());
                    throw new RuntimeException("更新错误");
                }
            }else {
                log.error("更新错误,交易状态错误，trade.getState():{}, trade.id():{}", trade.getState(), trade.getId());
                throw new RuntimeException("更新错误，交易状态错误");
            }
        }
    }

    @Transactional
    public void lackGoods(List<Long> goodsTradeIds) {
        GoodsTradeExample goodsTradeExample = new GoodsTradeExample();
        goodsTradeExample.createCriteria().andIdIn(goodsTradeIds);
        List<GoodsTrade> goodsTrades = goodsTradeMapper.selectByExample(goodsTradeExample);
        for(GoodsTrade trade : goodsTrades) {
            if(trade.getState().equals(ConstantUtil.STATE_VALID)) {
                trade.setState(ConstantUtil.STATE_SHOP_LACK);
                int num = goodsTradeMapper.updateByPrimaryKeySelective(trade);
                if (num != 1) {
                    log.error("更新错误, num:{}, trade.id():{}", num, trade.getId());
                    throw new RuntimeException("更新错误");
                }
                User buyer = userService.getUserById(trade.getBuyerId());
                buyer.setShopPoints(buyer.getShopPoints().add(trade.getGoodsPrice().multiply(BigDecimal.valueOf(trade.getNum()))));
                userService.updateUser(buyer);
            }else {
                log.error("更新错误,交易状态错误，trade.getState():{}, trade.id():{}", trade.getState(), trade.getId());
                throw new RuntimeException("更新错误，交易状态错误");
            }
        }
    }

    public void validateGoods(List<Integer> ids) {
        GoodsExample goodsExample = new GoodsExample();
        goodsExample.createCriteria().andIdIn(ids);
        List<Goods> goodsList = goodsMapper.selectByExample(goodsExample);
        for(Goods g : goodsList) {
            if(!g.getState().equals(ConstantUtil.STATE_VALID)) {
                g.setState(ConstantUtil.STATE_VALID);
                int num = goodsMapper.updateByPrimaryKeySelective(g);
                if (num != 1) {
                    log.error("更新错误, num:{}, trade.id():{}", num, g.getId());
                    throw new RuntimeException("更新错误");
                }
            }
        }
    }

    public void invalidateGoods(List<Integer> ids) {
        GoodsExample goodsExample = new GoodsExample();
        goodsExample.createCriteria().andIdIn(ids);
        List<Goods> goodsList = goodsMapper.selectByExample(goodsExample);
        for(Goods g : goodsList) {
            if(!g.getState().equals(ConstantUtil.STATE_INVALID)) {
                g.setState(ConstantUtil.STATE_INVALID);
                int num = goodsMapper.updateByPrimaryKeySelective(g);
                if (num != 1) {
                    log.error("更新错误, num:{}, trade.id():{}", num, g.getId());
                    throw new RuntimeException("更新错误");
                }
            }
        }
    }

    public List<GoodsStatistic>  goodsStatistic(String days1, String days2, String goodsName, Integer goodsType, Integer pageIndex, Integer pageSize) {
        if(goodsName!=null && !StringUtils.isEmpty(goodsName)) {
            goodsName = "%" + goodsName + "%";
        }
        PageHelper.startPage(pageIndex, pageSize);
        return shopMapper.getStatisticData(days1, days2, goodsName, goodsType);

    }
}

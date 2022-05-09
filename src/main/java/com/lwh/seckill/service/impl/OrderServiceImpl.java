package com.lwh.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lwh.seckill.entity.Order;
import com.lwh.seckill.entity.SeckillGoods;
import com.lwh.seckill.entity.SeckillOrder;
import com.lwh.seckill.entity.User;
import com.lwh.seckill.exception.GlobalException;
import com.lwh.seckill.mapper.OrderMapper;
import com.lwh.seckill.service.IGoodsService;
import com.lwh.seckill.service.IOrderService;
import com.lwh.seckill.service.ISeckillGoodsService;
import com.lwh.seckill.service.ISeckillOrderService;
import com.lwh.seckill.utils.JsonUtil;
import com.lwh.seckill.utils.MD5Util;
import com.lwh.seckill.utils.UUIDUtil;
import com.lwh.seckill.vo.GoodsVo;
import com.lwh.seckill.vo.OrderDetailVo;
import com.lwh.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lwh
 * @since 2021-11-04
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired(required = false)
    OrderMapper orderMapper;
    @Autowired
    ISeckillOrderService seckillOrderService;
   @Autowired
    IGoodsService goodsService;
   @Autowired
    RedisTemplate redisTemplate;
    @Override
    public Order seckill(User user, GoodsVo goodsVo) {


        ValueOperations valueOperations = redisTemplate.opsForValue();
//        秒杀商品表减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goodsVo.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
//       减库存更新到数据库的时候,判断一下库存的是否>0,大于0才进行更新
        seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count="+"stock_count-1").eq("goods_id",goodsVo.getId()).gt("stock_count",0));
        if (seckillGoods.getStockCount()<1){
//            判断是否还有库存
            valueOperations.set("isStockEmpty:"+goodsVo.getId(),"0");
            return null;
        }
        //        生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);

        order.setGoodsPrice(seckillGoods.getMiaoshaPrice());
        order.setOrderChannel(1);//pc购买
        order.setStatus(0);//订单创建未支付
        order.setCreateDate(new Date());
        orderMapper.insert(order);
//       生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();

        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+ goodsVo.getId(), JsonUtil.obj2JsonStr(seckillOrder));


        return order;
    }

    @Override
    public OrderDetailVo detail(Long orderId,User user) {
        if(null==orderId){
            throw new GlobalException(RespBeanEnum.ORDER_NOTEXIST);
        }
        Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("goods_id", orderId).eq("user_id",user.getId()));
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detailVo = new OrderDetailVo();
        detailVo.setOrder(order);
        detailVo.setGoodsVo(goodsVo);

        return detailVo;
    }

    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid());
        redisTemplate.opsForValue().set("seckillPath:"+user.getId()+goodsId,str,600, TimeUnit.SECONDS);
        return str;
    }

    @Override
    public boolean checkPath(User user, Long goodsId,String path) {
        if(user==null|| goodsId<0||path==null){
            return false;
        }
        String realPath= (String) redisTemplate.opsForValue().get("seckillPath:"+user.getId()+goodsId);
        System.out.println(realPath+'-'+path);
        System.out.println(path);
        System.out.println(realPath.equals(path));
        return path.equals(path);
    }

    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if (user==null|| StringUtils.isEmpty(captcha)||goodsId<0){
            return false;
        }
        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        return captcha.equals(redisCaptcha);
    }
}

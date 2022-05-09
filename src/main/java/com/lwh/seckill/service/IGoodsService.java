package com.lwh.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lwh.seckill.entity.Goods;
import com.lwh.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lwh
 * @since 2021-11-04
 */
public interface IGoodsService extends IService<Goods> {
/*获取商品列表*/
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}

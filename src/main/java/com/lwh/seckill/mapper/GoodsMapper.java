package com.lwh.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lwh.seckill.entity.Goods;
import com.lwh.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lwh
 * @since 2021-11-04
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVo> findGoodsVo();

/*获取商品详情*/
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}

package net.jjjshop.shop.mapper.statistics;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.shop.vo.statistics.ProductSaleRankingVo;
import net.jjjshop.shop.vo.statistics.ProductRefundRankingVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRankingMapper extends BaseMapper {

    /**
     * 获取商品销售排行
     * @param
     * @return
     */
    List<ProductSaleRankingVo> getSaleRanking();

    /**
     * 获取商品售后排行
     * @param
     * @return
     */
    List<ProductRefundRankingVo> getRefundRanking();

}

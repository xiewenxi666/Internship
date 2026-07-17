package com.meession.etm.module.crm.event.listener;

import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.business.CrmBusinessDO;
import com.meession.etm.module.crm.dal.dataobject.business.CrmBusinessProductDO;
import com.meession.etm.module.crm.event.CrmBusinessWonEvent;
import com.meession.etm.module.crm.service.business.CrmBusinessService;
import com.meession.etm.module.crm.service.order.CrmOrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商机成交事件监听器
 *
 * 监听 {@link CrmBusinessWonEvent}，自动从商机创建草稿订单
 *
 * @author 23计三倪雨晗
 */
@Slf4j
@Component
public class CrmBusinessWonListener {

    @Resource
    private CrmBusinessService businessService;
    @Resource
    private CrmOrderService orderService;

    @EventListener
    public void onBusinessWon(CrmBusinessWonEvent event) {
        Long businessId = event.getBusinessId();
        log.info("[onBusinessWon][商机成交, businessId={}]", businessId);

        CrmBusinessDO business = businessService.getBusiness(businessId);
        if (business == null) {
            log.warn("[onBusinessWon][商机不存在, businessId={}]", businessId);
            return;
        }

        List<CrmBusinessProductDO> businessProducts = businessService.getBusinessProductListByBusinessId(businessId);

        CrmOrderSaveReqVO reqVO = new CrmOrderSaveReqVO();
        reqVO.setName(business.getName());
        reqVO.setCustomerId(business.getCustomerId());
        reqVO.setBusinessId(businessId);
        reqVO.setOwnerUserId(business.getOwnerUserId());
        reqVO.setOrderDate(LocalDateTime.now());
        reqVO.setDiscountPercent(business.getDiscountPercent() != null ? business.getDiscountPercent() : java.math.BigDecimal.ZERO);
        reqVO.setRemark(business.getRemark());

        if (businessProducts != null && !businessProducts.isEmpty()) {
            List<CrmOrderSaveReqVO.Product> products = businessProducts.stream()
                    .map(bp -> new CrmOrderSaveReqVO.Product(
                            bp.getProductId(),
                            bp.getProductPrice(),
                            bp.getBusinessPrice(),
                            bp.getCount().intValue()))
                    .toList();
            reqVO.setProducts(products);
        }

        Long orderId = orderService.createOrder(reqVO, business.getOwnerUserId());
        log.info("[onBusinessWon][订单创建成功, orderId={}, businessId={}]", orderId, businessId);
    }

}

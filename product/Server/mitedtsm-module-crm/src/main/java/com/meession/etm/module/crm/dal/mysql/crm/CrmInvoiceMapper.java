package com.meession.etm.module.crm.dal.mysql.crm;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.crm.invoice.vo.CrmInvoicePageReqVO;
import com.meession.etm.module.crm.dal.dataobject.crm.CrmInvoiceDO;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.util.CrmPermissionUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 发票 Mapper
 *
 * @author 赤焰
 */
@Mapper
public interface CrmInvoiceMapper extends BaseMapperX<CrmInvoiceDO> {

    default CrmInvoiceDO selectByNo(String no) {
        return selectOne(CrmInvoiceDO::getNo, no);
    }

    default PageResult<CrmInvoiceDO> selectPage(CrmInvoicePageReqVO pageReqVO, Long userId) {
        MPJLambdaWrapperX<CrmInvoiceDO> query = new MPJLambdaWrapperX<>();
        CrmPermissionUtils.appendPermissionCondition(query, CrmBizTypeEnum.CRM_INVOICE.getType(),
                CrmInvoiceDO::getId, userId, pageReqVO.getSceneType());
        query.selectAll(CrmInvoiceDO.class)
                .eqIfPresent(CrmInvoiceDO::getNo, pageReqVO.getNo())
                .eqIfPresent(CrmInvoiceDO::getOrderNo, pageReqVO.getOrderNo())
                .eqIfPresent(CrmInvoiceDO::getType, pageReqVO.getType())
                .betweenIfPresent(CrmInvoiceDO::getInvoiceDate, pageReqVO.getInvoiceDateStart(), pageReqVO.getInvoiceDateEnd())
                .eqIfPresent(CrmInvoiceDO::getOwnerUserId, pageReqVO.getOwnerUserId())
                .eqIfPresent(CrmInvoiceDO::getHandlerUserId, pageReqVO.getHandlerUserId())
                .orderByDesc(CrmInvoiceDO::getId);
        return selectJoinPage(pageReqVO, CrmInvoiceDO.class, query);
    }

    default List<CrmInvoiceDO> selectListForReport(Integer year, Long ownerUserId) {
        LambdaQueryWrapperX<CrmInvoiceDO> query = new LambdaQueryWrapperX<CrmInvoiceDO>()
                .orderByDesc(CrmInvoiceDO::getId);
        if (ownerUserId != null) {
            query.eq(CrmInvoiceDO::getOwnerUserId, ownerUserId);
        }
        if (year != null) {
            query.apply("YEAR(invoice_date) = {0}", year);
        }
        return selectList(query);
    }

}

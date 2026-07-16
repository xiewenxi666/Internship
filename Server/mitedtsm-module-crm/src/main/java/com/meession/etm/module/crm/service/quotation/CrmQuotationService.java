package com.meession.etm.module.crm.service.quotation;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.quotation.vo.quotation.CrmQuotationPageReqVO;
import com.meession.etm.module.crm.controller.admin.quotation.vo.quotation.CrmQuotationSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.quotation.CrmQuotationDO;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;

/**
 * CRM 报价单 Service 接口
 *
 * @author engineer
 */
public interface CrmQuotationService {

    /**
     * 创建报价单
     *
     * @param createReqVO 创建信息
     * @return 报价单ID
     */
    Long createQuotation(@Valid CrmQuotationSaveReqVO createReqVO);

    /**
     * 更新报价单
     *
     * @param updateReqVO 更新信息
     */
    void updateQuotation(@Valid CrmQuotationSaveReqVO updateReqVO);

    /**
     * 删除报价单
     *
     * @param id 报价单ID
     */
    void deleteQuotation(Long id);

    /**
     * 获得报价单
     *
     * @param id 报价单ID
     * @return 报价单
     */
    CrmQuotationDO getQuotation(Long id);

    /**
     * 获得报价单分页
     *
     * @param pageReqVO 分页查询
     * @return 报价单分页
     */
    PageResult<CrmQuotationDO> getQuotationPage(CrmQuotationPageReqVO pageReqVO);

    /**
     * 获得报价单列表（根据商机ID）
     *
     * @param businessId 商机ID
     * @return 报价单列表
     */
    List<CrmQuotationDO> getQuotationListByBusinessId(Long businessId);

    /**
     * 获得报价单列表（根据客户ID）
     *
     * @param customerId 客户ID
     * @return 报价单列表
     */
    List<CrmQuotationDO> getQuotationListByCustomerId(Long customerId);

    /**
     * 确认报价（状态变更为已通过）
     *
     * @param id 报价单ID
     * @param auditUserId 审批人ID
     * @param auditRemark 审批备注
     */
    void confirmQuotation(Long id, Long auditUserId, String auditRemark);

    /**
     * 拒绝报价（状态变更为已拒绝）
     *
     * @param id 报价单ID
     * @param auditUserId 审批人ID
     * @param auditRemark 审批备注
     */
    void rejectQuotation(Long id, Long auditUserId, String auditRemark);

    /**
     * 作废报价（状态变更为已作废）
     *
     * @param id 报价单ID
     */
    void voidQuotation(Long id);

    /**
     * 提交报价审批（草稿→待审批）
     *
     * @param id 报价单ID
     */
    void submitQuotation(Long id);

    /**
     * 获得报价单数量（根据商机ID）
     *
     * @param businessId 商机ID
     * @return 数量
     */
    Long getQuotationCountByBusinessId(Long businessId);

}

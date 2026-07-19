package com.meession.etm.module.crm.service.refund;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.refund.vo.refund.CrmRefundApprovalPageReqVO;
import com.meession.etm.module.crm.controller.admin.refund.vo.refund.CrmRefundPageReqVO;
import com.meession.etm.module.crm.controller.admin.refund.vo.refund.CrmRefundReportReqVO;
import com.meession.etm.module.crm.controller.admin.refund.vo.refund.CrmRefundSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.dal.dataobject.refund.CrmRefundDO;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;

/**
 * CRM 退款 Service 接口
 *
 * @author 赤焰
 */
public interface CrmRefundService {

    /**
     * 创建退款
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createRefund(@Valid CrmRefundSaveReqVO createReqVO);

    /**
     * 更新退款
     *
     * @param updateReqVO 更新信息
     */
    void updateRefund(@Valid CrmRefundSaveReqVO updateReqVO);

    /**
     * 更新退款流程审批结果
     *
     * @param id        退款编号
     * @param bpmResult BPM 审批结果
     */
    void updateRefundAuditStatus(Long id, Integer bpmResult);

    /**
     * 删除退款
     *
     * @param id 编号
     */
    void deleteRefund(Long id);

    /**
     * 发起退款审批流程
     *
     * @param id     退款编号
     * @param userId 用户编号
     */
    void submitRefund(Long id, Long userId);

    /**
     * 获得退款
     *
     * @param id 编号
     * @return 退款
     */
    CrmRefundDO getRefund(Long id);

    /**
     * 获得退款列表
     *
     * @param ids 编号
     * @return 退款列表
     */
    List<CrmRefundDO> getRefundList(Collection<Long> ids);

    /**
     * 获得退款分页
     *
     * 数据权限：基于 {@link CrmRefundDO} 读取
     *
     * @param pageReqVO 分页查询
     * @param userId    用户编号
     * @return 退款分页
     */
    PageResult<CrmRefundDO> getRefundPage(CrmRefundPageReqVO pageReqVO, Long userId);

    /**
     * 获得退款分页，基于指定客户
     *
     * 数据权限：基于 {@link CrmCustomerDO} 读取
     *
     * @param pageReqVO 分页查询
     * @return 退款分页
     */
    PageResult<CrmRefundDO> getRefundPageByCustomerId(CrmRefundPageReqVO pageReqVO);

    /**
     * 获得待审核退款数量
     *
     * @param userId 用户编号
     * @return 待审批数量
     */
    Long getAuditRefundCount(Long userId);

    /**
     * 获得退款记录报表分页
     *
     * @param reqVO 查询条件
     * @return 退款分页
     */
    PageResult<CrmRefundDO> getRefundReport(CrmRefundReportReqVO reqVO);

    /**
     * 撤销退款审批
     *
     * @param id     退款编号
     * @param reason 撤销原因
     */
    void cancelRefund(Long id, String reason);

    /**
     * 获得退款审批分页
     *
     * @param pageReqVO 分页查询
     * @param userId    用户编号
     * @return 退款分页
     */
    PageResult<CrmRefundDO> getRefundApprovalPage(CrmRefundApprovalPageReqVO pageReqVO, Long userId);

}

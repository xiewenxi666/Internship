package com.meession.etm.module.crm.service.reimbursement;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.reimbursement.CrmReimbursementApprovalPageReqVO;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.reimbursement.CrmReimbursementPageReqVO;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.reimbursement.CrmReimbursementSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.dal.dataobject.reimbursement.CrmReimbursementDO;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * CRM 报销 Service 接口
 *
 * @author 赤焰
 */
public interface CrmReimbursementService {

    /**
     * 创建报销
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createReimbursement(@Valid CrmReimbursementSaveReqVO createReqVO);

    /**
     * 更新报销
     *
     * @param updateReqVO 更新信息
     */
    void updateReimbursement(@Valid CrmReimbursementSaveReqVO updateReqVO);

    /**
     * 更新报销流程审批结果
     *
     * @param id        报销编号
     * @param bpmResult BPM 审批结果
     */
    void updateReimbursementAuditStatus(Long id, Integer bpmResult);

    /**
     * 删除报销
     *
     * @param id 编号
     */
    void deleteReimbursement(Long id);

    /**
     * 发起报销审批流程
     *
     * @param id     报销编号
     * @param userId 用户编号
     */
    void submitReimbursement(Long id, Long userId);

    /**
     * 获得报销
     *
     * @param id 编号
     * @return 报销
     */
    CrmReimbursementDO getReimbursement(Long id);

    /**
     * 获得报销列表
     *
     * @param ids 编号
     * @return 报销列表
     */
    List<CrmReimbursementDO> getReimbursementList(Collection<Long> ids);

    /**
     * 获得报销 Map
     *
     * @param ids 编号
     * @return 报销 Map
     */
    default Map<Long, CrmReimbursementDO> getReimbursementMap(Collection<Long> ids) {
        return convertMap(getReimbursementList(ids), CrmReimbursementDO::getId);
    }

    /**
     * 获得报销分页
     *
     * 数据权限：基于 {@link CrmReimbursementDO} 读取
     *
     * @param pageReqVO 分页查询
     * @param userId    用户编号
     * @return 报销分页
     */
    PageResult<CrmReimbursementDO> getReimbursementPage(CrmReimbursementPageReqVO pageReqVO, Long userId);

    /**
     * 获得报销分页，基于指定客户
     *
     * 数据权限：基于 {@link CrmCustomerDO} 读取
     *
     * @param pageReqVO 分页查询
     * @return 报销分页
     */
    PageResult<CrmReimbursementDO> getReimbursementPageByCustomerId(CrmReimbursementPageReqVO pageReqVO);

    /**
     * 获得待审核报销数量
     *
     * @param userId 用户编号
     * @return 待审批数量
     */
    Long getAuditReimbursementCount(Long userId);

    /**
     * 撤销报销审批
     *
     * @param id     报销编号
     * @param reason 撤销原因
     */
    void cancelReimbursement(Long id, String reason);

    /**
     * 获得报销审批分页
     *
     * @param pageReqVO 分页查询
     * @param userId    用户编号
     * @return 报销分页
     */
    PageResult<CrmReimbursementDO> getReimbursementApprovalPage(CrmReimbursementApprovalPageReqVO pageReqVO, Long userId);

    /**
     * 审批通过报销
     *
     * @param id     报销编号
     * @param reason 审批意见
     */
    void approveReimbursement(Long id, String reason);

    /**
     * 审批驳回报销
     *
     * @param id     报销编号
     * @param reason 驳回原因
     */
    void rejectReimbursement(Long id, String reason);

    /**
     * 审批否决报销
     *
     * @param id     报销编号
     * @param reason 否决原因
     */
    void vetoReimbursement(Long id, String reason);

}

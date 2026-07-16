// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.service.workorder;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.workorder.vo.CrmWorkOrderPageReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.CrmWorkOrderSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;
import com.meession.etm.module.crm.dal.mysql.workorder.CrmWorkOrderMapper;
import com.meession.etm.module.crm.dal.redis.no.CrmBizNoPrefix;
import com.meession.etm.module.crm.dal.redis.no.CrmNoRedisDAO;
import com.meession.etm.module.crm.enums.workorder.CrmWorkOrderStatusEnum;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.*;

@Service
@Validated
public class CrmWorkOrderServiceImpl implements CrmWorkOrderService {

    @Resource
    private CrmWorkOrderMapper workOrderMapper;

    @Resource
    private CrmNoRedisDAO noRedisDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "CRM_WORK_ORDER", subType = "CREATE",
            success = "创建了工单【{{#title}}】：#{getWorkOrderById(#result)}",
            bizNo = "{{#result}}")
    public Long createWorkOrder(CrmWorkOrderSaveReqVO createReqVO) {
        CrmWorkOrderDO workOrder = new CrmWorkOrderDO();
        workOrder.setNo(noRedisDAO.generate(CrmBizNoPrefix.WORK_ORDER));
        workOrder.setTitle(createReqVO.getTitle());
        workOrder.setType(createReqVO.getType());
        workOrder.setPriority(createReqVO.getPriority());
        workOrder.setStatus(CrmWorkOrderStatusEnum.INITIATED.getStatus());
        workOrder.setOwnerUserId(createReqVO.getOwnerUserId());
        workOrder.setDescription(createReqVO.getDescription());
        workOrder.setStartTime(createReqVO.getStartTime());
        workOrder.setEndTime(createReqVO.getEndTime());
        workOrderMapper.insert(workOrder);
        // 记录 title 用于日志上下文
        LogRecordContext.putVariable("title", workOrder.getTitle());
        return workOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "CRM_WORK_ORDER", subType = "UPDATE",
             success = "更新了工单【#{getWorkOrderById(#updateReqVO.id)}】",
             bizNo = "{{#updateReqVO.id}}")
    public void updateWorkOrder(CrmWorkOrderSaveReqVO updateReqVO) {
        validateWorkOrderExists(updateReqVO.getId());
        CrmWorkOrderDO updateObj = new CrmWorkOrderDO();
        updateObj.setId(updateReqVO.getId());
        updateObj.setTitle(updateReqVO.getTitle());
        updateObj.setType(updateReqVO.getType());
        updateObj.setPriority(updateReqVO.getPriority());
        updateObj.setOwnerUserId(updateReqVO.getOwnerUserId());
        updateObj.setDescription(updateReqVO.getDescription());
        updateObj.setStartTime(updateReqVO.getStartTime());
        updateObj.setEndTime(updateReqVO.getEndTime());
        workOrderMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "CRM_WORK_ORDER", subType = "DELETE",
             success = "删除了工单【#{getWorkOrderById(#id)}】",
             bizNo = "{{#id}}")
    public void deleteWorkOrder(Long id) {
        validateWorkOrderExists(id);
        workOrderMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "CRM_WORK_ORDER", subType = "PROCESS",
             success = "处理了工单【#{getWorkOrderById(#id)}】",
             bizNo = "{{#id}}")
    public void processWorkOrder(Long id) {
        CrmWorkOrderDO workOrder = validateWorkOrderExists(id);
        if (!CrmWorkOrderStatusEnum.canProcess(workOrder.getStatus())) {
            throw exception(WORK_ORDER_NOT_EXISTS);
        }
        workOrderMapper.updateById(new CrmWorkOrderDO().setId(id).setStatus(CrmWorkOrderStatusEnum.PROCESSING.getStatus()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "CRM_WORK_ORDER", subType = "COMPLETE",
             success = "完结了工单【#{getWorkOrderById(#id)}】，解决方案：{{#solution}}",
             bizNo = "{{#id}}")
    public void completeWorkOrder(Long id, String solution) {
        CrmWorkOrderDO workOrder = validateWorkOrderExists(id);
        if (!CrmWorkOrderStatusEnum.canComplete(workOrder.getStatus())) {
            throw exception(WORK_ORDER_NOT_EXISTS);
        }
        CrmWorkOrderDO updateObj = new CrmWorkOrderDO();
        updateObj.setId(id);
        updateObj.setStatus(CrmWorkOrderStatusEnum.COMPLETED.getStatus());
        updateObj.setSolution(solution);
        workOrderMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "CRM_WORK_ORDER", subType = "RETURN",
             success = "退回了工单【#{getWorkOrderById(#id)}】",
             bizNo = "{{#id}}")
    public void returnWorkOrder(Long id) {
        CrmWorkOrderDO workOrder = validateWorkOrderExists(id);
        if (!CrmWorkOrderStatusEnum.canReturn(workOrder.getStatus())) {
            throw exception(WORK_ORDER_NOT_EXISTS);
        }
        workOrderMapper.updateById(new CrmWorkOrderDO().setId(id).setStatus(CrmWorkOrderStatusEnum.RETURNED.getStatus()));
    }

    @Override
    public CrmWorkOrderDO getWorkOrder(Long id) {
        return validateWorkOrderExists(id);
    }

    @Override
    public PageResult<CrmWorkOrderDO> getWorkOrderPage(CrmWorkOrderPageReqVO pageReqVO) {
        return workOrderMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<CrmWorkOrderDO> getWorkOrderPageByCustomer(CrmWorkOrderPageReqVO pageReqVO) {
        return workOrderMapper.selectPageByCustomer(pageReqVO);
    }

    private CrmWorkOrderDO validateWorkOrderExists(Long id) {
        CrmWorkOrderDO workOrder = workOrderMapper.selectById(id);
        if (workOrder == null) {
            throw exception(WORK_ORDER_NOT_EXISTS);
        }
        return workOrder;
    }
}

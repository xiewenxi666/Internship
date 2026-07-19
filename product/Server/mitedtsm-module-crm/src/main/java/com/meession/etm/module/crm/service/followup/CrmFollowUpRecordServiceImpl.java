package com.meession.etm.module.crm.service.followup;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.followup.vo.CrmFollowUpRecordPageReqVO;
import com.meession.etm.module.crm.controller.admin.followup.vo.CrmFollowUpRecordSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.followup.CrmFollowUpRecordDO;
import com.meession.etm.module.crm.dal.mysql.followup.CrmFollowUpRecordMapper;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.enums.permission.CrmPermissionLevelEnum;
import com.meession.etm.module.crm.framework.permission.core.annotations.CrmPermission;
import com.meession.etm.module.crm.service.business.CrmBusinessService;
import com.meession.etm.module.crm.service.clue.CrmClueService;
import com.meession.etm.module.crm.service.contact.CrmContactService;
import com.meession.etm.module.crm.service.contract.CrmContractService;
import com.meession.etm.module.crm.service.customer.CrmCustomerService;
import com.meession.etm.module.crm.service.order.CrmOrderService;
import com.meession.etm.module.crm.service.followup.bo.CrmFollowUpCreateReqBO;
import com.meession.etm.module.crm.service.permission.CrmPermissionService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.FOLLOW_UP_RECORD_DELETE_DENIED;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.FOLLOW_UP_RECORD_NOT_EXISTS;

/**
 * 跟进记录 Service 实现类
 *
 * @author 密讯
 */
@Service
@Validated
public class CrmFollowUpRecordServiceImpl implements CrmFollowUpRecordService {

    @Resource
    private CrmFollowUpRecordMapper crmFollowUpRecordMapper;

    @Resource
    @Lazy
    private CrmPermissionService permissionService;
    @Resource
    @Lazy
    private CrmBusinessService businessService;
    @Resource
    @Lazy
    private CrmClueService clueService;
    @Resource
    @Lazy
    private CrmContactService contactService;
    @Resource
    @Lazy
    private CrmContractService contractService;
    @Resource
    @Lazy
    private CrmCustomerService customerService;
    @Resource
    @Lazy
    private CrmOrderService orderService;

    /**
     * 创建跟进记录，并更新对应业务实体的跟进信息
     *
     * @param createReqVO 创建请求
     * @return 跟进记录编号
     */
    @Override
    @CrmPermission(bizTypeValue = "#createReqVO.bizType", bizId = "#createReqVO.bizId", level = CrmPermissionLevelEnum.WRITE)
    public Long createFollowUpRecord(CrmFollowUpRecordSaveReqVO createReqVO) {
        // 1. 创建更进记录
        CrmFollowUpRecordDO record = BeanUtils.toBean(createReqVO, CrmFollowUpRecordDO.class);
        crmFollowUpRecordMapper.insert(record);

        // 2. 更新 bizId 对应的记录
        if (ObjUtil.equal(CrmBizTypeEnum.CRM_CUSTOMER.getType(), record.getBizType())) { // 更新客户跟进信息
            customerService.updateCustomerFollowUp(record.getBizId(), record.getNextTime(), record.getContent());
        }
        if (ObjUtil.equal(CrmBizTypeEnum.CRM_BUSINESS.getType(), record.getBizType())) { // 更新商机跟进信息
            businessService.updateBusinessFollowUp(record.getBizId(), record.getNextTime(), record.getContent());
        }
        if (ObjUtil.equal(CrmBizTypeEnum.CRM_CLUE.getType(), record.getBizType())) { // 更新线索跟进信息
            clueService.updateClueFollowUp(record.getBizId(), record.getNextTime(), record.getContent());
        }
        if (ObjUtil.equal(CrmBizTypeEnum.CRM_CONTACT.getType(), record.getBizType())) { // 更新联系人跟进信息
            contactService.updateContactFollowUp(record.getBizId(), record.getNextTime(), record.getContent());
        }
        if (ObjUtil.equal(CrmBizTypeEnum.CRM_CONTRACT.getType(), record.getBizType())) { // 更新合同跟进信息
            contractService.updateContractFollowUp(record.getBizId(), record.getNextTime(), record.getContent());
        }
        if (ObjUtil.equal(CrmBizTypeEnum.CRM_ORDER.getType(), record.getBizType())) { // 更新订单跟进信息
            orderService.updateOrderFollowUp(record.getBizId(), record.getNextTime());
        }

        // 3.1 更新 contactIds 对应的记录，只更新 nextTime
        if (CollUtil.isNotEmpty(createReqVO.getContactIds())) {
            contactService.updateContactContactNextTime(createReqVO.getContactIds(), createReqVO.getNextTime());
        }
        // 3.2 需要更新 businessIds 对应的记录，只更新 nextTime
        if (CollUtil.isNotEmpty(createReqVO.getBusinessIds())) {
            businessService.updateBusinessContactNextTime(createReqVO.getBusinessIds(), createReqVO.getNextTime());
        }
        return record.getId();
    }

    /**
     * 批量创建跟进记录
     *
     * @param list 跟进记录列表
     */
    @Override
    public void createFollowUpRecordBatch(List<CrmFollowUpCreateReqBO> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        crmFollowUpRecordMapper.insertBatch(BeanUtils.toBean(list, CrmFollowUpRecordDO.class));
    }

    /**
     * 删除跟进记录
     *
     * @param id 跟进记录编号
     * @param userId 用户编号
     */
    @Override
    public void deleteFollowUpRecord(Long id, Long userId) {
        // 校验存在
        CrmFollowUpRecordDO followUpRecord = validateFollowUpRecordExists(id);
        // 校验权限
        if (!permissionService.hasPermission(followUpRecord.getBizType(), followUpRecord.getBizId(), userId, CrmPermissionLevelEnum.OWNER)) {
            throw exception(FOLLOW_UP_RECORD_DELETE_DENIED);
        }

        // 删除
        crmFollowUpRecordMapper.deleteById(id);
    }

    /**
     * 根据业务类型和业务编号删除跟进记录
     *
     * @param bizType 业务类型
     * @param bizId 业务编号
     */
    @Override
    public void deleteFollowUpRecordByBiz(Integer bizType, Long bizId) {
        crmFollowUpRecordMapper.deleteByBiz(bizType, bizId);
    }

    /**
     * 校验跟进记录是否存在
     *
     * @param id 跟进记录编号
     * @return 跟进记录
     */
    private CrmFollowUpRecordDO validateFollowUpRecordExists(Long id) {
        CrmFollowUpRecordDO followUpRecord = crmFollowUpRecordMapper.selectById(id);
        if (followUpRecord == null) {
            throw exception(FOLLOW_UP_RECORD_NOT_EXISTS);
        }
        return followUpRecord;
    }

    /**
     * 查询跟进记录详情
     *
     * @param id 跟进记录编号
     * @return 跟进记录
     */
    @Override
    public CrmFollowUpRecordDO getFollowUpRecord(Long id) {
        return crmFollowUpRecordMapper.selectById(id);
    }

    /**
     * 分页查询跟进记录
     *
     * @param pageReqVO 分页请求
     * @return 分页结果
     */
    @Override
    @CrmPermission(bizTypeValue = "#pageReqVO.bizType", bizId = "#pageReqVO.bizId", level = CrmPermissionLevelEnum.READ)
    public PageResult<CrmFollowUpRecordDO> getFollowUpRecordPage(CrmFollowUpRecordPageReqVO pageReqVO) {
        return crmFollowUpRecordMapper.selectPage(pageReqVO);
    }

    /**
     * 根据业务类型和业务编号集合查询跟进记录列表
     *
     * @param bizType 业务类型
     * @param bizIds 业务编号集合
     * @return 跟进记录列表
     */
    @Override
    public List<CrmFollowUpRecordDO> getFollowUpRecordByBiz(Integer bizType, Collection<Long> bizIds) {
        return crmFollowUpRecordMapper.selectListByBiz(bizType, bizIds);
    }

}
package com.meession.etm.module.crm.service.crm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.crm.invoice.vo.CrmInvoicePageReqVO;
import com.meession.etm.module.crm.controller.admin.crm.invoice.vo.CrmInvoiceReportReqVO;
import com.meession.etm.module.crm.controller.admin.crm.invoice.vo.CrmInvoiceSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.crm.CrmInvoiceDO;
import com.meession.etm.module.crm.dal.mysql.crm.CrmInvoiceMapper;
import com.meession.etm.module.crm.dal.redis.no.CrmBizNoPrefix;
import com.meession.etm.module.crm.dal.redis.no.CrmNoRedisDAO;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.enums.permission.CrmPermissionLevelEnum;
import com.meession.etm.module.crm.framework.permission.core.annotations.CrmPermission;
import com.meession.etm.module.crm.service.permission.CrmPermissionService;
import com.meession.etm.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.*;
import static com.meession.etm.module.crm.enums.LogRecordConstants.*;

/**
 * CRM 发票 Service 实现类
 *
 * @author 赤焰
 */
@Service
@Validated
@Slf4j
public class CrmInvoiceServiceImpl implements CrmInvoiceService {

    @Resource
    private CrmInvoiceMapper invoiceMapper;

    @Resource
    private CrmNoRedisDAO noRedisDAO;

    @Resource
    private CrmPermissionService permissionService;

    @Resource
    private AdminUserApi adminUserApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_INVOICE_TYPE, subType = CRM_INVOICE_CREATE_SUB_TYPE, bizNo = "{{#invoice.id}}",
            success = CRM_INVOICE_CREATE_SUCCESS)
    public Long createInvoice(CrmInvoiceSaveReqVO createReqVO) {
        // 1. 校验关联数据存在
        validateRelationDataExists(createReqVO);
        // 2. 生成发票编号
        String no = noRedisDAO.generate(CrmBizNoPrefix.INVOICE);
        if (invoiceMapper.selectByNo(no) != null) {
            throw exception(INVOICE_NO_EXISTS);
        }
        // 3. 插入发票
        CrmInvoiceDO invoice = BeanUtils.toBean(createReqVO, CrmInvoiceDO.class).setNo(no);
        invoiceMapper.insert(invoice);
        // 4. 创建数据权限
        permissionService.createPermission(new CrmPermissionCreateReqBO().setBizType(CrmBizTypeEnum.CRM_INVOICE.getType())
                .setBizId(invoice.getId()).setUserId(createReqVO.getOwnerUserId())
                .setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));
        // 5. 记录操作日志上下文
        LogRecordContext.putVariable("invoice", invoice);
        return invoice.getId();
    }

    private void validateRelationDataExists(CrmInvoiceSaveReqVO reqVO) {
        if (reqVO.getOwnerUserId() != null) {
            adminUserApi.validateUser(reqVO.getOwnerUserId());
        }
        if (reqVO.getHandlerUserId() != null) {
            adminUserApi.validateUser(reqVO.getHandlerUserId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_INVOICE_TYPE, subType = CRM_INVOICE_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = CRM_INVOICE_UPDATE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_INVOICE, bizId = "#updateReqVO.id", level = CrmPermissionLevelEnum.WRITE)
    public void updateInvoice(CrmInvoiceSaveReqVO updateReqVO) {
        Assert.notNull(updateReqVO.getId(), "发票编号不能为空");
        // 1. 校验存在
        CrmInvoiceDO oldInvoice = validateInvoiceExists(updateReqVO.getId());
        // 2. 更新发票
        CrmInvoiceDO updateObj = BeanUtils.toBean(updateReqVO, CrmInvoiceDO.class);
        invoiceMapper.updateById(updateObj);
        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("oldInvoice", oldInvoice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_INVOICE_TYPE, subType = CRM_INVOICE_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_INVOICE_DELETE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_INVOICE, bizId = "#id", level = CrmPermissionLevelEnum.OWNER)
    public void deleteInvoice(Long id) {
        // 1. 校验存在
        CrmInvoiceDO invoice = validateInvoiceExists(id);
        // 2. 删除发票
        invoiceMapper.deleteById(id);
        // 3. 删除数据权限
        permissionService.deletePermission(CrmBizTypeEnum.CRM_INVOICE.getType(), id);
        // 4. 记录操作日志上下文
        LogRecordContext.putVariable("invoice", invoice);
    }

    private CrmInvoiceDO validateInvoiceExists(Long id) {
        CrmInvoiceDO invoice = invoiceMapper.selectById(id);
        if (invoice == null) {
            throw exception(INVOICE_NOT_EXISTS);
        }
        return invoice;
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_INVOICE, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public CrmInvoiceDO getInvoice(Long id) {
        return invoiceMapper.selectById(id);
    }

    @Override
    public List<CrmInvoiceDO> getInvoiceList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return invoiceMapper.selectByIds(ids);
    }

    @Override
    public PageResult<CrmInvoiceDO> getInvoicePage(CrmInvoicePageReqVO pageReqVO, Long userId) {
        return invoiceMapper.selectPage(pageReqVO, userId);
    }

    @Override
    public PageResult<CrmInvoiceDO> getInvoiceReport(CrmInvoiceReportReqVO reqVO) {
        List<CrmInvoiceDO> allInvoices = invoiceMapper.selectListForReport(reqVO.getYear(), reqVO.getOwnerUserId());
        int total = allInvoices.size();
        int fromIndex = (reqVO.getPageNo() - 1) * reqVO.getPageSize();
        int toIndex = Math.min(fromIndex + reqVO.getPageSize(), total);
        if (fromIndex >= total) {
            return new PageResult<>(ListUtil.empty(), (long) total);
        }
        return new PageResult<>(allInvoices.subList(fromIndex, toIndex), (long) total);
    }

    @Override
    public List<CrmInvoiceDO> getInvoiceListForExport(CrmInvoicePageReqVO pageReqVO, Long userId) {
        return getInvoicePage(pageReqVO, userId).getList();
    }

}

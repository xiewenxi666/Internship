package com.meession.etm.module.crm.service.crm;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.crm.invoice.vo.CrmInvoicePageReqVO;
import com.meession.etm.module.crm.controller.admin.crm.invoice.vo.CrmInvoiceReportReqVO;
import com.meession.etm.module.crm.controller.admin.crm.invoice.vo.CrmInvoiceSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.crm.CrmInvoiceDO;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * CRM 发票 Service 接口
 *
 * @author 赤焰
 */
public interface CrmInvoiceService {

    /**
     * 创建发票
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createInvoice(@Valid CrmInvoiceSaveReqVO createReqVO);

    /**
     * 更新发票
     *
     * @param updateReqVO 更新信息
     */
    void updateInvoice(@Valid CrmInvoiceSaveReqVO updateReqVO);

    /**
     * 删除发票
     *
     * @param id 编号
     */
    void deleteInvoice(Long id);

    /**
     * 获得发票
     *
     * @param id 编号
     * @return 发票
     */
    CrmInvoiceDO getInvoice(Long id);

    /**
     * 获得发票列表
     *
     * @param ids 编号
     * @return 发票列表
     */
    List<CrmInvoiceDO> getInvoiceList(Collection<Long> ids);

    /**
     * 获得发票 Map
     *
     * @param ids 编号
     * @return 发票 Map
     */
    default Map<Long, CrmInvoiceDO> getInvoiceMap(Collection<Long> ids) {
        return convertMap(getInvoiceList(ids), CrmInvoiceDO::getId);
    }

    /**
     * 获得发票分页
     *
     * 数据权限：基于 {@link CrmInvoiceDO} 读取
     *
     * @param pageReqVO 分页查询
     * @param userId    用户编号
     * @return 发票分页
     */
    PageResult<CrmInvoiceDO> getInvoicePage(CrmInvoicePageReqVO pageReqVO, Long userId);

    /**
     * 获得发票记录报表分页
     *
     * @param reqVO 查询条件
     * @return 发票分页
     */
    PageResult<CrmInvoiceDO> getInvoiceReport(CrmInvoiceReportReqVO reqVO);

    /**
     * 导出发票 Excel
     *
     * @param pageReqVO 查询条件
     * @param userId    用户编号
     * @return 发票列表
     */
    List<CrmInvoiceDO> getInvoiceListForExport(CrmInvoicePageReqVO pageReqVO, Long userId);

}

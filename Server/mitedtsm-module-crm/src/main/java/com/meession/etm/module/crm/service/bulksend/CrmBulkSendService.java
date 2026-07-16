package com.meession.etm.module.crm.service.bulksend;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.bulksend.vo.CrmBulkSendPageReqVO;
import com.meession.etm.module.crm.controller.admin.bulksend.vo.CrmBulkSendSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.bulksend.CrmBulkSendDO;
import jakarta.validation.Valid;

/**
 * 群发管理 Service 接口
 *
 * @author Wanwan
 */
public interface CrmBulkSendService {

    /**
     * 创建群发
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBulkSend(@Valid CrmBulkSendSaveReqVO createReqVO);

    /**
     * 更新群发
     *
     * @param updateReqVO 更新信息
     */
    void updateBulkSend(@Valid CrmBulkSendSaveReqVO updateReqVO);

    /**
     * 删除群发
     *
     * @param id 编号
     */
    void deleteBulkSend(Long id);

    /**
     * 获得群发
     *
     * @param id 编号
     * @return 群发
     */
    CrmBulkSendDO getBulkSend(Long id);

    /**
     * 获得群发分页
     *
     * @param pageReqVO 分页查询
     * @return 群发分页
     */
    PageResult<CrmBulkSendDO> getBulkSendPage(CrmBulkSendPageReqVO pageReqVO);

    /**
     * 提交审核
     *
     * @param id 编号
     */
    void submitForApproval(Long id);

    void approve(Long id);

    void reject(Long id);
}

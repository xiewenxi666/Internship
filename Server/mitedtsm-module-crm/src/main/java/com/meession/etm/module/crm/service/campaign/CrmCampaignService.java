package com.meession.etm.module.crm.service.campaign;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.campaign.vo.CrmCampaignPageReqVO;
import com.meession.etm.module.crm.controller.admin.campaign.vo.CrmCampaignSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.campaign.CrmCampaignDO;
import jakarta.validation.Valid;

/**
 * 营销活动 Service 接口
 *
 * @author Wanwan
 */
public interface CrmCampaignService {

    /**
     * 创建营销活动
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCampaign(@Valid CrmCampaignSaveReqVO createReqVO);

    /**
     * 更新营销活动
     *
     * @param updateReqVO 更新信息
     */
    void updateCampaign(@Valid CrmCampaignSaveReqVO updateReqVO);

    /**
     * 删除营销活动
     *
     * @param id 编号
     */
    void deleteCampaign(Long id);

    /**
     * 获得营销活动
     *
     * @param id 编号
     * @return 营销活动
     */
    CrmCampaignDO getCampaign(Long id);

    /**
     * 获得营销活动分页
     *
     * @param pageReqVO 分页查询
     * @return 营销活动分页
     */
    PageResult<CrmCampaignDO> getCampaignPage(CrmCampaignPageReqVO pageReqVO);

}

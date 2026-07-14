package com.meession.etm.module.crm.service.campaign;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.campaign.vo.CrmCampaignPageReqVO;
import com.meession.etm.module.crm.controller.admin.campaign.vo.CrmCampaignSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.campaign.CrmCampaignDO;
import com.meession.etm.module.crm.dal.mysql.campaign.CrmCampaignMapper;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.CAMPAIGN_NOT_EXISTS;
import static com.meession.etm.module.crm.enums.LogRecordConstants.*;
import static com.meession.etm.module.system.enums.ErrorCodeConstants.USER_NOT_EXISTS;

/**
 * 营销活动 Service 实现类
 *
 * @author Wanwan
 */
@Service
@Validated
public class CrmCampaignServiceImpl implements CrmCampaignService {

    @Resource
    private CrmCampaignMapper campaignMapper;

    @Resource
    private AdminUserApi adminUserApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_CAMPAIGN_TYPE, subType = CRM_CAMPAIGN_CREATE_SUB_TYPE, bizNo = "{{#campaign.id}}",
            success = CRM_CAMPAIGN_CREATE_SUCCESS)
    public Long createCampaign(CrmCampaignSaveReqVO createReqVO) {
        // 1. 校验负责人是否存在
        adminUserApi.validateUser(createReqVO.getOwnerUserId());

        // 2. 插入营销活动
        CrmCampaignDO campaign = BeanUtils.toBean(createReqVO, CrmCampaignDO.class);
        campaignMapper.insert(campaign);

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("campaign", campaign);
        return campaign.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_CAMPAIGN_TYPE, subType = CRM_CAMPAIGN_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = CRM_CAMPAIGN_UPDATE_SUCCESS)
    public void updateCampaign(CrmCampaignSaveReqVO updateReqVO) {
        // 1. 校验营销活动是否存在
        CrmCampaignDO oldCampaign = validateCampaignExists(updateReqVO.getId());
        // 2. 校验负责人是否存在
        adminUserApi.validateUser(updateReqVO.getOwnerUserId());

        // 3. 更新营销活动
        CrmCampaignDO updateObj = BeanUtils.toBean(updateReqVO, CrmCampaignDO.class);
        campaignMapper.updateById(updateObj);

        // 4. 记录操作日志上下文
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(oldCampaign, CrmCampaignSaveReqVO.class));
        LogRecordContext.putVariable("campaignName", oldCampaign.getTitle());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_CAMPAIGN_TYPE, subType = CRM_CAMPAIGN_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_CAMPAIGN_DELETE_SUCCESS)
    public void deleteCampaign(Long id) {
        // 1. 校验营销活动是否存在
        CrmCampaignDO campaign = validateCampaignExists(id);

        // 2. 删除营销活动
        campaignMapper.deleteById(id);

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("campaignName", campaign.getTitle());
    }

    @Override
    public CrmCampaignDO getCampaign(Long id) {
        return campaignMapper.selectById(id);
    }

    @Override
    public PageResult<CrmCampaignDO> getCampaignPage(CrmCampaignPageReqVO pageReqVO) {
        return campaignMapper.selectPage(pageReqVO);
    }

    private CrmCampaignDO validateCampaignExists(Long id) {
        CrmCampaignDO campaignDO = campaignMapper.selectById(id);
        if (campaignDO == null) {
            throw exception(CAMPAIGN_NOT_EXISTS);
        }
        return campaignDO;
    }

}

package com.meession.etm.module.crm.service.common;

import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.business.vo.business.CrmBusinessRespVO;
import com.meession.etm.module.crm.controller.admin.common.vo.CustomerRelationRespVO;
import com.meession.etm.module.crm.controller.admin.contract.vo.contract.CrmContractRespVO;
import com.meession.etm.module.crm.controller.admin.receivable.vo.receivable.CrmReceivableRespVO;
import com.meession.etm.module.crm.dal.dataobject.business.CrmBusinessDO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.receivable.CrmReceivableDO;
import com.meession.etm.module.crm.dal.mysql.business.CrmBusinessMapper;
import com.meession.etm.module.crm.dal.mysql.contract.CrmContractMapper;
import com.meession.etm.module.crm.dal.mysql.receivable.CrmReceivableMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * CRM 跨域关联查询 Service 实现
 * <p>
 * 聚合查询多个 CRM 子模块的数据
 *
 * @author 密讯
 */
@Service
@Slf4j
public class CrmRelationServiceImpl implements CrmRelationService {

    @Resource
    private CrmBusinessMapper businessMapper;
    @Resource
    private CrmContractMapper contractMapper;
    @Resource
    private CrmReceivableMapper receivableMapper;

    @Override
    public CustomerRelationRespVO getCustomerRelation(Long customerId) {
        // 1. 查询关联商机
        List<CrmBusinessDO> businessList = businessMapper.selectList(
                CrmBusinessDO::getCustomerId, customerId);
        List<CrmBusinessRespVO> businessVOList = businessList != null
                ? BeanUtils.toBean(businessList, CrmBusinessRespVO.class)
                : Collections.emptyList();

        // 2. 查询关联合同
        List<CrmContractDO> contractList = contractMapper.selectList(
                CrmContractDO::getCustomerId, customerId);
        List<CrmContractRespVO> contractVOList = contractList != null
                ? BeanUtils.toBean(contractList, CrmContractRespVO.class)
                : Collections.emptyList();

        // 3. 查询关联回款
        List<CrmReceivableDO> receivableList = receivableMapper.selectList(
                CrmReceivableDO::getCustomerId, customerId);
        List<CrmReceivableRespVO> receivableVOList = receivableList != null
                ? BeanUtils.toBean(receivableList, CrmReceivableRespVO.class)
                : Collections.emptyList();

        // 4. 组装返回
        return CustomerRelationRespVO.builder()
                .businessList(businessVOList)
                .contractList(contractVOList)
                .receivableList(receivableVOList)
                // 工单、拜访等由对应域开发后补充查询
                .build();
    }
}

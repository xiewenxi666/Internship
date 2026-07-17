package com.meession.etm.module.crm.service.customercare;

import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.customercare.vo.CrmCustomerCareConfigSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.customercare.CrmCustomerCareConfigDO;
import com.meession.etm.module.crm.dal.mysql.customercare.CrmCustomerCareConfigMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * 客户关怀配置 Service 实现类
 *
 * @author 密讯
 */
@Service
@Validated
public class CrmCustomerCareConfigServiceImpl implements CrmCustomerCareConfigService {

    @Resource
    private CrmCustomerCareConfigMapper customerCareConfigMapper;

    @Override
    public CrmCustomerCareConfigDO getCustomerCareConfig() {
        return customerCareConfigMapper.selectOne();
    }

    @Override
    public void saveCustomerCareConfig(CrmCustomerCareConfigSaveReqVO saveReqVO) {
        // 1. 存在，则进行更新
        CrmCustomerCareConfigDO dbConfig = getCustomerCareConfig();
        CrmCustomerCareConfigDO config = BeanUtils.toBean(saveReqVO, CrmCustomerCareConfigDO.class);
        if (Objects.nonNull(dbConfig)) {
            customerCareConfigMapper.updateById(config.setId(dbConfig.getId()));
            return;
        }

        // 2. 不存在，则进行插入
        customerCareConfigMapper.insert(config);
    }

}

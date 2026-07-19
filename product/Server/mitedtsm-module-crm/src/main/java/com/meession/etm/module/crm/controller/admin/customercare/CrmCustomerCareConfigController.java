package com.meession.etm.module.crm.controller.admin.customercare;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.customercare.vo.CrmCustomerCareConfigRespVO;
import com.meession.etm.module.crm.controller.admin.customercare.vo.CrmCustomerCareConfigSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.customercare.CrmCustomerCareConfigDO;
import com.meession.etm.module.crm.service.customercare.CrmCustomerCareConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.meession.etm.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 客户关怀")
@RestController
@RequestMapping("/crm/customer-care")
@Validated
public class CrmCustomerCareConfigController {

    @Resource
    private CrmCustomerCareConfigService customerCareConfigService;

    @GetMapping("/config")
    @Operation(summary = "获取客户关怀配置")
    @PreAuthorize("@ss.hasPermission('crm:customer-care:query')")
    public CommonResult<CrmCustomerCareConfigRespVO> getConfig() {
        CrmCustomerCareConfigDO config = customerCareConfigService.getCustomerCareConfig();
        return success(BeanUtils.toBean(config, CrmCustomerCareConfigRespVO.class));
    }

    @PostMapping("/save")
    @Operation(summary = "保存客户关怀配置")
    @PreAuthorize("@ss.hasPermission('crm:customer-care:update')")
    public CommonResult<Boolean> saveConfig(@Valid @RequestBody CrmCustomerCareConfigSaveReqVO saveReqVO) {
        customerCareConfigService.saveCustomerCareConfig(saveReqVO);
        return success(true);
    }

    @PostMapping("/send-test")
    @Operation(summary = "发送测试邮件")
    @PreAuthorize("@ss.hasPermission('crm:customer-care:update')")
    public CommonResult<Boolean> sendTest(@Valid @RequestBody CrmCustomerCareConfigSaveReqVO saveReqVO) {
        customerCareConfigService.sendTestEmail(saveReqVO);
        return success(true);
    }

}

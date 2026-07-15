package com.meession.etm.module.crm.controller.admin.common;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.module.crm.controller.admin.common.vo.CustomerRelationRespVO;
import com.meession.etm.module.crm.service.common.CrmRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.meession.etm.framework.common.pojo.CommonResult.success;

/**
 * CRM 跨域关联查询控制器
 * <p>
 * 提供按客户/商机等维度查询其关联的所有 CRM 数据
 *
 * @author 密讯
 */
@Tag(name = "管理后台 - CRM 关联查询")
@RestController
@RequestMapping("/crm/relation")
public class CrmRelationController {

    @Resource
    private CrmRelationService relationService;

    @GetMapping("/customer/{id}")
    @Operation(summary = "获取客户关联的所有 CRM 数据")
    @Parameter(name = "id", description = "客户ID", required = true)
    @PreAuthorize("@ss.hasPermission('crm:customer:query')")
    public CommonResult<CustomerRelationRespVO> getCustomerRelation(@PathVariable("id") Long id) {
        return success(relationService.getCustomerRelation(id));
    }
}

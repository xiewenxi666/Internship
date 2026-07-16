// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.controller.admin.common;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.module.crm.controller.admin.common.vo.CustomerRelationRespVO;
import com.meession.etm.module.crm.service.common.CrmRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.meession.etm.framework.common.pojo.CommonResult.success;

/**
 * CRM 关联查询 Controller
 *
 * @author 密讯
 */
@Tag(name = "管理后台 - CRM 关联查询")
@RestController
@RequestMapping("/crm/relation")
@Validated
public class CrmRelationController {

    @Resource
    private CrmRelationService relationService;

    @GetMapping("/customer/{id}")
    @Operation(summary = "获取客户关联数据（商机、合同、回款）")
    @PreAuthorize("@ss.hasPermission('crm:customer:query')")
    public CommonResult<CustomerRelationRespVO> getCustomerRelation(@PathVariable("id") Long id) {
        return success(relationService.getCustomerRelation(id));
    }

}

package com.meession.etm.module.crm.controller.admin.bulksend;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.number.NumberUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.bulksend.vo.CrmBulkSendPageReqVO;
import com.meession.etm.module.crm.controller.admin.bulksend.vo.CrmBulkSendRespVO;
import com.meession.etm.module.crm.controller.admin.bulksend.vo.CrmBulkSendSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.bulksend.CrmBulkSendDO;
import com.meession.etm.module.crm.service.bulksend.CrmBulkSendService;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.meession.etm.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertListByFlatMap;
import static com.meession.etm.framework.common.util.collection.MapUtils.findAndThen;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static java.util.Collections.singletonList;

@Tag(name = "管理后台 - 群发管理")
@RestController
@RequestMapping("/crm/bulk-send")
@Validated
public class CrmBulkSendController {

    @Resource
    private CrmBulkSendService bulkSendService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建群发")
    @PreAuthorize("@ss.hasPermission('crm:bulk-send:create')")
    public CommonResult<Long> createBulkSend(@Valid @RequestBody CrmBulkSendSaveReqVO createReqVO) {
        return success(bulkSendService.createBulkSend(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新群发")
    @PreAuthorize("@ss.hasPermission('crm:bulk-send:update')")
    public CommonResult<Boolean> updateBulkSend(@Valid @RequestBody CrmBulkSendSaveReqVO updateReqVO) {
        bulkSendService.updateBulkSend(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除群发")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:bulk-send:delete')")
    public CommonResult<Boolean> deleteBulkSend(@RequestParam("id") Long id) {
        bulkSendService.deleteBulkSend(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得群发")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:bulk-send:query')")
    public CommonResult<CrmBulkSendRespVO> getBulkSend(@RequestParam("id") Long id) {
        CrmBulkSendDO bulkSend = bulkSendService.getBulkSend(id);
        return success(buildBulkSendDetail(bulkSend));
    }

    private CrmBulkSendRespVO buildBulkSendDetail(CrmBulkSendDO bulkSend) {
        if (bulkSend == null) {
            return null;
        }
        return buildBulkSendDetailList(singletonList(bulkSend)).get(0);
    }

    @GetMapping("/page")
    @Operation(summary = "获得群发分页")
    @PreAuthorize("@ss.hasPermission('crm:bulk-send:query')")
    public CommonResult<PageResult<CrmBulkSendRespVO>> getBulkSendPage(@Valid CrmBulkSendPageReqVO pageVO) {
        PageResult<CrmBulkSendDO> pageResult = bulkSendService.getBulkSendPage(pageVO);
        return success(new PageResult<>(buildBulkSendDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @PostMapping("/submit/{id}")
    @Operation(summary = "提交群发审核")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:bulk-send:update')")
    public CommonResult<Boolean> submitForApproval(@PathVariable("id") Long id) {
        bulkSendService.submitForApproval(id);
        return success(true);
    }

    @PostMapping("/approve/{id}")
    @Operation(summary = "审批通过群发")
    @PreAuthorize("@ss.hasPermission('crm:bulk-send:update')")
    public CommonResult<Boolean> approve(@PathVariable("id") Long id) {
        bulkSendService.approve(id);
        return success(true);
    }

    @PostMapping("/reject/{id}")
    @Operation(summary = "驳回群发")
    @PreAuthorize("@ss.hasPermission('crm:bulk-send:update')")
    public CommonResult<Boolean> reject(@PathVariable("id") Long id) {
        bulkSendService.reject(id);
        return success(true);
    }

    private List<CrmBulkSendRespVO> buildBulkSendDetailList(List<CrmBulkSendDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        // 1. 获取创建人、负责人列表
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(list,
                contact -> Stream.of(NumberUtils.parseLong(contact.getCreator()), contact.getOwnerUserId())));
        // 2. 转换成 VO
        return BeanUtils.toBean(list, CrmBulkSendRespVO.class, bulkSendVO -> {
            // 2.1 设置创建人、负责人名称
            findAndThen(userMap, NumberUtils.parseLong(bulkSendVO.getCreator()),
                    user -> bulkSendVO.setCreatorName(user.getNickname()));
            findAndThen(userMap, bulkSendVO.getOwnerUserId(), user -> {
                bulkSendVO.setOwnerUserName(user.getNickname());
            });
        });
    }

}

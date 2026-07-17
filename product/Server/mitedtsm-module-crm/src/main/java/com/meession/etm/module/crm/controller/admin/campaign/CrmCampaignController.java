package com.meession.etm.module.crm.controller.admin.campaign;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.apilog.core.annotation.ApiAccessLog;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.number.NumberUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.excel.core.util.ExcelUtils;
import com.meession.etm.module.crm.controller.admin.campaign.vo.CrmCampaignImportExcelVO;
import com.meession.etm.module.crm.controller.admin.campaign.vo.CrmCampaignImportReqVO;
import com.meession.etm.module.crm.controller.admin.campaign.vo.CrmCampaignPageReqVO;
import com.meession.etm.module.crm.controller.admin.campaign.vo.CrmCampaignRespVO;
import com.meession.etm.module.crm.controller.admin.campaign.vo.CrmCampaignSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.campaign.CrmCampaignDO;
import com.meession.etm.module.crm.service.campaign.CrmCampaignService;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.meession.etm.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.meession.etm.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.common.pojo.PageParam.PAGE_SIZE_NONE;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertListByFlatMap;
import static com.meession.etm.framework.common.util.collection.MapUtils.findAndThen;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static java.util.Collections.singletonList;

@Tag(name = "管理后台 - 营销活动")
@RestController
@RequestMapping("/crm/campaign")
@Validated
public class CrmCampaignController {

    @Resource
    private CrmCampaignService campaignService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建营销活动")
    @PreAuthorize("@ss.hasPermission('crm:campaign:create')")
    public CommonResult<Long> createCampaign(@Valid @RequestBody CrmCampaignSaveReqVO createReqVO) {
        return success(campaignService.createCampaign(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新营销活动")
    @PreAuthorize("@ss.hasPermission('crm:campaign:update')")
    public CommonResult<Boolean> updateCampaign(@Valid @RequestBody CrmCampaignSaveReqVO updateReqVO) {
        campaignService.updateCampaign(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除营销活动")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:campaign:delete')")
    public CommonResult<Boolean> deleteCampaign(@RequestParam("id") Long id) {
        campaignService.deleteCampaign(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得营销活动")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:campaign:query')")
    public CommonResult<CrmCampaignRespVO> getCampaign(@RequestParam("id") Long id) {
        CrmCampaignDO campaign = campaignService.getCampaign(id);
        return success(buildCampaignDetail(campaign));
    }

    private CrmCampaignRespVO buildCampaignDetail(CrmCampaignDO campaign) {
        if (campaign == null) {
            return null;
        }
        return buildCampaignDetailList(singletonList(campaign)).get(0);
    }

    @GetMapping("/page")
    @Operation(summary = "获得营销活动分页")
    @PreAuthorize("@ss.hasPermission('crm:campaign:query')")
    public CommonResult<PageResult<CrmCampaignRespVO>> getCampaignPage(@Valid CrmCampaignPageReqVO pageVO) {
        PageResult<CrmCampaignDO> pageResult = campaignService.getCampaignPage(pageVO);
        return success(new PageResult<>(buildCampaignDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出营销活动 Excel")
    @PreAuthorize("@ss.hasPermission('crm:campaign:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCampaignExcel(@Valid CrmCampaignPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PAGE_SIZE_NONE);
        List<CrmCampaignDO> list = campaignService.getCampaignPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "营销活动.xls", "数据", CrmCampaignRespVO.class, buildCampaignDetailList(list));
    }

    @GetMapping("/get-import-template")
    @Operation(summary = "获得导入营销活动模板")
    public void importTemplate(HttpServletResponse response) throws IOException {
        // 手动创建导出 demo
        List<CrmCampaignImportExcelVO> list = Arrays.asList(
                CrmCampaignImportExcelVO.builder().title("春季促销").type(1)
                        .startTime(null).endTime(null).estimatedCost(null).estimatedRevenue(null)
                        .ownerUserId(null).participants("").address("").description("").status(1).build(),
                CrmCampaignImportExcelVO.builder().title("秋季大促").type(2)
                        .startTime(null).endTime(null).estimatedCost(null).estimatedRevenue(null)
                        .ownerUserId(null).participants("").address("").description("").status(1).build()
        );
        // 输出
        ExcelUtils.write(response, "营销活动导入模板.xls", "营销活动列表", CrmCampaignImportExcelVO.class, list);
    }

    @PostMapping("/import")
    @Operation(summary = "导入营销活动")
    @PreAuthorize("@ss.hasPermission('crm:campaign:import')")
    public CommonResult<Boolean> importExcel(@Valid CrmCampaignImportReqVO importReqVO) throws Exception {
        List<CrmCampaignImportExcelVO> list = ExcelUtils.read(importReqVO.getFile(), CrmCampaignImportExcelVO.class);
        int count = 0;
        for (CrmCampaignImportExcelVO importVO : list) {
            CrmCampaignSaveReqVO saveReqVO = BeanUtils.toBean(importVO, CrmCampaignSaveReqVO.class);
            campaignService.createCampaign(saveReqVO);
            count++;
        }
        return success(true);
    }

    private List<CrmCampaignRespVO> buildCampaignDetailList(List<CrmCampaignDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        // 1. 获取创建人、负责人列表
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(list,
                contact -> Stream.of(NumberUtils.parseLong(contact.getCreator()), contact.getOwnerUserId())));
        // 2. 转换成 VO
        return BeanUtils.toBean(list, CrmCampaignRespVO.class, campaignVO -> {
            // 2.1 设置创建人、负责人名称
            findAndThen(userMap, NumberUtils.parseLong(campaignVO.getCreator()),
                    user -> campaignVO.setCreatorName(user.getNickname()));
            findAndThen(userMap, campaignVO.getOwnerUserId(), user -> {
                campaignVO.setOwnerUserName(user.getNickname());
            });
        });
    }

}

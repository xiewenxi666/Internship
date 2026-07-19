package com.meession.etm.module.oa.controller.admin.documentDir;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.convert.documentDir.OaDocumentDirConvert;
import com.meession.etm.module.oa.controller.admin.documentDir.vo.OaDocumentDirCreateReqVO;
import com.meession.etm.module.oa.controller.admin.documentDir.vo.OaDocumentDirPageReqVO;
import com.meession.etm.module.oa.controller.admin.documentDir.vo.OaDocumentDirRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaDocumentDirDO;
import com.meession.etm.module.oa.service.documentDir.OaDocumentDirService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - OA 文档目录")
@RestController
@RequestMapping("/oa/document-dir")
@Validated
public class OaDocumentDirController {

    @Resource
    private OaDocumentDirService documentDirService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('oa:document-dir:create')")
    @Operation(summary = "创建文档目录/文档")
    public CommonResult<Long> createDocumentDir(@Valid @RequestBody OaDocumentDirCreateReqVO createReqVO) {
        return success(documentDirService.createDocumentDir(getLoginUserId(), createReqVO));
    }

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('oa:document-dir:update')")
    @Operation(summary = "更新文档目录/文档")
    public CommonResult<Boolean> updateDocumentDir(@Valid @RequestBody OaDocumentDirCreateReqVO updateReqVO,
                                                   @RequestParam("id") Long id) {
        documentDirService.updateDocumentDir(id, updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('oa:document-dir:delete')")
    @Operation(summary = "删除文档目录/文档")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteDocumentDir(@RequestParam("id") Long id) {
        documentDirService.deleteDocumentDir(id);
        return success(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:document-dir:query')")
    @Operation(summary = "获得文档目录/文档")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaDocumentDirRespVO> getDocumentDir(@RequestParam("id") Long id) {
        OaDocumentDirDO dir = documentDirService.getDocumentDir(id);
        return success(OaDocumentDirConvert.convert(dir));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:document-dir:query')")
    @Operation(summary = "获得文档目录分页")
    public CommonResult<PageResult<OaDocumentDirRespVO>> getDocumentDirPage(@Valid OaDocumentDirPageReqVO pageVO) {
        PageResult<OaDocumentDirDO> pageResult = documentDirService.getDocumentDirPage(pageVO);
        return success(OaDocumentDirConvert.convertPage(pageResult));
    }

    @GetMapping("/children")
    @PreAuthorize("@ss.hasPermission('oa:document-dir:query')")
    @Operation(summary = "获得子目录列表")
    @Parameter(name = "parentId", description = "父目录ID", required = true, example = "0")
    public CommonResult<List<OaDocumentDirRespVO>> getChildList(@RequestParam("parentId") Long parentId) {
        List<OaDocumentDirDO> list = documentDirService.getChildList(parentId, getLoginUserId());
        return success(OaDocumentDirConvert.convertList(list));
    }

}
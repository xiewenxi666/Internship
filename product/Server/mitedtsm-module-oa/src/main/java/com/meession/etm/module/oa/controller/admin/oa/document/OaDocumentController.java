package com.meession.etm.module.oa.controller.admin.oa.document;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.oa.vo.document.OaDocumentCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.document.OaDocumentPageReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.document.OaDocumentRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaDocumentDO;
import com.meession.etm.module.oa.service.document.OaDocumentService;
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

@Tag(name = "管理后台 - OA 文档")
@RestController
@RequestMapping("/oa/document")
@Validated
public class OaDocumentController {

    @Resource
    private OaDocumentService documentService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('oa:document:create')")
    @Operation(summary = "创建文档")
    public CommonResult<Long> createDocument(@Valid @RequestBody OaDocumentCreateReqVO createReqVO) {
        return success(documentService.createDocument(getLoginUserId(), createReqVO));
    }

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('oa:document:update')")
    @Operation(summary = "更新文档")
    public CommonResult<Boolean> update(@Valid @RequestBody OaDocumentCreateReqVO updateReqVO) {
        documentService.updateDocument(BeanUtils.toBean(updateReqVO, OaDocumentDO.class));
        return success(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:document:query')")
    @Operation(summary = "获得文档")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaDocumentRespVO> getDocument(@RequestParam("id") Long id) {
        OaDocumentDO document = documentService.getDocument(id);
        return success(BeanUtils.toBean(document, OaDocumentRespVO.class));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:document:query')")
    @Operation(summary = "获得文档分页")
    public CommonResult<PageResult<OaDocumentRespVO>> getDocumentPage(@Valid OaDocumentPageReqVO pageVO) {
        PageResult<OaDocumentDO> pageResult = documentService.getDocumentPage(getLoginUserId(), pageVO);
        return success(BeanUtils.toBean(pageResult, OaDocumentRespVO.class));
    }

    @GetMapping("/list-children")
    @PreAuthorize("@ss.hasPermission('oa:document:query')")
    @Operation(summary = "获得子文档列表")
    public CommonResult<List<OaDocumentRespVO>> listChildren(@RequestParam(value = "parentId", defaultValue = "0") Long parentId) {
        List<OaDocumentDO> list = documentService.getChildren(parentId);
        return success(BeanUtils.toBean(list, OaDocumentRespVO.class));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('oa:document:delete')")
    @Operation(summary = "删除文档")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteDocument(@RequestParam("id") Long id) {
        documentService.deleteDocument(id);
        return success(true);
    }

}

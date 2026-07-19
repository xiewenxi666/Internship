package com.meession.etm.module.oa.controller.admin.message;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.convert.message.OaMessageConvert;
import com.meession.etm.module.oa.controller.admin.message.vo.OaMessageCreateReqVO;
import com.meession.etm.module.oa.controller.admin.message.vo.OaMessagePageReqVO;
import com.meession.etm.module.oa.controller.admin.message.vo.OaMessageRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaMessageDO;
import com.meession.etm.module.oa.service.message.OaMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - OA 内部消息")
@RestController
@RequestMapping("/oa/message")
@Validated
public class OaMessageController {

    @Resource
    private OaMessageService messageService;

    @PostMapping("/send")
    @PreAuthorize("@ss.hasPermission('oa:message:send')")
    @Operation(summary = "发送内部消息")
    public CommonResult<Long> sendMessage(@Valid @RequestBody OaMessageCreateReqVO createReqVO) {
        return success(messageService.sendMessage(getLoginUserId(), createReqVO));
    }

    @PutMapping("/read")
    @PreAuthorize("@ss.hasPermission('oa:message:read')")
    @Operation(summary = "标记已读")
    @Parameter(name = "id", description = "消息编号", required = true)
    public CommonResult<Boolean> markRead(@RequestParam("id") Long id) {
        messageService.markRead(id, getLoginUserId());
        return success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('oa:message:delete')")
    @Operation(summary = "删除消息")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteMessage(@RequestParam("id") Long id) {
        messageService.deleteMessage(id);
        return success(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:message:query')")
    @Operation(summary = "获得消息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaMessageRespVO> getMessage(@RequestParam("id") Long id) {
        OaMessageDO message = messageService.getMessage(id);
        return success(OaMessageConvert.convert(message));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:message:query')")
    @Operation(summary = "获得消息分页")
    public CommonResult<PageResult<OaMessageRespVO>> getMessagePage(@Valid OaMessagePageReqVO pageVO) {
        PageResult<OaMessageDO> pageResult = messageService.getMessagePage(getLoginUserId(), pageVO);
        return success(OaMessageConvert.convertPage(pageResult));
    }

    @GetMapping("/unread-count")
    @PreAuthorize("@ss.hasPermission('oa:message:query')")
    @Operation(summary = "获得未读消息数量")
    public CommonResult<Long> getUnreadCount() {
        return success(messageService.getUnreadCount(getLoginUserId()));
    }

}
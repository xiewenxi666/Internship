package com.meession.etm.module.oa.service.message;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.message.vo.OaMessageCreateReqVO;
import com.meession.etm.module.oa.controller.admin.message.vo.OaMessagePageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaMessageDO;
import jakarta.validation.Valid;

public interface OaMessageService {

    Long sendMessage(Long senderUserId, @Valid OaMessageCreateReqVO createReqVO);

    void markRead(Long messageId, Long userId);

    void deleteMessage(Long id);

    OaMessageDO getMessage(Long id);

    PageResult<OaMessageDO> getMessagePage(Long userId, OaMessagePageReqVO pageReqVO);

    Long getUnreadCount(Long userId);

}
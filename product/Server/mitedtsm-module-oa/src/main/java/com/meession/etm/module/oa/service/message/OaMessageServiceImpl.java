package com.meession.etm.module.oa.service.message;

import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.message.vo.OaMessageCreateReqVO;
import com.meession.etm.module.oa.controller.admin.message.vo.OaMessagePageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaMessageDO;
import com.meession.etm.module.oa.dal.mysql.OaMessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_MESSAGE_NOT_EXISTS;

@Service
@Validated
public class OaMessageServiceImpl implements OaMessageService {

    @Resource
    private OaMessageMapper messageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long sendMessage(Long senderUserId, OaMessageCreateReqVO createReqVO) {
        OaMessageDO message = BeanUtils.toBean(createReqVO, OaMessageDO.class)
                .setSenderUserId(senderUserId).setReadStatus(0);
        messageMapper.insert(message);
        return message.getId();
    }

    @Override
    public void markRead(Long messageId, Long userId) {
        validateOwner(messageId, userId);
        messageMapper.updateById(new OaMessageDO().setId(messageId)
                .setReadStatus(1).setReadTime(LocalDateTime.now()));
    }

    @Override
    public void deleteMessage(Long id) {
        validateExists(id);
        messageMapper.deleteById(id);
    }

    @Override
    public OaMessageDO getMessage(Long id) {
        return messageMapper.selectById(id);
    }

    @Override
    public PageResult<OaMessageDO> getMessagePage(Long userId, OaMessagePageReqVO pageReqVO) {
        return messageMapper.selectPage(userId, pageReqVO);
    }

    @Override
    public Long getUnreadCount(Long userId) {
        return messageMapper.selectCount(new com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX<OaMessageDO>()
                .eq(OaMessageDO::getReceiverUserId, userId)
                .eq(OaMessageDO::getReadStatus, 0));
    }

    private void validateExists(Long id) {
        if (messageMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(OA_MESSAGE_NOT_EXISTS);
        }
    }

    private void validateOwner(Long messageId, Long userId) {
        OaMessageDO msg = messageMapper.selectById(messageId);
        if (msg == null) {
            throw ServiceExceptionUtil.exception(OA_MESSAGE_NOT_EXISTS);
        }
        if (!msg.getReceiverUserId().equals(userId)) {
            throw ServiceExceptionUtil.exception(OA_MESSAGE_NOT_EXISTS);
        }
    }

}
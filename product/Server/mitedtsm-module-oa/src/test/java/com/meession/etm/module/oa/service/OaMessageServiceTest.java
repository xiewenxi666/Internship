package com.meession.etm.module.oa.service;

import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.oa.controller.admin.message.vo.OaMessageCreateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaMessageDO;
import com.meession.etm.module.oa.dal.mysql.OaMessageMapper;
import com.meession.etm.module.oa.service.message.OaMessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import jakarta.annotation.Resource;

import static com.meession.etm.framework.test.core.util.RandomUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@Import(OaMessageServiceImpl.class)
public class OaMessageServiceTest extends BaseDbUnitTest {

    @Resource
    private OaMessageServiceImpl messageService;

    @Resource
    private OaMessageMapper messageMapper;

    @Test
    public void testSendMessage_success() {
        OaMessageCreateReqVO reqVO = randomPojo(OaMessageCreateReqVO.class);
        Long senderId = randomLongId();

        Long msgId = messageService.sendMessage(senderId, reqVO);

        assertNotNull(msgId);
        OaMessageDO msg = messageMapper.selectById(msgId);
        assertEquals(senderId, msg.getSenderUserId());
        assertEquals(0, msg.getReadStatus());
    }

    @Test
    public void testMarkRead_success() {
        Long userId = randomLongId();
        OaMessageDO dbMsg = randomPojo(OaMessageDO.class, o -> {
            o.setReadStatus(0);
            o.setReceiverUserId(userId);
        });
        messageMapper.insert(dbMsg);

        messageService.markRead(dbMsg.getId(), userId);

        OaMessageDO updated = messageMapper.selectById(dbMsg.getId());
        assertEquals(1, (int) updated.getReadStatus());
        assertNotNull(updated.getReadTime());
    }

    @Test
    public void testGetUnreadCount() {
        Long receiverId = randomLongId();
        messageMapper.insert(randomPojo(OaMessageDO.class, o -> o.setReceiverUserId(receiverId).setReadStatus(0)));
        messageMapper.insert(randomPojo(OaMessageDO.class, o -> o.setReceiverUserId(receiverId).setReadStatus(0)));
        messageMapper.insert(randomPojo(OaMessageDO.class, o -> o.setReceiverUserId(receiverId).setReadStatus(1)));

        Long unreadCount = messageService.getUnreadCount(receiverId);

        assertEquals(2, unreadCount);
    }

    @Test
    public void testDeleteMessage_success() {
        OaMessageDO dbMsg = randomPojo(OaMessageDO.class);
        messageMapper.insert(dbMsg);

        messageService.deleteMessage(dbMsg.getId());

        assertNull(messageMapper.selectById(dbMsg.getId()));
    }
}

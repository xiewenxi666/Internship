package com.meession.etm.module.oa.controller.admin.message;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.security.core.LoginUser;
import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.oa.controller.admin.message.vo.OaMessageCreateReqVO;
import com.meession.etm.module.oa.controller.admin.message.vo.OaMessagePageReqVO;
import com.meession.etm.module.oa.controller.admin.message.vo.OaMessageRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaMessageDO;
import com.meession.etm.module.oa.service.message.OaMessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static com.meession.etm.framework.test.core.util.RandomUtils.randomPojo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OaMessageControllerTest extends BaseMockitoUnitTest {

    private static final Long LOGIN_USER_ID = 1L;

    @InjectMocks
    private OaMessageController messageController;

    @Mock
    private OaMessageService messageService;

    @BeforeEach
    void setUp() {
        LoginUser loginUser = new LoginUser();
        loginUser.setId(LOGIN_USER_ID);
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testSendMessage() {
        OaMessageCreateReqVO reqVO = randomPojo(OaMessageCreateReqVO.class);
        when(messageService.sendMessage(LOGIN_USER_ID, reqVO)).thenReturn(1L);

        CommonResult<Long> result = messageController.sendMessage(reqVO);

        assertEquals(0, result.getCode());
        assertEquals(1L, result.getData());
        verify(messageService).sendMessage(LOGIN_USER_ID, reqVO);
    }

    @Test
    void testMarkRead() {
        Long id = 1L;

        CommonResult<Boolean> result = messageController.markRead(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(messageService).markRead(id, LOGIN_USER_ID);
    }

    @Test
    void testDeleteMessage() {
        Long id = 1L;

        CommonResult<Boolean> result = messageController.deleteMessage(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(messageService).deleteMessage(id);
    }

    @Test
    void testGetMessage() {
        Long id = 1L;
        OaMessageDO messageDO = randomPojo(OaMessageDO.class);
        when(messageService.getMessage(id)).thenReturn(messageDO);

        CommonResult<OaMessageRespVO> result = messageController.getMessage(id);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(messageService).getMessage(id);
    }

    @Test
    void testGetMessagePage() {
        OaMessagePageReqVO pageVO = randomPojo(OaMessagePageReqVO.class);
        PageResult<OaMessageDO> pageResult = new PageResult<>(Collections.emptyList(), 0L);
        when(messageService.getMessagePage(LOGIN_USER_ID, pageVO)).thenReturn(pageResult);

        CommonResult<PageResult<OaMessageRespVO>> result = messageController.getMessagePage(pageVO);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(messageService).getMessagePage(LOGIN_USER_ID, pageVO);
    }

    @Test
    void testGetUnreadCount() {
        when(messageService.getUnreadCount(LOGIN_USER_ID)).thenReturn(5L);

        CommonResult<Long> result = messageController.getUnreadCount();

        assertEquals(0, result.getCode());
        assertEquals(5L, result.getData());
        verify(messageService).getUnreadCount(LOGIN_USER_ID);
    }

}

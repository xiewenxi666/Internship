package com.meession.etm.module.oa.controller.admin.documentDir;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.security.core.LoginUser;
import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.oa.controller.admin.documentDir.vo.OaDocumentDirCreateReqVO;
import com.meession.etm.module.oa.controller.admin.documentDir.vo.OaDocumentDirPageReqVO;
import com.meession.etm.module.oa.controller.admin.documentDir.vo.OaDocumentDirRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaDocumentDirDO;
import com.meession.etm.module.oa.service.documentDir.OaDocumentDirService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static com.meession.etm.framework.test.core.util.RandomUtils.randomPojo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OaDocumentDirControllerTest extends BaseMockitoUnitTest {

    private static final Long LOGIN_USER_ID = 1L;

    @InjectMocks
    private OaDocumentDirController documentDirController;

    @Mock
    private OaDocumentDirService documentDirService;

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
    void testCreateDocumentDir() {
        OaDocumentDirCreateReqVO reqVO = randomPojo(OaDocumentDirCreateReqVO.class);
        when(documentDirService.createDocumentDir(LOGIN_USER_ID, reqVO)).thenReturn(1L);

        CommonResult<Long> result = documentDirController.createDocumentDir(reqVO);

        assertEquals(0, result.getCode());
        assertEquals(1L, result.getData());
        verify(documentDirService).createDocumentDir(LOGIN_USER_ID, reqVO);
    }

    @Test
    void testUpdateDocumentDir() {
        Long id = 1L;
        OaDocumentDirCreateReqVO reqVO = randomPojo(OaDocumentDirCreateReqVO.class);

        CommonResult<Boolean> result = documentDirController.updateDocumentDir(reqVO, id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(documentDirService).updateDocumentDir(id, reqVO);
    }

    @Test
    void testDeleteDocumentDir() {
        Long id = 1L;

        CommonResult<Boolean> result = documentDirController.deleteDocumentDir(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(documentDirService).deleteDocumentDir(id);
    }

    @Test
    void testGetDocumentDir() {
        Long id = 1L;
        OaDocumentDirDO dirDO = randomPojo(OaDocumentDirDO.class);
        when(documentDirService.getDocumentDir(id)).thenReturn(dirDO);

        CommonResult<OaDocumentDirRespVO> result = documentDirController.getDocumentDir(id);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(documentDirService).getDocumentDir(id);
    }

    @Test
    void testGetDocumentDirPage() {
        OaDocumentDirPageReqVO pageVO = randomPojo(OaDocumentDirPageReqVO.class);
        PageResult<OaDocumentDirDO> pageResult = new PageResult<>(Collections.emptyList(), 0L);
        when(documentDirService.getDocumentDirPage(pageVO)).thenReturn(pageResult);

        CommonResult<PageResult<OaDocumentDirRespVO>> result = documentDirController.getDocumentDirPage(pageVO);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(documentDirService).getDocumentDirPage(pageVO);
    }

    @Test
    void testGetChildList() {
        Long parentId = 0L;
        List<OaDocumentDirDO> list = Collections.emptyList();
        when(documentDirService.getChildList(parentId, LOGIN_USER_ID)).thenReturn(list);

        CommonResult<List<OaDocumentDirRespVO>> result = documentDirController.getChildList(parentId);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(documentDirService).getChildList(parentId, LOGIN_USER_ID);
    }

}

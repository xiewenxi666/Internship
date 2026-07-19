package com.meession.etm.module.oa.service;

import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.oa.controller.admin.documentDir.vo.OaDocumentDirCreateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaDocumentDirDO;
import com.meession.etm.module.oa.dal.mysql.OaDocumentDirMapper;
import com.meession.etm.module.oa.service.documentDir.OaDocumentDirServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import jakarta.annotation.Resource;
import java.util.List;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.meession.etm.framework.test.core.util.RandomUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@Import(OaDocumentDirServiceImpl.class)
public class OaDocumentDirServiceTest extends BaseDbUnitTest {

    @Resource
    private OaDocumentDirServiceImpl documentDirService;

    @Resource
    private OaDocumentDirMapper documentDirMapper;

    @Test
    public void testCreateDir_success() {
        OaDocumentDirCreateReqVO reqVO = randomPojo(OaDocumentDirCreateReqVO.class, o -> o.setType(1).setPermission(0));

        Long dirId = documentDirService.createDocumentDir(randomLongId(), reqVO);

        assertNotNull(dirId);
        OaDocumentDirDO dir = documentDirMapper.selectById(dirId);
        assertEquals(1, dir.getVersion());
        assertEquals(0, dir.getStatus());
    }

    @Test
    public void testUpdateDocumentDir_success() {
        OaDocumentDirDO dbDir = randomPojo(OaDocumentDirDO.class, o -> o.setType(1).setVersion(1).setPermission(0));
        documentDirMapper.insert(dbDir);

        OaDocumentDirCreateReqVO updateReqVO = randomPojo(OaDocumentDirCreateReqVO.class, o -> o.setType(1).setPermission(0));

        documentDirService.updateDocumentDir(dbDir.getId(), updateReqVO);

        OaDocumentDirDO updated = documentDirMapper.selectById(dbDir.getId());
        assertPojoEquals(updateReqVO, updated, "id", "ownerUserId", "version", "status",
                "creator", "createTime", "updater", "updateTime", "deleted", "tenantId");
    }

    @Test
    public void testGetChildList() {
        Long parentId = randomLongId();
        Long userId = randomLongId();
        documentDirMapper.insert(randomPojo(OaDocumentDirDO.class, o -> o.setParentId(parentId).setType(1).setPermission(0)));
        documentDirMapper.insert(randomPojo(OaDocumentDirDO.class, o -> o.setParentId(parentId).setType(2).setPermission(0)));

        List<OaDocumentDirDO> children = documentDirService.getChildList(parentId, userId);

        assertEquals(2, children.size());
    }

    @Test
    public void testDeleteDocumentDir_success() {
        OaDocumentDirDO dbDir = randomPojo(OaDocumentDirDO.class, o -> o.setPermission(0));
        documentDirMapper.insert(dbDir);

        documentDirService.deleteDocumentDir(dbDir.getId());

        assertNull(documentDirMapper.selectById(dbDir.getId()));
    }
}

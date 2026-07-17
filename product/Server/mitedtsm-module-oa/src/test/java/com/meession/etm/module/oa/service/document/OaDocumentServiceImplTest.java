package com.meession.etm.module.oa.service.document;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.oa.controller.admin.oa.vo.document.OaDocumentCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.document.OaDocumentPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaDocumentDO;
import com.meession.etm.module.oa.dal.mapper.OaDocumentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import jakarta.annotation.Resource;

import java.util.List;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomLongId;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomPojo;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_DOCUMENT_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

@Import(OaDocumentServiceImpl.class)
class OaDocumentServiceImplTest extends BaseDbUnitTest {

    @Resource
    private OaDocumentServiceImpl documentService;

    @Resource
    private OaDocumentMapper documentMapper;

    @Test
    void testCreateDocument_success() {
        OaDocumentCreateReqVO reqVO = randomPojo(OaDocumentCreateReqVO.class);
        Long userId = randomLongId();

        Long id = documentService.createDocument(userId, reqVO);

        assertNotNull(id);
        OaDocumentDO document = documentMapper.selectById(id);
        assertNotNull(document);
    }

    @Test
    void testGetDocument_success() {
        OaDocumentDO dbDocument = randomPojo(OaDocumentDO.class);
        documentMapper.insert(dbDocument);

        OaDocumentDO document = documentService.getDocument(dbDocument.getId());

        assertNotNull(document);
        assertPojoEquals(dbDocument, document);
    }

    @Test
    void testGetDocument_notExists() {
        assertNull(documentService.getDocument(randomLongId()));
    }

    @Test
    void testGetDocumentPage_success() {
        OaDocumentDO dbDocument = randomPojo(OaDocumentDO.class, o -> o.setIsFolder(true));
        documentMapper.insert(dbDocument);
        documentMapper.insert(randomPojo(OaDocumentDO.class, o -> o.setIsFolder(false)));
        OaDocumentDO otherParent = randomPojo(OaDocumentDO.class, o -> {
            o.setParentId(randomLongId());
            o.setIsFolder(false);
        });
        documentMapper.insert(otherParent);

        OaDocumentPageReqVO reqVO = new OaDocumentPageReqVO();
        reqVO.setIsFolder(true);

        PageResult<OaDocumentDO> result = documentService.getDocumentPage(randomLongId(), reqVO);

        assertEquals(1, result.getTotal());
        assertPojoEquals(dbDocument, result.getList().get(0));
    }

    @Test
    void testGetDocumentPage_byParent() {
        Long parentId = randomLongId();
        OaDocumentDO child = randomPojo(OaDocumentDO.class, o -> o.setParentId(parentId));
        documentMapper.insert(child);
        documentMapper.insert(randomPojo(OaDocumentDO.class, o -> o.setParentId(randomLongId())));

        OaDocumentPageReqVO reqVO = new OaDocumentPageReqVO();
        reqVO.setParentId(parentId);

        PageResult<OaDocumentDO> result = documentService.getDocumentPage(randomLongId(), reqVO);

        assertEquals(1, result.getTotal());
        assertPojoEquals(child, result.getList().get(0));
    }

    @Test
    void testUpdateDocument_success() {
        OaDocumentDO dbDocument = randomPojo(OaDocumentDO.class);
        documentMapper.insert(dbDocument);

        OaDocumentDO update = new OaDocumentDO();
        update.setId(dbDocument.getId());
        update.setName("Updated name");
        documentService.updateDocument(update);

        OaDocumentDO document = documentMapper.selectById(dbDocument.getId());
        assertEquals("Updated name", document.getName());
    }

    @Test
    void testUpdateDocument_notExists() {
        assertServiceException(() ->
                        documentService.updateDocument(new OaDocumentDO().setId(randomLongId())),
                OA_DOCUMENT_NOT_EXISTS);
    }

    @Test
    void testDeleteDocument_success() {
        OaDocumentDO dbDocument = randomPojo(OaDocumentDO.class);
        documentMapper.insert(dbDocument);

        documentService.deleteDocument(dbDocument.getId());

        assertNull(documentMapper.selectById(dbDocument.getId()));
    }

    @Test
    void testDeleteDocument_notExists() {
        assertServiceException(() ->
                        documentService.deleteDocument(randomLongId()),
                OA_DOCUMENT_NOT_EXISTS);
    }

    @Test
    void testGetChildren_success() {
        Long parentId = randomLongId();
        OaDocumentDO child1 = randomPojo(OaDocumentDO.class, o -> o.setParentId(parentId));
        OaDocumentDO child2 = randomPojo(OaDocumentDO.class, o -> o.setParentId(parentId));
        documentMapper.insert(child1);
        documentMapper.insert(child2);
        documentMapper.insert(randomPojo(OaDocumentDO.class, o -> o.setParentId(randomLongId())));

        List<OaDocumentDO> children = documentService.getChildren(parentId);

        assertEquals(2, children.size());
    }

    @Test
    void testGetChildren_empty() {
        List<OaDocumentDO> children = documentService.getChildren(randomLongId());

        assertTrue(children.isEmpty());
    }
}

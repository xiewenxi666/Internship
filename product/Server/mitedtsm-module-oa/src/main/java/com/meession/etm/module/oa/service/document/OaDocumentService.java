package com.meession.etm.module.oa.service.document;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.oa.vo.document.OaDocumentCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.document.OaDocumentPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaDocumentDO;

import java.util.List;

public interface OaDocumentService {

    Long createDocument(Long userId, OaDocumentCreateReqVO createReqVO);

    void updateDocument(OaDocumentDO document);

    void deleteDocument(Long id);

    OaDocumentDO getDocument(Long id);

    PageResult<OaDocumentDO> getDocumentPage(Long userId, OaDocumentPageReqVO pageReqVO);

    List<OaDocumentDO> getChildren(Long parentId);

}

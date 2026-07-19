package com.meession.etm.module.oa.service.documentDir;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.documentDir.vo.OaDocumentDirCreateReqVO;
import com.meession.etm.module.oa.controller.admin.documentDir.vo.OaDocumentDirPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaDocumentDirDO;
import jakarta.validation.Valid;

import java.util.List;

public interface OaDocumentDirService {

    Long createDocumentDir(Long userId, @Valid OaDocumentDirCreateReqVO createReqVO);

    void updateDocumentDir(Long id, @Valid OaDocumentDirCreateReqVO updateReqVO);

    void deleteDocumentDir(Long id);

    OaDocumentDirDO getDocumentDir(Long id);

    PageResult<OaDocumentDirDO> getDocumentDirPage(OaDocumentDirPageReqVO pageReqVO);

    List<OaDocumentDirDO> getChildList(Long parentId, Long userId);

}
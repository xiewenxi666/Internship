package com.meession.etm.module.oa.service.document;

import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.oa.vo.document.OaDocumentCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.document.OaDocumentPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaDocumentDO;
import com.meession.etm.module.oa.dal.mapper.OaDocumentMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_DOCUMENT_NOT_EXISTS;

@Slf4j
@Service
@Validated
public class OaDocumentServiceImpl implements OaDocumentService {

    @Resource
    private OaDocumentMapper documentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDocument(Long userId, OaDocumentCreateReqVO createReqVO) {
        OaDocumentDO document = BeanUtils.toBean(createReqVO, OaDocumentDO.class);
        documentMapper.insert(document);
        return document.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDocument(OaDocumentDO document) {
        validateDocumentExists(document.getId());
        documentMapper.updateById(document);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDocument(Long id) {
        validateDocumentExists(id);
        documentMapper.deleteById(id);
    }

    private void validateDocumentExists(Long id) {
        if (documentMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(OA_DOCUMENT_NOT_EXISTS);
        }
    }

    @Override
    public OaDocumentDO getDocument(Long id) {
        return documentMapper.selectById(id);
    }

    @Override
    public PageResult<OaDocumentDO> getDocumentPage(Long userId, OaDocumentPageReqVO pageReqVO) {
        return documentMapper.selectPage(userId, pageReqVO);
    }

    @Override
    public List<OaDocumentDO> getChildren(Long parentId) {
        return documentMapper.selectListByParentId(parentId);
    }

}

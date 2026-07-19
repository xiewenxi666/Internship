package com.meession.etm.module.oa.service.documentDir;

import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.documentDir.vo.OaDocumentDirCreateReqVO;
import com.meession.etm.module.oa.controller.admin.documentDir.vo.OaDocumentDirPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaDocumentDirDO;
import com.meession.etm.module.oa.dal.mysql.OaDocumentDirMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_DOCUMENT_DIR_NOT_EXISTS;

@Service
@Validated
public class OaDocumentDirServiceImpl implements OaDocumentDirService {

    @Resource
    private OaDocumentDirMapper documentDirMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDocumentDir(Long userId, OaDocumentDirCreateReqVO createReqVO) {
        OaDocumentDirDO dir = BeanUtils.toBean(createReqVO, OaDocumentDirDO.class)
                .setOwnerUserId(userId).setVersion(1).setStatus(0);
        documentDirMapper.insert(dir);
        return dir.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDocumentDir(Long id, OaDocumentDirCreateReqVO updateReqVO) {
        validateExists(id);
        OaDocumentDirDO exist = documentDirMapper.selectById(id);
        OaDocumentDirDO dir = BeanUtils.toBean(updateReqVO, OaDocumentDirDO.class).setId(id);
        if (exist.getType() == 2) {
            dir.setVersion(exist.getVersion() + 1);
        }
        documentDirMapper.updateById(dir);
    }

    @Override
    public void deleteDocumentDir(Long id) {
        validateExists(id);
        documentDirMapper.deleteById(id);
    }

    @Override
    public OaDocumentDirDO getDocumentDir(Long id) {
        return documentDirMapper.selectById(id);
    }

    @Override
    public PageResult<OaDocumentDirDO> getDocumentDirPage(OaDocumentDirPageReqVO pageReqVO) {
        return documentDirMapper.selectPage(pageReqVO);
    }

    @Override
    public List<OaDocumentDirDO> getChildList(Long parentId, Long userId) {
        return documentDirMapper.selectList(OaDocumentDirDO::getParentId, parentId);
    }

    private void validateExists(Long id) {
        if (documentDirMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(OA_DOCUMENT_DIR_NOT_EXISTS);
        }
    }

}
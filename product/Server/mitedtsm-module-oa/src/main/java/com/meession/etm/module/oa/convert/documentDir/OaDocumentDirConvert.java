package com.meession.etm.module.oa.convert.documentDir;

import com.meession.etm.module.oa.controller.admin.documentDir.vo.OaDocumentDirCreateReqVO;
import com.meession.etm.module.oa.controller.admin.documentDir.vo.OaDocumentDirRespVO;
import com.meession.etm.module.oa.controller.admin.documentDir.vo.OaDocumentDirUpdateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaDocumentDirDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.CollectionUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;

import java.util.List;

public class OaDocumentDirConvert {

    public static OaDocumentDirRespVO convert(OaDocumentDirDO bean) {
        return BeanUtils.toBean(bean, OaDocumentDirRespVO.class);
    }

    public static List<OaDocumentDirRespVO> convertList(List<OaDocumentDirDO> list) {
        return CollectionUtils.convertList(list, OaDocumentDirConvert::convert);
    }

    public static PageResult<OaDocumentDirRespVO> convertPage(PageResult<OaDocumentDirDO> page) {
        return BeanUtils.toBean(page, OaDocumentDirRespVO.class);
    }

    public static OaDocumentDirDO convert(OaDocumentDirCreateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaDocumentDirDO.class);
    }

    public static OaDocumentDirDO convert(OaDocumentDirUpdateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaDocumentDirDO.class);
    }

}

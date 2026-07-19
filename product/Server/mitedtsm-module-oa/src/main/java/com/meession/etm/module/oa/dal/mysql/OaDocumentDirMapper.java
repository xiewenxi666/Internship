package com.meession.etm.module.oa.dal.mysql;

import com.meession.etm.module.oa.controller.admin.documentDir.vo.OaDocumentDirPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaDocumentDirDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OaDocumentDirMapper extends BaseMapperX<OaDocumentDirDO> {

    default PageResult<OaDocumentDirDO> selectPage(OaDocumentDirPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaDocumentDirDO>()
                .eqIfPresent(OaDocumentDirDO::getParentId, reqVO.getParentId())
                .eqIfPresent(OaDocumentDirDO::getType, reqVO.getType())
                .eqIfPresent(OaDocumentDirDO::getStatus, reqVO.getStatus())
                .likeIfPresent(OaDocumentDirDO::getName, reqVO.getName())
                .orderByAsc(OaDocumentDirDO::getSort));
    }

}
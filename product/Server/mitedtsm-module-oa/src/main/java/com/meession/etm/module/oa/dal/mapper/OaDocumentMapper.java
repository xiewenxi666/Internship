package com.meession.etm.module.oa.dal.mapper;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.oa.controller.admin.oa.vo.document.OaDocumentPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaDocumentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OaDocumentMapper extends BaseMapperX<OaDocumentDO> {

    default List<OaDocumentDO> selectListByParentId(Long parentId) {
        return selectList(new LambdaQueryWrapperX<OaDocumentDO>()
                .eq(OaDocumentDO::getParentId, parentId)
                .orderByAsc(OaDocumentDO::getIsFolder)
                .orderByAsc(OaDocumentDO::getName));
    }

    default PageResult<OaDocumentDO> selectPage(Long userId, OaDocumentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaDocumentDO>()
                .eqIfPresent(OaDocumentDO::getParentId, reqVO.getParentId())
                .likeIfPresent(OaDocumentDO::getName, reqVO.getName())
                .eqIfPresent(OaDocumentDO::getIsFolder, reqVO.getIsFolder())
                .orderByAsc(OaDocumentDO::getIsFolder)
                .orderByAsc(OaDocumentDO::getName));
    }

}

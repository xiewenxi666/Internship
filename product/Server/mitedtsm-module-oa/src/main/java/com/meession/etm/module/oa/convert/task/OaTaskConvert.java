package com.meession.etm.module.oa.convert.task;

import com.meession.etm.module.oa.controller.admin.task.vo.OaTaskCreateReqVO;
import com.meession.etm.module.oa.controller.admin.task.vo.OaTaskRespVO;
import com.meession.etm.module.oa.controller.admin.task.vo.OaTaskUpdateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaTaskDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.CollectionUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;

import java.util.List;

public class OaTaskConvert {

    public static OaTaskRespVO convert(OaTaskDO bean) {
        return BeanUtils.toBean(bean, OaTaskRespVO.class);
    }

    public static List<OaTaskRespVO> convertList(List<OaTaskDO> list) {
        return CollectionUtils.convertList(list, OaTaskConvert::convert);
    }

    public static PageResult<OaTaskRespVO> convertPage(PageResult<OaTaskDO> page) {
        return BeanUtils.toBean(page, OaTaskRespVO.class);
    }

    public static OaTaskDO convert(OaTaskCreateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaTaskDO.class);
    }

    public static OaTaskDO convert(OaTaskUpdateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaTaskDO.class);
    }

}

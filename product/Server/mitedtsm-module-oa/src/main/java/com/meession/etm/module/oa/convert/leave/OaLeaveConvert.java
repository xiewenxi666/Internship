package com.meession.etm.module.oa.convert.leave;

import com.meession.etm.module.oa.controller.admin.leave.vo.OaLeaveCreateReqVO;
import com.meession.etm.module.oa.controller.admin.leave.vo.OaLeaveRespVO;
import com.meession.etm.module.oa.controller.admin.leave.vo.OaLeaveUpdateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaLeaveDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.CollectionUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;

import java.util.List;

public class OaLeaveConvert {

    public static OaLeaveRespVO convert(OaLeaveDO bean) {
        return BeanUtils.toBean(bean, OaLeaveRespVO.class);
    }

    public static List<OaLeaveRespVO> convertList(List<OaLeaveDO> list) {
        return CollectionUtils.convertList(list, OaLeaveConvert::convert);
    }

    public static PageResult<OaLeaveRespVO> convertPage(PageResult<OaLeaveDO> page) {
        return BeanUtils.toBean(page, OaLeaveRespVO.class);
    }

    public static OaLeaveDO convert(OaLeaveCreateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaLeaveDO.class);
    }

    public static OaLeaveDO convert(OaLeaveUpdateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaLeaveDO.class);
    }

}

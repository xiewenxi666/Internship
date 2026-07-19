package com.meession.etm.module.oa.convert.schedule;

import com.meession.etm.module.oa.controller.admin.schedule.vo.OaScheduleCreateReqVO;
import com.meession.etm.module.oa.controller.admin.schedule.vo.OaScheduleRespVO;
import com.meession.etm.module.oa.controller.admin.schedule.vo.OaScheduleUpdateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaScheduleDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.CollectionUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;

import java.util.List;

public class OaScheduleConvert {

    public static OaScheduleRespVO convert(OaScheduleDO bean) {
        return BeanUtils.toBean(bean, OaScheduleRespVO.class);
    }

    public static List<OaScheduleRespVO> convertList(List<OaScheduleDO> list) {
        return CollectionUtils.convertList(list, OaScheduleConvert::convert);
    }

    public static PageResult<OaScheduleRespVO> convertPage(PageResult<OaScheduleDO> page) {
        return BeanUtils.toBean(page, OaScheduleRespVO.class);
    }

    public static OaScheduleDO convert(OaScheduleCreateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaScheduleDO.class);
    }

    public static OaScheduleDO convert(OaScheduleUpdateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaScheduleDO.class);
    }

}

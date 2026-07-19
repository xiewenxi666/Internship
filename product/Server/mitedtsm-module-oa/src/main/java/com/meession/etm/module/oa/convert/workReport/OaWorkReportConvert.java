package com.meession.etm.module.oa.convert.workReport;

import com.meession.etm.module.oa.controller.admin.workReport.vo.OaWorkReportCreateReqVO;
import com.meession.etm.module.oa.controller.admin.workReport.vo.OaWorkReportRespVO;
import com.meession.etm.module.oa.controller.admin.workReport.vo.OaWorkReportUpdateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaWorkReportDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.CollectionUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;

import java.util.List;

public class OaWorkReportConvert {

    public static OaWorkReportRespVO convert(OaWorkReportDO bean) {
        return BeanUtils.toBean(bean, OaWorkReportRespVO.class);
    }

    public static List<OaWorkReportRespVO> convertList(List<OaWorkReportDO> list) {
        return CollectionUtils.convertList(list, OaWorkReportConvert::convert);
    }

    public static PageResult<OaWorkReportRespVO> convertPage(PageResult<OaWorkReportDO> page) {
        return BeanUtils.toBean(page, OaWorkReportRespVO.class);
    }

    public static OaWorkReportDO convert(OaWorkReportCreateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaWorkReportDO.class);
    }

    public static OaWorkReportDO convert(OaWorkReportUpdateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaWorkReportDO.class);
    }

}

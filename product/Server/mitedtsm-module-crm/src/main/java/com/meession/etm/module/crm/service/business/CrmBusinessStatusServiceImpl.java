package com.meession.etm.module.crm.service.business;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.common.pojo.PageParam;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.business.vo.status.CrmBusinessStatusSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.business.CrmBusinessStatusDO;
import com.meession.etm.module.crm.dal.dataobject.business.CrmBusinessStatusTypeDO;
import com.meession.etm.module.crm.dal.mysql.business.CrmBusinessStatusMapper;
import com.meession.etm.module.crm.dal.mysql.business.CrmBusinessStatusTypeMapper;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertSet;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.diffList;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.*;

/**
 * 商机状态 Service 实现类
 *
 * @author ljlleo
 */
@Service
@Validated
public class CrmBusinessStatusServiceImpl implements CrmBusinessStatusService {

    @Resource
    private CrmBusinessStatusTypeMapper businessStatusTypeMapper;
    @Resource
    private CrmBusinessStatusMapper businessStatusMapper;

    @Resource
    @Lazy // 延迟加载，避免循环依赖
    private CrmBusinessService businessService;

    /**
     * 创建商机状态类型及状态列表
     *
     * @param createReqVO 创建请求
     * @return 商机状态类型编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createBusinessStatus(CrmBusinessStatusSaveReqVO createReqVO) {
        // 1.1 检验名称是否存在
        validateBusinessStatusTypeNameUnique(createReqVO.getName(), null);
        // 1.2 设置状态的排序
        int sort = 0;
        for (CrmBusinessStatusSaveReqVO.Status status : createReqVO.getStatuses()) {
            status.setSort(sort++);
        }

        // 2.1 插入类型
        CrmBusinessStatusTypeDO statusType = BeanUtils.toBean(createReqVO, CrmBusinessStatusTypeDO.class);
        businessStatusTypeMapper.insert(statusType);
        // 2.2 插入状态
        List<CrmBusinessStatusDO> statuses = BeanUtils.toBean(createReqVO.getStatuses(), CrmBusinessStatusDO.class,
                status -> status.setTypeId(statusType.getId()));
        businessStatusMapper.insertBatch(statuses);
        return statusType.getId();
    }

    /**
     * 更新商机状态类型及状态列表
     *
     * @param updateReqVO 更新请求
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBusinessStatus(CrmBusinessStatusSaveReqVO updateReqVO) {
        // 1.1 校验存在
        validateBusinessStatusTypeExists(updateReqVO.getId());
        // 1.2 校验名称是否存在
        validateBusinessStatusTypeNameUnique(updateReqVO.getName(), updateReqVO.getId());
        // 1.3 设置状态的排序
        int sort = 0;
        for (CrmBusinessStatusSaveReqVO.Status status : updateReqVO.getStatuses()) {
            status.setSort(sort++);
        }
        // 1.4 已经使用，无法更新
        if (businessService.getBusinessCountByStatusTypeId(updateReqVO.getId()) > 0) {
            throw exception(BUSINESS_STATUS_UPDATE_FAIL_USED);
        }

        // 2.1 更新类型
        CrmBusinessStatusTypeDO updateObj = BeanUtils.toBean(updateReqVO, CrmBusinessStatusTypeDO.class);
        businessStatusTypeMapper.updateById(updateObj);
        // 2.2 更新状态
        updateBusinessStatus(updateReqVO.getId(), BeanUtils.toBean(updateReqVO.getStatuses(), CrmBusinessStatusDO.class));
    }

    /**
     * 更新商机状态列表（差量更新）
     *
     * @param id 商机状态类型编号
     * @param newList 新状态列表
     */
    private void updateBusinessStatus(Long id, List<CrmBusinessStatusDO> newList) {
        List<CrmBusinessStatusDO> oldList = businessStatusMapper.selectListByTypeId(id);
        List<List<CrmBusinessStatusDO>> diffList = diffList(oldList, newList, // id 不同，就认为是不同的记录
                (oldVal, newVal) -> oldVal.getId().equals(newVal.getId()));
        if (CollUtil.isNotEmpty(diffList.get(0))) {
            diffList.get(0).forEach(o -> o.setTypeId(id));
            businessStatusMapper.insertBatch(diffList.get(0));
        }
        if (CollUtil.isNotEmpty(diffList.get(1))) {
            businessStatusMapper.updateBatch(diffList.get(1));
        }
        if (CollUtil.isNotEmpty(diffList.get(2))) {
            businessStatusMapper.deleteByIds(convertSet(diffList.get(2), CrmBusinessStatusDO::getId));
        }
    }

    /**
     * 校验商机状态类型是否存在
     *
     * @param id 商机状态类型编号
     */
    private void validateBusinessStatusTypeExists(Long id) {
        if (businessStatusTypeMapper.selectById(id) == null) {
            throw exception(BUSINESS_STATUS_TYPE_NOT_EXISTS);
        }
    }

    /**
     * 校验商机状态类型名称是否唯一
     *
     * @param name 名称
     * @param id 排除的编号（更新时使用）
     */
    private void validateBusinessStatusTypeNameUnique(String name, Long id) {
        CrmBusinessStatusTypeDO statusType = businessStatusTypeMapper.selectByName(name);
        if (statusType == null
                || statusType.getId().equals(id)) {
            return;
        }
        throw exception(BUSINESS_STATUS_TYPE_NAME_EXISTS);
    }

    /**
     * 删除商机状态类型及关联的状态列表
     *
     * @param id 商机状态类型编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBusinessStatusType(Long id) {
        // 1.1 校验存在
        validateBusinessStatusTypeExists(id);
        // 1.2 已经使用，无法更新
        if (businessService.getBusinessCountByStatusTypeId(id) > 0) {
            throw exception(BUSINESS_STATUS_DELETE_FAIL_USED);
        }

        // 2.1 删除类型
        businessStatusTypeMapper.deleteById(id);
        // 2.2 删除状态
        businessStatusMapper.deleteByTypeId(id);
    }

    /**
     * 查询商机状态类型详情
     *
     * @param id 商机状态类型编号
     * @return 商机状态类型
     */
    @Override
    public CrmBusinessStatusTypeDO getBusinessStatusType(Long id) {
        return businessStatusTypeMapper.selectById(id);
    }

    /**
     * 校验商机状态类型是否存在
     *
     * @param id 商机状态类型编号
     */
    @Override
    public void validateBusinessStatusType(Long id) {
        validateBusinessStatusTypeExists(id);
    }

    /**
     * 查询所有商机状态类型列表
     *
     * @return 商机状态类型列表
     */
    @Override
    public List<CrmBusinessStatusTypeDO> getBusinessStatusTypeList() {
        return businessStatusTypeMapper.selectList();
    }

    /**
     * 分页查询商机状态类型
     *
     * @param pageReqVO 分页请求
     * @return 分页结果
     */
    @Override
    public PageResult<CrmBusinessStatusTypeDO> getBusinessStatusTypePage(PageParam pageReqVO) {
        return businessStatusTypeMapper.selectPage(pageReqVO);
    }

    /**
     * 根据编号集合查询商机状态类型列表
     *
     * @param ids 商机状态类型编号集合
     * @return 商机状态类型列表
     */
    @Override
    public List<CrmBusinessStatusTypeDO> getBusinessStatusTypeList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return businessStatusTypeMapper.selectByIds(ids);
    }

    /**
     * 根据类型编号查询商机状态列表（按排序升序）
     *
     * @param typeId 类型编号
     * @return 商机状态列表
     */
    @Override
    public List<CrmBusinessStatusDO> getBusinessStatusListByTypeId(Long typeId) {
        List<CrmBusinessStatusDO> list = businessStatusMapper.selectListByTypeId(typeId);
        list.sort(Comparator.comparingInt(CrmBusinessStatusDO::getSort));
        return list;
    }

    /**
     * 根据编号集合查询商机状态列表
     *
     * @param ids 商机状态编号集合
     * @return 商机状态列表
     */
    @Override
    public List<CrmBusinessStatusDO> getBusinessStatusList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return businessStatusMapper.selectByIds(ids);
    }

    /**
     * 查询商机状态详情
     *
     * @param id 商机状态编号
     * @return 商机状态
     */
    @Override
    public CrmBusinessStatusDO getBusinessStatus(Long id) {
        return businessStatusMapper.selectById(id);
    }

    /**
     * 校验商机状态是否存在
     *
     * @param statusTypeId 商机状态类型编号
     * @param statusId 商机状态编号
     * @return 商机状态
     */
    @Override
    public CrmBusinessStatusDO validateBusinessStatus(Long statusTypeId, Long statusId) {
        CrmBusinessStatusDO status = businessStatusMapper.selectByTypeIdAndId(statusTypeId, statusId);
        if (status == null) {
            throw exception(BUSINESS_STATUS_NOT_EXISTS);
        }
        return status;
    }

}

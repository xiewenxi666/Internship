package com.meession.etm.module.oa.service.task;

import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.oa.vo.task.OaTaskCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.task.OaTaskPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaTaskDO;
import com.meession.etm.module.oa.dal.mapper.OaTaskMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_TASK_NOT_EXISTS;

@Slf4j
@Service
@Validated
public class OaTaskServiceImpl implements OaTaskService {

    @Resource
    private OaTaskMapper taskMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTask(Long userId, OaTaskCreateReqVO createReqVO) {
        OaTaskDO task = BeanUtils.toBean(createReqVO, OaTaskDO.class)
                .setUserId(userId);
        taskMapper.insert(task);
        return task.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTask(OaTaskDO task) {
        validateTaskExists(task.getId());
        taskMapper.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTask(Long id) {
        validateTaskExists(id);
        taskMapper.deleteById(id);
    }

    private void validateTaskExists(Long id) {
        if (taskMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(OA_TASK_NOT_EXISTS);
        }
    }

    @Override
    public OaTaskDO getTask(Long id) {
        return taskMapper.selectById(id);
    }

    @Override
    public PageResult<OaTaskDO> getTaskPage(Long userId, OaTaskPageReqVO pageReqVO) {
        return taskMapper.selectPage(userId, pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeTask(Long id) {
        OaTaskDO task = taskMapper.selectById(id);
        if (task == null) {
            throw ServiceExceptionUtil.exception(OA_TASK_NOT_EXISTS);
        }
        taskMapper.updateById(new OaTaskDO().setId(id).setStatus(3).setCompletedTime(LocalDateTime.now()));
    }

}

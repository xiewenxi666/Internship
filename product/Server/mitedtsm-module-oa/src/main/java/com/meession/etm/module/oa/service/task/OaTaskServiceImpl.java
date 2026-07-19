package com.meession.etm.module.oa.service.task;

import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.task.vo.OaTaskCreateReqVO;
import com.meession.etm.module.oa.controller.admin.task.vo.OaTaskPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaTaskDO;
import com.meession.etm.module.oa.dal.mysql.OaTaskMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_TASK_NOT_EXISTS;

@Service
@Validated
public class OaTaskServiceImpl implements OaTaskService {

    @Resource
    private OaTaskMapper taskMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTask(Long userId, OaTaskCreateReqVO createReqVO) {
        OaTaskDO task = BeanUtils.toBean(createReqVO, OaTaskDO.class)
                .setUserId(userId).setStatus(0).setProgress(0);
        taskMapper.insert(task);
        return task.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTask(Long id, OaTaskCreateReqVO updateReqVO) {
        validateExists(id);
        OaTaskDO task = BeanUtils.toBean(updateReqVO, OaTaskDO.class).setId(id);
        taskMapper.updateById(task);
    }

    @Override
    public List<OaTaskDO> getTaskListByAssignee(Long assigneeUserId, Integer status) {
        return taskMapper.selectListByAssignee(assigneeUserId, status);
    }

    @Override
    public Map<String, Long> getTaskStatistics(Long userId) {
        List<OaTaskDO> allTasks = taskMapper.selectList(new com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX<OaTaskDO>()
                .eq(OaTaskDO::getUserId, userId));
        long total = allTasks.size();
        long todo = allTasks.stream().filter(t -> t.getStatus() == 0).count();
        long inProgress = allTasks.stream().filter(t -> t.getStatus() == 1).count();
        long completed = allTasks.stream().filter(t -> t.getStatus() == 2).count();
        long overdue = allTasks.stream().filter(t -> t.getStatus() != 2 && t.getDeadline() != null
                && t.getDeadline().isBefore(LocalDateTime.now())).count();
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("todo", todo);
        stats.put("inProgress", inProgress);
        stats.put("completed", completed);
        stats.put("overdue", overdue);
        return stats;
    }

    @Override
    public void deleteTask(Long id) {
        validateExists(id);
        taskMapper.deleteById(id);
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
    public void updateTaskStatus(Long id, Integer status) {
        validateExists(id);
        taskMapper.updateById(new OaTaskDO().setId(id).setStatus(status));
    }

    @Override
    public void updateTaskProgress(Long id, Integer progress) {
        validateExists(id);
        taskMapper.updateById(new OaTaskDO().setId(id).setProgress(progress));
    }

    @Override
    public void completeTask(Long id) {
        validateExists(id);
        taskMapper.updateById(new OaTaskDO().setId(id)
                .setStatus(2).setProgress(100).setCompletedTime(LocalDateTime.now()));
    }

    private void validateExists(Long id) {
        if (taskMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(OA_TASK_NOT_EXISTS);
        }
    }

}
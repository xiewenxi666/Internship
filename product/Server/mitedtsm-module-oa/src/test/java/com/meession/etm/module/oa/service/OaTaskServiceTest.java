package com.meession.etm.module.oa.service;

import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.oa.controller.admin.task.vo.OaTaskCreateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaTaskDO;
import com.meession.etm.module.oa.dal.mysql.OaTaskMapper;
import com.meession.etm.module.oa.service.task.OaTaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.meession.etm.framework.test.core.util.RandomUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@Import(OaTaskServiceImpl.class)
public class OaTaskServiceTest extends BaseDbUnitTest {

    @Resource
    private OaTaskServiceImpl taskService;

    @Resource
    private OaTaskMapper taskMapper;

    @Test
    public void testCreateTask_success() {
        OaTaskCreateReqVO reqVO = randomPojo(OaTaskCreateReqVO.class, o -> {
            o.setPriority(1);
        });
        Long userId = randomLongId();

        Long taskId = taskService.createTask(userId, reqVO);

        assertNotNull(taskId);
        OaTaskDO task = taskMapper.selectById(taskId);
        assertEquals(userId, task.getUserId());
        assertEquals(0, task.getStatus());
        assertEquals(0, task.getProgress());
    }

    @Test
    public void testUpdateTask_success() {
        OaTaskDO dbTask = randomPojo(OaTaskDO.class, o -> {
            o.setPriority(1);
        });
        taskMapper.insert(dbTask);

        OaTaskCreateReqVO updateReqVO = randomPojo(OaTaskCreateReqVO.class, o -> {
            o.setPriority(1);
        });

        taskService.updateTask(dbTask.getId(), updateReqVO);

        OaTaskDO updated = taskMapper.selectById(dbTask.getId());
        assertPojoEquals(updateReqVO, updated, "id", "userId", "status", "progress",
                "completedTime",
                "creator", "createTime", "updater", "updateTime", "deleted", "tenantId");
    }

    @Test
    public void testCompleteTask() {
        OaTaskDO dbTask = randomPojo(OaTaskDO.class, o -> o.setStatus(1).setProgress(50).setPriority(1));
        taskMapper.insert(dbTask);

        taskService.completeTask(dbTask.getId());

        OaTaskDO updated = taskMapper.selectById(dbTask.getId());
        assertEquals(2, (int) updated.getStatus());
        assertEquals(100, (int) updated.getProgress());
        assertNotNull(updated.getCompletedTime());
    }

    @Test
    public void testUpdateTaskProgress() {
        OaTaskDO dbTask = randomPojo(OaTaskDO.class, o -> o.setProgress(0).setPriority(1));
        taskMapper.insert(dbTask);

        taskService.updateTaskProgress(dbTask.getId(), 80);

        OaTaskDO updated = taskMapper.selectById(dbTask.getId());
        assertEquals(80, (int) updated.getProgress());
    }

    @Test
    public void testGetTaskListByAssignee() {
        Long assigneeId = randomLongId();
        taskMapper.insert(randomPojo(OaTaskDO.class, o -> o.setAssigneeUserId(assigneeId).setStatus(0).setPriority(1)));
        taskMapper.insert(randomPojo(OaTaskDO.class, o -> o.setAssigneeUserId(assigneeId).setStatus(1).setPriority(1)));

        List<OaTaskDO> list = taskService.getTaskListByAssignee(assigneeId, null);

        assertEquals(2, list.size());
    }

    @Test
    public void testGetTaskStatistics() {
        Long userId = randomLongId();
        taskMapper.insert(randomPojo(OaTaskDO.class, o -> o.setUserId(userId).setStatus(0).setPriority(1)));
        taskMapper.insert(randomPojo(OaTaskDO.class, o -> o.setUserId(userId).setStatus(1).setPriority(1)));
        taskMapper.insert(randomPojo(OaTaskDO.class, o -> o.setUserId(userId).setStatus(2).setPriority(1)));

        Map<String, Long> stats = taskService.getTaskStatistics(userId);

        assertEquals(3, stats.get("total"));
        assertEquals(1, stats.get("todo"));
        assertEquals(1, stats.get("inProgress"));
        assertEquals(1, stats.get("completed"));
    }

    @Test
    public void testDeleteTask_success() {
        OaTaskDO dbTask = randomPojo(OaTaskDO.class, o -> o.setPriority(1));
        taskMapper.insert(dbTask);

        taskService.deleteTask(dbTask.getId());

        assertNull(taskMapper.selectById(dbTask.getId()));
    }
}

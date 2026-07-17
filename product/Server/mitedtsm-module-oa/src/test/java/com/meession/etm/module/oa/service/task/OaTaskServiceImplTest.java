package com.meession.etm.module.oa.service.task;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.oa.controller.admin.oa.vo.task.OaTaskCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.task.OaTaskPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaTaskDO;
import com.meession.etm.module.oa.dal.mapper.OaTaskMapper;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import jakarta.annotation.Resource;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomLongId;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomPojo;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_TASK_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

@Import(OaTaskServiceImpl.class)
class OaTaskServiceImplTest extends BaseDbUnitTest {

    @Resource
    private OaTaskServiceImpl taskService;

    @Resource
    private OaTaskMapper taskMapper;

    @Test
    void testCreateTask_success() {
        OaTaskCreateReqVO reqVO = randomPojo(OaTaskCreateReqVO.class);
        Long userId = randomLongId();

        Long id = taskService.createTask(userId, reqVO);

        assertNotNull(id);
        OaTaskDO task = taskMapper.selectById(id);
        assertNotNull(task);
        assertEquals(userId, task.getUserId());
    }

    @Test
    void testGetTask_success() {
        OaTaskDO dbTask = randomPojo(OaTaskDO.class);
        taskMapper.insert(dbTask);

        OaTaskDO task = taskService.getTask(dbTask.getId());

        assertNotNull(task);
        assertPojoEquals(dbTask, task);
    }

    @Test
    void testGetTask_notExists() {
        assertNull(taskService.getTask(randomLongId()));
    }

    @Test
    void testGetTaskPage_success() {
        OaTaskDO dbTask = randomPojo(OaTaskDO.class, o -> o.setStatus(1));
        taskMapper.insert(dbTask);
        taskMapper.insert(randomPojo(OaTaskDO.class, o -> {
            o.setUserId(dbTask.getUserId());
            o.setStatus(2);
        }));
        OaTaskDO otherUser = randomPojo(OaTaskDO.class);
        taskMapper.insert(otherUser);

        OaTaskPageReqVO reqVO = new OaTaskPageReqVO();
        reqVO.setStatus(1);

        PageResult<OaTaskDO> result = taskService.getTaskPage(dbTask.getUserId(), reqVO);

        assertEquals(1, result.getTotal());
        assertPojoEquals(dbTask, result.getList().get(0));
    }

    @Test
    void testGetTaskPage_noFilter() {
        OaTaskDO dbTask = randomPojo(OaTaskDO.class);
        taskMapper.insert(dbTask);
        taskMapper.insert(randomPojo(OaTaskDO.class, o -> o.setUserId(dbTask.getUserId())));

        OaTaskPageReqVO reqVO = new OaTaskPageReqVO();

        PageResult<OaTaskDO> result = taskService.getTaskPage(dbTask.getUserId(), reqVO);

        assertEquals(2, result.getTotal());
    }

    @Test
    void testUpdateTask_success() {
        OaTaskDO dbTask = randomPojo(OaTaskDO.class);
        taskMapper.insert(dbTask);

        OaTaskDO update = new OaTaskDO();
        update.setId(dbTask.getId());
        update.setTitle("Updated title");
        taskService.updateTask(update);

        OaTaskDO task = taskMapper.selectById(dbTask.getId());
        assertEquals("Updated title", task.getTitle());
    }

    @Test
    void testUpdateTask_notExists() {
        assertServiceException(() ->
                        taskService.updateTask(new OaTaskDO().setId(randomLongId())),
                OA_TASK_NOT_EXISTS);
    }

    @Test
    void testDeleteTask_success() {
        OaTaskDO dbTask = randomPojo(OaTaskDO.class);
        taskMapper.insert(dbTask);

        taskService.deleteTask(dbTask.getId());

        assertNull(taskMapper.selectById(dbTask.getId()));
    }

    @Test
    void testDeleteTask_notExists() {
        assertServiceException(() ->
                        taskService.deleteTask(randomLongId()),
                OA_TASK_NOT_EXISTS);
    }

    @Test
    void testCompleteTask_success() {
        OaTaskDO dbTask = randomPojo(OaTaskDO.class, o -> o.setStatus(1));
        taskMapper.insert(dbTask);

        taskService.completeTask(dbTask.getId());

        OaTaskDO task = taskMapper.selectById(dbTask.getId());
        assertEquals(3, task.getStatus());
        assertNotNull(task.getCompletedTime());
    }

    @Test
    void testCompleteTask_notExists() {
        assertServiceException(() ->
                        taskService.completeTask(randomLongId()),
                OA_TASK_NOT_EXISTS);
    }
}

# 章节 C：OA 域开发规则

## 1. 模块架构

### 1.1 模块定位

- OA 域为**独立模块** `mitedtsm-module-oa`，不嵌套在 BPM 或 CRM 模块中
- 单模块结构（**不拆分** api/biz），VO 直接放在 `controller/admin/oa/vo/` 下
- package 路径：`com.meession.etm.module.oa`
- 依赖关系：`module-oa` → `module-bpm`（审批流）、`module-system`（用户/通知/消息）、`module-infra`（文件管理）

### 1.2 模块目录结构

```
mitedtsm-module-oa/
└── src/main/java/com/meession/etm/module/oa/
    ├── controller/
    │   └── admin/
    │       ├── oa/
    │       │   ├── leave/
    │       │   │   └── OaLeaveController.java
    │       │   ├── trip/
    │       │   │   └── OaTripController.java
    │       │   ├── loan/
    │       │   │   └── OaLoanController.java
    │       │   ├── visit/
    │       │   │   └── OaVisitController.java
    │       │   ├── request/
    │       │   │   └── OaRequestController.java
    │       │   ├── report/
    │       │   │   └── OaReportController.java
    │       │   ├── schedule/
    │       │   │   └── OaScheduleController.java
    │       │   ├── task/
    │       │   │   └── OaTaskController.java
    │       │   └── document/
    │       │       └── OaDocumentController.java
    │       └── vo/
    │           ├── leave/
    │           │   ├── OaLeaveCreateReqVO.java
    │           │   ├── OaLeavePageReqVO.java
    │           │   └── OaLeaveRespVO.java
    │           ├── trip/...
    │           ├── loan/...
    │           ├── visit/...
    │           ├── request/...
    │           ├── report/...
    │           ├── schedule/...
    │           ├── task/...
    │           └── document/...
    ├── service/
    │   ├── leave/
    │   │   ├── OaLeaveService.java
    │   │   └── OaLeaveServiceImpl.java
    │   ├── trip/...
    │   ├── loan/...
    │   ├── visit/...
    │   ├── request/...
    │   ├── report/...
    │   ├── schedule/...
    │   ├── task/...
    │   └── document/...
    ├── service/listener/
    │   ├── OaLeaveStatusListener.java
    │   ├── OaTripStatusListener.java
    │   ├── OaLoanStatusListener.java
    │   ├── OaVisitStatusListener.java
    │   └── OaRequestStatusListener.java
    ├── dal/
    │   ├── dataobject/
    │   │   ├── OaLeaveDO.java
    │   │   ├── OaTripDO.java
    │   │   ├── OaLoanDO.java
    │   │   ├── OaVisitDO.java
    │   │   ├── OaRequestDO.java
    │   │   ├── OaReportDO.java
    │   │   ├── OaScheduleDO.java
    │   │   ├── OaTaskDO.java
    │   │   └── OaDocumentDO.java
    │   └── mapper/
    │       ├── OaLeaveMapper.java
    │       ├── OaTripMapper.java
    │       ├── OaLoanMapper.java
    │       ├── OaVisitMapper.java
    │       ├── OaRequestMapper.java
    │       ├── OaReportMapper.java
    │       ├── OaScheduleMapper.java
    │       ├── OaTaskMapper.java
    │       └── OaDocumentMapper.java
    ├── enums/
    │   ├── OaLeaveTypeEnum.java
    │   ├── OaTripTypeEnum.java
    │   ├── OaLoanPurposeEnum.java
    │   ├── OaVisitTypeEnum.java
    │   ├── OaReportTypeEnum.java
    │   └── OaScheduleTypeEnum.java
    └── job/
        └── OaScheduleRemindJob.java
```

## 2. 命名规范

### 2.1 类名前缀

| 层次 | 命名规则 | 示例 |
|------|---------|------|
| DO | `Oa{Business}DO` | `OaLeaveDO` |
| Mapper | `Oa{Business}Mapper` | `OaLeaveMapper` |
| Service 接口 | `Oa{Business}Service` | `OaLeaveService` |
| Service 实现 | `Oa{Business}ServiceImpl` | `OaLeaveServiceImpl` |
| Controller | `Oa{Business}Controller` | `OaLeaveController` |
| StatusListener | `Oa{Business}StatusListener` | `OaLeaveStatusListener` |
| CreateReqVO | `Oa{Business}CreateReqVO` | `OaLeaveCreateReqVO` |
| PageReqVO | `Oa{Business}PageReqVO` | `OaLeavePageReqVO` |
| RespVO | `Oa{Business}RespVO` | `OaLeaveRespVO` |

### 2.2 数据表命名

- 所有 OA 表使用 `oa_` 前缀：`oa_leave`、`oa_trip`、`oa_loan`、`oa_visit`、`oa_request`、`oa_report`、`oa_schedule`、`oa_task`、`oa_document`
- 多租户表继承 `TenantBaseDO`（含 `tenant_id`）
- 系统字段标准同章节 A（`creator`/`create_time`/`updater`/`update_time`/`deleted`）

### 2.3 请求映射路径

- Admin 路径：`/admin-api/oa/{business}` 如 `/admin-api/oa/leave`
- 权限前缀：`oa:{business}:{action}` 如 `oa:leave:create`

## 3. 代码开发规范

### 3.1 对象转换

- **不使用 MapStruct**，统一使用 `BeanUtils.toBean()` / `BeanUtils.toBeanList()`（来自 `com.meession.etm.framework.common.util.object.BeanUtils`）
- VO 类直接放在 `controller/admin/oa/vo/{business}/` 下，不拆分单独 DTO 模块

### 3.2 BPM 审批流接入模式（所有 BPM 单据统一模式）

所有需要审批的 OA 单据（请假/出差/借款/拜访/请示）遵循以下模式：

**Service 层**：`create` 方法中调用 `BpmProcessInstanceApi.createProcessInstance()` 发起流程

```java
public Long create(Long userId, OaXxxCreateReqVO reqVO) {
    OaXxxDO entity = BeanUtils.toBean(reqVO, OaXxxDO.class)
            .setUserId(userId)
            .setStatus(BpmTaskStatusEnum.RUNNING.getStatus());
    mapper.insert(entity);

    Map<String, Object> variables = new HashMap<>();
    String processInstanceId = processInstanceApi.createProcessInstance(userId,
            new BpmProcessInstanceCreateReqDTO()
                    .setProcessDefinitionKey(PROCESS_KEY)
                    .setVariables(variables)
                    .setBusinessKey(String.valueOf(entity.getId()))
                    .setStartUserSelectAssignees(reqVO.getStartUserSelectAssignees()));

    mapper.updateById(new OaXxxDO().setId(entity.getId()).setProcessInstanceId(processInstanceId));
    return entity.getId();
}
```

**监听器层**：继承 `BpmProcessInstanceStatusEventListener`，在审批完成时更新单据状态

```java
@Component
public class OaXxxStatusListener extends BpmProcessInstanceStatusEventListener {
    @Resource private OaXxxService xxxService;

    @Override
    protected String getProcessDefinitionKey() {
        return OaXxxServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        xxxService.updateStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
    }
}
```

### 3.3 BPMN 流程定义

- 流程定义 KEY 统一前缀 `oa_`：`oa_leave`、`oa_trip`、`oa_loan`、`oa_visit`、`oa_request`
- BPMN 文件存放在 `resources/process/oa/` 目录下

### 3.4 服务接口标准方法

每个 OA 业务 Service 接口必须包含以下方法：

```java
interface OaXxxService {
    Long create(Long userId, OaXxxCreateReqVO reqVO);
    void update(OaXxxUpdateReqVO reqVO);
    void delete(Long id);
    OaXxxRespVO get(Long id);
    PageResult<OaXxxRespVO> page(OaXxxPageReqVO reqVO, Long userId);
    void updateStatus(Long id, Integer status);
}
```

### 3.5 不需 BPM 审批的单据

工作报告、日程、任务、文档 4 项不需要 BPM 审批流，仅做 CRUD：
- 不需要 `StatusListener`
- 不需要 `PROCESS_KEY`
- Service 中无需调用 `BpmProcessInstanceApi`
- Controller 中不加 `startUserSelectAssignees` 字段

## 4. 数据库设计规范

### 4.1 OA 公共字段（所有 DO 继承 TenantBaseDO）

id (BIGINT AUTO_INCREMENT)
tenant_id, creator, create_time, updater, update_time, deleted

### 4.2 BPM 审批单据特有字段

```sql
`user_id`           BIGINT      NOT NULL  COMMENT '申请人ID',
`status`            TINYINT     NOT NULL  DEFAULT 0  COMMENT '审批状态: 1-审批中 2-通过 3-不通过 4-已取消',
`process_instance_id` VARCHAR(64) DEFAULT NULL COMMENT '流程实例ID',
```

### 4.3 数据字典

- OA 类型枚举尽量使用 `system_dict` 字典管理，不新建 Java 枚举（减少编译依赖）
- 字典类型前缀 `oa_`：`oa_leave_type`、`oa_trip_type`、`oa_loan_purpose`、`oa_visit_type`、`oa_report_type`
- SQL 文件中统一插入字典数据

## 5. 前端开发规范

### 5.1 页面目录

```
src/views/oa/
├── leave/
│   ├── index.vue
│   ├── create.vue
│   └── detail.vue
├── trip/...
├── loan/...
├── visit/...
├── request/...
├── report/
│   ├── index.vue
│   └── create.vue
├── schedule/
│   └── index.vue
├── task/
│   ├── index.vue
│   └── create.vue
└── document/
    ├── index.vue
    └── create.vue
```

### 5.2 页面模板规范

- 所有 BPM 审批单据页面（leave/trip/loan/visit/request）严格参照现有 `bpm/oa/leave/` 目录的 3 个页面模板
- 批量生成策略：替换 `leave` → `trip`/`loan`/`visit`/`request`，同步替换 API 路径和 i18n key
- 通用审批弹窗统一使用 BPM 模块已有的 `ProcessInstanceDetail` 组件（路径：`@/views/bpm/processInstance/detail/index.vue`）

### 5.3 API 文件

- API 文件路径：`src/api/oa/{business}/index.ts`
- 命名规则：`Oa{business}Api` 如 `OaLeaveApi`

## 6. 遗留代码迁移规则

### 6.1 BPM 模块中的 OA Demo 处理

现有 `BpmOALeave*` 系列文件（BPM 模块自带的请假示例）按以下方式处理：

1. **迁移**：将 10 个 Java 文件 + 3 个 Vue 文件复制到 `module-oa`，改包名 `com.meession.etm.module.bpm` → `com.meession.etm.module.oa`，改类名 `BpmOALeave` → `OaLeave`
2. **适配**：将 `@TableName("bpm_oa_leave")` 改为 `@TableName("oa_leave")`，路由 `/bpm/oa/leave` → `/admin-api/oa/leave`，权限 `bpm:oa-leave:*` → `oa:leave:*`
3. **删除**：迁移完成后删除 BPM 模块中所有 `BpmOALeave*` 文件和相关字典/菜单 SQL
4. **注意**：不改造 Demo 的逻辑模式（BeanUtils 方式），保持与新模块一致

### 6.2 数据库表迁移

- `bpm_oa_leave` 表重命名为 `oa_leave`，字段不变
- 原字典数据 `bpm_oa_leave_type` 保留不动（可被 `oa_leave_type` 覆盖）

## 7. 交付优先级定义

### P0（必须交付 — 核心 7 项）

| # | 模块 | 后端 | 前端 | BPM | 测试 |
|---|------|------|------|-----|------|
| 1 | 请假 | 迁移 | 迁移 | ✅ | Service + Controller |
| 2 | 出差 | 新建 | 批量 | ✅ | Service + Controller |
| 3 | 借款 | 新建 | 批量 | ✅ | Service + Controller |
| 4 | 拜访 | 新建 | 批量 | ✅ | Service + Controller |
| 5 | 请示 | 新建 | 批量 | ✅ | Service + Controller |
| 6 | 工作报告 | 新建 | 新建 | ❌ 无 | Service |
| 7 | 日程 | 新建 | 日历 | ❌ 无 | Service |

### P1（基础版交付 — 3 项）

| # | 模块 | 交付标准 |
|---|------|---------|
| 8 | 任务管理 | DO/Mapper/Service CRUD + 列表/创建页面，不涉及指派流转 |
| 9 | 文档目录 | DO/Mapper/Service（目录树）+ 前端文件列表，文件存储复用 `infra_file` |
| 10 | 内部消息 | 审批通知自动调用 `NotifySendService.sendSingleNotifyToAdmin()` + 消息模板种子 SQL |

### P2（当前版本不交付）

- 任务流转/指派/依赖
- 文档版本管理/全文搜索
- 日历订阅/共享/提醒
- App 移动端接口

## 8. 测试规范（OA 域专有）

### 8.1 测试覆盖目标

| 天数 | Service 层 | Controller 层 | Mapper 层 | 说明 |
|------|-----------|--------------|-----------|------|
| Day1 交付时 | ≥ 50% 核心方法 | ≥ 50% 核心接口 | ≥ 30% | 主流程覆盖 |
| Day2 交付时 | ≥ 70% | ≥ 70% | ≥ 50% | 含异常场景 |

### 8.2 测试重点

- **BPM 审批单据**：Mock `BpmProcessInstanceApi`，验证 `create` 方法正确调用发起流程、`updateStatus` 正确响应回调
- **非审批单据**：验证 CRUD 完整链路、逻辑删除生效、分页查询正确
- **Controller**：验证参数校验（`@Valid`）、权限注解（`@PreAuthorize`）、响应结构

### 8.3 BPM 流程测试验证清单

```
□ 申请人创建单据 → BPM 流程发起成功
□ 审批人审批通过 → 单据状态更新为 APPROVED
□ 审批人审批驳回 → 单据状态更新为 REJECTED
□ 申请人撤销 → 单据状态更新为 CANCELLED
□ 分页查询过滤状态/时间范围
□ 权限不足时返回 403
```

## 9. 批量生成策略

### 9.1 后端批量生成顺序

以 `OaLeave` 为模板 → 批量替换生成：

```
Step 1: OaLeaveDO → OaTripDO / OaLoanDO / OaVisitDO / OaRequestDO / OaReportDO / OaScheduleDO / OaTaskDO / OaDocumentDO
Step 2: OaLeaveMapper → 对应 Mapper
Step 3: OaLeaveService + OaLeaveServiceImpl → 对应 Service
Step 4: OaLeaveStatusListener → OaTrip/Loan/Visit/Request StatusListener
Step 5: OaLeaveController → 对应 Controller
Step 6: OaLeaveCreateReqVO / PageReqVO / RespVO → 对应 VO
```

### 9.2 前端批量生成顺序

以 `views/bpm/oa/leave/` 目录下 3 个 Vue 文件为模板：

```
Step 1: leave/index.vue → trip/loan/visit/request/report 的 index.vue
Step 2: leave/create.vue → trip/loan/visit/request/report 的 create.vue
Step 3: leave/detail.vue → trip/loan/visit/request 的 detail.vue
Step 4: 调整各模块特有的字段差异
```

### 9.3 替换映射表

| 占位符 | 替换值列表 |
|--------|-----------|
| `{business}` | leave, trip, loan, visit, request, report, schedule, task, document |
| `{Business}` | Leave, Trip, Loan, Visit, Request, Report, Schedule, Task, Document |
| `{BUSINESS}` | LEAVE, TRIP, LOAN, VISIT, REQUEST, REPORT, SCHEDULE, TASK, DOCUMENT |
| `{hasBpm}` | leave/trip/loan/visit/request → true; report/schedule/task/document → false |
| `{route}` | leave, trip, loan, visit, request, report, schedule, task, document |
| `{tableName}` | oa_leave, oa_trip, oa_loan, oa_visit, oa_request, oa_report, oa_schedule, oa_task, oa_document |
| `{permission}` | oa:leave, oa:trip, oa:loan, oa:visit, oa:request, oa:report, oa:schedule, oa:task, oa:document |

## 10. 章节 A 的 CRM 通用规则适配说明

OA 域遵循章节 B（项目开发约束）的所有全局规则，但以下条款 OA 域有自身特化：

| 章节 B 规则 | OA 域适配 |
|------------|-----------|
| 测试覆盖率第1天≥50% | OA 域 Day1 目标为 Service 层核心方法 ≥ 50% |
| 测试覆盖率第3天≥90% | OA 域 2 天交付，Day2 目标为 Service + Controller ≥ 70% |
| 使用 MapStruct | OA 域统一使用 `BeanUtils.toBean()`，不引入 MapStruct |
| DO 需子包分类 | OA 域 DO 扁平化放 `dal/dataobject/`，不分子包 |

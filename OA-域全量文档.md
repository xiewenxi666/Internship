# OA 域全量文档

> 模块路径：`Server/mitedtsm-module-oa`
> package：`com.meession.etm.module.oa`

---

## 一、模块架构

### 1.1 依赖关系

```
mitedtsm-module-oa
  ├── mitedtsm-module-system   (用户、通知、消息)
  ├── mitedtsm-module-infra    (文件管理)
  └── mitedtsm-module-bpm      (审批流 API / 事件监听)
```

### 1.2 目录结构

```
mitedtsm-module-oa/
└── src/main/java/com/meession/etm/module/oa/
    ├── controller/admin/oa/
    │   ├── leave/OaLeaveController.java
    │   ├── trip/OaTripController.java
    │   ├── loan/OaLoanController.java
    │   ├── visit/OaVisitController.java
    │   ├── request/OaRequestController.java
    │   ├── report/OaReportController.java
    │   ├── schedule/OaScheduleController.java
    │   ├── task/OaTaskController.java
    │   └── document/OaDocumentController.java
    │   └── vo/                         # 扁平化 VO
    │       ├── leave/OaLeave{CreateReq,PageReq,Resp}VO.java
    │       ├── trip/OaTrip{CreateReq,PageReq,Resp}VO.java
    │       ├── loan/OaLoan{CreateReq,PageReq,Resp}VO.java
    │       └── ... (visit/request/report/schedule/task/document)
    ├── service/
    │   ├── leave/OaLeaveService.java + OaLeaveServiceImpl.java
    │   ├── trip/OaTripService.java + OaTripServiceImpl.java
    │   ├── loan/OaLoanService.java + OaLoanServiceImpl.java
    │   ├── visit/OaVisitService.java + OaVisitServiceImpl.java
    │   ├── request/OaRequestService.java + OaRequestServiceImpl.java
    │   ├── report/OaReportService.java + OaReportServiceImpl.java
    │   ├── schedule/OaScheduleService.java + OaScheduleServiceImpl.java
    │   ├── task/OaTaskService.java + OaTaskServiceImpl.java
    │   └── document/OaDocumentService.java + OaDocumentServiceImpl.java
    ├── service/listener/                    # BPM 审批状态监听器
    │   ├── OaLeaveStatusListener.java
    │   ├── OaTripStatusListener.java
    │   ├── OaLoanStatusListener.java
    │   ├── OaVisitStatusListener.java
    │   └── OaRequestStatusListener.java
    ├── dal/dataobject/                      # 扁平化 DO
    │   ├── OaLeaveDO.java
    │   ├── OaTripDO.java
    │   ├── OaLoanDO.java
    │   ├── OaVisitDO.java
    │   ├── OaRequestDO.java
    │   ├── OaReportDO.java
    │   ├── OaScheduleDO.java
    │   ├── OaTaskDO.java
    │   └── OaDocumentDO.java
    └── dal/mapper/                          # 扁平化 Mapper
        └── Oa{Business}Mapper.java

src/test/java/.../service/
    └── 各模块 ServiceImplTest.java
```

---

## 二、已完成工作记录

### 2.1 后端修复

| # | 文件 | 改动 | 原因 |
|---|------|------|------|
| 1 | `DefaultController.java` | 删除 `bpm404()` 方法 (`@RequestMapping("/admin-api/bpm/**")`) | Spring Boot 3.5.9 auto-prefix 优先级问题，该兜底拦截了正常 BPM Controller 的请求 |
| 2 | `Oa{Leave/Trip/Loan/Visit/Request}Controller.java` | `@RequestMapping` 去掉 `/admin-api` 前缀 | 改为 `/oa/leave`，靠全局配置自动拼接 |

### 2.2 前端修复

| # | 文件/范围 | 改动 |
|---|----------|------|
| 1 | `locales/zh-CN/bpm.ts` | `oa` 节拆分为 `leave/trip/loan/visit/request` 独立子节，`oa.leave` 移除不属于 leave 的 `destination`/`title`/`content`/`urgency` 等键 |
| 2 | `locales/en/bpm.ts` | 同步英文 i18n 结构 |
| 3 | `locales/ar/bpm.ts` | 同步阿拉伯文 i18n 结构（新增 trip/loan/visit/request 节） |
| 4 | `dict.ts` | `OA_REQUEST_URGENCY = 'oa_urgency_type'` 指向已有字典类型 |
| 5 | `router.ts` (zh-CN/en) | 添加 `oa{Leave/Trip/Loan/Visit/Request}{Create/Detail}` 及 `bpmProcessInstanceDetail/Report` 翻译 |
| 6 | `remaining.ts` | `activeMenu` 路径 `/bpm/oa/*` → `/oa/*` |
| 7 | `api/bpm/leave/index.ts` | 旧 API 路径 `/bpm/oa/leave/*` → `/oa/leave/*` |
| 8 | 12 个 Vue 文件 | `t('oa.leave.*')` → `t('oa.{module}.*')`；硬编码中文 → i18n 调用；错误字典类型修正 |

### 2.3 数据库

| # | 文件 | 内容 |
|---|------|------|
| 1 | `sql/oa/001-oa-tables.sql` | 9 张 OA 表 DDL |
| 2 | `sql/oa/002-oa-dict-menu.sql` | 字典类型+数据、菜单+按钮权限 |
| 3 | `sql/oa/003-oa-menu-fixed.sql` | 菜单修复 |

---

## 三、BPMN 流程模型建立指南

### 3.1 操作入口

系统管理 → 流程管理 → 流程模型 → 新建

### 3.2 各模块参数速查表

| 模块 | 流程标识 KEY | 流程名称 | 表单提交路由 | 表单查看路径 | PROCESS_KEY(Java) | 流程变量 |
|------|------------|---------|-------------|-------------|------------------|---------|
| 请假 | `oa_leave` | 请假审批 | `oa/leave/create` | `oa/leave/detail` | `oa_leave` | `{ day: <天数> }` |
| 出差 | `oa_trip` | 出差审批 | `oa/trip/create` | `oa/trip/detail` | `oa_trip` | `{ day: <天数> }` |
| 借款 | `oa_loan` | 借款审批 | `oa/loan/create` | `oa/loan/detail` | `oa_loan` | `{}` |
| 拜访 | `oa_visit` | 拜访审批 | `oa/visit/create` | `oa/visit/detail` | `oa_visit` | `{}` |
| 请示 | `oa_request` | 请示审批 | `oa/request/create` | `oa/request/detail` | `oa_request` | 不传 |

### 3.3 字段对应表（模型器 → 表单配置）

#### 请假 Leave

| 字段名 | 字段类型 | 字典 | 说明 |
|--------|---------|------|------|
| type | 下拉选择 | `oa_leave_type` | 请假类型(年假/病假/事假/产假/婚假/陪产假/丧假) |
| reason | 多行文本 | — | 请假原因 |
| startTime | 日期时间 | — | 开始时间 |
| endTime | 日期时间 | — | 结束时间 |

#### 出差 Trip

| 字段名 | 字段类型 | 字典 | 说明 |
|--------|---------|------|------|
| type | 下拉选择 | `oa_trip_type` | 出差类型(商务/培训/会议) |
| destination | 文本 | — | 目的地 |
| reason | 多行文本 | — | 出差原因 |
| startTime | 日期时间 | — | 开始时间 |
| endTime | 日期时间 | — | 结束时间 |

#### 借款 Loan

| 字段名 | 字段类型 | 字典 | 说明 |
|--------|---------|------|------|
| purpose | 下拉选择 | `oa_loan_purpose` | 借款用途(差旅/采购/其他) |
| amount | 数字 | — | 借款金额 |
| expectedRepayTime | 日期 | — | 预计还款日期 |
| reason | 多行文本 | — | 借款原因 |

#### 拜访 Visit

| 字段名 | 字段类型 | 字典 | 说明 |
|--------|---------|------|------|
| customerId | 数字 | — | 客户ID |
| contactPerson | 文本 | — | 联系人 |
| contactPhone | 文本 | — | 联系电话 |
| visitTime | 日期时间 | — | 拜访时间 |
| location | 文本 | — | 拜访地点 |
| purpose | 文本 | — | 拜访目的 |
| notes | 多行文本 | — | 备注 |

#### 请示 Request

| 字段名 | 字段类型 | 字典 | 说明 |
|--------|---------|------|------|
| title | 文本 | — | 标题 |
| urgency | 下拉选择 | `oa_urgency_type` | 紧急程度(普通/紧急/特急) |
| content | 多行文本 | — | 内容 |

### 3.4 统一配置项（所有模块相同）

| 配置项 | 值 |
|--------|-----|
| 流程分类 | OA审批 |
| 表单类型 | 自定义表单(CUSTOM) |
| 审批人节点策略 | 发起人部门负责人 (dept_leader_from_initiator) |
| 流程设计 | [开始] → [发起人节点(自动)] → [审批人节点] → [结束] |

### 3.5 操作步骤（模型器内操作）

以"出差"为例，其他 4 个模块同理替换模块名。

#### Step 1：新建模型

系统管理 → 流程管理 → 流程模型 → 新建

弹出窗口填写：

| 字段 | 值 |
|------|-----|
| 流程标识 | `oa_trip` |
| 流程名称 | 出差审批 |
| 流程分类 | OA审批 |
| 描述 | （可选，可留空） |

→ 点击"确认"，进入模型器

#### Step 2：流程设计

左侧拖拽节点到画布：

1. 拖一个 **「开始」** 事件
2. 拖一个 **「用户任务」**（作为发起人节点，不需要配置审批人）
3. 拖一个 **「用户任务」**（作为审批人节点）
4. 拖一个 **「结束」** 事件
5. 依次连接：开始 → 发起人节点 → 审批人节点 → 结束

配置审批人节点：

- 点击审批人节点 → 右侧面板 → **审批人设置**
- 候选人策略 → **发起人部门负责人**
- 多人审批方式 → 或签（任一人审批即可）
- 节点名称 → 修改为 **"部门负责人审批"**

#### Step 3：表单配置

选择画布空白处 → 右侧面板 **表单配置**：

- 表单类型 → **自定义表单(CUSTOM)**
- 点击"添加表单字段"，按模块添加字段（参考下方 3.2 节字段表）：

| 字段名 | 类型 | 显示名称 |
|--------|------|---------|
| type | 下拉选择 | 出差类型 |
| destination | 单行文本 | 目的地 |
| reason | 多行文本 | 出差原因 |
| startTime | 日期时间 | 开始时间 |
| endTime | 日期时间 | 结束时间 |

> 下拉选择字段需要在"选项"中勾选对应的字典类型（如 `oa_trip_type`）

#### Step 4：更多设置

右侧面板 → 更多设置：

| 字段 | 值 |
|------|-----|
| 表单提交路由 | `oa/trip/create` |
| 表单查看路径 | `oa/trip/detail` |

#### Step 5：保存并发布

1. 点击右上角 **"保存"**
2. 点击 **"发布"** 按钮
3. 确认发布 → 输入版本描述 → 确认

发布成功后，该流程就会出现在"请假审批"的流程定义列表中，前端创建页面会通过 `processDefineKey = 'oa_trip'` 自动匹配。

### 3.6 各模块操作差异

| 模块 | 表单字段（按顺序添加） | 表单提交路由 | 表单查看路径 |
|------|---------------------|-------------|-------------|
| 请假 | type(下拉/oa_leave_type), startTime(日期时间), endTime(日期时间), reason(多行文本) | `oa/leave/create` | `oa/leave/detail` |
| 出差 | type(下拉/oa_trip_type), destination(文本), reason(多行文本), startTime(日期时间), endTime(日期时间) | `oa/trip/create` | `oa/trip/detail` |
| 借款 | purpose(下拉/oa_loan_purpose), amount(数字), expectedRepayTime(日期), reason(多行文本) | `oa/loan/create` | `oa/loan/detail` |
| 拜访 | customerId(数字), contactPerson(文本), contactPhone(文本), visitTime(日期时间), location(文本), purpose(文本), notes(多行文本) | `oa/visit/create` | `oa/visit/detail` |
| 请示 | title(文本), urgency(下拉/oa_urgency_type), content(多行文本) | `oa/request/create` | `oa/request/detail` |

### 3.7 注意事项

- **流程 KEY 必须**与 Java 代码中 `PROCESS_KEY` 常量完全一致（大小写敏感）
- **表单字段名必须**与前端 `formData.xxx` 中的属性名完全一致（用来实现表单数据自动匹配）
- **字典类型**在字段选项中选择"字典"并填入对应字典类型名（如 `oa_trip_type`）
- 请假之外 4 个模块创建前需确认该模块的后端字典数据已导入（运行过 `002-oa-dict-menu.sql`）
- 发布后如果前端创建时提示"流程模型未配置"，检查 `processDefineKey` 与发布的 KEY 是否一致

### 3.8 表单提交路由 vs 表单查看路径说明

```
表单提交路由 = Vue 路由地址，用于创建页面的路由导航
表单查看路径 = Vue 组件路径，用于动态加载业务详情组件（通过 registerComponent）
```

---

## 四、模块交互接口

### 4.1 BPM 模块 → OA（后端）

OA 域通过调用 BPM 模块的 API 发起审批流、接收审批回调。

#### 后端接口

| 方向 | 接口 | 调用方 | 说明 |
|------|------|-------|------|
| OA→BPM | `BpmProcessInstanceApi.createProcessInstance()` | 所有 5 个 BPM 类型 ServiceImpl.create() | 创建单据后发起审批流程 |
| BPM→OA | `BpmProcessInstanceStatusEventListener.onEvent()` | 5 个 StatusListener 类 | BPM 审批完成时回调更新单据状态 |

#### API 类路径

```
BpmProcessInstanceApi:
  com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi
  方法: createProcessInstance(Long userId, BpmProcessInstanceCreateReqDTO reqDTO)

BpmProcessInstanceStatusEventListener:
  com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEventListener
  方法: getProcessDefinitionKey() → 返回流程 KEY
        onEvent(BpmProcessInstanceStatusEvent event) → 回调处理
```

#### 流程交互时序

```
用户提交表单 → OA Service.create()
  → OaXxxDO 插入数据库（status=RUNNING）
  → BpmProcessInstanceApi.createProcessInstance() 发起 BPM 流程
  → 返回 processInstanceId → 更新到 OaXxxDO

审批人操作 → Flowable 引擎完成审批
  → BpmProcessInstanceStatusEvent 发布
  → OaXxxStatusListener.onEvent() 接收
  → OaXxxService.updateStatus(id, status) 更新单据状态
```

### 4.2 前端 → BPM API

| API 路径 | 前端调用方 | 说明 |
|----------|----------|------|
| `GET /admin-api/bpm/process-instance/get-approval-detail` | 各模块 create.vue | 获取审批节点/Timeline/候选审批人 |
| `GET /admin-api/bpm/process-instance/get-bpmn-model-view` | 审批详情页 | 获取 BPMN 模型视图 |
| `DELETE /admin-api/bpm/process-instance/cancel-by-start-user` | 各模块 index.vue | 撤销流程 |
| `GET /admin-api/bpm/definition/get` | 各模块 create.vue | 获取流程定义(含 startUserSelectTasks 等) |

### 4.3 OA 自身 API

所有 OA 业务 API 映射到 `/admin-api/oa/{business}/...`

| HTTP | 路径 | 说明 |
|------|------|------|
| POST | `/oa/{business}/create` | 创建 + 提交审批 |
| GET | `/oa/{business}/get` | 查询详情 |
| GET | `/oa/{business}/page` | 分页查询 |
| DELETE | `/oa/{business}/delete` | 删除 |

### 4.4 字典接口

| 字典类型 | 用途 | 数据来源 |
|---------|------|---------|
| `oa_leave_type` | 请假类型 | 后端 SQL `system_dict_data` |
| `oa_trip_type` | 出差类型 | 后端 SQL |
| `oa_loan_purpose` | 借款用途 | 后端 SQL |
| `oa_visit_type` | 拜访类型 | 后端 SQL |
| `oa_urgency_type` | 紧急程度(请示用) | 后端 SQL |
| `oa_report_type` | 报告类型 | 后端 SQL |
| `bpm_process_instance_status` | 审批状态 | BPM 模块内置 |

### 4.5 数据库交互

| 表 | 用途 |
|----|------|
| `oa_leave` / `oa_trip` / `oa_loan` / `oa_visit` / `oa_request` | 业务单据数据 |
| `oa_report` / `oa_schedule` / `oa_task` / `oa_document` | 非审批模块数据 |
| `system_dict_type` / `system_dict_data` | 字典数据 |
| `system_menu` | 菜单 + 按钮权限 |
| (Flowable 表: `act_*`) | BPM 引擎自动管理，OA 不直接操作 |

---

## 五、未审批模块说明（P1）

以下模块不需要 BPM 审批流，仅有基本的 CRUD：

| 模块 | 后端 | 前端 | 备注 |
|------|------|------|------|
| 工作报告 | ✅ 全量 | ✅ 列表+创建 | DO: OaReportDO |
| 日程 | ✅ 全量 | ✅ 日历 | DO: OaScheduleDO |
| 任务 | ✅ 全量 | ✅ 列表+创建 | DO: OaTaskDO |
| 文档 | ✅ 全量 | ✅ 文件列表 | DO: OaDocumentDO；文件存于 `infra_file` 表 |

---

## 六、前端文件清单

```
Web/src/
├── api/oa/
│   ├── leave/index.ts
│   ├── trip/index.ts
│   ├── loan/index.ts
│   ├── visit/index.ts
│   ├── request/index.ts
│   ├── report/index.ts
│   ├── schedule/index.ts
│   ├── task/index.ts
│   └── document/index.ts
├── views/oa/
│   ├── leave/   (index.vue - 列表, create.vue - 创建, detail.vue - 详情)
│   ├── trip/    (同上)
│   ├── loan/    (同上)
│   ├── visit/   (同上)
│   ├── request/ (同上)
│   ├── report/  (index.vue, create.vue)
│   ├── schedule/(index.vue - 日历)
│   ├── task/    (index.vue, create.vue)
│   └── document/(index.vue, create.vue)
├── router/modules/remaining.ts   ← OA 路由定义
├── locales/zh-CN/bpm.ts          ← 中文 i18n (oa.leave/trip/loan/visit/request/bpm)
├── locales/en/bpm.ts             ← 英文 i18n
├── locales/ar/bpm.ts             ← 阿拉伯文 i18n
├── locales/zh-CN/router.ts       ← 中文路由标题
├── locales/en/router.ts          ← 英文路由标题
└── utils/dict.ts                 ← 字典类型枚举
```

---

## 七、数据库 SQL 清单

```
Server/sql/oa/
├── 001-oa-tables.sql      → 9 张 OA 表建表语句
├── 002-oa-dict-menu.sql   → 字典类型 + 字典数据 + 菜单 + 按钮权限
└── 003-oa-menu-fixed.sql  → 菜单修复数据
```

---

## 八、后端文件清单

```
Server/mitedtsm-module-oa/
├── pom.xml
└── src/main/java/com/meession/etm/module/oa/
    ├── controller/admin/oa/      → 9 个 Controller
    ├── controller/admin/oa/vo/   → 27 个 VO (每模块 × 3)
    ├── service/                  → 18 个 Service (9 × 2: interface + impl)
    ├── service/listener/         → 5 个 StatusListener
    ├── dal/dataobject/           → 9 个 DO
    ├── dal/mapper/               → 9 个 Mapper
    ├── enums/ErrorCodeConstants.java
    └── package-info.java
```

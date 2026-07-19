# OA 模块数据模型设计文档

---

## 一、模块概述

OA（Office Automation）模块提供企业日常办公功能，包含请假、出差、借款、拜访、工作报告、日程、请示等7项业务功能，以及文档目录扩展管理。

**模块技术架构对齐现有项目标准：**
- 表前缀：`oa_`
- Java 包路径：`com.meession.etm.module.oa`
- REST 路径前缀：`/oa/`
- 权限前缀：`oa:{entity}:{action}`（如 `oa:leave:create`）
- 流程定义 Key：`oa_leave`、`oa_business_trip`、`oa_loan`、`oa_request`
- 审批状态对齐 Flowable 引擎 `BpmProcessInstanceStatusEnum`

---

## 二、模块间依赖关系

```
oa_leave.user_id          -->  system_users.id          (申请人)
oa_business_trip.user_id  -->  system_users.id          (申请人)
oa_loan.user_id           -->  system_users.id          (申请人)
oa_visit.user_id          -->  system_users.id          (拜访人)
oa_visit.crm_customer_id  -->  crm_customer.id           (关联CRM客户)
oa_work_report.user_id    -->  system_users.id          (报告人)
oa_work_report.reviewer_user_id --> system_users.id     (审阅人)
oa_schedule.user_id       -->  system_users.id          (所属人)
oa_request.user_id        -->  system_users.id          (申请人)
oa_document_dir.owner_user_id --> system_users.id       (所属人)

oa_leave.process_instance_id      -->  act_ru_execution.ID_   (Flowable流程实例)
oa_business_trip.process_instance_id --> act_ru_execution.ID_
oa_loan.process_instance_id       -->  act_ru_execution.ID_
oa_request.process_instance_id    -->  act_ru_execution.ID_

oa_request.attachment             -->  infra_file.id          (附件文件)
oa_document_dir.file_url          -->  infra_file.id          (文档附件)

所有表.tenant_id                   -->  system_tenant.id       (多租户)
所有表.creator/updater             -->  system_users.id        (操作人)
```

---

## 三、业务表分类

### 3.1 审批流程类（4张表）

对接 Flowable 工作流引擎，支持发起/审批/驳回/撤销完整流程。

| 表名 | 中文名 | 流程定义Key | 特有业务流程 |
|------|--------|-------------|-------------|
| `oa_leave` | 请假表 | `oa_leave` | 计算请假天数，流程结束后可销假 |
| `oa_business_trip` | 出差表 | `oa_business_trip` | 预估费用，流程结束后可报销关联 |
| `oa_loan` | 借款表 | `oa_loan` | 审批通过后进入还款管理（status=5已还款） |
| `oa_request` | 请示表 | `oa_request` | 通用审批，支持多种请示类型和附件 |

### 3.2 非审批流程类（4张表）

纯业务记录管理，不绑工作流。

| 表名 | 中文名 | 数据特点 |
|------|--------|----------|
| `oa_visit` | 拜访记录表 | 关联CRM客户，记录拜访计划与结果 |
| `oa_work_report` | 工作报告表 | 日报/周报/月报/年报，提交后审阅 |
| `oa_schedule` | 日程表 | 个人日程管理，支持提醒 |
| `oa_document_dir` | 文档目录扩展表 | 树形目录结构，文件版本管理，权限控制 |

---

## 四、完整字段清单

### 4.1 oa_leave（请假表）

| 字段 | 类型 | 长度 | 必填 | 默认值 | 说明 |
|------|------|------|------|--------|------|
| `id` | bigint | - | PK | auto | 请假表单主键 |
| `user_id` | bigint | - | 是 | - | 申请人的用户编号 |
| `type` | tinyint | - | 是 | - | 请假类型（1-事假，2-病假，3-年假，4-婚假，5-产假，6-丧假，7-调休，8-其他） |
| `reason` | varchar | 500 | 是 | - | 请假原因 |
| `start_time` | datetime | - | 是 | - | 开始时间 |
| `end_time` | datetime | - | 是 | - | 结束时间 |
| `day` | decimal(5,1) | - | 是 | - | 请假天数（支持0.5半天） |
| `status` | tinyint | - | 是 | -1 | 审批状态（-1-未开始，1-审批中，2-审批通过，3-审批不通过，4-已取消） |
| `process_instance_id` | varchar | 64 | 否 | NULL | 流程实例编号（关联 act_ru_execution.ID_） |
| `start_user_select_assignees` | varchar | 2000 | 否 | NULL | 发起人选定审批人Map（JSON格式） |
| `creator` | varchar | 64 | 否 | '' | 创建者 |
| `create_time` | datetime | - | 是 | NOW | 创建时间 |
| `updater` | varchar | 64 | 否 | '' | 更新者 |
| `update_time` | datetime | - | 是 | NOW | 更新时间 |
| `deleted` | bit(1) | - | 是 | 0 | 是否删除 |
| `tenant_id` | bigint | - | 是 | 0 | 租户编号 |

### 4.2 oa_business_trip（出差表）

| 字段 | 类型 | 长度 | 必填 | 默认值 | 说明 |
|------|------|------|------|--------|------|
| `id` | bigint | - | PK | auto | 出差表单主键 |
| `user_id` | bigint | - | 是 | - | 申请人的用户编号 |
| `destination` | varchar | 200 | 是 | - | 出差目的地 |
| `reason` | varchar | 500 | 是 | - | 出差事由 |
| `companion` | varchar | 200 | 否 | NULL | 同行人员 |
| `start_time` | datetime | - | 是 | - | 开始时间 |
| `end_time` | datetime | - | 是 | - | 结束时间 |
| `day` | decimal(5,1) | - | 是 | - | 出差天数 |
| `vehicle` | varchar | 100 | 否 | NULL | 交通工具 |
| `estimated_amount` | decimal(12,2) | - | 否 | NULL | 预估费用（元） |
| `status` | tinyint | - | 是 | -1 | 审批状态 |
| `process_instance_id` | varchar | 64 | 否 | NULL | 流程实例编号 |
| `start_user_select_assignees` | varchar | 2000 | 否 | NULL | 发起人选定审批人Map（JSON格式） |
| `creator` | varchar | 64 | 否 | '' | 创建者 |
| `create_time` | datetime | - | 是 | NOW | 创建时间 |
| `updater` | varchar | 64 | 否 | '' | 更新者 |
| `update_time` | datetime | - | 是 | NOW | 更新时间 |
| `deleted` | bit(1) | - | 是 | 0 | 是否删除 |
| `tenant_id` | bigint | - | 是 | 0 | 租户编号 |

### 4.3 oa_loan（借款表）

| 字段 | 类型 | 长度 | 必填 | 默认值 | 说明 |
|------|------|------|------|--------|------|
| `id` | bigint | - | PK | auto | 借款表单主键 |
| `user_id` | bigint | - | 是 | - | 申请人的用户编号 |
| `amount` | decimal(12,2) | - | 是 | - | 借款金额（元） |
| `purpose` | varchar | 500 | 是 | - | 借款事由 |
| `repayment_plan` | varchar | 500 | 否 | NULL | 还款计划 |
| `expected_repayment_time` | datetime | - | 否 | NULL | 预计还款时间 |
| `actual_repayment_time` | datetime | - | 否 | NULL | 实际还款时间 |
| `status` | tinyint | - | 是 | -1 | 审批状态（-1-未开始，1-审批中，2-审批通过，3-审批不通过，4-已取消，5-已还款） |
| `process_instance_id` | varchar | 64 | 否 | NULL | 流程实例编号 |
| `start_user_select_assignees` | varchar | 2000 | 否 | NULL | 发起人选定审批人Map（JSON格式） |
| `creator` | varchar | 64 | 否 | '' | 创建者 |
| `create_time` | datetime | - | 是 | NOW | 创建时间 |
| `updater` | varchar | 64 | 否 | '' | 更新者 |
| `update_time` | datetime | - | 是 | NOW | 更新时间 |
| `deleted` | bit(1) | - | 是 | 0 | 是否删除 |
| `tenant_id` | bigint | - | 是 | 0 | 租户编号 |

### 4.4 oa_visit（拜访记录表）

| 字段 | 类型 | 长度 | 必填 | 默认值 | 说明 |
|------|------|------|------|--------|------|
| `id` | bigint | - | PK | auto | 拜访表单主键 |
| `user_id` | bigint | - | 是 | - | 拜访人的用户编号 |
| `crm_customer_id` | bigint | - | 否 | NULL | 关联CRM客户编号（crm_customer.id） |
| `customer_name` | varchar | 100 | 是 | - | 客户名称 |
| `contact_person` | varchar | 50 | 否 | NULL | 联系人 |
| `contact_phone` | varchar | 20 | 否 | NULL | 联系电话 |
| `visit_address` | varchar | 200 | 否 | NULL | 拜访地址 |
| `visit_time` | datetime | - | 是 | - | 拜访时间 |
| `purpose` | varchar | 500 | 是 | - | 拜访目的 |
| `result` | text | - | 否 | NULL | 拜访结果 |
| `next_visit_time` | datetime | - | 否 | NULL | 下次拜访时间 |
| `status` | tinyint | - | 是 | 0 | 状态（0-待拜访，1-已完成，2-已取消） |
| `creator` | varchar | 64 | 否 | '' | 创建者 |
| `create_time` | datetime | - | 是 | NOW | 创建时间 |
| `updater` | varchar | 64 | 否 | '' | 更新者 |
| `update_time` | datetime | - | 是 | NOW | 更新时间 |
| `deleted` | bit(1) | - | 是 | 0 | 是否删除 |
| `tenant_id` | bigint | - | 是 | 0 | 租户编号 |

### 4.5 oa_work_report（工作报告表）

| 字段 | 类型 | 长度 | 必填 | 默认值 | 说明 |
|------|------|------|------|--------|------|
| `id` | bigint | - | PK | auto | 工作报告主键 |
| `user_id` | bigint | - | 是 | - | 报告人的用户编号 |
| `type` | tinyint | - | 是 | - | 报告类型（1-日报，2-周报，3-月报，4-年报） |
| `title` | varchar | 200 | 是 | - | 报告标题 |
| `content` | text | - | 是 | - | 报告内容 |
| `plan` | text | - | 否 | NULL | 明日/下周/下月计划 |
| `summary` | text | - | 否 | NULL | 工作总结 |
| `report_date` | date | - | 是 | - | 报告日期 |
| `status` | tinyint | - | 是 | 0 | 状态（0-草稿，1-已提交，2-已审阅） |
| `reviewer_user_id` | bigint | - | 否 | NULL | 审阅人用户编号 |
| `review_time` | datetime | - | 否 | NULL | 审阅时间 |
| `review_content` | varchar | 500 | 否 | NULL | 审阅意见 |
| `creator` | varchar | 64 | 否 | '' | 创建者 |
| `create_time` | datetime | - | 是 | NOW | 创建时间 |
| `updater` | varchar | 64 | 否 | '' | 更新者 |
| `update_time` | datetime | - | 是 | NOW | 更新时间 |
| `deleted` | bit(1) | - | 是 | 0 | 是否删除 |
| `tenant_id` | bigint | - | 是 | 0 | 租户编号 |

### 4.6 oa_schedule（日程表）

| 字段 | 类型 | 长度 | 必填 | 默认值 | 说明 |
|------|------|------|------|--------|------|
| `id` | bigint | - | PK | auto | 日程主键 |
| `user_id` | bigint | - | 是 | - | 所属人的用户编号 |
| `title` | varchar | 200 | 是 | - | 日程标题 |
| `description` | varchar | 500 | 否 | NULL | 日程描述 |
| `start_time` | datetime | - | 是 | - | 开始时间 |
| `end_time` | datetime | - | 是 | - | 结束时间 |
| `is_all_day` | bit(1) | - | 是 | 0 | 是否全天事件 |
| `location` | varchar | 200 | 否 | NULL | 地点 |
| `type` | tinyint | - | 是 | 1 | 日程类型（1-个人，2-会议，3-任务，4-纪念日，5-其他） |
| `priority` | tinyint | - | 是 | 0 | 优先级（0-普通，1-重要，2-紧急） |
| `status` | tinyint | - | 是 | 0 | 状态（0-待办，1-进行中，2-已完成，3-已取消） |
| `reminder_time` | int | - | 否 | NULL | 提前提醒分钟数 |
| `color` | varchar | 20 | 否 | NULL | 日程颜色标识 |
| `creator` | varchar | 64 | 否 | '' | 创建者 |
| `create_time` | datetime | - | 是 | NOW | 创建时间 |
| `updater` | varchar | 64 | 否 | '' | 更新者 |
| `update_time` | datetime | - | 是 | NOW | 更新时间 |
| `deleted` | bit(1) | - | 是 | 0 | 是否删除 |
| `tenant_id` | bigint | - | 是 | 0 | 租户编号 |

### 4.7 oa_request（请示表）

| 字段 | 类型 | 长度 | 必填 | 默认值 | 说明 |
|------|------|------|------|--------|------|
| `id` | bigint | - | PK | auto | 请示表单主键 |
| `user_id` | bigint | - | 是 | - | 申请人的用户编号 |
| `title` | varchar | 200 | 是 | - | 请示标题 |
| `content` | text | - | 是 | - | 请示内容 |
| `type` | tinyint | - | 是 | 1 | 请示类型（1-人事审批，2-财务审批，3-行政审批，4-采购审批，5-其他） |
| `urgency` | tinyint | - | 是 | 0 | 紧急程度（0-普通，1-紧急，2-特急） |
| `attachment` | varchar | 2000 | 否 | NULL | 附件地址（关联 infra_file 表） |
| `expected_amount` | decimal(12,2) | - | 否 | NULL | 涉及金额（元） |
| `status` | tinyint | - | 是 | -1 | 审批状态（-1-未开始，1-审批中，2-审批通过，3-审批不通过，4-已取消） |
| `process_instance_id` | varchar | 64 | 否 | NULL | 流程实例编号（关联 act_ru_execution.ID_） |
| `start_user_select_assignees` | varchar | 2000 | 否 | NULL | 发起人选定审批人Map（JSON格式） |
| `creator` | varchar | 64 | 否 | '' | 创建者 |
| `create_time` | datetime | - | 是 | NOW | 创建时间 |
| `updater` | varchar | 64 | 否 | '' | 更新者 |
| `update_time` | datetime | - | 是 | NOW | 更新时间 |
| `deleted` | bit(1) | - | 是 | 0 | 是否删除 |
| `tenant_id` | bigint | - | 是 | 0 | 租户编号 |

### 4.8 oa_document_dir（文档目录扩展表）

| 字段 | 类型 | 长度 | 必填 | 默认值 | 说明 |
|------|------|------|------|--------|------|
| `id` | bigint | - | PK | auto | 文档目录主键 |
| `parent_id` | bigint | - | 是 | 0 | 父目录ID（0表示根目录） |
| `name` | varchar | 100 | 是 | - | 目录名称/文档名称 |
| `type` | tinyint | - | 是 | 1 | 类型（1-目录，2-文档） |
| `sort` | int | - | 是 | 0 | 排序（值越小越靠前） |
| `description` | varchar | 500 | 否 | NULL | 目录描述/文档摘要 |
| `keywords` | varchar | 500 | 否 | NULL | 搜索关键字（多个用逗号分隔） |
| `owner_user_id` | bigint | 否 | NULL | 所属人用户编号（NULL表示公共目录） |
| `permission` | tinyint | - | 是 | 0 | 访问权限（0-公开，1-部门可见，2-仅自己可见） |
| `file_url` | varchar | 500 | 否 | NULL | 文档附件地址（关联 infra_file 表） |
| `file_name` | varchar | 200 | 否 | NULL | 原始文件名 |
| `file_type` | varchar | 50 | 否 | NULL | 文件类型（doc/pdf/xlsx等） |
| `file_size` | bigint | - | 否 | NULL | 文档大小（字节） |
| `version` | int | - | 否 | 1 | 文档版本号 |
| `status` | tinyint | - | 是 | 0 | 状态（0-正常，1-归档） |
| `creator` | varchar | 64 | 否 | '' | 创建者 |
| `create_time` | datetime | - | 是 | NOW | 创建时间 |
| `updater` | varchar | 64 | 否 | '' | 更新者 |
| `update_time` | datetime | - | 是 | NOW | 更新时间 |
| `deleted` | bit(1) | - | 是 | 0 | 是否删除 |
| `tenant_id` | bigint | - | 是 | 0 | 租户编号 |

---

## 五、编码表

### 5.1 审批状态（status）- 审批流程类表

| 值 | 含义 | 对应Flowable状态 | 说明 |
|----|------|------------------|------|
| `-1` | 未开始 | NOT_START | 新建记录，尚未发起流程 |
| `1` | 审批中 | RUNNING | 流程实例已创建，正在进行审批 |
| `2` | 审批通过 | APPROVE | 全部审批节点通过 |
| `3` | 审批不通过 | REJECT | 任一审批节点驳回 |
| `4` | 已取消 | CANCEL | 发起人撤销申请或管理员取消 |
| `5` | 已还款 | - | **仅 oa_loan**，审批通过后的业务状态 |

### 5.2 请假类型（oa_leave.type）

| 值 | 含义 |
|----|------|
| `1` | 事假 |
| `2` | 病假 |
| `3` | 年假 |
| `4` | 婚假 |
| `5` | 产假 |
| `6` | 丧假 |
| `7` | 调休 |
| `8` | 其他 |

### 5.3 工作报告类型（oa_work_report.type）

| 值 | 含义 | 报告日期规则 |
|----|------|-------------|
| `1` | 日报 | 当天 |
| `2` | 周报 | 当周周一 |
| `3` | 月报 | 当月1号 |
| `4` | 年报 | 当年1月1号 |

### 5.4 工作报告状态（oa_work_report.status）

| 值 | 含义 |
|----|------|
| `0` | 草稿 |
| `1` | 已提交 |
| `2` | 已审阅 |

### 5.5 拜访状态（oa_visit.status）

| 值 | 含义 |
|----|------|
| `0` | 待拜访 |
| `1` | 已完成 |
| `2` | 已取消 |

### 5.6 日程类型（oa_schedule.type）

| 值 | 含义 |
|----|------|
| `1` | 个人 |
| `2` | 会议 |
| `3` | 任务 |
| `4` | 纪念日 |
| `5` | 其他 |

### 5.7 日程优先级（oa_schedule.priority）

| 值 | 含义 |
|----|------|
| `0` | 普通 |
| `1` | 重要 |
| `2` | 紧急 |

### 5.8 日程状态（oa_schedule.status）

| 值 | 含义 |
|----|------|
| `0` | 待办 |
| `1` | 进行中 |
| `2` | 已完成 |
| `3` | 已取消 |

### 5.9 请示类型（oa_request.type）

| 值 | 含义 |
|----|------|
| `1` | 人事审批 |
| `2` | 财务审批 |
| `3` | 行政审批 |
| `4` | 采购审批 |
| `5` | 其他 |

### 5.10 紧急程度（oa_request.urgency）

| 值 | 含义 |
|----|------|
| `0` | 普通 |
| `1` | 紧急 |
| `2` | 特急 |

### 5.11 文档目录类型（oa_document_dir.type）

| 值 | 含义 |
|----|------|
| `1` | 目录（文件夹） |
| `2` | 文档（文件） |

### 5.12 文档访问权限（oa_document_dir.permission）

| 值 | 含义 | 可见范围 |
|----|------|----------|
| `0` | 公开 | 所有租户内用户可见 |
| `1` | 部门可见 | 所属人所在部门 + 上级部门可见 |
| `2` | 仅自己可见 | 仅 owner_user_id + 系统管理员可见 |

### 5.13 文档状态（oa_document_dir.status）

| 值 | 含义 |
|----|------|
| `0` | 正常 |
| `1` | 归档 |

---

## 六、审批流程设计要点

### 6.1 流程创建流程（参考 BpmOALeaveServiceImpl.createLeave）

```
1. 插入OA业务记录（status=-1 未开始）
2. 构建 processInstanceVariables（表单数据映射到Flowable流程变量）
3. 调用 processInstanceApi.createProcessInstance():
   - processDefinitionKey: "oa_leave" / "oa_business_trip" / "oa_loan" / "oa_request"
   - variables: { day: 3.5, ... }
   - businessKey: String.valueOf(业务记录ID)
   - startUserSelectAssignees: { taskKey: [userId1, userId2] }
4. 更新业务记录: setProcessInstanceId(返回的流程实例ID), setStatus(1 审批中)
```

### 6.2 流程变量映射

| 表 | 核心流程变量 | 说明 |
|----|-------------|------|
| `oa_leave` | `{day, type}` | 天数用于审批条件表达式 |
| `oa_business_trip` | `{day, estimated_amount, destination}` | 金额用于审批条件（如>5000需总监批） |
| `oa_loan` | `{amount, purpose}` | 金额用于审批条件 |
| `oa_request` | `{urgency, expected_amount, type}` | 紧急程度和类型决定审批层级 |

### 6.3 审批结果回调

通过 `BpmProcessInstanceStatusEventListener` 监听审批结果事件：
```
onEvent(BpmProcessInstanceStatusEvent event):
  - 根据 businessKey 找到对应OA业务记录
  - 调用 updateXxxStatus(id, event.getStatus())
  - event.getStatus(): 2=通过, 3=驳回, 4=取消
```

---

## 七、文档目录树设计

### 7.1 树结构特点

- `parent_id` = 0 表示根节点
- 支持无限层级嵌套（通过递归 parent_id 查询）
- `sort` 字段控制同级节点排序
- 目录（type=1）下可包含子目录和文档（type=2）
- 文档（type=2）下不可包含子节点

### 7.2 权限模型

```
访问决策逻辑：
  if (permission == 2 仅自己可见)
    → 当前用户 == owner_user_id 或 系统管理员
  if (permission == 1 部门可见)
    → 当前用户所在部门属于 owner_user_id 的部门及其子部门
  if (permission == 0 公开)
    → 同租户下所有用户可见
```

### 7.3 文档版本策略

- `version` 字段记录版本号，默认1
- 更新文档时 version+1，旧版本归档为 status=1
- `file_name` 保留原始文件名
- `file_type` 用于前端区分展示图标（doc/pdf/xlsx等）

---

## 八、索引设计

### 8.1 审批流程类表（4张）

| 表名 | 索引名 | 索引列 | 用途 |
|------|--------|--------|------|
| `oa_leave` | `idx_user_id` | user_id | 按申请人查询 |
| `oa_leave` | `idx_process_instance_id` | process_instance_id | 按流程实例查询 |
| `oa_leave` | `idx_status` | status | 按审批状态过滤 |
| `oa_business_trip` | 同上3个 | - | 同上 |
| `oa_loan` | `idx_user_id` / `idx_process_instance_id` / `idx_status` | - | 同上 |
| `oa_request` | `idx_user_id` / `idx_process_instance_id` / `idx_status` / `idx_urgency` | - | 紧急程度用于排序 |

### 8.2 非审批流程类表（4张）

| 表名 | 索引名 | 索引列 | 用途 |
|------|--------|--------|------|
| `oa_visit` | `idx_user_id` | user_id | 按拜访人查询 |
| `oa_visit` | `idx_crm_customer_id` | crm_customer_id | 按客户查询拜访记录 |
| `oa_visit` | `idx_visit_time` | visit_time | 按时间排序 |
| `oa_work_report` | `idx_user_id` | user_id | 按报告人查询 |
| `oa_work_report` | `idx_report_date` | report_date | 按日期过滤 |
| `oa_work_report` | `idx_type` | type | 按报告类型过滤 |
| `oa_schedule` | `idx_user_id` | user_id | 按所属人查询 |
| `oa_schedule` | `idx_start_time` | start_time | 按时间排序/日历视图 |
| `oa_document_dir` | `idx_parent_id` | parent_id | 树形结构查询 |
| `oa_document_dir` | `idx_owner_user_id` | owner_user_id | 按所属人查询 |
| `oa_document_dir` | `idx_type` | type | 区分目录/文档 |
| `oa_document_dir` | `idx_sort` | sort | 排序 |

---

## 九、数据库SQL文件位置

- SQL文件：`database/new/new-oa-module.sql`
- 包含9张表的完整 CREATE TABLE 语句
- 支持 InnoDB 引擎、utf8mb4 字符集
- 所有表含 `deleted` 软删除 + `tenant_id` 多租户隔离

---

## 十、后续开发清单

### 10.1 Java后端（mitedtsm-module-oa）

```
Server/mitedtsm-module-oa/
├── pom.xml
└── src/main/java/com/meession/etm/module/oa/
    ├── controller/admin/
    │   ├── leave/OaLeaveController.java         (请假CRUD)
    │   ├── businessTrip/OaBusinessTripController.java
    │   ├── loan/OaLoanController.java
    │   ├── visit/OaVisitController.java
    │   ├── workReport/OaWorkReportController.java
    │   ├── schedule/OaScheduleController.java
    │   ├── request/OaRequestController.java
    │   └── documentDir/OaDocumentDirController.java
    ├── dal/dataobject/
    │   ├── OaLeaveDO.java
    │   ├── OaBusinessTripDO.java
    │   ├── OaLoanDO.java
    │   ├── OaVisitDO.java
    │   ├── OaWorkReportDO.java
    │   ├── OaScheduleDO.java
    │   ├── OaRequestDO.java
    │   └── OaDocumentDirDO.java
    ├── dal/mysql/
    │   ├── OaLeaveMapper.java
    │   └── ... (8个Mapper)
    ├── service/
    │   ├── OaLeaveService.java / OaLeaveServiceImpl.java
    │   └── ... (8组Service)
    ├── convert/
    └── enums/
        ├── ErrorCodeConstants.java              (OA模块错误码)
        └── DictTypeConstants.java               (OA字典类型)
```

### 10.2 前端Vue页面（Web/mall）

```
Web/src/views/oa/
├── leave/index.vue              (请假管理)
├── businessTrip/index.vue       (出差管理)
├── loan/index.vue               (借款管理)
├── visit/index.vue              (拜访管理)
├── workReport/index.vue         (工作报告管理)
├── schedule/index.vue           (日程管理)
├── request/index.vue            (请示管理)
└── documentDir/index.vue        (文档目录管理)
```

### 10.3 菜单权限SQL

需在 `system_menu` 表中新增9组菜单条目（目录+菜单+按钮权限），并为每个菜单添加 `system_menu_i18n` 翻译记录。

权限标识格式：`oa:leave:query`、`oa:leave:create`、`oa:leave:update`、`oa:leave:delete` 等。
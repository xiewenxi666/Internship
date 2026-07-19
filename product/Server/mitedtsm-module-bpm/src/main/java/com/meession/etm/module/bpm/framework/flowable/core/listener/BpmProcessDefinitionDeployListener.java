package com.meession.etm.module.bpm.framework.flowable.core.listener;

import com.meession.etm.framework.tenant.core.context.TenantContextHolder;
import com.meession.etm.module.bpm.dal.dataobject.definition.BpmProcessDefinitionInfoDO;
import com.meession.etm.module.bpm.dal.mysql.definition.BpmProcessDefinitionInfoMapper;
import com.meession.etm.module.bpm.enums.definition.BpmAutoApproveTypeEnum;
import com.meession.etm.module.bpm.enums.definition.BpmModelFormTypeEnum;
import com.meession.etm.module.bpm.enums.definition.BpmModelTypeEnum;
import com.meession.etm.module.bpm.framework.flowable.core.enums.BpmTaskCandidateStrategyEnum;
import com.meession.etm.module.bpm.framework.flowable.core.util.BpmnModelUtils;
import com.meession.etm.module.bpm.framework.flowable.core.util.FlowableUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 内置流程定义的部署监听器
 *
 * 目的：启动时自动部署 CRM 回款审批流程，解决回款单提交审核时提示“流程定义不存在”的问题
 *
 * 注意：
 * 1. 项目开启了多租户（mitedtsm.tenant.enable = true），发起流程时会按照租户过滤流程定义，
 *    因此部署时必须指定租户编号，否则查询不到，报“流程定义不存在”
 * 2. 审批节点必须配置候选人策略（candidateStrategy），否则流程发起后无法计算审批人
 */
@Slf4j
@Component
public class BpmProcessDefinitionDeployListener implements ApplicationRunner {

    /**
     * CRM 回款审批流程标识
     *
     * 与 CrmReceivableServiceImpl.BPM_PROCESS_DEFINITION_KEY 保持一致
     */
    private static final String RECEIVABLE_PROCESS_KEY = "crm-receivable-audit";
    private static final String RECEIVABLE_PROCESS_NAME = "回款审批";
    /**
     * 回款详情页路由，用于“发起流程”场景查看/填写业务表单
     */
    private static final String RECEIVABLE_CREATE_PATH = "/crm/receivable/detail/index";
    /**
     * 回款“等待审批”页路由，用于工作流审批中心内嵌业务表单
     */
    private static final String RECEIVABLE_VIEW_PATH = "/crm/receivable/approval/detail/index";
    /**
     * CRM 报销审批流程标识
     */
    private static final String REIMBURSEMENT_PROCESS_KEY = "crm-reimbursement-audit";
    private static final String REIMBURSEMENT_PROCESS_NAME = "报销审批";
    private static final String REIMBURSEMENT_CREATE_PATH = "/crm/reimbursement/detail/index";
    private static final String REIMBURSEMENT_VIEW_PATH = "/crm/reimbursement/approval/detail/index";
    /**
     * CRM 退款审批流程标识
     */
    private static final String REFUND_PROCESS_KEY = "crm-refund-audit";
    private static final String REFUND_PROCESS_NAME = "退款审批";
    private static final String REFUND_CREATE_PATH = "/crm/refund/detail/index";
    private static final String REFUND_VIEW_PATH = "/crm/refund/approval/detail/index";
    /**
     * 审批人角色编号：1 - 超级管理员
     */
    private static final String APPROVE_ROLE_IDS = "1";
    /**
     * 默认租户编号
     */
    private static final Long DEFAULT_TENANT_ID = 1L;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private BpmProcessDefinitionInfoMapper processDefinitionInfoMapper;

    @Override
    public void run(ApplicationArguments args) {
        TenantContextHolder.setTenantId(DEFAULT_TENANT_ID);
        try {
            deployReceivableAuditProcess();
            deployReimbursementAuditProcess();
            deployRefundAuditProcess();
        } catch (Exception ex) {
            log.error("[run][部署内置流程({}) 失败]", RECEIVABLE_PROCESS_KEY, ex);
        } finally {
            TenantContextHolder.clear();
        }
    }

    private void deployReceivableAuditProcess() {
        String tenantId = FlowableUtils.getTenantId();
        // 1. 清理历史部署的「无租户」流程定义
        cleanStaleDeployments(RECEIVABLE_PROCESS_KEY);

        // 2. 当前租户下已存在，则跳过部署
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(RECEIVABLE_PROCESS_KEY)
                .processDefinitionTenantId(tenantId)
                .latestVersion().singleResult();
        if (definition == null) {
            Deployment deployment = repositoryService.createDeployment()
                    .key(RECEIVABLE_PROCESS_KEY).name(RECEIVABLE_PROCESS_NAME)
                    .addBpmnModel(RECEIVABLE_PROCESS_KEY + ".bpmn20.xml", buildReceivableAuditModel())
                    .tenantId(tenantId) // 关键：指定租户，否则流程发起时查询不到，报“流程定义不存在”
                    .disableSchemaValidation()
                    .deploy();
            definition = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deployment.getId()).singleResult();
            log.info("[deployReceivableAuditProcess][部署流程({}) 成功，流程定义编号({})]",
                    RECEIVABLE_PROCESS_KEY, definition.getId());
        }

        // 3. 补全流程定义拓展信息。发起流程时若查询不到该记录，同样会报“流程定义不存在”
        BpmProcessDefinitionInfoDO info = processDefinitionInfoMapper.selectByProcessDefinitionId(definition.getId());
        if (info == null) {
            processDefinitionInfoMapper.insert(new BpmProcessDefinitionInfoDO()
                    .setProcessDefinitionId(definition.getId())
                    .setModelId(definition.getId())
                    .setModelType(BpmModelTypeEnum.BPMN.getType())
                    .setCategory("DEFAULT")
                    .setFormType(BpmModelFormTypeEnum.CUSTOM.getType())
                    .setFormCustomCreatePath(RECEIVABLE_CREATE_PATH)
                    .setFormCustomViewPath(RECEIVABLE_VIEW_PATH)
                    .setVisible(true)
                    .setSort(0L)
                    .setAllowCancelRunningProcess(true)
                    .setAllowWithdrawTask(false)
                    .setAutoApprovalType(BpmAutoApproveTypeEnum.NONE.getType()));
            log.info("[deployReceivableAuditProcess][创建流程定义拓展信息：{} ({})]",
                    RECEIVABLE_PROCESS_KEY, definition.getId());
        } else if (!RECEIVABLE_VIEW_PATH.equals(info.getFormCustomViewPath())) {
            // 已存在但表单路径不是“等待审批”页时，纠正为内嵌“等待审批”页面
            processDefinitionInfoMapper.updateById(new BpmProcessDefinitionInfoDO()
                    .setId(info.getId())
                    .setFormCustomCreatePath(RECEIVABLE_CREATE_PATH)
                    .setFormCustomViewPath(RECEIVABLE_VIEW_PATH));
            log.info("[deployReceivableAuditProcess][更新流程定义({}) 的业务表单查看路径为：{}]",
                    definition.getId(), RECEIVABLE_VIEW_PATH);
        }
    }

    private void deployReimbursementAuditProcess() {
        String tenantId = FlowableUtils.getTenantId();
        cleanStaleDeployments(REIMBURSEMENT_PROCESS_KEY);

        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(REIMBURSEMENT_PROCESS_KEY)
                .processDefinitionTenantId(tenantId)
                .latestVersion().singleResult();
        if (definition == null) {
            Deployment deployment = repositoryService.createDeployment()
                    .key(REIMBURSEMENT_PROCESS_KEY).name(REIMBURSEMENT_PROCESS_NAME)
                    .addBpmnModel(REIMBURSEMENT_PROCESS_KEY + ".bpmn20.xml", buildReimbursementAuditModel())
                    .tenantId(tenantId)
                    .disableSchemaValidation()
                    .deploy();
            definition = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deployment.getId()).singleResult();
            log.info("[deployReimbursementAuditProcess][部署流程({}) 成功，流程定义编号({})]",
                    REIMBURSEMENT_PROCESS_KEY, definition.getId());
        }

        BpmProcessDefinitionInfoDO info = processDefinitionInfoMapper.selectByProcessDefinitionId(definition.getId());
        if (info == null) {
            processDefinitionInfoMapper.insert(new BpmProcessDefinitionInfoDO()
                    .setProcessDefinitionId(definition.getId())
                    .setModelId(definition.getId())
                    .setModelType(BpmModelTypeEnum.BPMN.getType())
                    .setCategory("DEFAULT")
                    .setFormType(BpmModelFormTypeEnum.CUSTOM.getType())
                    .setFormCustomCreatePath(REIMBURSEMENT_CREATE_PATH)
                    .setFormCustomViewPath(REIMBURSEMENT_VIEW_PATH)
                    .setVisible(true)
                    .setSort(0L)
                    .setAllowCancelRunningProcess(true)
                    .setAllowWithdrawTask(false)
                    .setAutoApprovalType(BpmAutoApproveTypeEnum.NONE.getType()));
            log.info("[deployReimbursementAuditProcess][创建流程定义拓展信息：{} ({})]",
                    REIMBURSEMENT_PROCESS_KEY, definition.getId());
        } else if (!REIMBURSEMENT_VIEW_PATH.equals(info.getFormCustomViewPath())) {
            processDefinitionInfoMapper.updateById(new BpmProcessDefinitionInfoDO()
                    .setId(info.getId())
                    .setFormCustomCreatePath(REIMBURSEMENT_CREATE_PATH)
                    .setFormCustomViewPath(REIMBURSEMENT_VIEW_PATH));
            log.info("[deployReimbursementAuditProcess][更新流程定义({}) 的业务表单查看路径为：{}]",
                    definition.getId(), REIMBURSEMENT_VIEW_PATH);
        }
    }

    private void deployRefundAuditProcess() {
        String tenantId = FlowableUtils.getTenantId();
        cleanStaleDeployments(REFUND_PROCESS_KEY);

        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(REFUND_PROCESS_KEY)
                .processDefinitionTenantId(tenantId)
                .latestVersion().singleResult();
        if (definition == null) {
            Deployment deployment = repositoryService.createDeployment()
                    .key(REFUND_PROCESS_KEY).name(REFUND_PROCESS_NAME)
                    .addBpmnModel(REFUND_PROCESS_KEY + ".bpmn20.xml", buildRefundAuditModel())
                    .tenantId(tenantId)
                    .disableSchemaValidation()
                    .deploy();
            definition = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deployment.getId()).singleResult();
            log.info("[deployRefundAuditProcess][部署流程({}) 成功，流程定义编号({})]",
                    REFUND_PROCESS_KEY, definition.getId());
        }

        BpmProcessDefinitionInfoDO info = processDefinitionInfoMapper.selectByProcessDefinitionId(definition.getId());
        if (info == null) {
            processDefinitionInfoMapper.insert(new BpmProcessDefinitionInfoDO()
                    .setProcessDefinitionId(definition.getId())
                    .setModelId(definition.getId())
                    .setModelType(BpmModelTypeEnum.BPMN.getType())
                    .setCategory("DEFAULT")
                    .setFormType(BpmModelFormTypeEnum.CUSTOM.getType())
                    .setFormCustomCreatePath(REFUND_CREATE_PATH)
                    .setFormCustomViewPath(REFUND_VIEW_PATH)
                    .setVisible(true)
                    .setSort(0L)
                    .setAllowCancelRunningProcess(true)
                    .setAllowWithdrawTask(false)
                    .setAutoApprovalType(BpmAutoApproveTypeEnum.NONE.getType()));
            log.info("[deployRefundAuditProcess][创建流程定义拓展信息：{} ({})]",
                    REFUND_PROCESS_KEY, definition.getId());
        } else if (!REFUND_VIEW_PATH.equals(info.getFormCustomViewPath())) {
            processDefinitionInfoMapper.updateById(new BpmProcessDefinitionInfoDO()
                    .setId(info.getId())
                    .setFormCustomCreatePath(REFUND_CREATE_PATH)
                    .setFormCustomViewPath(REFUND_VIEW_PATH));
            log.info("[deployRefundAuditProcess][更新流程定义({}) 的业务表单查看路径为：{}]",
                    definition.getId(), REFUND_VIEW_PATH);
        }
    }

    private BpmnModel buildRefundAuditModel() {
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        process.setId(REFUND_PROCESS_KEY);
        process.setName(REFUND_PROCESS_NAME);
        process.setExecutable(true);
        model.addProcess(process);

        StartEvent start = new StartEvent();
        start.setId("start");
        process.addFlowElement(start);

        UserTask approve = new UserTask();
        approve.setId("approve");
        approve.setName("审批退款");
        BpmnModelUtils.addCandidateElements(BpmTaskCandidateStrategyEnum.ROLE.getStrategy(),
                APPROVE_ROLE_IDS, approve);
        process.addFlowElement(approve);

        EndEvent end = new EndEvent();
        end.setId("end");
        process.addFlowElement(end);

        process.addFlowElement(new SequenceFlow("start", "approve"));
        process.addFlowElement(new SequenceFlow("approve", "end"));

        new BpmnAutoLayout(model).execute();
        return model;
    }

    private void cleanStaleDeployments() {
        cleanStaleDeployments(RECEIVABLE_PROCESS_KEY);
    }

    private void cleanStaleDeployments(String processKey) {
        // 删除无租户的流程定义对应的拓展信息
        List<ProcessDefinition> staleDefinitions = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .processDefinitionWithoutTenantId().list();
        staleDefinitions.forEach(definition -> processDefinitionInfoMapper.delete(
                new LambdaQueryWrapper<BpmProcessDefinitionInfoDO>()
                        .eq(BpmProcessDefinitionInfoDO::getProcessDefinitionId, definition.getId())));
        // 删除无租户的部署（级联删除流程定义）
        List<Deployment> staleDeployments = repositoryService.createDeploymentQuery()
                .processDefinitionKey(processKey)
                .deploymentWithoutTenantId().list();
        staleDeployments.forEach(deployment -> {
            repositoryService.deleteDeployment(deployment.getId(), true);
            log.info("[cleanStaleDeployments][清理无租户的历史部署({})]", deployment.getId());
        });
    }

    private BpmnModel buildReimbursementAuditModel() {
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        process.setId(REIMBURSEMENT_PROCESS_KEY);
        process.setName(REIMBURSEMENT_PROCESS_NAME);
        process.setExecutable(true);
        model.addProcess(process);

        StartEvent start = new StartEvent();
        start.setId("start");
        process.addFlowElement(start);

        UserTask approve = new UserTask();
        approve.setId("approve");
        approve.setName("审批报销");
        BpmnModelUtils.addCandidateElements(BpmTaskCandidateStrategyEnum.ROLE.getStrategy(),
                APPROVE_ROLE_IDS, approve);
        process.addFlowElement(approve);

        EndEvent end = new EndEvent();
        end.setId("end");
        process.addFlowElement(end);

        process.addFlowElement(new SequenceFlow("start", "approve"));
        process.addFlowElement(new SequenceFlow("approve", "end"));

        new BpmnAutoLayout(model).execute();
        return model;
    }

    private BpmnModel buildReceivableAuditModel() {
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        process.setId(RECEIVABLE_PROCESS_KEY);
        process.setName(RECEIVABLE_PROCESS_NAME);
        process.setExecutable(true);
        model.addProcess(process);

        StartEvent start = new StartEvent();
        start.setId("start");
        process.addFlowElement(start);

        UserTask approve = new UserTask();
        approve.setId("approve");
        approve.setName("审批回款");
        // 配置审批候选人策略：按角色（超级管理员），否则流程发起后无法计算审批人
        BpmnModelUtils.addCandidateElements(BpmTaskCandidateStrategyEnum.ROLE.getStrategy(),
                APPROVE_ROLE_IDS, approve);
        process.addFlowElement(approve);

        EndEvent end = new EndEvent();
        end.setId("end");
        process.addFlowElement(end);

        process.addFlowElement(new SequenceFlow("start", "approve"));
        process.addFlowElement(new SequenceFlow("approve", "end"));

        new BpmnAutoLayout(model).execute();
        return model;
    }

}

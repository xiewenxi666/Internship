// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.dal.dataobject.workorder;

import com.baomidou.mybatisplus.annotation.*;
import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.meession.etm.module.crm.enums.workorder.CrmWorkOrderPriorityEnum;
import com.meession.etm.module.crm.enums.workorder.CrmWorkOrderStatusEnum;
import com.meession.etm.module.crm.enums.workorder.CrmWorkOrderTypeEnum;
import lombok.*;

import java.time.LocalDateTime;

/**
 * CRM 工单 DO
 *
 * @author 密讯
 */
@TableName("crm_work_order")
@KeySequence("crm_work_order_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmWorkOrderDO extends BaseDO {

    @TableId(type = IdType.INPUT)
    private Long id;

    /** 工单编号 */
    @TableField("order_no")
    private String no;

    /** 工单标题 */
    private String title;

    /** 工单类型 */
    @TableField("order_type")
    private Integer type;

    /** 优先级 */
    private Integer priority;

    /** 状态 {@link CrmWorkOrderStatusEnum} */
    private Integer status;

    /** 合同编号 */
    private Long contractId;

    /** 客户编号 */
    private Long customerId;

    /** 负责人编号 */
    private Long ownerUserId;

    /** 描述 */
    private String description;

    /** 解决方案 */
    private String solution;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 结束时间 */
    private LocalDateTime endTime;

}

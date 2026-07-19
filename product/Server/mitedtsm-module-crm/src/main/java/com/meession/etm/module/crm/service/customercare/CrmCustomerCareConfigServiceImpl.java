package com.meession.etm.module.crm.service.customercare;

import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.customercare.vo.CrmCustomerCareConfigSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.bulksend.CrmBulkSendDO;
import com.meession.etm.module.crm.dal.dataobject.customercare.CrmCustomerCareConfigDO;
import com.meession.etm.module.crm.dal.mysql.bulksend.CrmBulkSendMapper;
import com.meession.etm.module.crm.dal.mysql.customercare.CrmCustomerCareConfigMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 客户关怀配置 Service 实现类
 *
 * @author 密讯
 */
@Slf4j
@Service
@Validated
public class CrmCustomerCareConfigServiceImpl implements CrmCustomerCareConfigService {

    @Resource
    private CrmCustomerCareConfigMapper customerCareConfigMapper;

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private CrmBulkSendMapper bulkSendMapper;

    @Override
    public CrmCustomerCareConfigDO getCustomerCareConfig() {
        return customerCareConfigMapper.selectOne();
    }

    @Override
    public void saveCustomerCareConfig(CrmCustomerCareConfigSaveReqVO saveReqVO) {
        CrmCustomerCareConfigDO dbConfig = getCustomerCareConfig();
        CrmCustomerCareConfigDO config = BeanUtils.toBean(saveReqVO, CrmCustomerCareConfigDO.class);
        if (Objects.nonNull(dbConfig)) {
            customerCareConfigMapper.updateById(config.setId(dbConfig.getId()));
            return;
        }
        customerCareConfigMapper.insert(config);
    }

    @Override
    public void sendTestEmail(CrmCustomerCareConfigSaveReqVO saveReqVO) {
        saveCustomerCareConfig(saveReqVO);
        String to = saveReqVO.getTestEmail();
        if (to == null || to.trim().isEmpty()) {
            throw new IllegalArgumentException("测试收件人邮箱不能为空");
        }
        String from = saveReqVO.getSenderEmail();
        boolean realSend = !"mitedtsm_09@email.com".equals(from);
        if (realSend) {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(from);
            msg.setTo(to.trim());
            msg.setSubject(saveReqVO.getEmailTitle());
            String body = saveReqVO.getEmailBody();
            msg.setText(body != null && !body.trim().isEmpty() ? body : "测试邮件");
            mailSender.send(msg);
            log.info("[sendTestEmail][真实邮件已发送至 {}, 发件人={}]", to, from);
        } else {
            log.info("[sendTestEmail][模拟发送至 {}, 发件人={}]", to, from);
        }
        CrmBulkSendDO record = new CrmBulkSendDO();
        record.setTitle("客户关怀-发送");
        record.setType(2);
        record.setContent(saveReqVO.getEmailBody());
        record.setTargetType(1);
        record.setTargetCount(1);
        record.setSuccessCount(1);
        record.setFailCount(0);
        record.setStatus(4);
        record.setOwnerUserId(1L);
        record.setSendTime(LocalDateTime.now());
        bulkSendMapper.insert(record);
    }

}

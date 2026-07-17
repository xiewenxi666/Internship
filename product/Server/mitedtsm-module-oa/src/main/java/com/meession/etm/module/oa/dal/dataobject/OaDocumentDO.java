package com.meession.etm.module.oa.dal.dataobject;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("oa_document")
@KeySequence("oa_document_seq")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaDocumentDO extends BaseDO {

    @TableId
    private Long id;
    private Long parentId;
    private String name;
    private Boolean isFolder;
    private Long fileId;
    private String fileUrl;
    private Long fileSize;
    private String description;

}

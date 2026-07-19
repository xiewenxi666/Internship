package com.meession.etm.module.oa.dal.dataobject;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("oa_document_dir")
@KeySequence("oa_document_dir_seq")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaDocumentDirDO extends BaseDO {

    @TableId
    private Long id;
    private Long parentId;
    private String name;
    private Integer type;
    private Integer sort;
    private String description;
    private String keywords;
    private Long ownerUserId;
    private Integer permission;
    private String fileUrl;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private Integer version;
    private Integer status;

}
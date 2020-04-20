package cn.rainss.smartNote.adapter.entity;

import java.util.Date;

/**
 * 分类数据实体
 */
public class Type {
    /**
     * 主键编号
     */
    private long id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 分类创建时间
     */
    private Date createTime;
    /**
     * 设置封了的图标（备用）
     */
    private Long typeIcon;

    public Long getTypeIcon() {
        return typeIcon;
    }

    public void setTypeIcon(Long typeIcon) {
        this.typeIcon = typeIcon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Type(long id, String name, Date createTime, Long typeIcon) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
        this.typeIcon = typeIcon;
    }

    public Type(long id, String name, Date createTime) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
    }

    public Type() {
    }
}

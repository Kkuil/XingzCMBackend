package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName article
 */
@TableName(value = "article")
@Data
public class Article implements Serializable {
    /**
     * 文章ID
     */
    @TableId
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 评论信息（例如：'{id: '1', comment: '欢迎', 'createdTime': '1970-01-01 00:00:00', 'modifiedTime': '1970-01-01 00:00:00'}'）
     */
    private String remark;

    /**
     * 状态ID
     */
    private Integer statusid;

    /**
     * 文章封面图
     */
    private String cover;

    /**
     * 点赞信息（例如：[1, 2, 3]记录了点赞了该文章的星友ID）
     */
    private Object liked;

    /**
     * 收藏信息（例如：[1, 2, 3]记录了收藏了该文章的星友ID）
     */
    private Object collected;

    /**
     * 转发信息（例如：[1, 2, 3]记录了转发了该文章的星友ID）
     */
    private Object shared;

    /**
     * 标签信息（例如：[1, 2, 3]记录了标签的ID）
     */
    private Object tags;

    /**
     * 是否逻辑删除(0：未删除 1：已删除)
     */
    @TableLogic
    private Object isdeleted;

    /**
     * 创建时间
     */
    private Date createdtime;

    /**
     * 最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）
     */
    private Date modifiedtime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Article other = (Article) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
                && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
                && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
                && (this.getStatusid() == null ? other.getStatusid() == null : this.getStatusid().equals(other.getStatusid()))
                && (this.getCover() == null ? other.getCover() == null : this.getCover().equals(other.getCover()))
                && (this.getLiked() == null ? other.getLiked() == null : this.getLiked().equals(other.getLiked()))
                && (this.getCollected() == null ? other.getCollected() == null : this.getCollected().equals(other.getCollected()))
                && (this.getShared() == null ? other.getShared() == null : this.getShared().equals(other.getShared()))
                && (this.getTags() == null ? other.getTags() == null : this.getTags().equals(other.getTags()))
                && (this.getIsdeleted() == null ? other.getIsdeleted() == null : this.getIsdeleted().equals(other.getIsdeleted()))
                && (this.getCreatedtime() == null ? other.getCreatedtime() == null : this.getCreatedtime().equals(other.getCreatedtime()))
                && (this.getModifiedtime() == null ? other.getModifiedtime() == null : this.getModifiedtime().equals(other.getModifiedtime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getStatusid() == null) ? 0 : getStatusid().hashCode());
        result = prime * result + ((getCover() == null) ? 0 : getCover().hashCode());
        result = prime * result + ((getLiked() == null) ? 0 : getLiked().hashCode());
        result = prime * result + ((getCollected() == null) ? 0 : getCollected().hashCode());
        result = prime * result + ((getShared() == null) ? 0 : getShared().hashCode());
        result = prime * result + ((getTags() == null) ? 0 : getTags().hashCode());
        result = prime * result + ((getIsdeleted() == null) ? 0 : getIsdeleted().hashCode());
        result = prime * result + ((getCreatedtime() == null) ? 0 : getCreatedtime().hashCode());
        result = prime * result + ((getModifiedtime() == null) ? 0 : getModifiedtime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", content=").append(content);
        sb.append(", remark=").append(remark);
        sb.append(", statusid=").append(statusid);
        sb.append(", cover=").append(cover);
        sb.append(", liked=").append(liked);
        sb.append(", collected=").append(collected);
        sb.append(", shared=").append(shared);
        sb.append(", tags=").append(tags);
        sb.append(", isdeleted=").append(isdeleted);
        sb.append(", createdtime=").append(createdtime);
        sb.append(", modifiedtime=").append(modifiedtime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
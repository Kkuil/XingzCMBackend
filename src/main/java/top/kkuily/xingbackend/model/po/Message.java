package top.kkuily.xingbackend.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 小K
 * @description 聊天实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private User from;
    private User to;
    private LocalDateTime sendTime;
    private String content;
}

package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.CommentReaction;
import top.kkuily.xingbackend.service.ICommentReactionService;
import top.kkuily.xingbackend.mapper.CommentReactionMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【comment_reaction】的数据库操作Service实现
* @createDate 2023-06-16 16:14:53
*/
@Service
public class CommentReactionServiceImpl extends ServiceImpl<CommentReactionMapper, CommentReaction>
    implements ICommentReactionService {

}





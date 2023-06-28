package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.dto.response.user_rank.UserRankInfoResDTO;
import top.kkuily.xingbackend.model.po.UserRank;
import top.kkuily.xingbackend.service.IUserRankService;
import top.kkuily.xingbackend.mapper.UserRankMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.utils.Result;

import java.util.List;

/**
 * @author 小K
 * @description 针对表【user_rank】的数据库操作Service实现
 * @createDate 2023-05-18 11:22:01
 */
@Service
public class UserRankServiceImpl extends ServiceImpl<UserRankMapper, UserRank>
        implements IUserRankService {

    @Resource
    private UserRankMapper userRankMapper;

    @Override
    public Result listUserRank(int current, int pageSize, int sort) {
        int offset = (current - 1) * pageSize;
        // 带分页查询的用户等级信息
        List<UserRankInfoResDTO> userRankInfoResDTOS = userRankMapper.selectPageAndUserInfoWithLimit(offset, pageSize, sort);
        // 不带分页查询的用户等级信息
        int total = userRankMapper.selectPageAndUserInfoWithNotLimit(sort);
        // 封装返回结果
        ListResDTO<UserRankInfoResDTO> userRankListRes = new ListResDTO<>();
        userRankListRes.setList(userRankInfoResDTOS);
        userRankListRes.setCurrent(current);
        userRankListRes.setPageSize(pageSize);
        userRankListRes.setTotal(total);

        return Result.success("获取成功", userRankListRes, MsgType.SILENT);
    }
}





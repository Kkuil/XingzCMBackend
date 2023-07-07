package top.kkuily.xingbackend.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import top.kkuily.xingbackend.model.dto.request.activity.UActivityListParamsDTO;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.dto.response.activity.ActivityInfoResWithUserDTO;
import top.kkuily.xingbackend.model.dto.response.article.user.ArticleInfoResWithUserDTO;
import top.kkuily.xingbackend.model.po.Activity;
import top.kkuily.xingbackend.service.IActivityService;
import top.kkuily.xingbackend.mapper.ActivityMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.Token;

import java.util.List;
import java.util.Objects;

import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_KEY_IN_HEADER;
import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_SECRET;

/**
 * @author 小K
 * @description 针对表【activity】的数据库操作Service实现
 * @createDate 2023-07-03 13:53:13
 */
@Service
@Slf4j(topic = "ActivityServiceImpl")
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity>
        implements IActivityService {

    @Resource
    private ActivityMapper activityMapper;

    /**
     * @param uActivityListParamsDTO UActivityListParamsDTO
     * @param request                HttpServletRequest
     * @return Result
     * @description 获取活动列表
     */
    @Override
    public Result userGetList(UActivityListParamsDTO uActivityListParamsDTO, HttpServletRequest request) {
        List<ActivityInfoResWithUserDTO> activityInfoResWithUserDTOS = null;
        int total = 0;
        try {
            activityInfoResWithUserDTOS = activityMapper.listActivitiesWithLimitAndUser(
                    uActivityListParamsDTO.getActivityId(),
                    uActivityListParamsDTO.getCurrent(),
                    uActivityListParamsDTO.getPageSize()
            );
            total = activityMapper.listActivitiesWithNotLimitAndUser(
                    uActivityListParamsDTO.getActivityId()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 封装数据
        ListResDTO<ActivityInfoResWithUserDTO> activityInfoResWithUserDTOListResDTO = new ListResDTO<>();
        if (activityInfoResWithUserDTOS != null) {
            activityInfoResWithUserDTOListResDTO.setCurrent(uActivityListParamsDTO.getCurrent());
            activityInfoResWithUserDTOListResDTO.setPageSize(uActivityListParamsDTO.getPageSize());
            activityInfoResWithUserDTOListResDTO.setList(activityInfoResWithUserDTOS);
            activityInfoResWithUserDTOListResDTO.setTotal(total);
        }

        return Result.success("获取成功", activityInfoResWithUserDTOListResDTO);
    }
}





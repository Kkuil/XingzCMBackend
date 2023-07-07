package top.kkuily.xingbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import top.kkuily.xingbackend.model.dto.request.activity.UActivityListParamsDTO;
import top.kkuily.xingbackend.model.po.Activity;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kkuily.xingbackend.utils.Result;

/**
* @author 小K
* @description 针对表【activity】的数据库操作Service
* @createDate 2023-07-03 13:53:13
*/
public interface IActivityService extends IService<Activity> {

    /**
     * @description 获取活动列表
     * @param uActivityListParamsDTO UActivityListParamsDTO
     * @param request HttpServletRequest
     * @return Result
     */
    Result userGetList(UActivityListParamsDTO uActivityListParamsDTO, HttpServletRequest request);
}

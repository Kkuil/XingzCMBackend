package top.kkuily.xingbackend.service;

import top.kkuily.xingbackend.model.po.Auth;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.auth.list.AuthListFilterVO;
import top.kkuily.xingbackend.model.vo.auth.list.AuthListParamsVO;
import top.kkuily.xingbackend.model.vo.auth.list.AuthListSortVO;
import top.kkuily.xingbackend.utils.Result;

/**
* @author 小K
* @description 针对表【auth】的数据库操作Service
* @createDate 2023-05-31 11:00:28
*/
public interface IAuthService extends IService<Auth> {

    Result getList(ListParamsVO<AuthListParamsVO, AuthListSortVO, AuthListFilterVO> listParams);
}

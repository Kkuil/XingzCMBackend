package top.kkuily.xingbackend.service;

import top.kkuily.xingbackend.model.po.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.dept.list.DeptListFilterVO;
import top.kkuily.xingbackend.model.vo.dept.list.DeptListParamsVO;
import top.kkuily.xingbackend.model.vo.dept.list.DeptListSortVO;
import top.kkuily.xingbackend.utils.Result;

/**
* @author 小K
* @description 针对表【department】的数据库操作Service
* @createDate 2023-05-21 13:03:51
*/
public interface IDepartmentService extends IService<Department> {

    Result getList(ListParamsVO<DeptListParamsVO, DeptListSortVO, DeptListFilterVO> listParams);
}

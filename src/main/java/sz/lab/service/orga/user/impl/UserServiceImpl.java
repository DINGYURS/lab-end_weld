package sz.lab.service.orga.user.impl;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sz.lab.dto.orga.UserDTO;
import sz.lab.dto.orga.UserOptionDTO;
import sz.lab.dto.orga.UserOutputDTO;
import sz.lab.dto.system.OperateResultDTO;
import sz.lab.dto.system.TablePagingDTO;
import sz.lab.dto.system.TableRequestDTO;
import sz.lab.dto.system.role.RoleOptionDTO;
import sz.lab.entity.orga.dept.DeptEntity;
import sz.lab.entity.orga.user.UserEntity;
import sz.lab.entity.orga.user.UserRoleEntity;
import sz.lab.entity.system.RoleEntity;
import sz.lab.mapper.login.LoginMapper;
import sz.lab.mapper.orga.dept.DeptMapper;
import sz.lab.mapper.orga.user.UserMapper;
import sz.lab.mapper.orga.user.UserRoleMapper;
import sz.lab.mapper.system.role.RoleMapper;
import sz.lab.service.orga.user.UserService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表，用于记录用户账号信息。 服务实现类
 * </p>
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Resource
    private LoginMapper loginMapper;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private UserMapper userMapper;
    @Value("${custom-attribute.dept-id}")
    private Integer deptId;

    @Override
    public OperateResultDTO pageList(TableRequestDTO tableRequestDTO) {
        //获取该项目的部门列表
        List<Integer> deptList = getDeptList(deptId);
        deptList.add(deptId);
        //查询参数获取
        JSONObject param = tableRequestDTO.getJsonParam();
        String userName= param.getString("userName");
        String userPhone = param.getString("userPhone");
        List<Integer> deptIds = param.getList("deptIds",Integer.class);
        // 启动自动分页
        PageHelper.startPage(tableRequestDTO.getPageNo(), tableRequestDTO.getPageSize());
        //查询用户列表
        List<UserEntity> userEntityList = userMapper.selectList(Wrappers.lambdaQuery(UserEntity.class)
                .like(StrUtil.isNotBlank(userName), UserEntity::getUserName, userName)
                .like(StrUtil.isNotBlank(userPhone),UserEntity::getUserPhone, userPhone)
                .in(UserEntity::getDeptId, deptList)
                .in(deptIds!=null&& !deptIds.isEmpty(),UserEntity::getDeptId, deptIds));

        // 其他表的名字
        Map<Integer, DeptEntity> deptEntityMap = queryDept(userEntityList.stream().map(UserEntity::getDeptId).collect(Collectors.toList()));
        Map<Integer, List<Integer>> userRoleMap = queryRoleId(userEntityList.stream().map(UserEntity::getUserId).collect(Collectors.toList()));

        //返回到前端的数组
        List<UserOutputDTO> list = entitiesToDTOs(userEntityList, deptEntityMap, userRoleMap);
        //将数组封装到通用分页dto类
        TablePagingDTO pagingDTO = new TablePagingDTO(tableRequestDTO.getPageNo(), tableRequestDTO.getPageSize(), tableRequestDTO.getCurrentPage(),
                new PageInfo<>(userEntityList).getTotal(), list);
        //返回通用操作返回结果类
        return new OperateResultDTO(true,"成功",pagingDTO);
    }

    @Override
    public OperateResultDTO roleOptionList() {
        List<RoleEntity> list = roleMapper.selectList(Wrappers.lambdaQuery(RoleEntity.class));

        List<RoleOptionDTO> ret = getRoleOptions(list);
        return new OperateResultDTO(true,"成功",ret);
    }

    @Override
    public OperateResultDTO userOptionList() {
        List<UserEntity> list = userMapper.selectList(Wrappers.lambdaQuery(UserEntity.class)
                .select(UserEntity::getUserId,UserEntity::getUserName));
        List<UserOptionDTO> ret = getUserOptions(list);
        return new OperateResultDTO(true,"成功",ret);
    }

    @Override
    public OperateResultDTO add(UserDTO userDTO) {
        UserEntity userEntity = dtoToEntity(userDTO);
        save(userEntity);
        Integer userId = userEntity.getUserId();
        for(Integer roleId:userDTO.getRoleIdList()){
            UserRoleEntity entity = new UserRoleEntity();
            entity.setUserId(userId);
            entity.setRoleId(roleId);
            userRoleMapper.insert(entity);
        }
        UserEntity user = baseMapper.selectOne(Wrappers.lambdaQuery(UserEntity.class)
                .eq(UserEntity::getUserId,userEntity.getUserId()));
        return new OperateResultDTO(true,"成功",user.getUserId());
    }

    @Override
    public OperateResultDTO update(UserDTO userDTO) {
        UserEntity userEntity = dtoToEntity(userDTO);
        updateById(userEntity);
        return new OperateResultDTO(true,"成功",userEntity.getUserId());
    }

    private List<UserOutputDTO> entitiesToDTOs(List<UserEntity> list,
                                               Map<Integer, DeptEntity> deptEntityMap,
                                               Map<Integer, List<Integer>> userRoleMap) {
        List<UserOutputDTO> ret = new ArrayList<>(list.size());
        for (UserEntity userEntity : list) {
            UserOutputDTO dto = new UserOutputDTO();
            dto.setUserId(userEntity.getUserId());
            dto.setUserInfo(userEntity.getUserInfo());
            dto.setUserName(userEntity.getUserName());
            dto.setIsDeleted(userEntity.getIsDeleted());
            dto.setDeptId(userEntity.getDeptId());
            if(deptEntityMap.containsKey(userEntity.getDeptId())){
                dto.setDeptName(deptEntityMap.get(userEntity.getDeptId()).getDeptName());
            }
            dto.setUserPhone(userEntity.getUserPhone());
            if(userRoleMap.containsKey(userEntity.getUserId())){
                dto.setRoleIdList(userRoleMap.get(userEntity.getUserId()));
            }
            ret.add(dto);
        }
        return ret;
    }
    private List<RoleOptionDTO> getRoleOptions(List<RoleEntity> list) {
        List<RoleOptionDTO> ret = new ArrayList<>(list.size());
        for (RoleEntity roleEntity : list) {
            RoleOptionDTO dto = new RoleOptionDTO();
            dto.setRoleName(roleEntity.getRoleName());
            dto.setRoleId(roleEntity.getRoleId());
            ret.add(dto);
        }
        return ret;
    }
    private List<UserOptionDTO> getUserOptions(List<UserEntity> list) {
        List<UserOptionDTO> ret = new ArrayList<>(list.size());
        for (UserEntity userEntity : list) {
            UserOptionDTO dto = new UserOptionDTO();
            dto.setUserId(userEntity.getUserId());
            dto.setUserName(userEntity.getUserName());
            ret.add(dto);
        }
        return ret;
    }
    private Map<Integer, DeptEntity> queryDept(Collection<Integer> deptIds) {
        if (deptIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return deptMapper.selectList(Wrappers.lambdaQuery(DeptEntity.class)
                        .in(DeptEntity::getDeptId, deptIds))
                .stream()
                .collect(Collectors.toMap(DeptEntity::getDeptId, dept -> dept));
    }
    private Map<Integer, List<Integer>> queryRoleId(Collection<Integer> userIds) {
        HashMap<Integer, List<Integer>>  mapList = new HashMap<>();
        List<Integer> userList = (List<Integer>) userIds;
        for(Integer userId:userList){
            mapList.put(userId,getRoleCode(userId));
        }
        return mapList;
    }
    //获取用户的角色权限id
    private List<Integer> getRoleCode(Integer userId){
        return userRoleMapper.selectList(Wrappers.lambdaQuery(UserRoleEntity.class)
                        .eq(UserRoleEntity::getUserId,userId))
                .stream()
                .map(UserRoleEntity::getRoleId)
                .collect(Collectors.toList());
    }
    private List<Integer> getDeptList(Integer deptId){
        List<Integer> list = deptMapper.selectList(Wrappers.lambdaQuery(DeptEntity.class)
                        .eq(DeptEntity::getDeptFather,deptId))
                .stream()
                .map(DeptEntity::getDeptId)
                .collect(Collectors.toList());
        return list;
    }
    private UserEntity dtoToEntity(UserDTO dto) {
        UserEntity ret = new UserEntity();
        BeanUtils.copyProperties(dto,ret);
//        ret.setUserId(dto.getUserId());
//        ret.setLoginCode(dto.getLoginCode());
//        ret.setUserName(dto.getUserName());
//        ret.setUserPhone(dto.getUserPhone());
//        ret.setUserInfo(dto.getUserInfo());
//        ret.setDeptId(dto.getDeptId());
        return ret;
    }
}
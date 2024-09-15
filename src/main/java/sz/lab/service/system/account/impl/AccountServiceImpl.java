package sz.lab.service.system.account.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import sz.lab.dto.orga.UserOptionDTO;
import sz.lab.dto.system.OperateResultDTO;
import sz.lab.dto.system.TablePagingDTO;
import sz.lab.dto.system.TableRequestDTO;
import sz.lab.dto.system.account.AccountDTO;
import sz.lab.dto.system.role.RoleOptionDTO;
import sz.lab.entity.orga.user.UserEntity;
import sz.lab.entity.orga.user.UserRoleEntity;
import sz.lab.entity.system.RoleEntity;
import sz.lab.entity.system.account.AccountEntity;
import sz.lab.mapper.orga.user.UserMapper;
import sz.lab.mapper.orga.user.UserRoleMapper;
import sz.lab.mapper.system.account.AccountMapper;
import sz.lab.mapper.system.role.RoleMapper;
import sz.lab.service.system.account.AccountService;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountEntity> implements AccountService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Override
    public OperateResultDTO pageList(TableRequestDTO tableRequestDTO) {
        //查询参数获取
        JSONObject param = tableRequestDTO.getJsonParam();
        String accountName= param.getString("accountName");
        String loginCode = param.getString("loginCode");
        // 启动自动分页
        PageHelper.startPage(tableRequestDTO.getPageNo(), tableRequestDTO.getPageSize());
        //查询用户列表
        List<AccountEntity> accountEntityList = baseMapper.selectList(Wrappers.lambdaQuery(AccountEntity.class)
                .like(StrUtil.isNotBlank(accountName), AccountEntity::getAccountName, accountName)
                .like(StrUtil.isNotBlank(loginCode),AccountEntity::getLoginCode, loginCode));

        // 其他表的名字
        Map<Integer, List<Integer>> accountRoleMap = queryRoleId(accountEntityList.stream().map(AccountEntity::getAccountId).collect(Collectors.toList()));

        //返回到前端的数组
        List<AccountDTO> list = entitiesToDTOs(accountEntityList, accountRoleMap);
        //将数组封装到通用分页dto类
        TablePagingDTO pagingDTO = new TablePagingDTO(tableRequestDTO.getPageNo(), tableRequestDTO.getPageSize(), tableRequestDTO.getCurrentPage(),
                new PageInfo<>(accountEntityList).getTotal(), list);
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
        List<UserOptionDTO> ret = getUserOptions();
        return new OperateResultDTO(true,"成功",ret);
    }

    @Override
    public OperateResultDTO add(AccountDTO accountDTO) {
        AccountEntity accountEntity = dtoToEntity(accountDTO);
        save(accountEntity);
        AccountEntity account = baseMapper.selectOne(Wrappers.lambdaQuery(AccountEntity.class)
                .eq(AccountEntity::getLoginCode,accountEntity.getLoginCode()));
        //返回userId是执行下面的updateSignatureImage方法
        return new OperateResultDTO(true,"成功",account.getAccountId());
    }

    @Override
    public OperateResultDTO update(AccountDTO accountDTO) {
        AccountEntity accountEntity = dtoToEntity(accountDTO);
        updateById(accountEntity);
        return new OperateResultDTO(true,"成功",accountDTO.getAccountId());
    }

    @Override
    public OperateResultDTO codeIsExist(AccountDTO accountDTO) {
        Boolean isAdd = (accountDTO.getAccountId() == null);
        List<AccountEntity> list;
        if(isAdd){
            //新增，查询登录code是否存在
            list = baseMapper.selectList(Wrappers.lambdaQuery(AccountEntity.class)
                    .eq(AccountEntity::getLoginCode,accountDTO.getLoginCode()));
        }else {
            //编辑，查询这个accountId以外的登录code是否存在
            list = baseMapper.selectList(Wrappers.lambdaQuery(AccountEntity.class)
                    .eq(AccountEntity::getLoginCode, accountDTO.getLoginCode())
                    .ne(AccountEntity::getAccountId,accountDTO.getAccountId()));
        }
        //true则存在，false则不存在
        if(list.isEmpty()){
            return new OperateResultDTO(true,"成功",false);
        }else{
            return new OperateResultDTO(true,"成功",true);
        }
    }
    private List<AccountDTO> entitiesToDTOs(List<AccountEntity> list,
                                            Map<Integer, List<Integer>> accountRoleMap) {
        List<AccountDTO> ret = new ArrayList<>(list.size());
        for (AccountEntity entity : list) {
            AccountDTO dto = new AccountDTO();
            dto.setAccountId(entity.getAccountId());
            dto.setLoginCode(entity.getLoginCode());
            dto.setUserId(entity.getUserId());
//            dto.setAccountInfo(entity.getAccountInfo());
            dto.setAccountName(entity.getAccountName());
            if(accountRoleMap.containsKey(entity.getAccountId())){
                dto.setRoleIdList(accountRoleMap.get(entity.getAccountId()));
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
    private List<UserOptionDTO> getUserOptions() {
        List<UserEntity> list = userMapper.selectList(Wrappers.lambdaQuery(UserEntity.class));
        List<UserOptionDTO> ret =new ArrayList<>();
        for (UserEntity userEntity : list) {
            UserOptionDTO dto = new UserOptionDTO();
            dto.setUserId(userEntity.getUserId());
            dto.setUserName(userEntity.getUserName());
            ret.add(dto);
        }
        return ret;
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
    private AccountEntity dtoToEntity(AccountDTO dto) {
        AccountEntity ret = new AccountEntity();
        BeanUtils.copyProperties(dto,ret);
        //新增用户或重置密码,设置密码.修改则跳过
        if(dto.getAccountId() == null || dto.getLoginPwd() != null){
            ret.setLoginPwd(dto.getLoginPwd());
            ret.setPwdLastUpdate(LocalDateTime.now());
        }
        return ret;
    }
}
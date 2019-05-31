


/**
 * @Description:
 * @Author : yt
 * @Date : 2019/05/23 13:30
 */
public class UserInfoApi {


    /**
     * dubbo接口入参， 用户类型定义成枚举
     * 当此服务作为provider提供出去之后，如果枚举类型发生变化会序列化失败
     * 应该把UserTypeEnum类型改成Integer类型
     * @param userId
     * @param userTypeEnum
     * @return
     */
    RpcResult<UserInfo> getUserInfoById(Integer userId, UserTypeEnum userTypeEnum);

}
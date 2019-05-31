


/**
 * @Description: for循环里面调用rpc接口查询信息导致接口请求超时
 * @Author : yt
 * @Date : 2019/05/23 13:30
 */
public class InvokeInterfceInforLoop {

    @Resource
    private UserRemoteService userRemoteService;

    /**
     * 查询用户详细信息
     *
     * @param userInfoList
     * @return
     */
    public List<UserDetailDTO> queryList(List<UserInfo> userInfoList) {

        List<UserDetailDTO> result = new ArrayList<>(userInfoList.size());

        //根据用户id查询用户信息
        for (UserInfo userInfo : userInfoList) {
            UserExtInfo = userRemoteService.findByUserId(userInfo.getUserId());
            //设置扩展信息....
            assemble(userInfo, userExtInfo);
        }



        //建议批量查询，减少网络io请求次数
        Set<Long> userIds = userInfoList.stream().map(UserInfo::getUserId ()).collect(Collectors.toSet());
        List<UserExtInfo> =userRemoteService.findByUserIds(userIds);
    }


    private UserDetailDTO assemble(UserInfo userInfo, UserExtInfo userExtInfo) {
        //
    }


}
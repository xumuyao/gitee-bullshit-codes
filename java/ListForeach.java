/**
 * 查询list1然后遍历list1赋值
 * 在外面又建了一个全新的list2，然后把遍历赋值过的list1的每一个元素add到list2中
 * 然后返回list2.。。。。。。 为什么不直接把list1遍历后返回，，这是什么骚操作 脱裤子放屁吗-_-
 * @author chenjunhan
 * @date 2019-06-21 10:50:02
 */
public class ListForeach {
    @Autowired
    private CommentsService commentsService;

    /**
     * 获取评论详情
     * @param users
     * @param uuid 评论uuid
     * @return
     */
    @GetMapping("/commentInfo/{uuid}")
    public R commentInfo(@LoginUser Users users, @PathVariable("uuid") String uuid){
        //根据uuid查询一级评论
        CommentInfoDTO commentInfoDTO = commentsService.getCommentInfoByKey(uuid);
        //根据 commentInfoUUid 查询二级评论列表
        List<CommentsLowerDTO> list1 = commentsService.getCommentsListByReplyTo(commentInfoDTO.getUuid());
        /**
         * 这list是干啥了 一脸懵逼
         */
        List<CommentsLowerDTO> list2 = Lists.newArrayList();
        list1.forEach(commentsLowerDTO -> {
            //二级评论点赞存放地址
            String secondLevelPraiseKey = "Comments:"+ uuid + ;
            if (redisUtils.hExists(secondLevelPraiseKey, commentsLowerDTO.getUuid())) {
                commentsLowerDTO.setDisplayPraises(redisUtils.hGet(secondLevelPraiseKey, commentsLowerDTO.getUuid()).toString());
            }
            if (users != null) {
                //判断该用户是否点赞
                String praiseSetKey = CommonConstants.USER_PREFIX + CommonConstants.REDIS_NAMESPACE + users.getId() + CommonConstants.REDIS_NAMESPACE + commentsLowerDTO.getFirstLevelCommentId() + CommonConstants.REDIS_NAMESPACE + CommonConstants.COMMENTS_SUFFIX + CommonConstants.REDIS_NAMESPACE  + CommonConstants.PRAISE_SUFFIX   + CommonConstants.SHORTVIDEO_SUFFIX + CommonConstants.REDIS_NAMESPACE ;
                if (redisUtils.sexist(praiseSetKey, commentsLowerDTO.getUuid())) {
                    commentsLowerDTO.setHasPraises(true);
                }
            }
            commentsLowerDTO.setDisplayTime(JdttCommonUtils.friendlyFormatByLocalDateTime(commentsLowerDTO.getCreatedAt()));
            /**
             * 遍历完commentsLowerDTOList 都set过了 然后塞到新list里
             */
            list2.add(commentsLowerDTO);
        });
        /**
         * 然后把新list2进行setCommentsLowerDTOList，为什么不直接把遍历过的list1 set进去，要再建一个。。。。。。
         */
        commentInfoDTO.setCommentsLowerDTOList(list2);
        return new R<>().setMessage("获取评论详情成功").setData(commentInfoDTO);
    }


}
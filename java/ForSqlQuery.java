/**
 * 关联查询，不明白的是，为啥是在for循环里通过外键一条一条查询
 */
public class ForSqlQuery {

    /**
     * 分页
     */
//    @Cacheable(keyGenerator = "keyGenerator")
    public Object queryAll(OdsClueDTO odsClue, Pageable pageable){
        Page<OdsClue> page = odsClueRepository.findAll(new Spec(odsClue),pageable);
        List<OdsClue> list = page.getContent();
        for (OdsClue odsClue2 : list) {
        	if(odsClue2.getPartnerId()!=null) {
        		Optional<OdsWechatUser> wechatUserOptional = odsWechatUserRepository.findById(odsClue2.getPartnerId());
            	if(wechatUserOptional.isPresent()) {
                	OdsWechatUser odsWechatUser=wechatUserOptional.get();
                	odsClue2.setPartnerWechatName(odsWechatUser.getWechatName());
                	odsClue2.setPartnerRealName(odsWechatUser.getRealName());
                	odsClue2.setPartnerPhone(odsWechatUser.getPhone());
            	}
        	}
        	
        }
        Map map = Maps.newHashMap();
        map.put("content", list);
        map.put("totalElements",page.getTotalElements());
        return map;
    }
}

/**
 * 线上店铺表
 *
 * @author rexlin600
 * @email 3072054267@qq.com
 * @date 2018-11-06 19:01:04
 */
@SuppressWarnings("ALL")
@Service
@Slf4j
public class OnlineShopBiz {

    @Autowired
    private OnlineShopMapper onlineShopMapper;
    @Autowired
    private ShopRelationCategoryMapper shopRelationCategoryMapper;
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private OfflineShopMapper offlineShopMapper;
    @Autowired
    private MerchantAuditMapper merchantAuditMapper;
    @Autowired
    private MerchantAuditHostoryMapper merchantAuditHostoryMapper;

    @Autowired
    private CollectionBankMapper collectionBankMapper;

    @Autowired
    private CollectionTreasureMapper collectionTreasureMapper;

    @Autowired
    private GetLatAndLon getLatAndLon;

    @Autowired
    private CollectionWxpayMapper collectionWxpayMapper;

    @Autowired
    private RecommendShopMapper recommendShopMapper;

    @Autowired
    private MerchantUserMapper merchantUserMapper;

    public static Integer onlineType = 0;

    /**
     * 添加线上店铺接口
     *
     * @param addOnlineShopRequestVo
     * @author gaolei
     * @email gl_code@163.com
     * @date 2018-11-06
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse addOnlineShop(AddOnlineShopRequestVo addOnlineShopRequestVo) {
        OnlineShop onlineShop = new OnlineShop();
        if (addOnlineShopRequestVo.getMerchantUserId() == null) {
            return ApiResponse.buildCommonErrorResponse("登录信息错误");
        }
        Merchant merchant = merchantMapper.getByMerchantUserId(addOnlineShopRequestVo.getMerchantUserId());
        if (onlineShopMapper.getByMerchantId(merchant.getId()) != null) {
            return ApiResponse.buildCommonErrorResponse("同一个商户只能添加一个商铺");
        }
        if (addOnlineShopRequestVo.getCategoryId().size()==0||addOnlineShopRequestVo.getCategoryId()==null){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ApiResponse.buildCommonErrorResponse("请选择经营类目");
        }
//        if (!StringUtils.isEmpty(addOnlineShopRequestVo.getCommunicationMerchantId())) {
//            if (onlineShopMapper.getCommunicationMerchantIdCount(addOnlineShopRequestVo.getCommunicationMerchantId()) > 0) {
//                return ApiResponse.buildCommonErrorResponse("通联商户编号重复");
//            }
//        }
        boolean isRepeat = addOnlineShopRequestVo.getCategoryId().size() != new HashSet<String>(addOnlineShopRequestVo.getCategoryId()).size();
        if (isRepeat) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ApiResponse.buildCommonErrorResponse("经营类目重复");
        }
        try {
                 //OnlineShop shop = onlineShopMapper.getByMerchantIdRemove(merchant.getId());
                 onlineShop.setId(UUID.randomUUID().toString().replace("-", ""));
                 onlineShop.setMerchantId(merchant.getId());
                 onlineShop.setShopName(addOnlineShopRequestVo.getShopName());
                 onlineShop.setAddress(addOnlineShopRequestVo.getAddress());
                 onlineShop.setShopContact(addOnlineShopRequestVo.getShopContact());
                 onlineShop.setShopContactPhone(addOnlineShopRequestVo.getShopContactPhone());
                 onlineShop.setShopLicense(addOnlineShopRequestVo.getShopLicense());
                 onlineShop.setBusinessLicense(addOnlineShopRequestVo.getBusinessLicense());
                 onlineShop.setStorageStatus(0);
                 onlineShop.setAuditStatus(1);
                 onlineShop.setShopAdvert(addOnlineShopRequestVo.getShopAdvert());
                 onlineShop.setCommunicationMerchantId(addOnlineShopRequestVo.getCommunicationMerchantId());
                 String storeSn = RandomUtil.testNum(7);
                 onlineShop.setStoreSn(storeSn);

                 String provice = onlineShopMapper.selectCityById(addOnlineShopRequestVo.getProvinceId());
                 String city = onlineShopMapper.selectCityById(addOnlineShopRequestVo.getCityId());
                 String area = onlineShopMapper.selectCityById(addOnlineShopRequestVo.getAreaId());
                 onlineShop.setCity(city);
                 onlineShop.setProvince(provice);
                 onlineShop.setArea(area);

                 String a = getLatAndLon.getResponse(provice + city + area + addOnlineShopRequestVo.getAddress());
                 if (a != null) {
                     String[] longitudeAndLatitude = a.split(",");
                     onlineShop.setLongitude(longitudeAndLatitude[0]);
                     onlineShop.setLatitude(longitudeAndLatitude[1]);
                 } else {
                     onlineShop.setLongitude("0");
                     onlineShop.setLatitude("0");
                 }
                 onlineShop.setCreateBy(addOnlineShopRequestVo.getMerchantUserId());
                 onlineShop.setCreateTime(new Date());
                 //修改店铺类型
                 if (merchant.getShopType() == null) {
                     merchant.setShopType(addOnlineShopRequestVo.getShopType());
                     merchant.setIsRemove(0);
                     merchant.setUpdateBy(addOnlineShopRequestVo.getMerchantUserId());
                     merchant.setUpdateTime(new Date());
                 } else {
                     OnlineShop shop = onlineShopMapper.getByMerchantIdRemove(merchant.getId());
                     if (shop!=null){
                         onlineShop.setId(shop.getId());
                         onlineShopMapper.deleteScore(shop.getId());
                     }
                     onlineShopMapper.deleteByMerchantId(merchant.getId());
                     merchantAuditMapper.deleteByMerchantId(merchant.getId());
                     shopRelationCategoryMapper.deleteByMerchantId(merchant.getId());
                     collectionBankMapper.deleteByMerchantId(merchant.getId());
                     collectionTreasureMapper.deleteByMerchantId(merchant.getId());
                     collectionWxpayMapper.deleteByMerchantId(merchant.getId());
                     merchant.setShopType(addOnlineShopRequestVo.getShopType());
                     merchant.setIsRemove(0);
                     merchant.setUpdateBy(addOnlineShopRequestVo.getMerchantUserId());
                     merchant.setUpdateTime(new Date());
                 }
                 merchantMapper.updateShopType(merchant);
                 //店铺类目关系

                 addOnlineShopRequestVo.getCategoryId().forEach(categoryId -> {
                     ShopRelationCategory shopRelationCategory = new ShopRelationCategory();
                     shopRelationCategory.setId(UUID.randomUUID().toString().replace("-", ""));
                     shopRelationCategory.setMerchantId(merchant.getId());
                     shopRelationCategory.setShopId(onlineShop.getId());
                     shopRelationCategory.setCategoryId(categoryId);
                     shopRelationCategory.setCretaeBy(addOnlineShopRequestVo.getMerchantUserId());
                     shopRelationCategory.setCreateTime(new Date());
                     shopRelationCategoryMapper.saveRelationCategory(shopRelationCategory);
                 });
                 if (addOnlineShopRequestVo.getPayeeAccount() != null || addOnlineShopRequestVo.getTreasureAppId() != null
                         || addOnlineShopRequestVo.getPrivateKey() != null || addOnlineShopRequestVo.getPublicKey() != null || addOnlineShopRequestVo.getAlipayRsaType() != null) {
                     if (addOnlineShopRequestVo.getPayeeAccount() != null && addOnlineShopRequestVo.getTreasureAppId() != null
                             && addOnlineShopRequestVo.getPrivateKey() != null && addOnlineShopRequestVo.getPublicKey() != null
                             && addOnlineShopRequestVo.getAlipayRsaType() != null) {
                         CollectionTreasure collectionTreasure = new CollectionTreasure();
                         collectionTreasure.setId(UUID.randomUUID().toString().replace("-", ""));
                         collectionTreasure.setMerchantId(merchant.getId());
                         collectionTreasure.setShopId(onlineShop.getId());
                         collectionTreasure.setAppid(addOnlineShopRequestVo.getTreasureAppId());
                         collectionTreasure.setPayeeAccount(addOnlineShopRequestVo.getPayeeAccount());
                         collectionTreasure.setPrivateKey(addOnlineShopRequestVo.getPrivateKey());
                         collectionTreasure.setPublicKey(addOnlineShopRequestVo.getPublicKey());
                         collectionTreasure.setAlipayRsaType(addOnlineShopRequestVo.getAlipayRsaType());
                         collectionTreasure.setCreateBy(addOnlineShopRequestVo.getMerchantUserId());
                         collectionTreasure.setCreateTime(new Date());
                         onlineShop.setCollectionTreasureId(collectionTreasure.getId());
                         onlineShop.setUpdateBy(addOnlineShopRequestVo.getMerchantUserId());
                         onlineShop.setUpdateTime(new Date());
                         collectionTreasureMapper.saveCollectionTreasure(collectionTreasure);
                     } else {
                         TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                         return ApiResponse.buildCommonErrorResponse("支付宝参数不完整");
                     }

                 }
                 if (addOnlineShopRequestVo.getWxpayAppId() != null || addOnlineShopRequestVo.getMerchantsNumber() != null
                         || addOnlineShopRequestVo.getSecret() != null || addOnlineShopRequestVo.getWechatAppSecret() != null
                         || addOnlineShopRequestVo.getWechatCertPwd() != null || addOnlineShopRequestVo.getWechatCertId() != null) {
                     if (addOnlineShopRequestVo.getWxpayAppId() != null && addOnlineShopRequestVo.getMerchantsNumber() != null
                             && addOnlineShopRequestVo.getSecret() != null && addOnlineShopRequestVo.getWechatAppSecret() != null
                             && addOnlineShopRequestVo.getWechatCertPwd() != null && addOnlineShopRequestVo.getWechatCertId() != null) {
                         CollectionWxpay collectionWxpay = new CollectionWxpay();
                         collectionWxpay.setId(UUID.randomUUID().toString().replace("-", ""));
                         collectionWxpay.setMerchantId(merchant.getId());
                         collectionWxpay.setShopId(onlineShop.getId());
                         collectionWxpay.setSecret(addOnlineShopRequestVo.getSecret());
                         collectionWxpay.setAppId(addOnlineShopRequestVo.getWxpayAppId());
                         collectionWxpay.setMerchantsNumber(addOnlineShopRequestVo.getMerchantsNumber());
                         collectionWxpay.setWechatAppSecret(addOnlineShopRequestVo.getWechatAppSecret());
                         collectionWxpay.setWechatCertId(addOnlineShopRequestVo.getWechatCertId());
                         collectionWxpay.setWechatCertPwd(addOnlineShopRequestVo.getWechatCertPwd());
                         collectionWxpay.setCreateBy(addOnlineShopRequestVo.getMerchantUserId());
                         collectionWxpay.setCreateTime(new Date());
                         onlineShop.setCollectionWxpayId(collectionWxpay.getId());
                         onlineShop.setUpdateBy(addOnlineShopRequestVo.getMerchantUserId());
                         onlineShop.setUpdateTime(new Date());
                         collectionWxpayMapper.saveCollectionWxpay(collectionWxpay);
                     } else {
                         TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                         return ApiResponse.buildCommonErrorResponse("微信参数不完整");
                     }

                 }
                 if (addOnlineShopRequestVo.getCollectionBank() != null || addOnlineShopRequestVo.getCollectionUser() != null
                         || addOnlineShopRequestVo.getCollectionBankCard() != null || addOnlineShopRequestVo.getMerchantNo() != null) {
                     if (addOnlineShopRequestVo.getCollectionBank() != null && addOnlineShopRequestVo.getCollectionUser() != null
                             && addOnlineShopRequestVo.getCollectionBankCard() != null && addOnlineShopRequestVo.getMerchantNo() != null) {
                         CollectionBank collectionBank = new CollectionBank();
                         collectionBank.setId(UUID.randomUUID().toString().replace("-", ""));
                         collectionBank.setMerchantId(merchant.getId());
                         collectionBank.setShopId(onlineShop.getId());
                         collectionBank.setCollectionBank(addOnlineShopRequestVo.getCollectionBank());
                         collectionBank.setCollectionUser(addOnlineShopRequestVo.getCollectionUser());
                         collectionBank.setCollectionBankCard(addOnlineShopRequestVo.getCollectionBankCard());
                         collectionBank.setMerchantsNumber(addOnlineShopRequestVo.getMerchantNo());
                         collectionBank.setCreateBy(addOnlineShopRequestVo.getMerchantUserId());
                         collectionBank.setCreateTime(new Date());
                         onlineShop.setCollectionBankId(collectionBank.getId());
                         onlineShop.setUpdateBy(addOnlineShopRequestVo.getMerchantUserId());
                         onlineShop.setUpdateTime(new Date());
                         collectionBankMapper.saveCollectionBank(collectionBank);
                     } else {
                         TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                         return ApiResponse.buildCommonErrorResponse("银行卡参数不完整");
                     }
                 }
                 if (addOnlineShopRequestVo.getCollectionBankCard() == null && addOnlineShopRequestVo.getWxpayAppId() == null && addOnlineShopRequestVo.getPayeeAccount() == null) {
                     TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                     return ApiResponse.buildCommonErrorResponse("至少一种支付信息");
                 }
                 //保存添加线上店铺审核
                 saveOnlineShopAudit(addOnlineShopRequestVo, merchant, onlineShop);
                 onlineShopMapper.saveOnlineShop(onlineShop);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ApiResponse.buildCommonErrorResponse("线上店铺添加失败");
        }
        return ApiResponse.buildResponse(200, "线上店铺添加成功");
    }
}
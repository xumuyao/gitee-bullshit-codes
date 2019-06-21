
public class RedisCache {
    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);
    private static final long MAX_SIZE = 2048;
    private static final long EXPIRE = 15; //可适当调大
    private LoadingCache<String, Optional<SysLan>> lanCache;
 @PostConstruct
    public void init() {
        lanCache = CacheBuilder.newBuilder().maximumSize(MAX_SIZE)
                .expireAfterWrite(EXPIRE, TimeUnit.MINUTES).build(new CacheLoader<String, Optional<SysLan>>() {
                    @Override
                    public Optional<SysLan> load(String key) throws Exception {
                        // https://blog.csdn.net/codingtu/article/details/89577316
                            return loadLanData(key);

                    }
                });
    }

    private Optional<SysLan> loadLanData(String key) {
        
        SysLan sysLan = null;
        if (null == redisManager) {
            return null;
        }
        String value = redisManager.get("slan:" + key);
        if (StringUtils.isNotBlank(value)) {
            sysLan = JSONObject.parseObject(value, SysLan.class);
            return Optional.of(sysLan);
        }
        return Optional.empty();
    }

    /****** java.util.NoSuchElementException: No value present *****/ 滥用Optional，本意是想解决 cacheloader returned null for key
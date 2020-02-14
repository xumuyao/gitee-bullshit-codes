
@Service
public class ROLEService extends BaseService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;



    public List<Map> get(String userId) {
        RoleMapper mapper = super.getSqlSession().getMapper(RoleMapper.class);
        List<Map> right = mapper.get(userId);
        return right;
    }
    public List<Map> set(String userId) {
        RoleMapper mapper = super.getSqlSession().getMapper(RoleMapper.class);
        List<Map> right = mapper.set(userId);
        return right;
    }

     
}
/**
   使用springboot框架，用了@Controller注解
**/
@RequestMapping("/functional")
@Controller
public class FunctionalPageController {
   @Resource
    private URLMapService urlMapService;

   @RequestMapping("/totalData")
    public Result totalData(）{
       List<WondersLogUrlMap> list = urlMapService.getTableData();
       return Result.success(list);
    }
}
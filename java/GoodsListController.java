/**
 * 获取商品列表.
 *
 * @author 强哥
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** 一个远程微服务接口, 商品相关的管理, 通过spring boot提供 */
interface GoodsServiceAPi {

  /**
   * 通过检索条件 获取商品列表
   *
   * @param params
   * @return
   */
  ArrayList getGoodsList(Map params);
}

/**
 * 一个远程微服务接口,
 *
 * <p>优惠活动相关的接口
 *
 * <p>通过spring boot提供
 */
interface PromotionServiceAPi {

  /** 通过检索条件 获取商品列表 */
  Map getPromotionBygoodsId(Map params);
}

/**
 * 一个远程微服务接口,
 *
 * <p>价格相关的接口
 *
 * <p>通过spring boot提供
 */
interface PriceServiceAPi {

  /** 通过检索条件 获取商品列表 */
  Map getPriceBygoodsId(Map params);
}

/**
 * 一个远程微服务接口,
 *
 * <p>库存相关的接口
 *
 * <p>通过spring boot提供
 */
interface StoreServiceAPi {

  /** 通过检索条件 获取商品列表 */
  Map getStoreBygoodsId(Map params);
}

// @RestController
// @RequestMapping("/api/goods/list")
public class GoodsListController {

  // 缓存处理  这里是一个map做的
  Map<Map, ArrayList> cache = new HashMap<>();

  // @Inject
  GoodsServiceAPi goodsServiceAPi;

  // @Inject
  PriceServiceAPi priceServiceAPi;

  // @Inject
  StoreServiceAPi storeServiceAPi;
  // @Inject
  PromotionServiceAPi promotionServiceAPi;

  // @RequestMapping("/list")
  public ArrayList goodsList() {
    // ==========各种商品筛选条件获取==========

    Map tj = new HashMap();

    tj.put("price", "XXXX");

    tj.put("Brand", "xxxx");

    // ... 省略 很多条件

    // ======================
    // 如果查询条件之前使用过,直接从缓存中返回
    ArrayList<Map> list = cache.get(tj);

    if (list != null) {
      return list;
    }

    list = goodsServiceAPi.getGoodsList(tj);

    for (Map g : list) {
      Map p = new HashMap();

      // ---组装条件-------
      // p.put XXXX
      // -------
      // 查库存
      try {
        Map store = storeServiceAPi.getStoreBygoodsId(p);

        // 库存判断
        if (store.get("XXXX") == "XXXX") {

          // 查询促销信息
          Map prom = promotionServiceAPi.getPromotionBygoodsId(p);

          if (prom.get("XXXX") == "xxxx") {

            g.put("prom", prom.get("prom"));
            // 这里组装满足促销的信息,下边查询促销后的价格
            // p.put XXXX
          }

          // 查价格
          Map price = priceServiceAPi.getPriceBygoodsId(p);

          // 设置商品价格
          g.put("price", price.get("price"));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    // 缓存条件和结果
    cache.put(tj, list);

    return list;
  };

  // 上边的map 在实际中用的都是对应的对象封装类.
  // 注释掉的代码为了保证编译通过
  // 上边代码实际非常复杂,洋洋几百行代码,出现了一个连环逻辑问题.

  // 坑:
  // 咋一看 貌似没啥问题..高级码农挖的高级坑.

  // 1. 66行的map 是一个 线程不安全的 map..
  // 2. cache 没有缓存机制,如何释放内存
  // 3. 这些接口的服务都是提供在别的服务器上,通过http访问的...  这里写了循环中调用大量微服务,这些微服务内部还有 循环调用其他微服务,
  // 把微服务当做简单的类使用了...并发上来后基本都卡死了.
  // 4. 由于被缓存挡上了 当商品信息发生变化时候,再也看不到新的数据了.
  // 5. 为了防止for循环挂掉添加了一个try.但是,没有考虑到循环中对list的数据进行了修改, list之后被缓存起来了.出现异常的数据,里边价格,促销,等等信息都没有,导致调用该服务的地方挂了.
}

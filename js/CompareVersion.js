/**
 * 版本号比较 版本号格式 1.2.3
 * @param v1 当前
 * @param v2 目标
 *
 * @return 目标与当前比较  大于返回1 小于返回-1 相等 返回0
 * @constructor
 */
function CompareVersion(v1, v2) {

  v1 = v1.split("\\.");
  v2 = v2.split("\\.");

  for (let i = 0; i < v1.length; i++) {

    if (v1[i] > v2[i]) {
      return -1
    } else if (v1[i] < v2[i]) {
      return 1;
    } else {
      return 0
    }

  }

}

console.info("%s  : %s=%d ", "1.2.3", "1.2.4", CompareVersion("1.2.3", "1.2.4"))
console.info("%s  : %s=%d ", "1.2.3", "1.2.3", CompareVersion("1.2.3", "1.2.3"))
console.info("%s  : %s=%d ", "1.2.4", "1.2.3", CompareVersion("1.2.4", "1.2.3"))
console.info("====下边都不对======")
console.info("%s  : %s=%d ", "1.12.3", "1.2.4", CompareVersion("1.12.3", "1.2.4"))
console.info("%s  : %s=%d ", "12.2.3", "2.2.4", CompareVersion("12.2.3", "2.2.4"))
console.info("%s  : %s=%d ", "1.2.3", "1.2.21", CompareVersion("1.2.3", "1.2.21"))
console.info("==========")
// 坑
//没有考虑到 js弱类型的问题 不特殊处理,字符串比较式按照字典书序来的, 这样才会出现 2>12的情况
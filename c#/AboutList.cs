
using System.Collections.Generic;
using System.Linq;

namespace Microsoft.Fawvw.HR.Platform.Api.Controllers
{
    public class AboutList
    {
        /// <summary>
        /// 判断list是否为空
        /// </summary>
        public void ChargeList()
        {
            var list = new List<int>();//正常是请求一个service返回一个list
            if (list != null && list.Count > 0) //直接使用list.Any()即可
            {
                //继续下面的代码逻辑处理……
            }
        }
    }
}

using System.Collections.Generic;
using System.Linq;

namespace Microsoft.Fawvw.HR.Platform.Api.Controllers
{
    public class ListInstall
    {
        /// <summary>
        /// 导师让我用一行代码给一个List<List<string>>初始化2个数据 苦思冥想于是有了下一行代码
        /// </summary>
        public ListInstall()
        {
            List<List<string>> list = new List<List<string>>(new List<string>[2].Select(p => new List<string>()));
        }
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Lottery.Data;
using System.IO;
using System.Web;
using System.Web.Caching;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Xml;
using EntityFramework.Extensions;
using System.Globalization;
using System.Threading;
using Lottery.Model;
using CountUserDTO = Lottery.WebToService.CountUserDTO;
using CountSendDTO = Lottery.WebToService.CountSendDTO;

/*
14年的时候写的,看名字就碉堡
*/
namespace Lottery.Service
{
    public class 配置文件实体
    {
        public long 期号代码
        {
            get;
            set;
        }
        public DateTime 开奖日期
        {
            get;
            set;
        }
    }
    public class 时时彩开奖
    {

        /// <summary>
        /// 延迟60秒去获取
        /// </summary>
        public static readonly Int32 延迟读取秒数 = 20;

        public const String 时时彩客户端标识 = "67BE4FB5-9ED4-41B0-90EF-A6FE52E1ECE2";
        public const Int32 时时彩客户端ID = 1;
        public System.Timers.Timer 定时器 = new System.Timers.Timer();

        public static DateTime 当天日期 = DateTime.Today;
        public static List<配置文件实体> 开奖列表 = null;
        public 时时彩开奖()
        {
            开奖列表 = 读取配置文件生产日期(当天日期);
            if (开奖列表 == null || 开奖列表.Count == 0)
            {
                当天日期 = 当天日期.AddDays(1);
                开奖列表 = 读取配置文件生产日期(当天日期);
            }
            定时器.Elapsed += new System.Timers.ElapsedEventHandler(时间到达执行的事件);
            定时器.Enabled = true;
            var 下次执行间隔 = (开奖列表[0].开奖日期 - DateTime.Now).TotalMilliseconds;
            定时器.Interval = 下次执行间隔;
            定时器.Start();
        }

        void 时间到达执行的事件(object sender, System.Timers.ElapsedEventArgs e)
        {
            Console.WriteLine(延迟读取秒数 + "秒后，准备读取开奖信息" + DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
            Action<long> 异步执行 = (期号) =>
            {
                Thread.Sleep(延迟读取秒数 * 1000);
                读取开奖号和修改数据库开奖信息(期号);
            };
            异步执行.BeginInvoke(开奖列表[0].期号代码, null, null);
            lock (开奖列表)
            {
                定时器.Stop();
                开奖列表.RemoveAt(0);
                if (开奖列表 == null || 开奖列表.Count == 0)
                {
                    当天日期 = 当天日期.AddDays(1);
                    开奖列表 = 读取配置文件生产日期(当天日期);
                }
                MyTcpChannel.NoticNextOpenTime(时时彩客户端ID, new NextOpenTime
                {
                    OpenTime = 开奖列表[0].开奖日期,
                    Expect = 开奖列表[0].期号代码
                });
                var 下次执行间隔 = (开奖列表[0].开奖日期 - DateTime.Now).TotalMilliseconds;
                定时器.Interval = 下次执行间隔;
                定时器.Start();
            }
        }
        public void 读取开奖号和修改数据库开奖信息(long 期号, Int32 计数 = 0)
        {
            var 开奖信息 = 开奖获取.读取开奖信息(期号);
            if (开奖信息 == null)
            {
                计数++;
                Console.WriteLine("第{0}次读取,未读取到开奖信息{1}秒后重新读取", 计数, (5 * 计数));
                Thread.Sleep(5000 * 计数);
                if (计数 > 10)
                {
                    return;
                }
                Action<long, Int32> 异步执行 = (_期号, _计数) =>
                {
                    读取开奖号和修改数据库开奖信息(_期号, _计数);
                };
                异步执行.BeginInvoke(期号, 计数, null, null);
                //TODO:延迟读取
                return;
            }
            else
            {
                修改数据库开奖信息(开奖信息);

            }
        }

        public void 修改数据库开奖信息(开奖实体 开奖信息, DateTime? 日期 = null, Boolean Notice = true)
        {
            if (!日期.HasValue)
            {
                日期 = DateTime.Today;
            }
            var 开奖数组 = 开奖信息.开奖号.Split(',');
            var 开奖用户信息 = new List<CountUserDTO>();
            var 当前彩种ID = 0;
            using (var 数据库实体 = new LotteryDbContext())
            {

                数据库实体.Transaction(() =>
                {
                    var 彩票类型 = 数据库实体.LotteryTypes.Where(x => x.LotteryTypeGUID.Equals(时时彩客户端标识)).FirstOrDefault();
                    var 玩法 = 数据库实体.PlayTypes.Where(x => x.LotteryTypeID.Equals(彩票类型.LotteryTypeID));
                    var 数据库当前期号实体 = 数据库实体.LotteryInfos.Where(x => x.LotteryInfoCode.Equals(开奖信息.期号代码)).FirstOrDefault();

                    当前彩种ID = 彩票类型.LotteryTypeID;

                    if (数据库当前期号实体 == null)
                    {
                        数据库当前期号实体 = new LotteryInfos
                        {
                            LotteryInfoCode = 开奖信息.期号代码,
                            LotteryInfoDate = 日期.Value,
                            LotteryInfoOpenCode = 开奖信息.开奖号,
                            LotteryInfoOpenTime = null,
                            LotteryTypeID = 彩票类型.LotteryTypeID
                        };
                        数据库实体.LotteryInfos.AddObject(数据库当前期号实体);
                        数据库实体.SaveChanges();
                    }
                    foreach (var 玩法详细 in 玩法)
                    {
                        var 当前玩法开奖号 = new StringBuilder();

                        for (int i = 玩法详细.PlayTypeStartIdx; i <= 玩法详细.PlayTypeEndIdx; i++)
                        {
                            当前玩法开奖号.Append(开奖数组[i]);
                        }
                        当前玩法开奖号.Append("|");
                        var 开奖字符串 = 当前玩法开奖号.ToString();
                        var 猜中的用户ID = 数据库实体.UserLotterys.Where(x =>
                            x.UserLotteryExpect.Equals(开奖信息.期号代码)
                            && x.LotteryTypeID.Equals(彩票类型.LotteryTypeID)
                       && x.Content.Contains(开奖字符串)
                       && x.PlayTypeID.Equals(玩法详细.PlayTypeID)
                       && x.IsWin.Equals(0)).Select(x => x.UserId).ToList();

                        if (猜中的用户ID.Count > 0)
                        {
                            数据库实体.UserLotterys.Where(x =>
                                x.UserLotteryExpect.Equals(开奖信息.期号代码)
                                && x.LotteryTypeID.Equals(彩票类型.LotteryTypeID)
                                && 猜中的用户ID.Contains(x.UserId)
                                && x.PlayTypeID.Equals(玩法详细.PlayTypeID)
                           && x.IsWin.Equals(0)).Update(x => new UserLotterys
                           {
                               IsWin = 1
                           });
                            数据库实体.SaveChanges();
                            foreach (var 用户ID in 猜中的用户ID)
                            {
                                开奖用户信息.Add(new CountUserDTO
                                {
                                    playTypeId = 玩法详细.PlayTypeID,
                                    playTypeName = 玩法详细.PlayTypeName,
                                    IsWin = 1,
                                    userId = 用户ID,
                                });
                                var 用户统计信息 = 数据库实体.UserCounts.Where(x =>
                                    x.userId.Equals(用户ID)
                                    && x.LotteryTypeId.Equals(彩票类型.LotteryTypeID)
                                    && x.PlayTypeId.Equals(玩法详细.PlayTypeID)).FirstOrDefault();
                                用户统计信息.WinNum += 1;
                                用户统计信息.MathHistory += "1";

                            }
                            数据库实体.SaveChanges();
                        }
                        //.UserRules.Where(x => 猜中的用户ID.Contains(x.UserID)).Update(x => new UserRules
                        //{
                        //    UserWinNum = x.UserWinNum + 1
                        //});


                        var 未猜中的用户ID = 数据库实体.UserLotterys.Where(x =>
                           x.UserLotteryExpect.Equals(开奖信息.期号代码)
                           && x.IsWin.Equals(0)
                           && x.LotteryTypeID.Equals(彩票类型.LotteryTypeID)
                      && !x.Content.Contains(开奖字符串)
                      && x.PlayTypeID.Equals(玩法详细.PlayTypeID)).Select(x => x.UserId).ToList();

                        if (未猜中的用户ID.Count > 0)
                        {
                            数据库实体.UserLotterys.Where(x =>
                                x.UserLotteryExpect.Equals(开奖信息.期号代码)
                                && 未猜中的用户ID.Contains(x.UserId)
                                && x.LotteryTypeID.Equals(彩票类型.LotteryTypeID)
                                && x.PlayTypeID.Equals(玩法详细.PlayTypeID)
                                && x.IsWin.Equals(0)).Update(x => new UserLotterys
                                {
                                    IsWin = 2
                                });
                            数据库实体.SaveChanges();
                            foreach (var 用户ID in 未猜中的用户ID)
                            {
                                开奖用户信息.Add(new CountUserDTO
                                {
                                    playTypeId = 玩法详细.PlayTypeID,
                                    playTypeName = 玩法详细.PlayTypeName,
                                    IsWin = 2,
                                    userId = 用户ID,
                                });
                                var 用户统计信息 = 数据库实体.UserCounts.Where(x =>
                                    x.userId.Equals(用户ID)
                                    && x.LotteryTypeId.Equals(彩票类型.LotteryTypeID)
                                    && x.PlayTypeId.Equals(玩法详细.PlayTypeID)).FirstOrDefault();
                                用户统计信息.MathHistory += "2";
                            }
                            数据库实体.SaveChanges();
                        }
                    }
                });
            }
            if (Notice)
            {
                var 通知开奖实体 = new LotteyInfo
                {
                    Expect = 开奖信息.期号代码,
                    OpenCode = 开奖信息.开奖号
                };
                var 中奖数据 = from p in 开奖用户信息
                           group p by p.userId into g
                           select new
                           {
                               Key = g.Key,
                               Value = g.Select(x => new CountSendDTO
                               {
                                   IsWin = x.IsWin,
                                   playTypeId = x.playTypeId,
                                   playTypeName = x.playTypeName
                               })

                           };
                var 传输参数 = 中奖数据.ToDictionary(x => x.Key, x => x.Value.ToList());

                MyTcpChannel.OpenLotteyNotic(当前彩种ID, 通知开奖实体, 传输参数);
            }
        }
        public static List<配置文件实体> 读取配置文件生产日期(DateTime? 要生产的日期 = null)
        {
            var 当天开奖列表 = new List<配置文件实体>();
            var 生产日期 = DateTime.Today;
            if (要生产的日期.HasValue)
            {
                生产日期 = 要生产的日期.Value;
            }
            var 数据库实体 = new LotteryDbContext();
            var 彩票类型 = 数据库实体.LotteryTypes.Where(x => x.LotteryTypeGUID.Equals(时时彩客户端标识)).FirstOrDefault();
            using (var 文件流 = new StreamReader(Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "OTConfig/CQSSC.txt")))
            {
                var 一行数据 = 文件流.ReadLine();
                while (!String.IsNullOrEmpty(一行数据))
                {
                    var 临时变量数组 = 一行数据.Split('|');

                    if (临时变量数组[0] == "120")
                    {
                        当天开奖列表.Add(new 配置文件实体
                        {
                            期号代码 = Convert.ToInt64(生产日期.AddDays(-1).ToString(彩票类型.LotteryCodeFormat) + 临时变量数组[0]),
                            开奖日期 = Convert.ToDateTime(String.Format("{0} {1}:00", 生产日期.ToString("yyyy-MM-dd"), 临时变量数组[1]))
                        });
                    }
                    else
                    {
                        当天开奖列表.Add(new 配置文件实体
                        {
                            期号代码 = Convert.ToInt64(生产日期.ToString(彩票类型.LotteryCodeFormat) + 临时变量数组[0]),
                            开奖日期 = Convert.ToDateTime(String.Format("{0} {1}:00", 生产日期.ToString("yyyy-MM-dd"), 临时变量数组[1]))
                        });
                    }
                    一行数据 = 文件流.ReadLine();
                }
            }
            当天开奖列表 = 当天开奖列表.Where(x => x.开奖日期 >= DateTime.Now).ToList();
            return 当天开奖列表;
        }
        public void 期号补遗()
        {
            当天补遗();
            遗漏开奖();
            new 历史比赛记录().重新计算();
            #region 历史补遗
            var 数据库实体 = new LotteryDbContext();
            var 彩票类型 = 数据库实体.LotteryTypes.Where(x => x.LotteryTypeGUID.Equals(时时彩客户端标识)).Select(x => new { x.LotteryTypeID, x.LotteryMaxNum }).FirstOrDefault();
            var 查询结果 = from 开奖信息 in 数据库实体.LotteryInfos
                       group 开奖信息 by 开奖信息.LotteryInfoDate into 新集合
                       where 新集合.Count() < 100 &&
                       新集合.Key != DateTime.Today
                       select new
                       {
                           日期 = 新集合.Key
                       };
            foreach (var 当天日期 in 查询结果)
            {
                var 开奖查询结果 = 开奖获取.时时彩500W网站获取(当天日期.日期);
                var 数据库当前期号实体 = 数据库实体.LotteryInfos.Where(x => x.LotteryInfoDate == 当天日期.日期 && x.LotteryTypeID.Equals(彩票类型.LotteryTypeID)).Select(x => x.LotteryInfoCode).ToList();
                开奖查询结果 = 开奖查询结果.Where(x => !数据库当前期号实体.Contains(x.期号代码)).ToList();
                foreach (var 开奖信息 in 开奖查询结果)
                {

                    修改数据库开奖信息(开奖信息, 当天日期.日期, false);

                }
            }
            #endregion 历史补遗
        }
        public void 当天补遗()
        {

            var 当天日期 = DateTime.Today;
            var 开奖查询结果 = 开奖获取.时时彩500W网站获取(当天日期);
            var 数据库实体 = new LotteryDbContext();
            var 彩票类型 = 数据库实体.LotteryTypes.Where(x => x.LotteryTypeGUID.Equals(时时彩客户端标识)).Select(x => new { x.LotteryTypeID, x.LotteryMaxNum }).FirstOrDefault();
            var 数据库当前期号实体 = 数据库实体.LotteryInfos.Where(x => x.LotteryInfoDate == 当天日期 && x.LotteryTypeID.Equals(彩票类型.LotteryTypeID)).Select(x => x.LotteryInfoCode).ToList();
            开奖查询结果 = 开奖查询结果.Where(x => !数据库当前期号实体.Contains(x.期号代码)).ToList();
            foreach (var 开奖信息 in 开奖查询结果)
            {

                修改数据库开奖信息(开奖信息, 当天日期, false);

            }
        }

        public void 遗漏开奖()
        {
            var 数据库实体 = new LotteryDbContext();
            var 彩票类型 = 数据库实体.LotteryTypes.Where(x => x.LotteryTypeGUID.Equals(时时彩客户端标识)).Select(x => new { x.LotteryTypeID, x.LotteryMaxNum }).FirstOrDefault();
            var 查询期号 = from p in 数据库实体.UserLotterys.Where(x => x.LotteryTypeID.Equals(彩票类型.LotteryTypeID)
                   && x.IsWin == 0)
                       group p by p.UserLotteryExpect into 新集合
                       select 新集合.Key;
            foreach (var 期号 in 查询期号)
            {

                var 开奖信息 = 开奖获取.读取开奖信息(期号);
                if (开奖信息 != null)
                {
                    var 查询时间 = DateTime.ParseExact(期号.ToString().Substring(0, 8), "yyyyMMdd", System.Globalization.CultureInfo.CurrentCulture);
                    修改数据库开奖信息(开奖信息, 查询时间, false);
                }
            }
        }
    }
    public class 开奖实体
    {
        public long 期号代码
        {
            get;
            set;
        }
        public String 开奖号
        {
            get;
            set;
        }
    }
    public class 开奖获取
    {
        public static 开奖实体 时时彩官网获取(long 期号代码)
        {
            try
            {
                var Http访问客户端 = new HttpClient();
                Http访问客户端.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("*/*"));
                Http访问客户端.DefaultRequestHeaders.AcceptLanguage.Add(new StringWithQualityHeaderValue("zh-cn"));
                Http访问客户端.DefaultRequestHeaders.Referrer = new Uri("http://www.cqcp.net/game/ssc/");

                Http访问客户端.DefaultRequestHeaders.Add("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
                var Post提交内容 = new FormUrlEncodedContent(new Dictionary<String, String>
            {
                {"idMode","3002"},
                {"iCount","1"}
            });
                var Http请求信息 = new HttpRequestMessage(HttpMethod.Post, "http://www.cqcp.net/ajaxhttp/game/getopennumber.aspx");
                Http请求信息.Content = Post提交内容;
                Http请求信息.Content.Headers.ContentType = new MediaTypeHeaderValue("application/x-www-form-urlencoded");
                var 返回的HTML = Http访问客户端.SendAsync(Http请求信息).Result.Content.ReadAsStringAsync().Result;
                HtmlAgilityPack.HtmlDocument HTML文档解析类 = new HtmlAgilityPack.HtmlDocument();
                HTML文档解析类.LoadHtml(返回的HTML);
                var 查找ul标签 = HTML文档解析类.DocumentNode.SelectNodes("ul");
                if (查找ul标签 != null && 查找ul标签.Count > 1)
                {
                    for (var 循环变量i = 1; 循环变量i < 查找ul标签.Count; 循环变量i++)
                    {
                        var 当前ul标签 = 查找ul标签[循环变量i];
                        var 期号 = Convert.ToInt64("20" + 当前ul标签.SelectSingleNode("li[1]").InnerText);
                        var 开奖号 = 当前ul标签.SelectSingleNode("li[2]").InnerText.Replace("-", ",");
                        if (期号 == 期号代码)
                        {
                            return new 开奖实体
                            {
                                期号代码 = 期号,
                                开奖号 = 开奖号.Replace("-", ","),

                            };
                        }
                    }
                }
            }
            catch { }
            return null;
        }

        public static 开奖实体 读取开奖信息(long 期号代码)
        {
            var 返回数据 = 时时彩官网获取(期号代码);
            if (返回数据 == null)
            {
                var 查询时间 = DateTime.ParseExact(期号代码.ToString().Substring(0, 8), "yyyyMMdd", System.Globalization.CultureInfo.CurrentCulture);
                var 查询当日的开奖信息 = 时时彩500W网站获取(查询时间);
                返回数据 = 查询当日的开奖信息.Where(x => x.期号代码.Equals(期号代码)).FirstOrDefault();
            }
            return 返回数据;
        }
        public static List<开奖实体> 时时彩500W网站获取(DateTime? 查询时间 = null)
        {
            if (查询时间 == null)
            {
                查询时间 = DateTime.Now;
            }
            var requestUri = String.Format("http://kaijiang.500.com/static/public/ssc/xml/qihaoxml/{0}.xml", 查询时间.Value.ToString("yyyyMMdd"));
            XmlDocument XML文档解析 = new XmlDocument();
            XML文档解析.Load(requestUri);
            var 所有开奖节点 = XML文档解析.SelectNodes("xml/row");

            var 返回数据 = new List<开奖实体>();
            foreach (var 节点明细 in 所有开奖节点)
            {
                var 节点元素 = (XmlElement)节点明细;
                var 开奖实例 = new 开奖实体
                {
                    期号代码 = Convert.ToInt64(节点元素.GetAttribute("expect")),
                    开奖号 = 节点元素.GetAttribute("opencode"),
                };
                返回数据.Add(开奖实例);
            }
            return 返回数据;

        }
    }
}

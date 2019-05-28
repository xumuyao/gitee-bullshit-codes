using System;

namespace test
{
    public class Util
    {
        /// <summary>
        /// 返回值加三元运算符值
        /// 好像没什么不对 -_-!
        /// </summary>
        /// <param name="inputValue"></param>
        /// <param name="type"></param>
        /// <returns></returns>
        public static int GetValue(int inputValue, int type)
        {
            return inputValue + type == 1 ? 1 : 2;
        }
    }
}

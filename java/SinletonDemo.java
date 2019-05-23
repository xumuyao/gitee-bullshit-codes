class SingletonDemo {

    // 单例写法参加：https://blog.51cto.com/aiilive/2164281
    public static class Singleton {
        // 坑人的单例
        static Singleton i = null;

        public static Singleton Instance() {
            if (i == null)
                i = new Singleton();
            return i;
        }
    }
}
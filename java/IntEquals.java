public void getIncrT(String key,Integer minutes,Integer count) {
    if(redisTemplate.opsForValue().setIfAbsent(key,"1")) {
        if (null != minutes) {
            redisTemplate.expire(key, minutes, TimeUnit.MINUTES);
        }
        return;
    }else {
        String currentValue = redisTemplate.opsForValue().get(key);
        //如果当前数量已经 == 已存数量 则不继续累加
        if(Integer.valueOf(currentValue) == count) {
            throw new RuntimeException("超过限流数量");
        }else {
            RedisAtomicInteger integer  = new RedisAtomicInteger(key, redisTemplate.getConnectionFactory());
            integer.getAndIncrement();
        }
    }
}
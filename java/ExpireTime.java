/**
*  1. 先把当前时间戳转为Date   
*  2. 获取再获取Date时间戳  
*  3. 获取到当前Date的时间戳+要缓存的时间
*  4. 再将要过期的时间戳转为Date
*  6. 获取要过期的Date的时间戳
*. 7. 要过期的时间戳-当前时间（为缓存时间）
**/

long now = System.currentTimeMillis();
long expire_time = new Date(
        DateUtil.parseYYYYMMDD(DateUtil.formatYYYYMMDD(now)).getTime()
                + 24 * 60 * 60 * 1000L).getTime() - now;
Account account = this.accountService.get(accountId);
redisTemplate.opsForValue().set(key, now + "", expire_time, TimeUnit.MILLISECONDS);
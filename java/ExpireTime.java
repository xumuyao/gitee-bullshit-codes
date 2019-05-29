  long now = System.currentTimeMillis();
long expire_time = new Date(
        DateUtil.parseYYYYMMDD(DateUtil.formatYYYYMMDD(now)).getTime()
                + 24 * 60 * 60 * 1000L).getTime() - now;
Account account = this.accountService.get(accountId);
redisTemplate.opsForValue().set(key, now + "", expire_time, TimeUnit.MILLISECONDS);
//Stream 用的66的
final EventAction eventAction = redisObj(
                EventActionKey + distributionEventId,
                () -> Optional
                        .of(distributionEventId)
                        .map(eventId -> {
                            final EventActionExample example = new EventActionExample();
                            example.createCriteria()
                                    .andEventIdEqualTo(eventId)
                                    .andTriggerTypeEqualTo(EnumEventTriggerType.DISTRIBUTION_PURCHASE.getCode().byteValue());
                            return example;
                        })
                        .map(eventActionMapper::selectByExample)
                        .filter(StringUtil::isNotEmpty)
                        .map(e -> e.get(0)).orElseThrow(() -> ExceptionUtil.createParamException("事件触发信息不存在"))
                , EventAction.class);
        final AwardConfig awardConfig = redisObj(EventConfigKey + eventAction.getId(),
                () -> Optional.ofNullable(eventAction.getId())
                        .map(actionId -> {
                            final AwardConfigExample example = new AwardConfigExample();
                            example.createCriteria()
                                    .andActionIdEqualTo(actionId);
                            return example;
                        })
                        .map(awardConfigMapper::selectByExample)
                        .filter(StringUtil::isNotEmpty)
                        .map(e -> e.get(0)).orElseThrow(() -> ExceptionUtil.createParamException("xxx")),
                AwardConfig.class
        );
        Optional.of(req)
                .map(e -> e.clueUid)
                .map(id -> {
                    final ClueExample example = new ClueExample();
                    example.createCriteria()
                            .andClueUidEqualTo(id)
                            .andDeletedEqualTo(false)
                            .andReceivedEqualTo(false)
                            .andCreateTimeGreaterThan(now - cluetime);
                    example.setOrderByClause("create_time asc");
                    return example;
                })  // 获取该被邀请人所有未过期且未被领取的线索的线索
                .map(clueMapper::selectByExample)
                .filter(StringUtil::isNotEmpty)
                .ifPresent(clues -> {
                            final ClueResp clueResp = Optional.of(req)
                                    .filter(c -> {
                                        c.count = clues.size();
                                        return true;
                                    })
                                    .map(this::awardValue)
                                    .orElseThrow(() -> ExceptionUtil.createParamException("参数错误"));
                            final Integer specialId = req.getIsHead()
                                    ? clues.get(0).getId()
                                    : clues.get(clues.size() - 1).getId();
                            clues.stream()
                                    .peek(clue -> {
                                        final AwardConfig awardConfigclone = Optional.of(awardConfig)
                                                .map(JSONUtil::obj2Json)
                                                .map(json -> JSONUtil.json2Obj(json, AwardConfig.class))
                                                .orElseGet(AwardConfig::new);
                                        awardConfigclone.setMoney(
                                                Optional.of(clue.getId())
                                                        .filter(specialId::equals)
                                                        .map(e -> clueResp.specialReward.longValue())
                                                        .orElse(clueResp.otherAverageReward.longValue())
                                        );
                                        eventActionService.assembleAward(
                                                awardConfigclone,
                                                clue.getAdviserUid(),
                                                clue.getAdviserUid(),
                                                clue.getClueUid(),
                                                eventAction,
                                                new PasMessageParam(),
                                                clue.getId(),
                                                AwardRelationType.Clud.code()
                                        );
                                    })
                                    .forEach(clue -> {
                                        clue.setOrderNo(req.orderNo);
                                        clue.setCommodityName(req.commodityName);
                                        clue.setOrderAmount(req.orderAmount);
                                        clue.setReceived(true);
                                        clue.setModifyTime(now);
                                        clueMapper.updateByPrimaryKeySelective(clue);
                                    });
                        }
                );
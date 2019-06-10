// 手把手教你写生产环境 OOM 代码，具体分析请看这个：https://juejin.im/post/5ce9067df265da1bc4142f18
public Table<CustomerAccountTitle, Assistant, List<SubsidiaryLedgerRow>> queryMoreAssistantSubsidiaryLedger(AssistantSubsidiaryLedgerQueryParam queryParam) {
    
    Table<CustomerAccountTitle, Assistant, List<SubsidiaryLedgerRow>> resultTable = HashBasedTable.create();
    // 这个 list 有 2w 条数据
    for (Assistant assistant : assistants) {
        
        //这个 list 从数据库拿，4w 条数据，数据库交互，结合外面的看，2w * 4w，然后加上基础方法 baseQueryAssistantSubsidiaryLedgerRowsMap 的循环，线上直接 OOM 了
        List<AssistantTitleBalance> balances = balanceService.selectFromYear(customerId, accountSetId, PeriodUtils.getYear(fixedBeginPeriod));
        Map<CustomerAccountTitle, List<SubsidiaryLedgerRow>> rowMap = baseQueryAssistantSubsidiaryLedgerRowsMap(
                titles,
                AssistantType.fromCode(queryParam.getAssistantType()),
                assistant.getId(),
                fixedBeginPeriod,
                endPeriod,
                null,
                balances);
        for (Map.Entry<CustomerAccountTitle, List<SubsidiaryLedgerRow>> temp : rowMap.entrySet()) {
            resultTable.put(temp.getKey(), assistant, temp.getValue());
        }
    }
    return resultTable;
}

// 这是个基础方法
private Map<CustomerAccountTitle, List<SubsidiaryLedgerRow>> baseQueryAssistantSubsidiaryLedgerRowsMap(
        List<CustomerAccountTitle> titles,
        AssistantType assistantType,
        Integer assistantId,
        String beginPeriod,
        String endPeriod,
        BookType ledgerType,
        List<AssistantTitleBalance> balances) {
    Map<CustomerAccountTitle, List<SubsidiaryLedgerRow>> resultMap = Maps.newHashMap();

    List<String> titleCodes = Lists.newArrayList(Lists.transform(titles, new Function<CustomerAccountTitle, String>() {
        @Override
        public String apply(CustomerAccountTitle input) {
            return input.getCode();
        }
    }));

    for (AssistantTitleBalance balance : balances) {
        for (String titleCode : titleCodes) {
            if (!balance.getAccountTitleCode().startsWith(titleCode)) {
                continue;
            }
            List<Balance> tempBalances = balanceTable.get(titleCode, year);
            if (tempBalances == null) {
                tempBalances = new ArrayList();
                balanceTable.put(titleCode, year, tempBalances);
            }
            tempBalances.add(balance);
        }
    }
    Table<String, String, List<AccountEntry>> entryTable = HashBasedTable.create();
    // 这里就就恐怖了，每次查询有 2w 条数据，数据库交互
    Map<Integer, AccountDocument> documentMap = documentService.selectFromYear(beginPeriod);
    for (AccountDocument document : documentMap.values()) {
        //  内层遍历，每次 10 +
        for (AccountEntry entry : document.getEntryList()) {
            // 再遍历，最外层有 200+
            for (String titleCode : titleCodes) {
                if (!entry.isUseAssistant() || AssistantType.fromCode(entry.getAssistantType()) != assistantType || !assistantId.equals(entry.getAssistantId()) || !entry.getAccountTitleCode().startsWith(titleCode)) {
                    continue;
                }
                List<AccountEntry> entries = entryTable.get(titleCode, document.getAccountPeriod());
                if (entries == null) {
                    entries = new ArrayList();
                    entryTable.put(titleCode, document.getAccountPeriod(), entries);
                }
                entries.add(entry);
            }
        }
    }
    List<String> periods = PeriodUtils.getPeriodsUpTo(beginPeriod, endPeriod);
    for (CustomerAccountTitle title : titles) {
        if (ledgerType != null && AccountTitleUtils.getBookType(title) == ledgerType) {
            resultMap.put(title, buildSubsidiaryLedgerRows(periods, title, balanceTable.row(title.getCode()), entryTable.row(title.getCode()), documentMap, ledgerType, PeriodUtils.getIntegerYear(beginPeriod)));
        } else if (ledgerType == null) {
            resultMap.put(title, buildSubsidiaryLedgerRows(periods, title, balanceTable.row(title.getCode()), entryTable.row(title.getCode()), documentMap, AccountTitleUtils.getBookType(title), PeriodUtils.getIntegerYear(beginPeriod)));
        }
    }
    return resultMap;
}
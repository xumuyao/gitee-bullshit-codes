/**
     * 锁表 
     * @param workbook 
     * @param sheetIndex 
     * @param plusClass 
     * @throws Exception 
     */
    public static void lockSheet(Workbook workbook, int sheetIndex, Class<? extends ExcelPlus> plusClass,String password) throws Exception {

        // final short STRING_FORMAT = (short) BuiltinFormats.getBuiltinFormat("TEXT");
        // final Sheet sheet0 = workbook.getSheetAt(sheetIndex);
        // sheet0.protectSheet(password);
        // final CellStyle lockCellStyle = workbook.createCellStyle();
        // lockCellStyle.setAlignment(HorizontalAlignment.CENTER);
        // lockCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // lockCellStyle.setDataFormat(STRING_FORMAT);
        // lockCellStyle.setLocked(true);
        // final CellStyle unlockCellStyle = workbook.createCellStyle();
        // unlockCellStyle.setAlignment(HorizontalAlignment.CENTER);
        // unlockCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // unlockCellStyle.setDataFormat(STRING_FORMAT);
        // unlockCellStyle.setLocked(false);


        // final Field[] fields = plusClass.getDeclaredFields();
        // ExportParams entity = new ExportParams();
        // List<PlusExcelExportEntity> excelParams = new ArrayList<PlusExcelExportEntity>();
        // PlusExportCommonService plusExportCommonService = new PlusExportCommonService();
        // ExcelTarget etarget = plusClass.getAnnotation(ExcelTarget.class);
        // String targetId = etarget == null ? null : etarget.value();
        // plusExportCommonService.getAllExcelFieldPlus(entity.getExclusions(), targetId, fields, excelParams, plusClass,
        //         null, null);
        // //排序
        // plusExportCommonService.sortAllParamsPlus(excelParams);
        // final int lastRowNum = sheet0.getLastRowNum()+1;
        // final ExcelPlus excelPlus = plusClass.newInstance();
        // final int dataStartRowIndex = (Integer)plusClass.getMethod("dataStartRowIndex").invoke(excelPlus);
        // for (int i = 0; i < excelParams.size(); i++) {

        //     if(excelParams.get(i).isLock()){

        //         for (int i1 = dataStartRowIndex; i1 < lastRowNum; i1++) {
                    // final CellStyle cellStyle = sheet0.getRow(i1).getCell(i).getCellStyle();
                    // cellStyle.setLocked(true);
                    
                    //坑爹的api，得到原本的cellStyle直接setLocked无效，必须得重新setCellStyle
                    sheet0.getRow(i1).getCell(i).setCellStyle(lockCellStyle);
        //         }
        //     }else{
        //         for (int i1 = dataStartRowIndex; i1 < lastRowNum; i1++) {
                    //final CellStyle cellStyle = sheet0.getRow(i1).getCell(i).getCellStyle();
                    // cellStyle.setLocked(false);
                    sheet0.getRow(i1).getCell(i).setCellStyle(unlockCellStyle);
        //         }
        //     }
        // }
    }
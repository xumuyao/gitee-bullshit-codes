package service.pay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import service.BaseService;
import service.pay.ChannelSubMerPoolConfig;
import utils.Dao;

/**
 * Created by 80964 on 2018/10/11.
 */
@Service
class ChannleSubMerPoolBANKService extends BaseService{
    private static final Logger log = LoggerFactory.getLogger(ChannleSubMerPoolBANKService.class);
    private String index0= "index0";
    private String index1= "index1";
    private String index2= "index2";
    private String index3= "index3";
    private String index4= "index4";
    private String index5= "index5";
    private String index6= "index6";
    private String index7= "index7";
    private String index8= "index8";
    private String index9= "index9";
    private String index10= "index10";
    private String index11= "index11";
    private String index12= "index12";
    private String index13= "index12";
    private String index14= "index14";
    private String index15= "index15";
    private String index16= "index16";
    private String index17= "index17";
    private String index18= "index18";
    private String index19= "index19";
    private String index20= "index20";
    private String indexDef= "indexDef";
    private String IndexPri= "IndexPri";

    public Map<String,Object> findFastPool(params){
        int Index = 1;
        synchronized (IndexPri){
            if(ChannelSubMerPoolConfig.BANKINDEX == 21){
                ChannelSubMerPoolConfig.BANKINDEX = 0;
            }
            Index = ChannelSubMerPoolConfig.BANKINDEX;
            ChannelSubMerPoolConfig.BANKINDEX = ChannelSubMerPoolConfig.BANKINDEX + 1;
        }
        Map<String,Object> entity = null;
        switch (Index){
            case 0: entity = findFastPool0(params); break;
            case 1: entity = findFastPool1(params); break;
            case 2: entity = findFastPool2(params);break;
            case 3: entity = findFastPool3(params);break;
            case 4: entity = findFastPool4(params);break;
            case 5: entity = findFastPool5(params);break;
            case 6: entity = findFastPool6(params);break;
            case 7: entity = findFastPool7(params);break;
            case 8: entity = findFastPool8(params);break;
            case 9: entity = findFastPool9(params);break;
            case 10: entity = findFastPool10(params);break;
            case 11: entity = findFastPool11(params);break;
            case 12: entity = findFastPool12(params);break;
            case 13: entity = findFastPool13(params);break;
            case 14: entity = findFastPool14(params);break;
            case 15: entity = findFastPool15(params);break;
            case 16: entity = findFastPool16(params);break;
            case 17: entity = findFastPool17(params);break;
            case 18: entity = findFastPool18(params);break;
            case 19: entity = findFastPool19(params);break;
            case 20: entity = findFastPool20(params);break;
            default: entity = findFastPoolDef(params);break;
        }
        return entity;
    }

    public Map<String,Object> findFastPoolDef(params){
        synchronized(indexDef){
            Map<String,Object> acqSubMer = findAndUpdata(params,"0");
            return acqSubMer;
        }
    }
    public Map<String,Object> findFastPool0(params){
        synchronized(index0){
            Map<String,Object> acqSubMer = findAndUpdata(params,"0");
            return acqSubMer;
        }
    }

    public Map<String,Object> findFastPool1(params){
        synchronized(index1){
            Map<String,Object> acqSubMer = findAndUpdata(params,"5");
            return acqSubMer;
        }
    }

    public Map<String,Object> findFastPool2(params){
        synchronized(index2){
            Map<String,Object> acqSubMer = findAndUpdata(params,"10");
            return acqSubMer;
        }
    }

    public Map<String,Object> findFastPool3(params){
        synchronized(index3){
            Map<String,Object> acqSubMer = findAndUpdata(params,"15");
            return acqSubMer;
        }
    }

    public Map<String,Object> findFastPool4(params){
        synchronized(index4){
            Map<String,Object> acqSubMer = findAndUpdata(params,"20");
            return acqSubMer;
        }
    }

    public Map<String,Object> findFastPool5(params){
        synchronized(index5){
            Map<String,Object> acqSubMer = findAndUpdata(params,"25");
            return acqSubMer;
        }
    }
    public Map<String,Object> findFastPool6(params){
        synchronized(index6){
            Map<String,Object> acqSubMer = findAndUpdata(params,"30");
            return acqSubMer;
        }
    }
    public Map<String,Object> findFastPool7(params){
        synchronized(index7){
            Map<String,Object> acqSubMer = findAndUpdata(params,"35");
            return acqSubMer;
        }
    }
    public Map<String,Object> findFastPool8(params){
        synchronized(index8){
            Map<String,Object> acqSubMer = findAndUpdata(params,"40");
            return acqSubMer;
        }
    }
    public Map<String,Object> findFastPool9(params){
        synchronized(index9){
            Map<String,Object> acqSubMer = findAndUpdata(params,"45");
            return acqSubMer;
        }
    }
    public Map<String,Object> findFastPool10(params){
        synchronized(index10){
            Map<String,Object> acqSubMer = findAndUpdata(params,"50");
            return acqSubMer;
        }
    }

    public Map<String,Object> findFastPool11(params){
        synchronized(index11){
            Map<String,Object> acqSubMer = findAndUpdata(params,"55");
            return acqSubMer;
        }
    }

    public Map<String,Object> findFastPool12(params){
        synchronized(index12){
            Map<String,Object> acqSubMer = findAndUpdata(params,"60");
            return acqSubMer;
        }
    }

    public Map<String,Object> findFastPool13(params){
        synchronized(index13){
            Map<String,Object> acqSubMer = findAndUpdata(params,"65");
            return acqSubMer;
        }
    }

    public Map<String,Object> findFastPool14(params){
        synchronized(index14){
            Map<String,Object> acqSubMer = findAndUpdata(params,"70");
            return acqSubMer;
        }
    }

    public Map<String,Object> findFastPool15(params){
        synchronized(index15){
            Map<String,Object> acqSubMer = findAndUpdata(params,"75");
            return acqSubMer;
        }
    }
    public Map<String,Object> findFastPool16(params){
        synchronized(index16){
            Map<String,Object> acqSubMer = findAndUpdata(params,"80");
            return acqSubMer;
        }
    }
    public Map<String,Object> findFastPool17(params){
        synchronized(index17){
            Map<String,Object> acqSubMer = findAndUpdata(params,"85");
            return acqSubMer;
        }
    }
    public Map<String,Object> findFastPool18(params){
        synchronized(index18){
            Map<String,Object> acqSubMer = findAndUpdata(params,"90");
            return acqSubMer;
        }
    }
    public Map<String,Object> findFastPool19(params){
        synchronized(index19){
            Map<String,Object> acqSubMer = findAndUpdata(params,"95");
            return acqSubMer;
        }
    }
    public Map<String,Object> findFastPool20(params){
        synchronized(index20){
            Map<String,Object> acqSubMer = findAndUpdata(params,"100");
            return acqSubMer;
        }
    }

    public Map<String,Object> findAndUpdata(params,indxe){
        String sql = "select ID,MER_NO,ORG_MER_NO,MER_NAME,BODY,STORE_NO from channel_sub_merchant_pool_bank where AMOUNT_LEVEL = '"+params.get("AMOUNT_LEVEL")+"' ";
        if (params.TIME_TYPE && params.TIME_TYPE != "1"){
            sql += " and TIME_TYPE = '${params.TIME_TYPE}' ";
        }
        sql += " and POOL_MER_TYPE = '"+params.get("POOL_MER_TYPE")+"' and IS_OPEN = '1' and MER_STATUS = 'normal' ";
        sql += " and CHANNEL_CODE = '"+params.get("CHANNEL_CODE")+"' ORDER BY CONUT LIMIT "+indxe+",1";
        Map<String,Object> acqSubMer = Dao.db.rows(sql).get(0);
        upDataConutUp(acqSubMer);
        return acqSubMer;
    }

    /**
     * 计数 + 1
     * @param entity
     */
    private int upDataConutUp(entity){
        def id = entity.get("ID");
        def sql = "UPDATE channel_sub_merchant_pool_bank set CONUT = CONUT+1 ,UPDATE_TIME = now() where ID = "+id;
        dao.db.execute(sql.toString());
    }
}

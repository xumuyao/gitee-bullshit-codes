<?php
namespace store;
/*入职第一天 老板说先优化下网站卡顿, 然后发现了它*/
class Store
{
    public function info($store_id){

        $store_info = null;

        $stores = Db::name('stores')->select();/* 2w + */

        foreach ($stores as $skey => $store) {
            if( $store_id == $store['store_id'] ){
                $store_info = $store;
            }
        }

        return $store_info;
    }

}

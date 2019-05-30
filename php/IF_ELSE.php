<?php
/*
    写个switch会死么
    要起飞么
*/
if (!empty($_GPC["attribute"])) {
    if ($_GPC["attribute"] == "new") {
        $condition .= " AND `isnew`=1 ";
    } else {
        if ($_GPC["attribute"] == "hot") {
            $condition .= " AND `ishot`=1 ";
        } else {
            if ($_GPC["attribute"] == "recommand") {
                $condition .= " AND `isrecommand`=1 ";
            } else {
                if ($_GPC["attribute"] == "discount") {
                    $condition .= " AND `isdiscount`=1 ";
                } else {
                    if ($_GPC["attribute"] == "time") {
                        $condition .= " AND `istime`=1 ";
                    } else {
                        if ($_GPC["attribute"] == "sendfree") {
                            $condition .= " AND `issendfree`=1 ";
                        } else {
                            if ($_GPC["attribute"] == "nodiscount") {
                                $condition .= " AND `isdiscount`=1 ";
                            } else {
                                if ($_GPC["attribute"] == "discount2") {
                                    $condition .= " AND `isdiscount`=1 ";
                                } else {
                                    if ($_GPC["attribute"] == "discount3") {
                                        $condition .= " AND `isdiscount`=0 ";
                                    } else {
                                        if ($_GPC["attribute"] == "discount4") {
                                            $condition .= " AND `isdiscount`=0 ";
                                        } 
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
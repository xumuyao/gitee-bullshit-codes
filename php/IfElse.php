if ($rearr['udid'] != '' && $cpid != 207) {
    $local_json = $this->request_get('https://www.aaaaaa.com/a?app_id=' . $rearr['appid'] . '&udid=' . $rearr['udid'] . '&idfa=' . $rearr['idfa']);
    $local_ar = json_decode($local_json, true);
    if ($local_ar['code'] == 1 && $local_ar['install'] == 1) {
        $this->db->insert('aso_' . $this->date . '_log', array('cpid' => $rearr['cpid'], 'appid' => $rearr['appid'], 'adid' => $rearr['adid'], 'idfa' => $rearr['idfa'], 'json' => $local_json, 'date' => time(), 'udid' => $rearr['udid'], 'status' => 0));
        echo json_encode(array($rearr['idfa'] => '0'));
        die;
    }
} elseif ($rearr['udid'] == '' && $cpid != 207) {
    $local_json = $this->request_get('https://www.aaaa.com/a?app_id=' . $rearr['appid'] . '&idfa=' . $rearr['idfa']);
    $local_ar = json_decode($local_json, true);
    if ($local_ar['code'] == 1 && $local_ar['install'] == 1) {
        $this->db->insert('aso_' . $this->date . '_log', array('cpid' => $rearr['cpid'], 'appid' => $rearr['appid'], 'adid' => $rearr['adid'], 'idfa' => $rearr['idfa'], 'json' => $local_json, 'date' => time(), 'udid' => $rearr['udid'], 'status' => 0));
        echo json_encode(array($rearr['idfa'] => '0'));
        die;
    }
}
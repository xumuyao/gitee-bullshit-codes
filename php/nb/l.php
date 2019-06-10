<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Esslicense extends CI_Controller {

    private function build_pagination($url,$total,$per_page,$uri_segment) {
        $pgcfg = array(
            'base_url'=>base_url($url),
            'total_rows'=>$total,
            'per_page'=>$per_page,
            'uri_segment'=>$uri_segment,
            'use_page_numbers'=>TRUE,
            'num_links'=>5,
        );
        $this->load->library('customsizepagination');
        $this->customsizepagination->initialize($pgcfg);
        return $this->customsizepagination->create_links();
    }

    private function success($data = '', $status = 200, $msg = 'success') {
        $result = [
            'data' => $data,
            'status' => $status,
            'msg' => $msg
        ];
        header ( 'Content-Type:application/json; charset=utf-8' );
        echo json_encode ( $result );
        exit(0);
    }

    private function error($status = 404, $msg = '') {
        $result = [
            'status' => $status,
            'msg' => $msg,
            'data' => null
        ];
        header ( 'Content-Type:application/json; charset=utf-8' );
        echo json_encode ( $result );
        exit(0);
    }

    private function send_mail($data) {
        $this->load->library(array('email', 'parser'));

        $this->email->from('noreply@huorong.cn', 'Huorong Security');
        $this->email->to($data['email']);

        $this->email->subject('[xxxxxx管理系统] -产品授权');
        $this->email->message($this->parser->parse('mail_lic_info', $data, TRUE));

        return $this->email->send(FALSE);
    }

    private function send_sms ( $phone ) {
        $params = array(
            'SignName' => 'xxxx',
            'PhoneNumbers' => $phone,
            'TemplateCode' => 'SMS_123456789',
            'TemplateParam' => array('phone' => '123456789')
        );

        $this->load->library('aliyunsms');
        return $this->aliyunsms->sendSMS($params);
    }

    public function index() {
        if (!$this->session->is_login) {
            redirect(array('essmgr','index'));
        }
        redirect(array('esslicense','manage'));
    }

    private function fill_lic_dict(&$row) {
    	global $license,$licensed,$regsrcs;

    	if ($row['lictype'] === '0'){
    	    $row['nowlictype'] = $licensed[1];
    	}else{
    	    $row['nowlictype'] = $licensed[2];
    	}
    	if (array_key_exists($row['lictype'], $license)) {
    		$row['vlictype']=$license[$row['lictype']];
    	}

        if (array_key_exists($row['regsrc'], $regsrcs)) {
            $row['regsrc']=$regsrcs[$row['regsrc']];
        }
    	foreach($row as $k=>$v) {
          if ($v==='0000-00-00 00:00:00' || $v==='1000-01-01 00:00:00' || $v==='1970-01-02 00:00:00' || $v==='0') {
              $row[$k]='-';
          } else {
              $row[$k]=html_escape($v);
          }
      }
    }

    private function _get_filter_condition($where = array())  {

        if ($this->session->has_userdata('flt_license')){
            if (array_key_exists('onlyyou', $this->session->flt_license)) {
                $where['uid'] = $this->session->userinfo['id'];
            }

            if (array_key_exists('fltcls', $this->session->flt_license) && array_key_exists('flttxt',$this->session->flt_license) && !empty($this->session->flt_license['flttxt'])){
                $this->db->like('c.'.$this->session->flt_license['fltcls'], $this->session->flt_license['flttxt']);
            }
        }

        $this->load->helper('cookie');
        $lictype_param1 = get_cookie('lictype');
        $lictype_param2 = get_cookie('licensed');

        if ($lictype_param2 == 0){
            if ($lictype_param1 != 3){
                $where['c.lictype'] = $lictype_param1;
            }
        }else if ($lictype_param2 == 1){
            if ($lictype_param1 != 1 && $lictype_param1 != 2){
                $where['c.lictype'] = 0;
            }
        }else if ($lictype_param2 == 2){
            if ($lictype_param1 !== '0' && $lictype_param1 != 3){
                $where['c.lictype'] = $lictype_param1;
            }else if ($lictype_param1 == 3){
                $where['c.lictype != '] = 0;
            }
        }

        if ( ! isset($where['c.lictype'])){
            unset($where['c.lictype']);
        }

        return $where;
    }

    private function display_manage($state, $where, $pgno,$perpage, $isDesc = false) {

        if ($this->input->method()==='post') {
            $this->session->flt_license = $this->input->post();
        }

        $where = $this->_get_filter_condition($where);

        $where['c.state'] = 2;

        if($pgno == 1 || $pgno == 0){
            $start_id  = 0;
        }else{
            $start_id = $perpage*($pgno-1);
        }

        $nav = $this->load->view('ess_manage/license_nav',array('state'=>$state,'flt'=>$this->session->flt_license),TRUE);

        $rows = $this->db->select(array('SQL_CALC_FOUND_ROWS c.*, u.nick'), false)->from('clients as c')->join('user as u','c.uid = u.id', 'left')->where( $where);

        $rows = $isDesc ? $rows->order_by('c.id', 'desc')->limit( $perpage, $start_id)->get() : $rows->order_by('c.id', 'asc')->limit( $perpage, $start_id)->get() ;

        $total = $this->db->query('SELECT FOUND_ROWS() AS `Count`')->row()->Count;

        $pgvw=$this->build_pagination(array('esslicense','manage'), $total, $perpage, 3);

        $results = array();

        while ($row = $rows->unbuffered_row('array')) {
            $this->fill_lic_dict($row);
            $results[] = $row;
        }

        $this->load->view('ess_manage/license_header', array('flt_license' => $this->session->flt_license, 'pgno' => $pgno));
        $this->load->view('essmgr_main_nav');
        $this->load->view('ess_manage/license_'.$state, array('clients'=>$results,'pgvw'=>$pgvw,'total'=>$total,'manage_nav'=>$nav, 'per_page' => $perpage));
        $this->load->view('ess_manage/footer');
    }

    public function manage($pgno=0,$pagesize=DEFAULT_PAGE_NUMBER) {
        if (!$this->session->is_login) {
            redirect(array('essmgr','index'));
        }
        $this->display_manage('all', array(), $pgno,$pagesize, true);
    }

    public function update_pg($id=0) {
        if (!$this->session->is_login) {
            redirect(array('essmgr','index'));
        }

        $row = $this->db->query("SELECT * FROM `clients` WHERE id=?",array($id))->unbuffered_row('array');

        if ($row === NULL) {
            show_404();
        }

        $row['lic_tk'] = $this->encryption->encrypt(json_encode($row));

        $this->parser->parse('ess_manage/template/license_update',$row);
    }

    private function build_license($clntid) {

        $data = $this->db->select("name,licnodes,licexpire,lictm,serial")->get_where('clients', array('id'=>$clntid))->unbuffered_row('array');

    	$arr = array(
            'to' => $data['name'],
            'type' => 1,
            'nodes_num' => intval($data['licnodes']),
            'expire_time' => strtotime($data['licexpire']),
            'license_time' => strtotime($data['lictm']),
            'serial' => $data['serial'],
        );

	    $privatekey = openssl_pkey_get_private(file_get_contents('ess.key'), "");
	    $arr_json = json_encode($arr);
        openssl_sign($arr_json, $signature, $privatekey, OPENSSL_ALGO_SHA512);
        return strtr(base64_encode($signature) . '.' . base64_encode($arr_json), '+/', '-_');
    }

    //生成许可证
    public function update_update() {
        // var_dump(json_decode($this->decrypt_data(rawurldecode($this->input->post('token')))));die;
        $repass = $this->input->post('repass');
        if(isset($_REQUEST['source'])){
           if($this->input->post('source') == 'preseller'){

                $token = json_decode($this->decrypt_data(rawurldecode($this->input->post('token'))),TRUE);

                $uid = $this->input->post('uid');
                $email = $token['email'];
                if($token == 'error' || $token == ""){
                    echo false;
                }
                $lictype = '2';
                $token['lictype'] = '2';

            }else{
                if (!$this->session->is_login) {
                    redirect(array('essmgr','index'));
                }

                if ($this->input->method() !== 'post') {
                    exit();
                }
                $uid = $this->session->userinfo['id'];
                $email = trim( $this->input->post('email'));
                $token = json_decode($this->encryption->decrypt($this->input->post('token')),TRUE);
                $lictype = $this->input->post('lictype');
            }
        }else{
            if (!$this->session->is_login) {
                redirect(array('essmgr','index'));
            }

            if ($this->input->method() !== 'post') {
                exit();
            }
            $uid = $this->session->userinfo['id'];
            $email = trim( $this->input->post('email'));
            $token = json_decode($this->encryption->decrypt($this->input->post('token')),TRUE);
            $lictype = $this->input->post('lictype');
        }

        $clnt = array(
            'name'=>$this->input->post('name'),
            'lictm'=>$this->input->post('tm'),
            'lictype'=>$lictype,
            'licexpire'=>$this->input->post('expire'),
            'licnodes'=>$this->input->post('nodes'),
            'licdays'=>$this->input->post('days'),
            'uid'=>$uid
        );
        //如果代理商传过来的话 自动为正式版
        if(isset($_REQUEST['source'])){
            $clnt['state'] = 2;
        }

        if (empty($email)) {
            $email = $token['email'];
        }

        $log = array(
            'optm' => date("Y-m-d H:i:s", time()),
            'cid' => $token['id'],
            'name' => $clnt['name'],
            'email' => $email,
            'uid' => $uid,
            'state' => $token['state'],
            'days' => $clnt['licdays'],
            'nodes' => $clnt['licnodes'],
            'memo' => ''
        );

        $this->log_record('更新', $log);

        if (!$this->db->update('clients',$clnt, array('id'=>$token['id']))){
            show_error($this->db->error());
        }

        $clnt['id'] = $token['id'];

        $this->log_record('生成xxx', $log);
        if(data)
        $clnt['data'] = $this->build_license($token['id']);

        if ( isset($repass) && $repass == 1) {
            $passwd = $this->randomPassword();
            $clnt['passwd'] = sha1($passwd);
        }else if ($token['lictype'] != '1' && $token['lictype'] != '2' ){
            //根据此客户是否已授权，没有 则 第一次 需要 生成密码； 是 则 不再生成密码
            $passwd = $this->randomPassword();
            $clnt['passwd'] = sha1($passwd);
        }

        $this->http_update($clnt);

        if ( isset($repass) && $repass == 1) {
            $clnt['passwd'] = $passwd;
            $clnt['serial'] = $this->input->post('serial');
        }else if ($token['lictype'] != '1' && $token['lictype'] != '2' ){
            $clnt['passwd'] = $passwd;
            $clnt['serial'] = $this->input->post('serial');
        }

        if (!empty($email)) {
            $clnt['email'] = $email;
        }

        if (!$this->send_mail($clnt)){
            log_message('error', 'SendMail: "'.$clnt['name'].'","'.$email.'", fail,"'.$this->email->print_debugger(array('headers')).'"');
            $this->error(500, $this->email->print_debugger(array('headers')));
        }

        if (!$this->send_sms($token['phone'])){
            $this->error(401, 'system error');
        }
        if(!isset($_REQUEST['source'])){
            redirect($_SERVER['HTTP_REFERER']);
        }else{
            echo true;
        }
    }

    private function randomPassword() {
        $alphabet = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890';
        $pass = array();
        $alphaLength = strlen($alphabet) - 1;
        for ($i = 0; $i < 8; $i++) {
            $pass[] = $alphabet[mt_rand(0, $alphaLength)];
        }
        return implode($pass);
    }

    private function log_record($action, $data) {
        $data['action'] = $action;
        if(!$this->db->insert('record',$data)){
            show_error($this->db->error());
        }
    }
      private function decrypt_data($ciphertext){
                    $key = hex2bin('1d1a0dd573732cf5090738e1935138b3f12604ac6ba1fa127919c7cc85b0179f44cf13f78257261fba6de51a92e2ca29ee76afe3990e3c7cd5b716bd2721d851');
                    $c = base64_decode($ciphertext);
                    $ivlen = openssl_cipher_iv_length($cipher="AES-128-CBC");
                    $iv = substr($c, 0, $ivlen);
                    $hmac = substr($c, $ivlen, $sha2len=32);
                    $ciphertext_raw = substr($c, $ivlen+$sha2len);
                    $original_plaintext = openssl_decrypt($ciphertext_raw, $cipher, $key, $options=OPENSSL_RAW_DATA, $iv);
                    $calcmac = hash_hmac('sha256', $ciphertext_raw, $key, $as_binary=true);
                    if (hash_equals($hmac, $calcmac))//PHP 5.6+ timing attack safe comparison
                    {
                        return $original_plaintext;
                    }
                    return "error";
            }
    private function http_update($data) {
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, "http://".ESSMGR_FRONTEND_IP."/essmgr/esssync/update");
        curl_setopt($ch, CURLOPT_HTTPHEADER, array("Host: ".ESSMGR_FRONTEND_HOST));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HEADER, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $this->encryption->encrypt(json_encode($data)));
        $output = curl_exec($ch);
        curl_close($ch);
        return $output;
    }
    private function fill_client_dict(&$row) {
        global $nodescnts,$industrys,$provinces,$regsrcs,$states,$license;
        if (isset($row['nodes']) && array_key_exists($row['nodes'], $nodescnts)) {
            $row['vnodes'] = $nodescnts[$row['nodes']];
        }
        if (isset($row['trade']) && array_key_exists($row['trade'], $industrys)) {
            $row['vtrade'] = $industrys[$row['trade']];
        }
        if (isset($row['province']) && array_key_exists($row['province'], $provinces)) {
            $row['vprovince'] = $provinces[$row['province']];
        }
        if (isset($row['regsrc']) && array_key_exists($row['regsrc'], $regsrcs)) {
            $row['vregsrc'] = $regsrcs[$row['regsrc']];
        }
        if (isset($row['state']) && array_key_exists($row['state'], $states)) {
            $row['vstate'] = $states[$row['state']];
        }
        if (isset($row['lictype']) && array_key_exists($row['lictype'], $license)) {
            $row['vlictype'] = $license[$row['lictype']];
        }
        $row['nickname'] = $this->get_nickname($row['uid']);
        foreach($row as $k=>$v) {
            if ($v==='0000-00-00 00:00:00' || $v==='1000-01-01 00:00:00' || $v==='1970-01-02 00:00:00' || $v==='0') {
                $row[$k]='-';
            } else {
                $row[$k]=html_escape($v);
            }
        }
    }

    private function get_nickname($id) {
        $row = $this->db->select('nick')->where('id',$id)->get('user')->unbuffered_row('array');
        if ($row === NULL) {
            return '无';
        }
        return $row['nick'];
    }

    private function fill_record_dict(&$row) {
        global $states;
        if (array_key_exists($row['state'], $states)) {
            $row['state'] = $states[$row['state']];
        }
        $row['nickname'] = $this->get_nickname($row['uid']);
        foreach($row as $k=>$v) {
            if ($v==='0000-00-00 00:00:00' || $v==='1000-01-01 00:00:00' || $v==='1970-01-02 00:00:00' || $v==='0') {
                $row[$k]='-';
            } else {
                $row[$k]=html_escape($v);
            }
        }
    }

    public function reset_passwd(){
        if (!$this->session->is_login) {
            redirect(array('essmgr','index'));
        }

        $id = $this->input->post('id');
        $new_passwd = $this->input->post('newPasswd');

        $data = $this->db->limit(1)->get_where('clients', array('id'=> $id))->unbuffered_row('array');

        $data_log = array(
            'cid'=> $data['id'],
            'name'=> $data['name'],
            'email'=> $data['email'],
            'uid'=> $this->session->userinfo['id'],
            'state'=> $data['state'],
            'days'=> $data['licdays'],
            'nodes'=> $data['licnodes'],
            'memo'=> '',
        );

        $this->log_record('修改密码', $data_log);

        $this->http_reset_client_passwd(array('id'=> $id, 'passwd'=> sha1($new_passwd)));

        $data['passwd'] = $new_passwd;

        if (!$this->send_mail($data)){
            log_message('error', 'SendMail: "'.$data['name'].'","'.$data['email'].'", fail,"'.$this->email->print_debugger(array('headers')).'"');
            $this->error(500, $this->email->print_debugger(array('headers')));
        }

        $this->success();
    }

    public function details($clntid = 0, $pgno = 0,$perpage=15){

        if (!$this->session->is_login) {
            redirect(array('essmgr','index'));
        }

        $clnt = $this->db->get_where('clients',array('id'=>$clntid),1)->unbuffered_row('array');
        if ($clnt === NULL) {
            show_404();
        }
        $this->fill_client_dict($clnt);
        if($pgno == 1 || $pgno == 0){
            $start_id  = 0;
        }else{
            $start_id = $perpage*($pgno-1);
        }
        $rows = $this->db->select(array('SQL_CALC_FOUND_ROWS *'), false)->order_by('id', 'desc')->get_where( 'record', array('cid'=>$clntid), $perpage, $start_id );
        $total = $this->db->query('SELECT FOUND_ROWS() AS `Count`')->row()->Count;
        $pgvw=$this->build_pagination(array('esslicense','details', $clntid), $total, $perpage, 4);

        $records = array();
        while ($row = $rows->unbuffered_row('array')) {
            $this->fill_record_dict($row);
            $records[] = $row;
        }
//        //添加查询条件 是否有激活
        $this->db->select(array('SQL_CALC_FOUND_ROWS *'), FALSE);
        $this->db->from('clients');
        $this->db->where(array('id'=>intval($clntid)));
        $results = $this->db->get();
        $rows=$results->unbuffered_row('array');
        if($rows['licactivitytime']=='0000-00-00' || $rows['licactivitytime']=='1000-01-01 00:00:00' || $rows['licactivitytime']=='1970-01-02' || $rows['licactivitytime']=='0'){
           $returnData =  $this->licative_curl(array("serial" => $rows['serial']));
            $returnData = trim($returnData,'"');
            if($returnData !== 'false' && preg_match ("/^([0-9]{4})-([0-9]{2})-([0-9]{2})$/", $returnData)){
                $where=array(
                    'licactivitytime'=>$returnData,
                    'licactivitytype'=>1
                );
                $this ->db->where('serial',$rows['serial'])->update('clients',$where);
                $rows['licactivitytype'] = 1;
                $rows['licactivitytime'] = $returnData;
            }
        }
        if(intval($rows['licactivitytype']) == 1){
            $activesd = '已激活';
        }else if(intval($rows['licactivitytype']) == 0){
            $activesd = '未激活';
        }else{
            $activesd = '内测激活';
        }

        $actives['type'] = $activesd;
        $actives['time']= $rows['licactivitytime'];
        $actives['types']= $rows['licactivitytype'];
        $main_nav = $this->load->view('essmgr_main_nav',array(),TRUE);
        $this->load->view('license_detail', array('clnt'=>$clnt, 'records'=> $records,'clntid'=>$clntid, 'pgvw'=>$pgvw, 'main_nav'=>$main_nav,'actives'=>$actives));
    }

    /**
     * @param $data
     * @return mixed
     */
    private function licative_curl($data) {
//        var_dump($data);die;
        $arrContextOptions=array(
            "ssl"=>array(
                "verify_peer"=>false,
                "verify_peer_name"=>false,
            ),
        );
        return file_get_contents(HRSTATS_URL."?serial=".$data["serial"],false, stream_context_create($arrContextOptions));
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, HRSTATS_URL);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array("Host: ".ESSMGR_FRONTEND_HOST));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HEADER, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
        curl_setopt($ch, CURLOPT_USERAGENT, $_SERVER['HTTP_USER_AGENT']); // 模拟用户使用的浏览器
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1); // 使用自动跳转
        curl_setopt($ch, CURLOPT_AUTOREFERER, 1); // 自动设置Referer
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
        $output = curl_exec($ch);
        curl_close($ch);
        return $output;
    }
    public function  activeSubmit(){

        $id=$_REQUEST['id'];
        $actives = $_REQUEST['actives'];
        $time = $_REQUEST['time'];
        $where=array(
            'licactivitytime'=>$time,
            'licactivitytype'=>$actives
        );
        $this ->db->where('id',$id)->update('clients',$where);
        $result = [
            'status' => 200,
            'msg' => 'success'
        ];
        header ( 'Content-Type:application/json; charset=utf-8' );
        echo json_encode ( $result );
    }
    private function fill_title_dict($title_en_arr){
        global $title_cn;

        $title_cn_arr = array();

        foreach ($title_en_arr as $key =>$value) {
            $v = trim($value,'"');
            if (array_key_exists($v, $title_cn)){
                $title_cn_arr[$key] = $title_cn[$v];
            }
        }
        return $title_cn_arr;
    }

    private function fill_content_dict(&$arr)  {
        global $nodescnts,$industrys,$provinces,$regsrcs,$states,$license;

        $arr[2] = $this->check($regsrcs,$arr,2);
        $arr[4] = $this->check($provinces,$arr,4);
        $arr[5] = $this->check($industrys,$arr,5);
        $arr[6] = $this->check($nodescnts,$arr,6);
        if (trim($arr[10], '"') == '1'){
            $arr[10] = '已激活';
        }else{
            $arr[10] = '未激活';
        }
        $arr[13] = $states[trim($arr[13],'"')];
        $arr[16] = $license[trim($arr[16],'"')];

        foreach($arr as $k=>$v) {
            $v = trim($v, '"');
            if ($v==='0000-00-00 00:00:00' || $v==='1000-01-01 00:00:00' || $v==='1970-01-02 00:00:00' || $v==='0') {
                $arr[$k]='-';
            } else {
                $arr[$k]=html_escape($v);
            }
        }
    }

    public function export2csv(){
        if (!$this->session->is_login) {
            redirect(array('essmgr','index'));
        }

        $where = $this->_get_filter_condition();

        $where['c.state'] = 2;

        $query = $this->db->select('c.*, u.nick')->from('clients as c')->join('user as u','c.uid = u.id', 'left')->where( $where) ->order_by('c.id', 'asc')->get();
        $this->load->dbutil();
        $this->load->helper('file');
        $this->load->helper('download');

        $delimiter = ",";
        $newline = "\r\n";
        $enclosure = '"';
        $data = $this->dbutil->csv_from_result($query, $delimiter, $newline, $enclosure);

        $data = $this->_pretty_data($data);

        $Date = date("YmdHis");
        $Filename = $Date.'.csv';
        force_download($Filename, $data);
    }

    private function _pretty_data ($data)  {

        $data_arr = explode("\r\n", $data);

        array_pop($data_arr);

        $title_en_arr = explode(',', $data_arr[0]);

        $title_cn_arr = $this->fill_title_dict($title_en_arr);

        $title_cn = implode(',', $title_cn_arr);

        array_shift($data_arr);
        array_unshift($data_arr, $title_cn);

        foreach ($data_arr as $key =>$value) {
            if ($key == 0){ continue;}

            $v_arr = explode(',', $data_arr[$key]);

            $this->fill_content_dict($v_arr);

            $data_arr[$key] = implode(',', $v_arr);
        }

        $data = implode("\r\n", $data_arr);

        return mb_convert_encoding($data,'gb2312','utf-8');
    }

    private function http_reset_client_passwd($data) {
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, "http://".ESSMGR_FRONTEND_IP."/essmgr/esssync/reset_passwd");
        curl_setopt($ch, CURLOPT_HTTPHEADER, array("Host: ".ESSMGR_FRONTEND_HOST));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HEADER, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $this->encryption->encrypt(json_encode($data)));
        $output = curl_exec($ch);
        curl_close($ch);
        return $output;
    }
    /*给sem提供序列号的内容
     *
     *@return array serial 返回的序列号
     * */
    public function  find_serial_curl(){

        $data = json_decode($_REQUEST);
        $return = [];
        foreach($data as$k=> $v) {
            //添加查询条件 是否有激活
            $return[$k]['serial'] = $v['serial'];
            $this->db->select(array('SQL_CALC_FOUND_ROWS *'), FALSE);
            $this->db->from('clients');
            $this->db->where(array('serial' => $v['serial']));
            $results = $this->db->get();
            $rows = $results->unbuffered_row('array');
            if ($rows !== NULL ) {
                $return[$k]['licactivitytime'] = $rows['licactivitytime'];
            } else {
                $return[$k]['licactivitytime'] = "";
            }
        }
        // 生成json格式数据
        header('Access-Control-Allow-Origin: *');
        echo json_encode($return);
    }
    /**
     * 根据原始数据数组的key获取value作为字典的key得到对应的翻译值
     * @param $ay array 字典
     * @param @arr array 原始值数组
     * @param @index string
     * @return string
     */
    private function check($ay,$arr,$index){
        $key = isset($arr[$index]) ? trim($arr[$index]) : "";
        $key = str_replace('"',"",$key);
        if(array_key_exists($key, $ay)){
            return trim($ay[$key]);
        }
            return '"';
        }


    /**
     * http_put_client 推送客户端信息到前端
     *
     * @param  mixed $data
     *
     * @return void
     */
    private function http_put_client($data) {
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, "http://".ESSMGR_FRONTEND_IP."/essmgr/esssync/put_client");
        curl_setopt($ch, CURLOPT_HTTPHEADER, array("Host: ".ESSMGR_FRONTEND_HOST));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HEADER, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $this->encryption->encrypt(json_encode($data)));
        $output = curl_exec($ch);
        curl_close($ch);
        return $output;
    }

    /**
     * _build_serial 生成序列号
     *
     * @param  mixed $province
     * @param  mixed $company_name
     *
     * @return void
     */
    private function _build_serial($province, $company_name)  {
        $serial = array(
            'ESV1',
            $province.strtoupper(str_pad(dechex(mt_rand(0,255)), 2, '0', STR_PAD_LEFT)),
            date('yW'),
            strtoupper(str_pad(dechex(crc32($company_name)), 8, '0', STR_PAD_LEFT)),
        );
        return implode('-', $serial);
    }

    /**
     * _build_lic 生成证书
     *
     * @return void
     */
    public function _build_lic($data)
    {
        $arr = array(
            'to' => $data['name'],
            'type' => 1,
            'nodes_num' => intval($data['licnodes']),
            'expire_time' => strtotime($data['licexpire']),
            'license_time' => strtotime($data['lictm']),
            'serial' => $data['serial'],
        );

	    $privatekey = openssl_pkey_get_private(file_get_contents('ess.key'), "");
	    $arr_json = json_encode($arr);
        openssl_sign($arr_json, $signature, $privatekey, OPENSSL_ALGO_SHA512);
        return strtr(base64_encode($signature) . '.' . base64_encode($arr_json), '+/', '-_');
    }

    /**
     * showFormalLic 显示正式证书添加页面
     *
     * @return void
     */
    public function showFormalLic()
    {
        if (!$this->session->is_login) {
            redirect(array('essmgr','index'));
        }
        $this->load->view('ess_manage/template/createFormalLic',array()) ;
    }

    /**
     * 判断表单数据是否为空
     *
     * @param  array $fields 判断的字段
     * @param  array $data  判断的数据
     *
     * @return array
     */
    public function isFormEmpty($fields, $data)
    {
        foreach ($fields as $key => $val) {
            if (!isset($data[$val]) || empty($data[$val])) {
                // var_dump($val, $data);
                return [true, $key];
            }
        }
        return [false, ''];
    }

    /**
     * 判断表单数据是否为默认项
     *
     * @param  array $fields 判断的字段
     * @param  array $data  判断的数据
     *
     * @return array
     */
    public function isFormDefault($fields, $data)
    {
        if(count($fields) !== 0){
            foreach ($fields as $key => $val) {
                if (!isset($data[$val]) || empty($data[$val]) || $data[$val] == 1 ) {
                    // var_dump($val, $data);
                    return [true, $key];
                }
            }
        }
        return [false, ''];
    }

    /**
     * createFormalLic 创建正式证书
     *
     * @return void
     */


    public function createFormalLic()
    {
        if (!$this->session->is_login) {
            redirect(array('essmgr','index'));
        }

        // 非表单数据生成
        $now  = date("Y-m-d H:i:s", time());
        $post = $this->input->post();
        unset($post['csrf_test_name']);
        //判断传入数据
        if(!(isset($post['type_sales']) && isset($post['type_message']))){
            $this->error(401, '字段不正确');
        }
        // 拼接字段是否为空和验证字段
        //验证不能为空的数组

        //验证是否生效的数组
        $fields_checked = [];

        if($post['type_sales'] == "agent"){
            $fields_null = [
                '授权台数' => 'agent_licnodes',
                '授权期限' => 'agent_licdays',
                '邮箱通知' => 'agent_send_email',
                '短信通知' => 'agent_send_phone'
            ];
            $fields_nulls = [
                '订单编号' => 'order_id',
                '代理商名称' => 'agent_company_name'
            ];
            $fields_null = array_merge($fields_null,$fields_nulls);
        }else{
            $fields_null = [
                '授权台数' => 'licnodes',
                '授权期限' => 'licdays',
                '邮箱通知' => 'send_email',
                '短信通知' => 'send_phone'
            ];
            $fields_nulls = [
                '客户名称' => 'company_name'
            ];
            $fields_null = array_merge($fields_null,$fields_nulls);
        }

        if($post['type_message'] == "all_mess"){
            $fields_nulls = [
                '客户名称' => 'name',
                '联系人' =>'contact',
                '联系电话' =>'contact_phone',
                '邮箱地址' =>'contact_email',
                'QQ' => 'im'
            ];
            $fields_null = array_merge($fields_null,$fields_nulls);

            $fields_checked = [
                '所在地区' => 'province',
                '所属行业' => 'trade',
                '终端规模' => 'nodes'
            ];
        }

        // 验证字段是否为空
        $vaild = $this->isFormEmpty($fields_null, $post);
        if ($vaild[0]) {
            $this->error(401, $vaild[1].'未填写');
        }


        $vaild = $this->isFormDefault($fields_checked, $post);
        if ($vaild[0]) {
            $this->error(401, $vaild[1].'未选择');
        }
//        开始拼接使用的数组
        $data = [];
        //发送信息的数据
        $contant_info = [];
        if($post['type_sales'] == "agent"){
            $contant_info = [
                'phone' =>$post['agent_send_phone'],
                'email' =>$post['agent_send_email']
            ];
            if($post['type_message'] == "all_mess"){
                $data = [
                    'name' => $post['name'],
                    'contact' =>$post['contact'],
                    'phone' =>$post['contact_phone'],
                    'email' =>$post['contact_email'],
                    'im' => $post['im'],
                    'province' => $post['province'],
                    'trade' => $post['trade'],
                    'nodes' => $post['nodes'],
                    'licnodes' => $post['agent_licnodes'],
                    'licdays' => $post['agent_licdays']
                ];
                $data['email_valid'] = 1; // 邮箱是否有效 1 有效
                $data['serial']  = $this->_build_serial($data['province'], $data['name'].$this->guid()); // 序列号
            }else{
                $data = [
                    'name' => $post['agent_company_name'],
                    'licnodes' => $post['agent_licnodes'],
                    'licdays' => $post['agent_licdays'],
                    'contact' =>'0',
                    'im' => '0',
                    'province' => '1',
                    'trade' => 1,
                    'nodes' => 1,
                ];
                $data['serial']  = $this->_build_serial("BJ", $data['name'].$this->guid()); // 序列号
                $data['phone'] = $data['serial'];
                $data['email'] = $data['serial'];
                $data['email_valid'] = 0; // 邮箱是否有效 1 有效
            }
            $data['licexpire'] = date('Y-m-d H:i:s',strtotime('+'.$post['agent_licdays'].' day'));
            $data['regsrc'] = 5; // 来源 5 代理商渠道
        }else{
            $contant_info = [
                'phone' =>$post['send_phone'],
                'email' =>$post['send_email']
            ];
            if($post['type_message'] == "all_mess"){
                $data = [
                    'name' => $post['name'],
                    'contact' =>$post['contact'],
                    'phone' =>$post['contact_phone'],
                    'email' =>$post['contact_email'],
                    'im' => $post['im'],
                    'province' => $post['province'],
                    'trade' => $post['trade'],
                    'nodes' => $post['nodes'],
                    'licnodes' => $post['licnodes'],
                    'licdays' => $post['licdays']
                ];
                $data['email_valid'] = 1; // 邮箱是否有效 1 有效
                $data['serial']  = $this->_build_serial($data['province'], $data['name'].$this->guid()); // 序列号
            }else{
                $data = [
                    'name' => $post['company_name'],
                    'licnodes' => $post['licnodes'],
                    'licdays' => $post['licdays'],
                    'contact' =>'0',
                    'im' => '0',
                    'province' => '1',
                    'trade' => 1,
                    'nodes' => 1,
                ];
                $data['serial']  = $this->_build_serial("BJ", $data['name'].$this->guid()); // 序列号
                $data['phone'] = $data['serial'];
                $data['email'] = $data['serial'];
                $data['email_valid'] = 0; // 邮箱是否有效 1 有效
            }
            $data['licexpire'] = date('Y-m-d H:i:s',strtotime('+'.$post['licdays'].' day'));
            $data['regsrc'] = 2; // 来源 2 后台添加
        }

        $data['regtm']  = $now; // 申请时间
        $data['revwtm']  = $now; // 审核时间
        $data['uptime'] = $now; // 最后找回时间
        $data['licactivitytime'] = $now; // 序列号激活时间
        $data['last_operate_time'] = $now; // 最后操作时间
        $data['lictm'] = $now; //授权时间

        $passwd = $this->randomPassword();// 密码
        $data['passwd']  = sha1($passwd); // 加密后保存

        $data['data']    = $this->_build_lic($data); // 许可证
        $data['uid'] = $this->session->userinfo['id']; // 操作用户


        $data['lictype']  = 2; // 授权类型，2 正式授权

        $data['licactivitytype'] = 0; // 序列号类型 1 已激活激活
        $data['state'] = 2; // 状态 lic 已授权


        // 删除外网不需要的字段
        $wData = $data;
        unset($wData['state']);
        unset($wData['revwtm']);
        unset($wData['lictm']);
        unset($wData['lictype']);
        unset($wData['uid']);
        unset($wData['licactivitytime']);
        unset($wData['licactivitytype']);
        unset($wData['remark']);

        // 提交到外网服务器
        $ret = $this->http_put_client($wData);
        $ret = json_decode($ret, true);
        // var_dump($ret);exit;
        if ( !isset($ret['status']) || $ret['status'] != 200){
            $this->error(401, '添加失败，请检查手机号邮箱是否重复');
        }
        if ( !isset($ret['data']['id']) || empty($ret['data']['id'])) {
            $this->error(401, '添加失败，请检查邮箱是否重复');
        }

        // 删除内网不需要的数据
        $nData = $data;
        unset($nData['passwd']);
        unset($nData['uptime']);
        unset($nData['data']);
        unset($nData['last_operate_time']);

        // 根据外网id保存本地数据
        $nData['id'] = $ret['data']['id'];
        if (! $this->db->insert('clients', $nData)){
            $this->error(500, $this->db->error());
        }



        // 发送邮件
        $data['passwd'] = $passwd;
        if (!$this->createF_send_mail($data,$contant_info['email'])){
            log_message('error', 'SendMail: "'.$data['name'].'","'.$contant_info['email'].'", fail,"'.$this->email->print_debugger(array('headers')).'"');
            $this->error(500, '邮件格式不正确，或者已存在！');
        }

        // 发送短信
        if (!$this->send_sms($contant_info['phone'])){
            $this->error(401, '手机格式不正确，或者已存在！');
        }

        if($post['type_sales'] == "agent"){
            $upAgent =[
                'order_id'=>$post['order_id'],
                'serial' => $data['serial'],
                'data' =>$data['data']
            ];
            $rst = $this->http_put_agent($upAgent);
            $rst = json_decode($rst, true);
            // 同步数据
            if ($rst['code'] != 200){

                $this->error(401, $rst['msg']);
            }
        }

        $log = array(
            'optm' => date("Y-m-d H:i:s", time()),
            'cid' => $ret['data']['id'],
            'name' => $data['name'],
            'email' => $data['email'],
            'uid' => $data['uid'],
            'state' => 2,// 状态：已授权
            'days' => $data['licdays'],
            'nodes' => $data['licnodes'],
            'memo' => ''
        );

        // 记录日志
        $this->log_record('生成许可证', $log);

        // 存放正式证书生成日志
        $licStr = "{$nData['id']},{$wData['name']},{$wData['phone']},{$wData['email']}\n";
        file_put_contents(ESSLIC_FORMAL_LIC_FILE, $licStr, FILE_APPEND);
        $this->success();
    }

    /**
     * 发送邮件
     * @param $data
     * @param $email
     * @return mixed
     */
    private function createF_send_mail($data,$email) {
        $this->load->library(array('email', 'parser'));

        $this->email->from('noreply@huorong.cn', 'Huorong Security');
        $this->email->to($email);

        $this->email->subject('[xxxxxx管理系统] -产品授权');
        $this->email->message($this->parser->parse('mail_lic_info', $data, TRUE));

        return $this->email->send(FALSE);
    }
    /**
     * 生成uuid
     */
    private function guid()
    {
        if (function_exists('com_create_guid')) {
            return com_create_guid();
        } else {
            mt_srand((double)microtime() * 10000);//optional for php 4.2.0 and up.
            $charid = strtoupper(md5(uniqid(rand(), true)));
            $hyphen = chr(45);// "-"
            $uuid = chr(123)// "{"
                . substr($charid, 0, 8) . $hyphen
                . substr($charid, 8, 4) . $hyphen
                . substr($charid, 12, 4) . $hyphen
                . substr($charid, 16, 4) . $hyphen
                . substr($charid, 20, 12)
                . chr(125);// "}"
            return $uuid;
        }
    }
    /**
     * http_put_client 推送客户端信息更新到agent系统
     *
     * @param  mixed $data
     *
     * @return void
     */
    private function http_put_agent($data) {
        $curl = curl_init();
        curl_setopt($curl, CURLOPT_URL,PUT_AGENT_URL); // 要访问的地址
        curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, FALSE); // 对认证证书来源的检查
        curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, FALSE); // 从证书中检查SSL加密算法是否存在
        curl_setopt($curl, CURLOPT_USERAGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)"); // 模拟用户使用的浏览器
        curl_setopt($curl, CURLOPT_FOLLOWLOCATION, 1); // 使用自动跳转
        curl_setopt($curl, CURLOPT_AUTOREFERER, 1); // 自动设置Referer
        curl_setopt($curl, CURLOPT_REFERER, $_SERVER['SERVER_NAME']);
        curl_setopt($curl, CURLOPT_POST, 1); // 发送一个常规的Post请求
        curl_setopt($curl, CURLOPT_POSTFIELDS, http_build_query($data)); // Post提交的数据包
        curl_setopt($curl, CURLOPT_TIMEOUT, 30); // 设置超时限制防止死循环
        curl_setopt($curl, CURLOPT_HEADER, 0); // 显示返回的Header区域内容
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1); // 获取的信息以文件流的形式返回

        $output = curl_exec($curl);
        curl_close($curl);
        return $output;
    }
}
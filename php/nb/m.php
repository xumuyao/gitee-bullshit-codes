<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Essmgr extends CI_Controller
{
    public function validate_login()
    {
        $ip =$this->input->ip_address();
        $serial=$this->input->post('serialno');
        $where['serial'] = $serial;  $where['ipaddr'] = ip2long($ip);
        $time_now = date("Y-m-d H:i:s",intval(time()));
        $data = $this->db->where($where)->get('defense_failedlogin')->result_array();

        if(!empty($data)) {
            if (intval($data['0']['times']) > 8) {
                $times = 100;
                $mess = '您账号今日被锁定，请跟客服人员联系';
                $this->error_mess_login($ip, $times, $serial, $time_now, $mess);
                return FALSE;
            }
        }

        $dataa = $this->db->where('serial', $this->input->post('serialno'))->get('clients')->unbuffered_row('array');
        if ($dataa === NULL) {
            $this->form_validation->set_message('validate_login', '序列号或密码不正确');
            return FALSE;
        }
        if( intval($dataa['loginstate']) == 0 ){
            $this->form_validation->set_message('validate_login', '序列号和密码已失效');
            return FALSE;
        }
//        if(!($dataa['licexpire'] == "1970-01-01 00:00:00" || $dataa['licexpire'] == "1000-01-01 00:00:00")) {
//            if (intval($dataa['regsrc']) == 4 && (strtotime($dataa['licexpire']) - strtotime("now") < 0)) {
//                $this->form_validation->set_message('validate_login', '序列号和密码已失效');
//                return FALSE;
//            }
//        }
        if (empty($dataa['passwd']) || $dataa['passwd'] !== sha1($this->input->post('passwd'))) {
            //查出内容如果为空的话
            if(empty($data)){
                $ip2 = ip2long($ip);
                $datas = array(
                    'ipaddr' => $ip2 ,
                    'serial' => $serial ,
                    'times' => 1,
                    'lasttm'=>$time_now
                );
                $this->db->insert('defense_failedlogin', $datas);
                $this->form_validation->set_message('validate_login', '序列号或密码错误');
                return FALSE;
            }
            //查出内容如果不为空今天有记录的话
            $today = strtotime(date('Y-m-d', time()));
            if(strtotime($data['0']['lasttm']) - $today < 86400) {
                //查出内容如果不为空今天有记录 次数少于9次
                        $wait_time = (intval($data['0']['times']))*30;
                        if(((strtotime($time_now))-(strtotime($data['0']['lasttm']))) > $wait_time ) {
                            $times = intval($data['0']['times']) + 1;
                            $wait_time = (intval($data['0']['times'])+1)*30;
                            $mess = '请在'.$wait_time.'秒之后重试';
                            $this->error_mess_login($ip,$times ,$serial,$time_now,$mess);
                            return FALSE;
                        }else{
                            //如果没有到需要等待时间
                            $this->form_validation->set_message('validate_login', '序列号或密码错误，请在'.$wait_time.'秒之后重试');
                            return FALSE;
                        }
            }
            //如果不为空今天之前有记录的话
            if(strtotime($data['0']['lasttm']) - $today > 86400){
                $times = 1;
                $mess = '序列号或密码错误';
                $this->error_mess_login($ip,$times ,$serial,$time_now,$mess);
                return FALSE;
            }
        }
        //如果账号密码正确
        $this->session->is_login = true;
        $this->session->userinfo = $dataa;
        return TRUE;
    }
    public function error_mess_login($ip,$times ,$serial,$time_now,$mess){
        $ip2 = ip2long($ip);
        $wheres = array(
            'ipaddr' =>$ip2,
            'serial' => $serial
        );
        $datas = array(
            'times' => $times,
            'lasttm' => $time_now
        );
        $this->db->where($wheres)->update('defense_failedlogin', $datas);
        $this->form_validation->set_message('validate_login', $mess);
        return FALSE;
    }
    public function validate_captcha(){
        if($this->pic_captcha($this->input->post('captcha')) == FALSE){
            $this->form_validation->set_message('validate_captcha', '验证码不正确');
            $this->form_validation->set_message('validate_login', '');
            return FALSE;
        }else{
            return TRUE;
        }
    }

    public function index()
    {
        $uvr = $this->input->post('uv_r');

        /* if (!$this->_preLogin($uvr)){
            show_error('登录次数已达上限');
        } */

        $this->load->library(array('form_validation'));

        if (($this->form_validation->run('essmgr') == FALSE)) {
            // var_dump('form');
            $header = $this->load->view('common/header', array(), TRUE);
            $left = $this->load->view('common/left', array(), TRUE);
            $footer = $this->load->view('common/footer', array(), TRUE);
            $this->load->view('ess_login', array(
                'header' => $header,
                'left' => $left,
                'footer' => $footer
            ));
        }else{
            $referer_url = $_SERVER["HTTP_REFERER"];
            if(strpos($referer_url,"index") === false){
                redirect('essticket/ticket_create/', 'location', 301);
            }else{
                redirect('essmgr/manage/', 'location', 301);
            }
        }
    }

    private function fill_ess_client_dict(&$row)
    {
        foreach ($row as $k => $v) {
            if ($v === '0000-00-00 00:00:00' || $v === '1000-01-01 00:00:00' || $v === '1970-01-02 00:00:00' || $v === '0') {
                $row[$k] = '-';
            } else {
                $row[$k] = html_escape($v);
            }
        }
    }

    public function manage()
    {
        if (!$this->session->is_login) {
            redirect('essmgr/index', 'location', 301);
        }
        $noAuth = $this->check_auth();
        $userinfo = $this->session->userinfo;

        $datas = $this->db->where('serial',$this->session->userinfo['serial'] )->get('clients')->unbuffered_row('array');
        $ori_data = array(
            'company_name'=>$datas['name'],
            'licnodes'=>$datas['licnodes']
        );
        $this->fill_ess_client_dict($userinfo);

        $header = $this->load->view('common/header', array(), TRUE);
        $left = $this->load->view('common/left', array(), TRUE);
        $footer = $this->load->view('common/footer', array(), TRUE);
        global $province,$industry;
        $dir =  array(
            "(请安装企业版后，将授权文件导入控制中心，否则无法正常使用)",
            "(请先<div class='auth_mess' style='color:red;cursor: pointer'>完善信息</div>，在下载授权文件)",
            "(邮箱尚未验证，请先<div class='auth_email' style='color:red;cursor: pointer'>验证邮箱</div>)",
            "(授权文件正在生成，请稍等...)"
        );
//        var_dump($userinfo['nodes'],$nodescnt);die;
        $man_mess = $this->load->view('ess_manage_mess', array('province'=>$province,'industry'=>$industry,'ori_data'=>$ori_data,'nodescnt_data'=>$userinfo['nodes']), TRUE);

        $this->load->view('ess_manage', array('userinfo' => $userinfo, 'header'=> $header,  'left'=>$left, 'footer'=>$footer,'noauth' => $noAuth,'noauth_remark'=>$dir[$noAuth],'man_mess'=>$man_mess));
    }

    /**
     * 检查许可证生成情况
     */
    public function check_license(){
        if (!$this->session->is_login) {
            redirect('essmgr/index');
        }
        if($this->session->userinfo['id'] == ""){
            redirect('essmgr/index');
        }
        $dataa = $this->db->where('id',$this->session->userinfo['id'] )->get('clients')->unbuffered_row('array');
        if($dataa['data'] == '0'){
            $code = 400;
        }else{
            $code = 200;
//            $this->session->sess_destroy();
            $this->session->unset_userdata('userinfo');
            $this->session->userinfo = $dataa;
        }
        echo json_encode(array('code'=>$code));
    }
    /**
     * 检查email返回情况生成情况
     */
    public function check_email_return(){
        if (!$this->session->is_login) {
            redirect('essmgr/index');
        }
        if($this->session->userinfo['id'] == ""){
            redirect('essmgr/index');
        }
        $dataa = $this->db->where('id',$this->session->userinfo['id'] )->get('clients')->unbuffered_row('array');
        if(intval($dataa['email_valid']) == 0){
            $code = 400;
        }else{
            $code = 200;
        }
        echo json_encode(array('code'=>$code));
    }
    /**
     * 验证是否是第一次登陆需要完善信息
     * @return int 1没有填入信息 2没有邮箱验证 3没有许可证 0成功
     */
    public function check_auth(){
        if($this->session->userinfo['serial'] == ""){
            return 1;
        }
        $dataa = $this->db->where('serial',$this->session->userinfo['serial'] )->get('clients')->unbuffered_row('array');
        if(intval($dataa['regsrc']) == 4 || intval($dataa['regsrc']) == 2 ) {
            if (!preg_match('/^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$/', $dataa['email'] )) {
                $noAuth = 1;//1 为 没有填入信息
            } else {
                if (intval($dataa['email_valid']) == 0) {
                    $noAuth = 2;//没有邮箱验证
                } else {
                    if ($dataa['data'] == '0') {
                        $noAuth = 3;//验证是否有许可证
                    } else {
                        $noAuth = 0;
                    }
                }
            }
        }else{
            $noAuth = 0;
        }
        return $noAuth;
    }

    public function check_email(){
        if (!$this->session->is_login) {
            redirect('essmgr/index', 'location', 301);
        }
        $dataa = $this->db->where('serial',$this->session->userinfo['serial'] )->get('clients')->unbuffered_row('array');
        if( intval($dataa['email_valid']) !== 0){
            redirect('essmgr/index', 'location', 301);
        }else{
            echo json_encode(array('code'=>200,'data'=>$dataa['email']));
        }
    }

    /**
     * 下载许可证
     */
    public function get_license() {
        if (!$this->session->is_login) {
            redirect('essmgr/index', 'location', 301);
        }


        $row = $this->db->get_where('clients',array('id'=>$this->session->userinfo['id']))->unbuffered_row('array');

        if ($row === NULL) {
            show_404();
        }
        if($this->check_auth() !== 0){
            redirect('essmgr/manage/', 'location', 301);
        }

        header('Content-Type: application/octet-stream');
        header('Content-Disposition: attachment; filename="license.dat"');
        echo $row['data'];

        exit();
    }

    public function pkg_install() {
        if (!$this->session->is_login) {
            redirect('essmgr/index', 'location', 301);
        }
        $url = $this->ess_installer("ess_online");
        redirect($url);
    }

    public function pkg_offline() {
        if (!$this->session->is_login) {
            redirect('essmgr/index', 'location', 301);
        }
        $url = $this->ess_installer("ess_offline");
        redirect($url);
    }
    /*
    *根据key获取对应的下载文件名称
    *@param string 文件对应的key
    */

    private function  ess_installer($key){
        $content = file_get_contents(ESS_VERSION_FILE);
        return INSTALLER_URL.json_decode($content,JSON_OBJECT_AS_ARRAY)[$key];
    }

    private function send_mail($data) {
        $this->load->library(array('email', 'parser'));

        $this->email->from('noreply@huorong.cn', 'Huorong Security');
        $this->email->to($data['email']);

        $this->email->subject('[xxxxxx管理系统]-产品序列号或密码找回');
        $this->email->message($this->parser->parse('mail_template_getback', array('user'=>$data['name'], 'serial'=>$data['serial'], 'pass'=>$data['passwd']), TRUE));

        return $this->email->send();
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

    public function getBackInfo(){
        $email = $this->input->post('email', TRUE);
        $data = array(
            'email' => $email,
            'header' => $this->load->view('common/header', array(), TRUE),
            'footer' => $this->load->view('common/footer', array(), TRUE)
        );
        $this->load->library('form_validation');
        if ($this->form_validation->run('get_back_info') === FALSE){

           $this->load->view('ess_get_back', $data);

        }else{
            $this->send_email_mess($email);
        }
    }
    public function send_email_mess($email){
        $data = array(
            'email' => $email,
            'header' => $this->load->view('common/header', array(), TRUE),
            'footer' => $this->load->view('common/footer', array(), TRUE)
        );
        //生成密码，根据邮箱查clntreg 中 序列号，根据 序列号 存clients 中 新密码 然后 发邮件
        $new_passwd = $this->randomPassword();

        if (! $this->db->update('clients', array('passwd' => sha1($new_passwd)), array('email' => $email))){
            log_message('error', $this->db->error());
        }

        $data_clntreg = $this->db->select('name, serial')->limit(1)->get_where('clients', array('email' => $email))->unbuffered_row('array');

        $data_email = array(
            'email' => $email,
            'passwd' => $new_passwd,
            'name' => $data_clntreg['name'],
            'serial' => $data_clntreg['serial']
        );

        if (!$this->send_mail($data_email)){
            log_message('error', '发送邮件失败 '.$this->email->print_debugger(array('headers')));
        }

        $this->load->view('ess_verify_getback', $data);;

    }
    private function _preLogin($uvr){
        $ip_num = 0;
        $uvr_num = 0;
        $ip = $this->input->ip_address();

        $ip_num = $this->_doCheck('ip_'.$ip);
        $uvr_num = $this->_doCheck('uvr_'.$uvr);

        //当前浏览器 每日登录尝试 次数，当前 ip 每日登录尝试次数
        if( $ip_num < 10 && $uvr_num < 10) {
            return TRUE;
        }
        return FALSE;
    }

    private function _toFile($filepath, $data){
        try {
            file_put_contents($filepath, $data, FILE_APPEND);

        } catch (Exception $e){
            print $e->getMessage();
        }
    }

    private function _doCheck($data){
        $root = APPPATH.'cache/';
        $fileName = $data.'_'.date("Y-m-d").'.dat';
        $filePath = $root.$fileName;
        $c_sum = 1;

        if(is_file($filePath)){
            $arr = file_get_contents($filePath);
            $row = explode('|', $arr);
            $countArr = array_count_values($row);
            $c_sum = $countArr[$data];
            if($c_sum < 10){
                $this->_toFile($filePath,$data.'|');
                $c_sum++;
            }
        }else{
            $this->_toFile($filePath,$data.'|');
        }
        return $c_sum;
    }

    public function validate_getback()  {
        $email = $this->input->post('email', TRUE);
        $pattern="/([a-z0-9]*[-_.]?[a-z0-9]+)*@([a-z0-9]*[-_]?[a-z0-9]+)+[.][a-z]{2,3}([.][a-z]{2})?/i";

        if(!preg_match($pattern,$email)){
            $this->form_validation->set_message('validate_getback', '邮箱格式不正确，请重新输入');
            return FALSE;
        }
        if ( $this->db->where('email', $email)->count_all_results('clients') === 0 ){
            $this->form_validation->set_message('validate_getback', '该邮箱尚未注册,请重新输入');
            return FALSE;
        }
//        $ip = $_SERVER['REMOTE_ADDR'];

        $ip = $this->input->ip_address();
        $time_now = date("Y-m-d H:i:s",intval(time()));
        $ip2 = ip2long($ip);
        $where['ipaddr'] = $ip2;  $where['action'] = 1;
        $data = $this->db->where($where)->get('defense_failedip')->result_array();
        //查出内容如果为空的话
        if(empty($data)){
            $ip2 = ip2long($ip);
            $datas = array(
                'ipaddr' =>$ip2 ,
                'action' => 1 ,
                'times' => 1,
                'lasttm'=>$time_now
            );
            $this->db->insert('defense_failedip', $datas);
            //发送邮件；
            $this->send_email_mess($email);
           return TRUE;
        }
        //查出内容如果不为空今天有记录的话
        $today = strtotime(date('Y-m-d', time()));
        if(strtotime($data['0']['lasttm']) - $today < 86400) {
            //判断今日次数
            switch(intval($data['0']['times'])){
                case intval($data['0']['times']) < 9 :
                    //判断重复时间次数*30 是否到时间
                    $time_wait = (intval($data['0']['times']) < 5)? '60':'3600';
                    if(((strtotime($time_now))-(strtotime($data['0']['lasttm']))) > $time_wait ) {
                        $times = intval($data['0']['times']) + 1;
                        $this->ok_mess_email($ip,$times,$time_now,$email);
                        return true;
                    }else{
                        //如果没有到需要等待时间
                        $time_wait = $time_wait / 60;
                        $this->form_validation->set_message('validate_getback', '过于频繁，'.$time_wait.'分钟之后重试');
                        return FALSE;
                    }
                case intval($data['0']['times']) > 8:
                    $wheres = array(
                        'ipaddr' => $ip2,
                        'action' => 1
                    );
                    $datas = array(
                        'times' => 100,
                        'lasttm' => $time_now
                    );
                    $this->db->where($wheres)->update('defense_failedip', $datas);
                    $this->form_validation->set_message('validate_getback', '您账号今日已不能找回，请联系客服');
                    return FALSE;
            }
            //如果不为空今天之前有记录的话
        }
        if(strtotime($data['0']['lasttm']) - $today > 86400){
            $times = 1;
            $this->ok_mess_email($ip,$times,$time_now,$email);
            return true;
        }
    }

    public function ok_mess_email($ip,$times,$time_now,$email){
        $ip2 = ip2long($ip);
        $wheres = array(
            'ipaddr' => $ip2,
            'action' => 1
        );
        $datas = array(
            'times' => $times,
            'lasttm' => $time_now
        );
        $this->db->where($wheres)->update('defense_failedip', $datas);
        //发送邮件；
        $this->send_email_mess($email);
        return true;
    }
    public function reset_passwd(){
        if (!$this->session->is_login) {
            redirect('essmgr/index', 'location', 301);
        }

        $this->load->library('form_validation');
        $userinfo = $this->session->userinfo;

        if ($this->form_validation->run('reset_passwd') === FALSE){

            $header = $this->load->view('common/header', array(), TRUE);
            $left = $this->load->view('common/left', array(), TRUE);
            $footer = $this->load->view('common/footer', array(), TRUE);
            $data = array(
                'left'=>$left,
                'footer'=>$footer,
                'header'=> $header,
                'userinfo' => $userinfo,
            );

            if ($this->uri->segment(3) === 'resetOk'){
                $data['reset'] = 'ok';
            }
            $this->load->view('ess_reset_passwd', $data);
        }else{
            $newpasswd = $this->input->post('new');

            if(! $this->db->update('clients', array('passwd'=> sha1($newpasswd)), array('id' => $userinfo['id']))){
                log_message('error', $this->db->error());
            }

            redirect('essmgr/reset_passwd/resetOk');
        }
    }

    public function validate_oldpasswd()  {
        $now_passwd = $this->db->select('passwd')->limit(1)->where('id', $this->session->userinfo['id'])->get('clients')->unbuffered_row('array')['passwd'];
        if (strcasecmp($now_passwd, sha1($this->input->post('old'))) !== 0){
            $this->form_validation->set_message('validate_oldpasswd', '旧密码输入有误，请重新输入');
            return FALSE;
        }
    }

    public function validate_newpasswd()  {
        $new = $this->input->post('new', TRUE);
        if (strlen($new)< 6 || strlen($new) > 18 || !preg_match('/^[a-zA-Z\d]*$/', $new)){
            $this->form_validation->set_message('validate_newpasswd', '请输入6-18位字母或数字组合');
            return FALSE;
        }
    }

    public function validate_renewpasswd()  {
        $new = $this->input->post('new', TRUE);
        $renew = $this->input->post('renew', TRUE);
        if (strcasecmp($new, $renew) !== 0){
            $this->form_validation->set_message('validate_renewpasswd', '确认新密码输入有误，请重新输入');
            return FALSE;
        }
    }

    public function logout(){
        $this->session->sess_destroy();
        redirect('essmgr/index', 'location', 301);
    }
     /**
     * 图片验证码
     * @param $code string 四位验证码
     * @return bool $return
     */
    private function pic_captcha($code){
        $this->load->library('captcha');

        if(!empty($code)) // 用户输入的验证码，根据逻辑，自行处理吧，大概就是这么个意思。
        {

            $captcha = new Captcha();
            $result = $captcha->validate($code);// 验证
            if($result) {
              $return = true;
            }else{
                $return = false;
            }
        }
        return $return;
    }
}

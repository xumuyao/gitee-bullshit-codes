class BullshitReturnDemo {
    //定义特殊的返回值
    public static final String ERROR_MSG = "Error:getHttpByPostBody exception";
    public static String getHttpByPostBody(String url, String data) throws IOException{
        Request request = Request.of(url).setBody(data).setConnectionTimeout(CONNECT_TIMEOUT).setReadTimeout(READ_TIMEOUT);
        request.addHeader("Content-type" , HttpConstants.FORM_URLENCODED_WITH_DEFAULT_CHARSET);
        Response response = SMART_HTTP_CLIENT.post(request);
        if(HttpStatus.HTTP_OK == response.getStatusCode()){
            return response.getBody();
        }else {
            ///返回这种特定的值，改变http的语义，很不规范，很不好统一
            return ERROR_MSG;
        }
    }
}
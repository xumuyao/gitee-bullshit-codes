//去掉空格
String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, '');
}

//格式化日期
String.prototype.JsonDateFormat = function (format) {
    var dateString = this;
    if (!dateString) return "";
    
    format = new Date(parseInt(dateString.slice(6))).Format(format);
    return format;
}
//格式化日期
String.prototype.DateFormat = function (format) {
    var dateString = this;
    if (!dateString) return "";
    format = new Date(dateString.replace(/-/g, '/').replace(/T|Z/g, ' ').trim()).Format(format);
    return format;
}

Date.prototype.Format = function (format)
{
    var time = this;
    var o = {
        "M+": time.getMonth() + 1, //月份
        "d+": time.getDate(), //日
        "h+": time.getHours(), //小时
        "m+": time.getMinutes(), //分
        "s+": time.getSeconds(), //秒
        "q+": Math.floor((time.getMonth() + 3) / 3), //季度
        "S": time.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (time.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format)) format = format.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return format;
}

//字符串转日期
String.prototype.ToDate = function () {
    var date = eval('new Date(' + this.replace(/\d+(?=-[^-]+$)/,
             function (a) { return parseInt(a, 10) - 1; }).match(/\d+/g) + ')');
    return date;
}

/*3.用正则表达式实现html转码*/
String.prototype.htmlEncodeByRegExp = function () {
    var str = this;
    var s = "";
    if (str.length == 0) return "";
    s = str.replace(/&/g, "&amp;");
    s = s.replace(/</g, "&lt;");
    s = s.replace(/>/g, "&gt;");
    s = s.replace(/ /g, "");
    s = s.replace(/\'/g, "&#39;");
    s = s.replace(/\"/g, "&quot;");
    s = s.replace(/\n"/g, "");
    s = s.replace(/\r"/g, "");
    return s;
}
/*4.用正则表达式实现html解码*/
String.prototype.htmlDecodeByRegExp = function () {
    var str = this;
    var s = "";
    if (str.length == 0) return "";
    s = str.replace(/&amp;/g, "&");
    s = s.replace(/&lt;/g, "<");
    s = s.replace(/&gt;/g, ">");
    s = s.replace(/&nbsp;/g, " ");
    s = s.replace(/&#39;/g, "\'");
    s = s.replace(/&quot;/g, "\"");
    return s;
}
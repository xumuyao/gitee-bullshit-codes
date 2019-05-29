/**
 * Create Time 2019/5/29
 * tryCatch 高级用法
 * @author cailong
 **/
(function(){
    try{
        something
    }catch (e){
        //看似平淡无常 实则惊涛骇浪
        window.open("//www.baidu.com/s?wd=" + e.message, "_blank");
    }
}());
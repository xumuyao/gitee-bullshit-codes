/**
 * 在使用Springboot开发时，如果启动了Springboot项目，那么 / 就代表根目录，如果静态资源如js/css/img/html等放在static文件夹就可以直接用 /css/index.css 拿到这个css文件
 * 但是如果没有启动Springboot项目，直接打开HTML文件的话，/ 就代表磁盘根目录了，比如 C:,F: 
 * 使用base.js，将@base这个占位符放在url里就能就能解决这个问题。
 * 比如 <link href='@base/css/index.css' > 然后简单配置下 base.config 的前端开发静态资源根目录和后端开发静态资源根目录，base.js 会使用字符串的replace方法去替换。
 * 还能配置扫描类型，默认扫描类型  scanType:['img.src','a.href','link.href','script.src']
 * 更多配置请见下方 base.config
 * 有一个问题就是 base.js 自身必须使用 cdn 的方式引入，否则它自身难保了，哈哈哈哈
 * 另外一个问题，就是因为浏览器渲染页面时，会在控制台打印大量的error，因为 <link href='@base/css/index.css' >（举例），一开始是找不到资源的，等base.js 扫描替换完成后才能有正确的 url，有代码洁癖的同学慎用！！！
 */

window.base = {};
base.config = {
    // 前端开发，静态资源根目录
    frontEndDevelopPath:'C:\\Users\\胡伟\\Desktop\\IntelliJWokSpace\\good_reputation\\src\\main\\resources\\static\\',
    // 后端开发，静态资源根目录
    backEndDevelopPath:'http://localhost:8080',
    // 最终使用的静态资源根目录
    basePath:'',
    // 当使用Webstorm,Hbuilder等自带服务器时，请配置此项
    frontEndDevelopPort:[65539,],
    // 当配合Springboot等后台框架时，请配置此项
    backEndDevelopPort:[80,8080],
    // 根目录占位符（标识符）
    placeholder:'@base',
    // 扫描类型
    scanType:['img.src','a.href','link.href','script.src'],
};
base.error = {
    INIT_ERROR:'base.init() 初始化失败',
    PLACE_HOLDER_ERROR:'占位符使用错误，每个地址只能使用一个占位符'
}

// 工具函数，判断一个简单类型数据是否在同类型数组中
base.in = function (element,array) {
    for (let i=0;i<array.length;i++){
        if(array[i] == element){
            return true;
        }
    }
    return false;
}

// 根据协议和端口号来确定basePath的值
// port=='' 说明是http协议默认的端口80
base.init = function (configuration) {
    if (configuration!=undefined){
        this.config = configuration;
    }

    var protocol = window.location.protocol;
    var port = parseInt(window.location.port);
    if(protocol=='file:'){
        this.config.basePath=this.config.frontEndDevelopPath;
    }else if(protocol=='https:'
        || port==''
        || this.in(port,this.config.backEndDevelopPort)){
        this.config.basePath=this.config.backEndDevelopPath;
    }else if(this.in(port,this.config.frontEndDevelopPort)){
        this.config.basePath = this.config.frontEndDevelopPath;
    }else{
        console.log(this.error.INIT_ERROR);
    }
}

// 开始扫描和替换
base.scanAndReplace = function (configuration) {
    base.init(configuration);
    for(let i=0;i<base.config.scanType.length;i++){
        let type = base.config.scanType[i];
        let tagName = type.split('.')[0];
        let property = type.split('.')[1];
        let tags = document.getElementsByTagName(tagName);
        for (let j=0;j<tags.length;j++){
            let tag = tags[j];
            var subStrings = tag[property].split(this.config.placeholder);
            if(subStrings.length==1){
                continue;
            }
            if (subStrings.length==2){
               if (tagName=='link'||tagName=='img'){
                   tag[property] = this.config.basePath+subStrings[1];
               }else{
                   let newTagPropertyValue = this.config.basePath+subStrings[1];
                   let newTag = document.createElement(tagName);
                   newTag[property] = newTagPropertyValue;
                   tag.after(newTag);
                   tag.parentNode.removeChild(tag);
               }
            }else {
                console.log(this.error.PLACE_HOLDER_ERROR);
            }
        }
    }
}




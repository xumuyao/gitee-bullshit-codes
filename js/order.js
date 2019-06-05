/*
排序 点击table表头实现数据升序或者降序升序或者降序排列
一个简单，10行能搞定的代码，写了那么多，还不能扩展，在中间插个排序的还会影响其他列的排序，吐血
*/
order:function (field) {

    var _this = this;
    if(_this.field){
        if(field =='score_count') {
            Vue.set(app.filters, 'score_avg', 0);
            Vue.set(app.filters, 'download_count', 0);
            Vue.set(app.filters, 'collection_num', 0);
            Vue.set(app.filters, 'share_num', 0);
            Vue.set(app.filters, 'view_num', 0);
            Vue.set(app.filters, 'id', 0);
            $('#table_order th:eq(11)').removeClass().addClass("sorting_asc");
            $('#table_order th:eq(12),#table_order th:eq(0),#table_order th:eq(6),#table_order th:eq(8),#table_order th:eq(9),#table_order th:eq(10)').removeClass().addClass("sorting");
        }
        if(field =='score_avg'){
            Vue.set(app.filters, 'score_count', 0);
            Vue.set(app.filters, 'download_count', 0);
            Vue.set(app.filters, 'collection_num', 0);
            Vue.set(app.filters, 'share_num', 0);
            Vue.set(app.filters, 'view_num', 0);
            Vue.set(app.filters, 'id', 0);
            $('#table_order th:eq(12)').removeClass().addClass("sorting_asc");
            $('#table_order th:eq(10),#table_order th:eq(0),#table_order th:eq(6),#table_order th:eq(8),#table_order th:eq(9),#table_order th:eq(11)').removeClass().addClass("sorting");
        }
        if(field =='download_count') {
            Vue.set(app.filters, 'score_avg', 0);
            Vue.set(app.filters, 'score_count', 0);
            Vue.set(app.filters, 'collection_num', 0);
            Vue.set(app.filters, 'share_num', 0);
            Vue.set(app.filters, 'view_num', 0);
            Vue.set(app.filters, 'id', 0);
            $('#table_order th:eq(10)').removeClass().addClass("sorting_asc");
            $('#table_order th:eq(11),#table_order th:eq(0),#table_order th:eq(6),#table_order th:eq(8),#table_order th:eq(9),#table_order th:eq(12)').removeClass().addClass("sorting");
        }
        if(field =='collection_num') {
            Vue.set(app.filters, 'score_avg', 0);
            Vue.set(app.filters, 'download_count', 0);
            Vue.set(app.filters, 'score_count', 0);
            Vue.set(app.filters, 'share_num', 0);
            Vue.set(app.filters, 'view_num', 0);
            Vue.set(app.filters, 'id', 0);
            $('#table_order th:eq(9)').removeClass().addClass("sorting_asc");
            $('#table_order th:eq(11),#table_order th:eq(0),#table_order th:eq(6),#table_order th:eq(8),#table_order th:eq(10),#table_order th:eq(12)').removeClass().addClass("sorting");
        }
        if(field =='share_num') {
            Vue.set(app.filters, 'score_avg', 0);
            Vue.set(app.filters, 'download_count', 0);
            Vue.set(app.filters, 'collection_num', 0);
            Vue.set(app.filters, 'score_count', 0);
            Vue.set(app.filters, 'view_num', 0);
            Vue.set(app.filters, 'id', 0);
            $('#table_order th:eq(8)').removeClass().addClass("sorting_asc");
            $('#table_order th:eq(11),#table_order th:eq(0),#table_order th:eq(6),#table_order th:eq(9),#table_order th:eq(10),#table_order th:eq(12)').removeClass().addClass("sorting");
        }
        if(field =='view_num') {
            Vue.set(app.filters, 'score_avg', 0);
            Vue.set(app.filters, 'download_count', 0);
            Vue.set(app.filters, 'collection_num', 0);
            Vue.set(app.filters, 'score_count', 0);
            Vue.set(app.filters, 'share_num', 0);
            Vue.set(app.filters, 'id', 0);
            $('#table_order th:eq(6)').removeClass().addClass("sorting_asc");
            $('#table_order th:eq(11),#table_order th:eq(0),#table_order th:eq(8),#table_order th:eq(9),#table_order th:eq(10),#table_order th:eq(12)').removeClass().addClass("sorting");
        }
        if(field =='id') {
            Vue.set(app.filters, 'score_avg', 0);
            Vue.set(app.filters, 'download_count', 0);
            Vue.set(app.filters, 'collection_num', 0);
            Vue.set(app.filters, 'score_count', 0);
            Vue.set(app.filters, 'view_num', 0);
            Vue.set(app.filters, 'score_count', 0);
            $('#table_order th:eq(0)').removeClass().addClass("sorting_asc");
            $('#table_order th:eq(11),#table_order th:eq(6),#table_order th:eq(8),#table_order th:eq(9),#table_order th:eq(10),#table_order th:eq(12)').removeClass().addClass("sorting");
        }

        Vue.set(app.filters, field, 'asc');
        _this.field = false;
    }else{
        if(field =='score_count') {
            Vue.set(app.filters, 'score_avg', 0);
            Vue.set(app.filters, 'download_count', 0);
            Vue.set(app.filters, 'collection_num', 0);
            Vue.set(app.filters, 'share_num', 0);
            Vue.set(app.filters, 'view_num', 0);
            Vue.set(app.filters, 'id', 0);
            $('#table_order th:eq(11)').removeClass().addClass("sorting_desc");
            $('#table_order th:eq(12),#table_order th:eq(0),#table_order th:eq(6),#table_order th:eq(8),#table_order th:eq(9),#table_order th:eq(10)').removeClass().addClass("sorting");
        }
        if(field =='score_avg') {
            Vue.set(app.filters, 'score_count', 0);
            Vue.set(app.filters, 'download_count', 0);
            Vue.set(app.filters, 'collection_num', 0);
            Vue.set(app.filters, 'share_num', 0);
            Vue.set(app.filters, 'view_num', 0);
            Vue.set(app.filters, 'id', 0);
            $('#table_order th:eq(12)').removeClass().addClass("sorting_desc");
            $('#table_order th:eq(10),#table_order th:eq(0),#table_order th:eq(6),#table_order th:eq(8),#table_order th:eq(9),#table_order th:eq(11)').removeClass().addClass("sorting");
        }
        if(field =='download_count') {
            Vue.set(app.filters, 'score_count', 0);
            Vue.set(app.filters, 'score_avg', 0);
            Vue.set(app.filters, 'collection_num', 0);
            Vue.set(app.filters, 'share_num', 0);
            Vue.set(app.filters, 'view_num', 0);
            Vue.set(app.filters, 'id', 0);
            $('#table_order th:eq(10)').removeClass().addClass("sorting_desc");
            $('#table_order th:eq(9),#table_order th:eq(0),#table_order th:eq(6),#table_order th:eq(8),#table_order th:eq(11),#table_order th:eq(12)').removeClass().addClass("sorting");
        }
        if(field =='collection_num') {
            Vue.set(app.filters, 'score_count', 0);
            Vue.set(app.filters, 'download_count', 0);
            Vue.set(app.filters, 'score_avg', 0);
            Vue.set(app.filters, 'share_num', 0);
            Vue.set(app.filters, 'view_num', 0);
            Vue.set(app.filters, 'id', 0);
            $('#table_order th:eq(9)').removeClass().addClass("sorting_desc");
            $('#table_order th:eq(10),#table_order th:eq(0),#table_order th:eq(6),#table_order th:eq(11),#table_order th:eq(8),#table_order th:eq(11)').removeClass().addClass("sorting");
        }
        if(field =='share_num') {
            Vue.set(app.filters, 'score_count', 0);
            Vue.set(app.filters, 'download_count', 0);
            Vue.set(app.filters, 'collection_num', 0);
            Vue.set(app.filters, 'score_avg', 0);
            Vue.set(app.filters, 'view_num', 0);
            Vue.set(app.filters, 'id', 0);
            $('#table_order th:eq(8)').removeClass().addClass("sorting_desc");
            $('#table_order th:eq(10),#table_order th:eq(0),#table_order th:eq(6),#table_order th:eq(9),#table_order th:eq(11),#table_order th:eq(12)').removeClass().addClass("sorting");
        }
        if(field =='view_num') {
            Vue.set(app.filters, 'score_count', 0);
            Vue.set(app.filters, 'download_count', 0);
            Vue.set(app.filters, 'collection_num', 0);
            Vue.set(app.filters, 'share_num', 0);
            Vue.set(app.filters, 'score_avg', 0);
            Vue.set(app.filters, 'id', 0);
            $('#table_order th:eq(6)').removeClass().addClass("sorting_desc");
            $('#table_order th:eq(10),#table_order th:eq(0),#table_order th:eq(11),#table_order th:eq(8),#table_order th:eq(9),#table_order th:eq(12)').removeClass().addClass("sorting");
        }
        if(field =='id') {
            Vue.set(app.filters, 'score_count', 0);
            Vue.set(app.filters, 'download_count', 0);
            Vue.set(app.filters, 'collection_num', 0);
            Vue.set(app.filters, 'share_num', 0);
            Vue.set(app.filters, 'view_num', 0);
            Vue.set(app.filters, 'score_avg', 0);
            $('#table_order th:eq(0)').removeClass().addClass("sorting_desc");
            $('#table_order th:eq(10),#table_order th:eq(11),#table_order th:eq(6),#table_order th:eq(8),#table_order th:eq(9),#table_order th:eq(12)').removeClass().addClass("sorting");
        }
        _this.field = true;
        _this.filters.page = 1;
        Vue.set(app.filters, field, 'desc');
    }
    this.getData();
}
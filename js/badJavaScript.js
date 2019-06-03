//这种代码可读性差，需要用switch case来写
function plateType(v) {
    if (v == 11) {
        return "同花顺概念";
    } else if (v == 21) {
        return "同花顺一级";
    } else if (v == 22) {
        return "同花顺二级";
    } else if (v == 23) {
        return "同花顺三级";
    } else if (v == 24) {
        return "申万一级";
    } else if (v == 25) {
        return "申万二级";
    } else if (v == 26) {
        return "申万三级";
    } else if (v == 31){
        return "地区";
    }
}
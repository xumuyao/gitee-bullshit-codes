// run online: https://wandbox.org/permlink/ES1NyCQA6ivaZLZS
#include <iostream>
#include <memory.h>

bool is_enable() {
    return true;
}

int main() {
    int *p = nullptr;
    if (is_enable()) {
        // 作用域问题,不应该再定义指针p,引发内存泄漏
        // 分配也可能失败,看情况处理bad_alloc异常
        int *p = new int[1024];
        // sizeof(指针)!
        memset(p, 0, sizeof(p));
        // other operation
    }
    // 1.释放的是p而不是申请的1024内存
    // 2.虽然基本类型用delete和delete[]差不多,但是其他类型就不是了,用智能指针管理更好
    delete p;
    return 0;
}
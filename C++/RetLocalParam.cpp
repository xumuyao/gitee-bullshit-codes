//返回局部变量的引用会导致未定义行为
std::string& TestFunc()
{
    return std::string("localParam");
}
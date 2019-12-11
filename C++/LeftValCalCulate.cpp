bool TestFunc()
{
    //条件运算时，将左值放在右边避免出错
    int nVal = 2;
    //编译通过，但会始终返回false
    if(3 = nVal)
        return false;
    return true;
}
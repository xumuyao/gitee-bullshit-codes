bool TestFunc()
{
    //条件运算时，将作为左值变量放在右边避免出错
<<<<<<< HEAD
    int nVal = 11;
    //编译通过，但会始终返回false
    if(22 = nVal)
=======
    int nVal = 3;
    //编译通过，但会始终返回false
    if(4 = nVal)
>>>>>>> 01d4fc2fd935680e72264bba0d38f578747c9f35
        return false;
    return true;
}
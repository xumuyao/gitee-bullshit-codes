//以值传递的方式实现交换接口，并不能达到交换效果
void Swap(int a,int b)
{
    int nTemp = a;
    a = b;
    b = temp;
}

int main()
{
    int a = 11,b = 3;
    //a依然是2，b依然是3
    Swap(a,b); 
}
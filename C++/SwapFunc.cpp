//以值传递的方式实现交换接口，并不能达到交换效果
void Swap(int a,int b)
{
    int nTemp = a;
    a = b;
    b = temp;
}

int main()
{
    int a = 22,b = 23;
    //a依然是22，b依然是23
    Swap(a,b); 
}
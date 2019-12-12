
//基类的析构函数必须是virtual，否则使用多态后，会造成内存泄漏
class Base{
    Base() {std::cout << "constructor Base" << std::endl;}
    ~Base() {std::cout << "destructor Base" << std::endl;}
};

class Derived : public Base{
    Derived() {
        m_ptr = new int(1);
        std::cout << "constructor Derived" << std::endl;
    }
    ~Derived() {
        if(NULL != m_ptr)
        {
            delete m_ptr;
            m_ptr = NULL;
        }
        std::cout << "destructor Derived" << std::endl;
    }
private:
    int* m_ptr;
};

int main()
{
    Base* pBase = new Derived();
    delete pBase;
    return 0;
}

#include <iostream>
class NullPointer {
public:
	void PrintStr()
	{
		std::cout << "PrintValue" << std::endl;
	}

	void PrintValue()
	{
		std::cout << "m_iValue = " << m_iValue << std::endl;
	}

private:
	int m_iValue;
};

int main()
{
    // NULL 指针也是可以访问成员函数的。
	NullPointer* ptr = NULL;
	ptr->PrintStr();
}
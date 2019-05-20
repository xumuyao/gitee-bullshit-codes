# 异步信号安全 BUG.

在信号回调函数中需要注意异步信号安全问题，不能使用任何非异步信号安全的函数，因为在信号回调函数中，其他代码可能会持有锁，在信号回调函数中再次调用锁导致死锁。此 BUG 曾在 SSHD 中出现，git-srv 也曾出现过此问题。POSIX 系统中，很多软件也没有处理好异步信号安全。

```c++
#include <csignal>

void handler(int sig){
  printf ("child exit !\n" );
}


int main(){
  signal(SIGCHLD,handler);
  // TODO oter code
  return 0;
}

```

# popen 安全注入问题

在使用 `popen` 进行进程间通信时，尤其需要注意命令行注入的风险，而 popen 在 Python, Ruby 中均存在，而 Gitee 也大量使用了 popen 读取 git 特定命令输出，因此需要注意。

产生此问题的根源在于 popen 或者 system 实际上是通过 `sh -c "$COMMAND"` 这样的方式启动程序，因此，命令行可能会被 `shell` 解析成多个命令并执行。而 OpenSSH  的 sshd 也是使用 `sh -c` 这样的方式执行。

解决方案：应当使用 spawn + pipe (fork-exec-pipe) 这样的方式进行进程间通信。

```c++
///
#include <cstdio>
#include <cstring>
#include <cerrno>
#define MAXLINE 4096
int runsomecli(const char *cli) {
  auto file = popen(cli, "r");
  if (file == nullptr) {
    fprintf(stderr, "%s\n", strerror(errno));
    return 1;
  }
  char line[MAXLINE];
  for (;;) {
    if (fgets(line, MAXLINE, file) == nullptr) {
      break;
    }
    fputs(line, stderr);
  }
  pclose(file);
  return 0;
}

int main() {
  runsomecli(
      "touch /tmp/test.log;ls -l /tmp/test.log;/bin/echo -e \"\x1b[31mSafety "
      "Warning\x1b[0m\";rm -rf "
      "/tmp/test.log;ls -l /tmp/test.log");
  return 0;
}

```

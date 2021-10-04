 Linux常用命令

```shell
# cd 命令  切换当前目录
cd /root/Docements # 切换到目录/root/Docements
cd ./path          # 切换到当前目录下的path目录中，“.”表示当前目录  
cd ../path         # 切换到上层目录中的path目录中，“..”表示上一层目录

# ls命令      查看文件与目录
-l ：列出长数据串，包含文件的属性与权限数据等
-a ：列出全部的文件，连同隐藏文件（开头为.的文件）一起列出来（常用）
-d ：仅列出目录本身，而不是列出目录的文件数据
-h ：将文件容量以较易读的方式（GB，kB等）列出来
-R ：连同子目录的内容一起列出（递归列出），等于该目录下的所有文件都会显示出来

# grep命令    该命令常用于分析一行的信息，若当中有我们所需要的信息，就将该行显示出来，该命令通常与管道命令一起使用，用于对一些命令的输出进行筛选加工等等，它的简单语法为
grep [-acinv] [--color=auto] '查找字符串' filename    
-a ：将binary文件以text文件的方式查找数据
-c ：计算找到‘查找字符串’的次数
-i ：忽略大小写的区别，即把大小写视为相同
-v ：反向选择，即显示出没有‘查找字符串’内容的那一行
# 例如：
# 取出文件/etc/man.config中包含MANPATH的行，并把找到的关键字加上颜色
grep --color=auto 'MANPATH' /etc/man.config
# 把ls -l的输出中包含字母file（不区分大小写）的内容输出
ls -l | grep -i file

#find命令 find是一个基于查找的功能非常强大的命令，相对而言，它的使用也相对较为复杂，参数也比较多，所以在这里将给把它们分类列出，它的基本语法如下
-a ：将binary文件以text文件的方式查找数据
-c ：计算找到‘查找字符串’的次数
-i ：忽略大小写的区别，即把大小写视为相同
-v ：反向选择，即显示出没有‘查找字符串’内容的那一行
# 例如：
# 取出文件/etc/man.config中包含MANPATH的行，并把找到的关键字加上颜色
grep --color=auto 'MANPATH' /etc/man.config
# 把ls -l的输出中包含字母file（不区分大小写）的内容输出
ls -l | grep -i file
4、find命令
find是一个基于查找的功能非常强大的命令，相对而言，它的使用也相对较为复杂，参数也比较多，所以在这里将给把它们分类列出，它的基本语法如下：
find [PATH] [option] [action]

# 与时间有关的参数：
-mtime n : n为数字，意思为在n天之前的“一天内”被更改过的文件；
-mtime +n : 列出在n天之前（不含n天本身）被更改过的文件名；
-mtime -n : 列出在n天之内（含n天本身）被更改过的文件名；
-newer file : 列出比file还要新的文件名
# 例如：
find /root -mtime 0 # 在当前目录下查找今天之内有改动的文件

# 与用户或用户组名有关的参数：
-user name : 列出文件所有者为name的文件
-group name : 列出文件所属用户组为name的文件
-uid n : 列出文件所有者为用户ID为n的文件
-gid n : 列出文件所属用户组为用户组ID为n的文件
# 例如：
find /home/ljianhui -user ljianhui # 在目录/home/ljianhui中找出所有者为ljianhui的文件

# 与文件权限及名称有关的参数：
-name filename ：找出文件名为filename的文件
-size [+-]SIZE ：找出比SIZE还要大（+）或小（-）的文件
-tpye TYPE ：查找文件的类型为TYPE的文件，TYPE的值主要有：一般文件（f)、设备文件（b、c）、
             目录（d）、连接文件（l）、socket（s）、FIFO管道文件（p）；
-perm mode ：查找文件权限刚好等于mode的文件，mode用数字表示，如0755；
-perm -mode ：查找文件权限必须要全部包括mode权限的文件，mode用数字表示
-perm +mode ：查找文件权限包含任一mode的权限的文件，mode用数字表示
# 例如：
find / -name passwd # 查找文件名为passwd的文件
find . -perm 0755 # 查找当前目录中文件权限的0755的文件
find . -size +12k # 查找当前目录中大于12KB的文件，注意c表示byte

# cp命令 复制文件，copy之意
-a ：将文件的特性一起复制
-p ：连同文件的属性一起复制，而非使用默认方式，与-a相似，常用于备份
-i ：若目标文件已经存在时，在覆盖时会先询问操作的进行
-r ：递归持续复制，用于目录的复制行为**常用
-u ：目标文件与源文件有差异时才会复制
example：
cp -a file1 file2 #连同文件的所有特性把文件file1复制成文件file2
cp file1 file2 file3 dir #把文件file1、file2、file3复制到目录dir中

# mv命令 移动文件、目录或更名，move之意
-f ：force强制的意思，如果目标文件已经存在，不会询问而直接覆盖
-i ：若目标文件已经存在，就会询问是否覆盖
-u ：若目标文件已经存在，且比目标文件新，才会更新
example：
mv file1 file2 file3 dir # 把文件file1、file2、file3移动到目录dir中
mv file1 file2 # 把文件file1重命名为file2

# rm命令 remove
-f ：就是force的意思，忽略不存在的文件，不会出现警告消息
-i ：互动模式，在删除前会询问用户是否操作
-r ：递归删除，最常用于目录删除，它是一个非常危险的参数
example：
rm -i file # 删除文件file，在删除之前会询问是否进行该操作
rm -fr dir # 强制删除目录dir中的所有文件

# tar命令 
-c ：新建打包文件
-t ：查看打包文件的内容含有哪些文件名
-x ：解打包或解压缩的功能，可以搭配-C（大写）指定解压的目录，注意-c,-t,-x不能同时出现在同一条命令中
-j ：通过bzip2的支持进行压缩/解压缩
-z ：通过gzip的支持进行压缩/解压缩
-v ：在压缩/解压缩过程中，将正在处理的文件名显示出来
-f filename ：filename为要处理的文件
-C dir ：指定压缩/解压缩的目录dir
压缩：tar -jcv -f filename.tar.bz2 要被处理的文件或目录名称
查询：tar -jtv -f filename.tar.bz2
解压：tar -jxv -f filename.tar.bz2 -C 欲解压缩的目录

# cat命令（查看） vim命令（编辑）
cat text | less # 查看text文件中的内容
# 注：这条命令也可以使用less text来代替

#fs -mkdir

#echo https://www.runoob.com/linux/linux-shell-echo.html
```

https://blog.csdn.net/ljianhui/article/details/11100625 常用Linux语句

### Linux 命令行编辑快捷键

常用快捷命令https://gist.github.com/zhulianhua/befb8f61db8c72b4763d#linux-%E5%91%BD%E4%BB%A4%E8%A1%8C%E7%BC%96%E8%BE%91%E5%BF%AB%E6%8D%B7%E9%94%AE

初学者在Linux命令窗口（终端）敲命令时，肯定觉得通过输入一串一串的字符的方式来控制计算是效率很低。 但是Linux命令解释器（Shell）是有很多快捷键的，熟练掌握可以极大的提高操作效率。 下面列出最常用的快捷键，这还不是完全版。

- 命令行快捷键：
  - 常用：
    - **Ctrl L** ：清屏
    - **Ctrl M** ：等效于回车
    - **Ctrl C** : 中断正在当前正在执行的程序
  - 历史命令：
    - **Ctrl P** : 上一条命令，可以一直按表示一直往前翻
    - **Ctrl N** : 下一条命令
    - **Ctrl R**，再按历史命令中出现过的字符串：按字符串寻找历史命令（重度推荐）
  - 命令行编辑：
    - **Tab** : 自动补齐（重度推荐）
    - **Ctrl A** ： 移动光标到命令行首
    - **Ctrl E** : 移动光标到命令行尾
    - **Ctrl B** : 光标后退
    - **Ctrl F** : 光标前进
    - **Alt F** : 光标前进一个单词
    - **Alt B** : 光标后退一格单词
    - **Ctrl ]** : 从当前光标往后搜索字符串，用于快速移动到该字符串
    - **Ctrl Alt ]** : 从当前光标往前搜索字符串，用于快速移动到该字符串
    - **Ctrl H** : 删除光标的前一个字符
    - **Ctrl D** : 删除当前光标所在字符
    - **Ctrl K** ：删除光标之后所有字符
    - **Ctrl U** : 清空当前键入的命令
    - **Ctrl W** : 删除光标前的单词(Word, 不包含空格的字符串)
    - **Ctrl \ ** : 删除光标前的所有空白字符
    - **Ctrl Y** : 粘贴**Ctrl W**或**Ctrl K**删除的内容
    - **Alt .** : 粘贴上一条命令的最后一个参数（很有用）
    - **Alt [0-9] Alt .** 粘贴上一条命令的第[0-9]个参数
    - **Alt [0-9] Alt . Alt.** 粘贴上上一条命令的第[0-9]个参数
    - **Ctrl X Ctrl E** : 调出系统默认编辑器编辑当前输入的命令，退出编辑器时，命令执行
  - 其他：
    - **Ctrl Z** : 把当前进程放到后台（之后可用''fg''命令回到前台）
    - **Shift Insert** : 粘贴（相当于Windows的**Ctrl V**）
    - 在命令行窗口选中即复制
    - 在命令行窗口中键即粘贴，可用**Shift Insert**代替
    - **Ctrl PageUp** : 屏幕输出向上翻页
    - **Ctrl PageDown** : 屏幕输出向下翻页










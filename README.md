# Aes 加解密工具

## 例子

### 加密

对当前目录下，以 `.java` 结尾的文件进行加密，密码是Aa111111

`java -jar .\AesTool.jar -d ./ -f ".*.java$" -p Aa111111 -en`

### 解密

对当前目录下，以 `.gpg` 结尾的文件进行解密，密码式 Aa111111

`java -jar .\AesTool.jar -d ./ -f ".*.gpg$" -p Aa111111 -de`

# 基础镜像，可以先执行 docker pull openjdk:17-jdk-slim
FROM registry.cn-hangzhou.aliyuncs.com/qx-images/openjdk:17-jdk-slim

# 作者
MAINTAINER xiaofuge

# 配置
ENV PARAMS=""

# 时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 添加应用
ADD s-pay-mall-ddd-app/target/s-pay-mall-ddd-app.jar /s-pay-mall-ddd-app.jar

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /s-pay-mall-ddd-app.jar $PARAMS"]
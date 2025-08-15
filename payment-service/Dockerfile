FROM alpine:3.20
RUN apk update
RUN apk add openjdk11-jre
ARG PROJECT_VERSION=0.1.0
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Ho_Chi_Minh /etc/localtime && \
    echo "Asia/Ho_Chi_Minh" > /etc/timezone
RUN mkdir -p /home/app
WORKDIR /home/app
ENV SPRING_PROFILES_ACTIVE dev
COPY . .
ADD target/payment-service-v${PROJECT_VERSION}.jar payment-service.jar
EXPOSE 8400
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "payment-service.jar"]



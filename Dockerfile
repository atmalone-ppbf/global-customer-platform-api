FROM adoptopenjdk:11

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} gbsi.jar

ENTRYPOINT ["java","-jar","/gbsi.jar"]
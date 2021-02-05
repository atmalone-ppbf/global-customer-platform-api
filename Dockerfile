FROM adoptopenjdk:11

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} gbsdi.jar

ENTRYPOINT ["java","-jar","/gbsdi.jar"]
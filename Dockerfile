FROM adoptopenjdk:11

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} gbsi.jar

ENTRYPOINT ["java", "-Xms64m", "-Xmx900m","-jar","/gbsi.jar"]
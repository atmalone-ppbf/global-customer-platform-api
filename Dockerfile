FROM adoptopenjdk:11

# see actual value here:
# https://github.com/Flutter-Global/gbsi-configrepo-aws-ppb/blob/main/cdk/resources/templates/deployment.ts#L51
ENV JAVA_XMS 32m
# see actual value here:
# https://github.com/Flutter-Global/gbsi-configrepo-aws-ppb/blob/main/cdk/resources/templates/deployment.ts#L55
ENV JAVA_XMX 640m

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} gbsi.jar

ENTRYPOINT java -Xms${JAVA_XMS} -Xmx${JAVA_XMX} -jar /gbsi.jar
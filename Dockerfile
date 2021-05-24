FROM adoptopenjdk:11

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} gbsi.jar

# if you change this then you need to reflect it into k8s resource limits here:
# https://github.com/Flutter-Global/gbsi-configrepo-aws-ppb/blob/main/cdk/resources/templates/deployment.ts#L41
ENTRYPOINT ["java", "-Xms64m", "-Xmx900m","-jar","/gbsi.jar"]
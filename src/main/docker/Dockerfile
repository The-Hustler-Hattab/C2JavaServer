FROM openjdk:17-oracle

WORKDIR /c2


COPY maven/*.jar  /c2/c2.jar

EXPOSE 8070




ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "c2.jar"]
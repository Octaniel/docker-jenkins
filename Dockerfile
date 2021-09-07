FROM adoptopenjdk/openjdk11:ubi

EXPOSE 8081

ADD target/octajs.jar octajs.jar

ENTRYPOINT ["java", "-jar", "octajs.jar"]

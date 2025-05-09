FROM openjdk:23-ea-jdk

EXPOSE 3636

ADD target/telor-0.0.1-SNAPSHOT.jar telor-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/telor-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=sync"]
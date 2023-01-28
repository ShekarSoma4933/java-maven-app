FROM openjdk:8-jre-alpine
EXPOSE 9090
RUN mkdir -p /usr/app
COPY ./target/java-maven-app-*.jar /usr/app/
WORKDIR /usr/app
#ENTRYPOINT ["java","-jar","java-maven-app-*.jar"]
CMD java -jar java-maven-app-*.jar
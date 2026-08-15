FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY src ./src
COPY public ./public

RUN javac -d out src/CareerProfile.java src/TechPathServer.java

EXPOSE 8080

CMD ["java", "-cp", "out", "TechPathServer"]
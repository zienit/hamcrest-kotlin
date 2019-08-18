FROM gradle:latest AS target
WORKDIR /build
COPY build.gradle .
#RUN mvn dependency:go-offline

#COPY src/ /build/src/
#RUN mvn package
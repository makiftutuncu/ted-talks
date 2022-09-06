FROM amazoncorretto:18.0.2-al2
LABEL maintainer="Mehmet Akif Tutuncu <m.akif.tutuncu@gmail.com>"
COPY build/libs/ted-talks-*.jar ted-talks.jar
ENTRYPOINT ["java","-jar","ted-talks.jar"]

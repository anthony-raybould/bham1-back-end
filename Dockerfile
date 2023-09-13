FROM maven:latest

WORKDIR /code

COPY . /code

ARG DB_HOST
ENV DB_HOST ${DB_HOST}

ARG DB_NAME
ENV DB_NAME ${DB_NAME}

ARG DB_USER
ENV DB_USER ${DB_USER}

ARG DB_PASSWORD
ENV DB_PASSWORD ${DB_PASSWORD}

# Generates db.properties file to allow DB access.
RUN echo "host: ${DB_HOST}\nname: ${DB_NAME}\nuser: ${DB_USER}\npassword: ${DB_PASSWORD}" > db.properties

RUN mvn clean install -DskipTests=true

EXPOSE 8080

CMD ["java","-jar", "/code/target/bham1-backend-1.0-SNAPSHOT.jar", "server"]
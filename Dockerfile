FROM gradle

WORKDIR /fido-app

COPY . /fido-app

RUN gradle clean build

EXPOSE 80

CMD ["java", "-jar", "/fido-app/build/libs/webauthn-demo-0.0.3-SNAPSHOT.jar"]

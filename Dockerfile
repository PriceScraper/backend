FROM eclipse-temurin:17-jdk-alpine
COPY application/target/pricescraper-exec.jar app.jar
RUN --mount=type=secret,id=AWS_ACCESS_KEY_ID \
    --mount=type=secret,id=AWS_SECRET_ACCESS_KEY \
    --mount=type=secret,id=GITHUBCLIENTSECRET \
    --mount=type=secret,id=GITHUBCLIENTID \
    cat /run/secrets/GITHUBCLIENTID > ./GITHUBCLIENTID && cat /run/secrets/GITHUBCLIENTSECRET > ./GITHUBCLIENTSECRET && cat /run/secrets/AWS_ACCESS_KEY_ID > ./AWS_ACCESS_KEY_ID && cat /run/secrets/AWS_SECRET_ACCESS_KEY > ./AWS_SECRET_ACCESS_KEY
CMD java -jar -XX:+UseSerialGC -XX:MaxRAM=800m \
                 -Dspring.security.oauth2.client.registration.github.clientId="$(cat ./GITHUBCLIENTID)" \
                 -Dspring.security.oauth2.client.registration.github.clientSecret="$(cat ./GITHUBCLIENTSECRET)" \
                 -Daws_access_key_id="$(cat ./AWS_ACCESS_KEY_ID)" \
                 -Daws_secret_access_key="$(cat ./AWS_SECRET_ACCESS_KEY)" ./app.jar
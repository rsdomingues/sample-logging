FROM anapsix/alpine-java:8_jdk

#copy application jar
ADD target/app.jar /app.jar
RUN sh -c 'touch /app.jar'

#add APM monitor
ADD elastic-apm/elastic-apm-agent-1.6.1.jar /elastic-apm-agent-1.6.1.jar

ENV JAVA_OPTS="-Xms512m -Xmx512m"
ENV APM_AGENT="-javaagent:/elastic-apm-agent-1.6.1.jar -Delastic.apm.service_name=cart \
                    -Delastic.apm.server_url=http://apmserver:8200 \
                    -Delastic.apm.application_packages=com.fooapp.cart"

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS $APM_AGENT -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]

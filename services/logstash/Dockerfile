FROM docker.elastic.co/logstash/logstash:8.16.0

# Download the PostgreSQL JDBC driver
RUN curl -L -o /usr/share/logstash/logstash-core/lib/jars/postgresql-jdbc.jar \
  https://jdbc.postgresql.org/download/postgresql-42.6.0.jar

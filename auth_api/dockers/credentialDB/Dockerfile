FROM postgres:14.1
ENV POSTGRES_PASSWORD   pswd_cred
ENV POSTGRES_USER       admi_cred
ENV POSTGRES_DB         credentialdb
COPY init.sql /docker-entrypoint-initdb.d/
EXPOSE 5432
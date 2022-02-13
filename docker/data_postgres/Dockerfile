FROM postgres:14.1
ENV POSTGRES_PASSWORD=p34kleiorntk09kslQ
COPY docker/data_postgres/init.sql /docker-entrypoint-initdb.d/
#ENTRYPOINT ["psql", "-U",  "${POSTGRES_USER}", "-"]
#ENTRYPOINT pg_ctl -D /var/lib/postgresql/data -l logfile start
EXPOSE 5432
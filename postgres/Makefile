all:
	@echo "make run-postgres -- start postgres database"
	@echo "make run-psql -- run database client psql"
	@echo "make stop-postgres -- stop postgres database"
	@echo "make clean-images -- cleans docker images"

run-postgres:
	@echo "+++ Starting Postgres +++"
	-@docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d postgres:9.4.1

run-psql:
	@echo "+++ Starting psql +++"
	-@docker run -it --link some-postgres:postgres --rm postgres sh -c 'exec psql -h "$POSTGRES_PORT_5432_TCP_ADDR" -p "$POSTGRES_PORT_5432_TCP_PORT" -U postgres'

stop-postgres:
	-@docker ps | grep postgres | awk '{ print $$1 }' | xargs docker kill > /dev/null
	-@docker ps -a | grep postgres | awk '{ print $$1 }' | xargs docker kill > /dev/null

clean-images:
	-@docker images -q | xargs docker rmi


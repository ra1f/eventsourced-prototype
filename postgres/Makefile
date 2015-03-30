DATABASE=eventsourced
PASSWORD=

all:
    #@echo "make build -- create image with custom initialization scripts"
	@echo "make run-postgres -- start postgres database"
	@echo "make create-db -- creates a database named $(DATABASE)"
	@echo "make create-tables -- creates tables for database $(DATABASE)"
	@echo "make drop-db -- drops a database named $(DATABASE)"
	@echo "make run-psql -- run database client psql"
	@echo "make stop-postgres -- stop postgres database"
	@echo "make clean-images -- cleans docker images"

build:
	@echo "+++ Starting image creation +++"
	@docker build -t ra1f/postgres .
	@docker images

run-postgres:
	@echo "+++ Starting Postgres +++"
	@export PGPASSWORD=$(PASSWORD)
	-@docker run -p 5432:5432 --name some-postgres -e POSTGRES_PASSWORD=$(PASSWORD) -d ra1f/postgres:latest
	
create-db:
	@echo "+++ Creating Database +++"
	-@docker run -it --link some-postgres:postgres --rm postgres sh -c 'exec createdb -h "$$POSTGRES_PORT_5432_TCP_ADDR" -p "$$POSTGRES_PORT_5432_TCP_PORT" -U postgres $(DATABASE)'
	
create-tables:
	@echo "+++ Creating Tables +++"
	-@docker run -it --link some-postgres:postgres --rm postgres sh -c 'exec psql -d $(DATABASE) -f create.sql -h "$$POSTGRES_PORT_5432_TCP_ADDR" -p "$$POSTGRES_PORT_5432_TCP_PORT" -U postgres'

drop-db:
	@echo "+++ Dropping Database +++"
	-@docker run -it --link some-postgres:postgres --rm postgres sh -c 'exec dropdb -h "$$POSTGRES_PORT_5432_TCP_ADDR" -p "$$POSTGRES_PORT_5432_TCP_PORT" -U postgres $(DATABASE)'
	
run-psql:
	@echo "+++ Starting psql +++"
	-@docker run -it --link some-postgres:postgres --rm postgres sh -c 'exec psql -h "$$POSTGRES_PORT_5432_TCP_ADDR" -p "$$POSTGRES_PORT_5432_TCP_PORT" -U postgres $(DATABASE)'
		
stop-postgres:
	-@docker ps | grep postgres | awk '{ print $$1 }' | xargs docker kill > /dev/null
	-@docker ps -a | grep postgres | awk '{ print $$1 }' | xargs docker rm > /dev/null

clean-images:
	-@docker images -q | xargs docker rmi

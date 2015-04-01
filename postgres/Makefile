DATABASE=postgres
PASSWORD=

all:
	@echo "make build_image -- create image with custom initialization scripts"
	@echo "make start-postgres -- start postgres database"
	@echo "make run-psql -- run database client psql"
	@echo "make stop-postgres -- stop postgres database"
	@echo "make clean-images -- cleans docker images"

build_image:
	@echo "+++ Starting image creation +++"
	@docker build -t ra1f/postgres .
	@docker images

start-postgres:
	@echo "+++ Starting Postgres +++"
	@export PGPASSWORD=$(PASSWORD)
	@docker run -p 5432:5432 --name some-postgres -e POSTGRES_PASSWORD=$(PASSWORD) -d ra1f/postgres:latest
	
run-psql:
	@echo "+++ Starting psql +++"
	-@docker run -it --link some-postgres:postgres --rm postgres sh -c 'exec psql -h "$$POSTGRES_PORT_5432_TCP_ADDR" -p "$$POSTGRES_PORT_5432_TCP_PORT" -U postgres $(DATABASE)'
		
stop-postgres:
	-@docker ps | grep postgres | awk '{ print $$1 }' | xargs docker kill > /dev/null
	-@docker ps -a | grep postgres | awk '{ print $$1 }' | xargs docker rm > /dev/null

clean-images:
	-@docker images -q | xargs docker rmi

.PHONY: init update-jwt-key start-database kill-existing-port run-app fill-tables clean-port clean

DATA_RELATION_BASE_URL ?= http://localhost:8080
YML_FILE ?= api-gateway/src/main/resources/application-local.yml

init: start-database kill-existing-port run-app fill-tables clean-port

update-jwt-key:
	@echo "Generating JWT Secret"
	@NEW_KEY=$$(openssl rand -base64 32) && \
	ESCAPED_NEW_KEY=$$(printf '%s' "$${NEW_KEY}" | sed 's/[&/\]/\\&/g') && \
	sed -i '' "s/^spring\.auth\.jwt\.key:.*/spring.auth.jwt.key: $$ESCAPED_NEW_KEY/" $(YML_FILE) && \
	echo "JWT secret updated successfully in $(YML_FILE)"

start-database:
	@echo "Starting Docker Compose for Postgres DB..."
	@docker-compose up -d > /dev/null 2>&1

kill-existing-port:
	@echo "Cleaning port 8080..."
	@PID=$(shell lsof -t -i:8080); \
	if [ -n "$$PID" ]; then \
		kill -9 $$PID; \
	fi

run-app:
	@echo "Building and starting application..."
	@./gradlew clean bootRun > /dev/null 2>&1 &

fill-tables:
	@sleep 5
	@echo "Generating auth token..."
	@TOKEN=$$(curl -s -X POST "$(DATA_RELATION_BASE_URL)/api/auth/login?username=admin&password=admin"); \
	echo "Dropping Existing Tables..."; \
	DELETE_RES=$$(curl -s -w "%{http_code}" -o /dev/null -X DELETE "$(DATA_RELATION_BASE_URL)/gateway/v1?credit=true&checking=true&dropTables=true" \
					-H "Authorization: Bearer $$TOKEN"); \
	echo "Response Code: $$DELETE_RES"; \
	echo "Creating New Tables..."; \
	CREATE_RES=$$(curl -s -w "%{http_code}" -o /dev/null -X POST "$(DATA_RELATION_BASE_URL)/gateway/v1" \
               		-H "Authorization: Bearer $$TOKEN"); \
    echo "Response Code: $$CREATE_RES"; \
    echo "Uploading Checking Data..."; \
    CHECKING_RES=$$(curl -s -w "%{http_code}" -o /dev/null -X POST "$(DATA_RELATION_BASE_URL)/gateway/v1" \
					-H "Authorization: Bearer $$TOKEN" \
					-F "file=@api-calls/insert/checking_records.csv"); \
	echo "Response Code: $$CHECKING_RES"; \
	echo "Adding pause to allow async call to complete..."; \
	sleep 2; \
	echo "Uploading Credit Data..."; \
	CREDIT_RES=$$(curl -s -w "%{http_code}" -o /dev/null -X POST "$(DATA_RELATION_BASE_URL)/gateway/v1" \
					-H "Authorization: Bearer $$TOKEN" \
					-F "file=@api-calls/insert/credit_records.csv"); \
	echo "Response Code: $$CREDIT_RES"; \
	echo "Data population complete. Shutting down the application..."

clean-port:
	@echo "Cleaning port 8080..."
	@PID=$(shell lsof -t -i:8080); \
	if [ -n "$$PID" ]; then \
		kill -9 $$PID; \
	fi

clean:
	@echo "\nKilling all docker containers..."
	docker stop $$(docker ps -aq) && docker rm $$(docker ps -aq) && docker system prune -a --volumes -f


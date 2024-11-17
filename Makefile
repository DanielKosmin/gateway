.PHONY: init update-jwt-key start-database kill-existing-port run-app fill-tables clean-port clean

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
	@chmod +x ./scripts/fill-tables
	@echo "Please enter your API key:" && read API_KEY && ./scripts/fill-tables $$API_KEY

clean-port:
	@echo "Cleaning port 8080..."
	@PID=$(shell lsof -t -i:8080); \
	if [ -n "$$PID" ]; then \
		kill -9 $$PID; \
	fi

clean:
	@echo "\nKilling all docker containers..."
	docker stop $$(docker ps -aq) && docker rm $$(docker ps -aq) && docker system prune -a --volumes -f


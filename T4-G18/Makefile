APP_NAME=$(shell basename $(CURDIR))
GIT_COMMIT=$(shell git rev-parse --short HEAD)

CONFIG=$(CURDIR)/config.json

OUT_DIR=build

.PHONY: build run dev

## build: builds the application in "build" directory
build: clean
	@CGO_ENABLED=0 go build -o $(OUT_DIR)/ -ldflags "-s -w -X main.gitCommit=$(GIT_COMMIT)" ./...

## run: runs the application in "build/game-repository"
run: build
	@./$(OUT_DIR)/$(APP_NAME) --config=$(CONFIG)

## dev: executes the application with hot reload
dev: 
	air

## dev-dependecies: installs development dependencies
dev-dependecies:
	@go install github.com/cosmtrek/air@latest

## docker-build: builds a docker image
docker-build:
	docker build -t $(APP_NAME):$(GIT_COMMIT) .

## docker-run: runs a docker container. Needs "config" argument (i.e make docker-run config=$(pwd)/config.json)
docker-run: docker-build
	docker run --network=host -v $(CONFIG):/app/config.json $(APP_NAME):$(GIT_COMMIT)

## docker-push-ssh: sends the image on a server with ssh (i.e make docker-push-ssh SSH="10.10.1.1 -p1234")
docker-push-ssh: docker-build
	docker save $(APP_NAME):$(GIT_COMMIT) | bzip2 | pv | ssh $(SSH) docker load

## docker-push: sends the image on a registry (i.e make docker-push REGISTRY=<registry_name>)
docker-push: docker-build
	docker tag $(APP_NAME):$(GIT_COMMIT) $(REGISTRY)/$(APP_NAME):$(GIT_COMMIT)
	docker push $(REGISTRY)/$(APP_NAME):$(GIT_COMMIT)
	docker tag $(REGISTRY)/$(APP_NAME):$(GIT_COMMIT) $(REGISTRY)/$(APP_NAME):latest
	docker push $(REGISTRY)/$(APP_NAME):latest

## test: executes all unit tests in the repository. Use COVER_DIR=<PATH> to enable coverage. (i.e make test COVER_DIR=$(pwd)/coverage)
test:
ifeq ($(COVER_DIR),)
	CGO_ENABLED=0 SKIP_INTEGRATION=1 go test ./...
else
	CGO_ENABLED=0 SKIP_INTEGRATION=1 go test -v -cover ./... -args -test.gocoverdir=$(COVER_DIR)
	@go tool covdata percent -i=$(COVER_DIR)/ -o $(COVER_DIR)/profile
	go tool cover -func $(COVER_DIR)/profile
endif

## test-race: executes all unit tests with a race detector. Takes longer
test-race:
	@go test -race ./...

## test-integration: executes all tests. If CI is set, DB_URI can be used to set database URL, otherwis a docker container is used (i.e make test-integration CI=1 DB_URI=db-url COVER_DIR=/some/path)
test-integration:
	@mkdir -p $(COVER_DIR)
ifeq ($(CI),)
	$(info Running integration test with a local docker container)
	@ ID=$$(docker run -p 5432 -e POSTGRES_PASSWORD=postgres --rm -d postgres:14-alpine3.17); \
	PORT=$$(docker port $$ID | awk '{split($$0,a,":"); print a[2]}' ); \
	sleep 5; \
	DB_URI="postgresql://postgres:postgres@localhost:$$PORT/postgres?sslmode=disable" CGO_ENABLED=0  go test -cover  ./...  -args -test.gocoverdir=$(COVER_DIR) -- ; \
	docker kill $$ID
else
	$(info Running integration test on $(DB_URI))
	@DB_URI=$(DB_URI) CGO_ENABLED=0 go test -cover  ./...  -args -test.gocoverdir=$(COVER_DIR)
endif
	@go tool covdata percent -i=$(COVER_DIR)/ -o $(COVER_DIR)/profile
	go tool cover -func $(COVER_DIR)/profile -o=coverage.out

## clean: remove build files 
clean:
	@rm -f build/*

.PHONY: help
## help: prints this help message
help:
	@echo "Usage: \n"
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'


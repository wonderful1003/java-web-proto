# java-web-proto

Minimal Spring Boot web app ready for Cloud Run.

## Run locally (needs JDK 17 + Maven)
```
mvn -DskipTests package
java -jar target/java-web-proto-0.0.1-SNAPSHOT.jar
```
Open http://localhost:8080

## Deploy to Cloud Run (no local Docker needed)
```
gcloud init
gcloud config set project <YOUR_PROJECT_ID>
gcloud services enable run.googleapis.com artifactregistry.googleapis.com cloudbuild.googleapis.com

REGION=asia-northeast3   # Seoul
REPO=demo-repo
gcloud artifacts repositories create %REPO% --repository-format=docker --location=%REGION% || echo repo may exist

IMAGE="%REGION%-docker.pkg.dev/<YOUR_PROJECT_ID>/%REPO%/java-web-proto:1.0.0"
gcloud builds submit --tag "$IMAGE"
gcloud run deploy java-web-proto --image "$IMAGE" --region $REGION --allow-unauthenticated --memory 512Mi
```
The terminal prints the service URL. Open it in your browser.

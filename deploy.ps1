# Build and package the app with Maven
mvn clean package docker:build

# Tag the Docker image
docker tag c2-server:latest gcr.io/personal-projects-416300/c2-server:latest

# Push the Docker image to Google Container Registry (GCR)
docker push gcr.io/personal-projects-416300/c2-server:latest

# Deploy the app to Cloud Run
gcloud run deploy c2-server `
  --image gcr.io/personal-projects-416300/c2-server:latest `
  --platform managed `
  --region us-central1 `
  --allow-unauthenticated `
  --cpu 1 `
  --memory 2Gi `
  --port=8070
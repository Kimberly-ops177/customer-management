#!/bin/bash
# deploy.sh - Deploy customer-management to Kubernetes

echo "🚀 Deploying Customer Management System to Kubernetes..."

echo "📁 Creating namespace..."
kubectl apply -f k8s/namespace.yaml

echo "⚙️ Applying ConfigMap..."
kubectl apply -f k8s/configmap.yaml

echo "🔐 Applying Secrets..."
kubectl apply -f k8s/secret.yaml

echo "🐘 Deploying PostgreSQL..."
kubectl apply -f k8s/postgres.yaml

echo "⏳ Waiting for PostgreSQL to be ready..."
kubectl wait --for=condition=ready pod -l app=postgres -n customer-management --timeout=120s

echo "🌐 Deploying application..."
kubectl apply -f k8s/deployment.yaml

echo "🔌 Applying Service..."
kubectl apply -f k8s/service.yaml

echo "📈 Applying HPA..."
kubectl apply -f k8s/hpa.yaml

echo ""
echo "✅ Deployment complete!"
echo ""
echo "📊 Checking status..."
kubectl get all -n customer-management

echo ""
echo "🌍 App available at: http://localhost:30080"
echo "📖 Swagger UI: http://localhost:30080/swagger-ui.html"

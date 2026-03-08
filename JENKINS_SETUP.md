# Jenkins CI/CD Setup Guide

## Step 1 — Install Jenkins with Docker

Run this in PowerShell (Docker Desktop must be running):

```powershell
docker run -d `
  --name jenkins `
  -p 8090:8080 `
  -p 50000:50000 `
  -v jenkins_home:/var/jenkins_home `
  -v /var/run/docker.sock:/var/run/docker.sock `
  jenkins/jenkins:lts
```

Wait 30 seconds, then open: http://localhost:8090

## Step 2 — Unlock Jenkins

Get the initial admin password:
```powershell
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```
Copy the password → paste it in the browser → click Continue.

## Step 3 — Install Plugins

- Click **Install suggested plugins** and wait
- After install, create your admin user
- Then go to: **Manage Jenkins → Plugins → Available**
- Search and install these additional plugins:
  - `HTML Publisher` (for coverage reports)
  - `Docker Pipeline`
  - `GitHub Integration`

## Step 4 — Configure Tools

Go to **Manage Jenkins → Tools**:

### JDK
- Click **Add JDK**
- Name: `JDK17`
- Uncheck "Install automatically"
- JAVA_HOME: `D:\Users\USER\AppData\Local\Programs\Microsoft\jdk-17.0.18.8-hotspot`

### Gradle
- Click **Add Gradle**
- Name: `Gradle-9`
- Uncheck "Install automatically"  
- GRADLE_HOME: (run `where gradle` in PowerShell to find it)

## Step 5 — Add DockerHub Credentials

Go to **Manage Jenkins → Credentials → Global → Add Credentials**:
- Kind: `Username with password`
- Username: `kimberlyops`
- Password: your DockerHub password
- ID: `dockerhub-credentials` ← must match exactly
- Click Save

## Step 6 — Add the Jenkinsfile to your repo

Copy the Jenkinsfile to your project root:
```powershell
cd "C:\Users\USER\Documents\Java App with Gradle\customer-management"
# Copy the Jenkinsfile here, then:
git add Jenkinsfile
git commit -m "ci: add Jenkins pipeline"
git push
```

## Step 7 — Create Pipeline Job in Jenkins

1. Click **New Item**
2. Name: `customer-management-pipeline`
3. Select **Pipeline** → Click OK
4. Under **Pipeline**:
   - Definition: `Pipeline script from SCM`
   - SCM: `Git`
   - Repository URL: `https://github.com/Kimberly-ops177/customer-management`
   - Branch: `*/main`
   - Script Path: `Jenkinsfile`
5. Click **Save**

## Step 8 — Run the Pipeline

Click **Build Now** — watch all 7 stages run green! 🟢

Your Docker image will appear at:
https://hub.docker.com/r/kimberlyops/customer-management

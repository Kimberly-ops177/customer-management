stage('Code Coverage') {
    steps {
        echo '📈 Generating JaCoCo coverage report...'
        sh './gradlew jacocoTestReport --no-daemon'
        echo '✅ Coverage report generated at build/reports/jacoco/test/html/index.html'
    }
}
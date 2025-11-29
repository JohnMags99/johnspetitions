pipeline {
	agent any
	stages {
		stage('Checkout') {
			steps {
				checkout scm
			}
		}

		stage('Build') {
			steps {
				sh 'mvn -v'
				sh 'mvn clean compile'
			}
		}
	}
}



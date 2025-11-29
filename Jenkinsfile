pipeline {
	agent any
	environment {
		APP_NAME = 'johnspetitions'
		WAR_NAME = 'johnspetitions.war'
		EC2_HOST = 'ec2-51-21-220-35.eu-north-1.compute.amazonaws.com'
		EC2_USER = 'admin'
		TOMCAT_WEBAPPS = '/opt/tomcat/webapps'
		SSH_KEY_CREDENTIALS_ID = 'ec2-ssh-key'
	}
	stages {
		stage('Checkout') {
			steps {
				checkout scm
			}
		}
		stage('Build') {
			steps {
				ansiColor('xterm') {
					sh 'mvn -B -ntp clean compile'
				}
			}
		}
		stage('Test') {
			steps {
				ansiColor('xterm') {
					sh 'mvn -B -ntp test'
				}
			}
			post {
				always {
					junit 'target/surefire-reports/*.xml'
				}
			}
		}
		stage('Package') {
			steps {
				ansiColor('xterm') {
					sh 'mvn -B -ntp package'
				}
			}
			post {
				success {
					archiveArtifacts artifacts: "target/${WAR_NAME}", fingerprint: true
				}
			}
		}
		stage('Approval') {
			steps {
				input message: "Deploy ${WAR_NAME} to EC2 Tomcat?", ok: "Deploy"
			}
		}
		stage('Deploy') {
			steps {
				withCredentials([sshUserPrivateKey(credentialsId: env.SSH_KEY_CREDENTIALS_ID,
					keyFileVariable: 'SSH_KEY',
					usernameVariable: 'SSH_USER')]) {
					ansiColor('xterm') {
						sh """
                            scp -i $SSH_KEY target/${WAR_NAME} $SSH_USER@${EC2_HOST}:${TOMCAT_WEBAPPS}/${WAR_NAME}
                            ssh -i $SSH_KEY $SSH_USER@${EC2_HOST} 'sudo systemctl restart tomcat || sudo service tomcat restart'
                        """
					}
				}
			}
		}
	}
	triggers {
		githubPush()
	}
	options {
		timestamps()
	}
}



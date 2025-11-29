pipeline {
	agent any
	environment {
		APP_NAME = 'johnspetitions'
		WAR_NAME = 'johnspetitions.war'
		EC2_HOST = 'ec2-51-21-220-35.eu-north-1.compute.amazonaws.com'       // replace with your EC2 public IP or hostname
		EC2_USER = 'admin'              // or 'ubuntu' depending on your AMI
		TOMCAT_WEBAPPS = '/opt/tomcat/webapps'
		SSH_KEY_CREDENTIALS_ID = 'ec2-ssh-key' // ID of the SSH key you added in Jenkins
	}
	stages {
		stage('Checkout') {
			steps {
				checkout scm
			}
		}
		stage('Build') {
			steps {
				sh 'mvn -B -ntp clean compile'
			}
		}
		stage('Test') {
			steps {
				sh 'mvn -B -ntp test'
			}
			post {
				always {
					junit 'target/surefire-reports/*.xml'
				}
			}
		}
		stage('Package') {
			steps {
				sh 'mvn -B -ntp package'
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
					sh """
                        scp -i $SSH_KEY target/${WAR_NAME} $SSH_USER@${EC2_HOST}:${TOMCAT_WEBAPPS}/${WAR_NAME}
                        ssh -i $SSH_KEY $SSH_USER@${EC2_HOST} 'sudo systemctl restart tomcat || sudo service tomcat restart'
                    """
				}
			}
		}
	}
	triggers {
		githubPush() // webhook triggers pipeline on every push
	}
	options {
		timestamps()
		ansiColor('xterm')
	}
}


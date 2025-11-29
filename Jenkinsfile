pipeline {
	agent any
	environment {
		APP_NAME = 'johnspetitions'
		WAR_NAME = 'johnspetitions.war'
		EC2_HOST = 'ec2-13-62-168-70.eu-north-1.compute.amazonaws.com'       // replace with your EC2 public IP or hostname
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
		stage('Pre-clean') {
			steps {
				sh 'rm -rf target || true'
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
				withCredentials([sshUserPrivateKey(credentialsId: SSH_KEY_CREDENTIALS_ID,
					keyFileVariable: 'SSH_KEY',
					usernameVariable: 'SSH_USER')]) {
					sh """
                		# Upload WAR to home directory
                		scp -o StrictHostKeyChecking=no -i $SSH_KEY target/johnspetitions.war \
                    	$SSH_USER@ec2-13-62-168-70.eu-north-1.compute.amazonaws.com:/home/$SSH_USER/

                		# Move WAR into Tomcat webapps and restart Tomcat
                		ssh -o StrictHostKeyChecking=no -i $SSH_KEY $SSH_USER@ec2-13-62-168-70.eu-north-1.compute.amazonaws.com \
                    	'sudo mv /home/$SSH_USER/johnspetitions.war /opt/tomcat/webapps/ && sudo systemctl restart tomcat'
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
	}
}


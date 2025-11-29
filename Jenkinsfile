pipeline {
	agent any
	environment {
		APP_NAME = 'johnspetitions'
		WAR_NAME = 'johnspetitions.war'
		EC2_HOST = 'ec2-13-62-168-70.eu-north-1.compute.amazonaws.com'
		EC2_USER = 'admin'
		TOMCAT_WEBAPPS = '/opt/tomcat/latest/webapps'
		SSH_KEY_CREDENTIALS_ID = 'ec2-ssh-key'
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
		stage('Deploy') {
			steps {
				withCredentials([sshUserPrivateKey(credentialsId: SSH_KEY_CREDENTIALS_ID,
					keyFileVariable: 'SSH_KEY',
					usernameVariable: 'SSH_USER')]) {
					sh """
                		# Upload WAR to home directory
                		scp -o StrictHostKeyChecking=no -i $SSH_KEY target/$WAR_NAME \
                    	$SSH_USER@$EC2_HOST:/home/$SSH_USER/

                		# SSH into EC2, rebuild Docker image and restart container
                		ssh -o StrictHostKeyChecking=no -i $SSH_KEY $SSH_USER@$EC2_HOST \
                		cd /home/$SSH_USER && \
						docker rm -f $APP_NAME || true && \
                     	docker build -t $APP_NAME-tomcat . && \
                     	docker run -d --name $APP_NAME -p 9090:8080 $APP_NAME-tomcat
            		"""
				}
			}
		}
	}
	post {
		always {
			cleanWs()   // cleans up workspace after pipeline run
		}
	}
	triggers {
		githubPush() // webhook triggers pipeline on every push
	}
	options {
		timestamps()
	}
}


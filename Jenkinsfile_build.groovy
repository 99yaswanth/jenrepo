pipeline{
    agent any
    parameters{
        string(name: 'branch', defaultValue: '', description: '')
        string(name: 'buildno', defaultValue: '', description: '')
    }
    stages{
        stage("clone a code"){
            steps{
                git branch: "${branch}",
                url: 'https://github.com/99yaswanth/boxfuse-sample-java-war-hello.git'
            }

        }
        stage("build"){
            steps{
                sh "mvn clean package"
            }
        }
        stage("upload to s3"){
            steps{
                sh "aws s3 cp target/hello-${buildno}.war s3://yashwanth12/application/${buildno}/"
            }
        }
        stage("deploy"){
            steps{
                sh "scp -o StrictHostKeyChecking=no /tmp/yashnv.pem target/hello-${buildno}.war ec2-user@3.88.166.114:/tmp"
                sh "ssh -o StrictHostKeyChecking=no /tmp/yashnv.pem ec2-user@3.88.166.114 \"sudo cp /tmp/hello-${buildno} /var/lib/tomcat/webapps\""
            }
        }
    }
}
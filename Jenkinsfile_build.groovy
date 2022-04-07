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
    }
}
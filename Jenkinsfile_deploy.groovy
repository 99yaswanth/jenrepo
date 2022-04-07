pipeline{
    agent any
    parameters{
        string(name: 'branch', defaultValue: '', description: '')
        string(name: 'buildno', defaultValue: '', description: '')
        string(name: 'serverip', defaultValue: '', description: '')

    }
    stages{
        stage("deploy to multiple servers"){
            steps{
                sh '''
                aws s3 cp s3://yashwanth12/application/${buildno}/hello-${buildno}.war .
                IFS=',' read -ra outputArray <<< "${serverip}"
                for ip in \"${outputArray[@]}\"
                do
                echo $ip
                scp -o StrictHostkeyChecking=no -i /tmp/yashnv.pem hello-${buildno}.war ec2-user@$ip:/tmp
                ssh -i /tmp/yashnv.pem ec2-user@$ip "sudo cp /tmp/hello-${buildno}.war /var/lib/tomcat/webapps"
                done
                '''

            }
        }
    }
    
    
}
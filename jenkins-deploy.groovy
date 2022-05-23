pipeline{
    agent any
    parameters{
            string(name: 'branch',defaultValue :'',description:'')
            string(name: 'buildno',defaultValue:'',description:'')
            string(name: 'serverip',defaultValue:'',description:'')
        }
    stages{
        stage('download artifacts'){
            steps{
                println"here i'm download artifacts from s3"
                sh """
                aws s3 cp s3://alankruthiart/application4/${buildno}/hello-${buildno}.war .
                """
            }
        }
        stage("copy artifacts"){
            steps{
                println "here i'm copying artifacts from jenkins to tomcat servers"
                sh "scp -o StrictHostKeyChecking=no -i /tmp/alankruthi21.pem hello-${buildno}.war ec2-user@${serverip}:/tmp"
                sh "ssh -o StrictHostKeyChecking=no -i /tmp/alankruthi21.pem ec2-user@${serverip} \"sudo cp /tmp/hello-${buildno}.war /var/lib/tomcat/webapps\""
            }
        }
    }
}
def buildVersion(){
    echo "this method creates a new version"
    sh 'mvn build-helper:parse-version versions:set \
     -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} versions:commit'
    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    def version = matcher[0][1]
    env.IMAGE_NAME = "$version"
    echo "$IMAGE_NAME"

}

def buildJar(){
    echo "this is build stage"
    sh "mvn clean package"
}

/*def buildAndPushImage(){
    echo "this is push stage"
    withCredentials([usernamePassword('credentialsId':'nexus-repo-credentials','usernameVariable':'USER','passwordVariable':'PASS')]){
        sh "docker build -t 143.198.43.144:8083/java-maven-app:${IMAGE_NAME} ."
        sh "echo ${PASS} | docker login -u ${USER} --password-stdin 143.198.43.144:8083"
        sh "docker push 143.198.43.144:8083/java-maven-app:${IMAGE_NAME}"
    }
}*/

def buildAndPushImage(){
    echo "this is push stage"
    withCredentials([usernamePassword('credentialsId':'aws-ecr-credentials','usernameVariable':'USER','passwordVariable':'PASS')]){
        sh "docker build -t ${DOCKER_REPO}:${IMAGE_NAME} ."
        sh "echo ${PASS} | docker login -u ${USER} --password-stdin ${DOCKER_REPO_SERVER}"
        sh "docker push ${DOCKER_REPO}:${IMAGE_NAME}"
    }
}

def commitVersionToGitRepo(){
    withCredentials([usernamePassword('credentialsId': 'git_hub_credentials', 'usernameVariable': 'USER', 'passwordVariable': 'PASS')]){
        sh 'git config user.email "jenkins@example.com"'
        sh 'git config user.name "jenkins"'

        sh "git remote set-url origin https://${USER}:${PASS}@github.com/ShekarSoma4933/java-maven-app.git"
        sh "git add ."
        sh 'git commit -m "ci: version bump"'
        sh 'git push origin HEAD:master'
    }

}

def deployJar(){
    echo "Deploying the Jar to server"
    sh 'envsubst < Kubernetes/deployment.yaml | kubectl apply -f -'
    sh 'envsubst < Kubernetes/service.yaml | kubectl apply -f -'
}
/*def deployJar(){
    echo "Deploying the Jar to server"
    container = "docker run -d -p 8080:8080 185140774664.dkr.ecr.us-east-1.amazonaws.com/java-maven-app:${IMAGE_NAME}"
    sshagent(['docker-global-ec2-user']) {
        sh "ssh -o StrictHostKeyChecking=no ec2-user@18.234.80.161 ${container}"
    }
}*/



return this

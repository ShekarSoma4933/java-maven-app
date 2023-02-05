def gv
pipeline {
  agent any
  tools{
      maven 'maven-3.6'
  }
  environment{
    AWS_ACCESS_KEY_ID = credentials('AWS-access-key-id')
    AWS_ACCESS_KEY_SECRET = credentials('AWS-access-key-secret')
  }
  stages {
      stage('init') {
         steps {
            script{
               gv = load "script.groovy"
            }
         }
      }
      stage('Build Version') {
                     steps {
                        script{
                           gv.buildVersion()
                        }
                     }
                  }
      stage('Build Jar') {
               steps {
                  script{
                     gv.buildJar()
                  }
               }
            }
      stage('Build and Push Image') {
              steps {
                  script{
                    gv.buildAndPushImage()
                  }
              }
            }
      stage('commit version to git'){
           steps{
                script{
                gv.commitVersionToGitRepo()
                }
           }
      }
      stage('deploy'){
        steps{
            script{
            sh "kubectl create deployment nginx-deployment --image=nginx"
              //gv.deployJar()
            }
        }
      }
  }
}
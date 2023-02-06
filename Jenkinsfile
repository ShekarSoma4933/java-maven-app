#!/usr/bin/env groovy

def gv
pipeline {
  agent any
  tools{
      maven 'maven-3.6'
  }
  environment{
    APP_NAME = "java-maven-app"
    AWS_ACCESS_KEY_ID = credentials('AWS-access-key-id')
    AWS_ACCESS_KEY_SECRET = credentials('AWS-access-key-secret')
    DOCKER_REPO_SERVER = '185140774664.dkr.ecr.us-east-1.amazonaws.com'
    DOCKER_REPO = "${DOCKER_REPO_SERVER}/${APP_NAME}"
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
              gv.deployJar()
            }
        }
      }
  }
}
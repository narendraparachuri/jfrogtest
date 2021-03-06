node {

    stage('Clean workspace') {
        sh "rm -rf ./*"
    }

    stage('Clone repository') {
        git url: 'https://github.com/narendraparachuri/jfrogtest.git'
    }

    dir('node-version') {
        stage('Download build tools') {
            sh "curl -fL https://getcli.jfrog.io | sh"
            sh "chmod +x jfrog"

        }

        stage('Configure Artifactory') {
            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: ARTIFACTORY_CREDENTIALS, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                sh "./jfrog rt config --url $ARTIFACTORY_URL --user $USERNAME --password $PASSWORD"
            }
        }

        stage('Build Node App') {
            sh "./jfrog rt npm-install ${NPM_REPO} --build-name=${env.JOB_NAME} --build-number=${env.BUILD_NUMBER}"
        }

        stage('Publish Node App to Artifactory') {
            sh "./jfrog rt npm-publish ${NPM_REPO} --build-name=${env.JOB_NAME} --build-number=${env.BUILD_NUMBER}"

        }
    }

    stage('Publish Build Info') {
        sh "./node-version/jfrog rt bag ${env.JOB_NAME} ${env.BUILD_NUMBER}"
        sh "./node-version/jfrog rt bce ${env.JOB_NAME} ${env.BUILD_NUMBER}"
        sh "./node-version/jfrog rt bp ${env.JOB_NAME} ${env.BUILD_NUMBER}"
    }

    stage('Xray Scan') {
        def rtServer = Artifactory.newServer url: ARTIFACTORY_URL, credentialsId: ARTIFACTORY_CREDENTIALS
        if (XRAY_SCAN == "YES") {
             def xrayConfig = [
                'buildName'     : env.JOB_NAME,
                'buildNumber'   : env.BUILD_NUMBER,
                'failBuild'     : (FAIL_BUILD == "YES")
              ]
              def xrayResults = rtServer.xrayScan xrayConfig
              echo xrayResults as String
         } else {
              println "No Xray scan performed. To enable set XRAY_SCAN = YES"
         }
    }

}

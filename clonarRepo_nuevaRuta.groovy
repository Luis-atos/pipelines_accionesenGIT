pipeline {
    agent any

    environment {
        GIT_OLD_REPO = "git@git.servdev.mdef.es:administracion/portales/portales-intradef/xip/intradef_xip_front.git"
        GIT_NEW_REPO = "git@git.servdev.mdef.es:administracion/portales/portales-intradef/intradef/intradef_xip_front.git"
        GIT_CREDENTIALS = 'jenkins_Git_SSH_Access' // Asegúrate de que las credenciales sean correctas
        pathWS = 'workspace' // Directorio de trabajo
    }

    stages {
        stage('Checkout desde el repositorio antiguo') {
            steps {
                script {
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: "master"]],
                        doGenerateSubmoduleConfigurations: false,
                        extensions: [[
                            $class: 'RelativeTargetDirectory',
                            relativeTargetDir: "${pathWS}"
                        ]],
                        submoduleCfg: [],
                        userRemoteConfigs: [[
                            credentialsId: GIT_CREDENTIALS,
                            url: GIT_OLD_REPO
                        ]]
                    ])
                }
            }
        }

        stage('Subir código al nuevo repositorio') {
            steps {
                script {
                    dir("${pathWS}") {
                sh """
                    git remote remove origin || true
                    git remote add origin ${GIT_NEW_REPO}
                    git checkout master || git checkout -b master
                    git branch -M main
                    git push --force origin main
                """
                    }
                }
            }
        }
    }
}

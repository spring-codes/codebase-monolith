package com.cheroliv.codebase.monolith

interface ProfileConstants {
    companion object {
        // Spring profiles for development, test and production, see https://www.jhipster.tech/profiles/
        /** Constant `SPRING_PROFILE_DEVELOPMENT="dev"`  */
        const val SPRING_PROFILE_DEVELOPMENT = "dev"

        /** Constant `SPRING_PROFILE_TEST="test"`  */
        const val SPRING_PROFILE_TEST = "test"

        /** Constant `SPRING_PROFILE_PRODUCTION="prod"`  */
        const val SPRING_PROFILE_PRODUCTION = "prod"

        /** Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
         * Constant `SPRING_PROFILE_CLOUD="cloud"`  */
        const val SPRING_PROFILE_CLOUD = "cloud"

        /** Spring profile used when deploying to Heroku
         * Constant `SPRING_PROFILE_HEROKU="heroku"`  */
        const val SPRING_PROFILE_HEROKU = "heroku"

        /** Spring profile used when deploying to Amazon ECS
         * Constant `SPRING_PROFILE_AWS_ECS="aws-ecs"`  */
        const val SPRING_PROFILE_AWS_ECS = "aws-ecs"

        /** Spring profile used when deploying to Microsoft Azure
         * Constant `SPRING_PROFILE_AZURE="azure"`  */
        const val SPRING_PROFILE_AZURE = "azure"

        /** Spring profile used to enable swagger
         * Constant `SPRING_PROFILE_SWAGGER="swagger"`  */
        const val SPRING_PROFILE_SWAGGER = "swagger"

        /** Spring profile used to disable running liquibase
         * Constant `SPRING_PROFILE_NO_LIQUIBASE="no-liquibase"`  */
        const val SPRING_PROFILE_NO_LIQUIBASE = "no-liquibase"

        /** Spring profile used when deploying to Kubernetes and OpenShift
         * Constant `SPRING_PROFILE_K8S="k8s"`  */
        const val SPRING_PROFILE_K8S = "k8s"
    }
}

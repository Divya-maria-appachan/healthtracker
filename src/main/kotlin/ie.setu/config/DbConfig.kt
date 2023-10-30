package ie.setu.config

import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.name

class DbConfig{

    private val logger = KotlinLogging.logger {}

    fun getDbConnection() :Database{

        logger.info{"Starting DB Connection..."}

        val PGHOST = "kandula.db.elephantsql.com"
        val PGPORT = "5432"
        val PGUSER = "phalnxge"
        val PGPASSWORD = "j8KL1HquxJTkDtmstVzMWcz-kSQWAOLJ"
        val PGDATABASE = "phalnxge"

        //url format should be jdbc:postgresql://host:port/database
        val dbUrl = "jdbc:postgresql://$PGHOST:$PGPORT/$PGDATABASE"

        val dbConfig = Database.connect(
            url = dbUrl,
            driver="org.postgresql.Driver",
            user = PGUSER,
            password = PGPASSWORD
        )
        logger.info{"DbConfig name = " + dbConfig.name}
        logger.info{"DbConfig url = " + dbConfig.url}

        return dbConfig
    }

}
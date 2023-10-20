package ie.setu.config

import org.jetbrains.exposed.sql.Database

class DbConfig{

    fun getDbConnection() :Database{

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

        return dbConfig
    }

}
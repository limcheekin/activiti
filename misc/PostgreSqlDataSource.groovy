dataSource {
	pooled = true
	driverClassName = "org.postgresql.Driver"
	username = "${user}"
	password = "${password}"
}

hibernate {
	cache.use_second_level_cache = true
	cache.use_query_cache = true
	cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}

// environment specific settings
environments {
	development {
		dataSource {
			dbCreate = "update" // one of 'create', 'create-drop','update'
			url = "jdbc:postgresql:activiti"
		}
	}
	
	test {
		dataSource {
			dbCreate = "create-drop"          
			url = "jdbc:postgresql:activiti"
		}
	}
	
	production {
		dataSource {
			dbCreate = "update"
			url = "jdbc:postgresql:activiti"
		}
	}
}
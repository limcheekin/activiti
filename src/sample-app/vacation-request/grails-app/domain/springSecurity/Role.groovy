@package@

class Role implements org.activiti.engine.identity.Group {
	String id
	String name
	String authority
	String type
	
	static mapping = {
		cache true
		id generator: 'assigned'
	}

	static constraints = {
		authority blank: false, unique: true
		name blank: false
		type nullable: true
	}
}

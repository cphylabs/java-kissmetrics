jar:
	mvn package -DskipTests
	
test:
	mvn test -DargLine="-DKISS_API=$(KISS_API)"
	
depcopy:
	mvn dependency:copy-dependencies -DskipTests
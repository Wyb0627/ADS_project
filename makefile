JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        building_record_rbt.java \
        building_record.java \
        function_rbt.java \
        function1.java \
	risingCity.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
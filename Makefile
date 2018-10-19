JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
			CRUD.java \
			DirectFile.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	del *.class *~


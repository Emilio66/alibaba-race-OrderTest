#get the newest code jar
cd ~/singularity/Order
git pull
mvn package

#compile all java files
PROJECT=/home/hadoop/singularity/OrderTest
JAR=$PROJECT/order-system-impl-1.0.jar
SRC=$PROJECT/src
BIN=$PROJECT/bin

cp -f target/order-system-impl-1.0.jar $JAR
cd $PROJECT

find $SRC -name *.java > $SRC/sources.list

#remove compiled classes file
rm -rf $BIN/*

#compile the project's source files to $BIN
javac -d $BIN/ -cp $JAR @$SRC/sources.list

#test parsing case class
#java -cp $BIN:$JAR Parser /home/hadoop/tb/prerun_data/case.0 > screen.out
#run project as a background process
#nohup java -cp $BIN:$JAR Tester /home/hadoop/tb/prerun_data/case.0 &
java -cp $BIN:$JAR Tester /home/hadoop/tb/prerun_data/case.0 265 > std.out
echo "program running log is writting into std.out ..."

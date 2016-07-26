#get newest code jar
cd ~/singularity/Order
git pull
mvn package
cp -f target/order-system-impl-1.0.jar ~/singularity/OrderTest/order-system-impl-1.0.jar
cd ~/singularity/OrderTest/src/
javac -cp ~/singularity/OrderTest/order-system-impl-1.0.jar Tester.java
#run Tester, remember add . to the classpath, otherwise main class will not be found
java -cp ~/singularity/OrderTest/order-system-impl-1.0.jar:. Tester > screen.out
